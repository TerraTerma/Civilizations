package TTCivilizations.Civilization.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Civilization.Flags.CivilizationFlag;

public class Wilderness implements Civilization {

	List<CivilizationFlag<? extends Object>> FLAGS = new ArrayList<>();
	
	@Override
	public String getName() {
		return "Wilderness";
	}

	@Override
	public List<CivilizationFlag<? extends Object>> getFlags() {
		return FLAGS;
	}

	@Override
	public Optional<CivilizationFlag<? extends Object>> getFlag(String name) {
		Optional<CivilizationFlag<? extends Object>> opFlag = FLAGS.stream().filter(f -> f.getName().equalsIgnoreCase(name)).findFirst();
		if(opFlag.isPresent()){
			return opFlag;
		}else{
			Optional<CivilizationFlag<? extends Object>> opFlag2 = CivilizationFlag.LIST.stream().filter(f -> f.getName().equalsIgnoreCase(name)).findFirst();
			if(opFlag2.isPresent()){
				CivilizationFlag<? extends Object> flag = opFlag2.get().clone();
				FLAGS.add(flag);
				return Optional.of(flag);
			}
		}
		return Optional.empty();
	}

}
