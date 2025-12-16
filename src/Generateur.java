import java.util.*;

public class Generateur {
    private String nom;
    private double capaciteMax;
    private Set<Maison> maisons ;

    //constructeur
    public Generateur(String nom, double capaciteMax) {
        this.nom = nom;
        this.capaciteMax = capaciteMax;
        this.maisons = new HashSet<>();
    }

    //getters et setters
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public double getCapaciteMax() {
        return capaciteMax;
    }
    public void setCapaciteMax(double capaciteMax) {
        this.capaciteMax = capaciteMax;
    }
    public Set<Maison> getMaisons() {
        return maisons;
    }
    public void setMaisons(Set<Maison> maisons) {
        this.maisons = maisons;
    }

    //connecter une maison à ce générateur
    public void addMaison(Maison maison) {
        this.maisons.add(maison);
    }

    //déconnecter une maison de ce générateur
    public void supprimerMaison(Maison maison) {
        this.maisons.remove(maison);
    }

    // calculer la charge totale du générateur
    public double getChargeTotale(){
        double chargeMaison = 0 ;
        for (Maison maison : maisons) {
            chargeMaison +=  maison.getTypeConsommation().getConsommation();
        }
        return chargeMaison;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Generateur generateur = (Generateur) o;
        return nom.equals(generateur.nom); // Unicité basée sur le nom
    }

    public int hashCode() {
        return nom.hashCode();
    }
    
    // Redefinition de la methode toString()
    public String toString() {
        return nom+" ( "+capaciteMax+" kW ).";
    }

}
