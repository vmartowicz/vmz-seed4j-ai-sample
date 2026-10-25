package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class DinerValuesTest {

  @Test
  void shouldNormalizeEmail() {
    assertThat(new DinerEmail("  Jane@Mail.COM ").get()).isEqualTo("jane@mail.com");
  }

  @Test
  void shouldNotBuildInvalidEmail() {
    assertThatThrownBy(() -> new DinerEmail("jane@mail")).isExactlyInstanceOf(InvalidDinerEmailException.class).hasMessageContaining(
      "jane@mail"
    );
  }

  @Test
  void shouldNotBuildBlankEmail() {
    assertThatThrownBy(() -> new DinerEmail(" ")).isInstanceOf(AssertionException.class);
  }

  @ParameterizedTest
  @ValueSource(strings = { "06 12 34 56 78", "0612345678", "06.12.34.56.78", " 06-12-34-56-78 " })
  void shouldFormatFrenchPhoneNumber(String number) {
    assertThat(new PhoneNumber(number).get()).isEqualTo("06 12 34 56 78");
  }

  @ParameterizedTest
  @ValueSource(strings = { "+33612345678", "0012345678", "06 12 34 56", "06 12 34 56 789", "06 12 AB 56 78" })
  void shouldNotBuildInvalidPhoneNumber(String number) {
    assertThatThrownBy(() -> new PhoneNumber(number)).isExactlyInstanceOf(InvalidPhoneNumberException.class);
  }

  @Test
  void shouldNotBuildBlankPhoneNumber() {
    assertThatThrownBy(() -> new PhoneNumber(" ")).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldBuildContact() {
    var contact = new Contact(" Jane Doe ", new DinerEmail("jane@mail.com"), new PhoneNumber("0612345678"));

    assertThat(contact.fullName()).isEqualTo("Jane Doe");
  }

  @Test
  void shouldNotBuildContactWithoutName() {
    assertThatThrownBy(() -> new Contact(" ", new DinerEmail("jane@mail.com"), new PhoneNumber("0612345678"))).isInstanceOf(
      AssertionException.class
    );
  }

  @Test
  void shouldNotBuildContactWithTooLongName() {
    assertThatThrownBy(() -> new Contact("a".repeat(101), new DinerEmail("jane@mail.com"), new PhoneNumber("0612345678"))).isInstanceOf(
      AssertionException.class
    );
  }
}
