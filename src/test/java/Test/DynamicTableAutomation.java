package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DynamicTableAutomation {
	private WebDriver driver;

	@SuppressWarnings("deprecation")
	@BeforeTest
	public void setup() {
		// Setup WebDriver for Chrome
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(); // Initialize ChromeDriver
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html"); // Open the URL
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // Set implicit wait
	}

	@Test
	public void testDynamicTableAutomation() throws InterruptedException {
		// Click on Table Data button
		WebElement tableDataButton = driver.findElement(By.xpath("//summary[normalize-space()='Table Data']"));
		tableDataButton.click();

		// Insert the data
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

		Thread.sleep(5000);

		// Store the table data
		List<WebElement> rows = driver.findElements(By.xpath("//table[@id='dynamictable']//tr"));
		List<String> tableData = retrieveTableData(rows);

		// Expected data
		String expectedData = "[{\"name\":\"Bob\",\"age\":20,\"gender\":\"male\"}, "
				+ "{\"name\":\"George\",\"age\":42,\"gender\":\"male\"}, "
				+ "{\"name\":\"Sara\",\"age\":42,\"gender\":\"female\"}, "
				+ "{\"name\":\"Conor\",\"age\":40,\"gender\":\"male\"}, "
				+ "{\"name\":\"Jennifer\",\"age\":42,\"gender\":\"female\"}]";

		// Assertions to check if the data matches
		Assert.assertEquals(tableData.get(0).trim(), expectedData);
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

	public List<String> retrieveTableData(List<WebElement> rows) {
		List<String> tableData = new ArrayList<>();

		for (WebElement row : rows) { // Iterate through each table row
			List<WebElement> cells = row.findElements(By.tagName("td"));

			StringBuilder rowData = new StringBuilder(); // StringBuilder to store cell data

			for (WebElement cell : cells) { // Iterate through each cell in the row
				// Appending cell data to rowData string
				rowData.append(cell.getText()).append(", ");
			}

			tableData.add(rowData.toString().trim()); // Add row data to the list

			// Printing each row's data for verification
			System.out.println("Row Data: " + rowData);
		}
		return tableData;
	}
}
