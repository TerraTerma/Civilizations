package TTCivilizations.Civilization.Flags;

import java.util.ArrayList;
import java.util.List;

public class CivilizationFlag <T extends Object>{

	public static final List<CivilizationFlag<? extends Object>> LIST = new ArrayList<>();
	public static final CivilizationFlag<Boolean> BLOCK_BREAK_INSIDE = createRegister("Block break from insiders", "Allow/disallow players from the civilization to break blocks", true);
	public static final CivilizationFlag<Boolean> BLOCK_PLACE_INSIDE = createRegister("Block place from insiders", "Allow/disallow players from the civilization to place blocks", true);
	public static final CivilizationFlag<Boolean> BLOCK_BREAK_OUTSIDE = createRegister("Block break from outsiders", "Allow/disallow players from outside the civilization to break blocks", false);
	public static final CivilizationFlag<Boolean> BLOCK_PLACE_OUTSIDE = createRegister("Block place from outsiders", "Allow/disallow players from outside the civilization to place blocks", false);	
	public static final CivilizationFlag<Boolean> SHOP_INSIDE = createRegister("Shop from insiders", "Allow/disallow players from the civilization to trade with shops", true);
	public static final CivilizationFlag<Boolean> SHOP_OUTSIDE = createRegister("Shop from outsiders", "Allow/disallow players from outside the civilization to trade with shops", false);
	public static final CivilizationFlag<Boolean> SHIP_INSIDE = createRegister("Ships from insiders", "Allow/disallow ships from players inside the civilization to drive ships inside the civilization", true);
	public static final CivilizationFlag<Boolean> SHIP_OUTSIDE = createRegister("Ships from outside", "Allow/disallow ships from players outside the civilization to drive ships inside the civilization", false);
	public static final CivilizationFlag<Boolean> MOB_ENTER = createRegister("Mob enter", "Allow/disallow mobs to enter the civilization", false);
	
	String NAME;
	String DESCRIPTION;
	T VALUE;
	
	public CivilizationFlag(String name, String description, T defaultValue){
		NAME = name;
		DESCRIPTION = description;
		VALUE = defaultValue;
	}
	
	public String getName(){
		return NAME;
	}
	
	public String getDescription(){
		return DESCRIPTION;
	}
	
	public T getValue(){
		return VALUE;
	}
	
	public void setValue(T value){
		VALUE = value;
	}
	
	public CivilizationFlag<T> clone(){
		CivilizationFlag<T> flag = new CivilizationFlag<>(NAME, DESCRIPTION, VALUE);
		return flag;
	}
	
	@Override
	public String toString(){
		return NAME + "," + VALUE;
	}
	
	private static <T extends Object> CivilizationFlag<T> createRegister(String name, String description, T value){
		CivilizationFlag<T> flag = new CivilizationFlag<>(name, description, value);
		LIST.add(flag);
		return flag;
	}
	
}
