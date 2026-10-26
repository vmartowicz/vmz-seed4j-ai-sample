package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Entry(LocalDateTime at, BigDecimal amount, String label) {}
