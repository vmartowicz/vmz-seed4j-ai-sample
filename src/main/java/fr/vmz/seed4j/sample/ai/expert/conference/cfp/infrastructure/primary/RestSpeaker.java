package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SpeakerProfile;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import org.jspecify.annotations.Nullable;

record RestSpeaker(@Nullable String email, @Nullable String name, @Nullable String company, boolean fromAbroad, boolean spokeBefore) {
  static RestSpeaker from(SpeakerProfile speaker) {
    return new RestSpeaker(speaker.email().get(), speaker.name(), speaker.company(), speaker.fromAbroad(), speaker.spokeBefore());
  }

  SpeakerProfile toDomain() {
    return new SpeakerProfile(new Email(email), name, company, fromAbroad, spokeBefore);
  }
}
