package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */
    @Test
    public void testCase01() throws InterruptedException {
        driver.get("https://www.flipkart.com");
        Thread.sleep(5000);
        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'✕')]"))).click();
        
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Washing Machine");
        searchBox.submit();
        
        WebElement popularityElement = driver.findElement(By.xpath("//div[text()='Popularity']"));
        popularityElement.click();
        Thread.sleep(5000);
        
        List<WebElement> ratings = driver.findElements(By.cssSelector("div._3LWZlK"));
        long count = ratings.stream()
                .filter(r -> {
                    String text = r.getText();
                    return !text.isEmpty() && Double.parseDouble(text) <= 4.0;
                })
                .count();
        
        System.out.println("Count of items with rating <= 4 stars: " + count);
    }

    @Test
    public void testCase02() throws InterruptedException {
        driver.get("https://www.flipkart.com");
        Thread.sleep(5000);
        
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("iPhone");
        searchBox.submit();
        
        List<WebElement> items = driver.findElements(By.cssSelector("a._1fQZEK"));
        for (WebElement item : items) {
            String discountText = item.findElement(By.cssSelector("div._3Ay6Sb span")).getText().replaceAll("[^0-9]", "");
            if (!discountText.isEmpty() && Integer.parseInt(discountText) > 17) {
                String title = item.findElement(By.cssSelector("div._4rR01T")).getText();
                System.out.println("Title: " + title + ", Discount: " + discountText + "%");
            }
        }
    }

    @Test
    public void testCase03() throws InterruptedException {
        driver.get("https://www.flipkart.com");
        Thread.sleep(5000);
        
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Coffee Mug");
        searchBox.submit();
        
        WebElement filter=driver.findElement(By.xpath("//div[contains(text(),'4★ & above')]"));
        filter.click();
        Thread.sleep(5000);
        
        List<WebElement> items = driver.findElements(By.cssSelector("a._1fQZEK"));
        List<WebElement> topItems = items.stream()
                .sorted((item1, item2) -> {
                    int reviews1 = Integer.parseInt(item1.findElement(By.cssSelector("span._2_R_DZ span")).getText().replaceAll("[^0-9]", ""));
                    int reviews2 = Integer.parseInt(item2.findElement(By.cssSelector("span._2_R_DZ span")).getText().replaceAll("[^0-9]", ""));
                    return reviews2 - reviews1;
                })
                .limit(5)
                .collect(Collectors.toList());
        
        for (WebElement item : topItems) {
            String title = item.findElement(By.cssSelector("div._4rR01T")).getText();
            String imageUrl = item.findElement(By.cssSelector("img")).getAttribute("src");
            System.out.println("Title: " + title + ", Image URL: " + imageUrl);
            
        }

        }
     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}