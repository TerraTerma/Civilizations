package TTCivilizations.Ticker;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Civilization.SectionedCivilization;

public class Ticker {

	public void civilizationTick() {
		Bukkit.getScheduler().runTaskTimer(TTCivilizationsPlugins.getPlugin(), new Runnable() {

			@Override
			public void run() {
				Bukkit.getWorlds().stream().forEach(w -> {
					w.getEntities().stream().forEach(e -> {
						if (e instanceof Creature) {
							Optional<SectionedCivilization> civil = Civilization.getLoadedByChunk(e.getLocation().getChunk());
							if (civil.isPresent()) {
								e.remove();
							}
						}
					});
				});

			}

		}, 20L, 10);
	}
}
