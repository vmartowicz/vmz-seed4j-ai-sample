package fr.vmz.seed4j.sample.ai.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.clinic.domain.owner.PetType;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pet-types")
class PetTypesResource {

  @GetMapping
  List<RestPetType> list() {
    return Stream.of(PetType.values()).map(RestPetType::from).toList();
  }
}
