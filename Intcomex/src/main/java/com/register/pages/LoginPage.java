package com.register.pages;


import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.register.data.Constants;
import com.register.data.Page;



//import com.ts.commons.RaceConditions.WaitTool;


public class LoginPage extends Page{

	
	public LoginPage then(){
		return this;
	}
	
	public LoginPage and(){
		return this;
	}
	@FindBy(xpath = "//input[@id='User']")
	private WebElement user;
	
	@FindBy(xpath = "//input[@id='Password']")
	private WebElement password;
	
	@FindBy(xpath = "//button[@type='submit']")
	private WebElement Iniciar_sesion_btn;
		
	public MainMenu fullLogin(ChromeDriver driver){
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
			
		//WaitTool.waitForElementNotPresentAndVisible(driver, user);
			//boolean ispresent = user.isDisplayed();
			//Assert.assertTrue(ispresent, "No se logra encontrar el xpath del user");
		//JavascriptExecutor jse = (JavascriptExecutor) driver;
		//jse.executeScript("document.getElementById('User').value = 'XCRNCU4333';");
		//driver.findElement(By.id("User")).sendKeys("XCRNCU4333");
		//document.getElementById("User").value="XCRNCU4333"; 
			user.sendKeys(Constants.Username);
		//driver.findElement(By.id("Password")).sendKeys("0Ctubre2016");
			password.sendKeys(Constants.Password);
			

			//TODO replace executeScript for JavaScriptExcecutor		
			Iniciar_sesion_btn.click();
		boolean ispresent= driver.findElement(By.xpath("(//span[contains(.,'Mi Cuenta')])[2]")).isDisplayed();
        Assert.assertTrue(ispresent, "El usuario no se logueado satisfactoriamente");
        System.out.println("Logueado Correctamente");
		
		
				return PageFactory.initElements(driver, MainMenu.class);
	}
	public boolean isElementDisplayed(WebDriver driver, By selector)
	{
		try
		{
			driver.findElement(selector);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
		
	}
	/*public LoginPage clear(){
		user.clear();
		pass.clear();
		return this;
	}*/

	/*----------------------------------VALIDATIONS----------------------------------*/

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
}