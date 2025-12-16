public enum Consommation {
	BASSE(10), NORMAL(20), FORTE(40);
	
	private int consommation;
	
	//Définition du constructeur
	private Consommation(int consommation) {
		this.consommation=consommation;
	}
	
	//méthode pour récuperer la valeur consomamtion
	public int getConsommation(){
		return consommation;
	}
}
