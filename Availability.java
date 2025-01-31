import java.util.Vector;

public class Availability {
    // Static server list and prefix
    static Vector<Server> SERVER_LIST = new Vector<>();
    static Vector<String> CLIENTS = new Vector<>();
    static String SERVER_PREFIX = "D34dxb33F_";

    public static void main(String[] args) {
        populateClients(400);
        initializeServers(13);

        // Add clients to servers
        for (String client : CLIENTS) {
            addClientToServer(client, getLeastClients());
        }

        doResponses();

    }

    // Tiny method to smoothe the population of client and server list
    public static void populateClients(int numClients) {
        for (int i = 0; i < numClients; i++) {
            CLIENTS.add("Client_" + i);
        }
    }

    public static void initializeServers(int numServers) {
        for (int i = 0; i < numServers; i++) {
            addServer(SERVER_PREFIX + i);
        }
    }

    // Server class, handles server "Traffic"
    public static class Server {
        private final String name;
        private final Vector<String> serverClients = new Vector<>();

        // Basic functions for custom classes
        public Server(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        // Methods to handle clients
        public void addClient(String client) {
            serverClients.add(client);
        }

        public void removeClient(String client) {
            serverClients.remove(client);
        }

        public String[] getClients() {
            return serverClients.toArray(new String[0]);
        }

        private void doResponse() {
            java.util.Random random = new java.util.Random();
            for (String client : serverClients) {
                int randomData = random.nextInt(0xFFFFFF); // Generates a random 24-bit number
                String hexData = String.format("0x%06X", randomData); // Formats it as a zero-padded 6-digit hex
                System.out.println(name + " responding to " + client + " with " + hexData);
            }
        }

    }

    // Methods to add and remove servers
    public static void addServer(String serverName) {
        SERVER_LIST.add(new Server(serverName));
    }

    public static void killServer(String serverName) {
        for (int i = 0; i < SERVER_LIST.size(); i++) {
            if (SERVER_LIST.get(i).getName().equals(serverName)) {
                SERVER_LIST.remove(i);
                break;
            }
        }
    }

    // Simulate killing a random server and returning the clients
    // This is used to simulate a server going down, and the clients being returned
    // to the pool
    public static String[] nukeServer() {
        java.util.Random random = new java.util.Random();
        int randomIndex = random.nextInt(SERVER_LIST.size());
        Server serverToNuke = SERVER_LIST.get(randomIndex);
        String[] clients = serverToNuke.getClients();
        killServer(serverToNuke.getName());
        return clients;
    }

    // Simple print method
    public static void printServers() {
        for (Server server : SERVER_LIST) {
            System.out.println(server.getName());
        }
    }

    public static void doResponses() {
        for (Server server : SERVER_LIST) {
            server.doResponse();
        }
    }

    public static Server getLeastClients() {
        Server leastClients = SERVER_LIST.get(0);
        for (Server server : SERVER_LIST) {
            if (server.getClients().length < leastClients.getClients().length) {
                leastClients = server;
            }
        }
        return leastClients;
    }

    public static void addClientToServer(String client, Server targetServer) {
        targetServer.addClient(client);
    }

    public static void getClientCounts() {
        for (Server server : SERVER_LIST) {
            System.out.println(server.getName() + " has " + server.getClients().length + " clients.");
        }
    }
}