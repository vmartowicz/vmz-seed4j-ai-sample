package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.application.BillingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.Money;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaBilling {

  private final BillingApplicationService billing;

  public JavaBilling(BillingApplicationService billing) {
    this.billing = billing;
  }

  public JavaFirstInvoice openAccount(UUID member, BigDecimal monthlyFee, BigDecimal signUpFee) {
    var invoice = billing.openAccount(new MemberId(member), new Money(monthlyFee), new Money(signUpFee));

    return new JavaFirstInvoice(invoice.id().get(), invoice.amount().get());
  }

  public boolean isInArrears(UUID member) {
    return billing.isInArrears(new MemberId(member));
  }
}
