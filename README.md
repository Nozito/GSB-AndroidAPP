# GSB - Application Android de Gestion des Praticiens et Médicaments

Cette application Android permet aux utilisateurs de consulter et de gérer leurs praticiens ainsi que les visites associées. Elle offre la possibilité de créer, modifier et supprimer des visites. De plus, l'application permet de consulter un catalogue de médicaments avec des informations détaillées sur chaque produit. L'accès à l'application est sécurisé via un système de login.

## Fonctionnalités

### 1. **Gestion des Praticiens**
   - L'utilisateur peut voir la liste de ses praticiens.
   - Chaque praticien est associé à une liste de visites.
   - Les visites peuvent être créées, modifiées ou supprimées pour chaque praticien.

### 2. **Gestion des Visites**
   - L'utilisateur peut ajouter une nouvelle visite pour un praticien.
   - Chaque visite peut être mise à jour avec des détails comme la date, le motif et des commentaires.
   - Les visites peuvent être supprimées si nécessaire.

### 3. **Consultation du Catalogue de Médicaments**
   - L'utilisateur peut consulter un catalogue de médicaments.
   - Chaque médicament dans le catalogue contient des informations détaillées telles que :
     - Nom commercial
     - Dépôt légal
     - Famille
     - Effets thérapeutiques
     - Contre-indications
     - Interactions médicamenteuses
     - Posologie
   - L'utilisateur peut cliquer sur un médicament pour afficher ses informations détaillées dans un `BottomSheet`.

### 4. **Connexion Sécurisée**
   - L'application utilise un système de login pour sécuriser l'accès.
   - Seuls les utilisateurs authentifiés peuvent accéder aux informations des praticiens, visites et médicaments.

---

## Objectifs

L'objectif de cette application est de fournir un outil simple et sécurisé pour la gestion des visites et des médicaments pour les visiteurs. L'application permet une gestion fluide des rendez-vous et un accès rapide aux informations concernant les médicaments, tout en garantissant la sécurité des données grâce à l'authentification des utilisateurs.

---

## Technologies Utilisées

- **Android SDK** : Développement natif de l'application mobile en Java.
- **Retrofit** : Pour les appels API HTTP et la gestion des requêtes vers le backend.
- **Gson** : Pour le parsing des données JSON récupérées via l'API.
- **RecyclerView** : Affichage de la liste des praticiens, visites et médicaments.
- **BottomSheetDialog** : Affichage des détails des médicaments dans un BottomSheet.
- **CardView** : Utilisé pour afficher les informations des médicaments et des visites sous forme de cartes.

---

### Maquette de l'application

<img width="869" alt="Screenshot 2025-04-27 at 19 43 34" src="https://github.com/user-attachments/assets/e1b8b316-2b18-41a7-aa13-63c4b1359489" />


---

## Installation

1. Clonez le dépôt sur votre machine locale :

    ```bash
    git clone https://github.com/Nozito/GSB-AndroidAPP.git
    ```

2. Ouvrez le projet dans **Android Studio**.
3. Assurez-vous que toutes les dépendances sont installées et synchronisées. Si ce n'est pas le cas, Android Studio vous demandera de le faire.

---

## Fonctionnement de l'Application

### 1. **Page d'Accueil et Connexion**
   - L'utilisateur doit se connecter pour accéder à l'application.
   - Si l'utilisateur est authentifié, il peut accéder à la liste des praticiens et des visites associées.
   - Lancement de l'application avec une animation d'entrée.
   - Animation après la connexion, le temps de chargement des données.
     
<p align="center">
<img src="https://github.com/user-attachments/assets/6604be87-6f31-416e-bc5a-41d68e32a010" width="300" style="display:inline-block;">
<img src="https://github.com/user-attachments/assets/25ad8f52-7446-47c6-83cf-2ac2b8c61633" width="300" style="display:inline-block; margin-right:10px;">
<img src="https://github.com/user-attachments/assets/fb4fe7ca-c57e-48ec-be9c-607b6763489f" width="300" style="display:inline-block;">
</p>


### 2. **Gestion des Praticiens**
   - Une fois connecté, l'utilisateur voit une liste de ses praticiens.
   - Pour chaque praticien, l'utilisateur peut ajouter, modifier ou supprimer des visites.

<p align="center">
  <img src="https://github.com/user-attachments/assets/e89b961e-f8a1-42e6-8300-a93f4eebfb7f" width="300" style="display:inline-block;">
  <img src="https://github.com/user-attachments/assets/7a6b039a-5a89-43d5-b3e7-a4d8c6eedee1" width="300" style="display:inline-block; margin-right:10px;">
  <img src="https://github.com/user-attachments/assets/7e4c0c58-2d00-440d-a588-51c374689469" width="300" style="display:inline-block;">
</p>


### 3. **Consulter les Médicaments**
   - L'utilisateur peut consulter le catalogue de médicaments.
   - Chaque médicament affiche des informations détaillées, telles que le nom, la posologie, les effets et les contre-indications.
   - Lors de la consultation des médicaments, un BottomSheet affiche les informations détaillées lorsque l'utilisateur clique sur un médicament.

<p align="center">
<img src="https://github.com/user-attachments/assets/53c31535-4d85-4f52-ae0e-1fc8a6b71efa" width="300" style="display:inline-block">
<img src="https://github.com/user-attachments/assets/a6b03467-50c3-4ec1-8669-7692234ad8fe" width="300" style="display:inline-block; margin-right:10px;">
</p>

### 4. **Interactions avec l'API**
   - L'application se connecte à une API pour récupérer les données des praticiens, des visites et des médicaments.
   - Toutes les données affichées dans l'application sont récupérées en temps réel à partir de l'API, garantissant des informations toujours à jour.
