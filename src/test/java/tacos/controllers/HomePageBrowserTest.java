package tacos.controllers;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import tacos.repositories.TacoRepository;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomePageBrowserTest {

    @LocalServerPort
    private int port;
    private static HtmlUnitDriver browser;


    @BeforeClass
    public static void setUp() throws Exception {
        browser = new HtmlUnitDriver();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        browser.quit();
    }

    @Test
    public void testHomePage() {

        String homePageUrl = "http://localhost:" + port;
        browser.get(homePageUrl);

        String titleText = browser.getTitle();
        Assert.assertEquals(titleText, "Taco Cloud");

        String h1Text = browser.findElementByTagName("h1").getText();
        Assert.assertEquals(h1Text, "Welcome to...");

        String imgSrc = browser.findElementByTagName("img").getAttribute("src");
        Assert.assertEquals(imgSrc, homePageUrl + "/images/TacoCloud.png");
    }
}
