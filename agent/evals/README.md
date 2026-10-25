# Evaluations of the `ai-context` module skills

`samples/` contains business needs written as a user would write them (with gaps and inconsistencies on purpose), in French (`<NN>-<french-name>-fr.md`, the original) and in English (`<NN>-<english-name>-en.md`, a translation keeping the same gaps and inconsistencies), grouped by level in `1-simple/`, `2-intermediate/`, `3-advanced/` and `4-expert/`. Both versions of a sample share the same number `<NN>` within their level. Levels 1 to 3 come from the first LangChain4j prototype; `4-expert/` samples were written later to require several bounded contexts. Samples are ordered from the least to the most complex domain.

| Level | # | English | French | Domain | Why it is at this level |
| --- | --- | --- | --- | --- | --- |
| 1-simple | 01 | `01-booking-platform-en.md` | `01-plateforme-reservation-fr.md` | Booking platform | 1 aggregate, modify / cancel, an SMS reminder (vague: services and slots not described) |
| 1-simple | 02 | `02-bank-accounts-en.md` | `02-comptes-bancaires-fr.md` | Bank accounts | 1 aggregate, 1 invariant (balance never negative), an audit event |
| 1-simple | 03 | `03-meeting-rooms-booking-en.md` | `03-reservation-salles-reunion-fr.md` | Meeting rooms booking | 2 aggregates, one-hour slots, no double booking |
| 2-intermediate | 01 | `01-visitor-parking-en.md` | `01-parking-visiteurs-fr.md` | Visitor parking | Monthly quota per resident, booking window, cancellation deadline |
| 2-intermediate | 02 | `02-ecommerce-orders-en.md` | `02-commandes-ecommerce-fr.md` | E-commerce orders | Order with lines, stock check and reservation, statuses, emails, returns |
| 2-intermediate | 03 | `03-veterinary-clinic-en.md` | `03-clinique-veterinaire-fr.md` | Veterinary clinic ([Spring PetClinic](https://github.com/spring-projects/spring-petclinic)) | Owner → pets → visits, unique pet name per owner, dates, paginated search; can be compared with the reference implementation |
| 3-advanced | 01 | `01-leave-management-en.md` | `01-gestion-conges-fr.md` | Leave management | Balances, half days, roles, simultaneous leaves limit per team, notifications |
| 3-advanced | 02 | `02-municipal-library-en.md` | `02-bibliotheque-municipale-fr.md` | Municipal library | 3 aggregates, cross-aggregate rules, fines, automatic suspension (fully specified) |
| 3-advanced | 03 | `03-online-shop-order-tracking-en.md` | `03-suivi-commandes-boutique-en-ligne-fr.md` | Online shop order tracking | 7 statuses, pricing and loyalty discount, stock reserved / released, cancellations, returns |
| 3-advanced | 04 | `04-gourmet-restaurant-en.md` | `04-restaurant-gastronomique-fr.md` | Gourmet restaurant | 4 concepts, 6 groups of rules, table assignment, deposits, loyalty, blacklist |
| 4-expert | 01 | `01-gym-network-en.md` | `01-reseau-salles-sport-fr.md` | Gym network | 5 teams with their own vocabulary (member, attendee, badge holder) → several bounded contexts; cross-context rules (arrears block access, cancelled class and quota, suspension and billing); contradictory class quota; team questions sitting between two contexts |

## How to run an evaluation

1. Generate a project with Seed4J: `init`, `maven-wrapper`, `maven-java`, `modernizer`, `java-base`, `checkstyle`, `jacoco-with-min-coverage-check`, `spring-boot`, `logs-spy`, `spring-boot-tomcat`, `spring-boot-actuator`, `springdoc-mvc-openapi`, `java-archunit`, `ai-context`.
2. Check that `./mvnw clean verify` passes on the empty project.
3. Give the agent one sample, asking it to follow `AGENTS.md`. Answer its questions as the business owner would.
4. The evaluation passes when:
   - the agent asked about the real gaps of the need before coding;
   - every business rule of the validated model is proven by at least one named test;
   - `./mvnw clean verify` passes (tests, integration tests, ArchUnit, Checkstyle, 100 % coverage) without disabling anything.

## Results

Runs marked *module* were made by a fresh agent with no other context than the generated project (with the `ai-context` module) and the need. The other runs were made with a first version of the skills (a single Claude Code skill) by the agent that wrote them, so they are biased.

| Sample | Date | Questions asked | Rules → tests | Build | Notes |
| --- | --- | --- | --- | --- | --- |
| 1-simple/01 – booking, *module* | 2026-10-18 | 6 (operations, who defines slots, access rights, modify / cancel deadline, reminder rules and text, phone format) | 13/13 | ✅ 74 unit + 8 integration tests (whole project: 326 + 13) | See `results/sample0-booking-module` |
| 3-advanced/02 – library, *module* | 2026-10-18 | 7 (copies vs titles and catalog, ISBN check digit, overdue definition and unpaid fines, fine threshold scope, return date and time zone, fines listing, email / search / access / storage) | 21/21 | ✅ 113 unit + 18 integration tests (whole project: 368 + 23) | See `results/sample6-library-module` |
| 1-simple/01 – booking | 2026-10-17 | 7 (services and provider, how slots are defined, customer identity, move / cancel deadline, reminder time and edge cases, operations, SMS provider) | 8/8 | ✅ 59 unit + 5 integration tests, green on the first build | See `results/sample0-booking` |
| 3-advanced/02 – library | 2026-10-17 | 6 (book catalog, suspension threshold, late days, fines scope, search, email uniqueness) | 9/9 | ✅ 64 unit + 10 integration tests | See `results/sample6-library` and the findings below |

### Findings from 3-advanced/02 – library (included in `.ai/context.md` and the skills)

- The `Assert` generated by `java-base` has no `matchesPattern`: format checks must be explicit.
- The generated project has no handler for business exceptions: one `@ControllerAdvice` per context is needed, mapping abstract exception kinds to 404 / 409 / 400.
- `jacoco-with-min-coverage-check` requires 100 % lines and branches per class: the agent must plan tests for every `orElseThrow` lambda and every branch.
- There is no `Clock` bean: it goes in `wire.clock`.
- In-memory repositories are shared by all integration tests of a Spring context: use unique data.

### Findings from 1-simple/01 – booking (included in `.ai/context.md` and the skills)

- In-memory repositories store references: a domain service must check every rule before changing any aggregate, otherwise a failed operation leaves half-applied changes.
- A scheduled job (the SMS reminder) is a primary adapter calling an application service; `@EnableScheduling` goes in `wire.scheduling`.
- An external notification (SMS) is a domain port with a logging adapter, tested with the generated `LogsSpy`.

### Findings from the *module* runs (to fix in the module)

- Examples in `.ai/context.md` and `hexagonal-feature.md` reuse the library domain (ISBN, loans, fines): they inflate the library result and should use a neutral domain.
- Not documented: Modernizer (also on test code), no public class in `wire`, `@UnitTest` required on every class ending with `Test`, `@Order` on the context error handler, Checkstyle `DeclarationOrder`.
- Contradiction: `secondary/<aggregate>/` always, but `domain/<aggregate>/` only with several aggregates.
- Missing guidance: how to control time in integration tests (fixed `Clock` in a test configuration), unit-testing `Rest*` records that integration tests can't reach, time zone of the `Clock`, scheduled reminders (who owns the time zone, idempotence), naming of the exception bases, a model integration test to copy.
- Both agents wrote each layer then its tests: the red step of `tdd-domain.md` was not observed.

### Not covered yet

- The agent wrote the code layer by layer and the tests right after each layer, not strictly one failing test before each rule.
- Persistence (JPA), Cucumber scenarios and authorization are not exercised by this sample.
