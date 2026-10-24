### Exemple 9 - Niveau intermédiaire
```
Application de gestion d'une clinique vétérinaire (Spring PetClinic)

Contexte :
Nous sommes une clinique vétérinaire. L'accueil enregistre les propriétaires
et leurs animaux, et planifie les visites. Les clients doivent pouvoir voir
la liste de nos vétérinaires et leurs spécialités.

ENTITÉS PRINCIPALES :

1. Propriétaire
   - Prénom (obligatoire, 30 caractères max)
   - Nom (obligatoire, 30 caractères max)
   - Adresse (obligatoire, 255 caractères max)
   - Ville (obligatoire, 80 caractères max)
   - Téléphone (obligatoire, exactement 10 chiffres)
   - Liste de ses animaux, triés par nom

2. Animal
   - Nom (obligatoire, 30 caractères max)
   - Date de naissance (obligatoire)
   - Type : chat, chien, lézard, serpent, oiseau, hamster
   - Liste de ses visites, triées par date

3. Visite
   - Date
   - Description (obligatoire, 255 caractères max)

4. Vétérinaire
   - Prénom, nom (obligatoires, 30 caractères max)
   - Spécialités : aucune, une ou plusieurs parmi radiologie, chirurgie, dentisterie

RÈGLES MÉTIER :

1. Animaux :
   - Un propriétaire ne peut pas avoir deux animaux du même nom
     (sans tenir compte des majuscules : "Rex" et "rex" sont le même nom)
   - La date de naissance ne peut pas être dans le futur
   - Le type est obligatoire à la création d'un animal

2. Visites :
   - Une visite est toujours rattachée à un animal d'un propriétaire
   - La date de la visite doit être dans le futur (au plus tôt demain)

3. Recherche de propriétaires :
   - Recherche par début du nom de famille
   - Si aucun nom n'est saisi, tous les propriétaires sont retournés
   - Si aucun propriétaire ne correspond : message "non trouvé"
   - Si un seul propriétaire correspond : on affiche directement sa fiche
   - Résultats paginés par 5

CAS D'USAGE À IMPLÉMENTER :

1. Enregistrer un nouveau propriétaire
   - Entrée : prénom, nom, adresse, ville, téléphone
   - Sortie : propriétaire créé

2. Modifier un propriétaire
   - Entrée : ID propriétaire, prénom, nom, adresse, ville, téléphone

3. Rechercher des propriétaires
   - Entrée : début du nom de famille (facultatif), numéro de page
   - Sortie : liste paginée des propriétaires avec leurs animaux

4. Consulter la fiche d'un propriétaire
   - Entrée : ID propriétaire
   - Sortie : propriétaire, ses animaux et les visites de chaque animal

5. Ajouter un animal à un propriétaire
   - Entrée : ID propriétaire, nom, date de naissance, type
   - Vérifications : nom unique chez ce propriétaire, date non future

6. Modifier un animal
   - Entrée : ID propriétaire, ID animal, nom, date de naissance, type
   - Vérifications : le nouveau nom ne doit pas être celui d'un autre animal du propriétaire

7. Planifier une visite
   - Entrée : ID propriétaire, ID animal, date, description
   - Vérifications : l'animal appartient bien au propriétaire, date dans le futur

8. Lister les vétérinaires
   - Entrée : numéro de page
   - Sortie : liste paginée par 5, avec les spécialités de chacun

9. Lister les types d'animaux
   - Sortie : chat, chien, lézard, serpent, oiseau, hamster
```
