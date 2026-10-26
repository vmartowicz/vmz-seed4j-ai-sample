package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

import java.util.List;
import java.util.Optional;

public interface CareSheetsRepository {
  void save(CareSheet sheet);

  Optional<CareSheet> get(CareSheetId sheet);

  List<CareSheet> all();

  default CareSheet careSheetOf(CareSheetId sheet) {
    return get(sheet).orElseThrow(() -> new UnknownCareSheetException(sheet));
  }
}
