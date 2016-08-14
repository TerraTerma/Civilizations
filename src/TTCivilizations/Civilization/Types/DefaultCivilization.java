package TTCivilizations.Civilization.Types;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import TTCivilizations.Civilization.Civilization;
import TTCivilizations.Civilization.SectionedCivilization;
import TTCivilizations.Civilization.Chunk.Section;
import TTCivilizations.Civilization.Chunk.SectionType;
import TTCivilizations.Mechs.CivilData;
import TTCore.Mech.DataHandler;
import TTCore.Mech.DataHandlers.SavableData;
import TTCore.Mech.DataStores.SavableDataStore;
import TTCore.Savers.Saver;

public class DefaultCivilization extends SavableDataStore.AbstractSavableDataStore implements SectionedCivilization{

	public static final DefaultCivilization SAFE_ZONE = new DefaultCivilization("Safe Zone");
	public static final DefaultCivilization WAR_ZONE = new DefaultCivilization("War Zone");
	public static final Wilderness WILDERNESS = new Wilderness();
	
	String NAME;
	List<Section> SECTIONS;
	
	public DefaultCivilization(String name) {
		super(new File(ROOT_FILE, "Default/" + name + ".yml"));
		NAME = name;
		Saver saver = new Saver(getFile());
		if (saver.getFile().exists()) {
			String worldS = saver.get(String.class, DATA_WORLD);
			if (worldS != null) {
				World world = Bukkit.getWorld(worldS);
				List<String> sectionS = saver.getList(String.class, DATA_SECTIONS);
				sectionS.stream().forEach(s -> {
					String[] args = s.split(",");
					if (args.length == 3) {
						Chunk chunk = world.getChunkAt(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
						SectionType sectionType = SectionType.valueOf(args[2]);
						addChunk(chunk, sectionType);
					} else {
						Chunk chunk = world.getChunkAt(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
						SectionType sectionType = SectionType.valueOf(args[2]);
						addChunk(chunk, sectionType, args[3]);
					}
				});
				DataHandler.getHandlers().stream().forEach(d -> {
					try {
						DataHandler data = d.newInstance();
						if ((data instanceof SavableData) && (data instanceof CivilData)) {
							SavableData data2 = (SavableData) data;
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
		}
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public List<Section> getSections() {
		return SECTIONS;
	}

	@Override
	public List<Section> getLoadedSections() {
		if (CIVILS.contains(this)) {
			return SECTIONS.stream().filter(s -> {
				return (s.getChunk().isLoaded());
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@Override
	public Civilization addChunk(Chunk chunk) {
		SECTIONS.add(new Section(null, SectionType.NONE, chunk));
		return this;
	}

	@Override
	public Civilization addChunk(Chunk chunk, String name) {
		SECTIONS.add(new Section(name, SectionType.NONE, chunk));
		return this;
	}

	@Override
	public Civilization addChunk(Chunk chunk, SectionType type) {
		SECTIONS.add(new Section(null, type, chunk));
		return this;
	}

	@Override
	public Civilization addChunk(Chunk chunk, SectionType type, String name) {
		SECTIONS.add(new Section(name, type, chunk));
		return this;
	}
	
	@Override
	public World getWorld() {
		return SECTIONS.get(0).getChunk().getWorld();
	}

	@Override
	public boolean isLoaded() {
		return (!getLoadedSections().isEmpty());
	}

	@Override
	public boolean unload() {
		return CIVILS.remove(this);
	}
	
	@Override
	public void saveAll() {
		Saver saver = new Saver(getFile());
		List<String> sectionS = new ArrayList<>();
		List<String> uuidS = new ArrayList<>();
		SECTIONS.stream().forEach(s -> {
			Chunk chunk = s.getChunk();
			String section = chunk.getX() + "," + chunk.getZ() + "," + s.getType().name();
			if (s.getName().isPresent()) {
				section = section + "," + s.getName().get();
			}
			sectionS.add(section);
		});

		saver.set(sectionS, DATA_SECTIONS);
		saver.set(SECTIONS.get(0).getChunk().getWorld().getName(), DATA_WORLD);
		saver.set(uuidS, DATA_MEMBERS);
		saver.save();
		super.saveAll();
	}

	@Override
	public boolean load() {
		if (!isLoaded()) {
			return CIVILS.add(this);
		}
		return false;
	}

}
