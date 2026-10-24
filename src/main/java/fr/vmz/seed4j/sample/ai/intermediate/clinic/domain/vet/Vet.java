package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.vet;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.FirstName;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.LastName;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;

public final class Vet {

  private final VetId id;
  private final FirstName firstName;
  private final LastName lastName;
  private final List<Specialty> specialties;

  private Vet(VetBuilder builder) {
    Assert.notNull("id", builder.id);
    Assert.notNull("firstName", builder.firstName);
    Assert.notNull("lastName", builder.lastName);

    id = builder.id;
    firstName = builder.firstName;
    lastName = builder.lastName;
    specialties = builder.specialties.stream().distinct().sorted().toList();
  }

  public static VetBuilder builder() {
    return new VetBuilder();
  }

  public VetId id() {
    return id;
  }

  public FirstName firstName() {
    return firstName;
  }

  public LastName lastName() {
    return lastName;
  }

  public List<Specialty> specialties() {
    return specialties;
  }

  public static final class VetBuilder {

    private @Nullable VetId id;
    private @Nullable FirstName firstName;
    private @Nullable LastName lastName;
    private List<Specialty> specialties = List.of();

    private VetBuilder() {}

    public VetBuilder id(VetId id) {
      this.id = id;

      return this;
    }

    public VetBuilder firstName(FirstName firstName) {
      this.firstName = firstName;

      return this;
    }

    public VetBuilder lastName(LastName lastName) {
      this.lastName = lastName;

      return this;
    }

    public VetBuilder specialties(Specialty... specialties) {
      this.specialties = Stream.of(specialties).toList();

      return this;
    }

    public Vet build() {
      return new Vet(this);
    }
  }
}
