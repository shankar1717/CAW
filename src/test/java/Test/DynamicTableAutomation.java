	package Test;

	import org.openqa.selenium.By;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.testng.Assert;
	import org.testng.annotations.Test;

	import io.github.bonigarcia.wdm.WebDriverManager;

	import java.util.ArrayList;
	import java.util.List;
	import java.util.concurrent.TimeUnit;

	public class DynamicTableAutomation {

	    @SuppressWarnings("deprecation")
		@Test
	    public void testDynamicTableAutomation() {

	        // Setup the WebDriver for Chrome
	        WebDriverManager.chromedriver().setup();
	        WebDriver driver = new ChromeDriver();

	        // Open the page
	        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");

	        // implicit wait of 10 seconds
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	        // click on the "Table Data" button
	        WebElement tableDataButton = driver.findElement(By.xpath("//summary[normalize-space()='Table Data']"));
	        tableDataButton.click();

	        // input data in JSON format
	        String inputData = "[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, "
	                + "{\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, "
	                + "{\"name\": \"Sara\", \"age\" : 42, \"gender\": \"female\"}, "
	                + "{\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, "
	                + "{\"name\": \"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]";

	        // clear the input text box, then enter the input data
	        WebElement inputTextBox = driver.findElement(By.xpath("//textarea[@id='jsondata']"));
	        inputTextBox.clear();
	        inputTextBox.sendKeys(inputData);

	        // "Refresh Table" button
	        WebElement refreshButton = driver.findElement(By.xpath("//button[@id='refreshtable']"));
	        refreshButton.click();

	        // Locate the dynamic table and its rows
	        WebElement table = driver.findElement(By.id("dynamictable"));
	        
	        List<WebElement> rows = table.findElements(By.tagName("tr"));

	        // Store the data from the table
	        List<String> tableData = retrieveTableData(rows);

	        // Split the expected data into an array
	        String[] expectedData = inputData.split("}, ");

	        // Loop through the array to fix the formatting
	        for (int i = 0; i < expectedData.length; i++) {
	            if (i != expectedData.length - 1) {
	                expectedData[i] += "}";
	            }
	        }

	        // Assertions: Check if the actual data contains the expected data
	        String actualData = String.join("", tableData).trim();
	        String expectedDataString = String.join("", expectedData).replace("},", "}").trim();

	        // Output actual and expected data
	        System.out.println("Actual: " + actualData);
	        System.out.println("Expected: " + expectedDataString);

	        // Perform the assertion
	        Assert.assertTrue(actualData.contains(expectedDataString));

	        // Close the WebDriver
	        driver.quit();
	    }

	    public List<String> retrieveTableData(List<WebElement> rows) {
	        List<String> tableData = new ArrayList<>();

	        // Loop through the rows and cells to extract table data
	        for (WebElement row : rows) {
	            List<WebElement> cells = row.findElements(By.tagName("td"));
	            StringBuilder rowData = new StringBuilder();

	            for (WebElement cell : cells) {
	                rowData.append(cell.getText()).append(", ");
	            }

	            // Add the row data to the tableData list
	            tableData.add(rowData.toString());
	        }
	        return tableData;
	    }
	}


