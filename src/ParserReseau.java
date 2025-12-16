import java.io.*;
import java.util.*;

public class ParserReseau {

    // HashMap pour les erreurs contenues dans le fichier
    private HashMap<Integer, Set<String>> erreurs = new HashMap<>();    // La clé pour numéro de la ligne / valeurs pour les erreurs dans la ligne

    // recuperer fichier texte
    public Reseau lireReseau(String nomFichier) {
        Reseau reseau = new Reseau();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/instances_Test/"+nomFichier))) {
            String ligne;
            int numLigne = 0;// compteur pour le numéro de la ligne

            // booléens pour verifier l'ordre des lignes (generateur -> maison -> connexion)
            boolean finGenerateur = false;
            boolean finMaison = false;

            while ((ligne = reader.readLine()) != null) {
                String cleanLigne;
                numLigne++;
                cleanLigne = ligne.trim();// ligne apres suppression des espaces
                if (!cleanLigne.endsWith(".")) {
                    enregistrerErreur(numLigne, "Il manque un point à la fin de la ligne.");

                }
                if (!cleanLigne.startsWith("generateur") && !cleanLigne.startsWith("maison")
                        && !cleanLigne.startsWith("connexion")) {
                    enregistrerErreur(numLigne,
                            "Chaque ligne doit commencer par \"generateur\"  ,  \"maison\"  ou \"connexion\".");
                }
                // ligne generateur
                if (cleanLigne.startsWith("generateur")) {
                    if (finGenerateur) {
                        enregistrerErreur(numLigne, "Ordre non respectee, il faut créer tous les générateurs avant de créer des maisons.");
                        continue;
                    }
                    String[] arguments = extraireArguments(cleanLigne);
                    if (arguments == null) {
                        enregistrerErreur(numLigne, "Il manque au moins une parenthese dans cette ligne.");
                    } else if (arguments.length != 2) {
                        enregistrerErreur(numLigne, "Le nombre de parametres doit etre egal a 2.");
                    } else {
                        try {
                            String nom = arguments[0].trim().toUpperCase();
                            int capacite = Integer.parseInt(arguments[1]);

                            if (!isAlphaNumerique(nom)) {
                                enregistrerErreur(numLigne,
                                        "Le nom d'un generateur ne peut contenir que des lettres et des chiffres.");
                            }
                            if (capacite < 0) {
                                enregistrerErreur(numLigne, "La capacite d'un generateur doit etre positive.");
                            }

                            if (erreurs.get(numLigne) == null) {
                                reseau.ajouterGenerateur(new Generateur(nom, capacite));
                            }
                        } catch (NumberFormatException e) {
                            enregistrerErreur(numLigne, "La capacite d'un generateur doit etre un entier.");
                        }

                    }
                    // ligne maison
                } else if (cleanLigne.startsWith("maison")) {
                    finGenerateur = true;
                    if (finMaison) {
                        enregistrerErreur(numLigne, "Ordre non respectee, il faut créer toutes maisons avant de créer des connexions.");
                        continue;
                    }
                    String[] arguments = extraireArguments(cleanLigne);
                    if (arguments == null) {
                        enregistrerErreur(numLigne, "Il manque au moins une parenthese dans cette ligne.");
                    } else if (arguments.length != 2) {
                        enregistrerErreur(numLigne, "Le nombre de parametres doit etre egal a 2.");
                    } else {
                        String nom = arguments[0].trim().toUpperCase();
                        String type = arguments[1].trim().toUpperCase();
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
                                System.out.println("Par défaut, Consommation : NORMAL ");
                            }
                        }
                        if (!isAlphaNumerique(nom)) {
                            enregistrerErreur(numLigne,
                                    "Le nom d'une maison ne peut contenir que des lettres et des chiffres.");
                        }
                        if (erreurs.get(numLigne) == null) {
                            reseau.ajouterMaison(new Maison(nom, consommation));
                        }

                    }
                    // ligne connexion
                } else if (cleanLigne.startsWith("connexion")) {
                    if(!finGenerateur) { enregistrerErreur(numLigne,"Ordre non respectee, il faut creer des generateurs et maisons avant de creer des connexions"); }
                    finMaison = true;
                    String[] arguments = extraireArguments(cleanLigne);
                    if (arguments == null) {
                        enregistrerErreur(numLigne, "Il manque au moins une parenthese dans cette ligne.");
                    } else if (arguments.length != 2) {
                        enregistrerErreur(numLigne, "Le nombre de parametres doit etre egal a 2.");
                    } else {
                        String entite1 = arguments[0].trim().toUpperCase();
                        String entite2 = arguments[1].trim().toUpperCase();
                        if (!isAlphaNumerique(entite1) || !isAlphaNumerique(entite2)) {
                            enregistrerErreur(numLigne,
                                    "Les noms des entites de la connexion doivent etre alphanumeriques.");
                        } else {
                            Object[] Connexion = reseau.lireConnexion(entite1, entite2);
                            if(Connexion!=null){
                                Maison m = (Maison) Connexion[0];
                                Generateur g = (Generateur) Connexion[1];
                                // VÉRIFIER que les entités existent et sont du bon type avant d'ajouter
                                if (m == null || g == null) {
                                    enregistrerErreur(numLigne, "Connexion impossible : au moins une des entités ("
                                            + entite1 + ", " + entite2 + ") est introuvable ou n'est pas du bon type.");
                                } else {
                                    if (erreurs.get(numLigne) == null) {
                                        reseau.ajouterConnexion(g, m);
                                    }

                                }
                            }
                            

                            
                        }

                    }

                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier " + nomFichier + " est introuvable : " + e.getMessage());
            return null;
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier " + nomFichier + " : " + e.getMessage());
            return null;
        }

        // le reader est fermé automatiquement grace au try-with-ressources
        String reseauInvalide = reseau.reseauNonValide();
        if(reseauInvalide!=null){//les reseau n'est pas valide
            enregistrerErreur(0, reseauInvalide);
        }
        if (!erreurs.isEmpty()) {
            afficherRapport();
            return null;
        }
        
        return reseau;
    }

    // méthode pour supprimer les parenthèses et extraire les arguments
    public static String[] extraireArguments(String cleanLigne) {
        int debut = cleanLigne.indexOf("(");
        int fin = cleanLigne.indexOf(")");
        if (debut != -1 && fin != -1) {// parenthèses existantes
            return cleanLigne.substring(debut+1, fin).split(",");
        } else {
            return null;
        }
    }

    // methode pour vérifier que les noms des generateurs et maisons sont des
    // alphanumeriques
    public static boolean isAlphaNumerique(String nom) {
        // null ou vide
        if (nom == null || nom.isEmpty()) {
            return false;
        }

        // iterer sur la chaine
        for (int i = 0; i < nom.length(); i++) {
            char c = nom.charAt(i);

            if (!Character.isLetterOrDigit(c)) {
                return false;
            }

        }

        return true;
    }

    // methode pour enregistrer une erreur à une ligne du fichier
    private void enregistrerErreur(int numLigne, String message) {
        erreurs.computeIfAbsent(numLigne, k -> new HashSet<>()).add(message);
    }

    public void afficherRapport() {

        System.err.println("\n*** Rapport d'Erreurs ***");
        System.err.println("*************************\n");


        // nous allons simplement trier les clés pour un affichage propre :
        
        // Récupérer les clés et les trier (les numéros de ligne)
        List<Integer> lignesTriees = new ArrayList<>(erreurs.keySet());
        Collections.sort(lignesTriees);

        for (int numLigne : lignesTriees) {
            // Récupérer le Set des messages pour cette ligne
            Set<String> messages = erreurs.get(numLigne);

            // Déterminer le préfixe (Ligne ou Validation Globale)
            String prefixe;
            if (numLigne == 0) {
                prefixe = "Validation Globale : "; //pour la validation globale du reseau
            } else {
                prefixe = "Ligne " + numLigne + " : ";//pour chaque ligne
            }

            // 2. Parcourir le Set de messages pour chaque ligne
            for (String message : messages) {
                System.err.println(prefixe + message);
            }
        }
        
        System.err.println("\n*************************");
    }
    
}
