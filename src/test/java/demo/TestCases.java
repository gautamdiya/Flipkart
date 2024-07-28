package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.time.Duration;

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
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
      wait.until(ExpectedConditions.urlContains("flipkart"));

        // wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'✕')]"))).click();
        
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Washing Machine");
        searchBox.submit();
        
        WebElement popularityElement = driver.findElement(By.xpath("//div[text()='Popularity']"));
        popularityElement.click();
        Thread.sleep(5000);
        
        List<WebElement> ratings = driver.findElements(By.xpath("//div[@class='XQDdHH']"));
        // long count = ratings.stream()
        //         .filter(r -> {
        //             String text = r.getText();
        //             return !text.isEmpty() && Double.parseDouble(text) <= 4.0;
        //         })
        //         .count();
        int count=0;
        for(WebElement element:ratings){
            double rating=Double.parseDouble(element.getText().trim());
            if(rating<=4){
                count++;
            }
        }
        
        System.out.println("Count of items with rating <= 4 stars: " + count);
    }

    @Test
    public void testCase02() throws InterruptedException {
        driver.get("https://www.flipkart.com");
        WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
      wait.until(ExpectedConditions.urlContains("flipkart"));
        
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("iPhone");
        searchBox.submit();

        Thread.sleep(5000);
        List<WebElement> items = driver.findElements(By.xpath("//div[@class='tUxRFH']"));
        Thread.sleep(5000);
        for (WebElement item : items) {
            String discountText = item.findElement(By.xpath(".//div[@class='UkUFwK']")).getText().replaceAll("[^0-9]", "");
            // System.out.println(discountText);
            Integer Discount=Integer.parseInt(discountText);
            if (Discount > 17) {
                String title = item.findElement(By.xpath(".//div[@class='KzDlHZ']")).getText();
                System.out.println("Title: " + title + ", Discount: " + discountText + "%");
            }
        }
    }

    @Test
    public void testCase03() throws InterruptedException {
        driver.get("https://www.flipkart.com");
    Thread.sleep(5000);
    
    // Search for Coffee Mug
    WebElement searchBox = driver.findElement(By.name("q"));
    searchBox.sendKeys("Coffee Mug");
    searchBox.submit();
    Thread.sleep(5000);
    // Filter by 4 stars and above
    WebElement fourStarsAndAboveFilter = driver.findElement(By.xpath("//div[contains(text(),'4★ & above')]"));
    fourStarsAndAboveFilter.click();
    
    Thread.sleep(2000);
    // Fetch all items
    List<WebElement> items = driver.findElements(By.xpath("//div[@class='slAVV4']"));
    
    // Create a list to store items with their reviews count
    List<Item> itemList = new ArrayList<>();
    
    for (WebElement item : items) {
        try {
            // Find the number of reviews
            WebElement reviewsElement = item.findElement(By.xpath(".//span[@class='Wphh3N']"));
            String reviewsText = reviewsElement.getText().split(" ")[0].replaceAll("[^0-9]", "");
            int reviewsCount = Integer.parseInt(reviewsText);
            
            // Find the item title
            WebElement titleElement = item.findElement(By.xpath(".//a[@class='wjcEIp']"));
            String title = titleElement.getText();
            
            // Find the image URL
            WebElement imageElement = item.findElement(By.cssSelector("img"));
            String imageUrl = imageElement.getAttribute("src");
            
            // Add to the list
            itemList.add(new Item(title, imageUrl, reviewsCount));
        } catch (Exception e) {
            System.out.println("An item did not have the expected elements or had a format issue.");
        }
    }
    
    // Sort the items by reviews count in descending order and get the top 5
    itemList.sort((a, b) -> Integer.compare(b.getReviewsCount(), a.getReviewsCount()));
    List<Item> topItems = itemList.stream().limit(5).collect(Collectors.toList());
    
    // Print the title and image URL of the top 5 items
    for (Item item : topItems) {
        System.out.println("Title: " + item.getTitle() + ", Image URL: " + item.getImageUrl());
    }
    class Item {
    private String title;
    private String imageUrl;
    private int reviewsCount;
    
    public Item(String title, String imageUrl, int reviewsCount) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.reviewsCount = reviewsCount;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public int getReviewsCount() {
        return reviewsCount;
    }
}

}
class Item {
    private String title;
    private String imageUrl;
    private int reviewsCount;
    
    public Item(String title, String imageUrl, int reviewsCount) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.reviewsCount = reviewsCount;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public int getReviewsCount() {
        return reviewsCount;
    }
}
    //     driver.get("https://www.flipkart.com");
    //     WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(20));
    //   wait.until(ExpectedConditions.urlContains("flipkart"));
        
    //     WebElement searchBox = driver.findElement(By.name("q"));
    //     searchBox.sendKeys("Coffee Mug");
    //     searchBox.submit();
        
    //     WebElement filter=driver.findElement(By.xpath("//div[contains(text(),'4★ & above')]"));
    //     filter.click();
    //     Thread.sleep(5000);
        
    //     List<WebElement> items = driver.findElements(By.cssSelector("a._1fQZEK"));
    //     List<WebElement> topItems = items.stream()
    //             .sorted((item1, item2) -> {
    //                 int reviews1 = Integer.parseInt(item1.findElement(By.cssSelector("span._2_R_DZ span")).getText().replaceAll("[^0-9]", ""));
    //                 int reviews2 = Integer.parseInt(item2.findElement(By.cssSelector("span._2_R_DZ span")).getText().replaceAll("[^0-9]", ""));
    //                 return reviews2 - reviews1;
    //             })
    //             .limit(5)
    //             .collect(Collectors.toList());
        
    //     for (WebElement item : topItems) {
    //         String title = item.findElement(By.cssSelector("div._4rR01T")).getText();
    //         String imageUrl = item.findElement(By.cssSelector("img")).getAttribute("src");
    //         System.out.println("Title: " + title + ", Image URL: " + imageUrl);
            
    //     }

     
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