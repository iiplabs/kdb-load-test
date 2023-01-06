package com.iiplabs.klt.core.repositories

import com.iiplabs.klt.core.model.Subscription
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository("subscriptionRepository")
interface ISubscriptionRepository: CrudRepository<Subscription, Long> {
    fun findAllByMsisdn(msisdn: Long): Iterable<Subscription>

    @Query("select s.msisdn from Subscription s")
    fun findAllMsisdn(): List<Long>
}