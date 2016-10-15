import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Scanner;

public class Client{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Scanner scanner = new Scanner(System.in);
    Writer writer = null;
    Boolean shouldCancel = false;

    public static void main(String args[]) {
        Client client = new Client();
        client.run();
    }

    Client() {}

    void run() {
        try {
            // Create a socket to connect to the server
            requestSocket = new Socket("localhost", 52312);
            System.out.println("Connected to localhost through port 52312");

            // Get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Welcome the user only once, out here
            welcomeUser();

            //Create global tables that store all sports, teams, and playesr created"
            String global_teams = "CREATE TABLE teams (name VARCHAR(20), location VARCHAR(20), venue VARCHAR(20), wins INTEGER, " +
            "losses INTEGER, ties INTEGER) PRIMARY KEY (location, name);";
            sendMessage(global_teams);

            String global_players = "CREATE TABLE players (name VARCHAR(20), age INTEGER, jersey_number INTEGER, points_scored INTEGER) " +
            "PRIMARY KEY (name, jersey_number);";
            sendMessage(global_players);

            String global_sports = "CREATE TABLE sports (name VARCHAR(20), playing_surface VARCHAR(20), " +
            "country_created VARCHAR(20)) PRIMARY KEY (name, playing_surface);";
            sendMessage(global_sports);

            // Communicate with the server
            do {
                try {
                    message = (String)in.readObject();
                    // Check if the server has disconnected already
                    if (!message.toUpperCase().equals("EXIT;")) {
                        System.out.println("Server> " + message);
                        message = generateCommandFromInput();
                        sendMessage(message);
                    }
                }
                catch(ClassNotFoundException classNot) {
                    System.err.println("Data received in unknown format");
                }
            } while (!message.toUpperCase().equals("EXIT;"));
        }
        catch(UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
        finally {
            disconnect();
        }
    }

    void disconnect() {
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

    void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void welcomeUser() {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("Welcome to the official Aggie Sports Management Client!");
        System.out.println("Type 'help' for a detailed list of commands!");
        System.out.println("-------------------------------------------------------\n");
    }

    void showHelp() {
        System.out.println("\n-------------------------------------------------------");
        System.out.println("Here is a list of commands you can use:");
        System.out.println("- Add Player - Add a new player to a team");
        System.out.println("- Add Team -  Add Team to a Sport");
        System.out.println("- Add Sport - Add Sport to list of available options");
        System.out.println("- Remove Player -  Remove a player from a team");
        System.out.println("- Remove Team - Remove a team from a sport");
        System.out.println("- Trade Player -  Swap players from 2 teams");
        System.out.println("- Update Player - Update a Player's information");
        System.out.println("- Update Team - Update a Team's information");
        System.out.println("- View Player -  Examine a specified player's information");
        System.out.println("- View Team -  Examine the roster for specified team");
        System.out.println("- Quit - Exits the program");
        System.out.println("-------------------------------------------------------\n");
    }

    String generateCommandFromInput() {
        Boolean finished = false;
        String command = "";

        while (!finished) {
            System.out.print("User$ ");
            String input = scanner.nextLine();

            switch (input.toUpperCase()) {
                case "HELP":
                    showHelp();
                    break;
                case "ADD PLAYER":
                    command = addPlayer();
                    finished = true;
                    break;
                case "ADD TEAM":
                    command = addTeam();
                    finished = true;
                    break;
                case "ADD SPORT":
                    command = addSport();
                    finished = true;
                    break;
                case "REMOVE PLAYER":
                    command = removePlayer();
                    finished = true;
                    break;
                case "REMOVE TEAM":
                    command = removeTeam();
                    finished = true;
                    break;
                case "TRADE PLAYER":
                    command = tradePlayer();
                    finished = true;
                    break;
                case "UPDATE PLAYER":
                    command = updatePlayer();
                    finished = true;
                    break;
                case "UPDATE TEAM":
                    command = updateTeam();
                    finished = true;
                    break;
                case "VIEW PLAYER":
                    // command = viewPlayer()     
                    // finished = true;
                    break;
                case "VIEW TEAM":
                    command = viewTeam();
                    finished = true;
                    break;
                case "QUIT":
                    command = "EXIT;";
                    finished = true;
                    break;
                default:
                    System.out.println("Client> Invalid input");
                    break;
            }
        }

        return command;
    }

    String getUserInput(String prompt) {
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
        return input;
    }

    String addPlayer() {
        while (!shouldCancel) {
            String name = getUserInput("What is the new player's name? ");

            if (name.toUpperCase().equals("CANCEL")) {
                break;
            }

            String age = getUserInput("How old is " + name + "? ");
            String team = getUserInput("What team does " + name + " play for? ");
            String jersey = getUserInput("What is " + name + "\'s Jersey Number? ");
            String position = getUserInput("What is " + name + "\'s position? ");
            String points = getUserInput("How many points has " + name + " scored? ");

            String player_insert =
            "INSERT INTO players VALUES FROM " + "(\"" + name + "\"" +
            ", " + age + ", " + jersey + ", " + "\"" + position +
            "\"" + ", " + points + ");"; 

            String player_insert_team = 
            "INSERT INTO " + team + " VALUES FROM " + "(\"" + name + "\"" +
            ", " + age + ", " + jersey + ", " + "\"" + position +
            "\"" + ", " + points + ");";

            return player_insert + "\n" + player_insert_team;
        }
        return "";
    }

    String addTeam() {
        while(!shouldCancel){
            String team_sport = getUserInput("Enter Sport the team plays: ");

            if (team_sport.toUpperCase().equals("CANCEL")) {
                break;
            }

            String team_name = getUserInput("Enter Name of team: ");
            String team_location = getUserInput("Enter Location of team: ");
            String venue = getUserInput("Enter the name of the " + team_name + "'s venue (ex. Kyle Field): ");
            String total_wins = getUserInput("Enter " + team_name + "'s total wins: ");
            String total_losses = getUserInput("Enter " + team_name + "'s total losses: ");
            String total_ties = getUserInput("Enter " + team_name + "'s total ties: ");

            // Each team table consists of a list of players on said team
            String team_table = "CREATE TABLE " + team_name + "(name VARCHAR(20), age INTEGER, jersey_number INTEGER, points_scored INTEGER) " +
            "PRIMARY KEY (name, jersey_number);"; 
           

            String team_insert =
<<<<<<< HEAD
            "INSERT INTO teams VALUES FROM " + "(\"" + team_name + "\", \"" + 
            team_location + ");";

            String team_insert_sport = 
            "INSERT INTO " + team_sport + " VALUES FROM " + "(\"" + 
            team_sport + "\"" + ");"; 
=======
            "INSERT INTO teams VALUES FROM (\"" + team_name + "\", \"" + 
            team_location + "\", \"" + venue + "\", \"" + total_wins + "\", \"" +
            total_losses + "\", \"" + total_ties + "\", \"" + ");";

            String team_insert_sport = 
            "INSERT INTO " + team_sport + " VALUES FROM (\"" + team_name + "\", \"" + 
            team_location + "\", \"" + venue + "\", \"" + total_wins + "\", \"" +
            total_losses + "\", \"" + total_ties + "\", \");";

            return team_table + "\n" + team_insert + "\n" + team_insert_sport;
        }
        return "";
    }

    String addSport() {
        while(!shouldCancel){
            String name = getUserInput("Enter the name of the sport to create: ");
            String playing_surface = getUserInput("Enter " + name + "'s playing surface (Gym, Field, etc.): ");
            String country = getUserInput("Enter the country where " + name + " was created: ");

            // Each sport table consist of a list of teams playing said sport
            String sport_table = "CREATE TABLE " + name + "(name VARCHAR(20), location VARCHAR(20), venue VARCHAR(20), wins INTEGER, " +
            "losses INTEGER, ties INTEGER) PRIMARY KEY (location, name);";
>>>>>>> 085a92eb693bc1063947689e3accaf450d15dbcd

            String insert_sport = "INSERT INTO sports VALUES FROM (\"" + name + "\", \"" +
            playing_surface + "\", \"" + country + "\");";

            return sport_table + "\n" + insert_sport;
        }
        return "";
    }

    String removePlayer() {
        while(!shouldCancel){
            String name = getUserInput("Enter Name of the player to delete: ");

            if (name.toUpperCase().equals("CANCEL")) {
                break;
            }

            String player_jersey = getUserInput("Enter " + name + "'s jersey number: ");
            String team = getUserInput("Enter the name of " + name + "'s team: ");

            String player_remove = "DELETE FROM players WHERE name=\"" + name +
            "\"&&jersey=\"" + player_jersey + "\";";

            String team_remove = "DELETE FROM " + team + " WHERE name=\"" + name +
            "\"&&jersey=\"" + player_jersey + "\";";

            return player_remove + "\n" + team_remove;
        }
        return "";
    }

    String removeTeam() {
        while(!shouldCancel){
            String team_name = getUserInput("Enter Name of the team to delete: ");

            if (team_name.toUpperCase().equals("CANCEL")) {
                break;
            }

            String team_sport = getUserInput("Enter Sport that the team plays: ");

            String team_delete = "DELETE FROM teams WHERE name=\"" + 
            team_name + "\";";

            String sport_delete = "DELETE FROM " + team_sport + " WHERE name=\"" + 
            team_name + "\";";

            String drop_team = "DROP TABLE " + team_name + ";";
        
            return team_delete + "\n" + sport_delete;
        }  
        return ""; 
    }

    String tradePlayer() {
        while(!shouldCancel){
            String player1 = getUserInput("Enter first player's name: ");

            if (player1.toUpperCase().equals("CANCEL")) {
                break;
            }

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

            return trade1 + "\n" + trade2 + "\n" + delete1 + "\n" + delete2;
        }
        return "";
    }

    String updatePlayer() {
        while(!shouldCancel){
            Vector<String> attr_list = new Vector<String>();

            String name = getUserInput("Enter name of the player to be updated: ");

            if (name.toUpperCase().equals("CANCEL")) {
                break;
            }

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
            update_players += " WHERE name=\"" + name + "\"&&jersey=\"" + player_jersey + "\";";

            update_team += attr_list.get(attr_list.size()-1);
            update_team += " WHERE name=\"" + name + "\"&&jersey=\"" + player_jersey + "\";";

            return update_players + '\n' + update_team;
        }
        return "";
    }

    String updateTeam(){
        while(!shouldCancel){
            Vector<String> attr_list = new Vector<String>();

            String team_name = getUserInput("Enter the name of the team of be updated: ");

            if (team_name.toUpperCase().equals("CANCEL")) {
                break;
            }

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
            update_team += " WHERE team_name=\"" + team_name + "\"&&team_location=\"" +
                              team_location + "\";";

            update_sport += attr_list.get(attr_list.size()-1);
            update_sport += " WHERE name=\"" + team_name + "\"&&team_location=\"" +
                              team_location + "\";";

            return update_team + "\n" + update_sport;
        }
        return "";
    }
    
    String viewTeam() {
        while (!shouldCancel){
            String team = getUserInput("Enter the team name: ");

            if (team.toUpperCase().equals("CANCEL")) {
                break;
            }
            
            String view_command = "SHOW " + team + ";";
            return view_command;
        }
        return "";
    }
}



