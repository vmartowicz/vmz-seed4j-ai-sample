package fr.vmz.seed4j.sample.ai.advanced.restaurant.application;

import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.Diner;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinerEmail;
import fr.vmz.seed4j.sample.ai.advanced.restaurant.domain.diner.DinersRepository;
import java.time.Clock;
import java.time.Year;
import org.springframework.stereotype.Service;

@Service
public class DinersApplicationService {

  private final DinersRepository diners;
  private final Clock clock;

  public DinersApplicationService(DinersRepository diners, Clock clock) {
    this.diners = diners;
    this.clock = clock;
  }

  public Diner diner(DinerEmail email) {
    return diners.diner(email);
  }

  public Year currentYear() {
    return Year.now(clock);
  }
}
