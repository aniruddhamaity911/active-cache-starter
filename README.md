# Active Cache Starter

**Active Cache Starter** is a lightweight Spring Boot starter that simplifies Redis caching using declarative annotations.

Instead of manually implementing Redis operations, cache key generation, and caching logic, applications can simply add the starter as a project dependency and use:

```java
@EnableActiveCache
```

with:

```java
@CacheRead
@CacheWrite
@CacheEvict
```

The starter handles Redis integration, Spring AOP interception, cache key generation, serialization, and cache operations.

---

## Features

* Annotation-based Redis caching
* Automatic Redis configuration
* Spring AOP-based cache interception
* Automatic cache key generation
* SpEL support for dynamic cache keys
* Optional TTL support
* Cache eviction support
* Automatic serialization and deserialization
* Minimal application configuration
* Designed for Spring Boot applications

---

## How It Works

Active Cache Starter intercepts methods annotated with caching annotations and performs the corresponding Redis operation.

```text
Application
    │
    ▼
Annotated Method
    │
    ▼
Active Cache AOP
    │
    ├── @CacheRead  ──→ Redis GET
    │
    ├── @CacheWrite ──→ Execute Method ──→ Redis SET
    │
    └── @CacheEvict ──→ Execute Method ──→ Redis DELETE
```

---

## Requirements

* Java 21+
* Spring Boot 4.x
* Redis

---

## Add Active Cache Starter to Your Project

Active Cache Starter is currently not published to Maven Central.

To use it, download the source code from the latest GitHub Release and include it as a Maven module in your project.

### 1. Download the Release

Go to the repository's **Releases** page and download the latest **Source code (ZIP)**.

Extract the downloaded file and place the `active-cache-starter` project inside your application's parent project directory.

Your project structure should look similar to this:

```text
my-project/
│
├── active-cache-starter/
│   ├── src/
│   └── pom.xml
│
├── module/
│   ├── src/
│   └── pom.xml
│
└── pom.xml
```

### 2. Configure the Parent `pom.xml`

The parent `pom.xml` must include both the application and Active Cache Starter as Maven modules.

```xml
<packaging>pom</packaging>

<modules>
    <module>active-cache-starter</module>
    <module>student</module>
</modules>
```

For example:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
         http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>my-project</artifactId>
    <version>1.0.0</version>

    <packaging>pom</packaging>

    <modules>
        <module>active-cache-starter</module>
        <module>student</module>
    </modules>

</project>
```

### 3. Add Active Cache Starter as a Dependency

In your application's `pom.xml`, add:

```xml
<dependency>
    <groupId>io.github.aniruddhamaity911</groupId>
    <artifactId>active-cache-starter</artifactId>
    <version>{version of the active-cache-starter}</version>
</dependency>
```

Use the version defined in the `active-cache-starter` project's `pom.xml`.

### 4. Build the Project

Run the following command from the parent project directory:

```bash
mvn clean install
```

Maven will build the projects in the following order:

```text
active-cache-starter
        ↓
Install dependency
        ↓
student application
```

The Active Cache Starter can then be used by your Spring Boot application.

> Active Cache Starter is currently intended for local/project-level usage. Maven repository publication will be added in a future release.
### 5. Configure Redis

Add the following properties to your application's `application.properties`:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.application.name=my-app

# Optional, if Redis authentication is enabled
spring.data.redis.username=your-username
spring.data.redis.password=your-password
```

### 6. Enable Active Cache

Add `@EnableActiveCache` to your Spring Boot application:

```java
@SpringBootApplication
@EnableActiveCache
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

Active Cache Starter is now enabled.

---

# Annotations

## `@CacheRead`

Use `@CacheRead` when the application should check Redis before executing the method.

```java
@CacheRead(
    cacheName = "users",
    key = "#userId",
    ttl = 300
)
public User getUser(Long userId) {
    return userRepository.findById(userId);
}
```

### Execution Flow

```text
Method Called
     │
     ▼
Generate Cache Key
     │
     ▼
Check Redis
   ┌─┴─┐
   │   │
 HIT  MISS
   │   │
   │   ▼
   │ Execute Method
   │   │
   │   ▼
   │ Store Result
   │   │
   └───┴──→ Return Result
```

On a cache hit, the cached value is returned without executing the method.

On a cache miss, the method executes and its return value is stored in Redis.

---

## `@CacheWrite`

Use `@CacheWrite` when the method should always execute and its return value should be stored in Redis.

```java
@CacheWrite(
    cacheName = "users",
    key = "#user.id",
    ttl = 300
)
public User updateUser(User user) {
    return userRepository.save(user);
}
```

### Execution Flow

```text
Execute Method
      │
      ▼
Get Return Value
      │
      ▼
Generate Cache Key
      │
      ▼
Store Value in Redis
      │
      ▼
Return Value
```

`@CacheWrite` stores the **method's return value**, not the method parameter.

Conceptually, it is similar to Spring's `@CachePut`.

---

## `@CacheEvict`

Use `@CacheEvict` to remove an existing cache entry.

```java
@CacheEvict(
    cacheName = "users",
    key = "#userId"
)
public void deleteUser(Long userId) {
    userRepository.deleteById(userId);
}
```

The cache entry is removed after the method executes successfully.

```text
Execute Method
      │
      ▼
Method Successful
      │
      ▼
Generate Cache Key
      │
      ▼
Delete Redis Entry
```

---

# Cache Keys

Active Cache Starter generates Redis keys using:

```text
<spring.application.name>:<cacheName>:<key>
```

For example:

```properties
spring.application.name=my-app
```

and:

```java
@CacheRead(
    cacheName = "users",
    key = "#userId"
)
```

with:

```text
userId = 101
```

produces:

```text
my-app:users:101
```

### Key Components

| Component | Example | Description |
| --- | --- | --- |
| Application name | `my-app` | Value of `spring.application.name` |
| Cache name | `users` | Logical cache namespace |
| Key | `101` | Evaluated SpEL expression |

---

# SpEL Keys

Cache keys support Spring Expression Language (SpEL).

For example:

```java
@CacheRead(
        cacheName = "users",
        key = "#userId"
)
public User getUser(Long userId) {
    // ...
}
```

For an object parameter:

```java
@CacheRead(
        cacheName = "users",
        key = "#user.id"
)
public User getUser(User user) {
    // ...
}
```

The expression is evaluated at runtime to produce the actual cache key.

---

# TTL

`@CacheRead` and `@CacheWrite` support an optional TTL.

```java
@CacheRead(
        cacheName = "users",
        key = "#userId",
        ttl = 300
)
```

The cached entry will expire according to the specified TTL.

> TTL units and additional configuration options will be documented with the final API.

---

# Architecture

The project is organized into separate components with clearly defined responsibilities.

```text
io.github.aniruddhamaity.activecache
|
|-- ActiveCacheStarterApplication
|
|-- annotation/
|   |-- EnableActiveCache
|   |-- CacheRead
|   |-- CacheWrite
|   `-- CacheEvict
|
|-- aspect/
|   |-- CacheReadAspect
|   |-- CacheWriteAspect
|   `-- CacheEvictAspect
|
|-- config/
|   `-- ActiveCacheAutoConfiguration
|
|-- constant/
|   `-- ActiveCacheConstants
|
|-- exception/
|   |-- CacheException
|   `-- CacheSerializationException
|
|-- key/
|   |-- RedisKeyGenerator
|   `-- SpelKeyEvaluator
|
`-- service/
    |-- RedisCacheService
    `-- DefaultRedisCacheService
```

---

# Design

Active Cache Starter follows a few core principles.

### Minimal Configuration

Applications should not need to implement repetitive Redis caching logic.

### Declarative Caching

Caching behavior is defined directly on methods using annotations.

### Separation of Concerns

Redis operations, AOP interception, key generation, serialization, and configuration are implemented as separate components.

### Predictable Keys

All generated keys follow a consistent structure:

```text
<application>:<cacheName>:<key>
```

This keeps cache entries organized and reduces the possibility of key collisions between applications or cache namespaces.

---

# Example

A typical service can look like this:

```java
@Service
public class UserService {

    @CacheRead(
            cacheName = "users",
            key = "#userId",
            ttl = 300
    )
    public User getUser(Long userId) {
        return userRepository.findById(userId);
    }

    @CacheWrite(
            cacheName = "users",
            key = "#user.id",
            ttl = 300
    )
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(
            cacheName = "users",
            key = "#userId"
    )
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
```

---

# Project Status

**Status: In Development**

The project is currently under active development.

### Completed

* `@EnableActiveCache`
* `@CacheRead`
* `@CacheWrite`
* `@CacheEvict`
* `RedisCacheService`
* `DefaultRedisCacheService`
* `RedisKeyGenerator`
* Auto-configuration skeleton

### In Progress

* AOP caching aspect
* SpEL key evaluation
* Serialization layer
* Auto-configuration bean registration
* Support and utility classes
* Exception handling
* Tests
* Documentation

The API may change before the first stable release.

---

# Contributing

Contributions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Make your changes.
4. Add or update tests where appropriate.
5. Commit your changes.
6. Open a Pull Request.

Please keep changes focused and follow the existing project structure and coding conventions.

---

# Repository

[GitHub Repository](https://github.com/aniruddhamaity911/active-cache-starter)

If you find the project useful, consider giving it a star.
