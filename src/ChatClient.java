import java.io.*;
import java.net.*;

public class ChatClient {
    private String username;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatClient(String serverAddress, int serverPort) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void start() throws IOException {
        try {
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your username: ");
            username = userInput.readLine();
            out.println("JOIN " + username);

            String response;
            while ((response = in.readLine()) != null) {
                if (response.equals("JOIN_ACCEPTED")) {
                    System.out.println("You have joined the chat.");
                    break;
                } else if (response.startsWith("JOIN_REQUEST")) {
                    handleJoinRequest(response, userInput);
                } else if (response.equals("JOIN_DENIED")) {
                    System.out.println("You were denied access to the chat.");
                    socket.close();
                    return;
                } else {
                    System.out.println("Unexpected response from server: " + response);
                }
            }

            Thread listener = new Thread(new Listener());
            listener.start();

            while (true) {
                String message = userInput.readLine();
                if (message.equalsIgnoreCase("exit")) {
                    out.println("EXIT");
                    break;
                }
                out.println("MSG " + message);
            }

            listener.interrupt();
        } finally {
            socket.close();
        }
    }

    private void handleJoinRequest(String response, BufferedReader userInput) throws IOException {
        String newClientUsername = response.substring(13);
        System.out.println(newClientUsername + " wants to join the chat. Do you accept? (yes/no)");
        String acceptResponse = userInput.readLine();
        if (acceptResponse.equalsIgnoreCase("yes")) {
            out.println("ACCEPT " + newClientUsername);
        } else {
            out.println("DENY " + newClientUsername);
        }
    }

    private class Listener implements Runnable {
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient("localhost", 6789);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
