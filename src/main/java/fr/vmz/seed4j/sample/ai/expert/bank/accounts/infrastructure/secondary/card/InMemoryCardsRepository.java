package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.secondary.card;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.Card;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardId;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardsRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryCardsRepository implements CardsRepository {

  private final Map<CardId, Card> cards = new ConcurrentHashMap<>();

  @Override
  public void save(Card card) {
    cards.put(card.id(), card);
  }

  @Override
  public Optional<Card> get(CardId card) {
    return Optional.ofNullable(cards.get(card));
  }
}
