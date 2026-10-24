### Example 9 - Intermediate level
```
Veterinary clinic management application (Spring PetClinic)

Context:
We are a veterinary clinic. The front desk registers owners and their pets,
and schedules visits. Customers must be able to see the list of our vets
and their specialties.

MAIN ENTITIES:

1. Owner
   - First name (required, 30 characters max)
   - Last name (required, 30 characters max)
   - Address (required, 255 characters max)
   - City (required, 80 characters max)
   - Telephone (required, exactly 10 digits)
   - List of their pets, sorted by name

2. Pet
   - Name (required, 30 characters max)
   - Birth date (required)
   - Type: cat, dog, lizard, snake, bird, hamster
   - List of its visits, sorted by date

3. Visit
   - Date
   - Description (required, 255 characters max)

4. Vet
   - First name, last name (required, 30 characters max)
   - Specialties: none, one or several among radiology, surgery, dentistry

BUSINESS RULES:

1. Pets:
   - An owner cannot have two pets with the same name
     (case-insensitive: "Rex" and "rex" are the same name)
   - The birth date cannot be in the future
   - The type is required when creating a pet

2. Visits:
   - A visit is always attached to a pet of an owner
   - The visit date must be in the future (tomorrow at the earliest)

3. Owner search:
   - Search by the beginning of the last name
   - If no name is entered, all owners are returned
   - If no owner matches: "not found" message
   - If a single owner matches: their details are shown directly
   - Results paginated by 5

USE CASES TO IMPLEMENT:

1. Register a new owner
   - Input: first name, last name, address, city, telephone
   - Output: owner created

2. Update an owner
   - Input: owner ID, first name, last name, address, city, telephone

3. Search owners
   - Input: beginning of the last name (optional), page number
   - Output: paginated list of owners with their pets

4. View an owner's details
   - Input: owner ID
   - Output: owner, their pets and the visits of each pet

5. Add a pet to an owner
   - Input: owner ID, name, birth date, type
   - Checks: name unique for this owner, birth date not in the future

6. Update a pet
   - Input: owner ID, pet ID, name, birth date, type
   - Checks: the new name must not be the name of another pet of the owner

7. Schedule a visit
   - Input: owner ID, pet ID, date, description
   - Checks: the pet belongs to the owner, date in the future

8. List vets
   - Input: page number
   - Output: list paginated by 5, with the specialties of each vet

9. List pet types
   - Output: cat, dog, lizard, snake, bird, hamster
```
