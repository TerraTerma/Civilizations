package TTCivilizations.Commands.CMD;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import TTCivilizations.TTCivilizationsPlugins;
import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Civilization.Roles.PlayerPermission;
import TTCivilizations.Civilization.Types.UserCivilization;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Entity.Living.Human.Player.TTAccount;
import TTCore.Entity.Living.Human.Player.TTPlayer;

public class CivOwnerCMD {

	public static void setPermission(TTPlayer owner, TTAccount player, PlayerPermission permission) {
		TTCivilizationsPlugins plugin = TTCivilizationsPlugins.getPlugin();
		CivilizationData ownerData = owner.getSingleData(CivilizationData.class).get();
		CivilizationData playerData = player.getSingleData(CivilizationData.class).get();
		List<String> ownerArgs = Arrays.asList(ownerData.getPermission().getExtraCMD(owner));
		if (ownerArgs.contains("permission")) {
			UserCivilization ownerCivil = Civilization.getByPlayer(owner.getPlayer().getUniqueId()).get();
			Optional<TTPlayer> opPlayer = player.getOnline();
			if (ownerCivil.UUIDS.contains(player.getPlayer().getUniqueId())) {
				switch (permission) {
				case DEMI_LEADER:
					Optional<TTAccount> demiLeader = ownerCivil.getDemiLeader();
					if(demiLeader.isPresent()){
						CivilizationData dL = demiLeader.get().getSingleData(CivilizationData.class).get();
						dL.setPermission(playerData.getPermission());
					}
					playerData.setPermission(permission);
					owner.sendMessage(plugin,
							"You have set " + player.getPlayer().getName() + " to the demi-leader.");
					if (opPlayer.isPresent()) {
						opPlayer.get().sendMessage(plugin, "Your leader has set you to a demi-leader.");
					}
					return;
				case DEMI_MOD:
					playerData.setPermission(permission);
					owner.sendMessage(plugin,
							"You have set " + player.getPlayer().getName() + " to a demi-mod.");
					if (opPlayer.isPresent()) {
						opPlayer.get().sendMessage(plugin, "Your leader has set you to a demi-mod.");
					}
					return;
				case LEADER:
					if(ownerData.getPermission().equals(PlayerPermission.LEADER)){
						ownerData.setPermission(playerData.getPermission());
						playerData.setPermission(permission);
						owner.sendMessage(plugin,
								"You have set " + player.getPlayer().getName() + " to the leader, you have taken their place");
						if (opPlayer.isPresent()) {
							opPlayer.get().sendMessage(plugin, "Your leader has set you as the leader, they have taken your place");
						}
					}else{
						owner.sendMessage(plugin, "Only the true owner can do that");
					}
					return;
				case MOD:
					playerData.setPermission(permission);
					owner.sendMessage(plugin,
							"You have set " + player.getPlayer().getName() + " to a mod.");
					if (opPlayer.isPresent()) {
						opPlayer.get().sendMessage(plugin, "Your leader has set you to a mod.");
					}
					return;
				case NONE:
					owner.sendMessage(plugin, "You can not set a player civilization permission to none");
					return;
				case STANDARD:
					playerData.setPermission(permission);
					owner.sendMessage(plugin,
							"You have set " + player.getPlayer().getName() + " to a standard civilization member");
					if (opPlayer.isPresent()) {
						opPlayer.get().sendMessage(plugin, "Your leader has set you to a standard civilization member");
					}
					return;
				default:
					owner.sendMessage(plugin,
							"You can not set a player as " + permission + ". Please contact the devs with this error.");
					return;

				}
			}
		}
	}

}
