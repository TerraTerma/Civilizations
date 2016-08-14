package TTCivilizations.Civilization;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.World;

import TTCivilizations.Civilization.Chunk.Section;
import TTCivilizations.Civilization.Chunk.SectionType;
import TTCore.Mech.DataStores.SavableDataStore;

public interface SectionedCivilization extends Civilization, SavableDataStore{
	
	static List<SectionedCivilization> CIVILS = new ArrayList<>();
	
	public List<Section> getSections();
	public List<Section> getLoadedSections();
	public Civilization addChunk(Chunk chunk);
	public Civilization addChunk(Chunk chunk, String name);
	public Civilization addChunk(Chunk chunk, SectionType type);
	public Civilization addChunk(Chunk chunk, SectionType type, String name);
	public World getWorld();
	public boolean load();
	public boolean isLoaded();
	public boolean unload();

}
