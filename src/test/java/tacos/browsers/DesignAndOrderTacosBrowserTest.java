package tacos.browsers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DesignAndOrderTacosBrowserTest {

    private static HtmlUnitDriver unitDriver;

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void setup() {

        unitDriver = new HtmlUnitDriver();
        unitDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterAll
    public static void closeBrowser() {
        unitDriver.quit();
    }

    @Test
    public void testDesignATacoPage_HappyPath() throws Exception {

        //homePageUrl
        unitDriver.get(homePageUrl());

        clickDesignATaco();

        assertDesignPageElements();

        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");

        clickBuildAnotherTaco();

        buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR");

        fillInAndSubmitOrderForm();

        //homePageUrl
        assertThat(unitDriver.getCurrentUrl()).isEqualTo(homePageUrl());
    }

    @Test
    public void testDesignATacoPage_EmptyOrderInfo() throws Exception {

        //homePageUrl
        unitDriver.get(homePageUrl());

        clickDesignATaco();

        assertDesignPageElements();

        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");

        submitEmptyOrderForm();

        fillInAndSubmitOrderForm();

        //homePageUrl
        assertThat(unitDriver.getCurrentUrl()).isEqualTo(homePageUrl());
    }

    @Test
    public void testDesignATacoPage_InvalidOrderInfo() throws Exception {

        //homePageUrl
        unitDriver.get(homePageUrl());

        clickDesignATaco();

        assertDesignPageElements();

        buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");

        submitInvalidOrderForm();

        fillInAndSubmitOrderForm();

        //homePageUrl
        assertThat(unitDriver.getCurrentUrl()).isEqualTo(homePageUrl());
    }

    // ===Browser test action methods

    private void clickDesignATaco() {

        //homePageUrl
        assertThat(unitDriver.getCurrentUrl()).isEqualTo(homePageUrl());

        //<a href="/design" id="design">Design a new taco!</a>
        unitDriver.findElement(new By.ByCssSelector("a[id='design']")).click();
    }

    private void assertDesignPageElements() {

        //designPageUrl
        assertThat(unitDriver.getCurrentUrl()).isEqualTo(designPageUrl());

        List<WebElement> ingredientGroups = unitDriver.findElements(new By.ByClassName("ingredient-group"));

        assertThat(ingredientGroups.size()).isEqualTo(5);

        //wraps
        WebElement wrapGroup = unitDriver.findElement(new By.ByCssSelector("div.ingredient-group#wraps"));
        {
            List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));

            assertThat(wraps.size()).isEqualTo(2);
            assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla");
            assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla");
        }

        //proteins
        WebElement proteinGroup = unitDriver.findElement(new By.ByCssSelector("div.ingredient-group#proteins"));
        {
            List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));

            assertThat(proteins.size()).isEqualTo(2);
            assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef");
            assertIngredient(proteinGroup, 1, "CARN", "Carnitas");
        }

        //cheeses
        WebElement cheeseGroup = unitDriver.findElement(new By.ByCssSelector("div.ingredient-group#cheeses"));
        {
            List<WebElement> cheeses = proteinGroup.findElements(By.tagName("div"));

            assertThat(cheeses.size()).isEqualTo(2);
            assertIngredient(cheeseGroup, 0, "CHED", "Cheddar");
            assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack");
        }

        //veggies
        WebElement veggieGroup = unitDriver.findElement(new By.ByCssSelector("div.ingredient-group#veggies"));
        {
            List<WebElement> veggies = proteinGroup.findElements(By.tagName("div"));

            assertThat(veggies.size()).isEqualTo(2);
            assertIngredient(veggieGroup, 0, "TMTO", "Diced Tomatoes");
            assertIngredient(veggieGroup, 1, "LETC", "Lettuce");
        }

        //sauces
        WebElement sauceGroup = unitDriver.findElement(new By.ByCssSelector("div.ingredient-group#sauces"));
        {
            List<WebElement> sauces = proteinGroup.findElements(By.tagName("div"));

            assertThat(sauces.size()).isEqualTo(2);
            assertIngredient(sauceGroup, 0, "SLSA", "Salsa");
            assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream");
        }
    }


    private void assertIngredient(WebElement ingredientGroup, int ingredientIdx, String id, String name) {

        List<WebElement> elements = ingredientGroup.findElements(By.tagName("div"));
        WebElement ingredient = elements.get(ingredientIdx);

        assertThat(ingredient.findElement(By.tagName("input")).getAttribute("value")).isEqualTo(id);
        assertThat(ingredient.findElement(By.tagName("span")).getText()).isEqualTo(name);
    }


    private void buildAndSubmitATaco(String name, String... ingredients) {

        assertDesignPageElements();

        //ingredients
        for (String ingredient : ingredients) {
            unitDriver.findElement(new By.ByCssSelector("input[value='" + ingredient + "']")).click();
        }

        //name
        unitDriver.findElement(new By.ByCssSelector("input#name")).sendKeys(name);

        //submit
        unitDriver.findElement(new By.ByCssSelector("form")).submit();
    }


    private void clickBuildAnotherTaco() {

        //startsWith orders
        assertThat(unitDriver.getCurrentUrl()).startsWith(ordersPageUrl());

        //<a id="another" href="/design">Design another taco!</a>
        unitDriver.findElement(new By.ByCssSelector("a[id='another']")).click();
    }


    private void fillInAndSubmitOrderForm() {

        //startsWith orders
        assertThat(unitDriver.getCurrentUrl()).startsWith(ordersPageUrl());

        //delivery info
        fillField("input#deliveryName", "Ima Hungry");
        fillField("input#deliveryStreet", "1234 Culinary Blvd.");
        fillField("input#deliveryCity", "Foodsville");
        fillField("input#deliveryState", "CO");
        fillField("input#deliveryZip", "81019");

        //payment info
        fillField("input#ccNumber", "4111111111111111");
        fillField("input#ccExpiration", "10/23");
        fillField("input#ccCVV", "123");

        //submit
        unitDriver.findElement(new By.ByCssSelector("form")).submit();
    }


    private void fillField(String fieldName, String value) {

        WebElement field = unitDriver.findElement(new By.ByCssSelector(fieldName));
        field.clear();
        field.sendKeys(value);
    }

    private void submitEmptyOrderForm() {

        //ordersCurrentPageUrl
        assertThat(unitDriver.getCurrentUrl()).isEqualTo(ordersCurrentPageUrl());

        //submit
        unitDriver.findElement(new By.ByCssSelector("form")).submit();

        //ordersPageUrl
        assertThat(unitDriver.getCurrentUrl()).isEqualTo(ordersPageUrl());

        List<String> validationErrors = getValidationErrorTexts();

        assertThat(validationErrors.size()).isEqualTo(9);
        assertThat(validationErrors)

                .containsExactlyInAnyOrder("Please correct the problems below and resubmit.",

                        "Delivery name is required",

                        "Street is required",

                        "City is required",

                        "State is required",

                        "Zip code is required",

                        "Not a valid credit card number",

                        "Must be formatted MM/YY",

                        "Invalid CVV");
    }

    private List<String> getValidationErrorTexts() {

        List<WebElement> validationErrorElements = unitDriver.findElements(new By.ByClassName("validationError"));

        List<String> validationErrors = validationErrorElements.stream()

                .map(el -> el.getText())

                .collect(Collectors.toList());

        return validationErrors;
    }

    private void submitInvalidOrderForm() {

        //startsWith orders
        assertThat(unitDriver.getCurrentUrl()).startsWith(ordersPageUrl());

        {
            //delivery info
            fillField("input#deliveryName", "I");
            fillField("input#deliveryStreet", "1");
            fillField("input#deliveryCity", "F");
            fillField("input#deliveryState", "C");
            fillField("input#deliveryZip", "8");

            //payment info
            fillField("input#ccNumber", "1234432112344322");
            fillField("input#ccExpiration", "14/91");
            fillField("input#ccCVV", "1234");
        }

        //submit
        unitDriver.findElement(new By.ByCssSelector("form")).submit();

        //ordersPageUrl
        assertThat(unitDriver.getCurrentUrl()).isEqualTo(ordersPageUrl());

        List<String> validationErrors = getValidationErrorTexts();

        assertThat(validationErrors.size()).isEqualTo(4);
        assertThat(validationErrors)

                .containsExactlyInAnyOrder("Please correct the problems below and resubmit.",

                        "Not a valid credit card number",

                        "Must be formatted MM/YY",

                        "Invalid CVV");
    }


    // ===URL helper methods

    private String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    private String designPageUrl() {
        return homePageUrl() + "design";
    }

    private String ordersPageUrl() {
        return homePageUrl() + "orders";
    }

    private String ordersCurrentPageUrl() {
        return homePageUrl() + "orders/current";
    }

}
