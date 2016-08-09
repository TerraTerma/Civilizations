package TTCivilizations.Civilization.Chunk;

import org.bukkit.Chunk;

public class ChunkUtils {
	
	public static Chunk getRelitive(Chunk chunk, int X, int Z){
		return chunk.getWorld().getChunkAt(X+chunk.getX(), Z+chunk.getZ());
	}

}
