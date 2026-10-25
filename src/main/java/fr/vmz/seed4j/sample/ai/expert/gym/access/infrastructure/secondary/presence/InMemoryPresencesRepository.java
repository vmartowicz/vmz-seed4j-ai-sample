package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.secondary.presence;

import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Presence;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.PresencesRepository;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryPresencesRepository implements PresencesRepository {

  private final Map<UUID, Presence> presences = new ConcurrentHashMap<>();

  @Override
  public void save(Presence presence) {
    presences.put(presence.id(), presence);
  }

  @Override
  public Optional<Presence> openOf(MemberId member) {
    return presences
      .values()
      .stream()
      .filter(Presence::isOpen)
      .filter(presence -> presence.member().equals(member))
      .findFirst();
  }

  @Override
  public List<Presence> openIn(Gym gym) {
    return presences
      .values()
      .stream()
      .filter(Presence::isOpen)
      .filter(presence -> presence.gym() == gym)
      .toList();
  }
}
