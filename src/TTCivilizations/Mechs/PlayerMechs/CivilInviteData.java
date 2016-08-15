package TTCivilizations.Mechs.PlayerMechs;

import TTCore.Entity.Living.Human.Player.TTAccount;
import TTCore.Mech.DataHandlers.PlayerData;

public class CivilInviteData implements PlayerData {
	
	TTAccount INVITED_BY;
	
	public TTAccount getInvitedBy(){
		return INVITED_BY;
	}
	
	public CivilInviteData setInviteBy(TTAccount player){
		INVITED_BY = player;
		return this;
	}

}
