package com.kgi.swiftly.lib.prod_feed_parser.steps

import com.kgi.swiftly.lib.prod_feed_parser.TestContext
import com.kgi.swiftly.lib.prod_feed_parser.TestContext.currentPriceInputs
import com.kgi.swiftly.lib.prod_feed_parser.api.Currency
import com.kgi.swiftly.lib.prod_feed_parser.api.FeedFormat
import com.kgi.swiftly.lib.prod_feed_parser.api.ProductFlags
import com.kgi.swiftly.lib.prod_feed_parser.api.YN
import com.kgi.swiftly.lib.prod_feed_parser.impl.PriceInputs
import com.kgi.swiftly.lib.prod_feed_parser.impl.calcPrice
import com.kgi.swiftly.lib.prod_feed_parser.impl.displayPrice
import com.kgi.swiftly.lib.prod_feed_parser.parsers.*
import com.kgi.swiftly.lib.prod_feed_parser.parsers.FixedWidthFormatParser.Companion.moneyMathContext
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertSame


class ParserStepFunctions {


    @Given("{string} feed format")
    fun feed_format(feedFormat: String?) {
        //todo:
        TestContext.currentParser = ProdFeedParserFactory.getParserForFeedFormat( FeedFormat.STANDARD )
    }

    @Then("we can detect flags in {string} {string} {string}")
    fun we_can_detect_flags( fieldContent: String, taxable: String, perWeight: String) {
       val prodFlags:ProductFlags = FixedWidthFormatParser.convertTo(ProductFlagsFieldSpec(), fieldContent.toCharArray()) as ProductFlags
        assertEquals( YN.valueOf(taxable), prodFlags.taxable)
        assertEquals( YN.valueOf(perWeight), prodFlags.perWeight)
    }

    @Then("we can get string {string} from padded {string}")
    fun we_can_get_string(expected: String, fieldContent: String) {
        assertEquals( expected, FixedWidthFormatParser.convertTo( StringFieldSpec(), fieldContent.replace("\"","").toCharArray()))
    }

    @Then("we can parse currency {string} from {string}")
    fun we_can_parse_currency( expected:String, fieldContent: String){
        val currency = FixedWidthFormatParser.convertTo(CurrencyFieldSpec(), fieldContent.toCharArray()) as Currency
        assertEquals( expected, currency.v.toPlainString())
    }

    @Then("we can parse int {int} from {string}")
    fun we_can_parse_integer( expected:Int, fieldContent: String ){
        val int = FixedWidthFormatParser.convertTo(IntFieldSpec(), fieldContent.toCharArray()) as Int
        assertEquals( expected, int)
    }

    @Given("price parts {string} {string} and number {int}")
    fun given_price_parts( price:String, splitPrice:String, numItems:Int){
        TestContext.currentPriceInputs = PriceInputs( Currency( BigDecimal(price, moneyMathContext)), Currency(BigDecimal(splitPrice, moneyMathContext)), numItems)
    }

    @Then("we have {string} and {string}")
    fun calc_price_and_displayPrice_asExpected( expectedCalcPrice:String, expectedDisplay:String ){
        assertEquals( BigDecimal(expectedCalcPrice, moneyMathContext), currentPriceInputs.calcPrice()?.money?.v)
        assertEquals( expectedDisplay, currentPriceInputs.displayPrice())
    }

}
