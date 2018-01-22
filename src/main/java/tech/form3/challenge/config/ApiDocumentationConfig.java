package tech.form3.challenge.config;

import com.google.common.collect.ImmutableSet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * Configuration class for Swagger2 documentation.
 */
@Configuration
@EnableConfigurationProperties(AppInfoProperties.class)
@EnableSwagger2
public class ApiDocumentationConfig {

    @Bean
    public Docket api(AppInfoProperties appInfoProperties) {
        return new Docket(DocumentationType.SWAGGER_2)
                // API information (name, description, version, contact)
                .apiInfo(getApiInfo(appInfoProperties))
                // Security header for token
                .securitySchemes(Collections.singletonList(
                        new ApiKey("Authorization", "Authorization", "header")
                ))
                // Customize HTTP response codes
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, customResponses(commonResponses(), toErrorResponse(HttpStatus.NOT_FOUND)))
                .globalResponseMessage(RequestMethod.POST, customResponses(commonResponses(), toErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY)))
                .globalResponseMessage(RequestMethod.PUT, customResponses(commonResponses(), toErrorResponse(HttpStatus.NOT_FOUND), toErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY)))
                .globalResponseMessage(RequestMethod.PATCH, customResponses(commonResponses(), toErrorResponse(HttpStatus.NOT_FOUND), toErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY)))
                .globalResponseMessage(RequestMethod.DELETE, customResponses(commonResponses(), toErrorResponse(HttpStatus.NOT_FOUND), toErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY)))
                // Consumes and produces only JSON
                .consumes(ImmutableSet.of(MediaType.APPLICATION_JSON_VALUE))
                .produces(ImmutableSet.of(MediaType.APPLICATION_JSON_VALUE))
                // Describe the object encapsulated into ResponseEntity<T>
                .genericModelSubstitutes(ResponseEntity.class)
                // Define the API with documentation
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/v1/.*"))
                .build();
    }

    private ApiInfo getApiInfo(AppInfoProperties appInfoProperties) {
        return new ApiInfoBuilder()
                .title(appInfoProperties.getName())
                .description(appInfoProperties.getDescription())
                .version(appInfoProperties.getVersion())
                .contact(new Contact("Form3", "http://form3.tech", "contact@form3.tech"))
                .build();
    }

    private List<ResponseMessage> commonResponses() {
        return Arrays.asList(
                toErrorResponse(HttpStatus.UNAUTHORIZED),
                toErrorResponse(HttpStatus.FORBIDDEN),
                toErrorResponse(HttpStatus.BAD_REQUEST),
                toErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    private List<ResponseMessage> customResponses(List<ResponseMessage> common, ResponseMessage... custom) {
        List<ResponseMessage> commonResponses = ofNullable(common).orElse(emptyList());
        return Stream.concat(commonResponses.stream(), Stream.of(custom)).collect(toList());
    }

    private ResponseMessage toErrorResponse(HttpStatus status) {
        return new ResponseMessageBuilder()
                .code(status.value())
                .message(status.getReasonPhrase())
                .build();
    }
}