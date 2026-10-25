package fr.vmz.seed4j.sample.ai.advanced.shop.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.shop.domain.article.RestockQuantity;

record RestRestock(int quantity) {
  RestockQuantity toDomain() {
    return new RestockQuantity(quantity);
  }
}
