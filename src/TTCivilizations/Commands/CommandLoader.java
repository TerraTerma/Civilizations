package TTCivilizations.Commands;

import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Chunk.SectionType;
import TTCivilizations.Civilization.Roles.PlayerPermission;
import TTCivilizations.Commands.CMD.CivOwnerCMD;
import TTCivilizations.Commands.CMD.CreateCMD;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Entity.Living.Human.Player.TTAccount;
import TTCore.Entity.Living.Human.Player.TTPlayer;

public class CommandLoader implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String length, String[] args) {
		if (sender instanceof Player) {
			TTPlayer player = TTPlayer.getPlayer((Player) sender);
			TTCivilizationsPlugins plugin = TTCivilizationsPlugins.getPlugin();
			if (args.length == 0) {
				// HELP
				CivilizationData data = player.getSingleData(CivilizationData.class).get();
				switch (data.getPermission()) {
				case DEMI_LEADER:
					player.sendMessage(plugin, "/c expand [expand type] [name] - expand a civilization");
					player.sendMessage(plugin, "/c expand list - list all expand types");
					return true;
				case DEMI_MOD:
					break;
				case LEADER:
					player.sendMessage(plugin, "/c expand [expand type] [name] - expand a civilization");
					player.sendMessage(plugin, "/c expand list - list all expand types");
					return true;
				case MOD:
					break;
				case NONE:
					player.sendMessage(plugin, "/c create <name> - create a civilization");
					return true;
				case STANDARD:
					break;
				default:
					break;
				}
			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length >= 2) {
					CreateCMD.createCivil(player, args[1]);
				} else {
					player.sendMessage(plugin, "Please specifiy your Civilization name");
				}
			} else if (args[0].equalsIgnoreCase("expand")) {
				if (args.length >= 3) {
					CreateCMD.createChunk(player, SectionType.valueOf(args[1].toUpperCase()), args[2]);
				} else if (args.length == 2) {
					if (args[1].equalsIgnoreCase("list")) {
						player.getPlayer().sendMessage(ChatColor.YELLOW + "|----[expand types]----|");
						for (SectionType type : SectionType.values()) {
							player.getPlayer().sendMessage(ChatColor.AQUA + "- " + type.name().toLowerCase());
						}
					} else {
						CreateCMD.createChunk(player, SectionType.valueOf(args[1].toUpperCase()), null);
					}
				} else {
					CreateCMD.createChunk(player, SectionType.NONE, null);
				}
			} else if (args[0].equalsIgnoreCase("permission")) {
				if (args.length >= 3) {
					Optional<TTAccount> opAccount = TTAccount.getAccount(args[1]);
					if (opAccount.isPresent()) {
						CivOwnerCMD.setPermission(player, opAccount.get(), PlayerPermission.valueOf(args[2]));
					}
				}
			}
			return true;
		}
		return false;
	}

}
