package TTCivilizations.Civilization.Chunk;

import java.util.Optional;

import org.bukkit.Chunk;

public class Section {

	Chunk CHUNK;
	String NAME;
	SectionType TYPE;

	public Section(String name, SectionType type, Chunk chunk) {
		CHUNK = chunk;
		NAME = name;
		if (type == null) {
			TYPE = SectionType.NONE;
		} else {
			TYPE = type;
		}
	};

	public Chunk getChunk() {
		return CHUNK;
	}

	public Optional<String> getName() {
		return Optional.ofNullable(NAME);
	}

	public SectionType getType() {
		return TYPE;
	}

}
