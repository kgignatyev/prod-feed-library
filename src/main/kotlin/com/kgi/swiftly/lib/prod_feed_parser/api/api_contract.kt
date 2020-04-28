package com.kgi.swiftly.lib.prod_feed_parser.api

import com.kgi.swiftly.lib.prod_feed_parser.impl.parsers.ProdFeedParserFactory
import com.kgi.swiftly.lib.prod_feed_parser.impl.parsers.ProductFeedParser
import java.math.BigDecimal

enum class FeedType {
    STANDARD
}

enum class UnitOfMeasure {
    Each, Pound
}

data class FeedFormat(val feedType: FeedType, val version: String) {
    companion object {
        val STANDARD = FeedFormat(FeedType.STANDARD, "1.0")
    }
}

data class Price(val money: Currency, val perNumOfUnits: Int = 1)

data class ProductRecord(val id: String,

                         val description: String,
                         /**  Regular Calculator Price (price the calculator should use, rounded to, 4 decimal places, half-down, ) */
                         var regularCalcPrice: Price,
                         /** (English-readable string of your choosing, e.g. "$1.00" or "3 for $1.00") */
                         val regularPriceDisplay: String,
                         /**  if present the price the calculator should use, rounded to, 4 decimal places, half-down, ) */
                         var promoCalcPrice: Price?,
                         /** (English-readable string of your choosing, e.g. "$1.00" or "3 for $1.00") */
                         val promoPriceDisplay: String?,
                         val unitOfMeasure: UnitOfMeasure,
                         val size: String,
                         val taxRatePercents: BigDecimal
)


enum class YN {
    Y, N
}

data class ProductFlags( val taxable: YN = YN.N, val perWeight: YN = YN.N )

data class Currency(val v: BigDecimal, val currencySymbol: Char = '$')

/**
 * main method for using library
 */
fun getParserFor(ff: FeedFormat = FeedFormat.STANDARD): ProductFeedParser { return ProdFeedParserFactory.getParserForFeedFormat(ff)}

class ParsingException( val input:String, msg: String, cause:Throwable): Exception("input $input caused $msg", cause  )
