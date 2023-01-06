package com.iiplabs.klt.core.repositories

import com.iiplabs.klt.core.model.SubscriptionMeta
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository("subscriptionMetaRepository")
interface ISubscriptionMetaRepository: CrudRepository<SubscriptionMeta, Long> {
    fun findAllByMeta(meta: String): Iterable<SubscriptionMeta>
}