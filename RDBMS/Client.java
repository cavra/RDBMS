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

            // Communicate with the server
            do {
                try {
                    message = (String)in.readObject();
                    // Check if the server has disconnected already
                    if (!message.equals("EXIT;")) {
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
            String age = getUserInput("How old is " + name + "? ");
            String team = getUserInput("What team does " + name + " play for? ");
            String jersey = getUserInput("What is " + name + "\'s Jersey Number? ");
            String position = getUserInput("What is " + name + "\'s position? ");
            String points = getUserInput("How many points has " + name + " scored? ");

            String player_insert =
            "INSERT INTO players VALUES FROM " + "(\"" + name + "\"" +
            ", " + age + ", " + jersey + ", " + "\"" + position +
            "\"" + ", " + points + ")"; 

            String player_insert_team = 
            "INSERT INTO " + team + " VALUES FROM " + "(\"" + name + "\"" +
            ", " + age + ", " + jersey + ", " + "\"" + position +
            "\"" + ", " + points + ")";

            return player_insert;
        }
        return "";
    }

    void addTeam() {
       System.out.println("Enter Sport the team plays: ");
       String team_sport = scanner.nextLine();
       System.out.println("Enter Name of team: ");
       String team_name = scanner.nextLine();
       System.out.println("Enter Location of team: ");
       String team_location = scanner.nextLine();

       String team_insert =
        "INSERT INTO teams VALUES FROM " + "(\"" + team_name + "\"" +
        ", " +  "\"" + team_location + ")"; 
       String team_insert_sport = 
        "INSERT INTO " + team_sport + " VALUES FROM " + "(\"" + team_sport + "\"" + ")"; 

        try{
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("input.txt"), "utf-8"));
            writer.write(team_insert + '\n' + team_insert_sport);
        } catch (IOException ex) {} 
        finally {
            try {writer.close();}
            catch (Exception ex) {}
        }

        System.out.println(team_insert);
    }

    void removePlayer() {
        System.out.println("Enter Name of the player to delete: ");
        String name = scanner.nextLine();
        System.out.println("Enter Jersey number of the player to delete: ");
        String player_jersey = scanner.nextLine();
        System.out.println("Enter Team of the player to delete: ");
        String team = scanner.nextLine();

        String player_remove = "DELETE FROM players WHERE name=\"" + name +
                               "\"&&jersey=\"" + player_jersey + "\"";

        String team_remove = "DELETE FROM " + team + " WHERE name=\"" + name +
                               "\"&&jersey=\"" + player_jersey + "\"";

         try{
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("input.txt"), "utf-8"));
            writer.write(player_remove + "\n" + team_remove);
        } catch (IOException ex) {} 
        finally {
            try {writer.close();}
            catch (Exception ex) {}
        }

        System.out.println(player_remove);
        System.out.println(team_remove);
    }

    void removeTeam() {
        System.out.println("Enter Name of the team to delete: ");
        String team_name = scanner.nextLine();
        System.out.println("Enter Sport that the team plays: ");
        String team_sport = scanner.nextLine();

        String team_delete = "DELETE FROM teams WHERE name=\"" + team_name +
                             "\"";

        String sport_delete = "DELETE FROM " + team_sport + " WHERE name=\"" + 
                              team_name + "\"";

        try{
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("input.txt"), "utf-8"));
            writer.write(team_delete + "\n" + sport_delete);
        } catch (IOException ex) {} 
        finally {
            try {writer.close();}
            catch (Exception ex) {}
        }
        System.out.println(team_delete);
        System.out.println(sport_delete);
    }

    void tradePlayer() {
        System.out.println("Enter first player's TEAM: ");
        String team1 = scanner.nextLine();
        System.out.println("Enter first player's NAME: ");
        String player1 = scanner.nextLine();
        System.out.println("Enter first player's JERSEY NUMBER: ");
        String jersey1 = scanner.nextLine();
        System.out.println("Enter second player's TEAM: ");
        String team2 = scanner.nextLine();
        System.out.println("Enter second player's NAME: ");
        String player2 = scanner.nextLine();
        System.out.println("Enter second player's JERSEY NUMBER: ");
        String jersey2 = scanner.nextLine();

        String trade1 = "INSERT INTO " + team2 + " VALUES FROM RELATION select (name=\"" +
                        player1 + "\"&&jersey=\"" + jersey1 + "\") " + team1;

        String trade2 = "INSERT INTO " + team1 + " VALUES FROM RELATION select (name=\"" + 
                        player2 + "\"&&jersey=\"" + jersey2 + "\") " + team2;

        String delete1 = "DELETE FROM " + team1 + " WHERE name=\"" + player1 + "\"&&jersey=\"" +
                         jersey1 + "\"";

        String delete2 = "DELETE FROM " + team2 + " WHERE name=\"" + player2 + "\"&&jersey=\"" +
                         jersey2 + "\"";

        try{
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("input.txt"), "utf-8"));
            writer.write(trade1 + "\n" + trade2 + "\n" + delete1 + "\n" + delete2);
        } catch (IOException ex) {} 
        finally {
            try {writer.close();}
            catch (Exception ex) {}
        }

        System.out.println(trade1);
        System.out.println(trade2);
        System.out.println(delete1);
        System.out.println(delete2);
    }

    void updatePlayer() {
        Vector<String> attr_list = new Vector<String>();

        System.out.println("Enter NAME of the player to be updated: ");
        String name = scanner.nextLine();
        System.out.println("Enter JERSEY NUMBER of the player to be updated: ");
        String player_jersey = scanner.nextLine();
        System.out.println("Enter TEAM NAME of the player to be updated: ");
        String team = scanner.nextLine();

        System.out.println("Update PLAYER NAME (Type \"NO\" to continue without updating): ");
        String update_name = scanner.nextLine();
        if(update_name.toLowerCase() != "no"){
            String temp = "name=\"" + update_name + "\"";
            attr_list.add(temp);
        }

        System.out.println("Update JERSEY NUMBER (Type \"NO\" to continue without updating: ");
        String update_jersey = scanner.nextLine();
        if(update_jersey != "NO" && update_jersey != "no" && update_jersey != "No"){
            String temp = "jersey=\"" + update_jersey + "\"";
            attr_list.add(temp);
        }

        System.out.println("Update AGE (Type \"NO\" to continue without updating: ");
        String update_age = scanner.nextLine();
        if(update_age != "NO" && update_age != "No" && update_age != "no"){
            String temp = "age=\"" + update_age + "\"";
            attr_list.add(temp);
        }

        System.out.println("Update POINTS SCORED (Type \"NO\" to continue without updating: ");
        String update_points = scanner.nextLine();
        if(update_points != "NO" && update_points != "No" && update_points != "no"){
            String temp = "points_scored=\"" + update_points + "\"";
            attr_list.add(temp);
        }

        System.out.println("Update POSITION (Type \"NO\" to continue without updating: ");
        String update_position = scanner.nextLine();
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
        update_players += " WHERE name=\"" + name + "\"&&jersey=\"" +
                          player_jersey + "\"";

        update_team += attr_list.get(attr_list.size()-1);
        update_team += " WHERE name=\"" + name + "\"&&jersey=\"" +
                          player_jersey + "\"";

        System.out.println(update_players);
        System.out.println(update_team);

        try{
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("input.txt"), "utf-8"));
            writer.write(update_players + "\n" + update_team);
        } catch (IOException ex) {} 
        finally {
            try {writer.close();}
            catch (Exception ex) {}
        }
    }

    void updateTeam(){
        Vector<String> attr_list = new Vector<String>();

        System.out.println("Enter the NAME of the team to be updated: ");
        String team_name = scanner.nextLine();
        System.out.println("Enter the CITY LOCATION of the team to be updated: ");
        String team_location = scanner.nextLine();
        System.out.println("Enter the SPORT played by the team: ");
        String team_sport = scanner.nextLine();

        System.out.println("Update TEAM LOCATION (Type \"NO\" to continue without updating: ");
        String update_location = scanner.nextLine();
        if(update_location.toLowerCase() != "no"){
            String temp = "team_location=\"" + update_location +"\"";
            attr_list.add(temp);
        }

        System.out.println("Update TEAM NAME (Type \"NO\" to continue without updating: ");
        String update_name = scanner.nextLine();
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
                          team_location + "\"";

        update_sport += attr_list.get(attr_list.size()-1);
        update_sport += " WHERE name=\"" + team_name + "\"&&team_location=\"" +
                          team_location + "\"";

        System.out.println(update_team);
        System.out.println(update_sport);

        try{
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("input.txt"), "utf-8"));
            writer.write(update_team + "\n" + update_sport);
        } catch (IOException ex) {} 
        finally {
            try {writer.close();}
            catch (Exception ex) {}
        }
    }

}