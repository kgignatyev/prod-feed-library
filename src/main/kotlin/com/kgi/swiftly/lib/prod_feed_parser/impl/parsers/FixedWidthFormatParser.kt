package com.kgi.swiftly.lib.prod_feed_parser.impl.parsers

import com.kgi.swiftly.lib.prod_feed_parser.api.Currency
import com.kgi.swiftly.lib.prod_feed_parser.api.ProductFlags
import com.kgi.swiftly.lib.prod_feed_parser.api.YN
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


open class FieldSpec( val startPositionInclusive:Int, val endPositionInclusive:Int, val zeroBased:Boolean = false ) {
    fun getSequenceFrom(lineChars: CharArray): CharArray {
       val start = if(zeroBased) startPositionInclusive else startPositionInclusive -1
       val end = if(zeroBased) endPositionInclusive+1 else endPositionInclusive
       return  lineChars.copyOfRange( start, end)
    }
}

class StringFieldSpec( startPositionInclusive:Int = 0, endPositionInclusive:Int = 1):FieldSpec( startPositionInclusive, endPositionInclusive)
class IntFieldSpec( startPositionInclusive:Int = 0, endPositionInclusive:Int =1):FieldSpec( startPositionInclusive, endPositionInclusive)
class ProductFlagsFieldSpec(startPositionInclusive:Int = 0, endPositionInclusive:Int = 1):FieldSpec( startPositionInclusive, endPositionInclusive)
class CurrencyFieldSpec( startPositionInclusive:Int = 0, endPositionInclusive:Int = 1):FieldSpec( startPositionInclusive, endPositionInclusive)




class FixedWidthFormatParser(val fieldSpecs: Collection<FieldSpec>) {

    fun parseLine( line:String):List<Any>{
        val lineChars = line.toCharArray()
        return fieldSpecs.map { extractDataFrom( lineChars, it ) }
    }



    companion object {

        val moneyMathContext = MathContext(4, RoundingMode.HALF_DOWN)

        fun extractDataFrom(lineChars: CharArray, fieldSpec: FieldSpec):Any {
            return convertTo( fieldSpec, fieldSpec.getSequenceFrom( lineChars ))
        }

        fun convertTo(fieldSpec: FieldSpec, chars: CharArray): Any {
            return when( fieldSpec ){
                is ProductFlagsFieldSpec -> getProductFlagsFrom(chars)
                is StringFieldSpec -> getStringFrom( chars )
                is IntFieldSpec -> getIntFrom( chars )
                is CurrencyFieldSpec -> getCurrencyFrom( chars )
                else -> throw Exception("unhandled spec ${fieldSpec::class}")
            }
        }

        @OptIn(ExperimentalStdlibApi::class)
        private fun getCurrencyFrom(chars: CharArray): Currency {
            val sign = if( '-' == chars[0]) "-" else ""
            val len = chars.size
            val cents = chars.concatToString( len-2,len)
            val unitDigits  = chars.copyOfRange( 1,len-2)
            val units = makeStringOfSignificantIntegerDigits(unitDigits)

            return Currency(BigDecimal( "$sign$units.$cents", moneyMathContext))
        }
        @OptIn(ExperimentalStdlibApi::class)
        private fun makeStringOfSignificantIntegerDigits(unitDigits: CharArray): String {
            val indexOfNonZero = unitDigits.indexOfFirst { c -> '0' != c }
            val units = if (indexOfNonZero == -1) {
                "0"
            } else {
                unitDigits.concatToString(indexOfNonZero)
            }
            return units
        }

        private fun getIntFrom(chars: CharArray): Int {
            return makeStringOfSignificantIntegerDigits(chars).toInt()
        }

        private fun getStringFrom(chars: CharArray): String {
            return String(chars).trim()
        }

        /**
         * The first flag in the left-to-right array is #1

        If Flag #3 is set, this is a per-weight item
        If Flag #5 is set, the item is taxable. Tax rate is always 7.775%
         */
        private fun getProductFlagsFrom(chars: CharArray): ProductFlags {
            fun getFlagNumber( p:Int):YN {
                return if( chars[p-1] == 'Y') YN.Y else YN.N
            }
            return ProductFlags( getFlagNumber(5),getFlagNumber(3))
        }
    }
}
