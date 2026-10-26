package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.math.BigDecimal;
import java.time.LocalDate;

public record Borrower(CustomerId id, LocalDate birthDate, BigDecimal monthlyIncome, LocalDate customerSince) {}
