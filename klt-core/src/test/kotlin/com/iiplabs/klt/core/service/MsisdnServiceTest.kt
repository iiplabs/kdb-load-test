package com.iiplabs.klt.core.service

import com.iiplabs.klt.core.repositories.ISubscriptionMetaRepository
import com.iiplabs.klt.core.repositories.ISubscriptionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mock

class MsisdnServiceTest {

    @Mock
    private lateinit var subscriptionRepository: ISubscriptionRepository
    @Mock
    private lateinit var subscriptionMetaRepository: ISubscriptionMetaRepository

    @Test
    fun batchTest() {
        var batch: List<Long> = mutableListOf()
        assertEquals(0, batch.size)
        batch += 123
        assertEquals(1, batch.size)
    }

}