### Example 12 - Expert level
```
I run the "Banque des Canuts", a regional bank with 8 branches around Lyon and about 40,000 customers. Our current system is 20 years old and each department added its own tools on top of it. The result: frozen accounts that keep making transfers, loans granted to customers who have been overdrawn for months, cards blocked by the fraud team without the branch knowing...

I want an application to replace all that. Each department has its rules and its vocabulary, I describe them department by department.

═══════════════════════════════════════════════════════════════

1. ONBOARDING (new customers)

- A prospect becomes a customer once their file is validated: valid identity document, proof of address less than 3 months old, proof of income
- You must be 18. Between 12 and 17, a youth account can be opened with the consent of a legal guardian
- Each customer has a risk level: LOW, MEDIUM or HIGH. A politically exposed person is always HIGH risk
- The file must be reviewed every 3 years (every year for HIGH risk)
- A customer whose review is not done in time has their accounts frozen
- A customer can have several accounts, and an account can have two holders (joint account)

═══════════════════════════════════════════════════════════════

2. ACCOUNTS AND CARDS (the holders)

- Account types: CURRENT, LIVRET A, YOUTH ACCOUNT
- French IBAN: FR76 followed by 23 characters, with the check digits
- LIVRET A: capped at €22,950, no card, no overdraft, interest calculated per fortnight
- Authorised overdraft on the CURRENT account: up to one month of income, at most €2,000. Overdraft interest at 16% per year
- Beyond the authorised overdraft: €8 fee per transaction, capped at €80 per month
- Account fee: €2 per month, free for under-25s
- Cards: CLASSIC (payments €1,500 over 30 rolling days, withdrawals €500 over 7 days) and GOLD (payments €5,000, withdrawals €1,000)
- A card is blocked after 3 wrong PINs. A lost or stolen card is permanently cancelled
- No GOLD card on a YOUTH ACCOUNT

═══════════════════════════════════════════════════════════════

3. PAYMENTS (transfers and direct debits)

- A transfer has a payer, a payee (IBAN + name), an amount and a reference
- Standard transfer: executed the next business day if entered before 4 pm, otherwise the business day after
- Instant transfer: executed in 10 seconds, 7 days a week, up to €15,000
- A new payee can be used immediately after being added
- A transfer is refused if the balance is insufficient
- Standing orders: fixed amount, monthly or quarterly, optional end date
- A direct debit requires a signed mandate. The customer can ask for a refund for 8 weeks
- A rejected direct debit costs the customer €20

═══════════════════════════════════════════════════════════════

4. LENDING (personal loans)

- Amount from €1,000 to €75,000, term from 12 to 84 months
- Maximum debt-to-income ratio of 35%, including the new monthly instalment
- The borrower must have been a customer for 6 months and be under 75 at the end of the loan
- No loan if the customer had an unauthorised overdraft in the last 3 months
- Borrower insurance is mandatory
- 14-day cooling-off period after signing: the funds are only paid out afterwards
- Instalments are debited from the borrower's CURRENT account
- 2 unpaid instalments: the file goes to debt collection
- Early repayment allowed, with a 1% penalty if more than €10,000 is repaid over 12 months

═══════════════════════════════════════════════════════════════

5. COMPLIANCE AND FRAUD

- Every payee is checked against the sanctions list. A payee on the list: the transfer is blocked and the account is frozen
- A transfer of more than €10,000 to a new payee is held for 24 h, while an adviser calls the customer
- Two card payments in two different countries less than one hour apart: the card is blocked
- Cash deposits of more than €10,000 over a month: mandatory suspicious activity report, without telling the customer
- Customer files are reviewed every 2 years
- A frozen account no longer accepts any outgoing transaction
- Fees are free for YOUTH ACCOUNTS up to 26

═══════════════════════════════════════════════════════════════

🎯 WHAT I WANT FIRST

1. Opening an account with the file validation
2. Transfers and direct debits
3. Overdraft and fees
4. Cards and their limits
5. Personal loans

═══════════════════════════════════════════════════════════════

❓ THE QUESTIONS MY DEPARTMENTS ASKED ME

- Payments: "A loan instalment on a frozen account, do we still debit it?"
- Accounts: "A joint account where only one of the two holders did not do their review, is it frozen for both?"
- Lending: "The authorised overdraft, does it count in the debt-to-income ratio?"
```
