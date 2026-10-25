package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Edition;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProgramBoard {

  private static final LocalTime FOREIGN_SPEAKERS_ARRIVAL = LocalTime.of(11, 0);
  private static final Comparator<ProgramSession> BY_START_THEN_ROOM = Comparator.comparing(ProgramSession::start).thenComparing(
    ProgramSession::room
  );

  private final ProgramSessionsRepository sessions;
  private final ConfirmedTalks talks;

  public ProgramBoard(ProgramSessionsRepository sessions, ConfirmedTalks talks) {
    this.sessions = sessions;
    this.talks = talks;
  }

  public ProgramSession place(UUID talkId, Room room, Slot first) {
    var talk = talks.talk(talkId).orElseThrow(() -> new UnknownConfirmedTalkException(talkId));
    if (sessions.get(talkId).isPresent()) {
      throw new TalkAlreadyScheduledException(talkId);
    }

    if (first.isKeynote()) {
      throw new KeynoteSlotException(first);
    }

    if (talk.isWorkshop() != (room == Room.FOURVIERE)) {
      throw new WorkshopRoomException(room);
    }

    var slots = talk.isWorkshop() ? List.of(first, first.next().orElseThrow(() -> new WorkshopSlotsException(first))) : List.of(first);
    if (talk.speakerFromAbroad() && first.day().equals(Edition.FIRST_DAY) && first.start().toLocalTime().isBefore(FOREIGN_SPEAKERS_ARRIVAL)) {
      throw new SpeakerArrivesLateException(first);
    }

    var session = new ProgramSession(talk, room, slots);
    sessions.all().forEach(other -> assertCompatible(session, other));
    sessions.save(session);

    return session;
  }

  public void withdraw(UUID talk) {
    sessions.delete(talk);
  }

  public List<ProgramSession> day(LocalDate day) {
    return sessions
      .all()
      .stream()
      .filter(session -> session.start().toLocalDate().equals(day))
      .sorted(BY_START_THEN_ROOM)
      .toList();
  }

  public Optional<ProgramSession> sessionAt(Room room, LocalDateTime at) {
    return sessions
      .all()
      .stream()
      .filter(session -> session.room() == room && session.covers(at))
      .findFirst();
  }

  private static void assertCompatible(ProgramSession session, ProgramSession other) {
    var shared = session.slots().stream().filter(other::uses).findFirst();
    if (shared.isEmpty()) {
      return;
    }

    var slot = shared.orElseThrow();
    if (other.room() == session.room()) {
      throw new RoomBusyException(session.room(), slot);
    }

    if (other.talk().track() == session.talk().track()) {
      throw new TrackClashException(session.talk().track(), slot);
    }

    session
      .talk()
      .speakers()
      .stream()
      .filter(other.talk().speakers()::contains)
      .findFirst()
      .ifPresent(speaker -> {
        throw new SpeakerBusyException(speaker, slot);
      });
  }
}
