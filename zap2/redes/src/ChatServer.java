import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatServer {
    private static final int PORT = 6789;
    private static final Set<ClientHandler> clients = new HashSet<>();
    private static final Object lock = new Object();

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
                synchronized (lock) {
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                    if (clients.size() > 1) {
                        requestClientAcceptance(clientHandler);
                        lock.wait();
                    } else {
                        clientHandler.accept();
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void requestClientAcceptance(ClientHandler newClient) {
        for (ClientHandler client : clients) {
            if (!client.equals(newClient)) {
                client.requestAcceptance(newClient);
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;
        private String username;
        private boolean joined;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                log("User connected");
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        break;
                    }
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
                log("User disconnected");
            }
        }

        private synchronized void handleJoin(String input) throws IOException {
            this.username = input.substring(5);
            joined = true;
            out.println("JOIN_ACCEPTED");
            broadcast(username + " has joined the chat.");
            log(username + " has joined the chat.");
        }

        private void handleMessage(String input) {
            if (joined) {
                String message = input.substring(4);
                broadcast(username + ": " + message);
                log(username + " sent message: " + message);
            }
        }

        private void handleExit() {
            if (joined) {
                broadcast(username + " has left the chat.");
                log(username + " has left the chat.");
            }
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients) {
                client.out.println(message);
            }
        }

        private void log(String message) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            System.out.println("[" + timestamp + "] " + message);
        }

        private synchronized void requestAcceptance(ClientHandler newClient) {
            out.println("JOIN_REQUEST");
            // Aqui não precisamos chamar um método "accept"
        }

        // Método para aceitar a entrada de um novo cliente
        private synchronized void accept() {
            out.println("JOIN_ACCEPTED");
        }
    }
}
