package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ProgramSession;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.SessionFormat;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.List;
import java.util.UUID;

public record JavaSession(UUID talk, String title, boolean workshop, List<String> speakers) {
  static JavaSession from(ProgramSession session) {
    var talk = session.talk();

    return new JavaSession(talk.talk(), talk.title(), talk.format() == SessionFormat.WORKSHOP, talk.speakers().stream().map(Email::get).toList());
  }
}
