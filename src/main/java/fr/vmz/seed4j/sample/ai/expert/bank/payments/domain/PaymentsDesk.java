package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentsDesk {

  private static final BigDecimal REJECTED_DIRECT_DEBIT_FEE = new BigDecimal("20");

  private final PayeesRepository payees;
  private final TransfersRepository transfers;
  private final MandatesRepository mandates;
  private final DirectDebitsRepository debits;
  private final PaymentAccounts accounts;
  private final TransferScreening screening;

  public PaymentsDesk(
    PayeesRepository payees,
    TransfersRepository transfers,
    MandatesRepository mandates,
    DirectDebitsRepository debits,
    PaymentAccounts accounts,
    TransferScreening screening
  ) {
    this.payees = payees;
    this.transfers = transfers;
    this.mandates = mandates;
    this.debits = debits;
    this.accounts = accounts;
    this.screening = screening;
  }

  public Payee addPayee(Iban payer, String name, Iban iban, LocalDateTime now) {
    assertExists(payer);

    var payee = Payee.add(payer, name, iban, now);
    payees.save(payee);

    return payee;
  }

  public Transfer order(TransferToOrder order, LocalDateTime now) {
    Assert.notNull("order", order);

    var payee = payees.get(order.payee()).filter(found -> found.payer().equals(order.payer())).orElseThrow(() -> new UnknownPayeeException(order.payee()));
    var transfer = Transfer.order(order, payee, now);

    var screened = screening.screen(order.payer(), payee.name(), order.amount(), payee.addedAt());
    if (screened == ScreeningResult.BLOCK) {
      transfer.block();
    } else if (screened == ScreeningResult.HOLD) {
      transfer.hold();
    } else {
      executeIfInstant(transfer);
    }

    transfers.save(transfer);
    return transfer;
  }

  public Transfer release(TransferId id, LocalDateTime now) {
    var transfer = transfers.transferOf(id);

    transfer.release(now);
    executeIfInstant(transfer);
    transfers.save(transfer);

    return transfer;
  }

  public void executeDue(LocalDateTime now) {
    transfers
      .all()
      .stream()
      .filter(transfer -> transfer.isDueOn(now.toLocalDate()))
      .forEach(transfer -> {
        execute(transfer);
        transfers.save(transfer);
      });
  }

  public Transfer transfer(TransferId id) {
    return transfers.transferOf(id);
  }

  public Mandate signMandate(Iban account, String creditor, LocalDate today) {
    assertExists(account);

    var mandate = Mandate.sign(account, creditor, today);
    mandates.save(mandate);

    return mandate;
  }

  public Mandate revoke(MandateId id) {
    var mandate = mandates.mandateOf(id);

    mandate.revoke();
    mandates.save(mandate);

    return mandate;
  }

  public DirectDebit collect(MandateId id, BigDecimal amount, LocalDateTime now) {
    Assert.field("amount", amount).strictlyPositive();
    var mandate = mandates.mandateOf(id);
    mandate.assertActive();

    var outcome = accounts.directDebit(mandate.account(), amount, "Direct debit " + mandate.creditor());
    var debit = outcome == DebitOutcome.DONE ? DirectDebit.collected(mandate, amount, now) : DirectDebit.rejected(mandate, amount, now);
    if (outcome != DebitOutcome.DONE) {
      accounts.chargeFee(mandate.account(), REJECTED_DIRECT_DEBIT_FEE, "Rejected direct debit " + mandate.creditor());
    }

    debits.save(debit);
    return debit;
  }

  public DirectDebit refund(DirectDebitId id, LocalDateTime now) {
    var debit = debits.debitOf(id);

    debit.refund(now);
    accounts.credit(debit.account(), debit.amount(), "Refund of direct debit " + debit.creditor());
    debits.save(debit);

    return debit;
  }

  private void executeIfInstant(Transfer transfer) {
    if (transfer.kind() == TransferKind.INSTANT) {
      execute(transfer);
    }
  }

  private void execute(Transfer transfer) {
    var outcome = accounts.debit(transfer.payer(), transfer.amount(), "Transfer to " + transfer.payeeName() + ": " + transfer.reason());
    if (outcome != DebitOutcome.DONE) {
      transfer.rejected(outcome == DebitOutcome.FROZEN ? "The account is frozen" : "Insufficient funds");
      return;
    }

    if (transfer.payeeIban().isOurs()) {
      accounts.credit(transfer.payeeIban(), transfer.amount(), "Transfer from " + transfer.payer().get() + ": " + transfer.reason());
    }
    transfer.executed();
  }

  private void assertExists(Iban account) {
    if (!accounts.exists(account)) {
      throw new UnknownPayerAccountException(account);
    }
  }
}
