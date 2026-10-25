package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.secondary.cfp;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary.JavaConfirmedTalks;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.ConfirmedTalks;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.SessionFormat;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.TalkToSchedule;
import fr.vmz.seed4j.sample.ai.expert.conference.program.domain.Track;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class ProgramConfirmedTalks implements ConfirmedTalks {

  private final JavaConfirmedTalks talks;

  // Lazy: the call for papers tells the program about withdrawals, the contexts depend on each other
  ProgramConfirmedTalks(@Lazy JavaConfirmedTalks talks) {
    this.talks = talks;
  }

  @Override
  public Optional<TalkToSchedule> talk(UUID talk) {
    return talks
      .talk(talk)
      .map(confirmed ->
        new TalkToSchedule(
          confirmed.id(),
          confirmed.title(),
          SessionFormat.valueOf(confirmed.format()),
          Track.valueOf(confirmed.track()),
          confirmed.speakers().stream().map(Email::new).toList(),
          confirmed.speakerFromAbroad()
        )
      );
  }
}
