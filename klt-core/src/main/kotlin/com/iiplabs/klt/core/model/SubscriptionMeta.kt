package com.iiplabs.klt.core.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "MSISDN_SUBSCRIPTION_META")
open class SubscriptionMeta {
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "id")
    open var id: Long? = null

    @get:Column(name = "meta")
    open var meta: String? = null

    @get:OneToMany(mappedBy = "subscriptionMeta")
    open var subscriptions: Set<Subscription>? = HashSet()
}