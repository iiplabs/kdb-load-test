package com.iiplabs.klt.core.repositories

import com.iiplabs.klt.core.KltCoreApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@ContextConfiguration(classes = arrayOf(KltCoreApplication::class))
@DataJpaTest
class RepositoriesTest @Autowired constructor (
    val entityManager: TestEntityManager,
    @Qualifier("subscriptionRepository") val subscriptionRepository: ISubscriptionRepository,
    @Qualifier("subscriptionMetaRepository") val subscriptionMetaRepository: ISubscriptionMetaRepository) {

    @Test
    fun injectedComponents() {
        assertThat(subscriptionRepository).isNotNull
        assertThat(subscriptionMetaRepository).isNotNull
    }
}