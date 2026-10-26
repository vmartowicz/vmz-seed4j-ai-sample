package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.ItemKind;
import java.time.LocalDate;

record RestPrescriptionItem(ItemKind kind, String code, LocalDate validUntil) {}
