
public class Player {

	String player_name;
	String player_age;
	String jersey_number;
	String points_scored;

	String attributes[] = new String[4];
	// attributes[0] represents name
	// attributes[1] represents age
	// attributes[2] represents jersey_number
	// attributes[3] represents points_scored
	
	Player(String p_name, String p_age, String j_number, String p_scored){
		attributes[0] = player_name = p_name;
		attributes[1] = player_age = p_age;
		attributes[2] = jersey_number = j_number;
		attributes[3] = points_scored = p_scored;
	}

}
