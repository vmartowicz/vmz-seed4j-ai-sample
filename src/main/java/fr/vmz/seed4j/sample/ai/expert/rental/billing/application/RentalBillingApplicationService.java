package fr.vmz.seed4j.sample.ai.expert.rental.billing.application;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.Deposit;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.DepositsRepository;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoice;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoiceId;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.FinalInvoicesRepository;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.LoyaltyPoints;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.RentalCashier;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.ReturnReport;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RentalBillingApplicationService {

  private final RentalCashier cashier;
  private final Clock clock;

  public RentalBillingApplicationService(FinalInvoicesRepository invoices, DepositsRepository deposits, LoyaltyPoints loyalty, Clock clock) {
    cashier = new RentalCashier(invoices, deposits, loyalty);
    this.clock = clock;
  }

  public Deposit holdDeposit(UUID contract, Email customer, BigDecimal amount) {
    return cashier.holdDeposit(contract, customer, amount);
  }

  public FinalInvoice invoice(ReturnReport report) {
    return cashier.invoice(report, today());
  }

  public FinalInvoice invoice(FinalInvoiceId invoice) {
    return cashier.invoice(invoice);
  }

  public FinalInvoice pay(FinalInvoiceId invoice) {
    return cashier.pay(invoice);
  }

  public Deposit deposit(UUID contract) {
    return cashier.deposit(contract);
  }

  public Deposit retainForDamage(UUID contract, BigDecimal quote) {
    return cashier.retainForDamage(contract, quote, today());
  }

  public void releaseDeposits() {
    cashier.releaseDeposits(today());
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
