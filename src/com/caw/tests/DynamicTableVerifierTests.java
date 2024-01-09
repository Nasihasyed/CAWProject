package com.caw.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Test class for verifying Dynamic table functionality
 */
public class DynamicTableVerifierTests {

    private WebDriver driver;

    /**
     * Setup method for initializing the webdriver and navigate to respective page
     */
    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\selenium\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
    }

    /**
     * Test method to verify the table content and inserted values
     */
    @Test
    public void testDynamicTableVerification() {
        try{

            WebElement tableDataButton = driver.findElement(By.xpath("//summary[text()='Table Data']"));
            tableDataButton.click();

            JsonArray jsonData = new JsonArray();
            jsonData.add(createJsonObject("Bob", 20, "male"));
            jsonData.add(createJsonObject("George", 42, "male"));
            jsonData.add(createJsonObject("Sara", 42, "female"));
            jsonData.add(createJsonObject("Conor", 40, "male"));
            jsonData.add(createJsonObject("Jennifer", 42, "female"));

            WebElement jsonDataInputBox = driver.findElement(By.id("jsondata"));
            jsonDataInputBox.clear();
            jsonDataInputBox.sendKeys(jsonData.toString());

            WebElement refreshTableButton = driver.findElement(By.id("refreshtable"));
            refreshTableButton.click();

            WebElement table = driver.findElement(By.id("dynamictable"));
            String tableText = table.getText();

            int headerRowCount = 2;
            String[] rows = tableText.split("\n");

            for (int i = headerRowCount; i < rows.length; i++) {
                String actualRow = rows[i].trim();

                if (!actualRow.isEmpty()) {
                    JsonObject person = jsonData.get(i - headerRowCount).getAsJsonObject();
                    String expectedRow = String.format("%s %s %s", person.get("name").getAsString(),
                            person.get("age").getAsString(), person.get("gender").getAsString());

                    Assert.assertTrue(tableText.contains(expectedRow),
                            "Verification failed for row: " + expectedRow);
                }
            }

        }
        catch(Exception e)
        {
            Assert.fail("An Exception occurred while running test case - "+e.getMessage());
        }
    }

    /**
     * TearDown method to close the webdriver after the test execution
     */
    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    private JsonObject createJsonObject(String name, int age, String gender) {
        JsonObject person = new JsonObject();
        person.addProperty("name", name);
        person.addProperty("age", age);
        person.addProperty("gender", gender);
        return person;
    }
}