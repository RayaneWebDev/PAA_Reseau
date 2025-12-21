====================================================
               PROJET RÉSEAU ÉLECTRIQUE
            Optimisation par recuit simulé
====================================================
AUTEURS :
- TAOUACHE Rayane | Num Étudiant : 22522803
- BENHAMMA Dania  | Num Étudiant : 22416535

GROUPE : TP du Jeudi 15h45-18h45
DATE DE REMISE DU TRAVAIL : 21 Décembre 2025

----------------------------------------------------
1. PRÉSENTATION DU PROJET
----------------------------------------------------
Ce projet consiste à modéliser un réseau électrique
composé de générateurs et de maisons.

Chaque maison doit être connectée à un seul
générateur, et chaque générateur possède une
capacité maximale de production.

Le programme permet de créer un réseau, de vérifier
sa validité, de calculer son coût et d’optimiser les
connexions automatiquement.

----------------------------------------------------

2. OBJECTIF DU PROJET
----------------------------------------------------
L’objectif est de minimiser le coût global du réseau
électrique en :
- équilibrant la charge entre les générateurs,
- limitant les surcharges,
- respectant les contraintes du réseau.

----------------------------------------------------

3. MODÉLISATION DU RÉSEAU
----------------------------------------------------
Le réseau est composé de :

- Générateurs :
  * nom
  * capacité maximale (kW)
  * ensemble de maisons connectées

- Maisons :
  * nom
  * type de consommation (BASSE, NORMALE, FORTE)
  * générateur associé

Les relations sont bidirectionnelles et stockées à
l’aide de structures Set afin d’éviter les doublons.

----------------------------------------------------

4. CALCUL DU COÛT
----------------------------------------------------
Le coût d’un réseau est défini par :

- Dispersion :
  somme des écarts entre les taux d’utilisation des
  générateurs et la moyenne.

- Surcharge :
  pénalisation lorsque la capacité d’un générateur
  est dépassée.

Formule :
Coût = Dispersion + λ × Surcharge

où λ est un facteur de pénalisation.

----------------------------------------------------
                Arborescence du code
----------------------------------------------------
src/
    ├── instances_Test/          # Contient les fichiers d'instances de test du réseau (ex: instance1.txt)
    ├── reseaux_sauvegardes/     # Contient les fichiers de sauvegarde des réseaux optimisés
    ├── Consommation.java        # Enum définissant les types de consommation d'une maison (BASSE, NORMAL, FORTE)
    ├── Generateur.java          # Classe représentant un générateur avec sa capacité et les maisons connectées
    ├── Maison.java              # Classe représentant une maison avec son type de consommation et le générateur auquel elle est connectée
    ├── Menu.java                # Classe gérant l'affichage des menu ainsi que les interactions utilisateur
    ├── ParserReseau.java        # Classe pour lire un fichier d'instance et construire un objet Reseau (vérification et gestion des erreurs)
    └── Reseau.java              # Classe principale contenant la méthode main et toutes les méthodes liées au réseau, y compris le calcul du coût et l’optimisation par recuit simulé


----------------------------------------------------
5. CLASSE D’EXÉCUTION
----------------------------------------------------
La classe utilisée pour exécuter le programme est : Reseau


La classe Menu regroupe toutes les méthodes liées
aux menus et à l’interaction utilisateur.

----------------------------------------------------
6. ALGORITHME DE RÉSOLUTION AUTOMATIQUE
----------------------------------------------------
Un algorithme de résolution automatique plus
efficace que l’algorithme proposé dans le sujet
a été implémenté.

Il s’agit d’un algorithme de recuit simulé avancé.

Principe :
- On part d’un réseau valide initial.
- À chaque itération, une modification locale est
  proposée :
    * Move : déplacer une maison vers un autre
      générateur.
    * Swap : échanger les générateurs de deux maisons.
- Si la modification améliore le coût, elle est
  acceptée.
- Sinon, elle peut être acceptée avec une certaine
  probabilité dépendant d’une température décroissante.
- La meilleure solution rencontrée est sauvegardée.
- À la fin, le réseau est restauré dans son état
  optimal trouvé.

Cet algorithme permet d’éviter les minima locaux et
d’obtenir de meilleures solutions qu’un algorithme
naïf.

Voici un lien vers un article wikipedia pour plus de détails sur l'algorithme de recuit simulé : "https://fr.wikipedia.org/wiki/Recuit_simul%C3%A9"

----------------------------------------------------
7. FONCTIONNALITÉS IMPLÉMENTÉES
----------------------------------------------------
Fonctionnalités correctement implémentées :

- Création de générateurs
- Création de maisons
- Connexion maison ↔ générateur
- Modification du réseau
- Vérification de la validité du réseau
- Calcul du coût du réseau
- Lecture d'un réseau existant à partir d'un fichier texte
- Optimisation automatique du coût du réseau
- Sauvegarde du réseau dans un fichier texte

 ********** FONCTIONNALITES AJOUTEES *********** :
 - Suppression d'un generateur ou maison
 - Affichage de la liste des générateurs et maisons quand l'utilisateur veut ajouter une connexion
 - Sauvegarde du réseau créé manuellement
 - Implémentation d'algorithme d'optimisation du coût plus efficace


----------------------------------------------------

8. FONCTIONNALITÉS MANQUANTES OU LIMITATIONS
----------------------------------------------------
- L’algorithme ne garantit pas une solution optimale
- Les résultats peuvent varier selon la seed
- Temps de calcul dépendant du nombre d’itérations
- Interface en ligne de commande uniquement

----------------------------------------------------
9. COMPILATION ET EXÉCUTION
----------------------------------------------------
Prérequis : Java JDK (version 17 ou supérieure recommandée).

Ouvrez un terminal à la racine du projet (Celui qui contient le dossier src et le fichier README.txt):

1. Compilation :
  Tapez la commande suivante pour compiler tous les fichiers vers le dossier bin :

  javac -d bin src/*.java

  NOTE : Si vous obtenez une erreur indiquant que le dossier 'bin' est introuvable, 
  merci de le créer manuellement avant de relancer la compilation :

    Windows : mkdir bin
    Linux/Mac : mkdir bin

2. Exécution :
  A. Importer un fichier contenant le réseau : 
    Placez votre fichier .txt dans le dossier "src/instances_Test/" 
    Lancez ensuite la commande :

    java -cp bin Reseau <nom_du_fichier> <LAMBDA>

  B. Construire le réseau manuellement :
    Lancez simplement :

    java -cp bin Reseau 

N.B. : Toutes les commandes doivent être lancées depuis la racine du projet (le dossier contenant src et bin). 
Si vous avez navigué dans src/instances_Test pour ajouter un fichier, n'oubliez pas de revenir en arrière avec la
 commande cd .. avant de compiler ou d'exécuter.

----------------------------------------------------

10. SAUVEGARDE DU RÉSEAU
----------------------------------------------------
Le réseau optimisé peut être sauvegardé dans un
fichier texte au format :

generateur(G1,60).
maison(M1,NORMALE).
connexion(G1,M1).

Le fichier est enregistré dans le dossier src/reseaux_sauvegardes/

----------------------------------------------------

FIN
====================================================
