package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.application.MedicalBillingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheetId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medical/care-sheets")
class CareSheetsResource {

  private final MedicalBillingApplicationService billing;

  CareSheetsResource(MedicalBillingApplicationService billing) {
    this.billing = billing;
  }

  @GetMapping
  List<RestCareSheet> careSheets(@RequestParam UUID patient) {
    return billing.careSheetsOf(new PatientId(patient)).stream().map(RestCareSheet::from).toList();
  }

  @GetMapping("/{careSheetId}")
  RestCareSheet careSheet(@PathVariable UUID careSheetId) {
    return RestCareSheet.from(billing.careSheet(new CareSheetId(careSheetId)));
  }
}
