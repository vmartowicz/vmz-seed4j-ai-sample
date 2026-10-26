package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CriticalAlert(SampleId sample, PatientId patient, LabAnalysis test, BigDecimal value, DoctorId recipient, LocalDateTime at) {}
