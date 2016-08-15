package TTCivilizations.Mechs.CivilMechs.CivilRelationshipData;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public enum CivilRelationshipType {

	FRIENDLY(BarColor.YELLOW, BarStyle.SOLID, ChatColor.YELLOW),
	NETRUAL(BarColor.GREEN, BarStyle.SEGMENTED_6, ChatColor.DARK_GREEN),
	UNTRUSTED(BarColor.BLUE, BarStyle.SEGMENTED_12, ChatColor.DARK_AQUA),
	AT_WAR(BarColor.RED, BarStyle.SEGMENTED_20, ChatColor.DARK_RED);

	BarColor BAR_COLOUR;
	BarStyle BAR_STYLE;
	ChatColor CHAT_COLOUR;

	private CivilRelationshipType(BarColor barC, BarStyle barS, ChatColor colour) {
		BAR_COLOUR = barC;
		BAR_STYLE = barS;
		CHAT_COLOUR = colour;
	}

	public BarColor getBarColour() {
		return BAR_COLOUR;
	}

	public BarStyle getBarStyle() {
		return BAR_STYLE;
	}

	public ChatColor getChatColour() {
		return CHAT_COLOUR;
	}

}
