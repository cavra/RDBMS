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

    public static void main(String args[]) {
        Server server = new Server();
        //while(true) {
            server.run();
        //}
    }

    Server() {}

    void run() {
        try {
            // Create a server socket
            serverSocket = new ServerSocket(52312, 10);

            // Wait for connection
            System.out.print("Waiting for connection... ");
            connection = serverSocket.accept();
            System.out.println("Connected to " + connection.getInetAddress().getHostName() + "\n");

            // Get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful!");

            // Communicate with the client
            do {
                try {
                    message = (String)in.readObject();
                    // Check if the client wants to disconnect
                    if (!message.equals("EXIT;")) {
                        printMessage("Client", message);
                        parseMessage(message);
                    }
                }
                catch(ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                }
            } while (!message.toUpperCase().equals("EXIT;"));
        }
        catch(IOException ioException) {
            // ioException.printStackTrace();
            System.err.println("Input stream is empty!");
        }
        finally {
            // Close the connection
            try {
                printMessage("Server", "Closing connection");
                sendMessage("EXIT;");
                parseMessage("EXIT;");
                in.close();
                out.close();
                serverSocket.close();
                System.out.println("Client successfully disconnected");
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }

    void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
            printMessage("Server", message);
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    void parseMessage(String message) {
        System.out.println("Engine stream:");
        System.out.println("------------------------------------------------------------");
        // This string will be NULL unless SHOW is being executed
        // This where the SQL is sent to the RDBMS
        String show_results = Parser.readMessage(message); 
        System.out.println("------------------------------------------------------------\n");

        // So if it's not NULL, return the SHOW results
        if (message.indexOf("SHOW") >= 0) {
            if (show_results != null && !show_results.equals("")) {
                sendMessage(show_results);
            }
            // However, if a nonexistent table is given to SHOW, return this string
            else {
                sendMessage("Table doesn't exist");
            }
        }
        // If the message is calling for program exit, return EXIT;
        else if (message.toUpperCase().equals("EXIT")) { 
            sendMessage("EXIT;");
        }
        else {
            // do nothing, as we only want to send messages in response to the above actions
        }
    }

    void printMessage(String source, String message) {
        System.out.println(source + " stream:");
        System.out.println("------------------------------------------------------------");
        System.out.println(message);
        System.out.println("------------------------------------------------------------\n");
    }
}