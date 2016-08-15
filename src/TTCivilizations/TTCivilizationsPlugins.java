package TTCivilizations;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Commands.CommandLoader;
import TTCivilizations.Listeners.Listeners;
import TTCivilizations.Mechs.CivilMechs.CivilRelationshipData.CivilRelationshipData;
import TTCivilizations.Mechs.CivilMechs.Vault.CivilVaultData;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Mech.DataHandler;

public class TTCivilizationsPlugins extends JavaPlugin {

	static TTCivilizationsPlugins PLUGINS;

	public void onEnable() {
		PLUGINS = this;
		getCommand("Civilization").setExecutor(new CommandLoader());
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		Civilization.reload();
		List<Class<? extends DataHandler>> mechs = DataHandler.MECHS;
		mechs.add(CivilizationData.class);
		mechs.add(CivilRelationshipData.class);
		mechs.add(CivilVaultData.class);
	}

	public static TTCivilizationsPlugins getPlugin() {
		return PLUGINS;
	}

}
