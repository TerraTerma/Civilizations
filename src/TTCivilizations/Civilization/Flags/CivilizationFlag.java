package TTCivilizations.Civilization.Flags;

import java.util.ArrayList;
import java.util.List;

public abstract class CivilizationFlag<T extends Object> {

	public static final List<CivilizationFlag<? extends Object>> LIST = new ArrayList<>();
	public static final CivilizationFlag<Boolean> BLOCK_BREAK_INSIDE = createRegister("Block break from insiders",
			"Allow/disallow players from the civilization to break blocks", true);
	public static final CivilizationFlag<Boolean> BLOCK_PLACE_INSIDE = createRegister("Block place from insiders",
			"Allow/disallow players from the civilization to place blocks", true);
	public static final CivilizationFlag<Boolean> BLOCK_BREAK_OUTSIDE = createRegister("Block break from outsiders",
			"Allow/disallow players from outside the civilization to break blocks", false);
	public static final CivilizationFlag<Boolean> BLOCK_PLACE_OUTSIDE = createRegister("Block place from outsiders",
			"Allow/disallow players from outside the civilization to place blocks", false);
	public static final CivilizationFlag<Boolean> SHOP_INSIDE = createRegister("Shop from insiders",
			"Allow/disallow players from the civilization to trade with shops", true);
	public static final CivilizationFlag<Boolean> SHOP_OUTSIDE = createRegister("Shop from outsiders",
			"Allow/disallow players from outside the civilization to trade with shops", false);
	public static final CivilizationFlag<Boolean> SHIP_INSIDE = createRegister("Ships from insiders",
			"Allow/disallow ships from players inside the civilization to drive ships inside the civilization", true);
	public static final CivilizationFlag<Boolean> SHIP_OUTSIDE = createRegister("Ships from outside",
			"Allow/disallow ships from players outside the civilization to drive ships inside the civilization", false);
	public static final CivilizationFlag<Boolean> MOB_ENTER = createRegister("Mob enter",
			"Allow/disallow mobs to enter the civilization", false);

	String NAME;
	String DESCRIPTION;
	T VALUE;

	public CivilizationFlag(String name, String description, T defaultValue) {
		NAME = name;
		DESCRIPTION = description;
		VALUE = defaultValue;
	}

	public abstract T setValue(Object object);

	public abstract CivilizationFlag<T> clone();

	public String getName() {
		return NAME;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public T getValue() {
		return VALUE;
	}

	@Override
	public String toString() {
		return NAME + "," + VALUE;
	}

	private static CivilizationFlag<Boolean> createRegister(String name, String description, boolean value) {
		CivilizationFlag<Boolean> flag = new CivilizationBooleanFlag(name, description, value);
		LIST.add(flag);
		return flag;
	}

	public static class CivilizationBooleanFlag extends CivilizationFlag<Boolean> {

		public CivilizationBooleanFlag(String name, String description, Boolean defaultValue) {
			super(name, description, defaultValue);
		}

		@Override
		public Boolean setValue(Object object) {
			boolean value = Boolean.parseBoolean(object.toString());
			VALUE = value;
			return value;
		}

		@Override
		public CivilizationFlag<Boolean> clone() {
			CivilizationFlag<Boolean> flag = new CivilizationBooleanFlag(NAME, DESCRIPTION, VALUE);
			return flag;
		}
	}
}
