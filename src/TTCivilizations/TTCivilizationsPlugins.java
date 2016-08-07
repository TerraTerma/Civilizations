package TTCivilizations;

import org.bukkit.plugin.java.JavaPlugin;

import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Mech.DataHandler;

public class TTCivilizationsPlugins extends JavaPlugin {
	
	static TTCivilizationsPlugins PLUGINS;
	
	public void onEnable(){
		PLUGINS = this;
		DataHandler.MECHS.add(CivilizationData.class);
	}
	
	public static TTCivilizationsPlugins getPlugin(){
		return PLUGINS;
	}

}
