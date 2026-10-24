### Exemple 8 - Niveau avancé
```
Système de gestion de réservations pour restaurant gastronomique

═══════════════════════════════════════════════════════════════

📍 CONTEXTE MÉTIER

Restaurant "Le Gourmet" :
- 50 couverts au total
- 10 tables de 2 personnes
- 6 tables de 4 personnes
- 2 tables de 6 personnes
- Service midi : 12h-14h
- Service soir : 19h-22h
- Fermé le dimanche et lundi

═══════════════════════════════════════════════════════════════

🎯 OBJECTIFS

1. Permettre aux clients de réserver en ligne
2. Gérer l'occupation des tables de manière optimale
3. Appliquer une politique d'acompte pour certaines réservations
4. Gérer les annulations et no-shows

═══════════════════════════════════════════════════════════════

📊 MODÈLE DE DONNÉES

CLIENT :
- Nom complet
- Téléphone (format français : 0X XX XX XX XX)
- Email
- Historique de réservations
- Statut : fidèle (5+ réservations), normal, blacklisté (3+ no-shows)

RÉSERVATION :
- Date et heure
- Nombre de personnes (2 à 8 maximum)
- Service : MIDI ou SOIR
- Statut : CONFIRMÉE, EN_ATTENTE, ANNULÉE, HONORÉE, NO_SHOW
- Commentaires spéciaux (allergies, anniversaire, etc.)
- Table assignée (peut être null avant confirmation)

TABLE :
- Numéro (1 à 18)
- Capacité (2, 4 ou 6 personnes)
- Position : FENÊTRE, TERRASSE, INTÉRIEUR
- Disponibilité

ACOMPTE :
- Montant : 20€ par personne pour tables de 6+
- Statut : ATTENDU, PAYÉ, REMBOURSÉ
- Date limite de paiement : 48h avant la réservation

═══════════════════════════════════════════════════════════════

⚖️ RÈGLES MÉTIER

R1 - Capacité et délais :
   - Réservation possible jusqu'à 30 jours à l'avance
   - Minimum 2 heures avant le service pour réserver
   - Maximum 8 personnes par réservation
   - Adapter automatiquement la table à la taille du groupe

R2 - Politique d'acompte :
   - Obligatoire pour 6 personnes ou plus
   - Montant : nombre de personnes × 20€
   - À payer dans les 48h suivant la réservation
   - Remboursé si annulation > 48h avant

R3 - Gestion des annulations :
   - Gratuite si > 48h avant
   - Acompte perdu si < 48h avant
   - Maximum 3 annulations par client par an

R4 - No-show (absence sans prévenir) :
   - Acompte perdu
   - Client blacklisté après 3 no-shows
   - Clients blacklistés ne peuvent plus réserver

R5 - Attribution des tables :
   - 2 personnes → table de 2
   - 3-4 personnes → table de 4
   - 5-6 personnes → table de 6
   - 7-8 personnes → 2 tables de 4 combinées
   - Préférer les tables fenêtres pour les couples
   - Préférer la terrasse en été (juin-août)

R6 - Clients fidèles :
   - Statut "fidèle" après 5 réservations honorées
   - Peuvent réserver sans acompte
   - Priorité sur les meilleures tables

═══════════════════════════════════════════════════════════════

🔧 CAS D'USAGE

1. CRÉER UNE RÉSERVATION
   Entrée : nom, téléphone, email, date, heure, nb personnes, commentaires
   Préconditions :
   - Client pas blacklisté
   - Date dans les 30 jours
   - Créneau disponible
   - Service ouvert ce jour
   Sortie : Réservation créée avec statut EN_ATTENTE ou CONFIRMÉE

2. PAYER L'ACOMPTE
   Entrée : ID réservation, montant
   Préconditions :
   - Réservation existe et statut EN_ATTENTE
   - Montant correct (nb personnes × 20€)
   - Dans le délai de 48h
   Sortie : Acompte marqué PAYÉ, réservation CONFIRMÉE

3. ANNULER UNE RÉSERVATION
   Entrée : ID réservation
   Logique :
   - Si > 48h avant : remboursement total
   - Si < 48h avant : acompte perdu
   - Incrémenter compteur annulations client
   Sortie : Réservation ANNULÉE

4. CONFIRMER PRÉSENCE (check-in)
   Entrée : ID réservation
   Actions :
   - Marquer réservation comme HONORÉE
   - Incrémenter compteur réservations client
   - Vérifier si devient "fidèle" (5 réservations)

5. MARQUER NO-SHOW
   Entrée : ID réservation
   Actions :
   - Marquer réservation NO_SHOW
   - Acompte perdu
   - Incrémenter compteur no-shows
   - Si 3 no-shows → blacklister client

6. CONSULTER DISPONIBILITÉS
   Entrée : date, service, nb personnes
   Sortie : Liste des créneaux avec tables disponibles

7. VOIR RÉSERVATIONS DU JOUR
   Entrée : date, service
   Sortie : Liste des réservations avec détails tables

═══════════════════════════════════════════════════════════════

💡 CALCULS SPÉCIFIQUES

Montant acompte = nombre_personnes × 20
Jours avant réservation = date_reservation - date_actuelle
Statut fidèle = (reservations_honorees >= 5)
Blackliste = (no_shows >= 3)
```
