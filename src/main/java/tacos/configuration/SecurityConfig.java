package tacos.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    /*
    As with JDBC-based authentication, you can (and should) also configure a password
    encoder so that the password can be encoded in the database.

    You’ll do this by first declaring a bean of type PasswordEncoder and then
    injecting it into your user details service configuration by calling passwordEncoder():
     */
    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder("53cr3t");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        The security requirements for Taco Cloud should require that a user be authenticated
        before designing tacos or placing orders.

        But the homepage, login page, and registration page should be available to unauthenticated users.
        */

        /*
        You need to ensure that requests for /design and /orders are only available to authenticated
        users; all other requests should be permitted for all users.
         */


        http.authorizeRequests()
                /*
                   Requests for /design and /orders should be for users with a granted authority
                    of ROLE_USER.
                 */
                .antMatchers("/design", "/orders")
                //.hasRole("ROLE_USER")
                .access("hasRole('ROLE_USER')")

                /*
                    With SpEL-based security constraints, allow users with
                    ROLE_USER authority to create new tacos on Tuesdays (
                 */

                //.access("hasRole('ROLE_USER') && " +
                // "T(java.util.Calendar).getInstance()" +
                //".get(T(java.util.Calendar).DAY_OF_WEEK) == T(java.util.Calendar).TUESDAY")


                /*
                   The order of these rules is important. Security rules declared first take precedence
                   over those declared lower down
                */
                .antMatchers("/", "/**")
                //.permitAll();
                .access("permitAll")

                /*
                    The default login page is much better than the clunky HTTP basic dialog box you
                    started with, but it’s still rather plain and doesn’t quite fit into the look of the rest of
                    the Taco Cloud application.

                    To replace the built-in login page, you first need to tell Spring Security what path
                    your custom login page will be at. That can be done by calling formLogin() on the
                    HttpSecurity object passed into configure():
                */

                /*
                    Notice that before you call formLogin(), you bridge this section of configuration
                    and the previous section with a call to and().

                   and() method signifies that you’re finished with the authorization configuration
                   and are ready to apply some additional HTTP configuration.
                 */
                .and()
                .formLogin()
                .loginPage("/login")

                /*
                    The key things to note about this login page are the path it posts to and the names of
                    the username and password fields.

                    By default, Spring Security listens for login requests at /login and
                    expects that the username and password fields be named username
                    and password.

                    This is configurable, however. For example, the following configuration
                    customizes the path and field names:

                    Here, you specify that Spring Security should listen for requests to /authenticate to
                    handle login submissions. Also, the username and password fields should now be
                    named user and pwd.
                */

                //.loginProcessingUrl("/authenticate")
                //.usernameParameter("user\"")
                //.passwordParameter("pwd")

                /*
                    By default, a successful login will take the user directly to the page that they were
                    navigating to when Spring Security determined that they needed to log in.

                    If the user were to directly navigate to the login page, a successful login would take them to the
                    root path (for example, the homepage). But you can change that by specifying a
                    default success page:

                    Optionally, you can force the user to the design page after login, even if they were
                    navigating elsewhere prior to logging in, by passing true as a second parameter to
                    defaultSuccessUrl:
                */
                .defaultSuccessUrl("/design", true)

                /*
                    Just as important as logging into an application is logging out. To enable logout, you
                    simply need to call logout on the HttpSecurity object:

                    This sets up a security filter that intercepts POST requests to /logout. Therefore, to
                    provide logout capability, you just need to add a logout form and button to the views
                    in your application:

                    <form method="POST" th:action="@{/logout}">
                        <input type="submit" value="Logout"/>
                    </form>

                    When the user clicks the button, their session will be cleared, and they will be logged
                    out of the application. By default, they’ll be redirected to the login page where they
                    can log in again.

                    But if you’d rather they be sent to a different page, you can call
                    logoutSucessFilter() to specify a different post-logout landing page

                    logoutSuccessUrl("/")

                    In this case, users will be sent to the homepage following logout.
                */

                .and()
                .logout()
                .logoutSuccessUrl("/")

                /*
                    To protect against such attacks, applications can generate a CSRF token upon displaying
                    a form, place that token in a hidden field, and then stow it for later use on the
                    server. When the form is submitted, the token is sent back to the server along with the
                    rest of the form data. The request is then intercepted by the server and compared
                    with the token that was originally generated. If the token matches, the request is
                    allowed to proceed. Otherwise, the form must have been rendered by an evil website
                    without knowledge of the token generated by the server

                    Fortunately, Spring Security has built-in CSRF protection. Even more fortunate is
                    that it’s enabled by default and you don’t need to explicitly configure it. You only
                    need to make sure that any forms your application submits include a field named
                    _csrf that contains the CSRF token.

                    Spring Security even makes that easy by placing the CSRF token in a request attribute
                    with the name _csrf.

                    Therefore, you could render the CSRF token in a hidden field with the following in a Thymeleaf template:

                    <input type="hidden" name="_csrf" th:value="${_csrf.token}"/>

                    If you’re using Spring MVC’s JSP tag library or Thymeleaf with the Spring Security dialect,
                    you needn’t even bother explicitly including a hidden field. The hidden field will
                    be rendered automatically for you
                 */
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")

                /*
                    Allow pages to be loaded in frames from the same origin; needed for H2-Console
                 */
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
        ;


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        {
            /*
            you simply make a call to the userDetailsService() method, passing in
            the UserDetailsService instance that has been autowired into SecurityConfig.
             */

            /*
            It’s important that we discuss the last line in the configure() method. It would appear
            that you call the encoder() method and pass its return value to passwordEncoder().
            In reality, however, because the encoder() method is annotated with @Bean, it will
            be used to declare a PasswordEncoder bean in the Spring application context.

            Any calls to encoder() will then be intercepted to return the bean instance from the
            application context.
             */
            auth.userDetailsService(userDetailsService)
                    .passwordEncoder(encoder());
        }

        /*
        configure two users, "buzz" and "woody", in an in-memory user store.
         */
        {
            /*
            a call to the inMemoryAuthentication()
            method gives you an opportunity to specify user information directly in the security
            configuration itself.
             */

            //auth.inMemoryAuthentication()

            // .withUser("buzz")
            // .password("infinity")
            // .authorities("ROLE_USER")
            // .and()

            // .withUser("woody")
            // .password("bullseye")
            // .authorities("ROLE_USER");

        }

       /*
        User information is often maintained in a relational database, and a JDBC-based user
        store seems appropriate. The following listing shows how to configure Spring Security
        to authenticate against user information kept in a relational database with JDBC
        */
        {
            /*
             Although this minimal configuration will work, it makes some assumptions about your
             database schema.

             public static final String DEF_USERS_BY_USERNAME_QUERY =
            "select username,password,enabled " +
            "from users " +
            "where username = ?";

            public static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
            "select username,authority " +
            "from authorities " +
            "where username = ?";

            public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY =
            "select g.id, g.group_name, ga.authority " +
            "from groups g, group_members gm, group_authorities ga " +
            "where gm.username = ? " +
            "and g.id = ga.group_id " +
            "and g.id = gm.group_id";
             */

            // auth.jdbcAuthentication().dataSource(dataSource);

            /*
            chances are your database doesn’t
            look anything like this, and you’ll want more control over the queries. In that case,
            you can configure your own queries.
             */

            /*
            When replacing the default SQL queries with those of your own design, it’s important
            to adhere to the basic contract of the queries.

            All of them take the username as their only parameter.
            The authentication query selects the username, password, and enabled status.
            The authorities query selects zero or more rows containing the username and a granted authority.
            The group authorities query selects zero or more rows, each with a group ID, a group name, and an authority.
             */

            //  auth.jdbcAuthentication().dataSource(dataSource)
            //  .usersByUsernameQuery("select username, password, enabled from Users where username=? ")
            //  .authoritiesByUsernameQuery("select username,authority from UserAuthorities where username=? ")
            //  .passwordEncoder(new StandardPasswordEncoder("53cr3t"));
        }

       /*
        To configure Spring Security for LDAP-based authentication, you can use the ldap-
        Authentication() method. This method is the LDAP analog to jdbcAuthentication().
        The following configure() method shows a simple configuration for LDAP authentication:
        */
        {
            /*
           The userSearchFilter() and groupSearchFilter() methods are used to provide filters
            for the base LDAP queries, which are used to search for users and groups.

            By default, the base queries for both users and groups are empty, indicating that the
            search will be done from the root of the LDAP hierarchy.

            But you can change that by specifying a query base:
             */

            //auth.ldapAuthentication()
            //.userSearchBase("ou=people")
            //.userSearchFilter("(uid={0})")
            //.groupSearchBase("ou=groups")
            //.groupSearchFilter("member={0}")

                    /*
                    If you’d rather authenticate by doing a password comparison, you can declare so
                    with the passwordCompare() method:

                    sending the entered password to the LDAP directory
                    and asking the server to compare the password against a user’s password attribute.

                    Because the comparison is done within the LDAP server, the actual password
                    remains secret
                     */

                    /*
                    By default, the password given in the login form will be compared with the value of the
                    userPassword attribute in the user’s LDAP entry.

                    If the password is kept in a different
                    attribute, you can specify the password attribute’s name with passwordAttribute():
                     */

            //.passwordCompare()
            //.passwordEncoder(new BCryptPasswordEncoder())
            //.passwordAttribute("passcode");


                    /*
                    By default, Spring Security’s LDAP authentication assumes that the LDAP server is
                    listening on port 33389 on localhost. But if your LDAP server is on another machine,
                    you can use the contextSource() method to configure the location:
                     */

            //.contextSource()
            //.url("ldap://tacocloud.com:389/dc=tacocloud,dc=com")

                    /*
                    If you don’t happen to have an LDAP server lying around waiting to be authenticated
                    against, Spring Security can provide an embedded LDAP server for you. Instead of setting
                    the URL to a remote LDAP server, you can specify the root suffix for the embedded
                    server via the root() method:
                    */

            //.contextSource()
            //.root("dc=tacocloud,dc=com")
            //.ldif("classpath:users.ldif");


        }

    }
}
