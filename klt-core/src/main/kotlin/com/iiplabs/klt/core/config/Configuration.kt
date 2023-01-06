package com.iiplabs.klt.core.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@EnableCaching
@PropertySource("classpath:config.properties")
class Configuration {

    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager: CaffeineCacheManager = CaffeineCacheManager()
        cacheManager.setCaffeine(caffeineCacheBuilder())
        return cacheManager
    }

    fun caffeineCacheBuilder(): Caffeine<Any, Any> =
        Caffeine.newBuilder().weakKeys().recordStats()

}