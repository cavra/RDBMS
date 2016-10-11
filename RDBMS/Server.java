import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    ServerSocket serverSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    String choice;
    Scanner scanner = new Scanner(System.in);
    Boolean commanding = false;
    Writer writer = null;

    public static void main(String args[]) {
        Server server = new Server();
        while(true){
            server.run();
        }
    }

    Server() {}

    void run() {
        try {
            // Creating a server socket
            serverSocket = new ServerSocket(52312, 10);

            // Wait for connection
            System.out.println("Waiting for connection");
            connection = serverSocket.accept();
            System.out.println("Connection received from " + connection.getInetAddress().getHostName());

            // Get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful" + "\n" + listCommands());

            // The two parts communicate via the input and output streams
            do {
                try {
                    if (!commanding) {
                        message = (String)in.readObject();
                        System.out.println("Client> " + message);
                        switch(message) {
                            case "0":
                                sendMessage(listCommands());
                                break;
                            case "1":
                                commanding = true;
                                addPlayer();
                                commanding = false;
                                break;
                            case "2":
                                commanding = true;
                                addTeam();
                                commanding = false;
                                break;
                            case "3":
                                commanding = true;
                                removePlayer();
                                commanding = false;
                                break;
                            case "4":
                                commanding = true;
                                removeTeam();
                                commanding = false;
                                break;
                            case "5":
                                commanding = true;
                                tradePlayer();
                                commanding = false;
                                break;
                            case "6":
                                commanding = true;
                                updatePlayer();
                                commanding = false;
                                break;
                            case "7":
                                commanding = true;
                                updateTeam();
                                commanding = false;
                                break;
                            case "8":
                                commanding = true;
                            //    viewTeam();
                                commanding = false;
                                break;
                            case "9":
                                exitApplication();
                                break;
                            default: 
                                sendMessage("Invalid Command. Check list of Commands for valid input.");
                                break;
                        }
                    }
                } 
                catch(ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                }          
            } while (!message.equals("QUIT;"));
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
        finally {
            exitApplication();
        }
    }

    String listenToSocket() {
        try {
            // The two parts communicate via the input and output streams
            do {
                try {
                    message = (String)in.readObject();
                    System.out.println("Client> " + message);
                    return message;
                }
                catch(ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                }            
            } while (!message.equals("QUIT;")); 
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }

    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            System.out.println("Server> " + msg);
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    String listCommands(){
        String listedCommandsString = 
        "HERE IS A LIST OF COMMANDS YOU MAY USE:" +
        "\n1.) Add Player - Allows the user to add player to a team." +
        "\n2.) Add Team - Allows the user to add Team to a Sport." +
        "\n3.) Remove Player - Allows the user to remove a player from a team" +
        "\n4.) Remove Team - Allows user to remove a team from a sport " +
        "\n5.) Trade Player - Allows the user swap players from 2 teams. " +
        "\n6.) Update Player - Allows user to change Player's attribute(s). " +
        "\n7.) Update Team - Allows user to change Team's attribute(s). " +
        "\n8.) View Team - Allows the user to look at roster for specified team. " +
        "\n9.) EXIT APP";

        return listedCommandsString;
    }

    void addPlayer() {
       sendMessage("Enter Name of player: ");
       String player_name = listenToSocket();
       sendMessage("Enter Age of player: ");
       String player_age = listenToSocket();
       sendMessage("Enter Team of player: ");
       String player_team = listenToSocket();
       sendMessage("Enter Jersey Number of player: ");
       String jersey_number = listenToSocket();
       sendMessage("Enter Position of player: ");
       String player_position = listenToSocket();
       sendMessage("Enter Number of Point Scored by player: ");
       String player_points = listenToSocket();

       String player_insert =
        "INSERT INTO players VALUES FROM " + "(\"" + player_name + "\"" +
        ", " + player_age + ", " + jersey_number + ", " + "\"" + player_position +
        "\"" + ", " + player_points + ")"; 

        String player_insert_team = 
            "INSERT INTO " + player_team + " VALUES FROM " + "(\"" + player_name + "\"" +
        ", " + player_age + ", " + jersey_number + ", " + "\"" + player_position +
        "\"" + ", " + player_points + ")";

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("input.txt"), "utf-8"));
            writer.write(player_insert  + '\n' + player_insert_team);
        } 
        catch (IOException ex) {} 
        finally {
            try {writer.close();}
            catch (Exception ex) {}
        }

        Parser.readInputFile();

        sendMessage(player_insert);
    }

    void addTeam() {
       sendMessage("Enter Sport the team plays: ");
       String team_sport = listenToSocket();
       sendMessage("Enter Name of team: ");
       String team_name = listenToSocket();
       sendMessage("Enter Location of team: ");
       String team_location = listenToSocket();

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

        sendMessage(team_insert);
    }

    void removePlayer() {
        sendMessage("Enter Name of the player to delete: ");
        String player_name = listenToSocket();
        sendMessage("Enter Jersey number of the player to delete: ");
        String player_jersey = listenToSocket();
        sendMessage("Enter Team of the player to delete: ");
        String player_team = listenToSocket();

        String player_remove = "DELETE FROM players WHERE name=\"" + player_name +
                               "\"&&jersey_number=\"" + player_jersey + "\"";

        String team_remove = "DELETE FROM " + player_team + " WHERE name=\"" + player_name +
                               "\"&&jersey_number=\"" + player_jersey + "\"";

         try{
            writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream("input.txt"), "utf-8"));
            writer.write(player_remove + "\n" + team_remove);
        } catch (IOException ex) {} 
        finally {
            try {writer.close();}
            catch (Exception ex) {}
        }

        sendMessage(player_remove);
        sendMessage(team_remove);
    }

    void removeTeam() {
        sendMessage("Enter Name of the team to delete: ");
        String team_name = listenToSocket();
        sendMessage("Enter Sport that the team plays: ");
        String team_sport = listenToSocket();

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
        sendMessage(team_delete);
        sendMessage(sport_delete);
    }

    void tradePlayer() {
        sendMessage("Enter first player's TEAM: ");
        String team1 = listenToSocket();
        sendMessage("Enter first player's NAME: ");
        String player1 = listenToSocket();
        sendMessage("Enter first player's JERSEY NUMBER: ");
        String jersey1 = listenToSocket();
        sendMessage("Enter second player's TEAM: ");
        String team2 = listenToSocket();
        sendMessage("Enter second player's NAME: ");
        String player2 = listenToSocket();
        sendMessage("Enter second player's JERSEY NUMBER: ");
        String jersey2 = listenToSocket();

        String trade1 = "INSERT INTO " + team2 + " VALUES FROM RELATION select (name=\"" +
                        player1 + "\"&&jersey_number=\"" + jersey1 + "\") " + team1;

        String trade2 = "INSERT INTO " + team1 + " VALUES FROM RELATION select (name=\"" + 
                        player2 + "\"&&jersey_number=\"" + jersey2 + "\") " + team2;

        String delete1 = "DELETE FROM " + team1 + " WHERE name=\"" + player1 + "\"&&jersey_number=\"" +
                         jersey1 + "\"";

        String delete2 = "DELETE FROM " + team2 + " WHERE name=\"" + player2 + "\"&&jersey_number=\"" +
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

        sendMessage(trade1);
        sendMessage(trade2);
        sendMessage(delete1);
        sendMessage(delete2);
    }

    void updatePlayer() {
        Vector<String> attr_list = new Vector<String>();

        sendMessage("Enter NAME of the player to be updated: ");
        String player_name = listenToSocket();
        sendMessage("Enter JERSEY NUMBER of the player to be updated: ");
        String player_jersey = listenToSocket();
        sendMessage("Enter TEAM NAME of the player to be updated: ");
        String player_team = listenToSocket();

        sendMessage("Update PLAYER NAME (Type \"NO\" to continue without updating): ");
        String update_name = listenToSocket();
        if(update_name.toLowerCase() != "no"){
            String temp = "name=\"" + update_name + "\"";
            attr_list.add(temp);
        }

        sendMessage("Update JERSEY NUMBER (Type \"NO\" to continue without updating: ");
        String update_jersey = listenToSocket();
        if(update_jersey != "NO" && update_jersey != "no" && update_jersey != "No"){
            String temp = "jersey_number=\"" + update_jersey + "\"";
            attr_list.add(temp);
        }

        sendMessage("Update AGE (Type \"NO\" to continue without updating: ");
        String update_age = listenToSocket();
        if(update_age != "NO" && update_age != "No" && update_age != "no"){
            String temp = "age=\"" + update_age + "\"";
            attr_list.add(temp);
        }

        sendMessage("Update POINTS SCORED (Type \"NO\" to continue without updating: ");
        String update_points = listenToSocket();
        if(update_points != "NO" && update_points != "No" && update_points != "no"){
            String temp = "points_scored=\"" + update_points + "\"";
            attr_list.add(temp);
        }

        sendMessage("Update POSITION (Type \"NO\" to continue without updating: ");
        String update_position = listenToSocket();
        if(update_position.toLowerCase() != "no"){
            String temp = "position=\"" + update_position + "\"";
            attr_list.add(temp);
        }


        String update_players = "UPDATE players SET ";
        String update_team = "UPDATE " + player_team + " SET ";
        for(int i = 0; i < attr_list.size()-1; i++){
            update_players += attr_list.get(i) + ", ";
            update_team += attr_list.get(i) + ", ";
        }
        update_players += attr_list.get(attr_list.size()-1);
        update_players += " WHERE name=\"" + player_name + "\"&&jersey_number=\"" +
                          player_jersey + "\"";

        update_team += attr_list.get(attr_list.size()-1);
        update_team += " WHERE name=\"" + player_name + "\"&&jersey_number=\"" +
                          player_jersey + "\"";

        sendMessage(update_players);
        sendMessage(update_team);

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

        sendMessage("Enter the NAME of the team to be updated: ");
        String team_name = listenToSocket();
        sendMessage("Enter the CITY LOCATION of the team to be updated: ");
        String team_location = listenToSocket();
        sendMessage("Enter the SPORT played by the team: ");
        String team_sport = listenToSocket();

        sendMessage("Update TEAM LOCATION (Type \"NO\" to continue without updating: ");
        String update_location = listenToSocket();
        if(update_location.toLowerCase() != "no"){
            String temp = "team_location=\"" + update_location +"\"";
            attr_list.add(temp);
        }

        sendMessage("Update TEAM NAME (Type \"NO\" to continue without updating: ");
        String update_name = listenToSocket();
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

        sendMessage(update_team);
        sendMessage(update_sport);

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

    void exitApplication() {
        // Close the connection
        try {
            System.out.println("Server connection closed");
            in.close();
            out.close();
            serverSocket.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}