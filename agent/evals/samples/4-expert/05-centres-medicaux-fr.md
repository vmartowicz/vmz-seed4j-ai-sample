### Exemple 13 - Niveau expert
```
Je coordonne "MédiRhône", un groupe de 3 centres médicaux (Lyon 7e, Vénissieux, Vaulx-en-Velin) avec 25 médecins, un laboratoire d'analyses et un service d'imagerie. Aujourd'hui l'accueil a son agenda, les médecins leur logiciel de dossiers, le labo et l'imagerie leurs propres outils, et la facturation ressaisit tout. Résultat : des scanners avec produit de contraste faits sans bilan rénal, des résultats critiques vus trois jours plus tard, des feuilles de soins envoyées au mauvais assuré...

Je veux une application qui remplace tout ça. Chaque équipe a ses règles et son vocabulaire, je vous les décris équipe par équipe.

═══════════════════════════════════════════════════════════════

1. L'ACCUEIL (les patients et les rendez-vous)

- Un patient : nom, prénom, date de naissance, numéro de sécurité sociale (13 chiffres + clé de 2 chiffres), téléphone, médecin traitant
- Créneaux de 15 minutes pour les généralistes, 30 minutes pour les spécialistes
- Chaque médecin garde 2 créneaux par jour pour les urgences du jour même, non réservables en ligne
- Annulation possible jusqu'à 24 h avant le rendez-vous
- 3 rendez-vous non honorés sur 12 mois : le patient ne peut plus réserver en ligne, seulement par téléphone
- Un rendez-vous non honoré n'est jamais facturé
- Un mineur de moins de 16 ans doit venir accompagné
- L'accueil ne voit pas le contenu des consultations, seulement les rendez-vous

═══════════════════════════════════════════════════════════════

2. LES MÉDECINS (consultations et ordonnances)

- Une consultation : motif, examen clinique, diagnostic, ordonnance éventuelle
- Une consultation de généraliste dure 20 minutes
- Une consultation clôturée ne peut plus être modifiée, on peut seulement ajouter un complément daté et signé
- Le dossier médical contient les allergies, les antécédents et les traitements en cours
- Prescrire un médicament auquel le patient est allergique doit être bloqué
- Une ordonnance est valable 3 mois
- Une ordonnance peut prescrire des médicaments, des analyses de laboratoire ou des examens d'imagerie
- Un résultat anormal doit d'abord être annoncé au patient par son médecin

═══════════════════════════════════════════════════════════════

3. LE LABORATOIRE (prélèvements et résultats)

- Le labo ne fait un prélèvement que sur ordonnance de moins d'un an
- Certaines analyses demandent d'être à jeun (glycémie, bilan lipidique) : le prélèvement est refusé sinon
- Un échantillon doit être analysé dans les 4 h après le prélèvement, sinon il faut reprélever
- Chaque résultat est validé par un biologiste avant d'être diffusé
- Valeurs critiques (potassium > 6 mmol/L, glycémie < 0,5 g/L...) : le médecin prescripteur doit être prévenu dans l'heure
- Dès qu'un résultat est validé, il est visible par le patient sur son espace en ligne
- Les résultats sont conservés et réutilisables par l'imagerie et les médecins

═══════════════════════════════════════════════════════════════

4. L'IMAGERIE (radios, échographies, scanners, IRM)

- Tout examen se fait sur ordonnance
- Un scanner avec produit de contraste nécessite une créatinine de moins de 3 mois
- Radio et scanner pour une femme de 15 à 50 ans : il faut vérifier qu'elle n'est pas enceinte
- IRM interdite aux porteurs de pacemaker
- Le compte rendu est envoyé au médecin prescripteur sous 48 h
- Le patient reçoit ses images le jour même, le compte rendu en même temps que le médecin

═══════════════════════════════════════════════════════════════

5. LA FACTURATION (assurés et remboursements)

- Consultation de généraliste : 30 €, de spécialiste : 50 €. Certains spécialistes pratiquent des dépassements d'honoraires
- La Sécurité sociale rembourse 70 % du tarif, seulement 30 % si le patient n'a pas déclaré de médecin traitant
- Tiers payant (le patient ne paie rien) pour les patients en ALD et les bénéficiaires de la Complémentaire santé solidaire
- Un enfant est rattaché à l'assuré qui le déclare : la feuille de soins part au nom de cet assuré
- La feuille de soins électronique est envoyée sous 8 jours
- Un rendez-vous non honoré sans prévenir est facturé 30 €
- Une ordonnance d'imagerie est valable 1 an

═══════════════════════════════════════════════════════════════

🎯 CE QUE JE VEUX EN PRIORITÉ

1. La prise de rendez-vous
2. La consultation avec le dossier et l'ordonnance
3. Le laboratoire et la diffusion des résultats
4. L'imagerie
5. La facturation et les feuilles de soins

═══════════════════════════════════════════════════════════════

❓ LES QUESTIONS QUE M'ONT POSÉES LES ÉQUIPES

- L'accueil : "Un patient qui change de médecin traitant, on garde ses rendez-vous déjà pris avec l'ancien ?"
- Le laboratoire : "Un résultat critique validé un samedi soir, son médecin n'est pas là : on prévient qui ?"
- La facturation : "Un enfant déclaré sur les deux parents, on envoie la feuille de soins à qui ?"
```
