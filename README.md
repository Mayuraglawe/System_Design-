# System Design Basics - Java Implementations

A comprehensive collection of fundamental system design concepts implemented in Java. This repository demonstrates key patterns and algorithms used in building scalable, reliable, and high-performance distributed systems.

## 📚 Concepts Covered

### 1. **Load Balancing** ([LoadBalancer.java](LoadBalancer.java))
Load balancing distributes incoming requests across multiple servers to optimize resource usage and maximize throughput.

**Implementations:**
- ✅ **Round Robin**: Sequential distribution across servers
- ✅ **Least Connections**: Routes to server with fewest active connections
- ✅ **Weighted Round Robin**: Distribution based on server capacity

**Real-world usage:** Nginx, HAProxy, AWS Elastic Load Balancer, Kubernetes Services

---

### 2. **Caching Strategies** ([CachingSystem.java](CachingSystem.java))
Caching stores frequently accessed data in fast-access storage to reduce latency and database load.

**Implementations:**
- ✅ **LRU Cache**: Evicts least recently used items
- ✅ **Write-Through Cache**: Synchronous writes to cache and database
- ✅ **Write-Back Cache**: Asynchronous writes with eventual consistency

**Key Metrics:** Cache hit ratio, eviction rate, memory usage

**Real-world usage:** Redis, Memcached, CDNs, CPU caches

---

### 3. **Rate Limiting** ([RateLimiter.java](RateLimiter.java))
Rate limiting controls the rate of requests to prevent abuse and ensure fair resource allocation.

**Implementations:**
- ✅ **Token Bucket**: Allows bursts up to capacity, refills at fixed rate
- ✅ **Leaky Bucket**: Constant output rate with request queuing
- ✅ **Fixed Window Counter**: Simple time-window based limiting
- ✅ **Sliding Window Log**: Accurate sliding time window
- ✅ **Distributed Rate Limiter**: Redis-based multi-server limiting

**Use Cases:** API protection, DDoS prevention, cost control

**Real-world usage:** Kong API Gateway, AWS API Gateway, GitHub API, Cloudflare

---

### 4. **Circuit Breaker Pattern** ([CircuitBreaker.java](CircuitBreaker.java))
Circuit breakers prevent cascading failures by detecting failures and temporarily blocking requests to failing services.

**States:**
- 🟢 **CLOSED**: Normal operation, requests proceed
- 🔴 **OPEN**: Service is failing, block all requests
- 🟡 **HALF_OPEN**: Testing if service has recovered

**Benefits:**
- Prevents cascading failures
- Fails fast instead of waiting for timeouts
- Automatic recovery detection
- Reduces load on failing services

**Real-world usage:** Netflix Hystrix, Resilience4j, Spring Cloud Circuit Breaker

---

### 5. **Message Queues** ([MessageQueue.java](MessageQueue.java))
Message queues enable asynchronous, decoupled communication between system components.

**Implementations:**
- ✅ **Simple Queue**: Point-to-point messaging
- ✅ **Pub/Sub Pattern**: Multiple subscribers, broadcast messaging
- ✅ **Dead Letter Queue (DLQ)**: Failed message handling with retries
- ✅ **Priority Queue**: Priority-based message processing

**Benefits:**
- Asynchronous communication
- Producer-consumer decoupling
- Load leveling and traffic spike absorption
- Guaranteed delivery with persistence

**Real-world usage:** RabbitMQ, Apache Kafka, AWS SQS, Google Cloud Pub/Sub

---

## 🚀 Running the Examples

Each file is a standalone Java application with a `main` method that demonstrates the concept:

```bash
# Compile
javac LoadBalancer.java
javac CachingSystem.java
javac RateLimiter.java
javac CircuitBreaker.java
javac MessageQueue.java

# Run
java LoadBalancer
java CachingSystem
java RateLimiter
java CircuitBreaker
java MessageQueue
```

## 📖 Learning Path

**Recommended order for beginners:**

1. **Start with Caching** - Fundamental for performance
2. **Load Balancing** - Understanding traffic distribution
3. **Rate Limiting** - Protecting your services
4. **Circuit Breaker** - Building resilient systems
5. **Message Queues** - Asynchronous architecture

## 🎯 System Design Principles Demonstrated

- **Scalability**: Horizontal scaling through load balancing and caching
- **Reliability**: Circuit breakers and message queues for fault tolerance
- **Performance**: Caching strategies to reduce latency
- **Security**: Rate limiting to prevent abuse
- **Decoupling**: Message queues for independent service evolution

## 🔧 Key Concepts

### CAP Theorem
- **Consistency**: All nodes see the same data
- **Availability**: System remains operational
- **Partition Tolerance**: System continues despite network partitions

### Trade-offs
- **Write-Through vs Write-Back**: Consistency vs Performance
- **Token Bucket vs Leaky Bucket**: Burst handling vs Smooth rate
- **Synchronous vs Asynchronous**: Latency vs Complexity

## 📊 Performance Considerations

| Pattern | Time Complexity | Space Complexity | Use Case |
|---------|----------------|------------------|----------|
| LRU Cache | O(1) get/put | O(capacity) | Frequently accessed data |
| Token Bucket | O(1) | O(1) | API rate limiting |
| Circuit Breaker | O(1) | O(1) | Fault tolerance |
| Round Robin | O(1) | O(n servers) | Simple load distribution |

## 🌐 Real-World Applications

- **E-commerce**: Caching product data, rate limiting checkout API
- **Social Media**: Message queues for notifications, load balancing user requests
- **Banking**: Circuit breakers for payment services, distributed rate limiting
- **Video Streaming**: CDN caching, adaptive bitrate load balancing

## 📚 Further Reading

- [System Design Primer](https://github.com/donnemartin/system-design-primer)
- [Designing Data-Intensive Applications](https://dataintensive.net/) by Martin Kleppmann
- [Building Microservices](https://www.oreilly.com/library/view/building-microservices/9781491950340/) by Sam Newman
- [Release It!](https://pragprog.com/titles/mnee2/release-it-second-edition/) by Michael T. Nygard

## 🤝 Contributing

Feel free to add more system design patterns or improve existing implementations!

## 📝 License

This project is for educational purposes.

---

**Author**: Mayur Aglawe  
**Repository**: https://github.com/Mayuraglawe/System_Design-  
**Last Updated**: January 2026
