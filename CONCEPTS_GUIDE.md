# System Design Concepts - Quick Reference Guide

## Core Patterns Implemented

### 1. Load Balancing Algorithms

#### Round Robin
- **Complexity**: O(1)
- **When to use**: Equal server capacity, simple setup
- **Pros**: Simple, fair distribution
- **Cons**: Doesn't account for server load or capacity

#### Least Connections
- **Complexity**: O(n) for selection
- **When to use**: Variable request processing times
- **Pros**: Accounts for current server load
- **Cons**: More complex state management

#### Weighted Round Robin
- **Complexity**: O(1)
- **When to use**: Servers with different capacities
- **Pros**: Respects server capabilities
- **Cons**: Weights need manual configuration

---

### 2. Caching Strategies

#### LRU (Least Recently Used)
```
Access Pattern: A → B → C → A → D (capacity=3)
Cache State:  [A] → [B,A] → [C,B,A] → [A,C,B] → [D,A,C] (B evicted)
```

**Characteristics:**
- **Hit/Miss**: O(1) with HashMap + Doubly Linked List
- **Space**: O(capacity)
- **Best for**: Recently accessed data is likely to be accessed again

#### Write-Through Cache
```
Write Request → Update Cache → Update Database → Return
Read Request  → Check Cache → If miss, read DB → Update Cache → Return
```

**Trade-offs:**
- ✅ Strong consistency
- ❌ Higher write latency
- ✅ Simple to implement

#### Write-Back Cache
```
Write Request → Update Cache → Mark Dirty → Return (DB update async)
Read Request  → Check Cache → If miss, read DB → Update Cache → Return
```

**Trade-offs:**
- ✅ Lower write latency
- ❌ Risk of data loss
- ✅ Better performance

---

### 3. Rate Limiting Algorithms

#### Token Bucket
```
Bucket Capacity: 10 tokens
Refill Rate: 5 tokens/sec
Current Tokens: 7

Request arrives → Token--
If tokens > 0: ALLOW
If tokens = 0: DENY
```

**Use Cases:**
- APIs with burst allowance (AWS API Gateway)
- Network traffic shaping
- Microservices communication

#### Sliding Window Log
```
Window: 60 seconds
Max Requests: 100
Request Timestamps: [t1, t2, ..., t99]

New request at time T:
1. Remove timestamps < (T - 60s)
2. If count < 100: ALLOW
3. Else: DENY
```

**Characteristics:**
- ✅ Most accurate
- ❌ Higher memory usage O(requests in window)
- ❌ More complex implementation

---

### 4. Circuit Breaker States

```
           ┌─────────────┐
           │   CLOSED    │ Normal Operation
           │ (Requests   │
           │   flow)     │
           └──────┬──────┘
                  │ Failure Threshold Reached
                  ▼
           ┌─────────────┐
           │    OPEN     │ Blocking Requests
           │ (Fast Fail) │
           └──────┬──────┘
                  │ Timeout Elapsed
                  ▼
           ┌─────────────┐
           │  HALF-OPEN  │ Testing Recovery
           │ (Limited    │
           │   Requests) │
           └─────────────┘
                │      │
      Success   │      │ Failure
                ▼      ▼
             CLOSED   OPEN
```

**Configuration Example:**
```java
failureThreshold = 5      // Trip after 5 failures
successThreshold = 2      // Close after 2 successes in HALF-OPEN
timeout = 60000          // 60 seconds before HALF-OPEN
```

---

### 5. Message Queue Patterns

#### Point-to-Point (Simple Queue)
```
Producer → [Queue] → Consumer
                  → (message removed after consumption)
```

**Characteristics:**
- One consumer per message
- Guaranteed delivery
- Order preservation

#### Pub/Sub (Publish-Subscribe)
```
Publisher → [Topic: user.created]
                ├→ Email Service
                ├→ Analytics Service
                └→ CRM Service
```

**Characteristics:**
- Multiple consumers receive same message
- Broadcast semantics
- Decoupled communication

#### Dead Letter Queue (DLQ)
```
Main Queue → Consumer (Failure)
          ↓ Retry (Max: 3)
          ↓ Still Failing?
          → Dead Letter Queue (Manual inspection)
```

**Purpose:**
- Handle poison messages
- Debug processing failures
- Prevent infinite retries

---

## System Design Interview Patterns

### Scalability Patterns
1. **Horizontal Scaling**: Add more machines (Load Balancer enables this)
2. **Caching**: Reduce database load
3. **Sharding**: Partition data across multiple databases
4. **Async Processing**: Message queues for background tasks

### Reliability Patterns
1. **Circuit Breaker**: Prevent cascading failures
2. **Retry with Backoff**: Exponential backoff for transient failures
3. **Timeouts**: Fail fast on slow operations
4. **Health Checks**: Monitor service availability

### Performance Optimization
1. **Caching Layer**: Redis, Memcached
2. **CDN**: Cache static content closer to users
3. **Database Indexing**: Speed up queries
4. **Connection Pooling**: Reuse database connections

---

## Common Interview Questions

### Q: How do you handle 1 million requests per second?
**A**: Combination of:
1. Load balancing across multiple servers
2. Caching frequently accessed data (reduce DB hits)
3. Rate limiting to prevent abuse
4. Async processing with message queues
5. Database read replicas

### Q: How do you ensure high availability?
**A**:
1. Multiple availability zones
2. Circuit breakers for fault isolation
3. Health checks and auto-scaling
4. Database replication
5. Graceful degradation

### Q: Cache invalidation strategies?
**A**:
1. **TTL (Time To Live)**: Expire after X seconds
2. **Write-Through**: Invalidate on write
3. **Event-Based**: Invalidate on specific events
4. **Versioning**: Use versioned cache keys

---

## Real-World Architecture Examples

### Netflix
- **Load Balancing**: AWS ELB
- **Caching**: EVCache (custom Memcached)
- **Circuit Breaker**: Hystrix
- **Message Queue**: Apache Kafka

### Twitter
- **Load Balancing**: HAProxy
- **Caching**: Redis, Memcached
- **Rate Limiting**: Custom implementation
- **Message Queue**: Apache Kafka

### Uber
- **Load Balancing**: Custom solution
- **Caching**: Redis
- **Circuit Breaker**: Resilience4j
- **Message Queue**: Apache Kafka

---

## Performance Metrics

| Metric | Target | Measurement |
|--------|--------|-------------|
| Cache Hit Ratio | > 80% | Cache Hits / Total Requests |
| Response Time (P99) | < 200ms | 99th percentile latency |
| Availability | 99.99% | Uptime / Total Time |
| Request Rate | 10K req/s | Requests handled per second |
| Error Rate | < 0.1% | Failed Requests / Total Requests |

---

## Back-of-the-Envelope Calculations

### Storage Requirements
```
Daily Active Users: 10M
Requests per user: 50
Total requests/day: 500M
Avg request size: 1KB
Storage/day: 500GB
Storage/year: ~183TB
```

### Cache Sizing
```
Hot data: 20% of dataset
Dataset size: 1TB
Cache needed: 200GB (for 80% hit ratio)
With 3x replication: 600GB
```

### Server Capacity
```
Single server: 1000 req/s
Target load: 50K req/s
Servers needed: 50
With 2x redundancy: 100 servers
```

---

## Trade-offs Summary

| Decision | Pro | Con |
|----------|-----|-----|
| Caching | ⚡ Fast reads | 💾 Memory cost, ♻️ Stale data |
| Async (Queues) | 📈 Scalable | ⏱️ Eventual consistency |
| Replication | 🔒 HA/DR | 💰 Storage cost |
| Sharding | 📊 Scale writes | 🔧 Complex queries |
| Microservices | 🎯 Team autonomy | 🌐 Network overhead |

---

**Remember**: There's no one-size-fits-all solution. Always consider:
- Business requirements
- Scale expectations
- Team expertise
- Budget constraints
- Maintenance overhead
