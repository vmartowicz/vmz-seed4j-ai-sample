### Exemple 12 - Niveau expert
```
Je dirige la "Banque des Canuts", une banque régionale avec 8 agences autour de Lyon et environ 40 000 clients. Notre système actuel date de 20 ans et chaque service a ajouté ses propres outils par-dessus. Résultat : des comptes gelés qui continuent à faire des virements, des crédits accordés à des clients à découvert depuis des mois, des cartes bloquées par la fraude sans que l'agence soit au courant...

Je veux une application qui remplace tout ça. Chaque service a ses règles et son vocabulaire, je vous les décris service par service.

═══════════════════════════════════════════════════════════════

1. L'ENTRÉE EN RELATION (les nouveaux clients)

- Un prospect devient client une fois son dossier validé : pièce d'identité en cours de validité, justificatif de domicile de moins de 3 mois, justificatif de revenus
- Il faut avoir 18 ans. Entre 12 et 17 ans, un compte jeune peut être ouvert avec l'accord d'un représentant légal
- Chaque client a un niveau de risque : FAIBLE, MOYEN ou ÉLEVÉ. Une personne politiquement exposée est toujours en risque ÉLEVÉ
- Le dossier doit être revu tous les 3 ans (tous les ans pour le risque ÉLEVÉ)
- Un client dont la revue n'est pas faite à temps voit ses comptes gelés
- Un client peut avoir plusieurs comptes, et un compte peut avoir deux titulaires (compte joint)

═══════════════════════════════════════════════════════════════

2. LES COMPTES ET LES CARTES (les titulaires)

- Types de comptes : COURANT, LIVRET A, COMPTE JEUNE
- IBAN français : FR76 suivi de 23 caractères, avec la clé de contrôle
- LIVRET A : plafonné à 22 950 €, pas de carte, pas de découvert, intérêts calculés par quinzaine
- Découvert autorisé sur le compte COURANT : jusqu'à un mois de revenus, au maximum 2 000 €. Agios à 16 % par an
- Au-delà du découvert autorisé : 8 € de frais par opération, plafonnés à 80 € par mois
- Frais de tenue de compte : 2 € par mois, gratuits pour les moins de 25 ans
- Cartes : CLASSIC (paiements 1 500 € sur 30 jours glissants, retraits 500 € sur 7 jours) et GOLD (paiements 5 000 €, retraits 1 000 €)
- Une carte est bloquée après 3 codes faux. Une carte perdue ou volée est mise en opposition définitivement
- Pas de carte GOLD sur un COMPTE JEUNE

═══════════════════════════════════════════════════════════════

3. LES PAIEMENTS (virements et prélèvements)

- Un virement a un donneur d'ordre, un bénéficiaire (IBAN + nom), un montant et un motif
- Virement standard : exécuté le jour ouvré suivant s'il est saisi avant 16 h, sinon le surlendemain ouvré
- Virement instantané : exécuté en 10 secondes, 7 jours sur 7, jusqu'à 15 000 €
- Un nouveau bénéficiaire peut être utilisé immédiatement après son ajout
- Un virement est refusé si le solde est insuffisant
- Virements permanents : montant fixe, fréquence mensuelle ou trimestrielle, date de fin facultative
- Un prélèvement nécessite un mandat signé. Le client peut demander son remboursement pendant 8 semaines
- Un prélèvement rejeté coûte 20 € au client

═══════════════════════════════════════════════════════════════

4. LE CRÉDIT (prêts personnels)

- Montant de 1 000 € à 75 000 €, durée de 12 à 84 mois
- Taux d'endettement maximum de 35 %, en comptant la nouvelle mensualité
- L'emprunteur doit être client depuis 6 mois et avoir moins de 75 ans à la fin du prêt
- Pas de prêt si le client a été en découvert non autorisé dans les 3 derniers mois
- Assurance emprunteur obligatoire
- Délai de rétractation de 14 jours après la signature : les fonds ne sont versés qu'après
- Les mensualités sont prélevées sur le compte COURANT de l'emprunteur
- 2 mensualités impayées : le dossier passe au recouvrement
- Remboursement anticipé possible, avec une pénalité de 1 % si plus de 10 000 € sont remboursés sur 12 mois

═══════════════════════════════════════════════════════════════

5. LA CONFORMITÉ ET LA FRAUDE

- Tout bénéficiaire est vérifié contre la liste des sanctions. Un bénéficiaire sur la liste : le virement est bloqué et le compte est gelé
- Un virement de plus de 10 000 € vers un nouveau bénéficiaire est retenu 24 h, le temps qu'un conseiller appelle le client
- Deux paiements par carte dans deux pays différents à moins d'une heure d'écart : la carte est bloquée
- Dépôts d'espèces de plus de 10 000 € sur un mois : déclaration de soupçon obligatoire, sans prévenir le client
- Les dossiers clients sont revus tous les 2 ans
- Un compte gelé n'accepte plus aucune opération sortante
- Les frais sont gratuits pour les COMPTES JEUNES jusqu'à 26 ans

═══════════════════════════════════════════════════════════════

🎯 CE QUE JE VEUX EN PRIORITÉ

1. L'ouverture d'un compte avec la validation du dossier
2. Les virements et les prélèvements
3. Le découvert et les frais
4. Les cartes et leurs plafonds
5. Les prêts personnels

═══════════════════════════════════════════════════════════════

❓ LES QUESTIONS QUE M'ONT POSÉES LES SERVICES

- Les paiements : "La mensualité d'un prêt sur un compte gelé, on la prélève quand même ?"
- Les comptes : "Un compte joint dont un seul des deux titulaires n'a pas fait sa revue, il est gelé pour les deux ?"
- Le crédit : "Le découvert autorisé, il compte dans le taux d'endettement ?"
```
