package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.rental.RentalApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalApi;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalClock;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalClockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.application.RentalBillingApplicationService;
import java.util.UUID;

@IntegrationTest
@AutoConfigureMockMvc
@Import(RentalClockConfiguration.class)
class CounterResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private RentalClock clock;

  @Autowired
  private RentalBillingApplicationService billing;

  private RentalApi api;

  @BeforeEach
  void loadApi() {
    api = new RentalApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldPickUpVehicleOfBookedCategory() throws Exception {
    api.vehicle("ECO", "PART_DIEU");
    var email = uniqueEmail();
    var reservation = api.reservation(email, "ECO", 3);
    clock.at(PICK_UP);

    var contract = api
      .pickUp(reservation)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.reservationId").value(reservation))
      .andExpect(jsonPath("$.customer").value(email))
      .andExpect(jsonPath("$.bookedCategory").value("ECO"))
      .andExpect(jsonPath("$.vehicleCategory").value("ECO"))
      .andExpect(jsonPath("$.deposit").value(800))
      .andExpect(jsonPath("$.status").value("OPEN"));

    api.get("/api/rental/reservations/" + reservation).andExpect(jsonPath("$.status").value("PICKED_UP"));
    api.get("/api/rental/fleet/vehicles/" + read(contract, "$.plate")).andExpect(jsonPath("$.status").value("RENTED"));
    api.get("/api/rental/billing/deposits/" + read(contract, "$.id")).andExpect(jsonPath("$.status").value("HELD")).andExpect(jsonPath("$.amount").value(800));
    api.pickUp(reservation).andExpect(status().isConflict());
  }

  @Test
  void shouldSettleOnTimeReturnAndCreditPoints() throws Exception {
    api.vehicle("ECO", "PART_DIEU");
    var email = uniqueEmail();
    var reservation = api.reservation(email, "ECO", 3);
    clock.at(PICK_UP);
    var contract = api.contract(reservation);
    clock.at(PICK_UP.plusDays(3));

    var returned = api.giveBack(contract, "PART_DIEU", 300, "0", null).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CLOSED"));

    api
      .get("/api/rental/billing/invoices/" + read(returned, "$.invoiceId"))
      .andExpect(jsonPath("$.total").value(0))
      .andExpect(jsonPath("$.status").value("PAID"))
      .andExpect(jsonPath("$.charges.length()").value(0));
    api.get("/api/rental/loyalty/" + email).andExpect(jsonPath("$.balance").value(105));
    clock.at(PICK_UP.plusDays(10));
    billing.releaseDeposits();
    api.get("/api/rental/billing/deposits/" + contract).andExpect(jsonPath("$.status").value("RELEASED")).andExpect(jsonPath("$.released").value(800));
  }

  @Test
  void shouldBillLateOneWayReturnWithExtraKilometresAndFuel() throws Exception {
    api.vehicle("ECO", "PART_DIEU");
    var email = uniqueEmail();
    var reservation = api.reservation(email, "ECO", 3);
    clock.at(PICK_UP);
    var contract = api.contract(reservation);
    clock.at(PICK_UP.plusDays(3).plusMinutes(70));

    var returned = api.giveBack(contract, "SAINT_EXUPERY", 641, "10", null);

    var invoice = read(returned, "$.invoiceId");
    api
      .get("/api/rental/billing/invoices/" + invoice)
      .andExpect(jsonPath("$.total").value(115.25))
      .andExpect(jsonPath("$.status").value("ISSUED"))
      .andExpect(jsonPath("$.charges[?(@.kind == 'LATE_HOURS')].amount").value(contains(30)));
    api.get("/api/rental/loyalty/" + email).andExpect(jsonPath("$.balance").value(0));
    api.postJson("/api/rental/billing/invoices/" + invoice + "/payment", "{}").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PAID"));
    api.postJson("/api/rental/billing/invoices/" + invoice + "/payment", "{}").andExpect(status().isConflict());
    api.get("/api/rental/loyalty/" + email).andExpect(jsonPath("$.balance").value(195));
    api
      .get("/api/rental/fleet/vehicles/" + read(api.get("/api/rental/counter/contracts/" + contract), "$.plate"))
      .andExpect(jsonPath("$.point").value("SAINT_EXUPERY"))
      .andExpect(jsonPath("$.status").value("AVAILABLE"));
  }

  @Test
  void shouldRetainDamageQuoteOnDeposit() throws Exception {
    api.vehicle("COMFORT", "PART_DIEU");
    var reservation = api.reservation(uniqueEmail(), "COMFORT", 3);
    clock.at(PICK_UP);
    var contract = api.contract(reservation);
    clock.at(PICK_UP.plusDays(3));

    api.giveBack(contract, "PART_DIEU", 100, "0", "Scratched door").andExpect(jsonPath("$.damage").value("Scratched door"));

    api.get("/api/rental/fleet/vehicles/" + read(api.get("/api/rental/counter/contracts/" + contract), "$.plate")).andExpect(jsonPath("$.status").value("IN_MAINTENANCE"));
    clock.at(PICK_UP.plusDays(12));
    billing.releaseDeposits();
    api.get("/api/rental/billing/deposits/" + contract).andExpect(jsonPath("$.status").value("HELD"));
    api.postJson("/api/rental/billing/deposits/" + contract + "/damage-quote", "{}").andExpect(status().isBadRequest());
    api
      .postJson("/api/rental/billing/deposits/" + contract + "/damage-quote", "{\"amount\":3000}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.retained").value(800))
      .andExpect(jsonPath("$.released").value(0))
      .andExpect(jsonPath("$.status").value("RELEASED"));
    api.postJson("/api/rental/billing/deposits/" + contract + "/damage-quote", "{\"amount\":100}").andExpect(status().isConflict());
  }

  @Test
  void shouldNotCloseContractTwiceOrWithLowerMileage() throws Exception {
    api.vehicle("ECO", "PART_DIEU");
    var reservation = api.reservation(uniqueEmail(), "ECO", 3);
    clock.at(PICK_UP);
    var contract = api.contract(reservation);

    api.giveBack(contract, "PART_DIEU", -1, "0", null).andExpect(status().isConflict());
    api.giveBack(contract, "PART_DIEU", 10, "0", null).andExpect(status().isOk());
    api.giveBack(contract, "PART_DIEU", 10, "0", null).andExpect(status().isConflict());
  }

  @Test
  void shouldNotPickUpCancelledOrUnknownReservation() throws Exception {
    var reservation = api.reservation(uniqueEmail(), "ECO", 3);
    api.postJson("/api/rental/reservations/" + reservation + "/cancellation", "{}");

    api.pickUp(reservation).andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
    api.pickUp(UUID.randomUUID().toString()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
    api.get("/api/rental/counter/contracts/" + UUID.randomUUID()).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotReturnWithNegativeFuel() throws Exception {
    api.vehicle("ECO", "PART_DIEU");
    var reservation = api.reservation(uniqueEmail(), "ECO", 3);
    clock.at(PICK_UP);
    var contract = api.contract(reservation);

    api.giveBack(contract, "PART_DIEU", 10, "-1", null).andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotQuoteUnknownDeposit() throws Exception {
    api.get("/api/rental/billing/deposits/" + UUID.randomUUID()).andExpect(status().isNotFound());
    api.get("/api/rental/billing/invoices/" + UUID.randomUUID()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }
}
