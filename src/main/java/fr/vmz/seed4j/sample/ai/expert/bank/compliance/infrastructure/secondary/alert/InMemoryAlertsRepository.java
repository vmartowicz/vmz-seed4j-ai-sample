package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.secondary.alert;

import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.AlertsRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.ComplianceAlert;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryAlertsRepository implements AlertsRepository {

  private final List<ComplianceAlert> alerts = new CopyOnWriteArrayList<>();

  @Override
  public void save(ComplianceAlert alert) {
    alerts.add(alert);
  }

  @Override
  public List<ComplianceAlert> all() {
    return List.copyOf(alerts);
  }
}
