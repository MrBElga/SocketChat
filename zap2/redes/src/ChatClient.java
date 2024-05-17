import java.io.*;
import java.net.*;
import java.util.*;

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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        username = scanner.nextLine();
        out.println("JOIN " + username);

        String response = in.readLine();
        if (response.equals("JOIN_ACCEPTED")) {
            System.out.println("You have joined the chat.");
        } else {
            System.out.println("Join request denied: " + response);
            socket.close();
            return;
        }

        Thread listener = new Thread(new Listener());
        listener.start();

        while (true) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("sair")) {
                out.println("EXIT");
                break;
            }
            out.println("MSG " + message);
        }

        listener.interrupt();
        socket.close();
    }

    private class Listener implements Runnable {
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
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
