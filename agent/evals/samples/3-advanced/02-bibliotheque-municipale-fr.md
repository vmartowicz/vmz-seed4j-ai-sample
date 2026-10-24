### Exemple 6 - Niveau avancé
```
Application de gestion de bibliothèque municipale

Contexte :
Nous sommes une bibliothèque de quartier avec 5000 livres et 300 adhérents actifs.
Nous voulons digitaliser la gestion des emprunts et des amendes.

ENTITÉS PRINCIPALES :

1. Adhérent
   - Nom complet
   - Email (unique)
   - Date d'inscription
   - Statut : actif ou suspendu sous forme d'enum

2. Livre
   - ISBN (identifiant unique, format 13 chiffres)
   - Titre
   - Auteur
   - Statut : disponible, emprunté, en maintenance sous forme d'enum

3. Emprunt
   - Date d'emprunt
   - Date de retour prévue (14 jours après emprunt)
   - Date de retour effective (peut être null si pas encore retourné)
   - Référence vers l'adhérent
   - Référence vers le livre

RÈGLES MÉTIER STRICTES :

1. Limites d'emprunt :
   - Un adhérent peut emprunter maximum 3 livres simultanément
   - Durée d'emprunt : 14 jours exactement

2. Conditions d'emprunt :
   - Le livre doit être disponible
   - L'adhérent ne doit pas avoir de retards en cours
   - L'adhérent ne doit pas avoir 3 livres déjà empruntés
   - L'adhérent doit avoir le statut "actif"

3. Amendes :
   - Amende de 0,50€ par jour de retard
   - Calcul : (date retour effective - date retour prévue) × 0,50€
   - Si l'amende dépasse 10€, l'adhérent est automatiquement suspendu

4. Retour de livre :
   - Libère un slot d'emprunt pour l'adhérent
   - Calcule l'amende si retard
   - Met à jour le statut du livre à "disponible"

CAS D'USAGE À IMPLÉMENTER :

1. Inscrire un nouvel adhérent
   - Entrée : nom, email
   - Sortie : Adhérent créé avec date d'inscription = aujourd'hui

2. Emprunter un livre
   - Entrée : ID adhérent, ISBN du livre
   - Sortie : Emprunt créé
   - Vérifications : toutes les conditions d'emprunt

3. Retourner un livre
   - Entrée : ID de l'emprunt
   - Sortie : Amende éventuelle
   - Actions : calcul amende, libération du livre

4. Calculer les amendes d'un adhérent
   - Entrée : ID adhérent
   - Sortie : Liste des amendes avec montants

5. Rechercher les livres disponibles
   - Entrée : critères de recherche (titre, auteur)
   - Sortie : Liste des livres disponibles

CONTRAINTES TECHNIQUES :
- Tous les montants en BigDecimal
- Dates en LocalDate
- Identifiants en UUID sauf ISBN
```

