public class Maison {
    private String nom;
    private Generateur generateur;
    private Consommation typeConsommation;
    
    //constructeur
    public Maison(String nom, Consommation typeConsommation) {
    	this.nom=nom;
    	this.generateur = null;
    	this.typeConsommation = typeConsommation;
    }
    
    //setters et getters
    public String getNom() {
    	return nom;
    }
    
    public void setNom(String nom) {
    	this.nom=nom;
    }
    
    public Generateur getGenerateur() {
    	return generateur;
    }
    
    public void setGenerateur(Generateur generateur) {
    	this.generateur=generateur;
    }
    
    public Consommation getTypeConsommation() {
    	return typeConsommation;
    }
    
    public void setTypeConsommation(Consommation typeConsommation) {
    	this.typeConsommation=typeConsommation;
    }
    
    //redefinition de la méthode equals()
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maison maison = (Maison) o;
        return nom.equals(maison.nom); // Unicité basée sur le nom
    }

    public int hashCode() {
        return nom.hashCode();
    }
    
    //Redéfinition de la méthode toString()
    public String toString() {
    	return nom+" ( "+ typeConsommation.getConsommation()+" kW ).";
    }
    
    }
