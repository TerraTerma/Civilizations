package TTCivilizations.Mechs.PlayerMechs;

import TTCivilizations.Civilization.Roles.PlayerPermission;
import TTCivilizations.Civilization.Roles.PlayerRole;
import TTCore.Mech.DataHandlers.SavableData;
import TTCore.Mech.Stores.PlayerData;
import TTCore.Savers.Saver;

public class CivilizationData implements SavableData, PlayerData {
	
	public static final String DATA_POWER = "Power";
	public static final String DATA_ROLE = "Role";
	public static final String DATA_PERMISSION = "Permission";

	int POWER = 5;
	PlayerRole ROLE;
	PlayerPermission PERMISSION;
	
	public int getPower(){
		return POWER;
	}
	
	public CivilizationData setPower(int power){
		POWER = power;
		return this;
	}
	
	public PlayerRole getRole(){
		return ROLE;
	}
	
	public CivilizationData setRole(PlayerRole role){
		ROLE = role;
		return this;
	}
	
	public PlayerPermission getPermission(){
		return PERMISSION;
	}
	
	public CivilizationData setPermission(PlayerPermission permission){
		PERMISSION = permission;
		return this;
	}
	
	@Override
	public void save(Saver saver) {
		saver.set(POWER, DATA_POWER);
		saver.set(ROLE, DATA_ROLE);
		saver.set(PERMISSION, DATA_PERMISSION);
		
	}

	@Override
	public boolean load(Saver saver) {
		POWER = saver.get(Integer.class, DATA_POWER);
		String roleS = saver.get(String.class, DATA_ROLE);
		if(roleS != null){
			ROLE = PlayerRole.valueOf(roleS);
			PERMISSION = PlayerPermission.valueOf(saver.get(String.class, DATA_PERMISSION));
			return true;
		}
		return false;
	}

}
