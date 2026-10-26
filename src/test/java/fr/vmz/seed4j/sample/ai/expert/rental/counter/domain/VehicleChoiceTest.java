package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import org.junit.jupiter.api.Test;

@UnitTest
class VehicleChoiceTest {

  @Test
  void shouldTryBookedCategoryThenUpgrades() {
    assertThat(VehicleChoice.categoriesFor(Category.ECO, false, false)).containsExactly(Category.ECO, Category.COMFORT, Category.SUV, Category.PREMIUM);
  }

  @Test
  void shouldTryCategoryAboveFirstForGoldMembers() {
    assertThat(VehicleChoice.categoriesFor(Category.ECO, true, false)).containsExactly(Category.COMFORT, Category.ECO, Category.SUV, Category.PREMIUM);
    assertThat(VehicleChoice.categoriesFor(Category.PREMIUM, true, false)).containsExactly(Category.PREMIUM);
  }

  @Test
  void shouldNotUpgradeYoungDriversToPremium() {
    assertThat(VehicleChoice.categoriesFor(Category.SUV, true, true)).containsExactly(Category.SUV);
    assertThat(VehicleChoice.categoriesFor(Category.ECO, false, true)).containsExactly(Category.ECO, Category.COMFORT, Category.SUV);
  }

  @Test
  void shouldNotUpgradeVans() {
    assertThat(VehicleChoice.categoriesFor(Category.VAN, true, false)).containsExactly(Category.VAN);
  }

  @Test
  void shouldTakeDepositOfBookedCategory() {
    assertThat(VehicleChoice.depositOf(Category.ECO)).isEqualByComparingTo("800");
    assertThat(VehicleChoice.depositOf(Category.COMFORT)).isEqualByComparingTo("800");
    assertThat(VehicleChoice.depositOf(Category.SUV)).isEqualByComparingTo("1500");
    assertThat(VehicleChoice.depositOf(Category.VAN)).isEqualByComparingTo("1500");
    assertThat(VehicleChoice.depositOf(Category.PREMIUM)).isEqualByComparingTo("2500");
  }
}
