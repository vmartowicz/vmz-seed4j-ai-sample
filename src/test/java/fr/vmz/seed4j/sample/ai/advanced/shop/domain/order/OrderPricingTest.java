package fr.vmz.seed4j.sample.ai.advanced.shop.domain.order;

import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.MoneyFixture.money;
import static fr.vmz.seed4j.sample.ai.advanced.shop.domain.order.CustomerOrdersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.Money;
import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.ArticleId;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class OrderPricingTest {

  @Test
  void shouldChargeShippingUpTo50Euros() {
    var pricing = OrderPricing.of(List.of(line(ArticleId.newId(), 2, "25.00")), false);

    assertThat(pricing.items()).isEqualTo(money("50.00"));
    assertThat(pricing.discount()).isEqualTo(Money.ZERO);
    assertThat(pricing.shipping()).isEqualTo(money("5.00"));
    assertThat(pricing.total()).isEqualTo(money("55.00"));
  }

  @Test
  void shouldOfferShippingAbove50Euros() {
    var pricing = OrderPricing.of(List.of(line(ArticleId.newId(), 1, "25.00"), line(ArticleId.newId(), 1, "25.01")), false);

    assertThat(pricing.shipping()).isEqualTo(Money.ZERO);
    assertThat(pricing.total()).isEqualTo(money("50.01"));
  }

  @Test
  void shouldDiscount10PercentOfItemsForLoyalCustomers() {
    var pricing = OrderPricing.of(List.of(line(ArticleId.newId(), 1, "33.35")), true);

    assertThat(pricing.discount()).isEqualTo(money("3.34"));
    assertThat(pricing.itemsAfterDiscount()).isEqualTo(money("30.01"));
    assertThat(pricing.total()).isEqualTo(money("35.01"));
  }

  @Test
  void shouldChargeShippingWhenDiscountBringsItemsTo50EurosOrLess() {
    var pricing = OrderPricing.of(List.of(line(ArticleId.newId(), 1, "55.00")), true);

    assertThat(pricing.itemsAfterDiscount()).isEqualTo(money("49.50"));
    assertThat(pricing.shipping()).isEqualTo(money("5.00"));
  }
}
