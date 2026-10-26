package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class FinalInvoice {

  private final FinalInvoiceId id;
  private final UUID contract;
  private final Email customer;
  private final BigDecimal paidRental;
  private final List<Charge> charges;
  private FinalInvoiceStatus status;

  private FinalInvoice(ReturnReport report) {
    id = FinalInvoiceId.newId();
    contract = report.contract();
    customer = report.customer();
    paidRental = report.paidRental();
    charges = ReturnCharges.of(report);
    status = total().signum() > 0 ? FinalInvoiceStatus.ISSUED : FinalInvoiceStatus.PAID;
  }

  public static FinalInvoice issue(ReturnReport report) {
    return new FinalInvoice(report);
  }

  public void pay() {
    if (isPaid()) {
      throw new FinalInvoiceAlreadyPaidException(id);
    }

    status = FinalInvoiceStatus.PAID;
  }

  public BigDecimal total() {
    return charges.stream().map(Charge::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public int loyaltyPoints() {
    var earning = charges
      .stream()
      .filter(charge -> charge.kind() != ChargeKind.FUEL)
      .map(Charge::amount)
      .reduce(paidRental, BigDecimal::add);

    return Math.max(0, earning.intValue());
  }

  public boolean isPaid() {
    return status == FinalInvoiceStatus.PAID;
  }

  public FinalInvoiceId id() {
    return id;
  }

  public UUID contract() {
    return contract;
  }

  public Email customer() {
    return customer;
  }

  public List<Charge> charges() {
    return charges;
  }

  public FinalInvoiceStatus status() {
    return status;
  }
}
