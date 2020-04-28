package com.kgi.swiftly.lib.prod_feed_parser.impl

import com.kgi.swiftly.lib.prod_feed_parser.api.getParserFor
import java.io.File


object SampleRunner {

    @JvmStatic
    fun main(args: Array<String>) {
        val inFile = File(if (args.size > 0) args[0] else {
            println("no arguments were provided, using default")
            "input-sample.txt"
        })

        println("reading data from ${inFile.absolutePath}")
        val lines = inFile.readLines( Charsets.US_ASCII)
        val parser = getParserFor()
        val errors = mutableListOf<java.lang.Exception>()
        val productRecords = lines.map { line ->
            try {
                parser.parseLine(line)
            } catch (e: Exception) {
                errors.add(e)
                null
            }
        }.filterNotNull()
        if( !errors.isEmpty() ){
            error("There were errors")
            errors.forEach { error(it); it.printStackTrace(); }
        }
        println("got the following product records")

        productRecords.forEach{
            println( it )
        }

    }
}
