package com.register.data;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;

import com.register.pages.LoginPage;
import com.register.pages.UI;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

public class PageUtils {
	//protected FirefoxDriver driver = null;
	protected static LoginPage loginPage = null;
	protected static UI u = null;

	public static void moveTo(WebElement element, WebDriver driver2) {
		Actions builder = new Actions(driver2);
		builder.moveToElement(element).perform();
	}

	public static void selectOptionInDropDownByVisibleText(WebElement element, String TextOptionToSelect) {
		try {
			Select select = new Select(element);
			select.selectByVisibleText(TextOptionToSelect);

		} catch (NoSuchElementException e) {
			System.out.println("Option value not find in dropdown");
		}
	}

	public static void selectOptionInDropDownByValue(WebElement element, String value) {

		Select select = new Select(element);
		try {

			select.selectByValue(value);

		} catch (NoSuchElementException e) {

			System.out.println("Option value not find in dropdown");
		}
	}

	public static boolean isElementPresent(WebDriver driver, By by) {

		return isElementPresent(driver, by, 5);
	}

	public static boolean isElementPresent(WebDriver driver, By by, int waitSeconds) {

		try {
			driver.manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
			driver.findElement(by);
			driver.manage().timeouts().implicitlyWait(Constants.TIME_OUT, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			driver.manage().timeouts().implicitlyWait(Constants.TIME_OUT, TimeUnit.SECONDS);
			return false;
		}
	}

	public static boolean isElementPresent(WebDriver driver, WebElement element) {

		return isElementPresent(driver, element, 5);

	}

	public static boolean isElementPresent(WebDriver driver, WebElement element, int waitSeconds) {

		try {
			driver.manage().timeouts().implicitlyWait(waitSeconds, TimeUnit.SECONDS);
			element.isDisplayed();
			driver.manage().timeouts().implicitlyWait(Constants.TIME_OUT, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void manualPageRefresh(WebDriver driver) {
		driver.navigate().refresh();
	}

	public static boolean isElementDisplayed(WebDriver driver, WebElement element) {
		return isElementPresent(driver, element, 5);
	}

	public static boolean isElementDisplayed(WebDriver driver, By by) {
		try {
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			WebElement element = driver.findElement(by);
			element.isDisplayed();
			driver.manage().timeouts().implicitlyWait(Constants.TIME_OUT, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			driver.manage().timeouts().implicitlyWait(Constants.TIME_OUT, TimeUnit.SECONDS);
			return false;
		}
	}

}