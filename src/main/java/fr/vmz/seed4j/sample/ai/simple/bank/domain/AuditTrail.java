package fr.vmz.seed4j.sample.ai.simple.bank.domain;

public interface AuditTrail {
  void record(AuditEvent event);
}
