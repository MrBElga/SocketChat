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
        System.out.println("Running on hostname: " + hostname);
        System.out.println("Running on IP: " + ipAddress);
        System.out.println("Running on port: " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void requestClientAcceptance(ClientHandler newClient) throws IOException {
        synchronized (lock) {
            for (ClientHandler client : clients) {
                if (!client.equals(newClient)) {
                    client.out.println("JOIN_REQUEST " + newClient.username);
                }
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
                    } else if (input.startsWith("ACCEPT")) {
                        handleAccept(input);
                    } else if (input.startsWith("DENY")) {
                        handleDeny(input);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    clients.remove(this);
                    if (joined) {
                        broadcast(username + " has left the chat.");
                    }
                }
                log("User disconnected");
            }
        }

        private synchronized void handleJoin(String input) throws IOException, InterruptedException {
            this.username = input.substring(5);
            synchronized (lock) {
                if (clients.isEmpty()) {
                    accept();
                } else {
                    clients.add(this);  // Temporarily add the client to handle join requests
                    requestClientAcceptance(this);
                    lock.wait();
                }
            }
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

        private void handleAccept(String input) {
            String newClientUsername = input.substring(7);
            for (ClientHandler client : clients) {
                if (client.username.equals(newClientUsername)) {
                    client.accept();
                    break;
                }
            }
        }

        private void handleDeny(String input) throws IOException {
            String newClientUsername = input.substring(5);
            for (ClientHandler client : clients) {
                if (client.username.equals(newClientUsername)) {
                    client.out.println("JOIN_DENIED");
                    client.socket.close();
                    synchronized (lock) {
                        clients.remove(client);
                        lock.notifyAll();
                    }
                    break;
                }
            }
        }

        private void broadcast(String message) {
            synchronized (lock) {
                for (ClientHandler client : clients) {
                    client.out.println(message);
                }
            }
        }

        private void log(String message) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            System.out.println("[" + timestamp + "] " + message);
        }

        private void requestClientAcceptance(ClientHandler newClient) throws IOException {
            synchronized (lock) {
                for (ClientHandler client : clients) {
                    if (!client.equals(newClient)) {
                        client.out.println("JOIN_REQUEST " + newClient.username);
                    }
                }
            }
        }

        private synchronized void accept() {
            joined = true;
            out.println("JOIN_ACCEPTED");
            broadcast(username + " has joined the chat.");
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }
}
