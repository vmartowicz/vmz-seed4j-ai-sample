package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;

public record Payee(PayeeId id, Iban payer, String name, Iban iban, LocalDateTime addedAt) {
  public Payee {
    Assert.notNull("payer", payer);
    Assert.field("name", name).notBlank().maxLength(140);
    Assert.notNull("iban", iban);
    Assert.notNull("addedAt", addedAt);

    name = name.trim();
  }

  public static Payee add(Iban payer, String name, Iban iban, LocalDateTime now) {
    return new Payee(PayeeId.newId(), payer, name, iban, now);
  }
}
