package TTCivilizations.Commands.CMD;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Chunk;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Civilization.Chunk.ChunkUtils;
import TTCivilizations.Civilization.Chunk.SectionType;
import TTCivilizations.Civilization.Roles.PlayerPermission;
import TTCivilizations.Civilization.Roles.PlayerRole;
import TTCivilizations.Civilization.Types.UserCivilization;
import TTCivilizations.Mechs.CivilMechs.Vault.CivilVaultData;
import TTCivilizations.Mechs.CivilMechs.Vault.CivilVaultType;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Entity.Living.Human.Player.TTPlayer;

public class CreateCMD {

	public static final float PRICE_OF_CHUNK = 14;
	public static final float PRICE_OF_CIVIL = 20000;

	public static void createCivil(TTPlayer player, String name) {
		TTCivilizationsPlugins plugin = TTCivilizationsPlugins.getPlugin();
		CivilizationData data = player.getSingleData(CivilizationData.class).get();
		Optional<UserCivilization> opCivil = Civilization.getByPlayer(player.getPlayer().getUniqueId());
		if (!opCivil.isPresent()) {
			if (data.getPower() >= 10) {
				if (player.getBalance() >= PRICE_OF_CIVIL) {
					UserCivilization civil = new UserCivilization(name);
					civil.addChunk(player.getPlayer().getLocation().getChunk(), "Source");
					civil.addPlayer(player, PlayerRole.NONE, PlayerPermission.LEADER);
					civil.load();
					civil.saveAll();
					player.sendMessage(plugin, civil.getName() + " was created");
				} else {
					player.sendMessage(plugin, "You need " + PRICE_OF_CIVIL + " to buy a Civilization");
				}
			} else {
				player.sendMessage(plugin, "You need 10+ power before you can create a Civilization");
			}
		} else {
			player.sendMessage(plugin, "Please remove yourself from your current Civilization");
		}
	}

	public static void createChunk(TTPlayer player, SectionType type, String name) {
		TTCivilizationsPlugins plugin = TTCivilizationsPlugins.getPlugin();
		CivilizationData data = player.getSingleData(CivilizationData.class).get();
		List<String> args = Arrays.asList(data.getPermission().getExtraCMD(player));
		if (args.contains("expand") || args.contains("autoexpand")) {
			Optional<UserCivilization> opCivil = Civilization.getLoadedByPlayer(player.getPlayer().getUniqueId());
			if (opCivil.isPresent()) {
				UserCivilization civil = opCivil.get();
				Chunk chunk = player.getPlayer().getLocation().getChunk();
				boolean check = false;
				for (int X = -1; X < 2; X++) {
					for (int Z = -1; Z < 2; Z++) {
						Chunk chunk2 = ChunkUtils.getRelitive(chunk, X, Z);
						if (civil.getSections().stream().anyMatch(s -> s.getChunk().equals(chunk2))) {
							check = true;
							break;
						}
					}
				}
				if (check) {
					CivilVaultData vault = civil.getSingleData(CivilVaultData.class).get();
					float price = (PRICE_OF_CHUNK * civil.getSections().size());
					if (vault.withdraw(CivilVaultType.EXPAND, price)) {
						civil.addChunk(chunk, type, name);
						player.sendMessage(plugin, "chunk is now claimed");
					} else {
						player.sendMessage(plugin, "You do not have " + price + " within your Expand department");
					}
				} else {
					player.sendMessage(plugin, "chunk must be directly touching the civilization");
				}

			}
		}
	}

}
