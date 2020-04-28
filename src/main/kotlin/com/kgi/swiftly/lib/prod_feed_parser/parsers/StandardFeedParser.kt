package com.kgi.swiftly.lib.prod_feed_parser.parsers

import com.kgi.swiftly.lib.prod_feed_parser.api.*
import com.kgi.swiftly.lib.prod_feed_parser.impl.PriceInputs
import com.kgi.swiftly.lib.prod_feed_parser.impl.calcPrice
import com.kgi.swiftly.lib.prod_feed_parser.impl.displayPrice
import com.kgi.swiftly.lib.prod_feed_parser.parsers.FixedWidthFormatParser.Companion.extractDataFrom
import java.lang.Exception

/**
 *
Start	End [Inclusive]	Name	Type
1	    8	Product ID	Number
10	68	Product Description	String
70	77	Regular Singular Price	Currency
79	86	Promotional Singular Price	Currency
88	95	Regular Split Price	Currency
97	104	Promotional Split Price	Currency
106	113	Regular For X	Number
115	122	Promotional For X	Number
124	132	Flags	Flags
134	142	Product Size	String
 */
class StandardFeedParser() : ProductFeedParser() {

    override fun parseLine(line: String): ProductRecord {
        try {
            val chars = line.toCharArray()
            val regularPriceComponents = PriceInputs(
                    extractDataFrom(chars, CurrencyFieldSpec(70, 77)) as Currency,
                    extractDataFrom(chars, CurrencyFieldSpec(88, 95)) as Currency,
                    extractDataFrom(chars, IntFieldSpec(106, 113)) as Int
            )

            val promoPriceComponents = PriceInputs(
                    extractDataFrom(chars, CurrencyFieldSpec(79, 86)) as Currency,
                    extractDataFrom(chars, CurrencyFieldSpec(97, 104)) as Currency,
                    extractDataFrom(chars, IntFieldSpec(115, 122)) as Int
            )
            val flags = extractDataFrom(chars, ProductFlagsFieldSpec(124, 132)) as ProductFlags
            val productId = extractDataFrom(chars, StringFieldSpec(1, 8)) as String
            return ProductRecord(
                    id = productId,
                    description = extractDataFrom(chars, StringFieldSpec(10, 68)) as String,
                    regularCalcPrice = regularPriceComponents.calcPrice()!!,
                    regularPriceDisplay = regularPriceComponents.displayPrice()!!,
                    promoCalcPrice = promoPriceComponents.calcPrice(),
                    promoPriceDisplay = promoPriceComponents.displayPrice(),
                    unitOfMeasure = unitOfMeasurement(flags),
                    size = extractDataFrom(chars, StringFieldSpec(134, 142)) as String,
                    taxRatePercents = findTaxRateFor(productId, flags.taxable)
            )
        } catch (e: Exception) {
            throw ParsingException(line, e.message ?: "no message",e)
        }
    }


}
