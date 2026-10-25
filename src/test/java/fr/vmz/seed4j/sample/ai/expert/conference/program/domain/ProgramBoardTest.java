package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Edition;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class ProgramBoardTest {

  private final ProgramSessionsRepository sessions = mock(ProgramSessionsRepository.class);
  private final ConfirmedTalks talks = mock(ConfirmedTalks.class);
  private final ProgramBoard board = new ProgramBoard(sessions, talks);

  private final List<ProgramSession> placed = new ArrayList<>();

  @BeforeEach
  void loadProgram() {
    doReturn(placed).when(sessions).all();
    doReturn(Optional.empty()).when(sessions).get(any());
  }

  @Nested
  class Place {

    @Test
    void shouldPlaceConfirmedTalk() {
      var talk = known(talk(Track.BACK, "jane"));

      var session = board.place(talk.talk(), Room.AMPHI, dayOne(2));

      assertThat(session.talk()).isEqualTo(talk);
      assertThat(session.room()).isEqualTo(Room.AMPHI);
      assertThat(session.slots()).containsExactly(dayOne(2));
      assertThat(session.start()).isEqualTo(dayOne(2).start());
      assertThat(session.end()).isEqualTo(dayOne(2).end());
      verify(sessions).save(session);
    }

    @Test
    void shouldPlaceWorkshopOn2SlotsInFourviere() {
      var workshop = known(talk(SessionFormat.WORKSHOP, Track.OPS, false, "jane"));

      var session = board.place(workshop.talk(), Room.FOURVIERE, dayTwo(5));

      assertThat(session.slots()).containsExactly(dayTwo(5), dayTwo(6));
      assertThat(session.end()).isEqualTo(dayTwo(6).end());
    }

    @Test
    void shouldNotPlaceWorkshopOnLastSlotOfHalfDay() {
      var workshop = known(talk(SessionFormat.WORKSHOP, Track.OPS, false, "jane"));

      assertThatThrownBy(() -> board.place(workshop.talk(), Room.FOURVIERE, dayTwo(4))).isExactlyInstanceOf(WorkshopSlotsException.class);
    }

    @Test
    void shouldPlaceWorkshopsOnlyInFourviere() {
      var workshop = known(talk(SessionFormat.WORKSHOP, Track.OPS, false, "jane"));

      assertThatThrownBy(() -> board.place(workshop.talk(), Room.SAONE, dayTwo(5))).isExactlyInstanceOf(WorkshopRoomException.class);
    }

    @Test
    void shouldKeepFourviereForWorkshops() {
      var quickie = known(talk(SessionFormat.QUICKIE, Track.OPS, false, "jane"));

      assertThatThrownBy(() -> board.place(quickie.talk(), Room.FOURVIERE, dayTwo(5))).isExactlyInstanceOf(WorkshopRoomException.class);
    }

    @Test
    void shouldKeepFirstSlotForKeynote() {
      var talk = known(talk(Track.BACK, "jane"));

      assertThatThrownBy(() -> board.place(talk.talk(), Room.SAONE, dayOne(1))).isExactlyInstanceOf(KeynoteSlotException.class);
    }

    @Test
    void shouldNotScheduleSpeakerFromAbroadBefore11OnFirstDay() {
      var talk = known(talk(SessionFormat.TALK, Track.DATA, true, "jane"));

      assertThatThrownBy(() -> board.place(talk.talk(), Room.SAONE, dayOne(3))).isExactlyInstanceOf(SpeakerArrivesLateException.class);
      assertThat(board.place(talk.talk(), Room.SAONE, dayOne(4)).start()).isEqualTo(dayOne(4).start());
      assertThatCode(() -> board.place(known(talk(SessionFormat.TALK, Track.DATA, true, "john")).talk(), Room.SAONE, dayTwo(1))).doesNotThrowAnyException();
    }

    @Test
    void shouldNotPlaceTalkTwice() {
      var talk = known(talk(Track.BACK, "jane"));
      doReturn(Optional.of(new ProgramSession(talk, Room.AMPHI, List.of(dayOne(2))))).when(sessions).get(talk.talk());

      assertThatThrownBy(() -> board.place(talk.talk(), Room.SAONE, dayOne(3))).isExactlyInstanceOf(TalkAlreadyScheduledException.class);
    }

    @Test
    void shouldNotPlaceUnknownTalk() {
      var unknown = UUID.randomUUID();
      doReturn(Optional.empty()).when(talks).talk(unknown);

      assertThatThrownBy(() -> board.place(unknown, Room.AMPHI, dayOne(2))).isExactlyInstanceOf(UnknownConfirmedTalkException.class);
    }
  }

  @Nested
  class Conflicts {

    @BeforeEach
    void placeBackTalk() {
      placed.add(new ProgramSession(talk(Track.BACK, "jane"), Room.AMPHI, List.of(dayTwo(2))));
      placed.add(new ProgramSession(talk(SessionFormat.WORKSHOP, Track.OPS, false, "ada"), Room.FOURVIERE, List.of(dayTwo(2), dayTwo(3))));
    }

    @Test
    void shouldNotUseBusyRoom() {
      var talk = known(talk(Track.FRONT, "john"));

      assertThatThrownBy(() -> board.place(talk.talk(), Room.AMPHI, dayTwo(2))).isExactlyInstanceOf(RoomBusyException.class);
    }

    @Test
    void shouldNotScheduleSameTrackAtTheSameTime() {
      var talk = known(talk(Track.OPS, "john"));

      assertThatThrownBy(() -> board.place(talk.talk(), Room.SAONE, dayTwo(3))).isExactlyInstanceOf(TrackClashException.class).hasMessageContaining(
        "OPS"
      );
    }

    @Test
    void shouldNotPutSpeakerInTwoRooms() {
      var talk = known(talk(Track.FRONT, "john", "jane"));

      assertThatThrownBy(() -> board.place(talk.talk(), Room.SAONE, dayTwo(2))).isExactlyInstanceOf(SpeakerBusyException.class);
    }

    @Test
    void shouldPlaceOtherTrackWithOtherSpeakersInOtherRoom() {
      var talk = known(talk(Track.FRONT, "john"));

      assertThatCode(() -> board.place(talk.talk(), Room.SAONE, dayTwo(2))).doesNotThrowAnyException();
    }

    @Test
    void shouldReuseRoomOnOtherSlots() {
      var talk = known(talk(Track.BACK, "jane"));

      assertThatCode(() -> board.place(talk.talk(), Room.AMPHI, dayTwo(4))).doesNotThrowAnyException();
    }
  }

  @Test
  void shouldRemoveWithdrawnTalk() {
    var talk = UUID.randomUUID();

    board.withdraw(talk);

    verify(sessions).delete(talk);
  }

  @Test
  void shouldListDayProgramBySlotThenRoom() {
    var afternoon = new ProgramSession(talk(Track.BACK, "a"), Room.AMPHI, List.of(dayTwo(5)));
    var morningSaone = new ProgramSession(talk(Track.FRONT, "b"), Room.SAONE, List.of(dayTwo(2)));
    var morningAmphi = new ProgramSession(talk(Track.DATA, "c"), Room.AMPHI, List.of(dayTwo(2)));
    var firstDay = new ProgramSession(talk(Track.OPS, "d"), Room.AMPHI, List.of(dayOne(2)));
    placed.addAll(List.of(afternoon, morningSaone, firstDay, morningAmphi));

    assertThat(board.day(Edition.SECOND_DAY)).containsExactly(morningAmphi, morningSaone, afternoon);
  }

  @Test
  void shouldFindSessionInRoomAtTime() {
    var workshop = new ProgramSession(talk(SessionFormat.WORKSHOP, Track.OPS, false, "ada"), Room.FOURVIERE, List.of(dayTwo(2), dayTwo(3)));
    var talk = new ProgramSession(talk(Track.BACK, "jane"), Room.AMPHI, List.of(dayTwo(2)));
    placed.addAll(List.of(workshop, talk));

    assertThat(board.sessionAt(Room.FOURVIERE, dayTwo(3).start().plusMinutes(10))).contains(workshop);
    assertThat(board.sessionAt(Room.FOURVIERE, dayTwo(2).start())).contains(workshop);
    assertThat(board.sessionAt(Room.FOURVIERE, dayTwo(3).end())).isEmpty();
    assertThat(board.sessionAt(Room.FOURVIERE, dayTwo(2).start().minusMinutes(1))).isEmpty();
    assertThat(board.sessionAt(Room.SAONE, dayTwo(2).start())).isEmpty();
  }

  private TalkToSchedule known(TalkToSchedule talk) {
    doReturn(Optional.of(talk)).when(talks).talk(talk.talk());

    return talk;
  }
}
