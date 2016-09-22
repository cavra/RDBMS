import java.util.*;

public class Sport {

	String sport_name;
	String num_players;
	String playing_surface;


	Vector<String> sport_attributes = new Vector<String>(3);
	// attributes[0] represents name
	// attributes[1] represents num_players
	// attributes[2] represents playing_surface

	Sport(String s_name, String n_players, String p_surface){
		sport_name = s_name;
		sport_attributes.add(0, sport_name);
		sport_attributes.add(1, n_players);
		sport_attributes.add(2, playing_surface);
	}
}
