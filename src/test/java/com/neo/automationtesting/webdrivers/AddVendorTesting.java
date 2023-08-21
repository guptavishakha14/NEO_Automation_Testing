package com.neo.automationtesting.webdrivers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

import com.neo.automationtesting.dto.Vendor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.neo.automationtesting.utility.Constants;

public class AddVendorTesting {
	public static void main(String[] args) throws IOException, InterruptedException {

		System.setProperty("webdriver.chrome.driver", "D:\\Downloads\\chromedriver_win32\\chromedriver.exe");
		ChromeOptions co = new ChromeOptions();
		co.addArguments("--remote-allow-origins=*");
		WebDriver driver = new ChromeDriver(co);
		driver.manage().window().maximize();
		driver.navigate().to("http://localhost:3000/"); //To navigate to Neo project homescreen
		System.out.println(driver.getTitle());

		WebElement signIn = driver.findElement(By.xpath(Constants.SignIn));
		signIn.click();
		Thread.sleep(2000);

		String parentWindow = driver.getWindowHandle();
		System.out.println("Parent Window ID is : " + parentWindow);
		for(String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			System.out.println(driver.getTitle());
			driver.manage().window().maximize();
		}

		WebElement username = driver.findElement(By.xpath(Constants.Username));
		username.sendKeys("neotestinguser@gmail.com");

		WebElement next1 = driver.findElement(By.xpath(Constants.Next));
		next1.click();
		Thread.sleep(2000);

		WebElement password = driver.findElement(By.xpath(Constants.Password));
		password.sendKeys("Alchemist@2023");

		WebElement next2 = driver.findElement(By.xpath(Constants.Next));
		next2.click();
		Thread.sleep(6000);

		driver.switchTo().window(parentWindow);

		WebElement target = driver.findElement(By.xpath(Constants.Vendor)); //To move on the Client option
		WebElement target1 = driver.findElement(By.xpath(Constants.AddVendor)); //To be clicked on Add Client option and load its webpage
		Actions s = new Actions(driver);
		s.moveToElement(target);
		s.click(target1);
		s.build().perform();

		String vendorFolder = "D:\\Alchemist\\Neo-Automation\\src\\test\\resources\\data\\vendor";
		File[] vendorFiles = new File(vendorFolder).listFiles();

		ArrayList<Vendor> vendors = new ArrayList<>();
		Random rand = new Random();
		int i;
		for (i = 0; i < vendorFiles.length; i++) {
			String fileName = vendorFiles[i].getName();
			String file = fileName.substring(0, fileName.length() - 5);
			String email = file.replaceAll("\\s+", "") + "@gmail.com";
			Vendor vendor = new Vendor();
			vendor.setVendorName(file);
			vendor.setVendorMobileNo(rand.nextInt(900000000) + 1000000000L);
			vendor.setVendorEmail(email.toLowerCase());
			vendor.setMaterialType("Material-"+rand.nextInt(5)+1);
			vendors.add(vendor);
		}
		System.out.println(new ObjectMapper().writeValueAsString(vendors));

		for (Vendor vendor : vendors) {

			// Enter Vendor's Name
			WebElement vendorNameField = driver.findElement(By.xpath(Constants.VendorName));
			vendorNameField.sendKeys(vendor.getVendorName());

			// Enter Vendor's Mobile
			WebElement vendorMobileNo = driver.findElement(By.xpath(Constants.VendorMobileNo));
			vendorMobileNo.sendKeys(String.valueOf(vendor.getVendorMobileNo()));

			// Enter Vendor's Email id
			WebElement vendorEmail = driver.findElement(By.xpath(Constants.VendorEmail));
			vendorEmail.sendKeys(vendor.getVendorEmail());

			// Select Material Type
    		WebElement materialSelect = driver.findElement(By.xpath(Constants.MaterialType));
            materialSelect.sendKeys(vendor.getMaterialType());
            materialSelect.click();

			// Submit the data
			driver.findElement(By.xpath(Constants.Add)).submit();

			// Handle alert dialog
			try {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				Alert alert = wait.until(ExpectedConditions.alertIsPresent());
				String alertText = alert.getText();
				System.out.println("Alert text: " + alertText);
				alert.accept();
			} catch (Exception e) {
				// No alert dialog found
			}
		}
//		driver.quit();  //To close the browser
	}
}

