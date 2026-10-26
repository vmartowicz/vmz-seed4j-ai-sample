package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransferScreening {
  ScreeningResult screen(Iban payer, String payeeName, BigDecimal amount, LocalDateTime payeeAddedAt);
}
