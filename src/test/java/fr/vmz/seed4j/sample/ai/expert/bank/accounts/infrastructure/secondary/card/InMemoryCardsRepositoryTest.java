package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.secondary.card;

import static fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.Card;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardId;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardType;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryCardsRepositoryTest {

  private final InMemoryCardsRepository cards = new InMemoryCardsRepository();

  @Test
  void shouldGetSavedCard() {
    var card = Card.issue(current("1000"), CardType.CLASSIC);

    cards.save(card);

    assertThat(cards.get(card.id())).containsSame(card);
    assertThat(cards.get(CardId.newId())).isEmpty();
  }
}
