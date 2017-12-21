package com.register.pages;

import java.io.File;
import java.util.HashMap;

import org.apache.http.util.Args;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;

public class UI {
 
 private ChromeDriver driver = null;
 
 public ChromeDriver  getChromeDriver(String[] args){
	 String downloadFilepath = null;
	 File directorio= null;
	 String route= null;
if(driver == null)

//Aqui creamos un chrome preferences para decirle que descargue el archivo en la carpeta que nosotros queremos
route=System.getProperty("user.home");
directorio = new File(args[8]);
directorio.mkdirs();
downloadFilepath = directorio.toString();
HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
chromePrefs.put("profile.default_content_settings.popups", 1);
chromePrefs.put("download.default_directory", downloadFilepath);
ChromeOptions options = new ChromeOptions();
options.addArguments("port=4444"); 
options.setExperimentalOption("prefs", chromePrefs);
DesiredCapabilities cap = DesiredCapabilities.chrome();
cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//Lo pasamos a un desired capabilities y luego se lo enviamos al driver por medio del "cap"
cap.setCapability(ChromeOptions.CAPABILITY, options);
driver = new ChromeDriver(cap);
  
  return driver;
 } 

 public LoginPage getLandingPage(){
  return PageFactory.initElements(driver, LoginPage.class);
 } 
}