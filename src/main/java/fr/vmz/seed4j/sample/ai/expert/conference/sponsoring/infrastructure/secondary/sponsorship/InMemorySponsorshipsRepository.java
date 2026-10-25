package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.secondary.sponsorship;

import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorId;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.Sponsorship;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorshipsRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemorySponsorshipsRepository implements SponsorshipsRepository {

  private final Map<SponsorId, Sponsorship> sponsorships = new ConcurrentHashMap<>();

  @Override
  public void save(Sponsorship sponsorship) {
    sponsorships.put(sponsorship.id(), sponsorship);
  }

  @Override
  public Optional<Sponsorship> get(SponsorId sponsorship) {
    return Optional.ofNullable(sponsorships.get(sponsorship));
  }

  @Override
  public List<Sponsorship> all() {
    return List.copyOf(sponsorships.values());
  }
}
