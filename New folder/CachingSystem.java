import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Demonstrates Caching - A critical system design concept for performance
 * optimization
 * Caching stores frequently accessed data in fast-access storage to reduce
 * latency
 * and database load
 */
public class CachingSystem {

    // LRU (Least Recently Used) Cache Implementation
    static class LRUCache<K, V> {
        private final int capacity;
        private final Map<K, Node<K, V>> cache;
        private Node<K, V> head;
        private Node<K, V> tail;

        private static class Node<K, V> {
            K key;
            V value;
            Node<K, V> prev;
            Node<K, V> next;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.cache = new HashMap<>();

            // Initialize dummy head and tail
            head = new Node<>(null, null);
            tail = new Node<>(null, null);
            head.next = tail;
            tail.prev = head;
        }

        public V get(K key) {
            Node<K, V> node = cache.get(key);
            if (node == null) {
                System.out.println("   CACHE MISS: " + key);
                return null;
            }

            System.out.println("   CACHE HIT: " + key);
            // Move to front (most recently used)
            moveToFront(node);
            return node.value;
        }

        public void put(K key, V value) {
            Node<K, V> node = cache.get(key);

            if (node != null) {
                // Update existing node
                node.value = value;
                moveToFront(node);
                System.out.println("   CACHE UPDATE: " + key + " = " + value);
            } else {
                // Add new node
                if (cache.size() >= capacity) {
                    // Evict least recently used
                    Node<K, V> lru = tail.prev;
                    removeNode(lru);
                    cache.remove(lru.key);
                    System.out.println("   CACHE EVICTION: " + lru.key + " (LRU)");
                }

                Node<K, V> newNode = new Node<>(key, value);
                cache.put(key, newNode);
                addToFront(newNode);
                System.out.println("   CACHE ADD: " + key + " = " + value);
            }
        }

        private void moveToFront(Node<K, V> node) {
            removeNode(node);
            addToFront(node);
        }

        private void addToFront(Node<K, V> node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        private void removeNode(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        public void displayCache() {
            System.out.print("   Cache State: [");
            Node<K, V> current = head.next;
            List<String> items = new ArrayList<>();
            while (current != tail) {
                items.add(current.key + "=" + current.value);
                current = current.next;
            }
            System.out.println(String.join(", ", items) + "]");
        }
    }

    // Write-Through Cache
    static class WriteThroughCache<K, V> {
        private Map<K, V> cache;
        private Database<K, V> database;

        public WriteThroughCache(Database<K, V> database) {
            this.cache = new ConcurrentHashMap<>();
            this.database = database;
        }

        public V get(K key) {
            V value = cache.get(key);
            if (value != null) {
                System.out.println("   [Write-Through] CACHE HIT: " + key);
                return value;
            }

            System.out.println("   [Write-Through] CACHE MISS: " + key);
            value = database.read(key);
            if (value != null) {
                cache.put(key, value);
            }
            return value;
        }

        public void put(K key, V value) {
            // Write to both cache and database synchronously
            cache.put(key, value);
            database.write(key, value);
            System.out.println("   [Write-Through] Updated cache AND database: " + key);
        }
    }

    // Write-Back (Write-Behind) Cache
    static class WriteBackCache<K, V> {
        private Map<K, V> cache;
        private Set<K> dirtyKeys;
        private Database<K, V> database;

        public WriteBackCache(Database<K, V> database) {
            this.cache = new ConcurrentHashMap<>();
            this.dirtyKeys = ConcurrentHashMap.newKeySet();
            this.database = database;
        }

        public V get(K key) {
            V value = cache.get(key);
            if (value != null) {
                System.out.println("   [Write-Back] CACHE HIT: " + key);
                return value;
            }

            System.out.println("   [Write-Back] CACHE MISS: " + key);
            value = database.read(key);
            if (value != null) {
                cache.put(key, value);
            }
            return value;
        }

        public void put(K key, V value) {
            // Write to cache only, mark as dirty
            cache.put(key, value);
            dirtyKeys.add(key);
            System.out.println("   [Write-Back] Updated cache only: " + key + " (will sync later)");
        }

        public void flush() {
            System.out.println("   [Write-Back] Flushing dirty keys to database...");
            for (K key : dirtyKeys) {
                V value = cache.get(key);
                database.write(key, value);
                System.out.println("   [Write-Back] Synced to DB: " + key);
            }
            dirtyKeys.clear();
        }
    }

    // Simulated Database
    static class Database<K, V> {
        private Map<K, V> storage = new HashMap<>();

        public V read(K key) {
            simulateLatency();
            return storage.get(key);
        }

        public void write(K key, V value) {
            simulateLatency();
            storage.put(key, value);
        }

        private void simulateLatency() {
            try {
                Thread.sleep(50); // Simulate DB latency
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== CACHING SYSTEM DEMONSTRATION ===\n");

        // 1. LRU Cache Demo
        System.out.println("1. LRU (Least Recently Used) CACHE:");
        System.out.println("   Evicts least recently accessed item when capacity is full\n");

        LRUCache<String, String> lruCache = new LRUCache<>(3);

        lruCache.put("user:1", "Alice");
        lruCache.put("user:2", "Bob");
        lruCache.put("user:3", "Charlie");
        lruCache.displayCache();

        System.out.println();
        lruCache.get("user:1"); // Access user:1
        lruCache.displayCache();

        System.out.println();
        lruCache.put("user:4", "David"); // This will evict user:2
        lruCache.displayCache();

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 2. Write-Through Cache Demo
        System.out.println("2. WRITE-THROUGH CACHE:");
        System.out.println("   Writes to cache and database synchronously\n");

        Database<String, String> db1 = new Database<>();
        WriteThroughCache<String, String> wtCache = new WriteThroughCache<>(db1);

        wtCache.put("product:101", "Laptop");
        wtCache.put("product:102", "Mouse");

        System.out.println();
        wtCache.get("product:101");
        wtCache.get("product:103"); // Cache miss

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 3. Write-Back Cache Demo
        System.out.println("3. WRITE-BACK (Write-Behind) CACHE:");
        System.out.println("   Writes to cache immediately, syncs to DB later\n");

        Database<String, String> db2 = new Database<>();
        WriteBackCache<String, String> wbCache = new WriteBackCache<>(db2);

        wbCache.put("order:1", "Pending");
        wbCache.put("order:2", "Shipped");
        wbCache.put("order:3", "Delivered");

        System.out.println();
        wbCache.flush(); // Sync all dirty data to database

        System.out.println("\n=== KEY CONCEPTS ===");
        System.out.println("✓ LRU Cache: Most common eviction policy");
        System.out.println("✓ Write-Through: Strong consistency, higher latency");
        System.out.println("✓ Write-Back: Better performance, eventual consistency");
        System.out.println("✓ Cache Hit Ratio: Key performance metric");
        System.out.println("✓ Used in: Redis, Memcached, CPU caches, CDNs");

        System.out.println("\n=== CACHING STRATEGIES ===");
        System.out.println("• Cache-Aside: Application manages cache");
        System.out.println("• Read-Through: Cache loads data automatically");
        System.out.println("• Write-Through: Synchronous write to cache + DB");
        System.out.println("• Write-Back: Asynchronous write to DB");
        System.out.println("• Refresh-Ahead: Proactive cache refresh");
    }
}
