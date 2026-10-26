### Example 13 - Expert level
```
I coordinate "MédiRhône", a group of 3 medical centres (Lyon 7th, Vénissieux, Vaulx-en-Velin) with 25 doctors, a medical laboratory and an imaging department. Today the front desk has its diary, the doctors their records software, the lab and imaging their own tools, and billing types everything again. The result: CT scans with contrast done without a kidney test, critical results seen three days later, claim forms sent to the wrong insured person...

I want an application to replace all that. Each team has its rules and its vocabulary, I describe them team by team.

═══════════════════════════════════════════════════════════════

1. THE FRONT DESK (patients and appointments)

- A patient: last name, first name, date of birth, social security number (13 digits + 2-digit key), phone, referring doctor
- 15-minute slots for general practitioners, 30 minutes for specialists
- Each doctor keeps 2 slots per day for same-day emergencies, not bookable online
- Cancellation allowed up to 24 h before the appointment
- 3 missed appointments over 12 months: the patient can no longer book online, only by phone
- A missed appointment is never billed
- A minor under 16 must come accompanied
- The front desk does not see the content of consultations, only appointments

═══════════════════════════════════════════════════════════════

2. THE DOCTORS (consultations and prescriptions)

- A consultation: reason, clinical examination, diagnosis, optional prescription
- A general practitioner consultation lasts 20 minutes
- A closed consultation can no longer be modified, only a dated and signed addendum can be added
- The medical record contains allergies, medical history and current treatments
- Prescribing a drug the patient is allergic to must be blocked
- A prescription is valid for 3 months
- A prescription can order drugs, laboratory tests or imaging exams
- An abnormal result must first be announced to the patient by their doctor

═══════════════════════════════════════════════════════════════

3. THE LABORATORY (samples and results)

- The lab only takes a sample on a prescription less than one year old
- Some tests require fasting (blood glucose, lipid panel): otherwise the sample is refused
- A sample must be analysed within 4 h after collection, otherwise a new sample is needed
- Each result is validated by a biologist before being released
- Critical values (potassium > 6 mmol/L, blood glucose < 0.5 g/L...): the prescribing doctor must be warned within the hour
- As soon as a result is validated, the patient can see it in their online space
- Results are kept and can be reused by imaging and the doctors

═══════════════════════════════════════════════════════════════

4. IMAGING (X-rays, ultrasounds, CT scans, MRI)

- Every exam requires a prescription
- A CT scan with contrast requires a creatinine test less than 3 months old
- X-ray and CT scan for a woman aged 15 to 50: we must check she is not pregnant
- MRI forbidden for pacemaker wearers
- The report is sent to the prescribing doctor within 48 h
- The patient gets their images the same day, the report at the same time as the doctor

═══════════════════════════════════════════════════════════════

5. BILLING (insured persons and reimbursements)

- General practitioner consultation: €30, specialist: €50. Some specialists charge extra fees
- Social security reimburses 70% of the rate, only 30% if the patient has not declared a referring doctor
- Third-party payment (the patient pays nothing) for patients with a long-term condition (ALD) and beneficiaries of the Complémentaire santé solidaire
- A child is attached to the insured person who declares them: the claim form is sent in that insured person's name
- The electronic claim form is sent within 8 days
- A missed appointment without notice is billed €30
- An imaging prescription is valid for 1 year

═══════════════════════════════════════════════════════════════

🎯 WHAT I WANT FIRST

1. Booking appointments
2. The consultation with the record and the prescription
3. The laboratory and releasing results
4. Imaging
5. Billing and claim forms

═══════════════════════════════════════════════════════════════

❓ THE QUESTIONS MY TEAMS ASKED ME

- The front desk: "A patient who changes referring doctor, do we keep the appointments already booked with the previous one?"
- The laboratory: "A critical result validated on a Saturday evening, their doctor is not there: who do we warn?"
- Billing: "A child declared on both parents, who do we send the claim form to?"
```
