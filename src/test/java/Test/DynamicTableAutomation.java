package Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DynamicTableAutomation {
	WebDriver driver;
	String inputData;

	@SuppressWarnings("deprecation")
	@BeforeClass
	public void setUp() {
		// Setting up the WebDriver for Chrome
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		// Opening the webpage
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		// Setting an implicit wait for 10 seconds
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test(priority = 1)
	public void testNavigateToTableDataButton() {
		// Clicking on the "Table Data" button
		WebElement tableDataButton = driver.findElement(By.xpath("//summary[normalize-space()='Table Data']"));
		tableDataButton.click();
	}

	@Test(priority = 2)
	public void testEnterDataAndRefreshTable() {
		// Entering input data in JSON format into a textarea and refreshing the table
		inputData = "[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, "
				+ "{\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, "
				+ "{\"name\": \"Sara\", \"age\" : 42, \"gender\": \"female\"}, "
				+ "{\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, "
				+ "{\"name\": \"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]";

		WebElement inputTextBox = driver.findElement(By.xpath("//textarea[@id='jsondata']"));
		inputTextBox.clear();
		inputTextBox.sendKeys(inputData);
		//click on Refresh Button
		WebElement refreshButton = driver.findElement(By.xpath("//button[@id='refreshtable']"));
		refreshButton.click();
	}

	@Test(priority = 3)
	public void testAssertionOfData() {

		// Retrieving the data from the dynamic table
		WebElement table = driver.findElement(By.id("dynamictable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		List<String> tableData = retrieveTableData(rows);

		// Removing unwanted characters and formatting the actual data
		String actualDataString = String.join("", tableData).replaceAll("[\",\\s:{}]", "");
		System.out.println("actualDataString:" + actualDataString);

		// Preparing the expected data string
		String[] expectedDataArray = inputData.split(", ");
		List<String> expectedData = new ArrayList<>();
		for (String data : expectedDataArray) {
			String[] temp = data.split(":");
			expectedData.add(temp[1].replaceAll("[\"\\{\\}]", "").trim());
		}

		String expectedDataString = String.join("", expectedData);
		expectedDataString = expectedDataString.substring(0, expectedDataString.length() - 1);
		System.out.println("expectedDataString:" + expectedDataString);

		// Performing the assertion by comparing actual and expected data strings
		Assert.assertEquals(actualDataString, expectedDataString);
	}

	// Method to retrieve table data
	public List<String> retrieveTableData(List<WebElement> rows) {
		List<String> tableData = new ArrayList<>();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			StringBuilder rowData = new StringBuilder();
			for (WebElement cell : cells) {
				rowData.append(cell.getText()).append(", ");
			}
			tableData.add(rowData.toString());
		}
		return tableData;
	}

	@AfterClass
	public void tearDown() {
		
		driver.quit();
	}
}
