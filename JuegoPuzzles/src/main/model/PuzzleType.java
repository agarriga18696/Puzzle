package main.model;

public enum PuzzleType {

	ANIMALS("Animales"),
	CITIES("Ciudades"),
	NATURE("Naturaleza"),
	PEOPLE("Personas"),
	FOOD("Comida"),
	ART("Arte"),
	HISTORY("Historia"),
	SPACE("Espacio"),
	MARVEL("Marvel");
	
	private final String displayName;
	
	PuzzleType(String displayName){
		this.displayName = displayName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
}
