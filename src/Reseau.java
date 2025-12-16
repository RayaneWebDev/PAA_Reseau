import java.io.*;
import java.util.*;

public class Reseau {
    private Set<Generateur> generateurs;
    private Set<Maison> maisons;
    private double cout;
    private int lambda = 10; // sévérité de pénalisation, on choisit 10 par défaut

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

    public void setLambda(int lambda){
        this.lambda=lambda;
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

    public int getLambda(){
        return lambda;
    }

    // méthodes de gestion

    //afficher les maisons
    public void afficherMaisons(){
        for(Maison m:maisons){
            System.out.print(" "+m+" | ");
        }   
    }

    //afficher les generateurs
    public void afficherGenerateurs(){
        for(Generateur g:generateurs){
            System.out.print(" "+g+ " | ");
        }
    }

    // ajouter une maison
    public void ajouterMaison(Maison maison) {
        if (!maisons.add(maison)) { // add() retourne false si déjà présent

            // la maison existait → on met à jour l'objet existant
            for (Maison m : maisons) {
                if (m.equals(maison)) {
                    m.setTypeConsommation(maison.getTypeConsommation());
                    System.out.println(
                            "La maison ( " + maison.getNom() + " , " + maison.getTypeConsommation()
                                    + " ) est déjà existante, Consommation mise à jour !");
                    return;
                }
            }
        }

        System.out
                .println("La maison ( " + maison.getNom() + " , " + maison.getTypeConsommation() + " ) est ajoutée !");
    }

    // ajouter un générateur
    public void ajouterGenerateur(Generateur generateur) {
        if (!generateurs.add(generateur)) { // add() retourne false si déjà présent

            // le generateur existait → on met à jour l'objet existant
            for (Generateur g : generateurs) {
                if (g.equals(generateur)) {
                    g.setCapaciteMax(generateur.getCapaciteMax());
                    System.out.println(

                            "Le generateur ( " + generateur.getNom() + " , " + generateur.getCapaciteMax()
                                    + " ) est déjà existant, Capacite mise à jour !");
                    return;
                }
            }
        }

        System.out.println(
                "Le generateur ( " + generateur.getNom() + " , " + generateur.getCapaciteMax() + " ) est ajouté !");
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
            System.out.println("Générateur introuvable, veuillez réassayez");
            return;
        }
        if (m == null) {
            System.out.println("Maison introuvable, veuillez réassayez");
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
        if (generateur == null || trouverGenerateur(generateur.getNom()) == null) {
            System.out.println("Generateur introuvable");
            return;
        }
        generateurs.remove(generateur);
        System.out.println("Générateur "+generateur.toString()+" supprimé avec succès.");
        
        for(Maison m:maisons){
            if(verifConnexion(generateur, m)){
                m.setGenerateur(null);
            }
        }
        System.out.println("Suppression des connexions de "+generateur.getNom() +" : succès");
    }

    // supprimer une maison
    public void supprimerMaison(Maison maison) {
        if (maison ==null || trouverMaison(maison.getNom()) == null) {
            System.out.println("Maison introuvable");
            return;
        }
        maisons.remove(maison);
        System.out.println("Maison "+maison.toString()+" supprimé avec succès.");
        for(Generateur g:generateurs){
            if(verifConnexion(g, maison)){
                g.supprimerMaison(maison);
            }
        }
        System.out.println("Suppression des connexions de "+maison.getNom() +" : succès");
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
                System.out.println("Réseau invalide : Pas de connexion pour la maison " + m.getNom());
                return false;
            }
            if (nbrConnexions > 1) {
                builder.append(m.getNom());
                builder.append(" ");
            }
            if (!builder.isEmpty()) {
                System.out.println("Réseau invalide : Trop de connexions pour " + builder.toString());
                return false;
            }
        }
        return true;
    }

    // vérifier si le réseau est valide (cas lecture fichier)
    public String reseauNonValide() {
        StringBuilder globalError = new StringBuilder();
        for (Generateur g : generateurs) {
            if (g.getMaisons() == null) {
               globalError.append("Aucune maison connectee au generateur " + g.getNom());
               globalError.append("\n");
            }
        }
        StringBuilder builderMaison = new StringBuilder();
        for (Maison m : maisons) {
            int nbrConnexions = 0;
            for (Generateur g : generateurs) {
                if (verifConnexion(g, m)) {
                    nbrConnexions++;
                }
            }
            if (nbrConnexions == 0) {
                globalError.append("Pas de connexion pour la maison " + m.getNom());
                globalError.append("\n");
            }
            if (nbrConnexions > 1) {
                builderMaison.append(m.getNom());
                builderMaison.append(" ");
            }

        }
        if (!builderMaison.isEmpty()) {
            System.out.println();
            globalError.append("Trop de connexions pour " + builderMaison.toString());
            globalError.append("\n");
        }
        if(globalError.isEmpty()) return null;// si reseau valide
        else return "Réseau invalide -> " + globalError.toString();
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

        total = disp + lambda * surcharge;
        this.cout = total;
        System.out.println("-------------------------------------------------------");
        System.out.printf("Disp(S) = %.3f | Surcharge(S) = %.3f | Cout(S) = %.3f%n", disp, surcharge, total);
        System.out.println("-------------------------------------------------------");
        return total;
    }


    /* -------------------- Recuit simulé avancé (move + swap) -------------------- */
    /**
     * Optimisation par recuit simulé combinant moves et swaps.
     * swap : échanger les générateurs de deux maisons
     * move : déplacer une maison vers un autre générateur
     * On utilise le recuit simulé, qui est une méthode inspirée du refroidissement des métaux :
     * On commence avec une solution initiale (le réseau actuel).
     * On fait des petites modifications (move ou swap) :
     * Move : déplacer une maison vers un autre générateur
     * Swap : échanger les générateurs de deux maisons
     * On calcule le coût pour cette nouvelle solution.
     * Règle d’acceptation :
     * Si le coût est meilleur → on garde le changement
     * Si le coût est pire → on peut quand même l’accepter avec une probabilité qui dépend de la température
     * Au début, on accepte facilement les changements mauvais pour explorer
     * Ensuite, la probabilité diminue (le réseau « refroidit ») → on devient plus strict
     * On répète ce processus un grand nombre de fois
     * On sauvegarde la meilleure solution trouvée et on restaure ses connexions à la fin.
     *
     * @param iterations   nombre total d'itérations (ex : 20000)
     * @param initialTemp  température initiale (ex : 1.0)
     * @param coolingRate  facteur de refroidissement (0.99..0.9999)
     * @param seed         seed pour reproductibilité (0L -> aléatoire)
     * @param swapProb     probabilité (0..1) de tenter un swap (sinon tenter un move)
     */
    public void optimiserRecuitAvance(int iterations, double initialTemp, double coolingRate, long seed, double swapProb) {
        if (iterations <= 0) {
            System.out.println("iterations doit être > 0");
            return;
        }
        if (maisons.isEmpty() || generateurs.isEmpty()) {
            System.out.println("Réseau vide : optimisation impossible.");
            return;
        }
        Random rnd = (seed != 0L) ? new Random(seed) : new Random();

        // Convertir en listes temporaires pour accès par index (plus simple)
        List<Maison> maisonList = new ArrayList<>(maisons);
        List<Generateur> genList = new ArrayList<>(generateurs);

        // snapshot initial
        Map<String,String> bestAssign = snapshotAssignments();
        double bestCost = this.calculerCout();
        double currentCost = bestCost;
        double T = initialTemp;

        System.out.printf("Recuit avancé: coût initial=%.5f, it=%d, T0=%.5f, cooling=%.5f, swapProb=%.2f%n",
                bestCost, iterations, initialTemp, coolingRate, swapProb);

        for (int it = 0; it < iterations; it++) {
            boolean performedChange=false;
            double newCost = currentCost;

            if (rnd.nextDouble() < swapProb && maisonList.size() >= 2) {
                // ---- proposer un SWAP ----
                // choisir deux maisons distinctes
                Maison m1 = maisonList.get(rnd.nextInt(maisonList.size()));
                Maison m2 = maisonList.get(rnd.nextInt(maisonList.size()));
                if (m1.equals(m2)) continue;
                Generateur g1 = m1.getGenerateur();
                Generateur g2 = m2.getGenerateur();
                // si swap ne change rien (mêmes generateurs), skip
                if (Objects.equals(g1, g2)) continue;

                // appliquer swap temporaire : m1->g2, m2->g1
                if (g1 != null) g1.supprimerMaison(m1);
                if (g2 != null) g2.supprimerMaison(m2);

                if (g2 != null) { g2.addMaison(m1); m1.setGenerateur(g2); } else { m1.setGenerateur(null); }
                if (g1 != null) { g1.addMaison(m2); m2.setGenerateur(g1); } else { m2.setGenerateur(null); }

                newCost = this.calculerCout();
                performedChange = true;

                // acceptation
                double delta = newCost - currentCost;
                boolean accept = (delta <= 0) || (rnd.nextDouble() < Math.exp(-delta / Math.max(T, 1e-12)));

                if (!accept) {
                    // revert swap
                    if (g2 != null) { g2.supprimerMaison(m1); }
                    if (g1 != null) { g1.supprimerMaison(m2); }

                    if (g1 != null) { g1.addMaison(m1); m1.setGenerateur(g1); } else { m1.setGenerateur(null); }
                    if (g2 != null) { g2.addMaison(m2); m2.setGenerateur(g2); } else { m2.setGenerateur(null); }

                    // recalc currentCost to be safe
                    this.calculerCout();
                    performedChange = false;
                } else {
                    currentCost = newCost;
                    if (currentCost < bestCost) {
                        bestCost = currentCost;
                        bestAssign = snapshotAssignments();
                        // log éventuel
                        // System.out.printf("It %d: new best=%.5f (swap)%n", it+1, bestCost);
                    }
                }

            } else {
                // ---- proposer un MOVE ----
                Maison m = maisonList.get(rnd.nextInt(maisonList.size()));
                Generateur gOld = m.getGenerateur();
                Generateur gNew = genList.get(rnd.nextInt(genList.size()));
                if (Objects.equals(gOld, gNew)) continue;

                // appliquer move temporaire
                if (gOld != null) gOld.supprimerMaison(m);
                if (gNew != null) { gNew.addMaison(m); m.setGenerateur(gNew); } else { m.setGenerateur(null); }

                newCost = this.calculerCout();
                performedChange = true;

                double delta = newCost - currentCost;
                boolean accept = (delta <= 0) || (rnd.nextDouble() < Math.exp(-delta / Math.max(T, 1e-12)));

                if (!accept) {
                    // revert move
                    if (gNew != null) { gNew.supprimerMaison(m); }
                    if (gOld != null) { gOld.addMaison(m); m.setGenerateur(gOld); } else { m.setGenerateur(null); }
                    this.calculerCout();
                    performedChange = false;
                } else {
                    currentCost = newCost;
                    if (currentCost < bestCost) {
                        bestCost = currentCost;
                        bestAssign = snapshotAssignments();
                        // System.out.printf("It %d: new best=%.5f (move)%n", it+1, bestCost);
                    }
                }
            }

            // refroidissement
            T *= coolingRate;
            if (T < 1e-12) T = 1e-12;

            // optionnel: affichage périodique
            // if (it % 5000 == 0) System.out.printf("It %d current=%.5f best=%.5f T=%.5e%n", it, currentCost, bestCost, T);
        }

        // Restaurer la meilleure affectation trouvée (cohérence bidirectionnelle garantie)
        restoreAssignments(bestAssign);
        this.calculerCout();
        // sauvegarde de la solution dans un fichier
        this.sauvegarderDansFichier("reseau_optimise.txt");
        System.out.printf("Recuit terminé. Meilleur coût = %.5f%n", bestCost);
    }

    /* -------------------- Helpers pour snapshot / restore -------------------- */

    /** renvoie Map<nomMaison, nomGenerateur|null> */
    private Map<String, String> snapshotAssignments() {
        Map<String, String> map = new HashMap<>();
        for (Maison m : maisons) {
            Generateur g = m.getGenerateur();
            map.put(m.getNom(), (g == null ? null : g.getNom()));
        }
        return map;
    }

    /** restaure les affectations à partir de la map (reconstruit aussi les sets de maisons des generateurs) */
    private void restoreAssignments(Map<String, String> assign) {
        // Vider toutes les relations générateur -> maisons et détacher maisons
        for (Generateur g : new HashSet<>(generateurs)) {
            // parcourir copie pour éviter ConcurrentModification
            for (Maison m : new HashSet<>(g.getMaisons())) {
                g.supprimerMaison(m);
                m.setGenerateur(null);
            }
        }
        // Appliquer la map d'affectations
        for (Maison m : maisons) {
            String gName = assign.get(m.getNom());
            if (gName == null) {
                m.setGenerateur(null);
                continue;
            }
            Generateur g = trouverGenerateur(gName);
            if (g != null) {
                m.setGenerateur(g);
                if (!g.getMaisons().contains(m)) g.addMaison(m);
            } else {
                m.setGenerateur(null);
            }
        }
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
    
    public void sauvegarderDansFichier(String nomFichier) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/"+nomFichier))) {

            // 1) Générateurs
            for (Generateur g : generateurs) {
                writer.println("generateur(" + g.getNom() + "," + (int) g.getCapaciteMax() + ").");
            }

            // 2) Maisons
            for (Maison m : maisons) {
                writer.println("maison(" + m.getNom() + "," + m.getTypeConsommation().name() + ").");
            }

            // 3) Connexions
            for (Maison m : maisons) {
                Generateur g = m.getGenerateur();
                if (g != null) {
                    writer.println("connexion(" + g.getNom() + "," + m.getNom() + ").");
                }
            }

            System.out.println("Réseau sauvegardé dans : " + nomFichier);

        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }



    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Reseau reseau = new Reseau();
        if (args.length == 0) {
            Menu.constructionManuelle(reseau, sc);
        } else if (args.length == 2) {
            reseau.setLambda(Integer.parseInt(args[1]));
            ParserReseau pr = new ParserReseau();
            reseau = pr.lireReseau(args[0]);
            if (reseau != null) {
                reseau.afficherReseau();
                
                Menu.constructionFichier(reseau, sc);
                
            }

        } else {
            System.err.println("Usage : java Reseau <chemin_vers_fichier> <LAMBDA>");
        }

    }

}