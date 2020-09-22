package tacos.configurationproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/*
        @ConfigurationProperties annotation.
        Its prefix attribute is set to taco.orders, which means that
        when setting the pageSize property, you need to use a configuration property named
        taco.orders.pageSize

        taco:
            orders:
                pageSize: 10

        ConfigurationProperties are in fact often placed on beans
        whose sole purpose in the application is to be holders of configuration data
 */

/*
        It’s also annotated with @Component so that Spring component scanning will automatically
        discover it and create it as a bean in the Spring application context
 */
@Component
@ConfigurationProperties(prefix = "taco.orders")
@Data
@Validated
public class OrderProps {

    /*
            //the pageSize property defaults to 20
            private int pageSize = 20;
     */
    @Min(value = 5, message = "must be between 5 and 25")
    @Max(value = 25, message = "must be between 5 and 25")
    private int pageSize;

}
