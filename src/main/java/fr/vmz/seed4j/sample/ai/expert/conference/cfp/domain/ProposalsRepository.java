package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import java.util.List;
import java.util.Optional;

public interface ProposalsRepository {
  void save(Proposal proposal);

  Optional<Proposal> get(ProposalId proposal);

  List<Proposal> all();

  default Proposal proposal(ProposalId proposal) {
    return get(proposal).orElseThrow(() -> new UnknownProposalException(proposal));
  }
}
