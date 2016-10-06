import java.io.*;
import java.net.*;

public class Server{
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
                    sendMessage("Command received!");
                    if (message.equals("EXIT;")) {
                        sendMessage("Goodbye!");
                    }
                }
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
}