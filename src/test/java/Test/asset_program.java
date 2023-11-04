package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class asset_program {

    @SuppressWarnings("deprecation")
    @Test
    public void testDynamicTableAutomation() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(); // Initialize ChromeDriver
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Click on Table Data button
        WebElement tableDataButton = driver.findElement(By.xpath("//summary[normalize-space()='Table Data']"));
        tableDataButton.click();

        // Insert the following data in the input text box
        String inputData = "[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, "
                + "{\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, "
                + "{\"name\": \"Sara\", \"age\" : 42, \"gender\": \"female\"}, "
                + "{\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, "
                + "{\"name\": \"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]";
        WebElement inputTextBox = driver.findElement(By.xpath("//textarea[@id='jsondata']"));
        inputTextBox.clear();
        inputTextBox.sendKeys(inputData);

        // Click on Refresh Table button
        WebElement refreshButton = driver.findElement(By.xpath("//button[@id='refreshtable']"));
        refreshButton.click();

        // Waiting for the table to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//table[@id='dynamictable']//tr")));

        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='dynamictable']//tr"));

        // Store the table data
        List<String> tableData = retrieveTableData(rows);

        // Prepare expected data
        String expectedData = "[{\"name\":\"Bob\",\"age\":20,\"gender\":\"male\"}, "
                + "{\"name\":\"George\",\"age\":42,\"gender\":\"male\"}, "
                + "{\"name\":\"Sara\",\"age\":42,\"gender\":\"female\"}, "
                + "{\"name\":\"Conor\",\"age\":40,\"gender\":\"male\"}, "
                + "{\"name\":\"Jennifer\",\"age\":42,\"gender\":\"female\"}]";

        // Assertions to check if the data matches
        Assert.assertTrue(tableData.contains(expectedData));

        driver.quit();
    }

    private List<String> retrieveTableData(List<WebElement> rows) {
        List<String> tableData = new ArrayList<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            StringBuilder rowData = new StringBuilder();
            for (WebElement cell : cells) {
                rowData.append(cell.getText()).append(", ");
            }
            tableData.add(rowData.toString().trim());
        }
        return tableData;
    }
}
