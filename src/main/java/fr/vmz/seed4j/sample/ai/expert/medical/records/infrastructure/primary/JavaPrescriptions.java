package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.application.MedicalRecordsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ItemKind;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.Prescription;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionId;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaPrescriptions {

  private final MedicalRecordsApplicationService records;

  public JavaPrescriptions(MedicalRecordsApplicationService records) {
    this.records = records;
  }

  public Optional<JavaPrescription> prescription(UUID prescription) {
    return records
      .findPrescription(new PrescriptionId(prescription))
      .map(found ->
        new JavaPrescription(
          found.patient().get(),
          found.prescriber().get(),
          found.issuedOn(),
          codes(found, ItemKind.LAB_TEST),
          codes(found, ItemKind.IMAGING)
        )
      );
  }

  private static List<String> codes(Prescription prescription, ItemKind kind) {
    return prescription
      .items()
      .stream()
      .filter(item -> item.kind() == kind)
      .map(PrescriptionItem::code)
      .toList();
  }
}
