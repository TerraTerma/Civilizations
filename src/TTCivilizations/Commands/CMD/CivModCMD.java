package TTCivilizations.Commands.CMD;

import java.util.Arrays;
import java.util.Optional;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Civilization.Flags.CivilizationFlag;
import TTCivilizations.Civilization.Types.UserCivilization;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Entity.Living.Human.Player.TTPlayer;

public class CivModCMD {
	
	public void inviteCommand(TTPlayer player, TTPlayer inviting){
		
	}
	
	public void flagCommand(TTPlayer player, String name, String value){
		CivilizationData data = player.getSingleData(CivilizationData.class).get();
		String[] args = data.getPermission().getExtraCMD(player);
		if(Arrays.asList(args).contains("setFlag")){
			Optional<UserCivilization> opCivil = Civilization.getByPlayer(player.getPlayer().getUniqueId());
			if(opCivil.isPresent()){
				UserCivilization civil = opCivil.get();
				Optional<CivilizationFlag<? extends Object>> opFlag = civil.getFlag(name);
				if(opFlag.isPresent()){
					CivilizationFlag<? extends Object> flag = opFlag.get();
					flag.setValue(value);
				}else{
					player.sendMessage(TTCivilizationsPlugins.getPlugin(), name + " is not a valid flag");
				}
			}else{
				player.sendMessage(TTCivilizationsPlugins.getPlugin(), "You need to be in a civilization to use this command");
			}
		}else{
			player.sendMessage(TTCivilizationsPlugins.getPlugin(), "You do not have access to that command");
		}
	}

}
