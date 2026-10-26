package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.Customer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record JavaCustomer(UUID id, LocalDate birthDate, BigDecimal monthlyIncome, LocalDate customerSince, boolean minor) {
  static JavaCustomer from(Customer customer, LocalDate today) {
    return new JavaCustomer(customer.id().get(), customer.birthDate(), customer.monthlyIncome(), customer.customerSince(), customer.isMinorOn(today));
  }
}
