package com.kgi.swiftly.lib.prod_feed_parser


import com.kgi.swiftly.lib.prod_feed_parser.impl.PriceInputs
import com.kgi.swiftly.lib.prod_feed_parser.parsers.ProductFeedParser


object TestContext {

    lateinit var currentPriceInputs: PriceInputs
    lateinit var currentParser: ProductFeedParser
}
