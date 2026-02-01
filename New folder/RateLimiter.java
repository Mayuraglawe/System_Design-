import java.util.*;
import java.util.concurrent.*;

/**
 * Demonstrates Rate Limiting - Essential for API protection and resource
 * management
 * Rate limiting controls the rate of requests a client can make to prevent
 * abuse
 * and ensure fair resource allocation
 */
public class RateLimiter {

    // Token Bucket Algorithm
    static class TokenBucket {
        private final long capacity;
        private final long refillRate; // tokens per second
        private long availableTokens;
        private long lastRefillTimestamp;
        private final Object lock = new Object();

        public TokenBucket(long capacity, long refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.availableTokens = capacity;
            this.lastRefillTimestamp = System.currentTimeMillis();
        }

        public boolean allowRequest() {
            synchronized (lock) {
                refill();

                if (availableTokens > 0) {
                    availableTokens--;
                    System.out.println("   ✓ Request ALLOWED (Tokens remaining: " + availableTokens + ")");
                    return true;
                } else {
                    System.out.println("   ✗ Request DENIED (Rate limit exceeded)");
                    return false;
                }
            }
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long timeElapsed = now - lastRefillTimestamp;
            long tokensToAdd = (timeElapsed / 1000) * refillRate;

            if (tokensToAdd > 0) {
                availableTokens = Math.min(capacity, availableTokens + tokensToAdd);
                lastRefillTimestamp = now;
            }
        }
    }

    // Leaky Bucket Algorithm
    static class LeakyBucket {
        private final Queue<Long> requestQueue;
        private final long capacity;
        private final long leakRate; // requests per second
        private long lastLeakTimestamp;

        public LeakyBucket(long capacity, long leakRate) {
            this.requestQueue = new LinkedList<>();
            this.capacity = capacity;
            this.leakRate = leakRate;
            this.lastLeakTimestamp = System.currentTimeMillis();
        }

        public boolean allowRequest() {
            leak();

            if (requestQueue.size() < capacity) {
                requestQueue.offer(System.currentTimeMillis());
                System.out.println("   ✓ Request QUEUED (Queue size: " + requestQueue.size() + "/" + capacity + ")");
                return true;
            } else {
                System.out.println("   ✗ Request REJECTED (Queue full)");
                return false;
            }
        }

        private void leak() {
            long now = System.currentTimeMillis();
            long timeElapsed = now - lastLeakTimestamp;
            long requestsToLeak = (timeElapsed / 1000) * leakRate;

            for (int i = 0; i < requestsToLeak && !requestQueue.isEmpty(); i++) {
                requestQueue.poll();
            }

            if (requestsToLeak > 0) {
                lastLeakTimestamp = now;
            }
        }
    }

    // Fixed Window Counter
    static class FixedWindowCounter {
        private final long windowSize; // in milliseconds
        private final long maxRequests;
        private long windowStart;
        private long requestCount;

        public FixedWindowCounter(long windowSizeSeconds, long maxRequests) {
            this.windowSize = windowSizeSeconds * 1000;
            this.maxRequests = maxRequests;
            this.windowStart = System.currentTimeMillis();
            this.requestCount = 0;
        }

        public synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();

            // Check if we need to reset the window
            if (now - windowStart >= windowSize) {
                windowStart = now;
                requestCount = 0;
            }

            if (requestCount < maxRequests) {
                requestCount++;
                long timeRemaining = windowSize - (now - windowStart);
                System.out.println("   ✓ Request ALLOWED (" + requestCount + "/" + maxRequests +
                        " in window, resets in " + (timeRemaining / 1000) + "s)");
                return true;
            } else {
                long timeRemaining = windowSize - (now - windowStart);
                System.out.println("   ✗ Request DENIED (Limit reached, resets in " +
                        (timeRemaining / 1000) + "s)");
                return false;
            }
        }
    }

    // Sliding Window Log
    static class SlidingWindowLog {
        private final Queue<Long> requestTimestamps;
        private final long windowSize; // in milliseconds
        private final long maxRequests;

        public SlidingWindowLog(long windowSizeSeconds, long maxRequests) {
            this.requestTimestamps = new LinkedList<>();
            this.windowSize = windowSizeSeconds * 1000;
            this.maxRequests = maxRequests;
        }

        public synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();

            // Remove timestamps outside the current window
            while (!requestTimestamps.isEmpty() &&
                    now - requestTimestamps.peek() >= windowSize) {
                requestTimestamps.poll();
            }

            if (requestTimestamps.size() < maxRequests) {
                requestTimestamps.offer(now);
                System.out.println("   ✓ Request ALLOWED (" + requestTimestamps.size() +
                        "/" + maxRequests + " in sliding window)");
                return true;
            } else {
                long oldestRequest = requestTimestamps.peek();
                long resetTime = (windowSize - (now - oldestRequest)) / 1000;
                System.out.println("   ✗ Request DENIED (Next slot in " + resetTime + "s)");
                return false;
            }
        }
    }

    // Distributed Rate Limiter (simulated with Redis-like operations)
    static class DistributedRateLimiter {
        private final Map<String, Long> redis; // Simulated Redis
        private final long maxRequests;
        private final long windowSize;

        public DistributedRateLimiter(long windowSizeSeconds, long maxRequests) {
            this.redis = new ConcurrentHashMap<>();
            this.maxRequests = maxRequests;
            this.windowSize = windowSizeSeconds * 1000;
        }

        public boolean allowRequest(String userId) {
            String key = "rate_limit:" + userId;
            long now = System.currentTimeMillis();
            long windowStart = (now / windowSize) * windowSize;
            String windowKey = key + ":" + windowStart;

            Long count = redis.getOrDefault(windowKey, 0L);

            if (count < maxRequests) {
                redis.put(windowKey, count + 1);
                System.out.println("   ✓ User " + userId + " request ALLOWED (" +
                        (count + 1) + "/" + maxRequests + ")");
                return true;
            } else {
                System.out.println("   ✗ User " + userId + " request DENIED (Rate limit)");
                return false;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== RATE LIMITING DEMONSTRATION ===\n");

        // 1. Token Bucket Demo
        System.out.println("1. TOKEN BUCKET ALGORITHM:");
        System.out.println("   Capacity: 5 tokens, Refill: 2 tokens/sec");
        System.out.println("   Allows burst traffic up to capacity\n");

        TokenBucket tokenBucket = new TokenBucket(5, 2);
        for (int i = 1; i <= 7; i++) {
            System.out.print("   Request " + i + ": ");
            tokenBucket.allowRequest();
            Thread.sleep(300);
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 2. Fixed Window Counter Demo
        System.out.println("2. FIXED WINDOW COUNTER:");
        System.out.println("   Limit: 3 requests per 5-second window\n");

        FixedWindowCounter fixedWindow = new FixedWindowCounter(5, 3);
        for (int i = 1; i <= 5; i++) {
            System.out.print("   Request " + i + ": ");
            fixedWindow.allowRequest();
            Thread.sleep(500);
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 3. Sliding Window Log Demo
        System.out.println("3. SLIDING WINDOW LOG:");
        System.out.println("   Limit: 3 requests per 3-second sliding window\n");

        SlidingWindowLog slidingWindow = new SlidingWindowLog(3, 3);
        for (int i = 1; i <= 6; i++) {
            System.out.print("   Request " + i + ": ");
            slidingWindow.allowRequest();
            Thread.sleep(800);
        }

        System.out.println("\n" + "=".repeat(50) + "\n");

        // 4. Distributed Rate Limiter Demo
        System.out.println("4. DISTRIBUTED RATE LIMITER:");
        System.out.println("   Simulates Redis-based rate limiting for multiple users\n");

        DistributedRateLimiter distributedLimiter = new DistributedRateLimiter(10, 3);

        String[] users = { "user:alice", "user:bob", "user:alice", "user:alice", "user:bob" };
        for (String user : users) {
            distributedLimiter.allowRequest(user);
        }

        System.out.println("\n=== KEY CONCEPTS ===");
        System.out.println("✓ Token Bucket: Allows bursts, smooth traffic shaping");
        System.out.println("✓ Leaky Bucket: Constant output rate, queues requests");
        System.out.println("✓ Fixed Window: Simple but has boundary issues");
        System.out.println("✓ Sliding Window: More accurate, higher memory usage");
        System.out.println("✓ Distributed: Uses Redis/Memcached for multi-server");

        System.out.println("\n=== USE CASES ===");
        System.out.println("• API Rate Limiting (GitHub: 5000 req/hour)");
        System.out.println("• DDoS Protection");
        System.out.println("• Resource Allocation");
        System.out.println("• Cost Control (cloud services)");
        System.out.println("• Used in: Kong, AWS API Gateway, Cloudflare");
    }
}
