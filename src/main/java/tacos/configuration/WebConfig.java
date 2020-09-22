package tacos.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*
    when a controller is simple enough that it doesn’t populate a model or process input
    —as is the case with your HomeController—
    there’s another way that you can define the controller
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        /*
        declare a view controller —
        a controller that does nothing but forward the request to a view.
         */

        /*
        The addViewControllers() method is given a ViewControllerRegistry that you
        can use to register one or more view controllers. Here, you call addViewController()
        on the registry, passing in "/", which is the path for which your view controller will
        handle GET requests.

        That method returns a ViewControllerRegistration object,
        on which you immediately call setViewName() to specify home as the view that a
        request for "/" should be forwarded to.
         */
        registry.addViewController("/").setViewName("home");

        /*
        Now you need to provide a controller that handles requests at that path.
        Because your login page will be fairly simple—nothing but a view—it’s easy enough to declare
        it as a view controller in WebConfig.
         */
        registry.addViewController("/login").setViewName("login");
    }
}
