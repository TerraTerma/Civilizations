package TTCivilizations.Listeners;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Mechs.CivilMechs.CivilRelationshipData.CivilRelationshipData;
import TTCivilizations.Mechs.CivilMechs.CivilRelationshipData.CivilRelationshipType;

public class Listeners implements Listener {

	@EventHandler
	public void unloadEvent(ChunkUnloadEvent event) {
		Optional<Civilization> opCivil = Civilization.getLoadedByChunk(event.getChunk());
		if (opCivil.isPresent()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(TTCivilizationsPlugins.getPlugin(), new Runnable() {

				@Override
				public void run() {
					Civilization civil = opCivil.get();
					if (!civil.isLoaded()) {
						civil.unload();
					}

				}
			}, 1);
		}
	}

	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Optional<Civilization> opCivil = Civilization.getLoadedByPlayer(player.getUniqueId());
		Optional<Civilization> opCivilTo = Civilization.getLoadedByChunk(event.getTo().getChunk());
		Optional<Civilization> opCivilFrom = Civilization.getLoadedByChunk(event.getFrom().getChunk());

		if (opCivilTo.isPresent()) {
			if (opCivilFrom.isPresent()) {
				Civilization civilTo = opCivilTo.get();
				Civilization civilFrom = opCivilFrom.get();
				if (!civilFrom.equals(civilTo)) {
					BossBar barUnfinal = null;
					if (opCivil.isPresent()) {
						Civilization civil = opCivil.get();
						CivilRelationshipType relationship = civil.getOrCreateSingleData(CivilRelationshipData.class)
								.getRelationshipWith(civilTo);
						barUnfinal = Bukkit.getServer().createBossBar(
								relationship.getChatColour() + "Welcome to " + opCivil.get().getName(),
								relationship.getBarColour(), relationship.getBarStyle());
					} else {
						barUnfinal = Bukkit.getServer().createBossBar("Welcome to " + opCivil.get().getName(),
								BarColor.WHITE, BarStyle.SOLID);
					}
					final BossBar bar = barUnfinal;
					bar.addPlayer(player);
					bar.setVisible(true);
					Bukkit.getScheduler().scheduleSyncDelayedTask(TTCivilizationsPlugins.getPlugin(), new Runnable() {

						@Override
						public void run() {
							bar.setVisible(false);
							bar.removeAll();
						}

					}, 5);
				}
			}
		}
	}

	@EventHandler
	public void loadEvent(ChunkLoadEvent event) {
		Civilization civil = Civilization.getByChunk(event.getChunk());
		civil.load();
	}

}
