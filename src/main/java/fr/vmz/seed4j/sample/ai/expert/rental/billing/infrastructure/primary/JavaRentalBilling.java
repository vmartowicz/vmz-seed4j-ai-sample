package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.application.RentalBillingApplicationService;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaRentalBilling {

  private final RentalBillingApplicationService billing;

  public JavaRentalBilling(RentalBillingApplicationService billing) {
    this.billing = billing;
  }

  public void holdDeposit(UUID contract, String customer, BigDecimal amount) {
    billing.holdDeposit(contract, new Email(customer), amount);
  }

  public UUID finalInvoice(JavaReturnReport report) {
    return billing.invoice(report.toDomain()).id().get();
  }
}
