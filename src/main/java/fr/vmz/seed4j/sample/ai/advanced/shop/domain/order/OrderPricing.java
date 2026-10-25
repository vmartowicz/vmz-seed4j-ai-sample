package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Money;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.util.List;

public record OrderPricing(Money items, Money discount, Money shipping) {
  private static final Money FREE_SHIPPING_THRESHOLD = new Money(BigDecimal.valueOf(50));
  private static final Money SHIPPING_FEE = new Money(BigDecimal.valueOf(5));
  private static final int LOYALTY_DISCOUNT_PERCENT = 10;

  public OrderPricing {
    Assert.notNull("items", items);
    Assert.notNull("discount", discount);
    Assert.notNull("shipping", shipping);
  }

  public static OrderPricing of(List<OrderLine> lines, boolean loyalCustomer) {
    var items = lines.stream().map(OrderLine::total).reduce(Money.ZERO, Money::plus);
    var discount = loyalCustomer ? items.percent(LOYALTY_DISCOUNT_PERCENT) : Money.ZERO;
    var shipping = items.minus(discount).isGreaterThan(FREE_SHIPPING_THRESHOLD) ? Money.ZERO : SHIPPING_FEE;

    return new OrderPricing(items, discount, shipping);
  }

  public Money itemsAfterDiscount() {
    return items.minus(discount);
  }

  public Money total() {
    return itemsAfterDiscount().plus(shipping);
  }
}
