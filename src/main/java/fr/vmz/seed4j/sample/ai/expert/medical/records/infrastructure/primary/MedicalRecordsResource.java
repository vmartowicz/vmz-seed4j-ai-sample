package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.application.MedicalRecordsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.MedicalRecord;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medical/records/{patientId}")
class MedicalRecordsResource {

  private final MedicalRecordsApplicationService records;

  MedicalRecordsResource(MedicalRecordsApplicationService records) {
    this.records = records;
  }

  @GetMapping
  RestMedicalRecord record(@PathVariable UUID patientId) {
    return RestMedicalRecord.from(records.record(new PatientId(patientId)));
  }

  @PostMapping("/allergies")
  RestMedicalRecord addAllergy(@PathVariable UUID patientId, @RequestBody RestText allergy) {
    return update(patientId, record -> record.addAllergy(allergy.text()));
  }

  @PostMapping("/history")
  RestMedicalRecord addHistory(@PathVariable UUID patientId, @RequestBody RestText history) {
    return update(patientId, record -> record.addHistory(history.text()));
  }

  @PostMapping("/treatments")
  RestMedicalRecord addTreatment(@PathVariable UUID patientId, @RequestBody RestText treatment) {
    return update(patientId, record -> record.addTreatment(treatment.text()));
  }

  @PostMapping("/pacemaker")
  RestMedicalRecord declarePacemaker(@PathVariable UUID patientId) {
    return update(patientId, MedicalRecord::declarePacemaker);
  }

  private RestMedicalRecord update(UUID patientId, Consumer<MedicalRecord> update) {
    return RestMedicalRecord.from(records.updateRecord(new PatientId(patientId), update));
  }
}
