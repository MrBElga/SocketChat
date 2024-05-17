import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 6789;
    private static Set<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) throws UnknownHostException {
        System.out.println("Chat server started...");
        InetAddress inetAddress = InetAddress.getLocalHost();
        String hostname = inetAddress.getHostName();
        String ipAddress = inetAddress.getHostAddress();
        int port = 6789;
        System.out.println("Rodando no hostname: " + hostname);
        System.out.println("Rodando no IP: " + ipAddress);
        System.out.println("Rodando na porta: " + port);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private boolean joined;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                while (true) {
                    String input = in.readLine();
                    if (input.startsWith("JOIN")) {
                        handleJoin(input);
                    } else if (input.startsWith("MSG")) {
                        handleMessage(input);
                    } else if (input.equals("EXIT")) {
                        handleExit();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
            }
        }

        private synchronized void handleJoin(String input) throws IOException {
            this.username = input.substring(5);
            if (clients.size() == 1) {
                joined = true;
                out.println("JOIN_ACCEPTED");
                broadcast(username + " has joined the chat.");
            } else {
                out.println("Join request from " + username + ". Accept? (yes/no)");
                String response = in.readLine();
                if (response.equalsIgnoreCase("yes")) {
                    joined = true;
                    out.println("JOIN_ACCEPTED");
                    broadcast(username + " has joined the chat.");
                } else {
                    out.println("JOIN_DENIED");
                    socket.close();
                }
            }
        }

        private void handleMessage(String input) {
            if (joined) {
                String message = input.substring(4);
                broadcast(username + ": " + message);
            }
        }

        private void handleExit() {
            if (joined) {
                broadcast(username + " has left the chat.");
            }
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }
    }
}
