package com.iiplabs.klt.core.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "MSISDN_SUBSCRIPTION")
open class Subscription {
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    @get:Column(name = "id")
    open var id: Long? = null

    @get:Column(name = "msisdn")
    open var msisdn: Long? = null

    @get:ManyToOne
    @get:JoinColumn(name = "meta_id")
    open var subscriptionMeta: SubscriptionMeta? = null
}
