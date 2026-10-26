package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheet;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheetStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestCareSheet(
  UUID id,
  UUID consultationId,
  UUID patientId,
  UUID insuredId,
  UUID doctorId,
  LocalDate date,
  BigDecimal baseFee,
  BigDecimal feeOverrun,
  BigDecimal reimbursement,
  boolean thirdPartyPayment,
  BigDecimal paidByPatient,
  CareSheetStatus status,
  @Nullable LocalDate sentOn
) {
  static RestCareSheet from(CareSheet sheet) {
    return new RestCareSheet(
      sheet.id().get(),
      sheet.consultation(),
      sheet.patient().get(),
      sheet.insured().get(),
      sheet.doctor().get(),
      sheet.date(),
      sheet.baseFee(),
      sheet.feeOverrun(),
      sheet.reimbursement(),
      sheet.thirdPartyPayment(),
      sheet.paidByPatient(),
      sheet.status(),
      sheet.sentOn().orElse(null)
    );
  }
}
