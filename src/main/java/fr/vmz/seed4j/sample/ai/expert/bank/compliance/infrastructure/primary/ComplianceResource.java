package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.compliance.application.ComplianceApplicationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/retail-bank/compliance")
class ComplianceResource {

  private final ComplianceApplicationService compliance;

  ComplianceResource(ComplianceApplicationService compliance) {
    this.compliance = compliance;
  }

  @PostMapping("/sanctions")
  @ResponseStatus(HttpStatus.CREATED)
  RestSanction addSanction(@RequestBody RestSanction sanction) {
    return RestSanction.from(compliance.addSanction(sanction.name()));
  }

  @GetMapping("/alerts")
  List<RestAlert> alerts() {
    return compliance.alerts().stream().map(RestAlert::from).toList();
  }
}
