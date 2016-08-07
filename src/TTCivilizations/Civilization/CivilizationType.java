package TTCivilizations.Civilization;

public enum CivilizationType {
	
	USER("User"),
	DEFAULT("Default");
	
	String NAME;
	
	private CivilizationType(String name){
		NAME = name;
	}
	
	public String getName(){
		return NAME;
	}

}
