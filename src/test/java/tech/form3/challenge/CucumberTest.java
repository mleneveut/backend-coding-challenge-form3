package tech.form3.challenge;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = "pretty",
        features = "classpath:features",
        glue = "tech.form3.challenge"
)
public class CucumberTest {
}