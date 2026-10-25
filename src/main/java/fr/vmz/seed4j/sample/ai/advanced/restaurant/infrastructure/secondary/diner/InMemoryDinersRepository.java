package fr.vmz.seed4j.sample.ai.advanced.restaurant.infrastructure.secondary.diner;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Diner;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerEmail;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinersRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryDinersRepository implements DinersRepository {

  private final Map<DinerEmail, Diner> diners = new ConcurrentHashMap<>();

  @Override
  public void save(Diner diner) {
    diners.put(diner.email(), diner);
  }

  @Override
  public Optional<Diner> get(DinerEmail email) {
    return Optional.ofNullable(diners.get(email));
  }
}
