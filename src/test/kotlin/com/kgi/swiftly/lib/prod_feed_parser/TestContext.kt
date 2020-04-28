package com.kgi.swiftly.lib.prod_feed_parser


import com.kgi.swiftly.lib.prod_feed_parser.api.ProductRecord
import com.kgi.swiftly.lib.prod_feed_parser.impl.PriceInputs
import com.kgi.swiftly.lib.prod_feed_parser.parsers.ProductFeedParser
import java.io.File


object TestContext {

    lateinit var productRecords: List<ProductRecord>
    lateinit var inputFile: File
    lateinit var currentPriceInputs: PriceInputs
    lateinit var currentParser: ProductFeedParser
}
