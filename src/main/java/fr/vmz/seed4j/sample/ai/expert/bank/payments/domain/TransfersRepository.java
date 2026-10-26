package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import java.util.List;
import java.util.Optional;

public interface TransfersRepository {
  void save(Transfer transfer);

  Optional<Transfer> get(TransferId transfer);

  List<Transfer> all();

  default Transfer transferOf(TransferId transfer) {
    return get(transfer).orElseThrow(() -> new UnknownTransferException(transfer));
  }
}
