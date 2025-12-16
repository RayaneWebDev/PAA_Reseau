import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    // méthode pour vérifier que l'entrée du clavier est un entier
    public static int lireEntierAuClavier(Scanner sc, String message) {
        int res = 0;
        boolean lectureOK = false;

        while (!lectureOK) {
            try {
                System.out.print(message);
                res = sc.nextInt();
                if (res < 1 || res > 7) {
                    throw new InputMismatchException();
                }
                lectureOK = true;
            } catch (InputMismatchException e) {
                System.out.println("Il faut taper un nombre entier entre 1 et 7");
                sc.nextLine();
            }
        }
        return res;
    }

    public static void constructionManuelle(Reseau reseau, Scanner sc){
        int choix;
        do {
            System.out.println("""


                    ==== CRÉER LE RÉSEAU ====
                    1) Ajouter un générateur
                    2) Ajouter une maison
                    3) Ajouter une connexion
                    4) Supprimer un générateur
                    5) Supprimer une maison
                    6) Supprimer une connexion
                    7) Fin
                    =========================


                    """);
            choix = lireEntierAuClavier(sc, "choix = ");// Récupérer le choix de l'utilisateur

            switch (choix) {
                case 1 -> {// ajout d'un générateur
                    System.out.print("Nom et capacité (ex: G1 60) : ");
                    String nom = sc.next().toUpperCase();
                    double cap = sc.nextDouble();
                    if (!nom.startsWith("G") || cap < 0) {// verifier la saisie
                        System.out.println("Erreur : il faut Générateur (G...) et sa capacité maximale ( > 0 ) !");
                    } else {
                        reseau.ajouterGenerateur(new Generateur(nom, cap));
                    }

                }
                case 2 -> {// ajout d'une maison
                    System.out.print("Nom et type (BASSE/NORMAL/FORTE) : ");
                    String nom = sc.next().toUpperCase();
                    String type = sc.next().toUpperCase();
                    Consommation consommation;
                    switch (type) {
                        case "BASSE" -> {
                            consommation = Consommation.BASSE;
                        }
                        case "NORMAL" -> {
                            consommation = Consommation.NORMAL;
                        }
                        case "FORTE" -> {
                            consommation = Consommation.FORTE;
                        }
                        default -> {
                            consommation = Consommation.NORMAL;
                            System.out.println("Par défaut, Consommation : NORMALE ");
                        }

                    }
                    if (!nom.startsWith("M")) {
                        System.out.println("Erreur : il faut Maison (M...) et son type (BASSE, NORMALE ou FORTE) !");
                    } else {
                        reseau.ajouterMaison(new Maison(nom, consommation));
                    }

                }
                case 3 -> {
                    sc.nextLine();
                    System.out.print("\nListe des maisons : ");
                    reseau.afficherMaisons();
                    System.out.print("\n\nListe des générateurs : ");
                    reseau.afficherGenerateurs();
                    Object[] MaisonGen = reseau.lireConnexion(sc, "\n\nEntrer une connexion (ex M1 G1 ou G1 M1) : ");
                    if (MaisonGen != null) {
                        Maison m = (Maison) MaisonGen[0];
                        Generateur g = (Generateur) MaisonGen[1];
                        reseau.ajouterConnexion(g, m);
                    }

                }
                case 4 -> {// suppression d'un generateur
                    System.out.print("Nom du générateur à supprimer : ");
                    String nomGenerateur = sc.next().toUpperCase();
                    reseau.supprimerGenerateur(reseau.trouverGenerateur(nomGenerateur));
                }
                case 5 -> {
                    System.out.print("Nom de la maison à supprimer : ");
                    String nomMaison= sc.next().toUpperCase();
                    reseau.supprimerMaison(reseau.trouverMaison(nomMaison));
                }
                case 6 -> {
                    sc.nextLine();
                    Object[] MaisonGen = reseau.lireConnexion(sc, "Supprimer une connexion (ex M1 G1 ou G1 M1) : ");
                    if (MaisonGen != null) {
                        Maison m = (Maison) MaisonGen[0];
                        Generateur g = (Generateur) MaisonGen[1];
                        reseau.supprimerConnexion(g, m);
                    }
                }
                case 7 -> {
                    if (reseau.reseauValide()) {
                        System.out.println("Réseau valide !");
                        manipulerReseau(reseau, sc);
                    } else {
                        System.out.println("""
                                Veuillez corriger les connexions puis rééssayer.""");
                        choix = -1;// pour revenir au menu principal pour corriger les connexions
                    }
                }

            }
        } while (choix != 7);
    }
    
    public static void manipulerReseau(Reseau reseau, Scanner sc) {
        int choix;
        do {
            System.out.println("""
                    ==== MENU RÉSEAU ====
                    1) Calculer le coût du réseau
                    2) Modifier une connexion
                    3) Afficher le réseau
                    4) Modifier le réseau
                    5) Fin
                    =====================
                    """);
            choix = lireEntierAuClavier(sc, "choix = ");// Récupérer le choix de l'utilisateur

            switch (choix) {
                case 1 -> reseau.calculerCout();
                case 2 -> {
                    sc.nextLine();
                    Object[] MaisonGen1 = reseau.lireConnexion(sc,
                            "Veuillez saisir la connexion que vous souhaitez modifier : ");
                    if (MaisonGen1 != null) {
                        Maison m1 = (Maison) MaisonGen1[0];
                        Generateur g1 = (Generateur) MaisonGen1[1];
                        reseau.supprimerConnexion(g1, m1);
                        Object[] MaisonGen2 = reseau.lireConnexion(sc, "Veuillez saisir la nouvelle connexion : ");
                        if (MaisonGen2 != null) {
                            Maison m2 = (Maison) MaisonGen2[0];
                            Generateur g2 = (Generateur) MaisonGen2[1];
                            reseau.ajouterConnexion(g2, m2);
                        }
                    }

                }
                case 3 -> reseau.afficherReseau();
                case 4 -> constructionManuelle(reseau, sc);
                case 5 -> {
                    if (reseau.reseauValide()) {
                        System.out.println("Merci, à bientot !");
                    } else {
                        System.out.println("""
                                Réseau invalide.
                                Corrigez les connexions avant de continuer.""");
                        choix = -1;// pour revenir au menu principal pour corriger les connexions
                    }
                }
            }
        } while (choix !=5);
    }

     public static void constructionFichier(Reseau reseau, Scanner sc){
        int choix;
        do {
            System.out.println("""


                    ==== MENU ====
                    1) Résolution automatique
                    2) Sauvegarder La solution actuelle 
                    3) Fin
                    =========================


                    """);
            choix = lireEntierAuClavier(sc, "choix = ");// Récupérer le choix de l'utilisateur

            switch (choix) {
                case 1 -> {// résolution automatique
                    System.out.println("Recherche d'une solution optimale : ");
                    reseau.optimiserRecuitAvance(
                        2000,
                        1.0,
                        0.999,
                        0L,
                        0.4
                    );
                    System.out.println("-------------------------------------------------");
                    System.out.println("--------Nouveau Reseau apres optimisation--------");
                    System.out.println("-------------------------------------------------");
                    reseau.afficherReseau();

                }
                case 2 -> {//sauvegarder la solution actuelle
                    System.out.print("Entrez un nom de fichier pour enregistrer la solution actuelle : ");
                    String nomFichier = sc.next();
                    reseau.sauvegarderDansFichier(nomFichier);

                }
                case 3 -> {
                    System.out.println(" Merci, à Bientot ! ");
                }

            }
        } while (choix != 3);
        sc.close();
    }
}
