package tech.form3.challenge.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.form3.challenge.dto.PaymentDto;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Slf4j
@ActiveProfiles("test")
public class CommonRestCallStepDefs implements En {

    private static final String SCHEMA = "tests";

    private ResultActions resultActions;
    private PaymentDto createdDto;
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired(required = false)
    private WebApplicationContext context;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    public CommonRestCallStepDefs() {
        Given("^the web context is set$",
                () -> mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
        );

        Given("^the data (.+) is loaded$",
                (String sqlFile) -> ScriptUtils.executeSqlScript(ds.getConnection(), new ClassPathResource("tech/form3/challenge/web/" + sqlFile))
        );

        Given("^the database is empty$",
                () -> truncateAll()
        );

        Given("^the created payment is returned$",
                () -> createdDto = mapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), PaymentDto.class)
        );

        When("^client request GET ([\\S]*)$", (String resourceUri) ->
                resultActions = mockMvc.perform(
                        get(resourceUri)
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
        );

        When("^client request GET ([\\S]*) with created id$", (String resourceUri) ->
                resultActions = mockMvc.perform(
                        get(resourceUri + createdDto.getPaymentId())
                                .accept(MediaType.APPLICATION_JSON_UTF8)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
        );

        When("^client request POST ([\\S]*) with json data:$", (String resourceUri, String data) ->
                resultActions = mockMvc.perform(post(resourceUri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(data.getBytes()))
        );

        When("^client request PUT ([\\S]*) with json data:$", (String resourceUri, String data) ->
                resultActions = mockMvc.perform(put(resourceUri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(data.getBytes()))
        );

        When("^client request DELETE ([\\S]*)$", (String resourceUri) ->
                resultActions = mockMvc.perform(delete(resourceUri)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        );

        Then("^the response code should be (\\d*)$", (Integer statusCode) ->
                resultActions.andExpect(status().is(statusCode))
        );

        Then("^the response content type should be UTF8$", () ->
                resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        );

        Then("^the result json path (.+) should exists$", (String path) ->
                resultActions.andExpect(jsonPath(path).exists())
        );

        Then("^the result json should be:$", (String jsonString) ->
                resultActions.andExpect(content().json(jsonString))
        );

        Then("^the result json path (.+) should contain (\\d*) records$", (String path, Integer recordNumber) ->
                resultActions.andExpect(jsonPath(path, hasSize(recordNumber)))
        );

        Then("^the result json path (.+) should with created id be (.+)$", (String path, String val) ->
                resultActions.andExpect(jsonPath(path).value(val + createdDto.getPaymentId()))
        );

        Then("^the result json path (.+) should be (.+)$", (String path, String val) ->
                resultActions.andExpect(jsonPath(path).value(val))
        );

        Then("^the result json path (.+) should equals created id", (String path) ->
                resultActions.andExpect(jsonPath(path).value(createdDto.getPaymentId().toString()))
        );

        Then("clean the database$", () ->
                truncateAll()
        );
    }

    private void truncateAll() throws SQLException {
        jdbcTemplate.queryForList("SELECT * FROM pg_catalog.pg_tables WHERE schemaname = '" + SCHEMA + "'").stream()
                .map(e -> e.get("tablename"))
                .filter(tableName -> !"flyway_schema_history".equals(tableName)) // "flyway_schema_history" is a Flyway table !
                .peek(tableName -> log.info("Truncate table " + tableName))
                .forEach(tableName -> jdbcTemplate.execute("TRUNCATE TABLE \"" + tableName + "\""));
    }
}
