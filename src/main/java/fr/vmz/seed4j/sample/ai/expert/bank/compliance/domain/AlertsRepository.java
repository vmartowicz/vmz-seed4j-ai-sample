package fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain;

import java.util.List;

public interface AlertsRepository {
  void save(ComplianceAlert alert);

  List<ComplianceAlert> all();
}
