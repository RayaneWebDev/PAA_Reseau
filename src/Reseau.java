import java.util.*;

public class Reseau {
    private List<Generateur> generateurs;
    private List<Maison> maisons;
    private double cout;

    public Reseau() {}

    public void setGenerateurs(List<Generateur> generateurs) {
        this.generateurs = generateurs;
    }
    public void setMaisons(List<Maison> maisons) {
        this.maisons = maisons;
    }
    public void setCout(double cout) {
        this.cout = cout;
    }
    public List<Generateur> getGenerateurs() {
        return generateurs;
    }
    public List<Maison> getMaisons() {
        return maisons;
    }
    public double getCout() {
        return cout;
    }

    public void ajouterMaison(Maison maison) {
        maisons.add(maison);
    }
    public void ajouterGenerateur(Generateur generateur) {
        generateurs.add(generateur);
    }

    public void supprimerGenerateur(Generateur generateur) {
        generateurs.remove(generateur);
    }

    public void supprimerMaison(Maison maison) {
        maisons.remove(maison);
    }

    public double surcharge(){
        System.out.println("surcharge de la reseau");
        return 0.0;
    }
    public double calculerCout(){
        System.out.println("calculer le cout du reseau");
        return 0.0;
    }
    public static void main(String[] args) {
        Reseau R1 = new Reseau();

    }
}