package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Pet {

  private final PetId id;
  private final List<Visit> visits = new ArrayList<>();
  private PetInformation information;

  Pet(PetId id, PetInformation information) {
    Assert.notNull("id", id);
    Assert.notNull("information", information);

    this.id = id;
    this.information = information;
  }

  void update(PetInformation information) {
    this.information = information;
  }

  Visit schedule(VisitToSchedule visit) {
    var scheduled = new Visit(VisitId.newId(), visit.date(), visit.description());
    visits.add(scheduled);

    return scheduled;
  }

  public PetId id() {
    return id;
  }

  public PetName name() {
    return information.name();
  }

  public LocalDate birthDate() {
    return information.birthDate();
  }

  public PetType type() {
    return information.type();
  }

  public List<Visit> visits() {
    return visits.stream().sorted(Comparator.comparing(Visit::date)).toList();
  }
}
