package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import java.util.UUID;

public record JavaInsurance(UUID insured, boolean referringDoctorDeclared, boolean longTermIllness, boolean solidarityHealthCover) {}
