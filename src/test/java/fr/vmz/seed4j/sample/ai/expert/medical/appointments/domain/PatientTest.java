package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import static fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class PatientTest {

  @Test
  void shouldAcceptSocialSecurityNumberWithValidKey() {
    assertThat(new SocialSecurityNumber("1 90 05 75 123 456 22").get()).isEqualTo("190057512345622");
  }

  @ParameterizedTest
  @ValueSource(strings = { "190057512345623", "19005751234565", "1900575123456AB" })
  void shouldNotAcceptInvalidSocialSecurityNumber(String number) {
    assertThatThrownBy(() -> new SocialSecurityNumber(number)).isExactlyInstanceOf(InvalidSocialSecurityNumberException.class);
  }

  @Test
  void shouldRegisterPatient() {
    var referring = DoctorId.newId();
    var parent = PatientId.newId();

    var patient = new PatientToRegister(" Doe ", " Jane ", LocalDate.of(1990, 5, 4), Sex.FEMALE, SSN, "0612345678", referring, parent, true, true).register();

    assertThat(patient.id()).isNotNull();
    assertThat(patient.lastName()).isEqualTo("Doe");
    assertThat(patient.firstName()).isEqualTo("Jane");
    assertThat(patient.birthDate()).isEqualTo(LocalDate.of(1990, 5, 4));
    assertThat(patient.sex()).isEqualTo(Sex.FEMALE);
    assertThat(patient.socialSecurityNumber().get()).isEqualTo(SSN);
    assertThat(patient.phone()).isEqualTo("0612345678");
    assertThat(patient.referringDoctor()).contains(referring);
    assertThat(patient.insured()).isEqualTo(parent);
    assertThat(patient.longTermIllness()).isTrue();
    assertThat(patient.solidarityHealthCover()).isTrue();
  }

  @Test
  void shouldBeItsOwnInsuredByDefault() {
    var patient = patient();

    assertThat(patient.insured()).isEqualTo(patient.id());
    assertThat(patient.referringDoctor()).isEmpty();
  }

  @Test
  void shouldChangeReferringDoctorAndInsured() {
    var patient = patient();
    var doctor = DoctorId.newId();
    var parent = PatientId.newId();

    patient.changeReferringDoctor(doctor);
    patient.attachTo(parent);

    assertThat(patient.referringDoctor()).contains(doctor);
    assertThat(patient.insured()).isEqualTo(parent);
  }

  @Test
  void shouldKnowChildrenUnder16() {
    var child = child(MONDAY.minusYears(16).plusDays(1)).register();

    assertThat(child.isUnder16On(MONDAY)).isTrue();
    assertThat(child.isUnder16On(MONDAY.plusDays(1))).isFalse();
  }

  @Test
  void shouldNotRegisterWithoutLastName() {
    assertThatThrownBy(() -> new PatientToRegister(" ", "Jane", LocalDate.of(1990, 1, 1), Sex.FEMALE, SSN, "06", null, null, false, false)).isInstanceOf(
      AssertionException.class
    );
  }
}
