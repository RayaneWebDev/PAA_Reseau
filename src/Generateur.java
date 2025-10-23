import java.util.*;

public class Generateur {
    private String nom;
    private int capaciteMax;
    private List<Maison> maisons ;

    public Generateur(String nom, int capaciteMax) {
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
    public int getCapaciteMax() {
        return capaciteMax;
    }
    public void setCapaciteMax(int capaciteMax) {
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

}
