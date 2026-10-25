package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;

public class BillingDesk {

  private static final int DEBIT_DAY = 5;
  private static final Comparator<Invoice> BY_ISSUE = Comparator.comparing(Invoice::issuedOn).thenComparing(Invoice::kind);

  private final BillingAccountsRepository accounts;
  private final InvoicesRepository invoices;
  private final Bank bank;
  private final MembershipLifecycle memberships;

  public BillingDesk(BillingAccountsRepository accounts, InvoicesRepository invoices, Bank bank, MembershipLifecycle memberships) {
    this.accounts = accounts;
    this.invoices = invoices;
    this.bank = bank;
    this.memberships = memberships;
  }

  public Invoice openAccount(MemberId member, Money monthlyFee, Money signUpFee, LocalDate today) {
    var invoice = Invoice.firstMonth(member, monthlyFee, signUpFee, today);

    accounts.save(BillingAccount.open(member, monthlyFee));
    invoices.save(invoice);

    return invoice;
  }

  public Invoice payByCard(InvoiceId id) {
    var invoice = invoices.invoice(id);

    invoice.payByCard();
    invoices.save(invoice);
    if (invoice.kind() == InvoiceKind.FIRST_MONTH) {
      var account = accounts.account(invoice.member());
      account.firstPaymentReceived();
      accounts.save(account);
      memberships.activate(invoice.member());
    }

    return invoice;
  }

  public Invoice debitResult(InvoiceId id, boolean accepted, LocalDate today) {
    var invoice = invoices.invoice(id);

    if (accepted) {
      invoice.debitAccepted();
    } else if (invoice.debitRejected(today)) {
      var account = accounts.account(invoice.member());
      account.fallIntoArrears(today);
      accounts.save(account);
      invoices.save(Invoice.arrearsFee(invoice.member(), today));
    }

    invoices.save(invoice);
    return invoice;
  }

  public AccountStatement settle(MemberId member) {
    var account = accounts.account(member);

    account.settle();
    invoices
      .of(member)
      .stream()
      .filter(Invoice::isDue)
      .forEach(invoice -> {
        invoice.payByCard();
        invoices.save(invoice);
      });
    accounts.save(account);

    return statement(member);
  }

  public void runDaily(LocalDate today) {
    if (today.getDayOfMonth() == DEBIT_DAY) {
      billMonth(YearMonth.from(today), today);
    }

    invoices
      .all()
      .stream()
      .filter(invoice -> invoice.retryIfDue(today))
      .forEach(this::submit);

    accounts
      .all()
      .stream()
      .filter(account -> account.shouldBeTerminatedOn(today))
      .forEach(account -> {
        account.close();
        accounts.save(account);
        memberships.terminate(account.member());
      });
  }

  public AccountStatement statement(MemberId member) {
    return new AccountStatement(accounts.account(member), invoices.of(member).stream().sorted(BY_ISSUE).toList());
  }

  public boolean isInArrears(MemberId member) {
    return accounts.get(member).map(BillingAccount::isInArrears).orElse(false);
  }

  private void billMonth(YearMonth period, LocalDate today) {
    accounts
      .all()
      .stream()
      .filter(BillingAccount::isBillable)
      .filter(account -> !isBilled(account.member(), period))
      .filter(account -> !memberships.isSuspendedOn(account.member(), today))
      .map(account -> Invoice.monthly(account.member(), account.monthlyFee(), period, today))
      .forEach(this::submit);
  }

  private boolean isBilled(MemberId member, YearMonth period) {
    return invoices
      .of(member)
      .stream()
      .anyMatch(invoice -> invoice.kind() != InvoiceKind.ARREARS_FEE && invoice.period().equals(period));
  }

  private void submit(Invoice invoice) {
    invoices.save(invoice);
    bank.submitDebit(invoice);
  }
}
