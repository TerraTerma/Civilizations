package TTCivilizations.Civilization;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import TTCivilizations.Civilization.Chunk.Section;
import TTCivilizations.Civilization.Chunk.SectionType;
import TTCivilizations.Civilization.Roles.PlayerPermission;
import TTCivilizations.Civilization.Roles.PlayerRole;
import TTCivilizations.Mechs.CivilData;
import TTCivilizations.Mechs.PlayerMechs.CivilizationData;
import TTCore.Entity.Implementation.Living.Human.Player.TTAccountImp;
import TTCore.Entity.Living.Human.Player.TTAccount;
import TTCore.Entity.Living.Human.Player.TTPlayer;
import TTCore.Mech.DataHandler;
import TTCore.Mech.DataHandlers.SavableData;
import TTCore.Mech.DataStoreTypes.SavableDataStore.AbstractSavableDataStore;
import TTCore.Savers.Saver;

public class Civilization extends AbstractSavableDataStore {

	String NAME;
	List<Section> SECTIONS = new ArrayList<>();
	List<UUID> UUIDS = new ArrayList<>();

	static List<Civilization> CIVILS = new ArrayList<>();

	public static final File ROOT_FILE = new File("plugins/TTCore/Civilization");
	public static final Civilization WILDERNESS = new Civilization("Wilderness", CivilizationType.DEFAULT);
	public static final Civilization WAR_ZONE = new Civilization("War Zone", CivilizationType.DEFAULT);
	public static final Civilization SAFE_ZONE = new Civilization("Safe Zone", CivilizationType.DEFAULT);

	public static String DATA_NAME = "MetaData.Name";
	public static String DATA_SECTIONS = "MetaData.Sections";
	public static String DATA_WORLD = "MetaData.World";

	public Civilization(String name, CivilizationType type) {
		super(new File(ROOT_FILE, type.getName() + "/" + name + ".yml"));
		NAME = name;
		DataHandler.getHandlers().stream().forEach(d -> {
			try {
				DataHandler data = d.newInstance();
				if ((data instanceof SavableData) && (data instanceof CivilData)) {
					SavableData data2 = (SavableData) data;
					Saver saver = new Saver(getFile());
					saver.setSection("Mechs", d.getSimpleName());
					if (data2.load(saver)) {
						DATA.add(data2);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public String getName() {
		return NAME;
	}

	public List<Section> getSections() {
		return SECTIONS;
	}
	
	public void addChunk(Chunk chunk){
		SECTIONS.add(new Section(null, SectionType.NONE, chunk));
	}
	
	public void addChunk(Chunk chunk, String name){
		SECTIONS.add(new Section(name, SectionType.NONE, chunk));
	}
	
	public void addChunk(Chunk chunk, SectionType type){
		SECTIONS.add(new Section(null, type, chunk));
	}
	
	public void addChunk(Chunk chunk, SectionType type, String name){
		SECTIONS.add(new Section(name, type, chunk));
	}

	public List<TTAccount> getAccounts(PlayerPermission permission) {
		return getAccounts().stream().filter(a -> {
			CivilizationData data = a.getSingleData(CivilizationData.class).get();
			return (data.getPermission().equals(permission));
		}).collect(Collectors.toList());
	}

	public List<TTAccount> getAccounts(PlayerRole role) {
		return getAccounts().stream().filter(a -> {
			CivilizationData data = a.getSingleData(CivilizationData.class).get();
			return (data.getRole().equals(role));
		}).collect(Collectors.toList());
	}

	public List<TTAccount> getAccounts() {
		List<TTAccount> list = new ArrayList<>();
		UUIDS.stream().forEach(u -> {
			Optional<TTPlayer> opPlayer = TTPlayer.getPlayer(u);
			if (opPlayer.isPresent()) {
				list.add(opPlayer.get());
			} else {
				TTAccount account = new TTAccountImp(Bukkit.getOfflinePlayer(u));
				list.add(account);
			}
		});
		return list;
	}

	public boolean load() {
		if (!isLoaded()) {
			return CIVILS.add(this);
		}
		return false;
	}

	public List<Section> getLoadedSections() {
		if (CIVILS.contains(this)) {
			return SECTIONS.stream().filter(s -> {
				return (s.getChunk().isLoaded());
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	public boolean isLoaded() {
		return (!getLoadedSections().isEmpty());
	}

	public boolean unload() {
		return CIVILS.remove(this);
	}

	public static List<Civilization> getByWorld(World world) {
		List<Civilization> list = new ArrayList<>();
		File folderUser = new File(ROOT_FILE, CivilizationType.USER.getName());
		File folderDefault = new File(ROOT_FILE, CivilizationType.DEFAULT.getName());
		File[] filesUser = folderUser.listFiles();
		File[] filesDefault = folderDefault.listFiles();
		if (filesUser != null) {
			Optional<File> civil = Arrays.asList(filesUser).stream().filter(f -> {
				Saver saver = new Saver(f);
				String worldS = saver.get(String.class, DATA_WORLD);
				return (world.getName().equals(worldS));
			}).findFirst();
			list.add(new Civilization(civil.get().getName().replace(".yml", ""), CivilizationType.USER));
		}
		if (filesDefault != null) {
			Optional<File> civil = Arrays.asList(filesDefault).stream().filter(f -> {
				Saver saver = new Saver(f);
				String worldS = saver.get(String.class, DATA_WORLD);
				return (world.getName().equals(worldS));
			}).findFirst();
			String name = civil.get().getName().replace(".yml", "");
			switch (name) {
			case "War Zone":
				list.add(WAR_ZONE);
			case "Safe Zone":
				list.add(SAFE_ZONE);
			}
		}
		list.add(WILDERNESS);
		return list;
	}

	public static Optional<Civilization> getLoadedByChunk(Chunk chunk) {
		if (chunk.isLoaded()) {
			Optional<Civilization> opCivil = CIVILS.stream().filter(e -> {
				return e.getSections().stream().filter(s -> {
					return s.getChunk().equals(chunk);
				}).findFirst().isPresent();
			}).findFirst();
			if (opCivil.isPresent()) {
				return opCivil;
			} else {
				return Optional.of(WILDERNESS);
			}
		}
		return Optional.empty();
	}

	public static Civilization getByChunk(Chunk chunk) {
		File folderUser = new File(ROOT_FILE, CivilizationType.USER.getName());
		File folderDefault = new File(ROOT_FILE, CivilizationType.DEFAULT.getName());
		File[] filesUser = folderUser.listFiles();
		File[] filesDefault = folderDefault.listFiles();
		if (filesUser != null) {
			Optional<File> civil = Arrays.asList(filesUser).stream().filter(f -> {
				Saver saver = new Saver(f);
				List<String> sectionS = saver.getList(String.class, DATA_SECTIONS);
				if (sectionS != null) {
					Optional<String> section = sectionS.stream().filter(s -> {
						String[] args = s.split(",");
						if ((chunk.getWorld().getName().equals(saver.get(String.class, DATA_WORLD)))
								&& (chunk.getX() == Integer.parseInt(args[0]))
								&& (chunk.getZ() == Integer.parseInt(args[1]))) {
							return true;
						}
						return false;
					}).findFirst();
					return section.isPresent();
				}
				return false;
			}).findFirst();
			if (civil.isPresent()) {
				return new Civilization(civil.get().getName().replace(".yml", ""), CivilizationType.USER);
			}
		}
		if (filesDefault != null) {
			Optional<File> civil = Arrays.asList(filesDefault).stream().filter(f -> {
				Saver saver = new Saver(f);
				List<String> sectionS = saver.getList(String.class, DATA_SECTIONS);
				if (sectionS != null) {
					Optional<String> section = sectionS.stream().filter(s -> {
						String[] args = s.split(",");
						if ((chunk.getWorld().getName().equals(args[2])) && (chunk.getX() == Integer.parseInt(args[0]))
								&& (chunk.getZ() == Integer.parseInt(args[1]))) {
							return true;
						}
						return false;
					}).findFirst();
					return section.isPresent();
				}
				return false;
			}).findFirst();
			if (civil.isPresent()) {
				String name = civil.get().getName().replace(".yml", "");
				switch (name) {
				case "War Zone":
					return WAR_ZONE;
				case "Safe Zone":
					return SAFE_ZONE;
				}
			}
		}
		return WILDERNESS;
	}

	public static Optional<Civilization> getLoadedByPlayer(UUID uuid) {
		return CIVILS.stream().filter(c -> {
			return c.UUIDS.stream().anyMatch(u -> {
				return (u.equals(uuid));
			});
		}).findFirst();
	}

}
