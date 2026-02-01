import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates Load Balancing - A fundamental system design concept
 * Load balancing distributes incoming requests across multiple servers
 * to optimize resource usage, maximize throughput, and minimize response time
 */
public class LoadBalancer {

    // Round Robin Load Balancer
    static class RoundRobinLoadBalancer {
        private List<Server> servers;
        private AtomicInteger currentIndex;

        public RoundRobinLoadBalancer(List<Server> servers) {
            this.servers = new ArrayList<>(servers);
            this.currentIndex = new AtomicInteger(0);
        }

        public Server getNextServer() {
            if (servers.isEmpty()) {
                throw new IllegalStateException("No servers available");
            }

            int index = currentIndex.getAndIncrement() % servers.size();
            return servers.get(index);
        }
    }

    // Least Connections Load Balancer
    static class LeastConnectionsLoadBalancer {
        private List<Server> servers;

        public LeastConnectionsLoadBalancer(List<Server> servers) {
            this.servers = new ArrayList<>(servers);
        }

        public Server getNextServer() {
            if (servers.isEmpty()) {
                throw new IllegalStateException("No servers available");
            }

            Server leastConnectedServer = servers.get(0);
            for (Server server : servers) {
                if (server.getActiveConnections() < leastConnectedServer.getActiveConnections()) {
                    leastConnectedServer = server;
                }
            }
            return leastConnectedServer;
        }
    }

    // Weighted Round Robin Load Balancer
    static class WeightedRoundRobinLoadBalancer {
        private List<Server> weightedServerList;
        private AtomicInteger currentIndex;

        public WeightedRoundRobinLoadBalancer(List<Server> servers) {
            this.weightedServerList = new ArrayList<>();
            this.currentIndex = new AtomicInteger(0);

            // Create weighted list based on server weights
            for (Server server : servers) {
                for (int i = 0; i < server.getWeight(); i++) {
                    weightedServerList.add(server);
                }
            }
        }

        public Server getNextServer() {
            if (weightedServerList.isEmpty()) {
                throw new IllegalStateException("No servers available");
            }

            int index = currentIndex.getAndIncrement() % weightedServerList.size();
            return weightedServerList.get(index);
        }
    }

    // Server class representing backend servers
    static class Server {
        private String name;
        private String ipAddress;
        private int port;
        private int activeConnections;
        private int weight; // For weighted load balancing
        private boolean healthy;

        public Server(String name, String ipAddress, int port, int weight) {
            this.name = name;
            this.ipAddress = ipAddress;
            this.port = port;
            this.activeConnections = 0;
            this.weight = weight;
            this.healthy = true;
        }

        public void handleRequest(String request) {
            activeConnections++;
            System.out.println(name + " (" + ipAddress + ":" + port + ") handling: " + request);
            // Simulate request processing
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            activeConnections--;
        }

        public int getActiveConnections() {
            return activeConnections;
        }

        public int getWeight() {
            return weight;
        }

        public String getName() {
            return name;
        }

        public boolean isHealthy() {
            return healthy;
        }

        public void setHealthy(boolean healthy) {
            this.healthy = healthy;
        }

        @Override
        public String toString() {
            return name + " (" + ipAddress + ":" + port + ") - Connections: " + activeConnections + " - Healthy: "
                    + healthy;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== LOAD BALANCING DEMONSTRATION ===\n");

        // Create servers
        List<Server> servers = Arrays.asList(
                new Server("Server-1", "192.168.1.1", 8080, 1),
                new Server("Server-2", "192.168.1.2", 8080, 2),
                new Server("Server-3", "192.168.1.3", 8080, 3));

        // 1. Round Robin Load Balancing
        System.out.println("1. ROUND ROBIN LOAD BALANCING:");
        System.out.println("   Each server gets requests in sequential order\n");
        RoundRobinLoadBalancer rrLB = new RoundRobinLoadBalancer(servers);

        for (int i = 1; i <= 6; i++) {
            Server server = rrLB.getNextServer();
            server.handleRequest("Request-" + i);
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 2. Least Connections Load Balancing
        System.out.println("2. LEAST CONNECTIONS LOAD BALANCING:");
        System.out.println("   Requests go to server with fewest active connections\n");
        LeastConnectionsLoadBalancer lcLB = new LeastConnectionsLoadBalancer(servers);

        // Simulate different connection counts
        servers.get(0).activeConnections = 5;
        servers.get(1).activeConnections = 2;
        servers.get(2).activeConnections = 8;

        for (int i = 1; i <= 3; i++) {
            Server server = lcLB.getNextServer();
            System.out.println("Selected: " + server);
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 3. Weighted Round Robin Load Balancing
        System.out.println("3. WEIGHTED ROUND ROBIN LOAD BALANCING:");
        System.out.println("   Servers with higher weights receive more requests");
        System.out.println("   Server-1 (weight=1), Server-2 (weight=2), Server-3 (weight=3)\n");

        WeightedRoundRobinLoadBalancer wrrLB = new WeightedRoundRobinLoadBalancer(servers);

        Map<String, Integer> requestCount = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            Server server = wrrLB.getNextServer();
            requestCount.put(server.getName(), requestCount.getOrDefault(server.getName(), 0) + 1);
        }

        System.out.println("Request Distribution:");
        requestCount
                .forEach((serverName, count) -> System.out.println("   " + serverName + ": " + count + " requests"));

        System.out.println("\n=== KEY CONCEPTS ===");
        System.out.println("✓ Round Robin: Simple, equal distribution");
        System.out.println("✓ Least Connections: Considers server load");
        System.out.println("✓ Weighted: Accounts for server capacity");
        System.out.println("✓ Used in: Nginx, HAProxy, AWS ELB, Kubernetes");
    }
}
