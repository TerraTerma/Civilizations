package TTCivilizations.Civilization;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import TTCivilizations.Civilization.Flags.CivilizationFlag;
import TTCivilizations.Civilization.Types.DefaultCivilization;
import TTCivilizations.Civilization.Types.UserCivilization;
import TTCore.Savers.Saver;

public interface Civilization {

	public static final File ROOT_FILE = new File("plugins/TTCore/Civilization");
	public static String DATA_NAME = "MetaData.Name";
	public static String DATA_WORLD = "MetaData.World";
	public static String DATA_MEMBERS = "Data.Members";
	public static String DATA_SECTIONS = "MetaData.Sections";
	public static String DATA_FLAGS = "Data.Flags";

	public String getName();
	
	public List<CivilizationFlag<? extends Object>> getFlags();
	
	public Optional<CivilizationFlag<? extends Object>> getFlag(String name);

	public static List<Civilization> getByWorld(World world) {
		List<Civilization> list = new ArrayList<>();
		File folderUser = new File(ROOT_FILE, "User");
		File folderDefault = new File(ROOT_FILE, "Default");
		File[] filesUser = folderUser.listFiles();
		File[] filesDefault = folderDefault.listFiles();
		if (filesUser != null) {
			Optional<File> civil = Arrays.asList(filesUser).stream().filter(f -> {
				Saver saver = new Saver(f);
				String worldS = saver.get(String.class, DATA_WORLD);
				return (world.getName().equals(worldS));
			}).findFirst();
			list.add(new UserCivilization(civil.get().getName().replace(".yml", "")));
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
				list.add(DefaultCivilization.WAR_ZONE);
			case "Safe Zone":
				list.add(DefaultCivilization.SAFE_ZONE);
			}
		}
		list.add(DefaultCivilization.WILDERNESS);
		return list;
	}

	public static Optional<SectionedCivilization> getLoadedByChunk(Chunk chunk) {
		if (chunk.isLoaded()) {
			Optional<SectionedCivilization> opCivil = UserCivilization.CIVILS.stream().filter(e -> {
				return e.getSections().stream().filter(s -> {
					return s.getChunk().equals(chunk);
				}).findFirst().isPresent();
			}).findFirst();
			if (opCivil.isPresent()) {
				return Optional.of(opCivil.get());
			}
		}
		return Optional.empty();
	}

	public static Optional<UserCivilization> getByPlayer(UUID uuid) {
		File folderUser = new File(ROOT_FILE, "User");
		File[] filesUser = folderUser.listFiles();
		if (filesUser != null) {
			Optional<File> opFile = Arrays.asList(filesUser).stream().filter(f -> {
				Saver saver = new Saver(f);
				return (saver.getList(String.class, DATA_MEMBERS).stream().anyMatch(u -> u.equals(uuid.toString())));
			}).findFirst();
			if (opFile.isPresent()) {
				return Optional.of(new UserCivilization(opFile.get().getName().replace(".yml", "")));
			}
		}
		return Optional.empty();
	}

	public static Civilization getByChunk(Chunk chunk) {
		File folderUser = new File(ROOT_FILE, "User");
		File folderDefault = new File(ROOT_FILE, "Default");
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
				return new UserCivilization(civil.get().getName().replace(".yml", ""));
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
					return DefaultCivilization.WAR_ZONE;
				case "Safe Zone":
					return DefaultCivilization.SAFE_ZONE;
				}
			}
		}
		return DefaultCivilization.WILDERNESS;
	}

	public static Optional<UserCivilization> getLoadedByPlayer(UUID uuid) {
		Optional<SectionedCivilization> opCivil = SectionedCivilization.CIVILS.stream().filter(c -> {
			if (c instanceof UserCivilization) {
				return ((UserCivilization) c).UUIDS.stream().anyMatch(u -> {
					return (u.equals(uuid));
				});
			}
			return false;
		}).findFirst();
		if (opCivil.isPresent()) {
			return Optional.of((UserCivilization) opCivil.get());
		}
		return Optional.empty();
	}

	public static void reload() {
		File folderUser = new File(ROOT_FILE, "User");
		File folderDefault = new File(ROOT_FILE, "Default");
		File[] filesUser = folderUser.listFiles();
		File[] filesDefault = folderDefault.listFiles();
		if (filesUser != null) {
			Optional<File> civil = Arrays.asList(filesUser).stream().filter(f -> {
				Saver saver = new Saver(f);
				List<String> sectionS = saver.getList(String.class, DATA_SECTIONS);
				String worldS = saver.get(String.class, DATA_WORLD);
				if (worldS != null) {
					World world = Bukkit.getWorld(worldS);
					Optional<String> section = sectionS.stream().filter(s -> {
						String[] args = s.split(",");
						Chunk chunk = world.getChunkAt(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
						return (chunk.isLoaded());
					}).findFirst();
					return section.isPresent();
				}
				return false;
			}).findFirst();
			if (civil.isPresent()) {
				UserCivilization civil2 = new UserCivilization(civil.get().getName().replace(".yml", ""));
				civil2.load();
			}
		}
		if (filesDefault != null) {
			Optional<File> civil = Arrays.asList(filesDefault).stream().filter(f -> {
				Saver saver = new Saver(f);
				List<String> sectionS = saver.getList(String.class, DATA_SECTIONS);
				String worldS = saver.get(String.class, DATA_WORLD);
				if (worldS != null) {
					World world = Bukkit.getWorld(worldS);
					Optional<String> section = sectionS.stream().filter(s -> {
						String[] args = s.split(",");
						Chunk chunk = world.getChunkAt(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
						return (chunk.isLoaded());
					}).findFirst();
					return section.isPresent();
				}
				return false;
			}).findFirst();
			if (civil.isPresent()) {
				String name = civil.get().getName().replace(".yml", "");
				switch (name) {
				case "War Zone":
					DefaultCivilization.WAR_ZONE.load();
				case "Safe Zone":
					DefaultCivilization.SAFE_ZONE.load();
				}
			}
		}
	}

}
