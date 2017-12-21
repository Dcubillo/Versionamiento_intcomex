package com.register.data;

import static com.jayway.restassured.RestAssured.given;
import java.awt.AWTException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;

import org.openqa.selenium.chrome.ChromeDriver;

import com.register.pages.LoginPage;
import com.register.pages.MainMenu;
import com.register.pages.UI;

import jxl.read.biff.BiffException;

public class sssssssssss {

public static void main( String[] args )throws InterruptedException, AWTException, IOException, BiffException, MessagingException{
		
	
		ChromeDriver driver = null;
		LoginPage loginPage = null;
		UI ui = null;
		
		Properties p = new Properties();
		FileInputStream  conf = new FileInputStream(args[0]);
		//FileInputStream  conf = new FileInputStream("C:\\Users\\Dcubillo\\workspace\\testingsoft.register\\automation.properties");
		p.load(conf);
		Constants.setDomain(p.getProperty( "domain" ).toString());
		Constants.setusername(p.getProperty("user").toString());
		Constants.setpassword(p.getProperty("password").toString());
		//Constants.setemail_username(p.getProperty("mail_username").toString());
		System.setProperty("webdriver.chrome.driver", args[11]);
		ui = new UI();
		driver = ui.getChromeDriver(args);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.get(Constants.DOMAIN);
		loginPage = ui.getLandingPage();
		
		
		LoginPage Login;
		MainMenu Main = new MainMenu();
		
		Main.fill_account_fields(driver);
			loginPage
			.fullLogin(driver);	
		Main.download_files(driver, args);
		Main.enviarcorreo_productos_no_disponibles(driver, args);
		Main.comparar_archivos(driver, args);
		Main.Update_de_Precios(driver, args);
		//Main.actualizar_archivo_de_precios(driver, args);
		Main.borrando_carpetas(driver, args);
		
		//driver.close();
			
	}		

}
