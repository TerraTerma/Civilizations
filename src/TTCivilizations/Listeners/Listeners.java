package TTCivilizations.Listeners;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Mechs.CivilMechs.CivilRelationshipData.CivilRelationshipData;
import TTCivilizations.Mechs.CivilMechs.CivilRelationshipData.CivilRelationshipType;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Entity.Living.Human.Player.TTPlayer;

public class Listeners implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void killEvent(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			Player player = (Player) entity;
			// DEATH EVENT
			TTPlayer ttPlayer = TTPlayer.getPlayer(player);
			
			Optional<Civilization> opCurrentCivil = Civilization.getLoadedByChunk(player.getLocation().getChunk());
			if (opCurrentCivil.isPresent()) {
				Civilization civil = opCurrentCivil.get();
				if (civil.UUIDS.contains(player.getUniqueId())) {
					event.setCancelled(true);
				}
			}
			
			if (event.isCancelled()) {
				if(!player.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
				if (player.getHealth() <= event.getDamage()) {
					CivilizationData data = ttPlayer.getSingleData(CivilizationData.class).get();
					int power = data.getPower();
					if (power != 0) {
						data.setPower(power - 1);
						ttPlayer.sendMessage(TTCivilizationsPlugins.getPlugin(), "Power is now " + data.getPower());
					}
				}
				}
			}
		}
	}
	
	@EventHandler
	public void playerDamageEntity(EntityDamageByEntityEvent event){
		TTCivilizationsPlugins plugin = TTCivilizationsPlugins.getPlugin();
		Entity dEntity = event.getDamager();
		Entity entity = event.getEntity();
		if(dEntity instanceof Player){
			TTPlayer damager = TTPlayer.getPlayer((Player)dEntity);
			if(entity instanceof LivingEntity){
				LivingEntity living = (LivingEntity)entity;
				if(living.getHealth() <= event.getDamage()){
					CivilizationData data = damager.getSingleData(CivilizationData.class).get();
					data.setPower(data.getPower() + 1);
					damager.sendMessage(plugin, "Power is now " + data.getPower());
				}
			}
		}
	}

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
		TTPlayer ttPlayer = TTPlayer.getPlayer(player);
		Optional<Civilization> opCivil = Civilization.getLoadedByPlayer(player.getUniqueId());
		Optional<Civilization> opCivilTo = Civilization.getLoadedByChunk(event.getTo().getChunk());
		Optional<Civilization> opCivilFrom = Civilization.getLoadedByChunk(event.getFrom().getChunk());

		if (opCivilTo.isPresent()) {
			if (opCivilFrom.isPresent()) {
				Civilization civilTo = opCivilTo.get();
				Civilization civilFrom = opCivilFrom.get();
				if (!civilFrom.equals(civilTo)) {
					if (opCivil.isPresent()) {
						Civilization civil = opCivil.get();
						CivilRelationshipType relationship = civil.getOrCreateSingleData(CivilRelationshipData.class)
								.getRelationshipWith(civilTo);
						if (civilTo.equals(Civilization.WILDERNESS)) {
							ttPlayer.createBar(true, relationship.getBarColour(), relationship.getBarStyle(),
									relationship.getChatColour() + "You have left " + civilFrom.getName());
							Bukkit.getScheduler().scheduleSyncDelayedTask(TTCivilizationsPlugins.getPlugin(),
									new Runnable() {

										@Override
										public void run() {
											ttPlayer.removeBars();
										}

									}, 25);
						} else {
							ttPlayer.createBar(true, relationship.getBarColour(), relationship.getBarStyle(),
									relationship.getChatColour() + "You are in " + civilTo.getName());
						}
					} else if (civilTo.equals(Civilization.WILDERNESS)) {
						ttPlayer.createBar(true, BarColor.WHITE, BarStyle.SOLID,
								"You have left " + civilFrom.getName());
						Bukkit.getScheduler().scheduleSyncDelayedTask(TTCivilizationsPlugins.getPlugin(),
								new Runnable() {

									@Override
									public void run() {
										ttPlayer.removeBars();
									}

								}, 25);
					} else {
						ttPlayer.createBar(true, BarColor.WHITE, BarStyle.SOLID, "You are in " + civilTo.getName());
					}
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
