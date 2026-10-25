package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProposalToSubmit(String title, String summary, Format format, Level level, Track track, List<SpeakerProfile> speakers) {
  public ProposalToSubmit {
    Assert.field("title", title).notBlank().maxLength(120);
    Assert.field("summary", summary).notBlank().maxLength(2000);
    Assert.notNull("format", format);
    Assert.notNull("level", level);
    Assert.notNull("track", track);
    Assert.field("speakers", speakers).notEmpty().maxSize(2).noNullElement();

    if (speakers.size() == 2 && speakers.getFirst().email().equals(speakers.getLast().email())) {
      throw new DuplicatedSpeakerException(speakers.getFirst().email());
    }

    speakers = List.copyOf(speakers);
  }

  public Proposal submit(LocalDateTime now) {
    return new Proposal(this, now, null);
  }

  public Proposal submitSponsored(UUID sponsor, LocalDateTime now) {
    if (format != Format.TALK) {
      throw new SponsoredTalkFormatException(format);
    }

    return new Proposal(this, now, sponsor);
  }
}
