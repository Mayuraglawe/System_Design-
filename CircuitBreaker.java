import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Demonstrates Circuit Breaker Pattern - Critical for resilient distributed
 * systems
 * Prevents cascading failures by stopping requests to failing services
 * and allowing them time to recover
 */
public class CircuitBreaker {

    enum State {
        CLOSED, // Normal operation
        OPEN, // Service is failing, block all requests
        HALF_OPEN // Testing if service has recovered
    }

    static class ServiceCircuitBreaker {
        private State state;
        private final int failureThreshold;
        private final int successThreshold;
        private final long timeout; // milliseconds

        private AtomicInteger failureCount;
        private AtomicInteger successCount;
        private AtomicLong lastFailureTime;

        public ServiceCircuitBreaker(int failureThreshold, int successThreshold, long timeoutMs) {
            this.state = State.CLOSED;
            this.failureThreshold = failureThreshold;
            this.successThreshold = successThreshold;
            this.timeout = timeoutMs;
            this.failureCount = new AtomicInteger(0);
            this.successCount = new AtomicInteger(0);
            this.lastFailureTime = new AtomicLong(0);
        }

        public synchronized String call(ExternalService service, String request) {
            // Check if we should transition from OPEN to HALF_OPEN
            if (state == State.OPEN) {
                if (System.currentTimeMillis() - lastFailureTime.get() > timeout) {
                    System.out.println("   [Circuit Breaker] State: OPEN → HALF_OPEN (Testing recovery)");
                    state = State.HALF_OPEN;
                    successCount.set(0);
                } else {
                    System.out.println("   [Circuit Breaker] BLOCKED - Circuit is OPEN");
                    long timeRemaining = timeout - (System.currentTimeMillis() - lastFailureTime.get());
                    System.out.println("   [Circuit Breaker] Retry in " + (timeRemaining / 1000) + "s");
                    return "CIRCUIT_OPEN - Service temporarily unavailable";
                }
            }

            try {
                // Attempt to call the service
                String response = service.execute(request);
                onSuccess();
                return response;

            } catch (ServiceException e) {
                onFailure();
                throw e;
            }
        }

        private void onSuccess() {
            failureCount.set(0);

            if (state == State.HALF_OPEN) {
                int successes = successCount.incrementAndGet();
                System.out.println("   [Circuit Breaker] Success in HALF_OPEN (" +
                        successes + "/" + successThreshold + ")");

                if (successes >= successThreshold) {
                    System.out.println("   [Circuit Breaker] State: HALF_OPEN → CLOSED (Service recovered!)");
                    state = State.CLOSED;
                }
            } else {
                System.out.println("   [Circuit Breaker] State: CLOSED (Normal operation)");
            }
        }

        private void onFailure() {
            lastFailureTime.set(System.currentTimeMillis());
            int failures = failureCount.incrementAndGet();

            if (state == State.HALF_OPEN) {
                System.out.println("   [Circuit Breaker] Failure in HALF_OPEN!");
                System.out.println("   [Circuit Breaker] State: HALF_OPEN → OPEN");
                state = State.OPEN;
                failureCount.set(0);

            } else if (failures >= failureThreshold) {
                System.out.println("   [Circuit Breaker] Failure threshold reached (" +
                        failures + "/" + failureThreshold + ")");
                System.out.println("   [Circuit Breaker] State: CLOSED → OPEN (Tripping circuit!)");
                state = State.OPEN;
            } else {
                System.out.println("   [Circuit Breaker] Failure detected (" +
                        failures + "/" + failureThreshold + ")");
            }
        }

        public State getState() {
            return state;
        }
    }

    // Simulated external service
    static class ExternalService {
        private boolean isHealthy;
        private String serviceName;

        public ExternalService(String serviceName) {
            this.serviceName = serviceName;
            this.isHealthy = true;
        }

        public String execute(String request) throws ServiceException {
            System.out.println("   [" + serviceName + "] Processing: " + request);

            if (!isHealthy) {
                System.out.println("   [" + serviceName + "] ✗ FAILED");
                throw new ServiceException(serviceName + " is down");
            }

            // Simulate processing
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            System.out.println("   [" + serviceName + "] ✓ SUCCESS");
            return serviceName + " processed: " + request;
        }

        public void setHealthy(boolean healthy) {
            this.isHealthy = healthy;
            System.out.println("\n>>> [" + serviceName + "] Health status changed: " +
                    (healthy ? "HEALTHY ✓" : "UNHEALTHY ✗") + " <<<\n");
        }
    }

    static class ServiceException extends RuntimeException {
        public ServiceException(String message) {
            super(message);
        }
    }

    // Retry mechanism with circuit breaker
    static class ResilientClient {
        private ServiceCircuitBreaker circuitBreaker;
        private ExternalService service;
        private int maxRetries;

        public ResilientClient(ExternalService service, ServiceCircuitBreaker circuitBreaker, int maxRetries) {
            this.service = service;
            this.circuitBreaker = circuitBreaker;
            this.maxRetries = maxRetries;
        }

        public String callWithRetry(String request) {
            int attempt = 0;

            while (attempt < maxRetries) {
                try {
                    attempt++;
                    System.out.println("\n=== Attempt " + attempt + " ===");
                    return circuitBreaker.call(service, request);

                } catch (ServiceException e) {
                    System.out.println("   [Resilient Client] Attempt " + attempt + " failed: " + e.getMessage());

                    if (circuitBreaker.getState() == State.OPEN) {
                        System.out.println("   [Resilient Client] Circuit is OPEN, aborting retries");
                        break;
                    }

                    if (attempt < maxRetries) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            return "ALL_ATTEMPTS_FAILED";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CIRCUIT BREAKER PATTERN DEMONSTRATION ===\n");

        // Configuration
        int failureThreshold = 3; // Open circuit after 3 failures
        int successThreshold = 2; // Close circuit after 2 successes
        long timeout = 5000; // 5 seconds timeout

        System.out.println("Configuration:");
        System.out.println("  • Failure Threshold: " + failureThreshold);
        System.out.println("  • Success Threshold: " + successThreshold);
        System.out.println("  • Timeout: " + (timeout / 1000) + " seconds");
        System.out.println();

        ExternalService paymentService = new ExternalService("Payment Service");
        ServiceCircuitBreaker circuitBreaker = new ServiceCircuitBreaker(
                failureThreshold, successThreshold, timeout);

        // Scenario 1: Normal operation
        System.out.println("=".repeat(60));
        System.out.println("SCENARIO 1: Normal Operation (Service Healthy)");
        System.out.println("=".repeat(60));

        for (int i = 1; i <= 3; i++) {
            System.out.println("\n--- Request " + i + " ---");
            try {
                circuitBreaker.call(paymentService, "Payment-" + i);
            } catch (ServiceException e) {
                // Handle error
            }
        }

        // Scenario 2: Service starts failing
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("SCENARIO 2: Service Failure (Circuit Opens)");
        System.out.println("=".repeat(60));

        paymentService.setHealthy(false); // Simulate service failure

        for (int i = 4; i <= 7; i++) {
            System.out.println("\n--- Request " + i + " ---");
            try {
                circuitBreaker.call(paymentService, "Payment-" + i);
            } catch (ServiceException e) {
                // Circuit breaker will eventually open
            }
        }

        // Scenario 3: Circuit is open, requests are blocked
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("SCENARIO 3: Circuit Open (Requests Blocked)");
        System.out.println("=".repeat(60));

        System.out.println("\n--- Request 8 (should be blocked) ---");
        try {
            circuitBreaker.call(paymentService, "Payment-8");
        } catch (ServiceException e) {
            // Blocked
        }

        // Scenario 4: Wait for timeout, then service recovers
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("SCENARIO 4: Service Recovery (Half-Open → Closed)");
        System.out.println("=".repeat(60));

        System.out.println("\nWaiting " + (timeout / 1000) + " seconds for circuit to enter HALF_OPEN...");
        Thread.sleep(timeout + 1000);

        paymentService.setHealthy(true); // Service is back online

        for (int i = 9; i <= 11; i++) {
            System.out.println("\n--- Request " + i + " ---");
            try {
                circuitBreaker.call(paymentService, "Payment-" + i);
            } catch (ServiceException e) {
                // Should succeed and close the circuit
            }
        }

        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("=== KEY BENEFITS ===");
        System.out.println("✓ Prevents cascading failures");
        System.out.println("✓ Fails fast instead of waiting for timeouts");
        System.out.println("✓ Automatic recovery detection");
        System.out.println("✓ Reduces load on failing services");

        System.out.println("\n=== REAL-WORLD USAGE ===");
        System.out.println("• Netflix Hystrix");
        System.out.println("• Resilience4j");
        System.out.println("• AWS API Gateway");
        System.out.println("• Spring Cloud Circuit Breaker");
        System.out.println("=".repeat(60));
    }
}
