
public class Team {

	String team_name;
	String location;
	String wins;
	String losses;
	String ties;
	String team_id = location + team_name;

	String team_attributes[] = new String[6];
	// team_attributes[0] represents team_id
	// team_attributes[0] represents team_name
	// team_attributes[1] represents location
	// team_attributes[2] represents wins
	// team_attributes[3] represents losses
	// team_attributes[4] represents ties

	Team(String t_name, String loc, String wins, String losses, String ties){
		team_attributes[0] = team_id;
		team_attributes[1] = team_name = t_name;
		team_attributes[2] = location = loc;
		team_attributes[3] = wins = wins;
		team_attributes[4] = losses = losses;
		team_attributes[5] = ties = ties;

	}


}
