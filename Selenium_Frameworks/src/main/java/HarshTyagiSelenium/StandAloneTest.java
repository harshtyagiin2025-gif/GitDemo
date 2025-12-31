//https://rahulshettyacademy.com/client

package HarshTyagiSelenium;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.Assert;



public class StandAloneTest {

	public static void main(String[] args) {
		String productname = "ZARA COAT 3";
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://rahulshettyacademy.com/client");
		driver.findElement(By.id("userEmail")).sendKeys("harsh.tyagi.in2025@gmail.com");
		driver.findElement(By.id("userPassword")).sendKeys("Harsh@123");
		driver.findElement(By.id("login")).click();
		
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Wait till products are visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3")));

	    List <WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
	  WebElement prod =  products.stream().filter(product->product.findElement(By.cssSelector("b")).getText().equals(productname)).findFirst().orElse(null);
	  
	 
	  Assert.assertNotNull("Product not found", prod);
      prod.findElement(By.cssSelector(".card-body button:last-of-type")).click();


	  
	  // Wait for toast message and animation
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-container")));
      wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ng-animating")));

      // Go to Cart
      driver.findElement(By.cssSelector("[routerlink*='cart']")).click();

      // Verify product in cart
      List<WebElement> cartProducts =
              driver.findElements(By.cssSelector(".cartSection h3"));

      boolean match = cartProducts.stream()
              .anyMatch(cartProduct ->
                      cartProduct.getText().equalsIgnoreCase(productname));


      Assert.assertTrue("Product not present in cart", match);


      // Checkout
      driver.findElement(By.cssSelector(".totalRow button")).click();

      // Select country
      Actions a = new Actions(driver);
      a.sendKeys(driver.findElement(By.cssSelector("[placeholder='Select Country']")),
              "india").build().perform();

      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-results")));
      driver.findElement(By.xpath("(//button[contains(@class,'ta-item')])[2]")).click();

      // Submit Order
      driver.findElement(By.cssSelector(".action__submit")).click();

      // Confirmation message
      String confirmMessage =
              driver.findElement(By.cssSelector(".hero-primary")).getText();
      Assert.assertTrue(
    		    "Order not placed successfully",
    		    confirmMessage.equalsIgnoreCase("THANKYOU FOR THE ORDER.")
    		);


      driver.quit();
 


	}

}
