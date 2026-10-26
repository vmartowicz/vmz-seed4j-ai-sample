package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import java.util.Optional;

public interface CardsRepository {
  void save(Card card);

  Optional<Card> get(CardId card);

  default Card card(CardId card) {
    return get(card).orElseThrow(() -> new UnknownCardException(card));
  }
}
