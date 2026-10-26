package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class OnboardingFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 6, 1);

  private OnboardingFixture() {}

  public static ProspectToOnboard adult() {
    return new ProspectToOnboard("Jane Doe", LocalDate.of(1990, 5, 4), new BigDecimal("2500"), TODAY.plusYears(5), TODAY.minusMonths(1), true, false, RiskLevel.LOW, null);
  }

  public static ProspectToOnboard born(LocalDate birthDate, String legalGuardian) {
    return new ProspectToOnboard("Kid Doe", birthDate, BigDecimal.ZERO, TODAY.plusYears(5), TODAY.minusMonths(1), true, false, RiskLevel.LOW, legalGuardian);
  }

  public static ProspectToOnboard withDocuments(LocalDate idExpiry, LocalDate proofOfAddress, boolean incomeProof) {
    return new ProspectToOnboard("Jane Doe", LocalDate.of(1990, 5, 4), new BigDecimal("2500"), idExpiry, proofOfAddress, incomeProof, false, RiskLevel.LOW, null);
  }

  public static Customer customer() {
    return adult().onboard(TODAY);
  }
}
