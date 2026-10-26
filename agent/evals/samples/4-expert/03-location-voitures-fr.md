### Exemple 11 - Niveau expert
```
Je gère "RhôneCar", une agence de location de voitures avec 3 points de location : Lyon Part-Dieu (gare), Lyon Saint-Exupéry (aéroport) et Villeurbanne. On a environ 120 véhicules. Aujourd'hui la flotte est suivie dans un tableur, les réservations dans un vieux logiciel, les contrats sur papier et la facturation dans un troisième outil. Résultat : des voitures louées alors qu'elles sont au garage, des clients facturés deux fois, des points fidélité jamais crédités...

Je veux une application qui remplace tout ça. Chaque équipe a ses habitudes et son vocabulaire, je vous les décris équipe par équipe.

═══════════════════════════════════════════════════════════════

1. L'ÉQUIPE FLOTTE (les véhicules et l'entretien)

- Un véhicule : immatriculation (format AA-123-AA), marque, modèle, catégorie, kilométrage, point de rattachement, carburant (ESSENCE, DIESEL, ÉLECTRIQUE)
- Catégories : ECO (Clio, 208...), CONFORT (308, Golf...), SUV, UTILITAIRE, PREMIUM
- Un véhicule est DISPONIBLE, LOUÉ, EN ENTRETIEN ou HORS SERVICE
- Révision obligatoire tous les 15 000 km ou tous les 12 mois. Un véhicule qui a dépassé sa révision ne doit plus être loué
- Un véhicule rendu avec un dommage passe en EN ENTRETIEN jusqu'à réparation
- Un véhicule de plus de 5 ans ou de plus de 150 000 km est sorti de la flotte
- Les véhicules restent là où ils ont été rendus. On fait des transferts entre agences quand un point manque de voitures

═══════════════════════════════════════════════════════════════

2. L'ÉQUIPE RÉSERVATIONS (le site web et le téléphone)

- Le client réserve une catégorie, pas un véhicule précis, avec un point et une date de départ, un point et une date de retour
- Tarifs par jour : ECO 35 €, CONFORT 49 €, SUV 69 €, UTILITAIRE 79 €, PREMIUM 119 €
- Une journée = 24 h à partir de l'heure de départ. Toute journée commencée est due
- Kilométrage inclus : 200 km par jour, puis 0,25 € par km supplémentaire. Kilométrage illimité pour PREMIUM
- Retour dans un autre point que le départ : 50 € de frais d'abandon (gratuit entre Part-Dieu et Villeurbanne)
- Le conducteur doit avoir 21 ans et 3 ans de permis. Supplément jeune conducteur de 15 €/jour avant 25 ans
- Pas de PREMIUM ni d'UTILITAIRE pour les jeunes conducteurs
- Options : siège enfant 5 €/jour, conducteur additionnel 10 €/jour, GPS 8 €/jour (plafonné à 50 € par location)
- Annulation gratuite jusqu'à 48 h avant le départ, ensuite 1 jour de location est retenu
- Si la catégorie réservée n'est pas disponible au départ, on surclasse gratuitement dans la catégorie au-dessus

═══════════════════════════════════════════════════════════════

3. LE COMPTOIR (départs, retours et états des lieux)

- Au départ, on attribue un véhicule de la catégorie réservée présent dans le point, on fait l'état des lieux (kilométrage, niveau de carburant ou de batterie, dommages existants avec photos) et on prend un dépôt de garantie
- Dépôt de garantie : 800 € pour ECO et CONFORT, 1 500 € pour SUV et UTILITAIRE, 2 500 € pour PREMIUM
- Le client doit présenter son permis et une carte bancaire à son nom
- Au retour : nouvel état des lieux. Si le plein n'est pas fait, on facture le carburant manquant à 2,50 € le litre (0,40 € le kWh pour les électriques)
- Retard : tolérance de 59 minutes, ensuite chaque heure entamée est facturée 15 €, et au-delà de 3 heures on facture une journée de plus
- Un nouveau dommage est facturé au client dans la limite de la franchise (le dépôt de garantie)
- On ne prolonge pas une location au comptoir, il faut faire une nouvelle réservation

═══════════════════════════════════════════════════════════════

4. LA FACTURATION

- La location est payée à la réservation
- La facture finale est faite au retour : kilomètres en plus, carburant, retard, frais d'abandon, dommages
- Le dépôt de garantie est libéré sous 7 jours après le retour, moins les sommes dues
- Les dommages sont facturés au retour, sur devis du garage, au plus tard 30 jours après le retour
- Un avoir est fait si la location est écourtée, dans la limite de 2 jours

═══════════════════════════════════════════════════════════════

5. LA FIDÉLITÉ

- 1 point par euro dépensé sur la location (hors carburant et dommages)
- Statuts : SILVER à partir de 1 000 points, GOLD à partir de 3 000 points sur les 12 derniers mois
- Les GOLD ont le conducteur additionnel offert et un surclassement garanti si disponible
- 500 points = 1 journée offerte en catégorie ECO ou CONFORT
- Les points expirent après 24 mois
- Les points ne sont crédités qu'une fois la facture finale payée

═══════════════════════════════════════════════════════════════

🎯 CE QUE JE VEUX EN PRIORITÉ

1. Réserver une catégorie et payer
2. Départ et retour au comptoir avec les états des lieux
3. La facture finale et le dépôt de garantie
4. Le suivi de la flotte et des révisions
5. La fidélité

═══════════════════════════════════════════════════════════════

❓ LES QUESTIONS QUE M'ONT POSÉES LES ÉQUIPES

- La flotte : "Un véhicule rendu dans un autre point, il est disponible tout de suite pour une réservation de ce point ?"
- Le comptoir : "Si un GOLD réserve un ECO et qu'il ne reste que des PREMIUM, on lui donne la PREMIUM ? Et le dépôt de garantie, c'est celui de l'ECO ?"
- La facturation : "Une journée offerte avec des points, elle compte pour gagner des points ?"
```
