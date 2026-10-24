package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import static fr.vmz.seed4j.sample.ai.clinic.domain.owner.OwnersFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class OwnerTest {

  @Test
  void shouldRegisterOwnerWithoutPets() {
    var owner = Owner.register(ownerInformation());

    assertThat(owner.id()).isNotNull();
    assertThat(owner.information()).isEqualTo(ownerInformation());
    assertThat(owner.pets()).isEmpty();
  }

  @Test
  void shouldUpdateInformationAndKeepPets() {
    var owner = owner();
    var pet = owner.addPet(leo(), TODAY);

    owner.update(ownerInformation("Betty", "Davis"));

    assertThat(owner.information()).isEqualTo(ownerInformation("Betty", "Davis"));
    assertThat(owner.pets()).containsExactly(pet);
  }

  @Nested
  class AddPet {

    @Test
    void shouldAddPet() {
      var owner = owner();

      var pet = owner.addPet(leo(), TODAY);

      assertThat(pet.id()).isNotNull();
      assertThat(pet.name()).isEqualTo(new PetName("Leo"));
      assertThat(pet.birthDate()).isEqualTo(LocalDate.of(2020, 9, 7));
      assertThat(pet.type()).isEqualTo(PetType.CAT);
      assertThat(pet.visits()).isEmpty();
      assertThat(owner.pets()).containsExactly(pet);
    }

    @Test
    void shouldAddPetBornToday() {
      var pet = owner().addPet(new PetInformation(new PetName("Leo"), TODAY, PetType.DOG), TODAY);

      assertThat(pet.birthDate()).isEqualTo(TODAY);
    }

    @Test
    void shouldNotAddPetBornInTheFuture() {
      var owner = owner();
      var tomorrow = TODAY.plusDays(1);

      assertThatThrownBy(() -> owner.addPet(new PetInformation(new PetName("Leo"), tomorrow, PetType.DOG), TODAY))
        .isExactlyInstanceOf(PetBornInTheFutureException.class)
        .hasMessageContaining(tomorrow.toString());
      assertThat(owner.pets()).isEmpty();
    }

    @Test
    void shouldNotAddPetWithSameNameIgnoringCase() {
      var owner = owner();
      owner.addPet(leo(), TODAY);

      assertThatThrownBy(() -> owner.addPet(pet(" lEO "), TODAY))
        .isExactlyInstanceOf(DuplicatedPetNameException.class)
        .hasMessageContaining("lEO");
      assertThat(owner.pets()).hasSize(1);
    }

    @Test
    void shouldSortPetsByName() {
      var owner = owner();
      owner.addPet(pet("max"), TODAY);
      owner.addPet(pet("Basil"), TODAY);
      owner.addPet(pet("leo"), TODAY);

      assertThat(owner.pets()).extracting(pet -> pet.name().get()).containsExactly("Basil", "leo", "max");
    }
  }

  @Nested
  class UpdatePet {

    @Test
    void shouldUpdatePet() {
      var owner = owner();
      var pet = owner.addPet(leo(), TODAY);

      var updated = owner.updatePet(pet.id(), new PetInformation(new PetName("Rosy"), LocalDate.of(2021, 4, 17), PetType.DOG), TODAY);

      assertThat(updated.id()).isEqualTo(pet.id());
      assertThat(updated.name()).isEqualTo(new PetName("Rosy"));
      assertThat(updated.birthDate()).isEqualTo(LocalDate.of(2021, 4, 17));
      assertThat(updated.type()).isEqualTo(PetType.DOG);
    }

    @Test
    void shouldNotUpdateUnknownPet() {
      var owner = owner();
      var unknownPet = PetId.newId();

      assertThatThrownBy(() -> owner.updatePet(unknownPet, leo(), TODAY))
        .isExactlyInstanceOf(UnknownPetException.class)
        .hasMessageContaining(unknownPet.get().toString());
    }

    @Test
    void shouldNotRenamePetWithNameOfAnotherPet() {
      var owner = owner();
      owner.addPet(leo(), TODAY);
      var basil = owner.addPet(pet("Basil"), TODAY);

      assertThatThrownBy(() -> owner.updatePet(basil.id(), pet("LEO"), TODAY)).isExactlyInstanceOf(DuplicatedPetNameException.class);
      assertThat(basil.name()).isEqualTo(new PetName("Basil"));
    }

    @Test
    void shouldRenamePetWithItsOwnNameInAnotherCase() {
      var owner = owner();
      var pet = owner.addPet(leo(), TODAY);
      owner.addPet(pet("Basil"), TODAY);

      var updated = owner.updatePet(pet.id(), pet("LEO"), TODAY);

      assertThat(updated.name()).isEqualTo(new PetName("LEO"));
    }

    @Test
    void shouldNotUpdatePetWithFutureBirthDate() {
      var owner = owner();
      var pet = owner.addPet(leo(), TODAY);

      assertThatThrownBy(() -> owner.updatePet(pet.id(), new PetInformation(new PetName("Leo"), TODAY.plusDays(1), PetType.CAT), TODAY))
        .isExactlyInstanceOf(PetBornInTheFutureException.class);
      assertThat(pet.birthDate()).isEqualTo(LocalDate.of(2020, 9, 7));
    }
  }

  @Nested
  class ScheduleVisit {

    @Test
    void shouldScheduleVisitTomorrow() {
      var owner = owner();
      var pet = owner.addPet(leo(), TODAY);

      var visit = owner.scheduleVisit(pet.id(), rabiesShot(), TODAY);

      assertThat(visit.id()).isNotNull();
      assertThat(visit.date()).isEqualTo(TODAY.plusDays(1));
      assertThat(visit.description()).isEqualTo(new VisitDescription("Rabies shot"));
      assertThat(pet.visits()).containsExactly(visit);
    }

    @Test
    void shouldNotScheduleVisitToday() {
      var owner = owner();
      var pet = owner.addPet(leo(), TODAY);

      assertThatThrownBy(() -> owner.scheduleVisit(pet.id(), visitOn(TODAY), TODAY))
        .isExactlyInstanceOf(VisitNotInFutureException.class)
        .hasMessageContaining(TODAY.toString());
      assertThat(pet.visits()).isEmpty();
    }

    @Test
    void shouldNotScheduleVisitForUnknownPet() {
      var owner = owner();
      var unknownPet = PetId.newId();

      assertThatThrownBy(() -> owner.scheduleVisit(unknownPet, rabiesShot(), TODAY)).isExactlyInstanceOf(UnknownPetException.class);
    }

    @Test
    void shouldSortVisitsByDate() {
      var owner = owner();
      var pet = owner.addPet(leo(), TODAY);
      owner.scheduleVisit(pet.id(), visitOn(TODAY.plusDays(10)), TODAY);
      owner.scheduleVisit(pet.id(), visitOn(TODAY.plusDays(2)), TODAY);
      owner.scheduleVisit(pet.id(), visitOn(TODAY.plusDays(5)), TODAY);

      assertThat(pet.visits()).extracting(Visit::date).containsExactly(TODAY.plusDays(2), TODAY.plusDays(5), TODAY.plusDays(10));
    }
  }
}
