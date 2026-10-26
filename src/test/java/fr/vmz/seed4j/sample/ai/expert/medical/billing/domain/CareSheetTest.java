package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class CareSheetTest {

  private static final LocalDate TODAY = LocalDate.of(2026, 6, 1);

  private final PatientId patient = PatientId.newId();
  private final ActToBill act = new ActToBill(UUID.randomUUID(), patient, DoctorId.newId(), TODAY);

  @Test
  void shouldReimburse70PercentOfGeneralPractitionerFee() {
    var sheet = CareSheet.issue(act, new Insurance(patient, true, false, false), new Tariff(false, BigDecimal.ZERO));

    assertThat(sheet.id()).isNotNull();
    assertThat(sheet.consultation()).isEqualTo(act.consultation());
    assertThat(sheet.patient()).isEqualTo(patient);
    assertThat(sheet.insured()).isEqualTo(patient);
    assertThat(sheet.doctor()).isEqualTo(act.doctor());
    assertThat(sheet.date()).isEqualTo(TODAY);
    assertThat(sheet.baseFee()).isEqualByComparingTo("30");
    assertThat(sheet.feeOverrun()).isEqualByComparingTo("0");
    assertThat(sheet.reimbursement()).isEqualByComparingTo("21.00");
    assertThat(sheet.thirdPartyPayment()).isFalse();
    assertThat(sheet.paidByPatient()).isEqualByComparingTo("30");
    assertThat(sheet.status()).isEqualTo(CareSheetStatus.PENDING);
  }

  @Test
  void shouldReimburseOnly30PercentWithoutReferringDoctor() {
    var sheet = CareSheet.issue(act, new Insurance(patient, false, false, false), new Tariff(true, new BigDecimal("20")));

    assertThat(sheet.baseFee()).isEqualByComparingTo("50");
    assertThat(sheet.reimbursement()).isEqualByComparingTo("15.00");
    assertThat(sheet.paidByPatient()).isEqualByComparingTo("70");
  }

  @Test
  void shouldNotChargePatientsInThirdPartyPayment() {
    var longTermIllness = CareSheet.issue(act, new Insurance(patient, true, true, false), new Tariff(true, new BigDecimal("20")));
    var solidarity = CareSheet.issue(act, new Insurance(patient, true, false, true), new Tariff(false, BigDecimal.ZERO));

    assertThat(longTermIllness.thirdPartyPayment()).isTrue();
    assertThat(longTermIllness.paidByPatient()).isEqualByComparingTo("0");
    assertThat(solidarity.thirdPartyPayment()).isTrue();
  }

  @Test
  void shouldSendCareSheetInNameOfInsuredParent() {
    var parent = PatientId.newId();

    var sheet = CareSheet.issue(act, new Insurance(parent, true, false, false), new Tariff(false, BigDecimal.ZERO));

    assertThat(sheet.insured()).isEqualTo(parent);
  }

  @Test
  void shouldBeSentOnce() {
    var sheet = CareSheet.issue(act, new Insurance(patient, true, false, false), new Tariff(false, BigDecimal.ZERO));

    sheet.sent(TODAY.plusDays(1));

    assertThat(sheet.status()).isEqualTo(CareSheetStatus.SENT);
    assertThat(sheet.sentOn()).contains(TODAY.plusDays(1));
    assertThat(sheet.isPending()).isFalse();
  }
}
