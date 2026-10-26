package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public final class Deposit {

  private static final int RELEASE_DAYS = 7;
  private static final int DAMAGE_QUOTE_DAYS = 30;

  private final UUID contract;
  private final Email customer;
  private final BigDecimal amount;
  private DepositStatus status;
  private @Nullable LocalDate returnedOn;
  private boolean damaged;
  private BigDecimal retained;

  public Deposit(UUID contract, Email customer, BigDecimal amount) {
    Assert.notNull("contract", contract);
    Assert.notNull("customer", customer);
    Assert.field("amount", amount).strictlyPositive();

    this.contract = contract;
    this.customer = customer;
    this.amount = amount;
    status = DepositStatus.HELD;
    retained = BigDecimal.ZERO;
  }

  public void returned(LocalDate on, boolean withDamage) {
    returnedOn = on;
    damaged = withDamage;
  }

  public boolean releaseIfDue(LocalDate today) {
    if (status != DepositStatus.HELD || returnedOn == null) {
      return false;
    }

    var waitingDays = damaged ? DAMAGE_QUOTE_DAYS + 1 : RELEASE_DAYS;
    if (today.isBefore(returnedOn.plusDays(waitingDays))) {
      return false;
    }

    status = DepositStatus.RELEASED;
    return true;
  }

  public void retainForDamage(BigDecimal quote, LocalDate today) {
    Assert.field("quote", quote).min(0);
    if (status != DepositStatus.HELD) {
      throw new DepositAlreadyReleasedException(contract);
    }

    var returned = returnedOn().filter(day -> damaged).orElseThrow(() -> new NoDamageDeclaredException(contract));
    if (today.isAfter(returned.plusDays(DAMAGE_QUOTE_DAYS))) {
      throw new DamageQuoteTooLateException(contract);
    }

    retained = quote.min(amount);
    status = DepositStatus.RELEASED;
  }

  public BigDecimal released() {
    return amount.subtract(retained);
  }

  public UUID contract() {
    return contract;
  }

  public Email customer() {
    return customer;
  }

  public BigDecimal amount() {
    return amount;
  }

  public DepositStatus status() {
    return status;
  }

  public Optional<LocalDate> returnedOn() {
    return Optional.ofNullable(returnedOn);
  }

  public BigDecimal retained() {
    return retained;
  }
}
