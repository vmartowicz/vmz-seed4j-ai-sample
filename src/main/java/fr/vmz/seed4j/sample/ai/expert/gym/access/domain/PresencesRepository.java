package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.List;
import java.util.Optional;

public interface PresencesRepository {
  void save(Presence presence);

  Optional<Presence> openOf(MemberId member);

  List<Presence> openIn(Gym gym);
}
