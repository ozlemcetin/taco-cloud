package tacos.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*
        WebMvcConfigurer defines several methods for configuring
        Spring MVC. Even though it’s an interface, it provides default implementations of all
        the methods, so you need to override only the methods you need. In this case, you
        override addViewControllers().
     */

    /*
        But when a controller is
        simple enough that it doesn’t populate a model or process input—as is the case with
        your HomeController—there’s another way that you can define the controller
     */

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        WebMvcConfigurer.super.addViewControllers(registry);

        /*
          a controller that does nothing but forward the request to a view.
         */
        registry.addViewController("/").setViewName("home");
    }
}
