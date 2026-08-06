active-cache-starter
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── io
│   │   │       └── github
│   │   │           └── aniruddhamaity911
│   │   │               └── activecache
│   │   │
│   │   │                   ├── annotation
│   │   │                   │   ├── EnableActiveCache.java
│   │   │                   │   ├── CacheRead.java
│   │   │                   │   ├── CacheWrite.java
│   │   │                   │   └── CacheEvict.java
│   │   │
│   │   │                   ├── aspect
│   │   │                   │   └── CacheAspect.java
│   │   │
│   │   │                   ├── config
│   │   │                   │   ├── ActiveCacheAutoConfiguration.java
│   │   │                   │   ├── CacheProperties.java
│   │   │                   │   └── ActiveCacheConfiguration.java
│   │   │
│   │   │                   ├── service
│   │   │                   │   ├── CacheService.java
│   │   │                   │   └── RedisCacheService.java
│   │   │
│   │   │                   ├── key
│   │   │                   │   ├── CacheKeyGenerator.java
│   │   │                   │   └── DefaultCacheKeyGenerator.java
│   │   │
│   │   │                   ├── serializer
│   │   │                   │   └── RedisValueSerializer.java
│   │   │
│   │   │                   ├── support
│   │   │                   │   ├── CacheOperation.java
│   │   │                   │   ├── CacheMetadata.java
│   │   │                   │   └── CacheContext.java
│   │   │
│   │   │                   ├── util
│   │   │                   │   ├── CacheUtils.java
│   │   │                   │   ├── ReflectionUtils.java
│   │   │                   │   └── StringUtils.java
│   │   │
│   │   │                   ├── exception
│   │   │                   │   └── CacheException.java
│   │   │
│   │   │                   └── constant
│   │   │                       └── CacheConstants.java
│   │   │
│   │   └── resources
│   │       ├── META-INF
│   │       │   └── spring
│   │       │       └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│   │       │
│   │       └── META-INF
│   │           └── spring-configuration-metadata.json
│   │
│   └── test
│       └── java
│           └── io
│               └── github
│                   └── aniruddhamaity911
│                       └── activecache
│                           ├── aspect
│                           ├── config
│                           ├── service
│                           ├── key
│                           └── integration
│
├── pom.xml
├── README.md
├── LICENSE
└── .gitignore# active-cache-starter