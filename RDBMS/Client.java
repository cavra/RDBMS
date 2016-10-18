import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    // Private variables
    private static Socket requestSocket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static String message;
    private static Scanner scanner = new Scanner(System.in);
    private static Boolean shouldCancel = false;
    private static Boolean checkStream = true;

// =============================================================================
// The Main Method call for the Client class. This just starts the Client, which 
//   attempts to connect to the Server
// Parameters:
//   args: Needed Parameter for main method call, not used
// =============================================================================

    public static void main(String args[]) {
        Client.run();
    }

// =============================================================================
// A function to set up the Client and continually check for input from the 
//   Server. This is the core of the Client-Server connection
// =============================================================================

    private static void run() {
        try {
            // Create a socket to connect to the server
            requestSocket = new Socket("localhost", 52312);
            System.out.println("Connected to localhost through port 52312");

            // Get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Welcome the user only once
            welcomeUser();

            // Open all default and saved tables
            resume();

            // Show the help menu up front
            showHelp();

            // Constantly check the input stream
            while (checkStream) {
                readMessage();
            }
        }
        catch(UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException) {
            // ioException.printStackTrace();
            System.err.println("Input stream is empty!");
            checkStream = false;
        }
        finally {
            disconnect();
        }
    }

// =============================================================================
// =============================================================================

    private static void readMessage() {
        try {
            message = (String)in.readObject();
            // Check if the server has disconnected already
            if (!message.toUpperCase().equals("EXIT;")) {
                System.out.println("------------------------------------------------------------");
                System.out.println(message);
                System.out.println("------------------------------------------------------------\n");
                promptUser();
            }
            else {
                checkStream = false;
            }
        }
        catch(ClassNotFoundException classNot) {
            System.err.println("Data received in unknown format");
        }
        catch(IOException ioException) {
            // ioException.printStackTrace();
            System.err.println("Input stream is empty!");
            checkStream = false;
        }
    }

// =============================================================================
// =============================================================================

    private static void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

// =============================================================================
// =============================================================================

    private static void disconnect() {
        // Close the connection
        try {
            System.out.print("Disconnecting from server... ");
            in.close();
            out.close();
            requestSocket.close();
            System.out.println("Connection closed.");
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

// =============================================================================
// =============================================================================

    private static void welcomeUser() {
        System.out.println("\n------------------------------------------------------------");
        System.out.println("Welcome to the official Aggie Sports Management Client!");
        System.out.println("Type 'help' for a detailed list of commands!");
        System.out.println("------------------------------------------------------------");
    }

// =============================================================================
// =============================================================================

    private static void resume() {
        // Create global tables that store all sports, teams, and players
        // Note: These only CREATE if they don't already exist as .ser files
        String global_teams = "CREATE TABLE teams (name VARCHAR(15), wins INTEGER, " + 
        "losses INTEGER, ties INTEGER) PRIMARY KEY (name, wins);";
        sendMessage(global_teams);

        String global_players = "CREATE TABLE players (name VARCHAR(15), age INTEGER, " + 
        "jersey INTEGER, position VARCHAR(15)) PRIMARY KEY (name, jersey);";
        sendMessage(global_players);

        String global_sports = "CREATE TABLE sports (name VARCHAR(15), playing_surface VARCHAR(20), " + 
        "country_created VARCHAR(12)) PRIMARY KEY (name, playing_surface);";
        sendMessage(global_sports);

        // Open all saved tables
        String open_all_relations = "OPEN OPEN_ALL_RELATIONS;";
        sendMessage(open_all_relations);
    }

// =============================================================================
// =============================================================================

    private static void showHelp() {
        System.out.println("\n------------------------------------------------------------");
        System.out.println("PROGRAM COMMANDS:");
        System.out.println("- Help - Show this helpful text menu");
        System.out.println("- Quit - Exits the program");
        System.out.println();
        System.out.println("ADD COMMANDS:");
        System.out.println("- Add Player - Add a new player to a team");
        System.out.println("- Add Team - Add Team to a Sport");
        System.out.println("- Add Sport - Add Sport to list of available options");
        System.out.println();
        System.out.println("REMOVE COMMANDS:");
        System.out.println("- Remove Player - Remove a player from a team");
        System.out.println("- Remove Team - Remove a team from a sport");
        System.out.println();
        System.out.println("UPDATE COMMANDS:");
        System.out.println("- Update Player - Update a Player's information");
        System.out.println("- Update Team - Update a Team's information");
        System.out.println();
        System.out.println("VIEW COMMANDS:");
        System.out.println("- View Player - Examine a specified player's information");
        System.out.println("- View Team - Examine the roster for specified team");
        System.out.println("- View All Players - Examine all of the players in the system");
        System.out.println("- View All Teams - Examine all of the teams in the system");
        System.out.println("- View All Sports - Examine all of the sports in the system");
        System.out.println();
        System.out.println("APPLICATION COMMANDS:");
        System.out.println("- Trade Player - Swap players from 2 teams");
        System.out.println("------------------------------------------------------------\n");
    }

// =============================================================================
// =============================================================================

    private static void promptUser() {
        Boolean checkInputStream = false;

        while (!checkInputStream) {
            System.out.print("User> ");
            String input = scanner.nextLine();

            switch (input.toUpperCase()) {
                case "HELP":
                    showHelp();
                    break;
                case "ADD PLAYER":
                    addPlayer();
                    break;
                case "ADD TEAM":
                    addTeam();
                    break;
                case "ADD SPORT":
                    addSport();
                    break;
                case "REMOVE PLAYER":
                    removePlayer();
                    break;
                case "REMOVE TEAM":
                    removeTeam();
                    break;
                case "TRADE PLAYER":
                    tradePlayer();
                    break;
                case "UPDATE PLAYER":
                    updatePlayer();
                    break;
                case "UPDATE TEAM":
                    updateTeam();
                    break;
                case "VIEW PLAYER":
                    viewPlayer();
                    checkInputStream = true;
                    break;
                case "VIEW TEAM":
                    viewTeam();
                    checkInputStream = true;
                    break;
                case "VIEW ALL PLAYERS":
                    viewAllPlayers();
                    checkInputStream = true;
                    break;
                case "VIEW ALL TEAMS":
                    viewAllTeams();
                    checkInputStream = true;
                    break;
                case "VIEW ALL SPORTS":
                    viewAllSports();
                    checkInputStream = true;
                    break;
                case "QUIT":
                    sendMessage("EXIT;");
                    checkInputStream = true;
                    checkStream = false;
                    break;
                default:
                    System.out.println("Client> Invalid input (type 'Help' to see a list of valid input)");
                    break;
            }
        }
    }

// =============================================================================
// =============================================================================

    private static String getUserInput(String prompt) {
        Boolean finished = false;
        String input = "";

        while (!finished) {
            System.out.print(prompt);
            input = scanner.nextLine();

            switch (input.toUpperCase()) {
                case "HELP":
                    showHelp();
                    break;
                case "QUIT":
                    sendMessage("EXIT;");
                    disconnect();
                    System.exit(0);
                case "CANCEL":
                    shouldCancel = true;
                    finished = true;
                    break;
                default:
                    finished = true;
                    break;
            }
        }

        input = input.trim();
        input = input.replaceAll(" ", "_");
        return input;
    }

// =============================================================================
// =============================================================================

    private static void addPlayer() {
        // Get the required information from the user
        String name = getUserInput("What is the new player's name? ");
        String age = getUserInput("How old is " + name + "? ");
        String team = getUserInput("What team does " + name + " play for? ");
        String jersey = getUserInput("What is " + name + "\'s Jersey Number? ");
        String position = getUserInput("What is " + name + "\'s position? ");
        String points = getUserInput("How many points has " + name + " scored? ");

        // Insert the player information into the players table
        String player_insert =
        "INSERT INTO players VALUES FROM (\"" + name + "\", " + 
        age + ", " + jersey + ", \"" + position +
        "\", " + points + ");"; 

        // Insert the players information into the player's team table
        String player_insert_team = 
        "INSERT INTO " + team + " VALUES FROM (\"" + name + "\", " + 
        age + ", " + jersey + ", \"" + position +
        "\", " + points + ");";

        sendMessage(player_insert);
        sendMessage(player_insert_team);
    }

// =============================================================================
// =============================================================================

    private static void addTeam() {
        // Get the required information from the user
        String team_sport = getUserInput("Enter Sport the team plays: ");
        String team_name = getUserInput("Enter Name of team: ");
        String team_location = getUserInput("Enter Location of team: ");
        String venue = getUserInput("Enter the name of " + team_name + "'s venue (ex. Kyle Field): ");
        String total_wins = getUserInput("Enter " + team_name + "'s total wins: ");
        String total_losses = getUserInput("Enter " + team_name + "'s total losses: ");
        String total_ties = getUserInput("Enter " + team_name + "'s total ties: ");

        // Create a table for the new team's players, if this is the first case of the team
        String team_table = "CREATE TABLE " + team_name + "(name VARCHAR(20), age INTEGER, jersey_number INTEGER, position VARCHAR(20), " +
        "points_scored INTEGER) PRIMARY KEY (name, jersey_number);"; 
       
       // Insert the team information into the teams table
        String team_insert =
        "INSERT INTO teams VALUES FROM (\"" + team_name + "\", \"" + 
        team_location + "\", \"" + venue + "\", " + total_wins + ", " +
        total_losses + ", " + total_ties + ");";

        // Insert the team information into the sports table
        String team_insert_sport = 
        "INSERT INTO " + team_sport + " VALUES FROM (\"" + team_name + "\", \"" + 
        team_location + "\", \"" + venue + "\", " + total_wins + ", " +
        total_losses + ", " + total_ties + ");";

        sendMessage(team_table);
        sendMessage(team_insert);
        sendMessage(team_insert_sport);
    }

// =============================================================================
// =============================================================================

    private static void addSport() {
        String name = getUserInput("Enter the name of the sport to create: ");
        String playing_surface = getUserInput("Enter " + name + "'s playing surface (Gym, Field, etc.): ");
        String country = getUserInput("Enter the country where " + name + " was created: ");

        // Each sport table consist of a list of teams playing said sport
        String sport_table = "CREATE TABLE " + name + "(name VARCHAR(20), location VARCHAR(20), venue VARCHAR(20), wins INTEGER, " +
        "losses INTEGER, ties INTEGER) PRIMARY KEY (location, name);";

        String insert_sport = "INSERT INTO sports VALUES FROM (\"" + name + "\", \"" +
        playing_surface + "\", \"" + country + "\");";

        sendMessage(sport_table);
        sendMessage(insert_sport);
    }

// =============================================================================
// =============================================================================

    private static void removePlayer() {
        String name = getUserInput("Enter Name of the player to delete: ");

        String player_jersey = getUserInput("Enter " + name + "'s jersey number: ");
        String team = getUserInput("Enter the name of " + name + "'s team: ");

        String player_remove = "DELETE FROM players WHERE name ==\"" + name +
        "\" && jersey_number == \"" + player_jersey + "\";";

        String team_remove = "DELETE FROM " + team + " WHERE name ==\"" + name +
        "\" && jersey_number ==\"" + player_jersey + "\";";

        sendMessage(player_remove);
        sendMessage(team_remove);
    }

// =============================================================================
// =============================================================================

    private static void removeTeam() {
        String team_name = getUserInput("Enter Name of the team to delete: ");

        String team_sport = getUserInput("Enter Sport that the team plays: ");

        String team_delete = "DELETE FROM teams WHERE name ==\"" + 
        team_name + "\";";

        String sport_delete = "DELETE FROM " + team_sport + " WHERE name ==\"" + 
        team_name + "\";";

        String drop_team = "DROP TABLE " + team_name + ";";
    
        sendMessage(team_delete);
        sendMessage(sport_delete);
    }

// =============================================================================
// =============================================================================

    private static void tradePlayer() {
        String player1 = getUserInput("Enter first player's name: ");

        String team1 = getUserInput("Enter " + player1 + "'s team name: ");
        String jersey1 = getUserInput("Enter " + player1 + "'s jersey number: ");
        String player2 = getUserInput("Enter second player's name: ");
        String team2 = getUserInput("Enter " + player2 + "'s team name: ");
        String jersey2 = getUserInput("Enter " + player2 + "'s jersey number: ");

        String trade1 = "INSERT INTO " + team2 + " VALUES FROM RELATION select (name=\"" +
        player1 + "\"&&jersey=\"" + jersey1 + "\") " + team1 + ";";

        String trade2 = "INSERT INTO " + team1 + " VALUES FROM RELATION select (name=\"" + 
        player2 + "\"&&jersey=\"" + jersey2 + "\") " + team2 + ";";

        String delete1 = "DELETE FROM " + team1 + " WHERE name=\"" + player1 + "\"&&jersey=\"" +
        jersey1 + "\";";

        String delete2 = "DELETE FROM " + team2 + " WHERE name=\"" + player2 + "\"&&jersey=\"" +
        jersey2 + "\";";

        sendMessage(trade1);
        sendMessage(trade2);
        sendMessage(delete1);
        sendMessage(delete2);
    }

// =============================================================================
// =============================================================================

    private static void updatePlayer() {
        Vector<String> attr_list = new Vector<String>();

        String name = getUserInput("Enter name of the player to be updated: ");

        String player_jersey = getUserInput("Enter " + name + "'s jersey number: ");
        String team = getUserInput("Enter " + name + "'s team name: ");

        String update_name = getUserInput("Update PLAYER NAME (Type \"NO\" to continue without updating): ");
        if(update_name.toLowerCase() != "no"){
            String temp = "name=\"" + update_name + "\"";
            attr_list.add(temp);
        }

        String update_jersey = getUserInput("Update JERSEY NUMBER (Type \"NO\" to continue without updating: ");
        if(update_jersey != "NO" && update_jersey != "no" && update_jersey != "No"){
            String temp = "jersey=\"" + update_jersey + "\"";
            attr_list.add(temp);
        }

        String update_age = getUserInput("Update AGE (Type \"NO\" to continue without updating: ");
        if(update_age != "NO" && update_age != "No" && update_age != "no"){
            String temp = "age=\"" + update_age + "\"";
            attr_list.add(temp);
        }

        String update_points = getUserInput("Update POINTS SCORED (Type \"NO\" to continue without updating: ");
        if(update_points != "NO" && update_points != "No" && update_points != "no"){
            String temp = "points_scored=\"" + update_points + "\"";
            attr_list.add(temp);
        }

        String update_position = getUserInput("Update POSITION (Type \"NO\" to continue without updating: ");
        if(update_position.toLowerCase() != "no"){
            String temp = "position=\"" + update_position + "\"";
            attr_list.add(temp);
        }

        String update_players = "UPDATE players SET ";
        String update_team = "UPDATE " + team + " SET ";
        for(int i = 0; i < attr_list.size()-1; i++){
            update_players += attr_list.get(i) + ", ";
            update_team += attr_list.get(i) + ", ";
        }
        update_players += attr_list.get(attr_list.size()-1);
        update_players += " WHERE name ==\"" + name + "\"&& jersey ==\"" + player_jersey + "\";";

        update_team += attr_list.get(attr_list.size()-1);
        update_team += " WHERE name ==\"" + name + "\"&& jersey ==\"" + player_jersey + "\";";

        sendMessage(update_players);
        sendMessage(update_team);
    }

// =============================================================================
// =============================================================================

    private static void updateTeam() {
        Vector<String> attr_list = new Vector<String>();

        String team_name = getUserInput("Enter the name of the team of be updated: ");
        String team_location = getUserInput("Enter the city where the " + team_name + "'s play: ");
        String team_sport = getUserInput("Enter the sport that the " + team_name + "'s play: ");

        String update_location = getUserInput("Update TEAM LOCATION (Type \"NO\" to continue without updating: ");
        if(update_location.toLowerCase() != "no"){
            String temp = "team_location=\"" + update_location +"\"";
            attr_list.add(temp);
        }

        String update_name = getUserInput("Update TEAM NAME (Type \"NO\" to continue without updating: ");
        if(update_location.toLowerCase() != "no"){
            String temp = "team_name=\"" + update_location + "\"";
            attr_list.add(temp);
        }

        String update_team = "UPDATE teams SET ";
        String update_sport = "UPDATE " + team_sport + " SET ";
        for(int i = 0; i < attr_list.size()-1; i++){
            update_team += attr_list.get(i) + ", ";
            update_sport += attr_list.get(i) + ", ";
        }
        update_team += attr_list.get(attr_list.size()-1);
        update_team += " WHERE team_name ==\"" + team_name + "\"&&team_location==\"" +
                          team_location + "\";";

        update_sport += attr_list.get(attr_list.size()-1);
        update_sport += " WHERE name==\"" + team_name + "\"&&team_location==\"" +
                          team_location + "\";";

        sendMessage(update_team);
        sendMessage(update_sport);
    }

// =============================================================================
// =============================================================================

    // Set Union
    private static void mergeTeams() {
        String name1 = getUserInput("What is the name of the first team you want to merge? ");
        String name2 = getUserInput("What is the name of the second team you want to merge? ");

        String join_teams = name1 + " + " + name2;
        sendMessage(join_teams);
    }

// =============================================================================
// =============================================================================

    // Set Difference
    private static void something1() {

    }

// =============================================================================
// =============================================================================

    // Natural Join
    private static void something2() {

    }

// =============================================================================
// =============================================================================

    // Select
    private static void viewPlayer() {
        String name = getUserInput("What is the name of the player you want to query? ");
        String jersey_number = getUserInput("What is " + name + "'s jersey number? ");
        
        String view_player = "SHOW select (name == \"" + name + "\" && jersey == " + jersey_number + ") players;";
        sendMessage(view_player);
    }

// =============================================================================
// =============================================================================

    // Project
    private static void viewTeamRoster() {
        String name = getUserInput("What is the name of the team whose roster you want to see? ");

        String view_roster = "SHOW project (name) " + name + ";";
        sendMessage(view_roster);
    }

// =============================================================================
// =============================================================================

    private static void viewTeam() {
        String team = getUserInput("Enter the team name: ");
        
        String view_team = "SHOW " + team + ";";
        sendMessage(view_team);
    }

// =============================================================================
// =============================================================================

    private static void viewAllPlayers() {
        String view_players = "SHOW players;";
        sendMessage(view_players);
    }

// =============================================================================
// =============================================================================

    private static void viewAllTeams() {
        String view_teams = "SHOW teams;";
        sendMessage(view_teams);
    }

// =============================================================================
// =============================================================================

    private static void viewAllSports() {
        String view_sports = "SHOW sports;";
        sendMessage(view_sports);
    }

}