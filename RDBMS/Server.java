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
            sendMessage("Connection successful");

            // The two parts communicate via the input and output streams
            do {
				try {
					if (!commanding) {
						message = (String)in.readObject();
						System.out.println("Client> " + message);

						switch(message.toUpperCase()) {
						    case "HELP":
						        sendMessage(listCommands());
						        break;
						    case "NEW":
						        commanding = true;
						        newCommand();
						        //commanding = false;
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
						    case "EXIT":
						    	exitApplication();
						    default: 
						        sendMessage("Invalid Command. Input 'HELP' for list of commands.");
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
        "\nNEW - Allows the user to create a new entry set for: PLAYER, TEAM, or GAME" +
        "\nADD - Allows the user to insert a new entry for: PLAYER, TEAM, or GAME" +
        "\nREMOVE - Allows the user to delete entries for: PLAYER, TEAM, or a set of the both" +
        "\nTRADE - Allows the user to move players to different teams" +
        "\nCHANGE - Allows user to make changes to: PLAYER or TEAM" +
        "\nPRINT - Allows the user to see all information for: RELATION, PLAYER, TEAM, or SPORT" +
        "\nSAVE - Allows the user to save all data." +
        "\nDELETE - Allows the user to permanently remove data." +
        "\nQUERY - Allows the user to get certain data from the database.";

        return listedCommandsString;
    }

    void newCommand() {
       sendMessage("Would you like to create a Sport, Team, or Player?");
       
       String team = listenToSocket();
       System.out.println("1st input: " + team);

       sendMessage("Enter Player Age: ");
       String age = listenToSocket();
       System.out.println("2nd input: " + age);

       sendMessage("Enter Player Jersey Number: ");
       String jerseyNumber = listenToSocket();
       System.out.println("3rd input: " + jerseyNumber);

       sendMessage("Enter Player Position: ");
       String position = listenToSocket();
       System.out.println("4th input: " + position);

       commanding = false;
    }

    void addCommand(){

    }
    void tradeCommand(){

    }
    void changeCommand(){

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