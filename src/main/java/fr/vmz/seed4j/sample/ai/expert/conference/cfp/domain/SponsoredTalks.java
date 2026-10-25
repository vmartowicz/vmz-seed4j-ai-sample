package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import java.util.UUID;

public interface SponsoredTalks {
  boolean claim(UUID sponsor);
}
