import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Scanner;

public class Server{
    ServerSocket serverSocket;
    Socket connection = null;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    String choice;
    Scanner scanner = new Scanner(System.in);
    Boolean commanding = false;

    public static void main(String args[]) {
        Server server = new Server();
        while(true){
            server.run();
        }
    }

    Server(){}

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
                        switch(Integer.parseInt(message)) {
                            case 1:
                                commanding = true;
                                addPlayer();
                                break;
                            case 2:
                                commanding = true;
                                //newCommand();
                                //commanding = false;
                                break;
                            case 3:
                                commanding = true;
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                commanding = true;
                            case 9:
                            case 10:
                            case 11:
                                exitApplication();
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
        "\n2.) Add Team- Allows the user to add Team to a Sport." +
        "\n3.) Add Sport - Allows the user to add a Sport to list of Sports." +
        "\n4.) Remove Player - Allows the user to remove a player from a team" +
        "\n5.) Remove Team - Allows user to remove a team from a sport " +
        "\n6.) Remove Sport - Allows the user to remove a sport from the database. " +
        "\n7.) Trade Player - Allows the user swap players from 2 teams. " +
        "\n8.) Update Player - Allows user to change Player's attribute(s). " +
        "\n9.) Update Team - Allows user to change Team's attribute(s). " +
        "\n10.) View Team - Allows the user to look at roster for specified team. " +
        "\n11.) EXIT APP";

        return listedCommandsString;
    }

    void addPlayer() {
       sendMessage("Enter Name of player: ");
       String playerName = listenToSocket();
       sendMessage("Enter Age of player: ");
       String playerAge = listenToSocket();
       sendMessage("Enter Jersey Number of player: ");
       String jerseyNumber = listenToSocket();
       sendMessage("Enter Position of player: ");
       String playerPosition = listenToSocket();
       sendMessage("Enter Number of Point Scored by player: ");
       String playerPoints = listenToSocket();

       String playerInsert =
        "INSERT INTO players VALUES FROM " + "(\"" + playerName + "\"" +
        ", " + playerAge + ", " + jerseyNumber + ", " + "\"" + playerPosition +
        "\"" + ", " + playerPoints + ")"; 

        sendMessage(playerInsert);


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