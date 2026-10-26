package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyAccount;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyStatus;
import java.time.LocalDate;

record RestLoyaltyAccount(String customer, int balance, int earnedInLast12Months, LoyaltyStatus status) {
  static RestLoyaltyAccount from(LoyaltyAccount account, LocalDate today) {
    return new RestLoyaltyAccount(account.customer().get(), account.balanceOn(today), account.earnedInLast12MonthsOn(today), account.statusOn(today));
  }
}
