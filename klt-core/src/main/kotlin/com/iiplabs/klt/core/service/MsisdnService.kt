package com.iiplabs.klt.core.service

import com.iiplabs.klt.core.model.Subscription
import com.iiplabs.klt.core.model.SubscriptionMeta
import com.iiplabs.klt.core.repositories.ISubscriptionMetaRepository
import com.iiplabs.klt.core.repositories.ISubscriptionRepository
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

interface MsisdnService {
    fun findMetaByMsisdn(msisdn: Long): List<String>

    fun getAllMsisdn(): List<Long>
}

@Service("msisdnService")
class MsisdnServiceImpl(
    @Qualifier("subscriptionRepository") val subscriptionRepository: ISubscriptionRepository,
    @Qualifier("subscriptionMetaRepository") val subscriptionMetaRepository: ISubscriptionMetaRepository,
    val cacheManager: CacheManager
) : MsisdnService {

    companion object {
        private val SUBSCRIPTION_CACHE: MutableMap<Long, List<String>> = mutableMapOf()
    }

    private val logger = KotlinLogging.logger {}

    @Value("\${generate.msisdn.records}")
    private val generateMsisdnRecords: Long = 0

    // @Cacheable(cacheNames = ["subscriptions"], sync = true)
    @Transactional(readOnly = true)
    override fun findMetaByMsisdn(msisdn: Long): List<String> {
        var m = SUBSCRIPTION_CACHE[msisdn]
        if (m == null) {
            logger.info { "Looking up meta info for MSISDN - ${msisdn}" }
            m = subscriptionRepository.findAllByMsisdn(msisdn)
                .mapNotNull { it.subscriptionMeta }
                .mapNotNull { it.meta }.distinct()
        }
        return m
    }

    /**
     * @return collection of all msisdn numbers in the system and invalidates subscriptions cache
     */
    @Transactional(readOnly = true)
    override fun getAllMsisdn(): List<Long> {
        // val c: Cache? = cacheManager.getCache("subscriptions")
        // c?.invalidate()
        // logger.info { "Subscriptions cache invalidated" }
        return subscriptionRepository.findAllMsisdn()
    }

    /**
     * @returns collection, but only one element (first) is needed
     */
    @Cacheable(cacheNames = ["meta"], sync = true)
    @Transactional(readOnly = true)
    fun getMetaByMeta(meta: String): Iterable<SubscriptionMeta> =
        subscriptionMetaRepository.findAllByMeta(meta)

    fun generateNewSubscription(meta: String, msisdn: Long): Subscription? {
        var metaRecord = getMetaByMeta(meta).first()

        if (metaRecord == null) {
            logger.error { "Record for ${meta} not found" }
            return null
        }
        val subscription = Subscription()
        subscription.msisdn = msisdn
        subscription.subscriptionMeta = metaRecord

        return subscription
    }

    private fun getBatchSize(remainingCounter: Long): Long {
        var batchSize: Long = 1
        if (remainingCounter >= 1000000) {
            batchSize = 200000
        } else if (remainingCounter >= 100000) {
            batchSize = 100000
        } else if (remainingCounter >= 10000) {
            batchSize = 10000
        } else if (remainingCounter >= 1000) {
            batchSize = 1000
        } else if (remainingCounter >= 500) {
            batchSize = 500
        } else if (remainingCounter >= 100) {
            batchSize = 100
        } else if (remainingCounter >= 50) {
            batchSize = 50
        } else if (remainingCounter >= 10) {
            batchSize = 10
        }
        return batchSize
    }

    private fun getSubscriptionBatch(metaList: List<String>, beginMsisdn: Long, batchSize: Long): List<Subscription> {
        var batch: List<Subscription?> = mutableListOf()
        for (i in 0..batchSize - 1) {
            batch += generateNewSubscription(metaList.random(), beginMsisdn + i)
        }
        return batch.mapNotNull { it }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveSubscriptionBatch(batch: List<Subscription>): Iterable<Subscription> {
        return subscriptionRepository.saveAll(batch)
    }

    @PostConstruct
    fun generateData() {
        logger.info { "generating MSISDN data - begin" }

        if (subscriptionRepository.count() > 0 || subscriptionMetaRepository.count() > 0) {
            logger.warn { "previous MSISDN data found, no new data added now, manual clear recommended!" }
            return
        }

        val metaList: List<String> = listOf(
            "meta_0", "meta_1", "meta_2", "meta_3", "meta_4",
            "meta_5", "meta_6", "meta_7", "meta_8", "meta_9",
            "meta_10", "meta_11", "meta_12", "meta_13", "meta_14"
        )
        metaList.forEach {
            val subscriptionMeta = SubscriptionMeta()
            subscriptionMeta.meta = it
            subscriptionMetaRepository.save(subscriptionMeta)
        }
        logger.info { "Saved ${metaList.size} meta rows" }

        var msisdnCounter: Long = 0
        while (msisdnCounter < generateMsisdnRecords) {
            val remainingCounter = generateMsisdnRecords - msisdnCounter
            val increment: Long = getBatchSize(remainingCounter)
            val generatedMsisdnList = getSubscriptionBatch(metaList, msisdnCounter, increment)
            logger.info { "${generatedMsisdnList.size} MSISDN rows ready for saving" }
            val savedMsisdnList = saveSubscriptionBatch(generatedMsisdnList)
            logger.info { "Saved ${savedMsisdnList.toList().size} MSISDN rows" }
            msisdnCounter += increment;
        }
        logger.info { "Saved total ${msisdnCounter} MSISDN rows" }

        logger.info { "generating MSISDN data - end" }

        /* logger.info { "filling up the cache" }
        subscriptionRepository.findAllMsisdn().forEach {
            val m: List<String> = subscriptionRepository.findAllByMsisdn(it).mapNotNull { it.subscriptionMeta }
                .mapNotNull { it.meta }.distinct()
            SUBSCRIPTION_CACHE[it] = m
        } */
    }

    @PreDestroy
    fun clearData() {
        logger.info { "clear all MSISDN data before shutdown - begin" }

        subscriptionRepository.deleteAll()
        subscriptionMetaRepository.deleteAll()

        logger.info { "clear all MSISDN data before shutdown - end" }
    }

}
