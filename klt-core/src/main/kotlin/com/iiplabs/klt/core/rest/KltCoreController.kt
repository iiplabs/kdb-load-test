package com.iiplabs.klt.core.rest

import com.iiplabs.klt.core.service.MsisdnService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpStatus

@RequestMapping("/api/v1")
@RestController
class KltCoreController(@Qualifier("msisdnService") val msisdnService: MsisdnService) {

    private val logger = KotlinLogging.logger {}

    /**
     * @param msisdn - telephone number
     * @return list of meta by telephone number
     */
    @GetMapping("/find-meta-by-msisdn")
    fun findMetaByMsisdn(@RequestParam msisdn: Long): ResponseEntity<List<String>> {
        val msisdnList = msisdnService.findMetaByMsisdn(msisdn)
        if (msisdnList.isEmpty()) {
            return ResponseEntity(msisdnList, HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(msisdnList)
    }

    /**
     * @return all msisdn generated at start-up
     */
    @GetMapping("/get-all-msisdn")
    fun getAllMsisdn(): ResponseEntity<List<Long>> {
        return ResponseEntity.ok(msisdnService.getAllMsisdn())
    }

}
