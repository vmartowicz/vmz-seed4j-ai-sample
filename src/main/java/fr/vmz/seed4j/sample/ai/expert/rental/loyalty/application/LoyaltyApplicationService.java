package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.application;

import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyAccount;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyAccountsRepository;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyProgram;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyStatus;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyApplicationService {

  private final LoyaltyProgram program;
  private final Clock clock;

  public LoyaltyApplicationService(LoyaltyAccountsRepository accounts, Clock clock) {
    program = new LoyaltyProgram(accounts);
    this.clock = clock;
  }

  public void credit(Email customer, int points, String reference) {
    program.credit(customer, points, today(), reference);
  }

  public void redeemFreeDay(Email customer, UUID reservation) {
    program.redeemFreeDay(customer, reservation, today());
  }

  public void cancelRedemption(Email customer, UUID reservation) {
    program.cancelRedemption(customer, reservation);
  }

  public LoyaltyStatus statusOf(Email customer) {
    return program.statusOf(customer, today());
  }

  public LoyaltyAccount account(Email customer) {
    return program.account(customer);
  }

  public LocalDate today() {
    return LocalDate.now(clock);
  }
}
