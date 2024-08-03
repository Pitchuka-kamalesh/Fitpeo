package org.fitpeo;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtils {
    WebDriver driver;
    JavascriptExecutor script;

    public TestUtils(WebDriver driver) {
        this.driver = driver;
        this.script = (JavascriptExecutor) driver;
    }

    public void waitForElement(WebElement element, int time) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            System.out.println("Element not found within " + time + " seconds.");
        } catch (Exception e) {
            System.out.println("An error occurred while waiting for the element: " + e.getMessage());
        }
    }

    public String getText(WebElement element, int time) {
        try {
            waitForElement(element, time);
            return element.getText();
        } catch (Exception e) {
            System.out.println("An error occurred while getting the text: " + e.getMessage());
            return "";
        }
    }

    public String getAttribute(WebElement element, String attribute, int time) {
        try {
            waitForElement(element, time);
            return element.getAttribute(attribute);
        } catch (Exception e) {
            System.out.println("An error occurred while getting the attribute: " + e.getMessage());
            return "";
        }
    }

    public void scrollIntoView(WebElement element) {
        try {
            script.executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            System.out.println("An error occurred while scrolling into view: " + e.getMessage());
        }
    }

    public void safeClick(WebElement element, int time) {
        try {
            waitForElement(element, time);
            element.click();
        } catch (TimeoutException e) {
            System.out.println("Element not found within " + time + " seconds.");
        } catch (ElementClickInterceptedException e) {
            System.out.println("Element is not clickable: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while clicking the element: " + e.getMessage());
        }
    }
}

