package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.proposal;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Proposal;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalId;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalsRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryProposalsRepository implements ProposalsRepository {

  private final Map<ProposalId, Proposal> proposals = new ConcurrentHashMap<>();

  @Override
  public void save(Proposal proposal) {
    proposals.put(proposal.id(), proposal);
  }

  @Override
  public Optional<Proposal> get(ProposalId proposal) {
    return Optional.ofNullable(proposals.get(proposal));
  }

  @Override
  public List<Proposal> all() {
    return List.copyOf(proposals.values());
  }
}
