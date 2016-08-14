package TTCivilizations.Civilization.Roles;

import java.util.List;
import java.util.Optional;

import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Civilization.Types.UserCivilization;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Entity.Living.Human.Player.TTAccount;
import TTCore.Entity.Living.Human.Player.TTPlayer;
import TTCore.Entity.Living.Human.Player.Lists.AccountList;

public enum PlayerPermission {

	NONE(false, "join", "create"),
	STANDARD(false),
	DEMI_MOD(true),
	MOD(false, "invite", "reject", "kick"),
	DEMI_LEADER(true),
	LEADER(false, "expand", "autoexpand", "permission");

	String[] ARGS;
	boolean IS_DEMI;

	private PlayerPermission(boolean demi, String... cmd) {
		IS_DEMI = demi;
		ARGS = cmd;
	}

	public boolean isDemi() {
		return IS_DEMI;
	}

	public AccountList<TTPlayer> getNextOnline(TTAccount account) {
		AccountList<TTPlayer> list = new AccountList<>();
		Optional<UserCivilization> opCivil = Civilization.getByPlayer(account.getPlayer().getUniqueId());
		if (opCivil.isPresent()) {
			CivilizationData data = account.getSingleData(CivilizationData.class).get();
			Civilization civil2 = opCivil.get();
			if (civil2 instanceof UserCivilization) {
				UserCivilization civil = (UserCivilization) civil2;
				Optional<PlayerPermission> opPermission = data.getPermission().getNextPermission();
				if (opPermission.isPresent()) {
					civil.getAccounts(opPermission.get()).stream().forEach(p -> {
						Optional<TTPlayer> opPlayer = p.getOnline();
						if (opPlayer.isPresent()) {
							list.add(opPlayer.get());
						}
					});
				}
			}
		}
		return list;
	}

	public String[] getExtraCMD(TTAccount player) {
		if (IS_DEMI) {
			Optional<UserCivilization> opCivil = Civilization.getLoadedByPlayer(player.getPlayer().getUniqueId());
			if (opCivil.isPresent()) {
				Civilization civil2 = opCivil.get();
				if (civil2 instanceof UserCivilization) {
					UserCivilization civil = (UserCivilization) civil2;
					PlayerPermission next = getNextPermission().get();
					List<TTAccount> accounts = civil.getAccounts();
					if (!accounts.stream().anyMatch(a -> a.getPlayer().isOnline())) {
						return next.ARGS;
					} else {
						Optional<PlayerPermission> opPermission = getPreviousPermission();
						if (opPermission.isPresent()) {
							return opPermission.get().ARGS;
						}
					}
				}
			}
		} else {
			return ARGS;
		}
		String[] args = {};
		return args;
	}

	public Optional<PlayerPermission> getPreviousPermission() {
		for (int A = 0; A < (values().length - 1); A++) {
			PlayerPermission permission = values()[A];
			if (permission.equals(this)) {
				return Optional.of(values()[(A + 1)]);
			}
		}
		return Optional.empty();
	}

	public Optional<PlayerPermission> getNextPermission() {
		for (int A = 0; A < (values().length - 1); A++) {
			PlayerPermission permission = values()[A];
			if (permission.equals(this)) {
				return Optional.of(values()[(A + 1)]);
			}
		}
		return Optional.empty();
	}

}
