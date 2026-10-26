package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.application.MedicalRecordsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ConsultationId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
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
@RequestMapping("/api/medical/consultations")
class ConsultationsResource {

  private final MedicalRecordsApplicationService records;

  ConsultationsResource(MedicalRecordsApplicationService records) {
    this.records = records;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestConsultation open(@RequestBody RestConsultationToOpen consultation) {
    return RestConsultation.from(
      records.open(new PatientId(consultation.patientId()), new DoctorId(consultation.doctorId()), consultation.reason())
    );
  }

  @GetMapping("/{consultationId}")
  RestConsultation consultation(@PathVariable UUID consultationId) {
    return RestConsultation.from(records.consultation(new ConsultationId(consultationId)));
  }

  @PostMapping("/{consultationId}/notes")
  RestConsultation write(@PathVariable UUID consultationId, @RequestBody RestNotes notes) {
    return RestConsultation.from(records.write(new ConsultationId(consultationId), notes.examination(), notes.diagnosis()));
  }

  @PostMapping("/{consultationId}/prescription")
  @ResponseStatus(HttpStatus.CREATED)
  RestPrescription prescribe(@PathVariable UUID consultationId, @RequestBody RestPrescriptionToWrite prescription) {
    return RestPrescription.from(records.prescribe(new ConsultationId(consultationId), prescription.toDomain()));
  }

  @PostMapping("/{consultationId}/closing")
  RestConsultation close(@PathVariable UUID consultationId) {
    return RestConsultation.from(records.close(new ConsultationId(consultationId)));
  }

  @PostMapping("/{consultationId}/addenda")
  RestConsultation addAddendum(@PathVariable UUID consultationId, @RequestBody RestAddendumToWrite addendum) {
    return RestConsultation.from(records.addAddendum(new ConsultationId(consultationId), new DoctorId(addendum.doctorId()), addendum.text()));
  }
}
