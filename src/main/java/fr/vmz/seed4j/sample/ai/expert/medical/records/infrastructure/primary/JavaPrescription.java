package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record JavaPrescription(UUID patient, UUID prescriber, LocalDate issuedOn, List<String> labTests, List<String> imagingExams) {}
