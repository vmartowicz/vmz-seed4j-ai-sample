package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.clinic.domain.Pagination;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.Comparator;
import java.util.Locale;

public record OwnersSearch(String lastNamePrefix, Pagination pagination) {
  public static final Comparator<Owner> ORDER = Comparator.<Owner, String>comparing(
    owner -> owner.information().lastName().get(),
    String.CASE_INSENSITIVE_ORDER
  ).thenComparing(owner -> owner.information().firstName().get(), String.CASE_INSENSITIVE_ORDER);

  public OwnersSearch {
    Assert.notNull("lastNamePrefix", lastNamePrefix);
    Assert.notNull("pagination", pagination);

    lastNamePrefix = lastNamePrefix.trim().toLowerCase(Locale.ROOT);
  }

  public boolean matches(Owner owner) {
    return owner.information().lastName().get().toLowerCase(Locale.ROOT).startsWith(lastNamePrefix);
  }
}
