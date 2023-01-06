package com.iiplabs.klt.core.rest

import com.iiplabs.klt.core.KltCoreApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = arrayOf(KltCoreApplication::class),
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KltCoreControllerTest {

    @Test
    fun contextLoads() {
        //
    }

}
