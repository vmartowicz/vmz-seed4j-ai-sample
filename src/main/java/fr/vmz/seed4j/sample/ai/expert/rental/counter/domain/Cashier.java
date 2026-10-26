package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.math.BigDecimal;
import java.util.UUID;

public interface Cashier {
  void holdDeposit(ContractId contract, Email customer, BigDecimal amount);

  UUID finalInvoice(RentalReturn rentalReturn);
}
