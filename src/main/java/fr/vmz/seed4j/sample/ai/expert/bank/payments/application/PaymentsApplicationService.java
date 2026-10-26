package fr.vmz.seed4j.sample.ai.expert.bank.payments.application;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebit;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebitId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebitsRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Mandate;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.MandateId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.MandatesRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Payee;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PayeesRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PaymentAccounts;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PaymentsDesk;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Transfer;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferScreening;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferToOrder;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransfersRepository;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class PaymentsApplicationService {

  private final PaymentsDesk desk;
  private final Clock clock;

  public PaymentsApplicationService(
    PayeesRepository payees,
    TransfersRepository transfers,
    MandatesRepository mandates,
    DirectDebitsRepository debits,
    PaymentAccounts accounts,
    TransferScreening screening,
    Clock clock
  ) {
    desk = new PaymentsDesk(payees, transfers, mandates, debits, accounts, screening);
    this.clock = clock;
  }

  public Payee addPayee(Iban payer, String name, Iban iban) {
    return desk.addPayee(payer, name, iban, now());
  }

  public Transfer order(TransferToOrder order) {
    return desk.order(order, now());
  }

  public Transfer transfer(TransferId transfer) {
    return desk.transfer(transfer);
  }

  public Transfer release(TransferId transfer) {
    return desk.release(transfer, now());
  }

  public void executeDueTransfers() {
    desk.executeDue(now());
  }

  public Mandate signMandate(Iban account, String creditor) {
    return desk.signMandate(account, creditor, LocalDate.now(clock));
  }

  public Mandate revoke(MandateId mandate) {
    return desk.revoke(mandate);
  }

  public DirectDebit collect(MandateId mandate, BigDecimal amount) {
    return desk.collect(mandate, amount, now());
  }

  public DirectDebit refund(DirectDebitId debit) {
    return desk.refund(debit, now());
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
