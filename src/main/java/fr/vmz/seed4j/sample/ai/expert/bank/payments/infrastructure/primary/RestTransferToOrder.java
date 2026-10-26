package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PayeeId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferKind;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferToOrder;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestTransferToOrder(@Nullable String payer, @Nullable UUID payeeId, @Nullable BigDecimal amount, @Nullable String reason, @Nullable TransferKind kind) {
  TransferToOrder toDomain() {
    return new TransferToOrder(new Iban(payer), new PayeeId(payeeId), amount, reason, kind);
  }
}
