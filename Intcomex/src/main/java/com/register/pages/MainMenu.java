package com.register.pages;

//import io.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.given;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.register.data.Page;
import com.register.data.PageUtils;

//import com.ts.commons.WaitTool;
import jxl.read.biff.BiffException;
//extends Page
public class MainMenu  {
	
	final static Logger logger = Logger.getLogger(MainMenu.class);
	
	String Cookie=null;
	@FindBy(xpath = "(//a[contains(., 'Ingresar')])[1]")
	private WebElement IngresarBtnclick;

	@FindBy(xpath = "(//a[@role='button'])[2]")
	private WebElement software;

	@FindBy(xpath = "(//a[@title='Aplicaciones para Negocio y Oficina'])[1]")
	private WebElement Aplicaciones;

	@FindBy(xpath = "(//a[@class='font-compact priceListButtom control-label'])[2]")
	private WebElement csv_link;
	
	
	 public static boolean isElementPresent(WebDriver driver, By by) {

			return isElementPresent(driver, by);
	}
	public MainMenu fill_account_fields(ChromeDriver driver) throws InterruptedException {
		
		//Ingreso a el login
		try {
			
			driver.findElement(By.xpath("(//span[contains(., 'Ingresar')])[1]")).click();
			logger.warn("Ingresado correctamente");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO: handle exception
		}

		return PageFactory.initElements(driver, MainMenu.class);

	}
	
	public MainMenu enviarcorreo_productos_no_disponibles(ChromeDriver driver,String[] args) throws InterruptedException, IOException {
		//Thread.sleep(10000);
		String productoslog = (args[12]);
		File defaultlogfile = new File (productoslog);
		String skulog = (args[14]);
		File SKU_Log_File = new File (skulog);
		String Productos_sin_sku = (args[15]);
		File NO_SKU_File = new File (Productos_sin_sku);
		
		/////////////////////////////////////////////
		//Aqui comparo por primera vez los archivos comparando los del archivo de nosotros y los de merge de archivos de intcomex
				File directorio_archivo_maestro = null;
				File directorio_archivo_merged = null;
		
				// Obtengo la ruta del sistema y le adjunto la ruta de mas que le quiero
				// poner
				
				//Archivo Intcomex
				directorio_archivo_maestro = new File(args[4]);
				BufferedReader br = new BufferedReader(new FileReader(directorio_archivo_maestro));
				// *************************************************************

				Calendar fecha = new GregorianCalendar();
				int updated=0;
				int noupdated=0;
				int notfind=0;
				
				String Pricetoupdate = (args[5]);
				File Updatefile = new File(Pricetoupdate);
			
				String line = "";
				String line2 = "";
				Boolean Existe = false;
		if (!defaultlogfile.exists() == true) {
			Date date = new Date();
			DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
			logger.warn(hourdateFormat.format(date)
					+ " Analizando el catalogo para verificar disponibilidad de los productos");
			System.out.println(hourdateFormat.format(date)
					+ " Analizando el catalogo para verificar disponibilidad de los productos");
			while ((line = br.readLine()) != null) {
				// use comma as separator
				Existe = false;
				String[] cols = line.split(";");
				String sku = cols[2];
				String Nombre = cols[3];
				// System.out.println(cols[2]);
				Thread.sleep(5000);
				directorio_archivo_merged = new File(args[3]);
				BufferedReader br2 = new BufferedReader(
						new InputStreamReader(new FileInputStream(directorio_archivo_merged), "UTF8"));
					/// aqui tengo que ver la parte de las lineas ademas de eso buscar como cachear ewl error
				// tengo que ver cual seria el problema con el error que se presenta en las actualizaciones de las producots de incomex vamos a a ver como nos ve con esto
				//
				while ((line2 = br2.readLine()) != null && Existe == false) {

					String[] cols2 = line2.split("\\t");

					String Fullsku = "";
					int conteo = cols2.length;
					char letra;
					// aqui se hace un nuevo string porque el csv trae un 0
					// entre letras y sino no machea
					if (conteo >= 9) {
						for (int i = 0; i < cols2[7].length(); i++) {
							letra = cols2[7].charAt(i);
							if (letra != 0) {
								Fullsku = Fullsku + letra;
							}
						}
					}

					if (conteo >= 9) {
						String Precio = cols2[4];
						String Producto = cols2[2];
						String precio_concatenado = concatenar(Precio);
						String disponibilidad = cols2[5];
						if (sku.equals(Fullsku) && precio_concatenado.length()<=7) {

							String producto_concatenado = concatenar(Producto);
							// String precio_concatenado = concatenar(Precio);
							String disponibilidad_concatenada = concatenar(disponibilidad);
							Boolean Chequeado = false;
							System.out.println(hourdateFormat.format(date) + " El articulo con el codigo " + sku
									+ " y el nombre " + Nombre
									+ " esta disponible en la lista de productos de Intcomex con un precio de "
									+ precio_concatenado);
							write(productoslog, producto_concatenado, sku, precio_concatenado,
									disponibilidad_concatenada, true, 1, false);
							Existe = true;
							
							
							//System.out.println(Cookie);
							String disponibidad = disponibilidad_concatenada.replaceAll("[^0-9]","");
							//System.out.println(disponibidad);
							//System.out.println(sku);
							//System.out.println(GroovySystem.getVersion());
							
							
							final String URL_NIMBUS  = args[13]; //"https://intranet.ncubo.com/UIQA12/rest";
							write(skulog, producto_concatenado, sku, precio_concatenado,
									disponibidad, true, 5, false);
							
							final com.jayway.restassured.response.Cookie sessionCookie = new com.jayway.restassured.response.Cookie.Builder("JSESSIONID", Cookie).build();
						
							String reponse = given().
								relaxedHTTPSValidation().
								cookie(sessionCookie).
								contentType("application/json").
								body("{nuevaCantidadDelProducto:"+disponibidad+"}").
								post(URL_NIMBUS+"v_nimbus/cambiar/cantidad/producto?codigosku="+sku).
								asString();
							//v_nimbus/cambiar/cantidad/producto?codigosku=0
							//System.out.println(Cookie);
							System.out.println("response "+reponse);
							
							
							if(reponse.contains("No se encontro un articulo"))
							{
								
								//write(Productos_sin_sku, producto_concatenado, sku, precio_concatenado,
									//	disponibidad, false, 6, false);
							
							
							}
							if(reponse.contains("Se actualizo exitosamente"))
							{updated=updated+1;}
							
							
									

							
							//Meter aqui el script de nimbus//////////////////////////////////////////////////
							///////////////////////////////////////
							///////////////////////
							///////////////////
							
							
/*
							XMLHttpRequest xhr = new XMLHttpRequest();
							xhr.open('POST', 'https://intranet.ncubo.com/UIQA8/loginPage', true);
							xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
							xhr.onload = function () {
							    alert(this.responseText);
							};

							xhr.send('userrname=abatista+pro50@testingsoft.com&password=123456');
*/
						}
						if (sku.equals(Fullsku) && precio_concatenado.length()>7)
						{
							String producto_concatenado = concatenar(Producto);
							// String precio_concatenado = concatenar(Precio);
							String disponibilidad_concatenada = concatenar(disponibilidad);
							Boolean Chequeado = false;
							System.out.println(hourdateFormat.format(date) + " El articulo con el codigo " + sku
									+ " y el nombre " + Nombre
									+ " esta presente en la lista pero temporalmente sin inventario.");
							write(productoslog, producto_concatenado, sku, "",
									"", true, 4, false);
							Existe = true;
						}


						
					/*	if (sku.equals(Fullsku) && precio_concatenado=="Contáctenos por precio.") {

							String producto_concatenado = concatenar(Producto);
							// String precio_concatenado = concatenar(Precio);
							String disponibilidad_concatenada = concatenar(disponibilidad);
							Boolean Chequeado = false;
							System.out.println(hourdateFormat.format(date) + " El articulo con el codigo " + sku
									+ " y el nombre " + Nombre
									+ " Aparece en las listas pero no esta disponible en este momento "
									);
							write(productoslog, producto_concatenado, sku, precio_concatenado,
									disponibilidad_concatenada, true, 4, false);
							Existe = true;

						}*/
						

					}

				}

				if (Existe.equals(false)) {

					System.out.println(hourdateFormat.format(date) + " El articulo con el codigo " + sku
							+ " y el nombre " + Nombre + " no esta disponible en la lista de productos de Intcomex");

					write(productoslog, Nombre, sku, "", "", false, 1, false);
					// aqui se llama el metodo de nimbus para desabilitar el
					// producto

				}

						//System.out.println("Agregado articulo con el SKU "+sku+"...Ok");
					}
					if(!(defaultlogfile.getFreeSpace()==0))
					{
					 enviar_correo(args[9],args[10],"Productos_no_encontrados.txt","Disponibilidad de Productos en Incomex");
					}
					if(!(NO_SKU_File.getFreeSpace()==0) && NO_SKU_File.exists()==true)
					{
					enviar_correo(Productos_sin_sku, args[10],"NO_SKU.txt", "Articulos sin SKU");
					 
					}
				
					
				}
		
		////////////////////////////////////////////////////
		if (defaultlogfile.exists() == true) {
			
			directorio_archivo_maestro = new File(args[4]);
			BufferedReader br3 = new BufferedReader(new FileReader(defaultlogfile));
			Date date = new Date();
			String line3 = "";
			DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
			System.out.println(hourdateFormat.format(date)
					+ " Comparando productos para verificar si cambio su disponibilidad");
			while ((line3 = br3.readLine()) != null) {
				// use comma as separator
				Existe = false;
				
				
				// System.out.println(cols[2]);
				Thread.sleep(3000);
				directorio_archivo_merged = new File(args[3]);
				BufferedReader br4 = new BufferedReader(
						new InputStreamReader(new FileInputStream(directorio_archivo_merged), "UTF8"));

			}
		}
		
		System.out.println("Articulos actualizados "+ updated);
		System.out.println("Articulos que quedaron igual "+ noupdated);
		System.out.println("Articulos con faltante de SKU "+ notfind);
		return PageFactory.initElements(driver, MainMenu.class);
	}
	

	public MainMenu download_files(WebDriver driver,String[] args)
			throws InterruptedException, AWTException, IOException, BiffException {
		// Aqui le digo cual es la ruta del archivo con las urls de las que
		// quiero descargar
		File directorio = null;
		File directorio_archivo_borrar = null;
		directorio_archivo_borrar = new File(args[8]);
		borrar_carpeta(directorio_archivo_borrar);
		//String route = null;
		//route = System.getProperty("user.home");
		directorio = new File(args[1]);
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		BufferedReader br = new BufferedReader(new FileReader(directorio));
		String line = "";
		System.out.println(hourdateFormat.format(date)+" Iniciando con la descarga de archivos...");
		System.out.println(hourdateFormat.format(date)+" Esto puede tomar un tiempo");
        //Voy leyendo el archivo y copiando las rutas
		while ((line = br.readLine()) != null) {
			driver.get(line);
			System.out.println(line);
			String URL = driver.getCurrentUrl().toString();
			if (URL.contains("Error")) {
				Thread.sleep(10000);
				driver.navigate().refresh(); 
				System.out.println("No se pudo acceder a esta pagina,"+line);
				//WebElement elemento= driver.findElement(By.xpath("//a[contains(.,' Ir al catálogo de productos')])"));
				//elemento.click();
				}
			
			boolean validurl = PageUtils.isElementDisplayed(driver, By.xpath("(//a[@class='font-compact priceListButtom control-label'])[2]"));
			if (validurl==true) 
			{
				
				 WebDriverWait wait = new WebDriverWait(driver,3);
				 Thread.sleep(1000);
				 boolean ispresent = PageUtils.isElementDisplayed(driver, driver.findElement(By.xpath("(//a[@class='font-compact priceListButtom control-label'])[2]")));
				 if (ispresent) {
					 WebElement boton= driver.findElement(By.xpath("(//a[@class='font-compact priceListButtom control-label'])[2]"));
					 wait.until(ExpectedConditions.elementToBeClickable(boton));
					//WaitTool.waitForElementPresentAndVisible(driver,driver.findElement(By.xpath("(//a[@class='font-compact priceListButtom control-label'])[2]")));
					driver.findElement(By.xpath("(//a[@class='font-compact priceListButtom control-label'])[2]")).click();	
				}
				 
				// Thread.sleep(3000);
					String absolutePath = args[8];
			        File dir = new File(absolutePath);
			        File[] filesInDir = dir.listFiles();
			        int i = 0;
			        Thread.sleep(3000);
			        if (line.contains("ByCategory")) {
			         String[] codigosinpartir= line.split("/");
			         String codigo= codigosinpartir[6].substring(4, 6);
			         String name = "products.csv";
			         File file = new File(absolutePath + "\\" + name);
				       File file2 = new File(absolutePath + "\\"+"my_file_" + codigo + ".csv");			  
				       file.renameTo(file2);
				       //System.out.println("Anexando archivo al archivo Merge "+file2);
				      // unir_archivos(driver, args);
					}
			        else
			        {    
			        	 Thread.sleep(4000);
			        	 String[] codigosinpartir= line.split("="); 
					        String codigo= codigosinpartir[1].substring(0, 10);		
					        String name = "products.csv";
					        //String newName = "my_file_" + codigo + ".csv";
					       // String newPath = absolutePath + "\\" + newName;
					        
					        
					        File file = new File(absolutePath + "\\" + name);
					        File file2 = new File(absolutePath + "\\"+"my_file_" + codigo + ".csv");			  
					        file.renameTo(file2);
					      // System.out.println("Anexando archivo al archivo Merge "+file2);
					       // unir_archivos(driver, args);
			        }
			       
			       
			       
			     
			     
			     
			     
			  /*   for(File file:filesInDir) {
			            i++;
			            String name = file.getName();
			            String newName = "my_file_" + Math.random() + ".csv";
			            String newPath = absolutePath + "\\" + newName;
			            file.renameTo(new File(newPath));
			            System.out.println(name + " changed to " + newName);
			        }*/
			}	
			

		}
		
		System.out.println(hourdateFormat.format(date)+" Descarga de archivos Finalizada");
		//Uno los archivos descargados
		System.out.println(hourdateFormat.format(date)+" Actualizando archivo ProductsMerge.csv");
		Thread.sleep(5000);
		unir_archivos(driver, args);
		Thread.sleep(5000);
		System.out.println(hourdateFormat.format(date)+" Archivo Creado");

		driver.get("https://site.nimbus.ncubo.com/UI/loginPage");
		driver.findElement(By.id("username")).sendKeys("nimbus@ncubo.com");
		driver.findElement(By.id("password")).sendKeys("asociaciones2016");
		driver.findElement(By.id("entrarBoton")).click();
		Set<org.openqa.selenium.Cookie> cookies=null;
		cookies=driver.manage().getCookies();
		 
		  
		   org.openqa.selenium.Cookie name= driver.manage().getCookieNamed("JSESSIONID");
		    String JsessionID = name.toString();
		    Cookie =  JsessionID.substring(11, 43);
		   // System.out.println(JsessionID);
		    //System.out.println(Cookie);
		
		
		
		
		
		
		
		
		
		return PageFactory.initElements(driver, MainMenu.class);
	}

	public void unir_archivos(WebDriver driver,String[] args) throws IOException, BiffException, InterruptedException, AWTException {

		File directorio = null;
		String directorio_archivo_merged = null;
		String route = null;

		// Obtengo la ruta del sistema y le adjunto la ruta de mas que le quiero
		// poner
		route = System.getProperty("user.home");
		//Thread.sleep(3000);
		//Archivo Merge.bat
		directorio = new File(args[2]);
		String directorio_linux =(args[2]);
		String target = new String(directorio_linux);
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(target);
        proc.waitFor();
		//Process procScript = Runtime.getRuntime().exec(cmdScript);
		//Runtime.getRuntime().exec("cmd /c start " + directorio + "");
		directorio_archivo_merged = (args[3]);
		//Thread.sleep(3000);
		

	}

	public MainMenu comparar_archivos(WebDriver driver,String[] args) throws IOException, InterruptedException {

		//Aqui comparo por primera vez los archivos comparando los del archivo de nosotros y los de merge de archivos de intcomex
		File directorio_archivo_maestro = null;
		File directorio_archivo_merged = null;
		String route = null;
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		// Obtengo la ruta del sistema y le adjunto la ruta de mas que le quiero
		// poner
		route = System.getProperty("user.home");
		//Archivo Intcomex
		directorio_archivo_maestro = new File(args[4]);
		BufferedReader br = new BufferedReader(new FileReader(directorio_archivo_maestro));
		// *************************************************************

		Calendar fecha = new GregorianCalendar();
		int mes = fecha.get(Calendar.MONTH);
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
		int minuto = fecha.get(Calendar.MINUTE);
		//String logname = "Nimbuslog";
		/*logname = logname + "_" + mes + "_" + dia + "_" + hora + "_" + minuto;
		String defaultLogFile = (route + File.separator + "Desktop" + File.separator + "Nimbus_files" + File.separator
				+ "" + logname + ".txt");*/
		String productoslog = (args[12]);
		File defaultlogfile = new File (productoslog);
		//Archivo UpdatePrice
		String Pricetoupdate = (args[5]);
		File Updatefile = new File(Pricetoupdate);
/*
		String filetoupdate = (route + File.separator + "Desktop" + File.separator + "Nimbus_files" + File.separator
				+ "" + "Priceupdatetorename.csv");
		File filetorename = new File(filetoupdate);
		*/
		String line = "";
		String line2 = "";
		Boolean Existe = false;

		if (Updatefile.exists() == false) {
			System.out.println(hourdateFormat.format(date)+" Creando archivo UpdatePrice con los productos encontrados...");
			// aqui comparo los dos archivos a ver si encuentra el sku
			while ((line = br.readLine()) != null) {
				// use comma as separator
				Existe = false;
				String[] cols = line.split(";");
				String sku = cols[2];
				String Nombre = cols[3];
				// System.out.println(cols[2]);
				Thread.sleep(3000);
				directorio_archivo_merged = new File(args[3]);
				BufferedReader br2 = new BufferedReader(
						new InputStreamReader(new FileInputStream(directorio_archivo_merged), "UTF8"));

				while ((line2 = br2.readLine()) != null && Existe == false) {

					String[] cols2 = line2.split("\\t");

					String Fullsku = "";
					int conteo = cols2.length;
					char letra;
					// aqui se hace un nuevo string porque el csv trae un 0
					// entre letras y sino no machea
					if (conteo >= 7) {
						for (int i = 0; i < cols2[7].length(); i++) {
							letra = cols2[7].charAt(i);
							if (letra != 0) {
								Fullsku = Fullsku + letra;
							}
						}
					}

					if (conteo >= 7) {
						String Precio = cols2[4];
						String Producto = cols2[2];
						String precio_concatenado = concatenar(Precio);
						String disponibilidad = cols2[5];
						//!precio_concatenado.equals("Cont�ctenos para el precio")
						if (sku.equals(Fullsku)) {

							String producto_concatenado = concatenar(Producto);
							//String precio_concatenado = concatenar(Precio);
							String disponibilidad_concatenada = concatenar(disponibilidad);
							Boolean Chequeado = false;

							// El log ya no me interesa que lo haga
							//write(defaultLogFile, producto_concatenado, sku, precio_concatenado,
									//disponibilidad_concatenada, true, 1, false);

							// aqui creo el archivo para comparar a futuro con
							// la lista
							if (6 >= precio_concatenado.length()) 
							{
								write(Pricetoupdate, producto_concatenado, sku, precio_concatenado, "", true, 2, false);
								System.out.println(hourdateFormat.format(date)+" Agregando articulo a la lista de disponibles con el SKU "+sku+"...Ok");
								Existe = true;
								
							}
							
						}

					}

				}

				/*if (Existe.equals(false)) {

					
					 System.out.println("El articulo con el codigo "+sku+
					  " y el nombre "+Nombre+ " no esta disponible en la lista de productos de Intcomex");
					 
					write(productoslog, Nombre, sku, "", "", false, 1, false);
					// aqui se llama el metodo de nimbus para desabilitar el
					// producto

				}*/

				//System.out.println("Agregado articulo con el SKU "+sku+"...Ok");
			}

		}
		return PageFactory.initElements(driver, MainMenu.class);

	}

	public MainMenu Update_de_Precios(WebDriver driver,String[] args) throws IOException {
		File directorio_archivoUpdate = null;
		File directorio_archivo_merged = null;
		String route = null;
		route = System.getProperty("user.home");
		directorio_archivoUpdate = new File(args[5]);
		try {
			BufferedReader br = new BufferedReader(new FileReader(directorio_archivoUpdate));	
		
		
		/////
		String archivo_cambio_precio = (args[6]);

		String line = "";
		String line2 = "";
		Boolean Existe = false;
		Boolean header = true;
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		System.out.println(hourdateFormat.format(date)+" Inicio de comparacion de precios entre Intcomex y Nimbus");
		while ((line = br.readLine()) != null) {
			// use comma as separator
			Existe = false;
			String[] cols = line.split(";");
			String sku = cols[1];
			String Nombre = cols[0];
			String PrecioNimbus = cols[2];
			directorio_archivo_merged = new File(args[3]);
			BufferedReader br2 = new BufferedReader(
					new InputStreamReader(new FileInputStream(directorio_archivo_merged), "UTF8"));
			while ((line2 = br2.readLine()) != null && Existe == false) {

				String[] cols2 = line2.split("\\t");

				String Fullsku = "";
				int conteo = cols2.length;
				char letra;
				// aqui se hace un nuevo string porque el csv trae un 0 entre
				// letras y sino no machea
				if (conteo >= 9) {
					for (int i = 0; i < cols2[7].length(); i++) {
						letra = cols2[7].charAt(i);
						if (letra != 0) {
							Fullsku = Fullsku + letra;
						}
					}
				}

				if (conteo >= 9) {
					String Precio = cols2[4];
					String Producto = cols2[2];
					String producto_concatenado = concatenar(Producto);
					String precio_concatenado = concatenar(Precio);

					if (sku.equals(Fullsku) && !precio_concatenado.equals(PrecioNimbus) &&precio_concatenado.length()<=7) {

						System.out.println(hourdateFormat.format(date)+ "El producto con el codigo " + sku + " con un precio en Nimbus de $ " + PrecioNimbus
								+ " fue encontrado en Intcomex con un precio de $ " + precio_concatenado + " considere ajustar el precio");
						// hacer nuevo archivo con resultados

						Boolean Chequeado = false;

						write(archivo_cambio_precio, producto_concatenado, sku, PrecioNimbus, precio_concatenado, true,
								3, header);
						Existe = true;
						header = false;

					}
					

				}

			}

			System.out.println(hourdateFormat.format(date)+" Comparacion Finalizada del articulo con el SKU "+sku+"...Ok");
			br2.close();
		}
		br.close();
		File filetomail = new File(archivo_cambio_precio);
		if (filetomail.exists() == true) {
			BufferedReader br3 = new BufferedReader(new FileReader(archivo_cambio_precio));
			if (br3.readLine() != null) {
				enviar_correo(args[9],args[10],"Cambio_precio.csv","Precios sugeridos para Nimbus");
				actualizar_archivo_de_precios(driver, args);
				br3.close();
			}
		}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			//File ninmbus_descargas = new File(args[8]);
			//borrar_carpeta(ninmbus_descargas);
			borrar_archivo(args[5]);
		}
		return PageFactory.initElements(driver, MainMenu.class);

	}

	public MainMenu actualizar_archivo_de_precios(WebDriver driver,String[] args) throws IOException, InterruptedException {
		String route = null;
		route = System.getProperty("user.home");
		String archivo_cambio_precio = (args[6]);
		File cambio_precio = new File(archivo_cambio_precio);
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		if (cambio_precio.exists() == true) {
			String line = "";
			String line2 = "";
			Boolean Existe = false;
			File directorio_archivo_merged = null;
			String archivotemporal = (args[7]);
			String archivo_update_borrar = (args[5]);
			File archivoUpdatePrice = new File(args[5]);

			Thread.sleep(3000);
			if (archivoUpdatePrice.exists() == true) {
				BufferedReader br = new BufferedReader(new FileReader(archivoUpdatePrice));
				System.out.println(hourdateFormat.format(date)+"Actualizando Archivo UpdatePrice con los nuevos precios encontrados");
				// aqui comparo los dos archivos a ver si encuentra el sku
				while ((line = br.readLine()) != null) {
					// use comma as separator
					Existe = false;
					String[] cols = line.split(";");
					String sku = cols[1];
					String Nombre = cols[0];
					String PrecioNimbus = cols[2];
					// System.out.println(cols[2]);

					directorio_archivo_merged = new File(args[3]);
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(new FileInputStream(directorio_archivo_merged), "UTF8"));

					while ((line2 = br2.readLine()) != null && Existe == false) {

						String[] cols2 = line2.split("\\t");
						String Fullsku = "";
						int conteo = cols2.length;
						char letra;
						// aqui se hace un nuevo string porque el csv trae un 0
						// entre letras y sino no machea
						if (conteo >= 9) {
							for (int i = 0; i < cols2[7].length(); i++) {
								letra = cols2[7].charAt(i);
								if (letra != 0) {
									Fullsku = Fullsku + letra;
								}
							}
						}

						if (conteo >= 9) {
							String Precio = cols2[4];
							String Producto = cols2[2];
							String producto_concatenado = concatenar(Producto);
							String precio_concatenado = concatenar(Precio);
							if (sku.equals(Fullsku)) {
								String Linea_total;

								// Linea_total = producto_concatenado + ";" +
								// Fullsku + ";" + precio_concatenado;
								write(archivotemporal, producto_concatenado, sku, precio_concatenado, "", true, 3,
										false);
								Boolean Chequeado = false;

								Existe = true;

							} else {

								// Correr script nimbus
							}

						}

					}
					br2.close();

				}
				br.close();

			} // aqui sale el primer while

		}

		/*File directorio_archivo_borrar = null;
		directorio_archivo_borrar = new File(args[8]);
		borrar_carpeta(directorio_archivo_borrar);*/
		//directorio_archivo_borrar.delete();
		//System.out.println("Borrando Carpetas");

		return PageFactory.initElements(driver, MainMenu.class);
	}

	private String concatenar(String cadena) {
		String Cadena = "";
		for (int i = 0; i < cadena.length(); i++) {
			char letra = cadena.charAt(i);
			if (letra != 0) {
				Cadena = Cadena + letra;
			}
		}
		return Cadena;
	}

	public MainMenu borrando_carpetas(WebDriver driver,String[] args) {
		String route = null;
		route = System.getProperty("user.home");
		String borrar_archivo_update = (args[5]);
		File borrar_update = new File(borrar_archivo_update);
		String archivotemporal = (args[7]);
		File archivotemporalfile = new File(archivotemporal);
		String cambio_precio_string = (args[6]);
		File cambiopreciofile = new File(cambio_precio_string);
		File directorio_archivo_borrar = null;
		directorio_archivo_borrar = new File(args[8]);
		borrar_carpeta(directorio_archivo_borrar);
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		if (archivotemporalfile.exists() == true) {

			try {
				System.out.println(hourdateFormat.format(date)+" Borrando archivos temporales");
				Path path1 = Paths.get(borrar_archivo_update);
				Path path2 = Paths.get(archivotemporal);
				cambiopreciofile.delete();
				// suceso = borrar_update.delete();

				Files.delete(path1);
				Files.move(path2, path1);
			} catch (Exception e) {
				System.out.println(hourdateFormat.format(date)+" Los archivos archivotemporal y Cambio precio no pudieron ser borrados debido a " +e+" por favor borrelos manualmente");
				// TODO: handle exception
			}
			driver.quit();

		}
		return PageFactory.initElements(driver, MainMenu.class);

	}

	public void borrar_carpeta(File directorio_archivo_borrar) {
		try {
			Date date = new Date();
			DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
			File contenido = (directorio_archivo_borrar);
			File[] ficheros = contenido.listFiles();
			File f = null;
			if (contenido.exists()) {
				System.out.println(hourdateFormat.format(date)+" Borrando catalogos descargados de Intcomex");
				for (int x = 0; x < ficheros.length; x++) {
					f = new File(ficheros[x].toString());
					f.delete();
				}
			} else {
				System.out.println(hourdateFormat.format(date)+" No existe el directorio");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO: handle exception
		}

	}

	public void borrar_archivo(String archivo_delete) {
		try {
			Date date = new Date();
			DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
			File archivo = new File(archivo_delete);
			if (archivo.delete()) {
				System.out.println(hourdateFormat.format(date)+" El archivo ha sido borrado");
			} else {
				System.out.println(hourdateFormat.format(date)+" El fichero no puede ser borrado");

			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	public static void write(String file, String producto, String codigo, String precio, String cantidad,
			boolean existente, int tipoarchivo, Boolean header) throws IOException {
		TimeZone tz = TimeZone.getTimeZone("EST"); // or PST, MID, etc ...
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy.mm.dd hh:mm:ss ");
		df.setTimeZone(tz);
		int count = 0;
		String currentTime = df.format(now);

		FileWriter aWriter = new FileWriter(file, true);

		if (existente == true && tipoarchivo == 1) {

			aWriter.write("Se a encontrado el " + producto + " codigo " + codigo + " con un precio de $" + precio
					+ " y una cantidad existente de " + cantidad + "\n");
			aWriter.flush();
			aWriter.close();
		}
		if (existente == true && tipoarchivo == 4) {

			aWriter.write("Se a encontrado el " + producto + " codigo " + codigo + " pero temporalmente esta sin existencias"+"\n" );
			aWriter.flush();
			aWriter.close();
		}
		if (existente == false && tipoarchivo == 1) {

			aWriter.write("El articulo con el codigo " + codigo + " y el nombre " + producto
					+ " no existe en la lista"+"\n");
			aWriter.flush();
			aWriter.close();
		}
		if (existente == false && tipoarchivo == 6) {

			aWriter.write("El articulo con el codigo " + codigo + " y el nombre " + producto
					+ " no fue encontrado en Nimbus"+"\n");
			aWriter.flush();
			aWriter.close();
		}

		if (existente == true && tipoarchivo == 2) {
			// aqui se hace un archivo con solo los productos que existen para
			// luego comparar los precios y ver si cambiaron
			if (precio.length() <= 3) {
				aWriter.append(producto);
				aWriter.append(";");
				aWriter.append(codigo);
				aWriter.append(";");
				aWriter.append(precio + "00");
				aWriter.append("\n");
			} else {
				aWriter.append(producto);
				aWriter.append(";");
				aWriter.append(codigo);
				aWriter.append(";");
				aWriter.append(precio);
				aWriter.append("\n");
			}
			aWriter.flush();
			aWriter.close();

		}
		
		if (existente == true && tipoarchivo == 5) {
			// Creo el Csv de el SKU y la cantidad
			
				aWriter.append(codigo);
				aWriter.append(";");
				aWriter.append(cantidad);
				aWriter.append(";");
				aWriter.append("\n");
				aWriter.flush();
				aWriter.close();

		}
		if (existente == true && tipoarchivo == 3) {
			// Aqui se hace el cvs de los articulos que cambiaron su precio para
			// enviarla por medio de correo

			if ((header == true)) {
				aWriter.append("Producto");
				aWriter.append(";");
				aWriter.append("Codigo");
				aWriter.append(";");
				aWriter.append("Precio Nimbus");
				aWriter.append(";");
				aWriter.append("Precio de Intcomex");
				aWriter.append("\n");
			}

			aWriter.append(producto);
			aWriter.append(";");
			aWriter.append(codigo);
			aWriter.append(";");
			aWriter.append(precio);
			aWriter.append(";");
			aWriter.append(cantidad);
			aWriter.append("\n");
			aWriter.flush();
			aWriter.close();

		}

	}

	public MainMenu and() {
		return this;
	}

	public MainMenu then() {
		return this;
	}

	public void enviar_correo(String archivo_update,String correos,String nombre_archivo_a_enviar, String mensaje) throws IOException {

		final String username = "robotnimbus@gmail.com";
		final String password = "nimbusproyect";
		String route;
		route = System.getProperty("user.home");
		String directorio_archivoUpdate = (archivo_update);
		Date date = new Date();
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		Properties props = new Properties();
		 props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "465");
	        props.put("mail.transport.protocol", "smtp");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		/*props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.EnableSSL.enable","true");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");*/
		
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("robotnimbus@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correos));
			message.setSubject(mensaje);
			message.setText("Esta es una prueba");

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			Multipart multipart = new MimeMultipart();

			messageBodyPart = new MimeBodyPart();
			String file = archivo_update;
			String fileName =nombre_archivo_a_enviar;
			DataSource source = new FileDataSource(file);
			messageBodyPart.setDataHandler(new DataHandler(source));
			File att = new File(new File(file), fileName);
			messageBodyPart.attachFile(att);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			System.out.println(hourdateFormat.format(date)+" Enviando Correo con los cambios");
			
			Transport.send(message);

			System.out.println(hourdateFormat.format(date)+" Correo enviado");
			
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println(hourdateFormat.format(date)+" El correo no pudo ser enviado debido a "+e);
		}
	}

}
