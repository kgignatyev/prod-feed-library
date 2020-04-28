package com.kgi.swiftly.lib.prod_feed_parser.parsers

import com.kgi.swiftly.lib.prod_feed_parser.api.*
import com.kgi.swiftly.lib.prod_feed_parser.api.FeedFormat.Companion.STANDARD
import com.kgi.swiftly.lib.prod_feed_parser.parsers.FixedWidthFormatParser.Companion.moneyMathContext
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


abstract class ProductFeedParser() {

    @Throws( ParsingException::class )
    abstract fun parseLine( line:String): ProductRecord


    val standardTaxRate = BigDecimal("7.775", moneyMathContext)

    fun findTaxRateFor(productId: String, taxable: YN): BigDecimal {
        return  when( taxable ){
            YN.Y -> standardTaxRate
            YN.N -> BigDecimal.ZERO
        }
    }

    fun unitOfMeasurement(flags: ProductFlags): UnitOfMeasure {
        return if( flags.perWeight == YN.Y ) UnitOfMeasure.Pound else UnitOfMeasure.Each
    }

}




object ProdFeedParserFactory {

    fun getParserForFeedFormat( ff: FeedFormat = STANDARD): ProductFeedParser {
        return findParserFor( ff )
    }


    private fun findParserFor(ff: FeedFormat): ProductFeedParser {
        return StandardFeedParser()
    }

}
