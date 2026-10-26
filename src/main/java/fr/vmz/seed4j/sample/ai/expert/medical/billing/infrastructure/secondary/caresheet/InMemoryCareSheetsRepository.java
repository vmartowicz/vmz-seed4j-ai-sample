package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.secondary.caresheet;

import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheet;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheetId;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheetsRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryCareSheetsRepository implements CareSheetsRepository {

  private final Map<CareSheetId, CareSheet> sheets = new ConcurrentHashMap<>();

  @Override
  public void save(CareSheet sheet) {
    sheets.put(sheet.id(), sheet);
  }

  @Override
  public Optional<CareSheet> get(CareSheetId sheet) {
    return Optional.ofNullable(sheets.get(sheet));
  }

  @Override
  public List<CareSheet> all() {
    return List.copyOf(sheets.values());
  }
}
