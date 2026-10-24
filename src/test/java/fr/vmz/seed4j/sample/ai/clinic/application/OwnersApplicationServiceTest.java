package fr.vmz.seed4j.sample.ai.clinic.application;

import static fr.vmz.seed4j.sample.ai.clinic.domain.owner.OwnersFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.clinic.domain.owner.Owner;
import fr.vmz.seed4j.sample.ai.clinic.domain.owner.OwnerId;
import fr.vmz.seed4j.sample.ai.clinic.domain.owner.OwnersRepository;
import fr.vmz.seed4j.sample.ai.clinic.domain.owner.UnknownOwnerException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class OwnersApplicationServiceTest {

  // 2026-03-10 at 23:30 UTC is already 2026-03-11 in Paris
  private static final Clock LATE_EVENING_IN_UTC = Clock.fixed(Instant.parse("2026-03-10T23:30:00Z"), ZoneId.of("Europe/Paris"));

  private final OwnersRepository owners = mock(OwnersRepository.class);
  private final OwnersApplicationService service = new OwnersApplicationService(owners, LATE_EVENING_IN_UTC);

  @Test
  void shouldSaveRegisteredOwner() {
    var owner = service.register(ownerInformation());

    verify(owners).save(owner);
  }

  @Test
  void shouldNotGetUnknownOwner() {
    var unknownOwner = OwnerId.newId();
    when(owners.get(unknownOwner)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.get(unknownOwner))
      .isExactlyInstanceOf(UnknownOwnerException.class)
      .hasMessageContaining(unknownOwner.get().toString());
  }

  @Test
  void shouldUseTodayInParisToScheduleVisits() {
    var owner = ownerWithLeo();
    var leo = owner.pets().getFirst();

    assertThatThrownBy(() -> service.scheduleVisit(owner.id(), leo.id(), visitOn(LocalDate.of(2026, 3, 11)))).hasMessageContaining(
      "2026-03-11"
    );
    assertThat(service.scheduleVisit(owner.id(), leo.id(), visitOn(LocalDate.of(2026, 3, 12))).date()).isEqualTo(LocalDate.of(2026, 3, 12));
  }

  private Owner ownerWithLeo() {
    var owner = owner();
    owner.addPet(leo(), TODAY);
    when(owners.get(owner.id())).thenReturn(Optional.of(owner));

    return owner;
  }
}
