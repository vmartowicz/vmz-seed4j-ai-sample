package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.secondary.vet;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.ClinicPage;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.FirstName;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.LastName;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.Pagination;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet.Specialty;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet.Vet;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet.VetId;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet.VetsRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryVetsRepository implements VetsRepository {

  private static final Comparator<Vet> BY_LAST_NAME = Comparator.comparing(vet -> vet.lastName().get());

  private final List<Vet> vets = Stream.of(
    vet("James", "Carter"),
    vet("Helen", "Leary", Specialty.RADIOLOGY),
    vet("Linda", "Douglas", Specialty.SURGERY, Specialty.DENTISTRY),
    vet("Rafael", "Ortega", Specialty.SURGERY),
    vet("Henry", "Stevens", Specialty.RADIOLOGY),
    vet("Sharon", "Jenkins")
  )
    .sorted(BY_LAST_NAME)
    .toList();

  private static Vet vet(String firstName, String lastName, Specialty... specialties) {
    return Vet.builder()
      .id(VetId.newId())
      .firstName(new FirstName(firstName))
      .lastName(new LastName(lastName))
      .specialties(specialties)
      .build();
  }

  @Override
  public ClinicPage<Vet> list(Pagination pagination) {
    return ClinicPage.of(vets, pagination);
  }
}
