package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestLoanToCreate(@Nullable UUID memberId, @Nullable String isbn) {
  MemberId member() {
    return new MemberId(memberId);
  }

  Isbn book() {
    return new Isbn(isbn);
  }
}
