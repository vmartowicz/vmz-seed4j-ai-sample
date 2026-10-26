package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.List;
import java.util.Optional;

public interface CustomersRepository {
  void save(Customer customer);

  Optional<Customer> get(CustomerId customer);

  List<Customer> all();

  default Customer customer(CustomerId customer) {
    return get(customer).orElseThrow(() -> new UnknownCustomerException(customer));
  }
}
