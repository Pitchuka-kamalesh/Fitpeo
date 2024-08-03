package org.fitpeo;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class RevenueCalculatorTest {
    WebDriver driver;
    RevenueCalculatorPage revenueCalculatorPage;
    TestUtils testUtils;

    @BeforeClass
    public void setUp() {
        // Driver set up
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.get("https://www.fitpeo.com/revenue-calculator");

        testUtils = new TestUtils(driver);
        revenueCalculatorPage = new RevenueCalculatorPage(driver);
    }

    @Test
    public void testOpenRevenueCalculator() {
        try {
            WebElement revenueCalculator = driver.findElement(By.linkText("Revenue Calculator"));
            testUtils.safeClick(revenueCalculator, 20); // Waits for the element and clicks on it
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test(dependsOnMethods = "testOpenRevenueCalculator")
    public void testSliderFunctionality() {
        try {
            revenueCalculatorPage.moveToSliderAndSetValue(820);
            int sliderChangeAfterCorrectionValue = revenueCalculatorPage.getSliderInputValue();
            Assert.assertEquals(sliderChangeAfterCorrectionValue, 820, "Both the values are not equal");
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test(dependsOnMethods = "testSliderFunctionality")
    public void testInputBoxFunctionality() {
        try {
            revenueCalculatorPage.setInputBoxValue(520);
            int sliderInputBoxValue = revenueCalculatorPage.getSliderInputValue();
            Assert.assertEquals(sliderInputBoxValue, 520, "Both the values are not equal");
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test(dependsOnMethods = "testInputBoxFunctionality")
    public void testCoverageSelection() {
        try {
            revenueCalculatorPage.setInputBoxValue(820);
            revenueCalculatorPage.scrollToElement();
            String[] coverageSelected = new String[]{"CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474"};
            int sumRecurringReimbursement = revenueCalculatorPage.getRecurringReimbursement(coverageSelected);
            Assert.assertEquals(sumRecurringReimbursement,135,"Recurring Reimbursement sum mismatch");

            Assert.assertEquals(revenueCalculatorPage.getHeadingText(),"Total Recurring Reimbursement for all Patients Per Month:","There is mismatch in header heading");
            Assert.assertEquals(revenueCalculatorPage.getAmountText(),"$110700","Amount mismatch ");
        } catch (Exception e) {
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
