package com.kgi.swiftly.lib.prod_feed_parser

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(value = Cucumber::class)
@CucumberOptions(
        features = ["src/test/resources/features"],
        glue = ["com.kgi.swiftly.lib.prod_feed_parser.steps"],
        plugin = ["pretty","html:target/cucumber", "json:target/cucumber/report.json"],
        strict = true
)
class ProdFeedCukeTests
