import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    Scanner scanner = new Scanner(System.in);
   
    public static void main(String args[]) {
        Client client = new Client();
        client.run();
    }

    Client(){}

    void run() {
        try {
            // Create a socket to connect to the server
            requestSocket = new Socket("localhost", 52312);
            System.out.println("Connected to localhost through port 52312");

            // Get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Communicating with the server
            do {
                try {
                    message = (String)in.readObject();
                    System.out.println("Server> " + message);
                    System.out.print("Client> ");
                    message = scanner.nextLine();
                    sendMessage(message);
                }
                catch(ClassNotFoundException classNot) {
                    System.err.println("Data received in unknown format");
                }
            } while (!message.toUpperCase().equals("EXIT"));
        }
        catch(UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
        finally {
            // Close the connection
            try {
                System.out.print("Disconnecting from server...");
                in.close();
                out.close();
                requestSocket.close();
                System.out.println("Connection closed.");
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }
    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }
}