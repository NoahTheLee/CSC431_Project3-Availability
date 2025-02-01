import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class Availabilitier {
    // Need to store a list of all servers
    static Vector<Server> SERVERS = new Vector<>();
    // Need to store the nubmer of servers that have been made
    static int SERVERS_MADE = 0;
    // "Need" to store server prefix
    static String SERVER_PREFIX = "D34dxb33F_";

    ////////////////////
    /// Server Rules ///
    /*
     * Servers keep track of the clients they are serving
     * Servers must be able to serve "data" to their clients
     * When or if a server goes down, it must push its clients to the pool
     */
    ////////////////////

    // Need to store the list of clients
    static Vector<String> UNASSIGNED_CLIENTS = new Vector<>();
    // Need to store the number of clients that have been made
    static int CLIENTS_MADE = 0;
    // "Need" to store client prefix
    static String CLIENT_PREFIX = "Client_";

    ////////////////////
        /// Client Rules ///
    /*
     * Clients only exist as targets, and do not handle any form of data
     * Clients are only assigned to one server at a time
     * If a client is added to a server, it must be removed from the pool
     */
    ////////////////////

    public static void main(String[] args) {
        Scanner lineReader = new Scanner(System.in);

        // Running loop must
        // Be able to add server(s)
        // Be able to add client(s)
        // Be able to nuke a server (pushing all clients to pool and removing server)
        // Be able to force a specific number of clients on a server to exhibit balacing
        // Be able to show the traffic sent to each client
        // Be able to view server status

        int command;
        boolean successfulCommand = false;
        while (true) {
            { // Command prompt and input
                if (successfulCommand) { // Periodic, generic status updates
                    getGlobalStatus();
                }
                System.out.print("""
                        ------------------------------------------------------------
                         Available Commands:
                         1  - Add Server
                         2  - Add Client(s)
                         3  - Nuke Random Server
                         4  - Balance Pending Clients
                         5  - Show Traffic
                         6  - View Server Status
                         7  - Add Specified Clients to Server
                         0  - Exit
                        ------------------------------------------------------------
                        Enter your choice: """);

                command = getInt(lineReader);
                successfulCommand = true;
            }

            switch (command) {
                case 1: // Add server to master list
                    addServer();
                    break;
                case 2: // Adding user-specified number of clients to pool
                    System.out.println("How many clients to add?");
                    int numClients = getInt(lineReader);
                    System.out.println("Adding " + numClients + " clients...");
                    for (int i = 0; i < numClients; i++) {
                        addClientToPool();
                    }
                    break;
                case 3: // Kill some random server, pushes clients back to pool
                    nukeServer();
                    break;
                case 4: // Balance unassigned clients across all servers
                    balanceClients();
                    break;
                case 5: // Show traffic from all servers to their clients
                    doResponses();
                    break;
                case 6: // Flavortext for server status
                    getStatuses();
                    break;
                case 7: // Force all pending clients onto server
                    addClientsToServer(lineReader);
                    break;
                // case 8: // TODO: Placeholder in case another function is needed
                // break;
                case 0: // Exiting the program
                    System.out.println("Exiting...");
                    lineReader.close();
                    return;
                default: // Generically handling bad input
                    System.out.println("Invalid command. Please try again.");
                    successfulCommand = false;
                    break;
            }

            System.out.println("\n\n\n"); // Spacing on the console
        }
    }

    // Integers are used repeatedly, method used to parse input for integers
    public static int getInt(Scanner lineReader) {
        int result = 0;
        boolean valid = false;
        while (!valid) {
            try {
                result = Integer.parseInt(lineReader.nextLine());
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("\n\n\nPlease enter a valid integer.");
            }
        }
        return result;
    }

    // Server class, handles server "Traffic" and stores clients
    public static class Server {
        private final String name;
        private final Vector<String> serverClients = new Vector<>();

        // Basic functions for custom classes
        public Server(String name) {
            this.name = name;
        }

        public int getNumClients() {
            return serverClients.size();
        }

        // Print the server status. Health and ping added for flavor
        // Is the status text or ping needed? No.
        // Do I like it? Yea :D
        private void getStatus() {
            java.util.Random random = new java.util.Random();
            int healthStatus = random.nextInt(3); // 0: Healthy, 1: Warning, 2: Danger
            String health;
            int ping;

            switch (healthStatus) {
                case 0:
                    health = "Healthy";
                    ping = random.nextInt(100) + 1; // 1-100 ms
                    break;
                case 1:
                    health = "Warning";
                    ping = random.nextInt(200) + 101; // 101-300 ms
                    break;
                case 2:
                default:
                    health = "Danger";
                    ping = random.nextInt(400) + 301; // 301-700 ms
                    break;
            }

            System.out.println("Server " + name + " has " + serverClients.size() + " clients. Server status: " + health
                    + ", with ping: " + ping + "ms.");
        }

        private void doResponse() {
            // Send some "data" to all clients. This is jut done for flavor
            java.util.Random random = new java.util.Random();
            for (String client : serverClients) {
                String hexData = String.format("0x%06X", random.nextInt(0xFFFFFF)); // 0padded 6digit hex num
                System.out.println(name + " responding to " + client + " with " + hexData);
            }
        }

        private void addClient(String client) {
            serverClients.add(client);
        }

        private String[] getClients() {
            return serverClients.toArray(new String[0]);
        }
    }

    // Runs responses on every server, targeting every client
    public static void doResponses() {
        for (Server server : SERVERS) {
            server.doResponse();
        }
    }

    // Get status of every server
    public static void getStatuses() {
        for (Server server : SERVERS) {
            server.getStatus();
        }
    }

    // Get a simple global status of every server
    public static void getGlobalStatus() {
        int totalClients = 0;
        for (Server server : SERVERS) {
            // Add the client count of every server to temporary count
            totalClients += server.getNumClients();
        }
        // Print the status
        System.out.println(SERVERS.size() + " servers, serving " + totalClients + " clients, with "
                + UNASSIGNED_CLIENTS.size() + " clients waiting in queue.");
    }

    // Add server to master list
    public static void addServer() {
        // Add a server to the list, and increment the server count
        SERVERS.add(new Server(SERVER_PREFIX + SERVERS_MADE));
        SERVERS_MADE++;
    }

    // Remove random server from list
    public static void nukeServer() {
        if (SERVERS.isEmpty()) { // Make sure there is a server to nuke
            System.out.println("No servers to nuke.");
        } else {
            int randomIndex = (int) (Math.random() * SERVERS.size()); // Pick some server
            Server targetServer = SERVERS.get(randomIndex); // Get the server's index
            System.out.println("Nuking " + targetServer.name + "...");

            // Push all of the server's clients back onto the queue
            String[] clients = targetServer.getClients();
            System.out.println("Pushing " + clients.length + " clients back to pool...");
            UNASSIGNED_CLIENTS.addAll(Arrays.asList(clients));

            // Kill the server
            SERVERS.remove(targetServer);
            System.out.println("Server killed");
        }
    }

    // Add a client to the pool of unassigned clients
    public static void addClientToPool() {
        // Add a client to the pool, and increment the client count
        UNASSIGNED_CLIENTS.add(CLIENT_PREFIX + CLIENTS_MADE);
        CLIENTS_MADE++;
    }

    // Empty the entire unassigned pool by balancing clients to available servers
    public static void balanceClients() {
        if (UNASSIGNED_CLIENTS.isEmpty()) { // Make sure there are clients to balance
            System.out.println("No clients to balance.");
            return;
        } else if (SERVERS.isEmpty()) { // Make sure there is a server to balance clients to
            System.out.println("No servers available to balance clients.");
            return;
        }
        // Empty out the unassigned client pool into the emptiest server
        while (!UNASSIGNED_CLIENTS.isEmpty()) {
            Server targetServer = getLeastClients();
            String client = UNASSIGNED_CLIENTS.get(0);
            addClientToServer(client, targetServer);
            UNASSIGNED_CLIENTS.remove(client);
        }
    }

    // Add a client to a server, used in two cases, so we set it up as a function
    public static void addClientToServer(String client, Server targetServer) {
        targetServer.addClient(client);
    }

    // Returns the server with the fewest clients
    /*
     * TODO
     * EXTRA CREDIT IMPLEMENTATION
     * Optional Enhancements (Extra Credit):
     * Implement a more advanced load-balancing strategy, such as least-
     * connections or weighted round-robin.
     * TODO
     */
    public static Server getLeastClients() {
        // Set the least clients to the first indexed server
        Server leastClients = SERVERS.get(0);
        for (Server server : SERVERS) {
            // For every server in the list, iterate over them
            if (server.getClients().length < leastClients.getClients().length) {
                // Check the size of the server's clients
                leastClients = server;
                // If it was smaller than the current least clients, set it as the new least
            }
        }
        return leastClients; // Return the server with the least clients
    }

    // Push all unassigned clients to a specific server
    public static void addClientsToServer(Scanner lineReader) {
        if (SERVERS.isEmpty()) { // Check if any servers are available
            System.out.println("No servers available.");
            return;
        }
        // Specify the available servers
        System.out.println("Currently available servers:");
        for (int i = 0; i < SERVERS.size(); i++) {
            System.out.println(i + ": " + SERVERS.get(i).name);
        }

        // Make sure server number is valid
        int serverNumber = -1;
        while (serverNumber < 0 || serverNumber >= SERVERS.size()) {
            System.out.println("Enter the server number to add clients to:");
            serverNumber = getInt(lineReader);
            if (serverNumber < 0 || serverNumber >= SERVERS.size()) {
                System.out.println("Invalid server number. Please try again.");
            }
        }

        // Get the target server and push all unassigned clients to it
        Server targetServer = SERVERS.get(serverNumber);
        while (!UNASSIGNED_CLIENTS.isEmpty()) {
            String client = UNASSIGNED_CLIENTS.remove(0);
            addClientToServer(client, targetServer);
        }

        System.out.println("All unassigned clients have been added to server " + targetServer.name);
    }

    // Terminate a number of clients from pending queue
    public static void terminateClients(Scanner lineReader) {
        if (UNASSIGNED_CLIENTS.isEmpty()) { // Check if any clients are available first
            System.out.println("No clients to terminate.");
            return;
        }
        // Prompt for number of clients, and clients to terminate
        System.out.println("There are " + UNASSIGNED_CLIENTS.size() + " pending in queue.");
        System.out.println("How many clients would you like to terminate?");

        int numClients = getInt(lineReader);
        if (numClients > UNASSIGNED_CLIENTS.size()) { // If input is greater than available, terminate all
            System.out.println("Terminating all clients.");
            UNASSIGNED_CLIENTS.clear();
        } else { // Otherwise, terminate the specified number
            for (int i = 0; i < numClients; i++) {
                UNASSIGNED_CLIENTS.remove(0);
            }
        }
    }
}
