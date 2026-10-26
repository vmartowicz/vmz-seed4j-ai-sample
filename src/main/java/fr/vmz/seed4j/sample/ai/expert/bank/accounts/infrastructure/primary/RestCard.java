package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.Card;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardStatus;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardType;
import java.util.UUID;

record RestCard(UUID id, String account, CardType type, CardStatus status) {
  static RestCard from(Card card) {
    return new RestCard(card.id().get(), card.account().get(), card.type(), card.status());
  }
}
