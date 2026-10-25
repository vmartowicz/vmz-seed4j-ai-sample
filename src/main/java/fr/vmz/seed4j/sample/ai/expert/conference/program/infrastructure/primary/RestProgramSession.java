package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramSession;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.SessionFormat;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.Track;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Room;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

record RestProgramSession(
  UUID talkId,
  String title,
  SessionFormat format,
  Track track,
  List<String> speakers,
  Room room,
  LocalDateTime start,
  LocalDateTime end
) {
  static RestProgramSession from(ProgramSession session) {
    var talk = session.talk();

    return new RestProgramSession(
      talk.talk(),
      talk.title(),
      talk.format(),
      talk.track(),
      talk.speakers().stream().map(Email::get).toList(),
      session.room(),
      session.start(),
      session.end()
    );
  }
}
