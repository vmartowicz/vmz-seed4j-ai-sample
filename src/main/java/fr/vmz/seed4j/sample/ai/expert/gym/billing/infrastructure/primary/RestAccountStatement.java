package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.AccountStatement;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.AccountStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestAccountStatement(
  UUID memberId,
  AccountStatus status,
  BigDecimal monthlyFee,
  @Nullable LocalDate arrearsSince,
  List<RestInvoice> invoices
) {
  static RestAccountStatement from(AccountStatement statement) {
    var account = statement.account();

    return new RestAccountStatement(
      account.member().get(),
      account.status(),
      account.monthlyFee().get(),
      account.arrearsSince().orElse(null),
      statement.invoices().stream().map(RestInvoice::from).toList()
    );
  }
}
