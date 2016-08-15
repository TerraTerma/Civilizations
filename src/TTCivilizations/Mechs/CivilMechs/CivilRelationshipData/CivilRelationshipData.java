package TTCivilizations.Mechs.CivilMechs.CivilRelationshipData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Mechs.CivilData;
import TTCore.Mech.DataHandlers.SavableData;
import TTCore.Savers.Saver;

public class CivilRelationshipData implements CivilData, SavableData {

	public static String DATA_RELATIONSHIPS = "Relationships";

	Map<String, CivilRelationshipType> RELATIONSHIP = new HashMap<>();

	public Map<String, CivilRelationshipType> get() {
		return RELATIONSHIP;
	}

	public CivilRelationshipData set(Civilization civil, CivilRelationshipType type) {
		if (RELATIONSHIP.get(civil.getName()) == null) {
			RELATIONSHIP.put(civil.getName(), type);
		} else {
			RELATIONSHIP.replace(civil.getName(), type);
		}
		return this;
	}

	public CivilRelationshipType getRelationshipWith(Civilization civil) {
		return getRelationshipWith(civil.getName());
	}

	public CivilRelationshipType getRelationshipWith(String civil) {
		CivilRelationshipType type = RELATIONSHIP.get(civil);
		if (type == null) {
			return CivilRelationshipType.NETRUAL;
		} else {
			return type;
		}
	}

	@Override
	public void save(Saver saver) {
		List<String> list = new ArrayList<>();
		RELATIONSHIP.entrySet().stream().forEach(r -> {
			list.add(r.getKey() + "," + r.getValue().name());
		});
		saver.set(list, DATA_RELATIONSHIPS);

	}

	@Override
	public boolean load(Saver saver) {
		List<String> list = saver.getList(String.class, DATA_RELATIONSHIPS);
		if (list != null) {
			list.stream().forEach(s -> {
				String[] args = s.split(",");
				RELATIONSHIP.put(args[0], CivilRelationshipType.valueOf(args[1]));
			});
			return true;
		}
		return false;
	}

}
