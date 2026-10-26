package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import org.jspecify.annotations.Nullable;

public record ProspectToOnboard(
  String fullName,
  LocalDate birthDate,
  BigDecimal monthlyIncome,
  LocalDate identityDocumentExpiry,
  LocalDate proofOfAddressDate,
  boolean incomeProofProvided,
  boolean politicallyExposed,
  RiskLevel risk,
  @Nullable String legalGuardian
) {
  private static final int ADULT_AGE = 18;
  private static final int YOUTH_AGE = 12;
  private static final int PROOF_OF_ADDRESS_MONTHS = 3;

  public ProspectToOnboard {
    Assert.field("fullName", fullName).notBlank().maxLength(100);
    Assert.notNull("birthDate", birthDate);
    Assert.field("monthlyIncome", monthlyIncome).min(0);
    Assert.notNull("identityDocumentExpiry", identityDocumentExpiry);
    Assert.notNull("proofOfAddressDate", proofOfAddressDate);
    Assert.notNull("risk", risk);

    legalGuardian = legalGuardian == null || legalGuardian.isBlank() ? null : legalGuardian.trim();
  }

  public Customer onboard(LocalDate today) {
    var age = Period.between(birthDate, today).getYears();
    if (age < YOUTH_AGE) {
      throw new IncompleteFileException("customers must be at least 12 years old");
    }

    if (age < ADULT_AGE && legalGuardian == null) {
      throw new IncompleteFileException("minors need the consent of a legal guardian");
    }

    if (identityDocumentExpiry.isBefore(today)) {
      throw new IncompleteFileException("the identity document has expired");
    }

    if (proofOfAddressDate.isBefore(today.minusMonths(PROOF_OF_ADDRESS_MONTHS))) {
      throw new IncompleteFileException("the proof of address is older than 3 months");
    }

    if (!incomeProofProvided) {
      throw new IncompleteFileException("the proof of income is missing");
    }

    return new Customer(this, today);
  }
}
