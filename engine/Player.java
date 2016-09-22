import java.util.*;

public class Player {

	String player_name;
	String player_age;
	String jersey_number;
	String points_scored;
	String player_id = player_name + player_age + jersey_number;

	Vector<String> player_attributes = new Vector<String>(5);
	// player_attributes[0] represents player ID
	// player_attributes[1] represents name
	// player_attributes[2] represents age
	// player_attributes[3] represents jersey_number
	// player_attributes[4] represents points_scored
	
	Player(String p_name, String p_age, String j_number, String p_scored){
		player_attributes.add(0, player_id);					// Player attributes = {Player ID, rest of attributes...}
		player_attributes.add(1, player_name);	
		player_attributes.add(2, player_age);
		player_attributes.add(3, jersey_number);
		player_attributes.add(4, points_scored);
	}

}
