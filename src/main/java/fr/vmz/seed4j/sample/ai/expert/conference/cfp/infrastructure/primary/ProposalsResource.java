package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.application.CallForPapersApplicationService;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Format;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalId;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conference/cfp")
class ProposalsResource {

  private final CallForPapersApplicationService cfp;

  ProposalsResource(CallForPapersApplicationService cfp) {
    this.cfp = cfp;
  }

  @PostMapping("/proposals")
  @ResponseStatus(HttpStatus.CREATED)
  RestProposal submit(@RequestBody RestProposalToSubmit proposal) {
    return RestProposal.from(cfp.submit(proposal.toDomain()));
  }

  @PostMapping("/sponsored-proposals")
  @ResponseStatus(HttpStatus.CREATED)
  RestProposal submitSponsored(@RequestBody RestProposalToSubmit proposal) {
    return RestProposal.from(cfp.submitSponsored(proposal.toDomain(), proposal.sponsorId()));
  }

  @GetMapping("/proposals/{proposalId}")
  RestProposal proposal(@PathVariable UUID proposalId) {
    return RestProposal.from(cfp.proposal(new ProposalId(proposalId)));
  }

  @PostMapping("/proposals/{proposalId}/ratings")
  RestProposal rate(@PathVariable UUID proposalId, @RequestBody RestRating rating) {
    return RestProposal.from(cfp.rate(new ProposalId(proposalId), rating.member(), rating.score()));
  }

  @PostMapping("/proposals/{proposalId}/confirmation")
  RestProposal confirm(@PathVariable UUID proposalId) {
    return RestProposal.from(cfp.confirm(new ProposalId(proposalId)));
  }

  @PostMapping("/proposals/{proposalId}/withdrawal")
  RestProposal withdraw(@PathVariable UUID proposalId) {
    return RestProposal.from(cfp.withdraw(new ProposalId(proposalId)));
  }

  @PostMapping("/committee-members")
  @ResponseStatus(HttpStatus.CREATED)
  RestCommitteeMember register(@RequestBody RestCommitteeMember member) {
    return RestCommitteeMember.from(cfp.register(member.toDomain()));
  }

  @PostMapping("/selection")
  List<RestProposal> select(@RequestBody RestPlaces places) {
    return cfp.select(places.toDomain()).stream().map(RestProposal::from).toList();
  }

  @GetMapping("/waiting-list")
  List<RestProposal> waitingList(@RequestParam Format format) {
    return cfp.waitingList(format).stream().map(RestProposal::from).toList();
  }
}
