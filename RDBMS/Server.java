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
        while(true){
            server.run();
        }
    }

    Server() {}

    void run() {
        try {
            // Create a server socket
            serverSocket = new ServerSocket(52312, 10);

            // Wait for connection
            System.out.print("Waiting for connection... ");
            connection = serverSocket.accept();
            System.out.println("Connected to " + connection.getInetAddress().getHostName());

            // Get Input and Output streams
            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());
            sendMessage("Connection successful!");

            // Communicate with the client
            do {
                try {
                    message = (String)in.readObject();
                    // Check if the client has disconnected already
                    if (!message.equals("EXIT;")) {
                        System.out.println("Client> " + message);
                        message = getRequestedData(message);
                        sendMessage(message);
                    }
                }
                catch(ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                }     
            } while (!message.toUpperCase().equals("EXIT;"));
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
        finally {
            // Close the connection
            try {
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
            System.out.println("Server> " + message);
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    String getRequestedData(String message) {
        switch (message.toUpperCase()) {
            case "EXIT": 
                return "EXIT;";
            default: 
                return "Input received";
        }
    }
}