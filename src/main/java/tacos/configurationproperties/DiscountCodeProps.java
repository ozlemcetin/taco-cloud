package tacos.configurationproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/*
    Spring beans can be annotated with @ConfigurationProperties to enable
    injection of values from one of several property sources

    Configuration properties can be set in command-line arguments, environment
    variables, JVM system properties, properties files, or YAML files, among other
    options.
 */
@Component
@ConfigurationProperties(prefix = "taco.discount")
@Data
public class DiscountCodeProps {

    private Map<String, Integer> codes = new HashMap<>();
}
