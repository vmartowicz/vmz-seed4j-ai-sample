### Example 6 - Advanced level
```
Municipal library management application

Context:
We are a neighborhood library with 5,000 books and 300 active members.
We want to digitize the management of loans and fines.

MAIN ENTITIES:

1. Member
   - Full name
   - Email (unique)
   - Registration date
   - Status: active or suspended, as an enum

2. Book
   - ISBN (unique identifier, 13-digit format)
   - Title
   - Author
   - Status: available, borrowed, under maintenance, as an enum

3. Loan
   - Loan date
   - Expected return date (14 days after the loan)
   - Actual return date (may be null if not returned yet)
   - Reference to the member
   - Reference to the book

STRICT BUSINESS RULES:

1. Loan limits:
   - A member can borrow at most 3 books at the same time
   - Loan duration: exactly 14 days

2. Loan conditions:
   - The book must be available
   - The member must not have any overdue loans
   - The member must not already have 3 borrowed books
   - The member must have the "active" status

3. Fines:
   - Fine of €0.50 per day late
   - Calculation: (actual return date - expected return date) × €0.50
   - If the fine exceeds €10, the member is automatically suspended

4. Book return:
   - Frees a loan slot for the member
   - Calculates the fine if late
   - Updates the book status to "available"

USE CASES TO IMPLEMENT:

1. Register a new member
   - Input: name, email
   - Output: Member created with registration date = today

2. Borrow a book
   - Input: member ID, book ISBN
   - Output: Loan created
   - Checks: all the loan conditions

3. Return a book
   - Input: loan ID
   - Output: Fine, if any
   - Actions: fine calculation, book release

4. Calculate a member's fines
   - Input: member ID
   - Output: List of fines with amounts

5. Search available books
   - Input: search criteria (title, author)
   - Output: List of available books

TECHNICAL CONSTRAINTS:
- All amounts as BigDecimal
- Dates as LocalDate
- Identifiers as UUID except ISBN
```
