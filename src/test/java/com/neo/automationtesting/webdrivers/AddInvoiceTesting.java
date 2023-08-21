package com.neo.automationtesting.webdrivers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

import com.neo.automationtesting.dto.Invoice;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.neo.automationtesting.utility.Constants;


public class AddInvoiceTesting {

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
        Thread.sleep(4000);

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

        WebElement target = driver.findElement(By.xpath(Constants.Invoice)); //To move on the Invoice option
        WebElement target1 = driver.findElement(By.xpath(Constants.AddInvoice)); //To be clicked on Add Invoice option and load its webpage
        Actions s = new Actions(driver);
        s.moveToElement(target);
        s.click(target1);
        s.build().perform();

        String dataFolder = "D:\\Alchemist\\Neo-Automation\\src\\test\\resources\\data";
        File[] userTypeFolder = new File(dataFolder).listFiles(File::isDirectory);

        ArrayList<Invoice> userTypes = new ArrayList<>();
        String typeFolder = null;
        String file = null;
        for (int i = 0; i < userTypeFolder.length; i++) {
            String folderName = userTypeFolder[i].getName();
            typeFolder = folderName.substring(0, folderName.length());
            File[] users = new File("D:\\Alchemist\\Neo-Automation\\src\\test\\resources\\data\\" + typeFolder).listFiles();
            for (int j = 0; j < users.length; j++) {
                String fileName = users[j].getName();
                file = fileName.substring(0, fileName.length() - 5);
                FileInputStream inputStream = new FileInputStream(users[j]);
                Workbook workbook = WorkbookFactory.create(inputStream);

                // Iterate over all sheets
                for (int k = 0; k < workbook.getNumberOfSheets(); k++) {
                    Sheet sheet = workbook.getSheetAt(k);

                    // Iterate over all rows
                    for (int l = 1; l <= sheet.getLastRowNum(); l++) {
                        Row row = sheet.getRow(i);
                        // Select User Type
                        Thread.sleep(1000);
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                        WebElement userSelect = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Constants.UserType)));
                        userSelect.sendKeys(Keys.CONTROL + "a");
                        userSelect.sendKeys(Keys.BACK_SPACE);
                        userSelect.sendKeys(typeFolder);
                        userSelect.click();
                        WebElement suggestion = driver.findElement(By.xpath(Constants.Suggestion));
                        suggestion.click();
                        Thread.sleep(1000);

                        // Select Company Name
                        WebElement companySelect = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Constants.compName)));
                        companySelect.sendKeys(Keys.CONTROL + "a");
                        companySelect.sendKeys(Keys.BACK_SPACE);
                        companySelect.sendKeys(file);
                        companySelect.click();
                        WebElement suggestions = driver.findElement(By.xpath(Constants.Suggestion));
                        suggestions.click();

                        String invoiceNo = getStringValue(row.getCell(0));
                        double excelDate = row.getCell(1).getNumericCellValue();
                        // Convert the Excel serial number to a Date object
                        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(excelDate);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                        String formattedDate = dateFormat.format(date);
                        double amount = getNumericValue(row.getCell(2));
                        System.out.println(typeFolder + ", " + file + ", " + invoiceNo + ", " + date + ", " + amount);

                        // Enter Invoice No
                        WebElement invoice = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Constants.InvoiceNo)));
                        invoice.sendKeys(invoiceNo);

                        // Enter Date
                        WebElement dateSelect = driver.findElement(By.xpath(Constants.Date));
                        dateSelect.sendKeys(formattedDate);
                        dateSelect.click();

                        // Enter Amount
                        WebElement amountField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Constants.Amount)));
                        amountField.sendKeys(Double.toString(amount));

                        // Submit the data
                        WebElement save = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(Constants.Save)));
                        save.submit();

                        // Handle alert dialog
                        try {
                            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                            String alertText = alert.getText();
                            System.out.println("Alert text: " + alertText);
                            alert.accept();
                        } catch (Exception e) {
                            // No alert dialog found
                        }
                    }
                }
                workbook.close();
                inputStream.close();

                Invoice data = new Invoice();
                data.setUserType(typeFolder);
                data.setCompanyName(file);
                userTypes.add(data);
                driver.quit(); //To close the browser
            }
        }
    }
    private static String getStringValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }
    private static double getNumericValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else {
            return 0;
        }
    }
}






