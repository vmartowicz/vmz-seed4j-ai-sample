package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Places;

record RestPlaces(int talks, int quickies, int workshops) {
  Places toDomain() {
    return new Places(talks, quickies, workshops);
  }
}
