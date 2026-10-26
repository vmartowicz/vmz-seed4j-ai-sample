package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.secondary.billing;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary.JavaRentalBilling;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary.JavaReturnReport;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.Cashier;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.ContractId;
import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.RentalReturn;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class CounterCashier implements Cashier {

  private final JavaRentalBilling billing;

  CounterCashier(JavaRentalBilling billing) {
    this.billing = billing;
  }

  @Override
  public void holdDeposit(ContractId contract, Email customer, BigDecimal amount) {
    billing.holdDeposit(contract.get(), customer.get(), amount);
  }

  @Override
  public UUID finalInvoice(RentalReturn rentalReturn) {
    return billing.finalInvoice(
      new JavaReturnReport(
        rentalReturn.contract().get(),
        rentalReturn.customer().get(),
        rentalReturn.bookedCategory(),
        rentalReturn.dailyRate(),
        rentalReturn.days(),
        rentalReturn.paid(),
        rentalReturn.pickedUpAt(),
        rentalReturn.plannedReturnAt(),
        rentalReturn.returnedAt(),
        rentalReturn.pickUpPoint(),
        rentalReturn.returnPoint(),
        rentalReturn.kilometres(),
        rentalReturn.missingFuel(),
        rentalReturn.electric(),
        rentalReturn.damaged()
      )
    );
  }
}
