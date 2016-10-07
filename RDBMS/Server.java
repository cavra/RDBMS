import java.io.*;
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

    public static void main(String args[]) {
        Server server = new Server();
        //while(true) {
        server.run();
        //}
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
            sendMessage("Connection successful");

            // The two parts communicate via the input and output streams
            do {
                try {
			    	message = (String)in.readObject();
			        System.out.println("Client> " + message);

			        switch(message){
			            case "HELP":
			                sendMessage(listCommands());
			                break;
			            case "NEW":
			                sendMessage(newCommand());
			                break;
			            case "ADD":
			            case "TRADE":
			            case "CHANGE":
			            case "RENAME":
			            case "REMOVE":
			            case "PRINT":
			            case "SAVE":
			            case "DELETE":
			            case "QUERY":
			            default: 
			                sendMessage("Invalid Command. Input 'HELP' for list of commands.");
			                break;
			        }
			        if (message.equals("EXIT;")) {
			            sendMessage("Goodbye!");
			        }                }
                catch(ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                }
            } while (!message.equals("EXIT;")); 
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
        finally {
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

    void listenToSocket() {

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
    	String listedCommandsString = "HERE IS A LIST OF COMMANDS YOU MAY USE:" +
        "\nNEW - Allows the user to create a new entry set for:" +
        "\tPLAYER, TEAM, or GAME" +
        "\nADD - Allows the user to insert a new entry for:" +
        "\tPLAYER, TEAM, or GAME" +
        "\nREMOVE - Allows the user to delete entries for:" +
        "\tPLAYER, TEAM, or a set of the both" +
        "\nTRADE - Allows the user to move players to different teams" +
        "\nCHANGE - Allows user to make changes to:" +
        "\tPLAYER or TEAM" +
        "\nPRINT - Allows the user to see all information for:" +
        "\tRELATION, PLAYER, TEAM, or SPORT" +
        "\nSAVE - Allows the user to save all data." +
        "\nDELETE - Allows the user to permanently remove data." +
        "\nQUERY - Allows the user to get certain data from the database.";

        return listedCommandsString;
    }

    String newCommand(){
        System.out.println("Would you like to create a Sport, Team, or Player?");
        System.out.println("Testing");
        
    
       //         String team = scanner.nextLine();
       //         sendMessage("Enter Player Age: ");
       //         int age = scanner.nextInt();
       //         sendMessage("Enter Player Jersey Number: ");
       //         int jerseyNumber = scanner.nextInt();
       //         sendMessage("Enter Player Position: ");
       //         String position = scanner.nextLine();
       //           
        //}
        return "ugh";

    }

    void addCommand(){

    }
    void tradeCommand(){

    }
    void changeCommand(){

    }
}