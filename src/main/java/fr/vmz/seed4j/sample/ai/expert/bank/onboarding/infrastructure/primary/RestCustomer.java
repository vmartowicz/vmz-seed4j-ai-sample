package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.Customer;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.RiskLevel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestCustomer(
  UUID id,
  String fullName,
  LocalDate birthDate,
  BigDecimal monthlyIncome,
  RiskLevel risk,
  LocalDate customerSince,
  LocalDate nextReviewOn,
  boolean minor,
  @Nullable String legalGuardian
) {
  static RestCustomer from(Customer customer, LocalDate today) {
    return new RestCustomer(
      customer.id().get(),
      customer.fullName(),
      customer.birthDate(),
      customer.monthlyIncome(),
      customer.risk(),
      customer.customerSince(),
      customer.nextReviewOn(),
      customer.isMinorOn(today),
      customer.legalGuardian().orElse(null)
    );
  }
}
