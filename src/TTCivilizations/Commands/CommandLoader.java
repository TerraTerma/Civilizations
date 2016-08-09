package TTCivilizations.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Chunk.SectionType;
import TTCivilizations.Commands.CMD.CreateCMD;
import TTCore.Entity.Living.Human.Player.TTPlayer;

public class CommandLoader implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String length, String[] args) {
		if(sender instanceof Player){
			TTPlayer player = TTPlayer.getPlayer((Player)sender);
			TTCivilizationsPlugins plugin = TTCivilizationsPlugins.getPlugin();
		if(args.length == 0){
			//HELP
			player.sendMessage(plugin, "/c create <name> - create a civilization");
			player.sendMessage(plugin, "/c expand [expand type] [name] - expand a civilization");
			player.sendMessage(plugin, "/c expand list - list all expand types");
		}else if (args[0].equalsIgnoreCase("create")){
			if(args.length >= 2){
				CreateCMD.createCivil(player, args[1]);
			}else{
				player.sendMessage(plugin, "Please specifiy your Civilization name");
			}
		}else if (args[0].equalsIgnoreCase("expand")){
			if(args.length >= 3){
				CreateCMD.createChunk(player, SectionType.valueOf(args[1].toUpperCase()), args[2]);
			}else if(args.length == 2){
				if(args[1].equalsIgnoreCase("list")){
					player.getPlayer().sendMessage(ChatColor.YELLOW + "|----[expand types]----|");
					for(SectionType type : SectionType.values()){
						player.getPlayer().sendMessage(ChatColor.AQUA + "- " + type.name().toLowerCase());
					}
				}else{
					CreateCMD.createChunk(player, SectionType.valueOf(args[1].toUpperCase()), null);
				}
			}else{
				CreateCMD.createChunk(player, SectionType.NONE, null);
			}
		}
		return true;
		}
		return false;
	}

}
