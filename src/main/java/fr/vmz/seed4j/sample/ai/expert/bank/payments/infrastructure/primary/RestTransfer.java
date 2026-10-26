package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Transfer;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferKind;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestTransfer(
  UUID id,
  String payer,
  String payeeName,
  String payeeIban,
  BigDecimal amount,
  String reason,
  TransferKind kind,
  LocalDateTime orderedAt,
  LocalDate executionDate,
  TransferStatus status,
  @Nullable String rejection
) {
  static RestTransfer from(Transfer transfer) {
    return new RestTransfer(
      transfer.id().get(),
      transfer.payer().get(),
      transfer.payeeName(),
      transfer.payeeIban().get(),
      transfer.amount(),
      transfer.reason(),
      transfer.kind(),
      transfer.orderedAt(),
      transfer.executionDate(),
      transfer.status(),
      transfer.rejection().orElse(null)
    );
  }
}
