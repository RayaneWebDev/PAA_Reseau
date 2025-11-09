import java.util.*;

public class Generateur {
    private String nom;
    private double capaciteMax;
    private List<Maison> maisons ;

    public Generateur(String nom, double capaciteMax) {
        this.nom = nom;
        this.capaciteMax = capaciteMax;
        this.maisons = new ArrayList<>();
    }
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
    public List<Maison> getMaisons() {
        return maisons;
    }
    public void setMaisons(List<Maison> maisons) {
        this.maisons = maisons;
    }
    public void addMaison(Maison maison) {
        this.maisons.add(maison);
    }
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

    // Redefinition de la methode toString
    public String toString() {
        return "Générateur : "+nom+" - Capacité Max :"+capaciteMax+" kW).";
    }

}
