package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public final class Mandate {

  private final MandateId id;
  private final Iban account;
  private final String creditor;
  private final LocalDate signedOn;
  private boolean active;

  private Mandate(Iban account, String creditor, LocalDate signedOn) {
    Assert.notNull("account", account);
    Assert.field("creditor", creditor).notBlank().maxLength(140);

    id = MandateId.newId();
    this.account = account;
    this.creditor = creditor.trim();
    this.signedOn = signedOn;
    active = true;
  }

  public static Mandate sign(Iban account, String creditor, LocalDate today) {
    return new Mandate(account, creditor, today);
  }

  public void revoke() {
    assertActive();

    active = false;
  }

  public void assertActive() {
    if (!active) {
      throw new MandateNotActiveException(id);
    }
  }

  public MandateId id() {
    return id;
  }

  public Iban account() {
    return account;
  }

  public String creditor() {
    return creditor;
  }

  public LocalDate signedOn() {
    return signedOn;
  }

  public boolean isActive() {
    return active;
  }
}
