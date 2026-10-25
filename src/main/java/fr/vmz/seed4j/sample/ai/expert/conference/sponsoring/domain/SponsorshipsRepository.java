package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain;

import java.util.List;
import java.util.Optional;

public interface SponsorshipsRepository {
  void save(Sponsorship sponsorship);

  Optional<Sponsorship> get(SponsorId sponsorship);

  List<Sponsorship> all();

  default Sponsorship sponsorship(SponsorId sponsorship) {
    return get(sponsorship).orElseThrow(() -> new UnknownSponsorshipException(sponsorship));
  }
}
