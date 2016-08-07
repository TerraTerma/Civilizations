package TTCivilizations.Commands.CMD;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Civilization.CivilizationType;
import TTCivilizations.Civilization.Chunk.SectionType;
import TTCivilizations.Civilization.Roles.PlayerPermission;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Entity.Living.Human.Player.TTPlayer;

public class Create {
	
	public static final double PRICE_OF_CHUNK = 14;
	public static final double PRICE_OF_CIVIL = 20000;
	
	
	public static void createCivil(TTPlayer player, String name){
		TTCivilizationsPlugins plugin = TTCivilizationsPlugins.getPlugin();
		CivilizationData data = player.getSingleData(CivilizationData.class).get();
		if(data.getPower() >= 10){
			if(player.getBalance() >= PRICE_OF_CIVIL){
				Civilization civil = new Civilization(name, CivilizationType.USER);
				civil.addChunk(player.getPlayer().getLocation().getChunk(), "Source");
				data.setPermission(PlayerPermission.LEADER);
			}else{
				player.sendMessage(plugin, "You need " + PRICE_OF_CIVIL + " to buy a Civilization");
			}
		}else{
			player.sendMessage(plugin, "You need 10+ power before you can create a Civilization");
		}
	}
	
	public static void createChunk(TTPlayer player, SectionType type, String name){
		TTCivilizationsPlugins plugin = TTCivilizationsPlugins.getPlugin();
		CivilizationData data = player.getSingleData(CivilizationData.class).get();
		
	}

}
