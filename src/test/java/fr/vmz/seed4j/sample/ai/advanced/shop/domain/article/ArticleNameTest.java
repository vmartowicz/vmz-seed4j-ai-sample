package fr.vmz.seed4j.sample.ai.advanced.shop.domain.article;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import fr.vmz.seed4j.sample.ai.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class ArticleNameTest {

  @Test
  void shouldNotBuildWithoutName() {
    assertThatThrownBy(() -> new ArticleName(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
  }

  @Test
  void shouldNotBuildWithMoreThan100Characters() {
    assertThatThrownBy(() -> new ArticleName("a".repeat(101))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldCompareNamesIgnoringCase() {
    assertThat(new ArticleName(" Silk scarf ").get()).isEqualTo("Silk scarf");
    assertThat(new ArticleName("Silk scarf").sameAs(new ArticleName("SILK SCARF"))).isTrue();
    assertThat(new ArticleName("Silk scarf").sameAs(new ArticleName("Wool scarf"))).isFalse();
  }
}
