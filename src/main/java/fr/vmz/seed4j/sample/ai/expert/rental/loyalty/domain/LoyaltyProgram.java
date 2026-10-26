package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.time.LocalDate;
import java.util.UUID;

public class LoyaltyProgram {

  private final LoyaltyAccountsRepository accounts;

  public LoyaltyProgram(LoyaltyAccountsRepository accounts) {
    this.accounts = accounts;
  }

  public void credit(Email customer, int points, LocalDate today, String reference) {
    var account = account(customer);

    account.credit(points, today, reference);
    accounts.save(account);
  }

  public void redeemFreeDay(Email customer, UUID reservation, LocalDate today) {
    var account = account(customer);

    account.redeemFreeDay(reservation, today);
    accounts.save(account);
  }

  public void cancelRedemption(Email customer, UUID reservation) {
    accounts
      .get(customer)
      .ifPresent(account -> {
        account.cancelRedemption(reservation);
        accounts.save(account);
      });
  }

  public LoyaltyStatus statusOf(Email customer, LocalDate today) {
    return account(customer).statusOn(today);
  }

  public LoyaltyAccount account(Email customer) {
    return accounts.get(customer).orElseGet(() -> LoyaltyAccount.open(customer));
  }
}
