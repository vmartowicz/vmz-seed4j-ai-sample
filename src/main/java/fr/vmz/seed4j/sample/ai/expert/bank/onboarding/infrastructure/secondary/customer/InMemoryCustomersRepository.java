package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.secondary.customer;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.Customer;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.CustomersRepository;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryCustomersRepository implements CustomersRepository {

  private final Map<CustomerId, Customer> customers = new ConcurrentHashMap<>();

  @Override
  public void save(Customer customer) {
    customers.put(customer.id(), customer);
  }

  @Override
  public Optional<Customer> get(CustomerId customer) {
    return Optional.ofNullable(customers.get(customer));
  }

  @Override
  public List<Customer> all() {
    return List.copyOf(customers.values());
  }
}
