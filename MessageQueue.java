import java.util.*;
import java.util.concurrent.*;

/**
 * Demonstrates Message Queue Pattern - Essential for asynchronous communication
 * Message queues enable decoupled, scalable, and reliable system architectures
 * Common in microservices, event-driven systems, and task processing
 */
public class MessageQueue {

    // Simple In-Memory Message Queue
    static class SimpleMessageQueue<T> {
        private final Queue<Message<T>> queue;
        private final int capacity;

        public SimpleMessageQueue(int capacity) {
            this.queue = new LinkedBlockingQueue<>(capacity);
            this.capacity = capacity;
        }

        public boolean publish(String topic, T payload) {
            Message<T> message = new Message<>(topic, payload);
            boolean added = queue.offer(message);

            if (added) {
                System.out.println("   [Publisher] ✓ Published to '" + topic + "': " + payload);
            } else {
                System.out.println("   [Publisher] ✗ Queue full, message dropped");
            }
            return added;
        }

        public Message<T> consume() {
            Message<T> message = queue.poll();
            if (message != null) {
                System.out.println("   [Consumer] ✓ Consumed from '" +
                        message.getTopic() + "': " + message.getPayload());
            }
            return message;
        }

        public int size() {
            return queue.size();
        }
    }

    // Message with metadata
    static class Message<T> {
        private final String id;
        private final String topic;
        private final T payload;
        private final long timestamp;
        private int retryCount;

        public Message(String topic, T payload) {
            this.id = UUID.randomUUID().toString();
            this.topic = topic;
            this.payload = payload;
            this.timestamp = System.currentTimeMillis();
            this.retryCount = 0;
        }

        public String getTopic() {
            return topic;
        }

        public T getPayload() {
            return payload;
        }

        public String getId() {
            return id;
        }

        public void incrementRetry() {
            retryCount++;
        }

        public int getRetryCount() {
            return retryCount;
        }

        @Override
        public String toString() {
            return "Message{topic='" + topic + "', payload=" + payload + "}";
        }
    }

    // Pub/Sub Pattern with Topics
    static class PubSubMessageBroker<T> {
        private final Map<String, List<Consumer<Message<T>>>> subscribers;

        public PubSubMessageBroker() {
            this.subscribers = new ConcurrentHashMap<>();
        }

        public void subscribe(String topic, Consumer<Message<T>> subscriber) {
            subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>())
                    .add(subscriber);
            System.out.println("   [Broker] Subscriber added to topic: " + topic);
        }

        public void publish(String topic, T payload) {
            Message<T> message = new Message<>(topic, payload);
            List<Consumer<Message<T>>> topicSubscribers = subscribers.get(topic);

            if (topicSubscribers == null || topicSubscribers.isEmpty()) {
                System.out.println("   [Broker] No subscribers for topic: " + topic);
                return;
            }

            System.out.println("   [Broker] Publishing to " + topicSubscribers.size() +
                    " subscribers: " + message);

            for (Consumer<Message<T>> subscriber : topicSubscribers) {
                // Asynchronous delivery
                CompletableFuture.runAsync(() -> subscriber.accept(message));
            }
        }
    }

    // Dead Letter Queue (DLQ) for failed messages
    static class MessageQueueWithDLQ<T> {
        private final BlockingQueue<Message<T>> mainQueue;
        private final BlockingQueue<Message<T>> deadLetterQueue;
        private final int maxRetries;

        public MessageQueueWithDLQ(int capacity, int maxRetries) {
            this.mainQueue = new LinkedBlockingQueue<>(capacity);
            this.deadLetterQueue = new LinkedBlockingQueue<>();
            this.maxRetries = maxRetries;
        }

        public void publish(String topic, T payload) {
            Message<T> message = new Message<>(topic, payload);
            mainQueue.offer(message);
            System.out.println("   [Queue] Enqueued: " + message);
        }

        public void processMessages(java.util.function.Function<Message<T>, Boolean> processor) {
            Message<T> message;
            while ((message = mainQueue.poll()) != null) {
                System.out.println("\n   [Processor] Processing: " + message);

                try {
                    boolean success = processor.apply(message);

                    if (success) {
                        System.out.println("   [Processor] ✓ Success");
                    } else {
                        handleFailure(message);
                    }
                } catch (Exception e) {
                    System.out.println("   [Processor] ✗ Exception: " + e.getMessage());
                    handleFailure(message);
                }
            }
        }

        private void handleFailure(Message<T> message) {
            message.incrementRetry();

            if (message.getRetryCount() < maxRetries) {
                System.out.println("   [Queue] Retry " + message.getRetryCount() +
                        "/" + maxRetries + " - Re-queuing");
                mainQueue.offer(message);
            } else {
                System.out.println("   [Queue] ✗ Max retries exceeded - Moving to DLQ");
                deadLetterQueue.offer(message);
            }
        }

        public int getDLQSize() {
            return deadLetterQueue.size();
        }

        public List<Message<T>> getDLQMessages() {
            return new ArrayList<>(deadLetterQueue);
        }
    }

    // Priority Queue
    static class PriorityMessageQueue<T> {
        private final PriorityBlockingQueue<PriorityMessage<T>> queue;

        static class PriorityMessage<T> implements Comparable<PriorityMessage<T>> {
            private final Message<T> message;
            private final int priority;

            public PriorityMessage(Message<T> message, int priority) {
                this.message = message;
                this.priority = priority;
            }

            @Override
            public int compareTo(PriorityMessage<T> other) {
                return Integer.compare(other.priority, this.priority); // Higher priority first
            }

            public Message<T> getMessage() {
                return message;
            }

            public int getPriority() {
                return priority;
            }
        }

        public PriorityMessageQueue() {
            this.queue = new PriorityBlockingQueue<>();
        }

        public void publish(String topic, T payload, int priority) {
            Message<T> message = new Message<>(topic, payload);
            PriorityMessage<T> priorityMessage = new PriorityMessage<>(message, priority);
            queue.offer(priorityMessage);

            System.out.println("   [Priority Queue] Enqueued (Priority " + priority + "): " + payload);
        }

        public Message<T> consume() {
            PriorityMessage<T> priorityMessage = queue.poll();
            if (priorityMessage != null) {
                System.out.println("   [Priority Queue] Consumed (Priority " +
                        priorityMessage.getPriority() + "): " +
                        priorityMessage.getMessage().getPayload());
                return priorityMessage.getMessage();
            }
            return null;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== MESSAGE QUEUE PATTERNS DEMONSTRATION ===\n");

        // 1. Simple Queue Demo
        System.out.println("1. SIMPLE MESSAGE QUEUE (Point-to-Point):");
        System.out.println("   Producer sends messages, Consumer processes them\n");

        SimpleMessageQueue<String> simpleQueue = new SimpleMessageQueue<>(10);

        simpleQueue.publish("orders", "Order-101");
        simpleQueue.publish("orders", "Order-102");
        simpleQueue.publish("orders", "Order-103");

        System.out.println("\n   Queue size: " + simpleQueue.size());
        System.out.println();

        simpleQueue.consume();
        simpleQueue.consume();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // 2. Pub/Sub Pattern Demo
        System.out.println("2. PUB/SUB PATTERN (Multiple Subscribers):");
        System.out.println("   Multiple consumers receive the same message\n");

        PubSubMessageBroker<String> broker = new PubSubMessageBroker<>();

        // Subscribe multiple consumers
        broker.subscribe("user.created",
                msg -> System.out.println("      → [Email Service] Sending welcome email for: " + msg.getPayload()));
        broker.subscribe("user.created",
                msg -> System.out.println("      → [Analytics Service] Tracking new user: " + msg.getPayload()));
        broker.subscribe("user.created",
                msg -> System.out.println("      → [CRM Service] Creating CRM record for: " + msg.getPayload()));

        Thread.sleep(500); // Let subscribers register
        System.out.println();

        broker.publish("user.created", "Alice");
        Thread.sleep(500);

        System.out.println();
        broker.publish("user.created", "Bob");
        Thread.sleep(500);

        System.out.println("\n" + "=".repeat(60) + "\n");

        // 3. Dead Letter Queue Demo
        System.out.println("3. DEAD LETTER QUEUE (Error Handling):");
        System.out.println("   Failed messages are retried, then moved to DLQ\n");

        MessageQueueWithDLQ<String> queueWithDLQ = new MessageQueueWithDLQ<>(10, 2);

        queueWithDLQ.publish("payment", "Payment-Success");
        queueWithDLQ.publish("payment", "Payment-Failure");
        queueWithDLQ.publish("payment", "Payment-Success");

        // Processor that fails for specific messages
        queueWithDLQ.processMessages(msg -> {
            if (msg.getPayload().contains("Failure")) {
                return false; // Simulate failure
            }
            return true;
        });

        System.out.println("\n   Dead Letter Queue size: " + queueWithDLQ.getDLQSize());

        System.out.println("\n" + "=".repeat(60) + "\n");

        // 4. Priority Queue Demo
        System.out.println("4. PRIORITY QUEUE:");
        System.out.println("   Messages are processed based on priority\n");

        PriorityMessageQueue<String> priorityQueue = new PriorityMessageQueue<>();

        priorityQueue.publish("tasks", "Low priority task", 1);
        priorityQueue.publish("tasks", "Critical task", 10);
        priorityQueue.publish("tasks", "Medium priority task", 5);
        priorityQueue.publish("tasks", "Urgent task", 8);

        System.out.println("\n   Processing in priority order:");
        while (priorityQueue.consume() != null) {
            // Process all messages
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("=== KEY CONCEPTS ===");
        System.out.println("✓ Asynchronous Communication");
        System.out.println("✓ Decoupling: Producers & consumers independent");
        System.out.println("✓ Scalability: Multiple consumers can process in parallel");
        System.out.println("✓ Reliability: Messages persist until processed");
        System.out.println("✓ Load Leveling: Absorbs traffic spikes");

        System.out.println("\n=== REAL-WORLD SYSTEMS ===");
        System.out.println("• RabbitMQ: AMQP protocol, flexible routing");
        System.out.println("• Apache Kafka: High-throughput, distributed log");
        System.out.println("• AWS SQS: Fully managed, serverless");
        System.out.println("• Redis Pub/Sub: Fast, in-memory messaging");
        System.out.println("• Google Cloud Pub/Sub: Global event distribution");
        System.out.println("=".repeat(60));
    }
}
