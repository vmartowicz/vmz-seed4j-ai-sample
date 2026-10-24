### Example 8 - Advanced level
```
Booking management system for a gourmet restaurant

═══════════════════════════════════════════════════════════════

📍 BUSINESS CONTEXT

Restaurant "Le Gourmet":
- 50 covers in total
- 10 tables for 2 people
- 6 tables for 4 people
- 2 tables for 6 people
- Lunch service: 12:00-14:00
- Dinner service: 19:00-22:00
- Closed on Sunday and Monday

═══════════════════════════════════════════════════════════════

🎯 GOALS

1. Allow customers to book online
2. Manage table occupancy optimally
3. Apply a deposit policy for some bookings
4. Manage cancellations and no-shows

═══════════════════════════════════════════════════════════════

📊 DATA MODEL

CUSTOMER:
- Full name
- Phone (French format: 0X XX XX XX XX)
- Email
- Booking history
- Status: loyal (5+ bookings), regular, blacklisted (3+ no-shows)

BOOKING:
- Date and time
- Number of people (2 to 8 maximum)
- Service: LUNCH or DINNER
- Status: CONFIRMED, PENDING, CANCELLED, HONORED, NO_SHOW
- Special comments (allergies, birthday, etc.)
- Assigned table (may be null before confirmation)

TABLE:
- Number (1 to 18)
- Capacity (2, 4 or 6 people)
- Position: WINDOW, TERRACE, INDOOR
- Availability

DEPOSIT:
- Amount: €20 per person for tables of 6+
- Status: EXPECTED, PAID, REFUNDED
- Payment deadline: 48h before the booking

═══════════════════════════════════════════════════════════════

⚖️ BUSINESS RULES

R1 - Capacity and lead times:
   - Booking possible up to 30 days in advance
   - Minimum 2 hours before the service to book
   - Maximum 8 people per booking
   - Automatically fit the table to the group size

R2 - Deposit policy:
   - Mandatory for 6 people or more
   - Amount: number of people × €20
   - To be paid within 48h after the booking
   - Refunded if cancelled > 48h before

R3 - Cancellation management:
   - Free if > 48h before
   - Deposit lost if < 48h before
   - Maximum 3 cancellations per customer per year

R4 - No-show (absent without notice):
   - Deposit lost
   - Customer blacklisted after 3 no-shows
   - Blacklisted customers can no longer book

R5 - Table assignment:
   - 2 people → table for 2
   - 3-4 people → table for 4
   - 5-6 people → table for 6
   - 7-8 people → 2 tables for 4 combined
   - Prefer window tables for couples
   - Prefer the terrace in summer (June-August)

R6 - Loyal customers:
   - "Loyal" status after 5 honored bookings
   - Can book without a deposit
   - Priority on the best tables

═══════════════════════════════════════════════════════════════

🔧 USE CASES

1. CREATE A BOOKING
   Input: name, phone, email, date, time, number of people, comments
   Preconditions:
   - Customer not blacklisted
   - Date within 30 days
   - Slot available
   - Service open that day
   Output: Booking created with status PENDING or CONFIRMED

2. PAY THE DEPOSIT
   Input: booking ID, amount
   Preconditions:
   - Booking exists and status PENDING
   - Correct amount (number of people × €20)
   - Within the 48h deadline
   Output: Deposit marked PAID, booking CONFIRMED

3. CANCEL A BOOKING
   Input: booking ID
   Logic:
   - If > 48h before: full refund
   - If < 48h before: deposit lost
   - Increment the customer's cancellation counter
   Output: Booking CANCELLED

4. CONFIRM ATTENDANCE (check-in)
   Input: booking ID
   Actions:
   - Mark the booking as HONORED
   - Increment the customer's booking counter
   - Check whether the customer becomes "loyal" (5 bookings)

5. MARK NO-SHOW
   Input: booking ID
   Actions:
   - Mark the booking NO_SHOW
   - Deposit lost
   - Increment the no-show counter
   - If 3 no-shows → blacklist the customer

6. VIEW AVAILABILITY
   Input: date, service, number of people
   Output: List of slots with available tables

7. SEE TODAY'S BOOKINGS
   Input: date, service
   Output: List of bookings with table details

═══════════════════════════════════════════════════════════════

💡 SPECIFIC CALCULATIONS

Deposit amount = number_of_people × 20
Days before booking = booking_date - current_date
Loyal status = (honored_bookings >= 5)
Blacklisted = (no_shows >= 3)
```
