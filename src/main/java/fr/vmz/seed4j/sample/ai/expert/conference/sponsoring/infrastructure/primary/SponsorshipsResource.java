package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.application.SponsoringApplicationService;
import fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.domain.SponsorId;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conference/sponsorships")
class SponsorshipsResource {

  private final SponsoringApplicationService sponsoring;

  SponsorshipsResource(SponsoringApplicationService sponsoring) {
    this.sponsoring = sponsoring;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestSponsorship sign(@RequestBody RestSponsorshipToSign sponsorship) {
    return RestSponsorship.from(sponsoring.sign(sponsorship.toDomain()));
  }

  @GetMapping("/{sponsorshipId}")
  RestSponsorship sponsorship(@PathVariable UUID sponsorshipId) {
    return RestSponsorship.from(sponsoring.sponsorship(new SponsorId(sponsorshipId)));
  }

  @PostMapping("/{sponsorshipId}/payment")
  RestSponsorship pay(@PathVariable UUID sponsorshipId) {
    return RestSponsorship.from(sponsoring.pay(new SponsorId(sponsorshipId)));
  }
}
