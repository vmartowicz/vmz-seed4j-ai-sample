package fr.vmz.seed4j.sample.ai.shared.bank.domain;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class BankKernelTest {

  @Test
  void shouldAcceptValidFrenchIban() {
    assertThat(new Iban("fr76 3000 6000 0112 3456 7890 189").get()).isEqualTo("FR7630006000011234567890189");
  }

  @ParameterizedTest
  @ValueSource(strings = { "FR7630006000011234567890188", "DE89370400440532013000", "FR763000600001123456789018", "FR76300060000112345678901X9" })
  void shouldNotAcceptInvalidIban(String iban) {
    assertThatThrownBy(() -> new Iban(iban)).isExactlyInstanceOf(InvalidIbanException.class);
  }

  @Test
  void shouldNotAcceptBlankIban() {
    assertThatThrownBy(() -> new Iban(" ")).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldGenerateValidIbansOfOurBank() {
    var iban = Iban.generate();

    assertThat(new Iban(iban.get())).isEqualTo(iban);
    assertThat(iban.get()).startsWith("FR").contains("17806");
    assertThat(iban.isOurs()).isTrue();
    assertThat(new Iban("FR7630006000011234567890189").isOurs()).isFalse();
    assertThat(Iban.generate()).isNotEqualTo(iban);
  }

  @Test
  void shouldGenerateCustomerIds() {
    assertThat(CustomerId.newId()).isNotEqualTo(CustomerId.newId());
  }
}
