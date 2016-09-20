
public class Team {

	String team_name;
	String location;
	Int wins;
	Int losses;
	int ties;

	String attributes[] = new String[5];
	// attributes[0] represents team_name
	// attributes[1] represents location
	// attributes[2] represents wins
	// attributes[3] represents losses
	// attributes[4] represents ties

	Sport(String t_name, String loc, String wins, String losses, String ties){
		team_name = t_name;
		location = loc;
		wins = wins;
		losses = losses;
		ties = ties;

	}


}
