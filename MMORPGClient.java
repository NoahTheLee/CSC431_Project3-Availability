import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MMORPGClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println(in.readLine()); // Prompt for name
            String playerName = consoleInput.readLine();
            out.println(playerName);

            new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        if (serverMessage.startsWith("UPDATE:")) {
                            String[] parts = serverMessage.split(":");
                            String otherPlayerName = parts[1];
                            String positionData = parts[2];
                            System.out.println(otherPlayerName + " is now at " + positionData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            String userInput;
            while ((userInput = consoleInput.readLine()) != null) {
                if (userInput.startsWith("MOVE:")) {
                    out.println(userInput); // Movement data to server
                }
            }
        } catch (

        IOException e) {
            e.printStackTrace();
        }

    }
}