package fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain;

import java.util.List;

public interface SanctionsRepository {
  void save(SanctionedName name);

  List<SanctionedName> all();
}
