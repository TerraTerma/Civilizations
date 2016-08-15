package TTCivilizations.Mechs.CivilMechs.Vault;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import TTCivilizations.Mechs.CivilData;
import TTCore.Mech.DataHandlers.SavableData;
import TTCore.Savers.Saver;
import TTCore.Stores.OneStore;

public class CivilVaultData implements CivilData, SavableData {

	public static final String DATA_CASH = "Bal";

	Map<CivilVaultType, Float> CASH = new HashMap<>();

	public float getBalance() {
		OneStore<Float> cash = new OneStore<>(0f);
		CASH.entrySet().stream().forEach(c -> {
			cash.setOne(cash.getOne() + c.getValue());
		});
		return cash.getOne();
	}

	public float getBalance(CivilVaultType type) {
		return CASH.get(type);
	}

	public boolean withdraw(CivilVaultType type, float amount) {
		float old = getBalance(type);
		if (old >= amount) {
			CASH.replace(type, (old - amount));
			return true;
		}
		return false;
	}

	public CivilVaultData deposit(CivilVaultType type, float amount) {
		float old = getBalance(type);
		CASH.replace(type, (old + amount));
		return this;
	}

	public boolean transfer(CivilVaultType from, float amount, CivilVaultType to) {
		if (withdraw(from, amount)) {
			deposit(to, amount);
			return true;
		}
		return false;
	}

	@Override
	public void save(Saver saver) {
		CASH.entrySet().stream().forEach(e -> {
			saver.set(e.getValue(), DATA_CASH + "." + e.getKey().name());
		});
	}

	@Override
	public boolean load(Saver saver) {
		Arrays.asList(CivilVaultType.values()).stream().forEach(v -> {
			Float bal = saver.get(Float.class, DATA_CASH + "." + v.name());
			if (bal == null) {
				CASH.put(v, 0f);
			} else {
				CASH.put(v, bal);
			}
		});
		return true;
	}

}
