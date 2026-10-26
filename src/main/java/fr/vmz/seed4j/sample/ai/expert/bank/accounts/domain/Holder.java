package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public record Holder(CustomerId id, LocalDate birthDate, BigDecimal monthlyIncome, boolean minor) {
  private static final int FREE_FEES_AGE = 25;

  boolean isUnder25On(LocalDate date) {
    return Period.between(birthDate, date).getYears() < FREE_FEES_AGE;
  }
}
