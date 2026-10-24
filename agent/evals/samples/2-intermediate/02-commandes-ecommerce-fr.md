### Exemple 4 - Niveau intermédiaire
```
Système de gestion de commandes e-commerce.

Contexte :
Un client passe une commande avec plusieurs lignes de produits.

Règles métier :
- Chaque commande a un statut : EN_ATTENTE, PAYÉE, EXPÉDIÉE, LIVRÉE, ANNULÉE.
- Un email est envoyé à chaque changement de statut.
- Chaque ligne de commande a un produit, une quantité et un prix unitaire.
- Le total de la commande est la somme des lignes de commande.
- Chaque produit a un stock disponible.
- Lorsqu'une commande est passée, le stock des produits est vérifié et réservé.
- Si le stock est insuffisant, la commande ne peut pas être validée.
- Les retours sont possibles sous 14 jours après livraison.
- Un retour met à jour le stock et le statut de la commande.

Opérations nécessaires :
- Ajouter un produit au catalogue avec son stock
- Passer une commande avec plusieurs lignes
- Payer une commande
- Expédier une commande
- Marquer une commande comme livrée
- Annuler une commande
- Retourner une commande livrée
- Consulter une commande et son total
```
