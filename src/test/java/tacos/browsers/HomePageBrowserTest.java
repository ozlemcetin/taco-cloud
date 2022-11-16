package tacos.browsers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HomePageBrowserTest {

    private static HtmlUnitDriver unitDriver;
    @LocalServerPort
    private int port;

    @BeforeAll
    public static void setup() {

        unitDriver = new HtmlUnitDriver();
        unitDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterAll
    public static void teardown() {
        unitDriver.quit();
    }

    @Test
    public void testHomePage() {

        //homePage
        String homePage = "http://localhost:" + port;
        unitDriver.get(homePage);

        String titleText = unitDriver.getTitle();
        Assertions.assertThat(titleText).isEqualTo("Taco Cloud");

        String h1Text = unitDriver.findElement(new By.ByTagName("h1")).getText();
        Assertions.assertThat(h1Text).isEqualTo("Welcome to...");

        //http://localhost:53459/images/TacoCloud.png
        String imgSrc = unitDriver.findElement(new By.ByTagName("img")).getAttribute("src");
        Assertions.assertThat(imgSrc).isEqualTo(homePage + "/images/TacoCloud.png");
    }


}
