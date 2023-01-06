package com.iiplabs.klt.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KltCoreApplication

fun main(args: Array<String>) {
	runApplication<KltCoreApplication>(*args)
}
