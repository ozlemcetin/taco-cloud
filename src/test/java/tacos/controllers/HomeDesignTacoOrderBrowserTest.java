package tacos.controllers;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import tacos.repositories.TacoRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HomeDesignTacoOrderBrowserTest {

    private static HtmlUnitDriver browser;

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate rest;

    @BeforeClass
    public static void setup() {
        browser = new HtmlUnitDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void test_HomeDesignTacoOrderPages_HappyPath() throws Exception {

        //home page
        browser.get(homePageUrl());
        assertEquals(homePageUrl(), browser.getCurrentUrl());

        //design taco
        clickDesignATaco();
        assertDesignPageElements();

        //submit taco
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");

        //design taco
        clickBuildAnotherTaco();
        assertDesignPageElements();

        //submit taco
        buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR");

        //submit order
        fillInAndSubmitOrderForm();

        //home page
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    @Test
    public void test_HomeDesignTacoOrderPages_EmptyOrderInfo() throws Exception {

        //home page
        browser.get(homePageUrl());
        assertEquals(homePageUrl(), browser.getCurrentUrl());

        //design taco
        clickDesignATaco();
        assertDesignPageElements();

        //submit taco
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");

        //submit order
        submitEmptyOrderForm();
        assertValidationErrorsOnEmptyOrder();

        //submit order
        fillInAndSubmitOrderForm();

        //home page
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    @Test
    public void test_HomeDesignTacoOrderPages_invalidOrderInfo() throws Exception {

        //home page
        browser.get(homePageUrl());
        assertEquals(homePageUrl(), browser.getCurrentUrl());

        //design taco
        clickDesignATaco();
        assertDesignPageElements();

        //submit taco
        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");

        //submit order
        submitInvalidOrderForm();
        assertValidationErrorsOnInvalidOrder();

        //submit order
        fillInAndSubmitOrderForm();

        //home page
        assertEquals(homePageUrl(), browser.getCurrentUrl());
    }

    private void assertDesignPageElements() {

        assertEquals(designPageUrl(), browser.getCurrentUrl());

        List<WebElement> ingredientGroups = browser.findElementsByClassName("ingredient-group");
        assertEquals(5, ingredientGroups.size());

        {
            WebElement wrapGroup = browser.findElementByCssSelector("div.ingredient-group#wraps");

            List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
            assertEquals(2, wraps.size());

            assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla");
            assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla");
        }

        {
            WebElement proteinGroup = browser.findElementByCssSelector("div.ingredient-group#proteins");

            List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
            assertEquals(2, proteins.size());

            assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef");
            assertIngredient(proteinGroup, 1, "CARN", "Carnitas");
        }

        {
            WebElement cheeseGroup = browser.findElementByCssSelector("div.ingredient-group#cheeses");

            List<WebElement> cheeses = cheeseGroup.findElements(By.tagName("div"));
            assertEquals(2, cheeses.size());

            assertIngredient(cheeseGroup, 0, "CHED", "Cheddar");
            assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack");
        }

        {
            WebElement veggieGroup = browser.findElementByCssSelector("div.ingredient-group#veggies");

            List<WebElement> veggies = veggieGroup.findElements(By.tagName("div"));
            assertEquals(2, veggies.size());

            assertIngredient(veggieGroup, 0, "TMTO", "Diced Tomatoes");
            assertIngredient(veggieGroup, 1, "LETC", "Lettuce");
        }

        {
            WebElement sauceGroup = browser.findElementByCssSelector("div.ingredient-group#sauces");

            List<WebElement> sauces = sauceGroup.findElements(By.tagName("div"));
            assertEquals(2, sauces.size());

            assertIngredient(sauceGroup, 0, "SLSA", "Salsa");
            assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream");
        }
    }

    private void assertIngredient(WebElement ingredientGroup,
                                  int ingredientIdx, String id, String name) {

        List<WebElement> divElements = ingredientGroup.findElements(By.tagName("div"));
        WebElement ingredient = divElements.get(ingredientIdx);

        assertEquals(id,
                ingredient.findElement(By.tagName("input")).getAttribute("value"));
        assertEquals(name,
                ingredient.findElement(By.tagName("span")).getText());
    }

    private void assertValidationErrorsOnEmptyOrder() {


        assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

        List<String> validationErrors = getValidationErrorTexts();
        assertEquals(9, validationErrors.size());
        assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
        assertTrue(validationErrors.contains("Delivery name is required"));
        assertTrue(validationErrors.contains("Street is required"));
        assertTrue(validationErrors.contains("City is required"));
        assertTrue(validationErrors.contains("State is required"));
        assertTrue(validationErrors.contains("Zip code is required"));
        assertTrue(validationErrors.contains("Not a valid credit card number"));
        assertTrue(validationErrors.contains("Must be formatted MM/YY"));
        assertTrue(validationErrors.contains("Invalid CVV"));
    }

    private List<String> getValidationErrorTexts() {

        List<WebElement> validationErrorElements = browser.findElementsByClassName("validationError");
        List<String> validationErrors = validationErrorElements.stream()
                .map(el -> el.getText())
                .collect(Collectors.toList());

        return validationErrors;
    }

    private void assertValidationErrorsOnInvalidOrder() {

        assertEquals(orderDetailsPageUrl(), browser.getCurrentUrl());

        List<String> validationErrors = getValidationErrorTexts();
        assertEquals(4, validationErrors.size());
        assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
        assertTrue(validationErrors.contains("Not a valid credit card number"));
        assertTrue(validationErrors.contains("Must be formatted MM/YY"));
        assertTrue(validationErrors.contains("Invalid CVV"));
    }



    /*
    Browser test action methods
     */

    private void clickDesignATaco() {

        assertEquals(homePageUrl(), browser.getCurrentUrl());

        //click
        browser.findElementByCssSelector("a[id='design']").click();
    }

    private void clickBuildAnotherTaco() {

        assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));

        //click
        browser.findElementByCssSelector("a[id='another']").click();
    }

    private void buildAndSubmitATaco(String name, String... ingredients) {

        assertEquals(designPageUrl(), browser.getCurrentUrl());

        for (String ingredient : ingredients) {
            browser.findElementByCssSelector("input[value='" + ingredient + "']").click();
        }

        browser.findElementByCssSelector("input#name").sendKeys(name);

        //submit
        browser.findElementByCssSelector("form").submit();
    }


    private void fillInAndSubmitOrderForm() {

        assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));

        findElementByCssSelector_clear_sendKeys("input#deliveryName", "Ima Hungry");
        findElementByCssSelector_clear_sendKeys("input#deliveryStreet", "1234 Culinary Blvd.");
        findElementByCssSelector_clear_sendKeys("input#deliveryCity", "Foodsville");
        findElementByCssSelector_clear_sendKeys("input#deliveryState", "CO");
        findElementByCssSelector_clear_sendKeys("input#deliveryZip", "81019");
        findElementByCssSelector_clear_sendKeys("input#ccNumber", "4111111111111111");
        findElementByCssSelector_clear_sendKeys("input#ccExpiration", "10/19");
        findElementByCssSelector_clear_sendKeys("input#ccCVV", "123");

        //submit
        browser.findElementByCssSelector("form").submit();
    }

    private void findElementByCssSelector_clear_sendKeys(String fieldName, String value) {

        WebElement field = browser.findElementByCssSelector(fieldName);
        field.clear();
        field.sendKeys(value);
    }


    private void submitEmptyOrderForm() {

        assertEquals(currentOrderDetailsPageUrl(), browser.getCurrentUrl());

        //submit
        browser.findElementByCssSelector("form").submit();

    }


    private void submitInvalidOrderForm() {

        assertTrue(browser.getCurrentUrl().startsWith(orderDetailsPageUrl()));

        findElementByCssSelector_clear_sendKeys("input#deliveryName", "I");
        findElementByCssSelector_clear_sendKeys("input#deliveryStreet", "1");
        findElementByCssSelector_clear_sendKeys("input#deliveryCity", "F");
        findElementByCssSelector_clear_sendKeys("input#deliveryState", "C");
        findElementByCssSelector_clear_sendKeys("input#deliveryZip", "8");
        findElementByCssSelector_clear_sendKeys("input#ccNumber", "1234432112344322");
        findElementByCssSelector_clear_sendKeys("input#ccExpiration", "14/91");
        findElementByCssSelector_clear_sendKeys("input#ccCVV", "1234");

        //submit
        browser.findElementByCssSelector("form").submit();

    }



    /*
    URL helper methods
     */

    private String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    private String designPageUrl() {
        return homePageUrl() + "design";
    }

    private String orderDetailsPageUrl() {
        return homePageUrl() + "orders";
    }

    private String currentOrderDetailsPageUrl() {
        return homePageUrl() + "orders/current";
    }

}