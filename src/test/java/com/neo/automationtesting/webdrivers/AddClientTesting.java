package com.neo.automationtesting.webdrivers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.neo.automationtesting.utility.Constants;
import com.neo.automationtesting.dto.Client;

public class AddClientTesting {
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
		Thread.sleep(3000);

		String parentWindow = driver.getWindowHandle();
		System.out.println("Parent Window ID is : " + parentWindow);
		for(String winHandle : driver.getWindowHandles())
		{
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

		WebElement target = driver.findElement(By.xpath(Constants.Client)); //To move on the Client option
		WebElement target1 = driver.findElement(By.xpath(Constants.AddClient)); //To be clicked on Add Client option and load its webpage
		Actions s = new Actions(driver);
		s.moveToElement(target);
		s.click(target1);
		s.build().perform();

		String clientFolder = "D:\\Alchemist\\Neo-Automation\\src\\test\\resources\\data\\client";
		File[] clientFiles = new File(clientFolder).listFiles();

		ArrayList<Client> clients = new ArrayList<>();
		Random rand = new Random();
		int i;
		for (i = 0; i < clientFiles.length; i++) {
			String fileName = clientFiles[i].getName();
			String file = fileName.substring(0, fileName.length() - 5);
			String email = file.replaceAll("\\s+", "") + "@gmail.com";
			Client client = new Client();
			client.setClientName(file);
			client.setMobileNo(rand.nextInt(900000000) + 1000000000L);
			client.setClientEmail(email.toLowerCase());
			clients.add(client);
		}
		System.out.println(new ObjectMapper().writeValueAsString(clients));

		for (Client client : clients) {

			// Enter Client's Name
			WebElement clientNameField = driver.findElement(By.xpath(Constants.Name));
			clientNameField.sendKeys(client.getClientName());

			// Enter Client's Mobile No.
			WebElement clientMobileNo = driver.findElement(By.xpath(Constants.MobileNo));
			clientMobileNo.sendKeys(String.valueOf(client.getMobileNo()));

			// Enter Client's Email id
			WebElement clientEmail = driver.findElement(By.xpath(Constants.Email));
			clientEmail.sendKeys(client.getClientEmail());

			// Submit the data
			driver.findElement(By.xpath(Constants.Submit)).submit();

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
		driver.quit();  //To close the browser
	}
}

