package fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.time.LocalDateTime;

public record ComplianceAlert(Iban account, String reason, LocalDateTime at) {}
