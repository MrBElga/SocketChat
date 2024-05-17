import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String argv[]) throws Exception
    {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String hostname = inetAddress.getHostName();
        String ipAddress = inetAddress.getHostAddress();
        int port = 6789;

        // Imprimindo o hostname, endereço IP e porta
        System.out.println("Rodando no hostname: " + hostname);
        System.out.println("Rodando no IP: " + ipAddress);
        System.out.println("Rodando na porta: " + port);


        String clientSentence;
        String capitalizedSentence;

        ServerSocket welcomeSocket = new ServerSocket(6789);

        while(true) {

            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            DataOutputStream  outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            clientSentence = inFromClient.readLine();

            capitalizedSentence = clientSentence.toUpperCase() + '\n';

            outToClient.writeBytes(capitalizedSentence);

            System.out.println(capitalizedSentence);
       }
    }
}



