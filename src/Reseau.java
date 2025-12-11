import java.util.*;

public class Reseau {
    private Set<Generateur> generateurs;
    private Set<Maison> maisons;
    private double cout;
    private static final int LAMBDA = 10; // sévérité de pénalisation, on choisit 10 par défaut

    public Reseau() {
        this.generateurs = new HashSet<>();
        this.maisons = new HashSet<>();
    }

    // setters
    public void setGenerateurs(Set<Generateur> generateurs) {
        this.generateurs = generateurs;
    }

    public void setMaisons(Set<Maison> maisons) {
        this.maisons = maisons;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    // getters
    public Set<Generateur> getGenerateurs() {
        return generateurs;
    }

    public Set<Maison> getMaisons() {
        return maisons;
    }

    public double getCout() {
        return cout;
    }

    // méthodes de gestion

    // ajouter une maison
    public void ajouterMaison(Maison maison) {
        if (!maisons.add(maison)) { // add() retourne false si déjà présent

            // la maison existait → on met à jour l'objet existant
            for (Maison m : maisons) {
                if (m.equals(maison)) {
                    m.setTypeConsommation(maison.getTypeConsommation());
                    System.out.println(
                            "La maison ( " + maison.getNom() + " , "+maison.getTypeConsommation() + " ) est déjà existante, Consommation mise à jour !");
                    return;
                }
            }
        }

        System.out.println("La maison ( "+ maison.getNom() + " , "+maison.getTypeConsommation() + " ) est ajoutée !");
    }

    // ajouter un générateur
    public void ajouterGenerateur(Generateur generateur) {
        if (!generateurs.add(generateur)) { // add() retourne false si déjà présent

            // le generateur existait → on met à jour l'objet existant
            for (Generateur g : generateurs) {
                if (g.equals(generateur)) {
                    g.setCapaciteMax(generateur.getCapaciteMax());
                    System.out.println(
                            
                            "Le generateur ( "  +generateur.getNom()+ " , " +generateur.getCapaciteMax()+ " ) est déjà existant, Capacite mise à jour !");
                    return;
                }
            }
        }

        System.out.println("Le generateur ( "  +generateur.getNom()+ " , " +generateur.getCapaciteMax()+ " ) est ajouté !");
    }

    // trouver une maison
    public Maison trouverMaison(String nomMaison) {
        return maisons.stream().filter(m -> m.getNom().equals(nomMaison)).findFirst().orElse(null);
    }

    // trouver un générateur
    public Generateur trouverGenerateur(String nomGenerateur) {
        return generateurs.stream().filter(g -> g.getNom().equals(nomGenerateur)).findFirst().orElse(null);
    }

    // ajouter une connexion
    public void ajouterConnexion(Generateur generateur, Maison maison) {
        Generateur g = trouverGenerateur(generateur.getNom());
        Maison m = trouverMaison(maison.getNom());
        if (g == null) {
            System.out.println("Générateur "+g.getNom()+" introuvable, veuillez réassayez");
            return;
        }
        if (m == null) {
            System.out.println("Maison "+m.getNom()+" introuvable, veuillez réassayez");
            return;
        }
        if (verifConnexion(generateur, maison)) {
            System.out.println(maison.getNom() + " est déjà connectée à " + generateur.getNom());
            return;
        }
        g.addMaison(m);
        m.setGenerateur(g);
        System.out.println("Connexion ajoutée : " + maison.getNom() + "-->" + generateur.getNom());
    }

    // vérifier qu'il existe une connexion entre un Générateur et une Maison
    public boolean verifConnexion(Generateur generateur, Maison maison) {
        // on suppose que la vérification de l'existence du générateur et de la maison a
        // été déjà vérifié au préalable avant d'appeler la méthode
        return generateur.getMaisons().contains(maison);
    }

    // Supprimer une connexion
    public void supprimerConnexion(Generateur generateur, Maison maison) {
        Generateur g = trouverGenerateur(generateur.getNom());
        Maison m = trouverMaison(maison.getNom());
        if (g == null) {
            System.out.println("Générateur introuvable, veuillez réassayez");
            return;
        }
        if (m == null) {
            System.out.println("Maison introuvable, veuillez réassayez");
            return;
        }
        if (!verifConnexion(g, m)) {
            System.out.println("Connexion introuvable");
            return;
        }
        g.supprimerMaison(m);
        m.setGenerateur(null);
        System.out.println(
                "Connexion entre " + generateur.getNom() + " et " + maison.getNom() + " supprimée avec succès !");

    }

    // supprimer un generateur
    public void supprimerGenerateur(Generateur generateur) {
        if (trouverGenerateur(generateur.getNom()) == null) {
            System.out.println("Generateur introuvable");
            return;
        }
        generateurs.remove(generateur);
    }

    // supprimer une maison
    public void supprimerMaison(Maison maison) {
        if (trouverMaison(maison.getNom()) == null) {
            System.out.println("Maison introuvable");
            return;
        }
        maisons.remove(maison);
    }

    // vérifier si le réseau est valide
    public boolean reseauValide() {
        for (Generateur g : generateurs) {
            if (g.getMaisons() == null) {
                return false;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (Maison m : maisons) {
            int nbrConnexions = 0;
            for (Generateur g : generateurs) {
                if (verifConnexion(g, m)) {
                    nbrConnexions++;
                }
            }
            if (nbrConnexions == 0) {
                System.out.println("Réseau invalide : Pas de connexion pour la maison"+m.getNom());
                return false;
            }
            if (nbrConnexions > 1) {
                builder.append(m.getNom());
                builder.append(" ");
            }
            if (!builder.isEmpty()) {
                System.out.println("Réseau invalide : Trop de connexions pour"+builder.toString());
                return false;
            }
        }
        return true;
    }

    // vérifier si le réseau est valide (cas lecture fichier)
    public String reseauNonValide() {
        for (Generateur g : generateurs) {
            if (g.getMaisons() == null) {
                return "Réseau invalide : Aucune maison connectee au generateur "+g.getNom();
            }
        }
        StringBuilder builder = new StringBuilder();
        for (Maison m : maisons) {
            int nbrConnexions = 0;
            for (Generateur g : generateurs) {
                if (verifConnexion(g, m)) {
                    nbrConnexions++;
                }
            }
            if (nbrConnexions == 0) {
                return "Réseau invalide : Pas de connexion pour la maison"+m.getNom();
            }
            if (nbrConnexions > 1) {
                builder.append(m.getNom());
                builder.append(" ");
            }
            if (!builder.isEmpty()) {
                System.out.println();
                return "Réseau invalide : Trop de connexions pour"+builder.toString();
            }
        }
        return null; //si reseau valide
    }



    // --- Calculs du coût (simplifié pour le moment) ---
    public double calculerCout() {
        double disp = 0.0, surcharge = 0.0;
        double total = 0.0;

        // Moyenne des taux d’utilisation
        List<Double> usages = new ArrayList<>(); // <ug1,ug2,...,ugn>
        double sumUsages = 0;
        for (Generateur g : generateurs) {
            double taux = g.getChargeTotale() / g.getCapaciteMax();
            usages.add(taux);
            sumUsages += taux;
        }

        double moyenne = sumUsages / generateurs.size();
        for (int i = 0; i < generateurs.size(); i++) {
            double taux = usages.get(i);
            disp += Math.abs(taux - moyenne);
            if (taux > 1)
                surcharge += (taux - 1);
        }

        total = disp + LAMBDA * surcharge;
        this.cout = total;
        System.out.println("-------------------------------------------------------");
        System.out.printf("Disp(S) = %.3f | Surcharge(S) = %.3f | Cout(S) = %.3f%n", disp, surcharge, total);
        System.out.println("-------------------------------------------------------");
        return total;
    }

    // --- Affichage du réseau ---
    public void afficherReseau() {
        System.out.println("\n====== RÉSEAU ACTUEL ======");
        for (Generateur g : generateurs) {
            System.out.print(g.getNom() + " (" + g.getCapaciteMax() + " kW) -> ");
            if (g.getMaisons().isEmpty())
                System.out.println("aucune maison");
            else {
                for (Maison m : g.getMaisons()) {
                    System.out.print(m.getNom() + "(" + m.getTypeConsommation().getConsommation() + "kW) ");
                }
                System.out.println();
            }
        }
        System.out.println("=========================\n");
    }

    // méthode pour vérifier que l'entrée du clavier est un entier
    private static int lireEntierAuClavier(Scanner sc, String message) {
        int res = 0;
        boolean lectureOK = false;

        while (!lectureOK) {
            try {
                System.out.print(message);
                res = sc.nextInt();
                if (res < 1 || res > 5) {
                    throw new InputMismatchException();
                }
                lectureOK = true;
            } catch (InputMismatchException e) {
                System.out.println("Il faut taper un nombre entier entre 1 et 5");
                sc.nextLine();
            }
        }
        return res;
    }

    public Object[] lireConnexion(String entite1, String entite2) {
        Maison m = null;
        Generateur g = null;

        // Déterminer les rôles selon la première lettre
        if (entite1.startsWith("M") && entite2.startsWith("G")) {
            m = trouverMaison(entite1);
            g = trouverGenerateur(entite2);
        } else if (entite1.startsWith("G") && entite2.startsWith("M")) {
            g = trouverGenerateur(entite1);
            m = trouverMaison(entite2);
        } else {
            System.out.println("Erreur : il faut une maison (M...) et un générateur (G...) !");

        }

        if (m == null) {
            System.out.println("Maison introuvable");
            return null;
        }
        if (g == null) {
            System.out.println("Generateur introuvable");
            return null;
        }
        return new Object[] { m, g };
    }

    // methode pour récupérer les paires (maison,generateur) ou (generateur, maison)
    public Object[] lireConnexion(Scanner sc, String message) {
        System.out.println(message);
        String ligne = sc.nextLine().trim();
        String[] elements = ligne.split("\\s+"); // séparer la ligne par espace ou tabulation et la transformer en
                                                 // tableau ["G1","M1"]
        if (elements.length != 2) {
            System.out.println("Format invalide !");
        }

        String entite1 = elements[0].toUpperCase();
        String entite2 = elements[1].toUpperCase();

        Maison m = null;
        Generateur g = null;

        // Déterminer les rôles selon la première lettre
        if (entite1.startsWith("M") && entite2.startsWith("G")) {
            m = trouverMaison(entite1);
            g = trouverGenerateur(entite2);
        } else if (entite1.startsWith("G") && entite2.startsWith("M")) {
            g = trouverGenerateur(entite1);
            m = trouverMaison(entite2);
        } else {
            System.out.println("Erreur : il faut une maison (M...) et un générateur (G...) !");

        }

        if (m == null) {
            System.out.println("Maison introuvable");
            return null;
        }
        if (g == null) {
            System.out.println("Generateur introuvable");
            return null;
        }
        return new Object[] { m, g };

    }

    // -- premier menu --
    public static void construireReseau(Reseau reseau, Scanner sc) {
        int choix;
        do {
            System.out.println("""


                    ==== MENU PRINCIPAL ====
                    1) Ajouter un générateur
                    2) Ajouter une maison
                    3) Ajouter une connexion
                    4) Supprimer une connexion
                    5) Fin
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
                    System.out.print("Nom et type (BASSE/NORMALE/FORTE) : ");
                    String nom = sc.next().toUpperCase();
                    String type = sc.next().toUpperCase();
                    Consommation consommation;
                    switch (type) {
                        case "BASSE" -> {
                            consommation = Consommation.BASSE;
                        }
                        case "NORMALE" -> {
                            consommation = Consommation.NORMALE;
                        }
                        case "FORTE" -> {
                            consommation = Consommation.FORTE;
                        }
                        default -> {
                            consommation = Consommation.NORMALE;
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
                    Object[] MaisonGen = reseau.lireConnexion(sc, "Entrer une connexion (ex M1 G1 ou G1 M1) : ");
                    if (MaisonGen != null) {
                        Maison m = (Maison) MaisonGen[0];
                        Generateur g = (Generateur) MaisonGen[1];
                        reseau.ajouterConnexion(g, m);
                    }

                }
                case 4 -> {
                    sc.nextLine();
                    Object[] MaisonGen = reseau.lireConnexion(sc, "Supprimer une connexion (ex M1 G1 ou G1 M1) : ");
                    if (MaisonGen != null) {
                        Maison m = (Maison) MaisonGen[0];
                        Generateur g = (Generateur) MaisonGen[1];
                        reseau.supprimerConnexion(g, m);
                    }
                }
                case 5 -> {
                    if (reseau.reseauValide()) {
                        System.out.println("Réseau valide !");
                        reseauMenu(reseau, sc);
                    } else {
                        System.out.println("""
                                Veuillez corriger les connexions puis rééssayer.""");
                        choix = -1;// pour revenir au menu principal pour corriger les connexions
                    }
                }

            }
        } while (choix != 5);
        sc.close();
    }

    // --- Second menu ---
    public static void reseauMenu(Reseau reseau, Scanner sc) {
        int choix;
        do {
            System.out.println("""
                    ==== MENU RÉSEAU ====
                    1) Calculer le coût du réseau
                    2) Modifier une connexion
                    3) Afficher le réseau
                    4) Fin
                    =====================
                    """);
            System.out.print("Votre choix : ");
            choix = sc.nextInt();

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
                case 4 -> {
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
        } while (choix != 4);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Reseau reseau = new Reseau();
        if (args.length == 0) {
            construireReseau(reseau, sc);
            reseauMenu(reseau, sc);
        } else if(args.length == 2) {
            ParserReseau pr =new ParserReseau();
            reseau=pr.lireReseau(args[0]);
            if(reseau!=null){
                reseau.afficherReseau();
            }
            
        }else{
            System.err.println("Usage : java Reseau <chemin_vers_fichier> <LAMBDA>");
        }

    }

}