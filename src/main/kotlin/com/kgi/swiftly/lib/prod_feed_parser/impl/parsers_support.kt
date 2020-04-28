package com.kgi.swiftly.lib.prod_feed_parser.impl

import com.kgi.swiftly.lib.prod_feed_parser.api.Currency
import com.kgi.swiftly.lib.prod_feed_parser.api.Price
import com.kgi.swiftly.lib.prod_feed_parser.impl.parsers.FixedWidthFormatParser.Companion.moneyMathContext
import java.math.BigDecimal.ZERO
import java.math.BigDecimal.valueOf
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*


data class PriceInputs( val price: Currency, val splitPrice:Currency, val numItems:Int =1)


fun Currency.isZero():Boolean {
    return v == ZERO || 0== (v.toDouble()*10000).toInt()
}


fun PriceInputs.hasPrices():Boolean  {
  return !splitPrice.isZero() || !price.isZero()
}

fun PriceInputs.calcPrice(): Price?  {
    return if( ! hasPrices()){
        null
    }else if( splitPrice.isZero()){
        Price(price,1)
    }else {
        if( splitPrice.isZero() ) null else {
            val calcPrice = splitPrice.v.divide(valueOf(numItems.toLong()), moneyMathContext)
            Price(Currency(calcPrice.setScale(4, RoundingMode.HALF_DOWN), splitPrice.currencySymbol), numItems)
        }
    }
}


fun PriceInputs.displayPrice():String?  {
    return if( ! hasPrices()){
        null
    } else if( splitPrice.isZero()){
        formatCurrency(price)
    }else {
        "$numItems for ${formatCurrency(splitPrice)}"
    }
}

val moneyNumberFormat = NumberFormat.getInstance(Locale.US)

fun formatCurrency(price: Currency): String {
    return "${price.currencySymbol}${moneyNumberFormat.format(price.v)}"
}
