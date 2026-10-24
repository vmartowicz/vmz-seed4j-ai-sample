package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.Customer;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.CustomerName;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.MobilePhone;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.catalog.ServiceId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestBookingToCreate(@Nullable UUID serviceId, @Nullable LocalDateTime start, @Nullable String customerName, @Nullable String phone) {
  ServiceId service() {
    return new ServiceId(serviceId);
  }

  Customer customer() {
    return new Customer(new CustomerName(customerName), new MobilePhone(phone));
  }
}
