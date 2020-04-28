package com.kgi.swiftly.lib.prod_feed_parser.steps

import com.kgi.swiftly.lib.prod_feed_parser.TestContext
import com.kgi.swiftly.lib.prod_feed_parser.TestContext.currentPriceInputs
import com.kgi.swiftly.lib.prod_feed_parser.TestContext.inputFile
import com.kgi.swiftly.lib.prod_feed_parser.TestContext.productRecords
import com.kgi.swiftly.lib.prod_feed_parser.api.*
import com.kgi.swiftly.lib.prod_feed_parser.impl.PriceInputs
import com.kgi.swiftly.lib.prod_feed_parser.impl.calcPrice
import com.kgi.swiftly.lib.prod_feed_parser.impl.displayPrice
import com.kgi.swiftly.lib.prod_feed_parser.impl.parsers.*
import com.kgi.swiftly.lib.prod_feed_parser.impl.parsers.FixedWidthFormatParser.Companion.moneyMathContext
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import java.io.File
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ParserStepFunctions {


    @Given("string {string} and StringFieldSpec {int},{int} we then extract {string}")
    fun testCharsExtraction( input:String, start:Int, end:Int, expected: String  ){
        val fieldSpec =  StringFieldSpec(start,end)
        val value = fieldSpec.getSequenceFrom( input.toCharArray())
        assertEquals( expected, String( value))
    }

    @Given("{string} feed format")
    fun feed_format(feedFormat: String?) {
        //todo:
        TestContext.currentParser = ProdFeedParserFactory.getParserForFeedFormat(FeedFormat.STANDARD)
    }

    @Then("we can detect flags in {string} {string} {string}")
    fun we_can_detect_flags(fieldContent: String, taxable: String, perWeight: String) {
        val prodFlags: ProductFlags = FixedWidthFormatParser.convertTo(ProductFlagsFieldSpec(), fieldContent.toCharArray()) as ProductFlags
        assertEquals(YN.valueOf(taxable), prodFlags.taxable)
        assertEquals(YN.valueOf(perWeight), prodFlags.perWeight)
    }

    @Then("we can get string {string} from padded {string}")
    fun we_can_get_string(expected: String, fieldContent: String) {
        assertEquals(expected, FixedWidthFormatParser.convertTo(StringFieldSpec(), fieldContent.replace("\"", "").toCharArray()))
    }

    @Then("we can parse currency {string} from {string}")
    fun we_can_parse_currency(expected: String, fieldContent: String) {
        val currency = FixedWidthFormatParser.convertTo(CurrencyFieldSpec(), fieldContent.toCharArray()) as Currency
        assertEquals(expected, currency.v.toPlainString())
    }

    @Then("we can parse int {int} from {string}")
    fun we_can_parse_integer(expected: Int, fieldContent: String) {
        val int = FixedWidthFormatParser.convertTo(IntFieldSpec(), fieldContent.toCharArray()) as Int
        assertEquals(expected, int)
    }

    @Given("price parts {string} {string} and number {int}")
    fun given_price_parts(price: String, splitPrice: String, numItems: Int) {
        TestContext.currentPriceInputs = PriceInputs(Currency(BigDecimal(price, moneyMathContext)), Currency(BigDecimal(splitPrice, moneyMathContext)), numItems)
    }

    @Then("we have {string} and {string}")
    fun calc_price_and_displayPrice_asExpected(expectedCalcPrice: String, expectedDisplay: String) {
        assertEquals(BigDecimal(expectedCalcPrice, moneyMathContext), currentPriceInputs.calcPrice()?.money?.v)
        assertEquals(expectedDisplay, currentPriceInputs.displayPrice())
    }

    @And("{string} input file")
    fun set_input_file_name(fileName: String) {
        TestContext.inputFile = File(fileName)
        assertTrue { inputFile.exists() }
    }

    @Then("we can parse it without errors")
    fun parse_input_file_without_errors() {
        val lines = inputFile.readLines(charset = Charsets.US_ASCII)
        val parser = getParserFor()
        val errors = mutableListOf<java.lang.Exception>()
        TestContext.productRecords = lines.map { line ->
            try {
                parser.parseLine(line)
            } catch (e: Exception) {
                errors.add(e)
                null
            }
        }.filterNotNull()
        if( !errors.isEmpty() ){
            errors.forEach { println(it); it.printStackTrace(); }
        }
        assertTrue(errors.isEmpty(),"We have parsing errors")
    }

    @And("for {string} we extracted proper {string} , {string}")
    fun verifyProductData( prodId:String, prodDescription:String, taxRate:String ){
        val prodR = productRecords.find{ p -> prodId == p.id }!!
        assertEquals( prodDescription, prodR.description )
        assertEquals( BigDecimal(taxRate), prodR.taxRatePercents )

    }
}
