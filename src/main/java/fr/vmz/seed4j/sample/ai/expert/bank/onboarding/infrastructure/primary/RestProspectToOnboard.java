package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.ProspectToOnboard;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.RiskLevel;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestProspectToOnboard(
  @Nullable String fullName,
  @Nullable LocalDate birthDate,
  @Nullable BigDecimal monthlyIncome,
  @Nullable LocalDate identityDocumentExpiry,
  @Nullable LocalDate proofOfAddressDate,
  boolean incomeProofProvided,
  boolean politicallyExposed,
  @Nullable RiskLevel risk,
  @Nullable String legalGuardian
) {
  ProspectToOnboard toDomain() {
    return new ProspectToOnboard(
      fullName,
      birthDate,
      monthlyIncome,
      identityDocumentExpiry,
      proofOfAddressDate,
      incomeProofProvided,
      politicallyExposed,
      risk,
      legalGuardian
    );
  }
}
