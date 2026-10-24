package fr.vmz.seed4j.sample.ai.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.clinic.application.VetsApplicationService;
import fr.vmz.seed4j.sample.ai.clinic.domain.Pagination;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vets")
class VetsResource {

  private final VetsApplicationService vets;

  VetsResource(VetsApplicationService vets) {
    this.vets = vets;
  }

  @GetMapping
  RestClinicPage<RestVet> list(@RequestParam(defaultValue = "1") int page) {
    return RestClinicPage.from(vets.list(new Pagination(page)), RestVet::from);
  }
}
