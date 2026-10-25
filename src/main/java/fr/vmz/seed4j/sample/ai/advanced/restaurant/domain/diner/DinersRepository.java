package fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner;

import java.util.Optional;

public interface DinersRepository {
  void save(Diner diner);

  Optional<Diner> get(DinerEmail email);

  default Diner diner(DinerEmail email) {
    return get(email).orElseThrow(() -> new UnknownDinerException(email));
  }
}
