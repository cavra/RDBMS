
public class Sport {

	String sport_name;
	String num_players;
	String playing_surface;

	String sport_attributes[] = new String[3];
	// attributes[0] represents name
	// attributes[1] represents num_players
	// attributes[2] represents playing_surface

	Sport(String s_name, String n_players, String p_surface){
		sport_attributes[0] = sport_name = s_name;
		sport_attributes[1] = n_players = num_players;
		sport_attributes[2] = playing_surface = p_surface;
	}
}
