package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.secondary.loan;

import static fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoansFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoanId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryLoansRepositoryTest {

  private final InMemoryLoansRepository loans = new InMemoryLoansRepository();

  @Test
  void shouldGetLoansOfMember() {
    var jane = MemberId.newId();
    var janes = loan(jane, EFFECTIVE_JAVA, TODAY);
    loans.save(janes);
    loans.save(loan(MemberId.newId(), DDD, TODAY));

    assertThat(loans.get(janes.id())).contains(janes);
    assertThat(loans.get(LoanId.newId())).isEmpty();
    assertThat(loans.of(jane)).containsExactly(janes);
  }
}
