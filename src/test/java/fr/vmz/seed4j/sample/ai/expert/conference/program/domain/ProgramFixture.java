package fr.vmz.seed4j.sample.ai.expert.conference.program.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Edition;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import java.util.UUID;

public final class ProgramFixture {

  private ProgramFixture() {}

  public static TalkToSchedule talk(Track track, String... speakers) {
    return talk(SessionFormat.TALK, track, false, speakers);
  }

  public static TalkToSchedule talk(SessionFormat format, Track track, boolean fromAbroad, String... speakers) {
    return new TalkToSchedule(
      UUID.randomUUID(),
      "A talk about " + track,
      format,
      track,
      List.of(speakers).stream().map(speaker -> new Email(speaker + "@mail.com")).toList(),
      fromAbroad
    );
  }

  public static Slot dayOne(int number) {
    return new Slot(Edition.FIRST_DAY, number);
  }

  public static Slot dayTwo(int number) {
    return new Slot(Edition.SECOND_DAY, number);
  }
}
