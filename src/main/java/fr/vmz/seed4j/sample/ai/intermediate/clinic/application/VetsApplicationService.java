package fr.vmz.seed4j.sample.ai.intermediate.clinic.application;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.ClinicPage;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.Pagination;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet.Vet;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet.VetsRepository;
import org.springframework.stereotype.Service;

@Service
public class VetsApplicationService {

  private final VetsRepository vets;

  public VetsApplicationService(VetsRepository vets) {
    this.vets = vets;
  }

  public ClinicPage<Vet> list(Pagination pagination) {
    return vets.list(pagination);
  }
}
