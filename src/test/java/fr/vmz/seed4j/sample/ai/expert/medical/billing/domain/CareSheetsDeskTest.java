package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class CareSheetsDeskTest {

  private static final LocalDate TODAY = LocalDate.of(2026, 6, 1);

  private final CareSheetsRepository sheets = mock(CareSheetsRepository.class, CALLS_REAL_METHODS);
  private final Insureds insureds = mock(Insureds.class);
  private final Practitioners practitioners = mock(Practitioners.class);
  private final HealthInsurer insurer = mock(HealthInsurer.class);
  private final CareSheetsDesk desk = new CareSheetsDesk(sheets, insureds, practitioners, insurer);

  private final PatientId patient = PatientId.newId();
  private final DoctorId doctor = DoctorId.newId();

  @Test
  void shouldBillClosedConsultation() {
    doReturn(Optional.of(new Insurance(patient, true, false, false))).when(insureds).insuranceOf(patient);
    doReturn(Optional.of(new Tariff(false, BigDecimal.ZERO))).when(practitioners).tariffOf(doctor);

    var sheet = desk.bill(new ActToBill(UUID.randomUUID(), patient, doctor, TODAY));

    assertThat(sheet.reimbursement()).isEqualByComparingTo("21");
    verify(sheets).save(sheet);
  }

  @Test
  void shouldNotBillUnknownPatientOrDoctor() {
    doReturn(Optional.empty()).when(insureds).insuranceOf(any());
    var act = new ActToBill(UUID.randomUUID(), patient, doctor, TODAY);

    assertThatThrownBy(() -> desk.bill(act)).isExactlyInstanceOf(UnknownBillingPartyException.class);

    doReturn(Optional.of(new Insurance(patient, true, false, false))).when(insureds).insuranceOf(patient);
    doReturn(Optional.empty()).when(practitioners).tariffOf(doctor);

    assertThatThrownBy(() -> desk.bill(act)).isExactlyInstanceOf(UnknownBillingPartyException.class);
  }

  @Test
  void shouldSendPendingCareSheets() {
    var pending = CareSheet.issue(new ActToBill(UUID.randomUUID(), patient, doctor, TODAY), new Insurance(patient, true, false, false), new Tariff(false, BigDecimal.ZERO));
    var sent = CareSheet.issue(new ActToBill(UUID.randomUUID(), patient, doctor, TODAY), new Insurance(patient, true, false, false), new Tariff(false, BigDecimal.ZERO));
    sent.sent(TODAY);
    doReturn(List.of(pending, sent)).when(sheets).all();

    desk.sendPending(TODAY.plusDays(1));

    assertThat(pending.status()).isEqualTo(CareSheetStatus.SENT);
    verify(insurer).send(pending);
    verify(insurer, never()).send(sent);
    verify(sheets).save(pending);
  }

  @Test
  void shouldListCareSheetsOfPatient() {
    var sheet = CareSheet.issue(new ActToBill(UUID.randomUUID(), patient, doctor, TODAY), new Insurance(patient, true, false, false), new Tariff(false, BigDecimal.ZERO));
    var other = CareSheet.issue(new ActToBill(UUID.randomUUID(), PatientId.newId(), doctor, TODAY), new Insurance(patient, true, false, false), new Tariff(false, BigDecimal.ZERO));
    doReturn(List.of(sheet, other)).when(sheets).all();

    assertThat(desk.careSheetsOf(patient)).containsExactly(sheet);
  }

  @Test
  void shouldNotFindUnknownCareSheet() {
    var unknown = CareSheetId.newId();
    doReturn(Optional.empty()).when(sheets).get(unknown);

    assertThatThrownBy(() -> desk.careSheet(unknown)).isExactlyInstanceOf(UnknownCareSheetException.class);
  }
}
