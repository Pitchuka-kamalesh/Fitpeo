package org.fitpeo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class RevenueCalculatorPage {
    WebDriver driver;
    Actions action;
    TestUtils testUtils;

    @FindBy(xpath = "//div[@class= 'MuiBox-root css-79elbk']")
    WebElement medicareEligiblePatients_Div;

    @FindBy(xpath = "//div[@class='MuiBox-root css-j7qwjs']")
    WebElement totalIndividualPatientPerMonth;

    @FindBy(xpath = "//input[@type='range']")
    WebElement sliderInput;

    @FindBy(id = ":R57alklff9da:")
    WebElement sliderInputBox;

    @FindBy(xpath = "//div[@class='MuiBox-root css-1p19z09']")
    WebElement scrollIntoViewElement;

    @FindBy(xpath = "//p[@class='MuiTypography-root MuiTypography-body2 inter css-1xroguk'][4]")
    WebElement heading;

    @FindBy(xpath = "//p[@class='MuiTypography-root MuiTypography-body2 inter css-1xroguk'][4]//p")
    WebElement amount;

    public RevenueCalculatorPage(WebDriver driver) {
        this.driver = driver;
        this.action = new Actions(driver);
        this.testUtils = new TestUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public int calculateSliderOffset(int sliderSetValue, int sliderStartingValue, int sliderMaxValue, int sliderMinValue, int widthOfSlider) {
        double proportionToTarget = (double) (sliderSetValue - sliderMinValue) / (sliderMaxValue - sliderMinValue);
        int xOffSetTarget = (int) Math.round(proportionToTarget * widthOfSlider);
        double proportionToStart = (double) (sliderStartingValue - sliderMinValue) / (sliderMaxValue - sliderMinValue);
        int xOffSetToStart = (int) Math.round(proportionToStart * widthOfSlider);
        return Math.abs(xOffSetTarget - xOffSetToStart);
    }

    public void moveToSliderAndSetValue(int sliderSetValue) {
        testUtils.waitForElement(totalIndividualPatientPerMonth, 10);
        if (totalIndividualPatientPerMonth.isDisplayed()) {
            testUtils.scrollIntoView(medicareEligiblePatients_Div);
        }

        int sliderMaxValue = 2000;
        int sliderMinValue = 0;
        int sliderStartingValue = 200;
        int widthOfSlider = 300;

        int activalOffSet = calculateSliderOffset(sliderSetValue, sliderStartingValue, sliderMaxValue, sliderMinValue, widthOfSlider);
        action.clickAndHold(sliderInput).moveByOffset(activalOffSet, 0).perform();
        int sliderChangeValue = Integer.parseInt(testUtils.getAttribute(sliderInput, "value", 10));
        int correction = Math.abs(sliderSetValue - sliderChangeValue);
        for (int keyPress = 0; keyPress < correction; keyPress++) {
            action.sendKeys(Keys.ARROW_RIGHT).build().perform();
        }
    }

    public void setInputBoxValue(int value) {
        testUtils.waitForElement(sliderInputBox, 10);
        sliderInputBox.clear();
        sliderInputBox.sendKeys(String.valueOf(value));
    }

    public int getSliderInputValue() {
        return Integer.parseInt(testUtils.getAttribute(sliderInput, "value", 10));
    }

    public void scrollToElement() {
        testUtils.scrollIntoView(scrollIntoViewElement);
    }

    public int getRecurringReimbursement(String[] coverageSelected) {
        int sumRecurringReimbursement = 0;
        for (String coverage : coverageSelected) {
            WebElement medicalCoverage = driver.findElement(By.xpath("//p[text() = '" + coverage + "']/.."));
            WebElement recurringTag = driver.findElement(By.xpath("//p[text() = '" + coverage + "']/..//div[@class='MuiChip-root MuiChip-outlined MuiChip-sizeMedium MuiChip-colorSuccess MuiChip-outlinedSuccess css-19fhy1m']//span"));

            testUtils.waitForElement(recurringTag, 10);

            if (testUtils.getText(recurringTag, 10).equals("Recurring in 30 days")) {
                sumRecurringReimbursement += Integer.parseInt(testUtils.getText(driver.findElement(By.xpath("//p[text() = '" + coverage + "']/..//span[@class='MuiTypography-root MuiTypography-body1 MuiFormControlLabel-label css-1s3unkt']")), 10));
            }
            medicalCoverage.findElement(By.tagName("input")).click();
        }
        return sumRecurringReimbursement;
    }

    public String getHeadingText() {
        return testUtils.getText(heading, 10).substring(0, 57);
    }

    public String getAmountText() {
        return testUtils.getText(amount, 10);
    }
}

