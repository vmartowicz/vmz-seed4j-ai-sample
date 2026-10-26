### Example 11 - Expert level
```
I run "RhôneCar", a car rental agency with 3 rental points: Lyon Part-Dieu (train station), Lyon Saint-Exupéry (airport) and Villeurbanne. We have about 120 vehicles. Today the fleet is tracked in a spreadsheet, bookings in an old piece of software, contracts on paper and billing in a third tool. The result: cars rented while they are at the garage, customers billed twice, loyalty points never credited...

I want an application to replace all that. Each team has its habits and its vocabulary, I describe them team by team.

═══════════════════════════════════════════════════════════════

1. THE FLEET TEAM (vehicles and maintenance)

- A vehicle: licence plate (format AA-123-AA), make, model, category, mileage, home point, fuel (PETROL, DIESEL, ELECTRIC)
- Categories: ECO (Clio, 208...), COMFORT (308, Golf...), SUV, VAN, PREMIUM
- A vehicle is AVAILABLE, RENTED, IN MAINTENANCE or OUT OF SERVICE
- Mandatory service every 15,000 km or every 12 months. A vehicle past its service must no longer be rented
- A vehicle returned with damage goes IN MAINTENANCE until it is repaired
- A vehicle older than 5 years or over 150,000 km leaves the fleet
- Vehicles stay where they were returned. We transfer vehicles between agencies when a point lacks cars

═══════════════════════════════════════════════════════════════

2. THE BOOKING TEAM (website and phone)

- The customer books a category, not a specific vehicle, with a pick-up point and date, a return point and date
- Daily prices: ECO €35, COMFORT €49, SUV €69, VAN €79, PREMIUM €119
- A day = 24 h from the pick-up time. Any started day is due
- Included mileage: 200 km per day, then €0.25 per extra km. Unlimited mileage for PREMIUM
- Return to another point than the pick-up one: €50 one-way fee (free between Part-Dieu and Villeurbanne)
- The driver must be 21 and have held a licence for 3 years. Young driver surcharge of €15/day under 25
- No PREMIUM or VAN for young drivers
- Options: child seat €5/day, additional driver €10/day, GPS €8/day (capped at €50 per rental)
- Free cancellation up to 48 h before pick-up, then 1 rental day is kept
- If the booked category is not available at pick-up, we upgrade for free to the category above

═══════════════════════════════════════════════════════════════

3. THE COUNTER (pick-ups, returns and inspections)

- At pick-up, we assign a vehicle of the booked category present at the point, we do the inspection (mileage, fuel or battery level, existing damage with photos) and we take a security deposit
- Security deposit: €800 for ECO and COMFORT, €1,500 for SUV and VAN, €2,500 for PREMIUM
- The customer must show their licence and a credit card in their name
- At return: new inspection. If the tank is not full, the missing fuel is billed €2.50 per litre (€0.40 per kWh for electric cars)
- Late return: 59-minute tolerance, then each started hour is billed €15, and beyond 3 hours we bill one more day
- New damage is billed to the customer within the excess (the security deposit)
- We don't extend a rental at the counter, a new booking is needed

═══════════════════════════════════════════════════════════════

4. BILLING

- The rental is paid at booking
- The final invoice is made at return: extra kilometres, fuel, late return, one-way fee, damage
- The security deposit is released within 7 days after return, minus the amounts due
- Damage is billed at return, on the garage's quote, at the latest 30 days after return
- A credit note is issued if the rental is cut short, up to 2 days

═══════════════════════════════════════════════════════════════

5. LOYALTY

- 1 point per euro spent on the rental (excluding fuel and damage)
- Statuses: SILVER from 1,000 points, GOLD from 3,000 points over the last 12 months
- GOLD members get the additional driver for free and a guaranteed upgrade when available
- 500 points = 1 free day in ECO or COMFORT
- Points expire after 24 months
- Points are only credited once the final invoice is paid

═══════════════════════════════════════════════════════════════

🎯 WHAT I WANT FIRST

1. Book a category and pay
2. Pick-up and return at the counter with the inspections
3. The final invoice and the security deposit
4. Fleet and service tracking
5. Loyalty

═══════════════════════════════════════════════════════════════

❓ THE QUESTIONS MY TEAMS ASKED ME

- The fleet: "A vehicle returned to another point, is it available right away for a booking at that point?"
- The counter: "If a GOLD member books an ECO and only PREMIUM cars are left, do we give them the PREMIUM? And the security deposit, is it the ECO one?"
- Billing: "A free day paid with points, does it earn points?"
```
