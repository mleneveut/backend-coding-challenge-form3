package tech.form3.challenge.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties mapping for information on application.
 */
@Data
@ConfigurationProperties(prefix = "info.app")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppInfoProperties {

    String name;

    String description;

    String version;
}