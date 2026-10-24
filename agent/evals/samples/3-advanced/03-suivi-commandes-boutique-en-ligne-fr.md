### Exemple 7 - Niveau avancé
```
Je veux créer un système de suivi de commandes pour ma boutique en ligne.

Q: C'est pour quel type de produits ?
R: Vêtements et accessoires de mode

Q: Quelles sont les étapes d'une commande ?
R:
1. Le client passe commande (panier → commande)
2. On prépare la commande (1-2 jours)
3. On expédie (transporteur)
4. Client reçoit
5. Client peut retourner sous 14 jours

Q: Quelles sont vos règles de prix ?
R:
- Livraison gratuite si commande > 50€
- Sinon 5€ de frais de port
- Retours gratuits toujours
- Remise fidélité : -10% dès 5 commandes

Q: Comment gérer les stocks ?
R:
- Vérifier disponibilité avant validation commande
- Réserver le stock dès validation
- Libérer si annulation ou retour
- Alert stock si < 5 unités

Q: Statuts de commande ?
R:
- EN_ATTENTE_PAIEMENT
- PAYÉE
- EN_PRÉPARATION
- EXPÉDIÉE
- LIVRÉE
- RETOURNÉE
- ANNULÉE

Q: Règles d'annulation ?
R:
- Gratuite si pas encore expédiée
- Impossible si déjà expédiée (faire un retour)
- Remboursement sous 5 jours

Q: Quelles opérations voulez-vous ?
R:
- Client : passer commande, payer, suivre sa commande, annuler, demander un retour
- Boutique : préparer une commande, l'expédier avec le numéro de suivi du transporteur, confirmer la livraison, réceptionner un retour et rembourser
- Stock : réapprovisionner un produit, voir les produits en alerte de stock

Générez-moi le modèle de domaine complet avec ces informations.
```
