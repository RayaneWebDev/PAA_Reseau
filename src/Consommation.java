
public enum Consommation {
	BASSE(10), NORMALE(20), FORTE(40);
	
	private int consommation;
	
	//Définition du constructeur
	private Consommation(int consommation) {
		this.consommation=consommation;
	}
	
	
	public int getConsommation(){
		return consommation;
	}
}
