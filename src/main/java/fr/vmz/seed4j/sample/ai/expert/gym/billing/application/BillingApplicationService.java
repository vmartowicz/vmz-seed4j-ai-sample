package fr.vmz.seed4j.sample.ai.expert.gym.billing.application;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.AccountStatement;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.Bank;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingAccountsRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingDesk;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.Invoice;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.InvoiceId;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.InvoicesRepository;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.MembershipLifecycle;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.Money;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class BillingApplicationService {

  private final BillingDesk desk;
  private final Clock clock;

  public BillingApplicationService(
    BillingAccountsRepository accounts,
    InvoicesRepository invoices,
    Bank bank,
    MembershipLifecycle memberships,
    Clock clock
  ) {
    desk = new BillingDesk(accounts, invoices, bank, memberships);
    this.clock = clock;
  }

  public Invoice openAccount(MemberId member, Money monthlyFee, Money signUpFee) {
    return desk.openAccount(member, monthlyFee, signUpFee, today());
  }

  public Invoice payByCard(InvoiceId invoice) {
    return desk.payByCard(invoice);
  }

  public Invoice debitResult(InvoiceId invoice, boolean accepted) {
    return desk.debitResult(invoice, accepted, today());
  }

  public AccountStatement settle(MemberId member) {
    return desk.settle(member);
  }

  public AccountStatement statement(MemberId member) {
    return desk.statement(member);
  }

  public boolean isInArrears(MemberId member) {
    return desk.isInArrears(member);
  }

  public void runDaily() {
    desk.runDaily(today());
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
