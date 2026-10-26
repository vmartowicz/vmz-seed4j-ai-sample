package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RentalCashier {

  private final FinalInvoicesRepository invoices;
  private final DepositsRepository deposits;
  private final LoyaltyPoints loyalty;

  public RentalCashier(FinalInvoicesRepository invoices, DepositsRepository deposits, LoyaltyPoints loyalty) {
    this.invoices = invoices;
    this.deposits = deposits;
    this.loyalty = loyalty;
  }

  public Deposit holdDeposit(UUID contract, Email customer, BigDecimal amount) {
    var deposit = new Deposit(contract, customer, amount);

    deposits.save(deposit);

    return deposit;
  }

  public FinalInvoice invoice(ReturnReport report, LocalDate today) {
    var deposit = deposits.deposit(report.contract());
    deposit.returned(today, report.damaged());
    deposits.save(deposit);

    var invoice = FinalInvoice.issue(report);
    invoices.save(invoice);
    if (invoice.isPaid()) {
      creditPoints(invoice);
    }

    return invoice;
  }

  public FinalInvoice pay(FinalInvoiceId id) {
    var invoice = invoices.invoice(id);

    invoice.pay();
    invoices.save(invoice);
    creditPoints(invoice);

    return invoice;
  }

  public void releaseDeposits(LocalDate today) {
    deposits
      .all()
      .stream()
      .filter(deposit -> deposit.releaseIfDue(today))
      .forEach(deposits::save);
  }

  public Deposit retainForDamage(UUID contract, BigDecimal quote, LocalDate today) {
    var deposit = deposits.deposit(contract);

    deposit.retainForDamage(quote, today);
    deposits.save(deposit);

    return deposit;
  }

  public FinalInvoice invoice(FinalInvoiceId id) {
    return invoices.invoice(id);
  }

  public Deposit deposit(UUID contract) {
    return deposits.deposit(contract);
  }

  private void creditPoints(FinalInvoice invoice) {
    loyalty.credit(invoice.customer(), invoice.loyaltyPoints(), invoice.contract().toString());
  }
}
