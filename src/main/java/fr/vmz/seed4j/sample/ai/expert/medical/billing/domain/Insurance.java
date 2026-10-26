package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;

public record Insurance(PatientId insured, boolean referringDoctorDeclared, boolean longTermIllness, boolean solidarityHealthCover) {
  private static final BigDecimal REFERRING_DOCTOR_RATE = new BigDecimal("0.70");
  private static final BigDecimal NO_REFERRING_DOCTOR_RATE = new BigDecimal("0.30");

  public Insurance {
    Assert.notNull("insured", insured);
  }

  BigDecimal reimbursementRate() {
    return referringDoctorDeclared ? REFERRING_DOCTOR_RATE : NO_REFERRING_DOCTOR_RATE;
  }

  boolean thirdPartyPayment() {
    return longTermIllness || solidarityHealthCover;
  }
}
