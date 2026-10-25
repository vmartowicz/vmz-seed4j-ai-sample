package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import static fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain.ScheduleFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import java.util.Set;
import org.junit.jupiter.api.Test;

@UnitTest
class ScheduleValuesTest {

  @Test
  void shouldHave3StudiosAtPartDieu() {
    assertThat(Studios.of(Gym.PART_DIEU)).extracting(Studio::name, Studio::capacity).containsExactly(
      tuple("A", 30),
      tuple("B", 20),
      tuple("C", 12)
    );
  }

  @Test
  void shouldHave2StudiosInOtherGyms() {
    assertThat(Studios.of(Gym.ECULLY)).extracting(Studio::name, Studio::capacity).containsExactly(tuple("A", 25), tuple("B", 15));
  }

  @Test
  void shouldNotFindUnknownStudio() {
    assertThatThrownBy(() -> Studios.studio(Gym.ECULLY, "C")).isExactlyInstanceOf(UnknownStudioException.class).hasMessageContaining("ECULLY");
  }

  @Test
  void shouldRegisterCertifiedCoach() {
    var coach = yogaCoach();

    assertThat(coach.id()).isNotNull();
    assertThat(coach.name()).isEqualTo("Alice Martin");
    assertThat(coach.certifications()).containsExactlyInAnyOrder(Discipline.YOGA, Discipline.PILATES);
    assertThat(coach.isCertifiedFor(Discipline.YOGA)).isTrue();
    assertThat(coach.isCertifiedFor(Discipline.BOXING)).isFalse();
  }

  @Test
  void shouldNotRegisterCoachWithoutCertification() {
    assertThatThrownBy(() -> new CoachToRegister("Alice Martin", Set.of())).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldNotRegisterCoachWithoutName() {
    assertThatThrownBy(() -> new CoachToRegister(" ", Set.of(Discipline.YOGA))).isInstanceOf(AssertionException.class);
  }
}
