package TTCivilizations.Civilization.Roles;

import java.util.List;
import java.util.Optional;

import TTCivilizations.Civilization.Civilization;
import TTCore.Entity.Living.Human.Player.TTAccount;

public enum PlayerPermission {

	NONE(false, "join"),
	STANDARD(false),
	DEMI_MOD(true),
	MOD(false, "invite", "reject", "kick"),
	DEMI_LEADER(true),
	LEADER(false, "expand", "autoexpand", "kick");

	String[] ARGS;
	boolean IS_DEMI;

	private PlayerPermission(boolean demi, String... cmd) {
		IS_DEMI = demi;
		ARGS = cmd;
	}
	
	public boolean isDemi(){
		return IS_DEMI;
	}

	public String[] getExtraCMD(TTAccount player) {
		if (IS_DEMI) {
			Optional<Civilization> opCivil = Civilization.getLoadedByPlayer(player.getPlayer().getUniqueId());
			if (opCivil.isPresent()) {
				Civilization civil = opCivil.get();
				PlayerPermission next = getNextPermission().get();
				List<TTAccount> accounts = civil.getAccounts();
				if(!accounts.stream().anyMatch(a -> a.getPlayer().isOnline())){
					return next.ARGS;
				}else{
					Optional<PlayerPermission> opPermission = getPreviousPermission();
					if(opPermission.isPresent()){
						return opPermission.get().ARGS;
					}
				}
			}
		}else{
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
