package fr.vmz.seed4j.sample.ai.clinic.domain.vet;

import fr.vmz.seed4j.sample.ai.clinic.domain.ClinicPage;
import fr.vmz.seed4j.sample.ai.clinic.domain.Pagination;

public interface VetsRepository {
  ClinicPage<Vet> list(Pagination pagination);
}
