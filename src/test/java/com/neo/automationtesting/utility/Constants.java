package com.neo.automationtesting.utility;

public class Constants {

	//Sign in XPaths
	public final static String SignIn = "//button[@class='google-signin']";
	public final static String Username ="//input[@type='email']";
	public final static String Next ="//span[contains(text(),'Next')]";
	public final static String Password ="//input[@type='password']";
	
	//Add Vendor XPaths
	public final static String Vendor = "//button[contains(text(),'Vendor')]";
	public final static String AddVendor = "//a[contains(text(),'Add Vendor')]";
	public final static String VendorName = "//input[@placeholder='Name']";
	public final static String VendorMobileNo = "//input[@placeholder='Mobile Number']";
	public final static String VendorEmail = "//input[@placeholder='Email id']";
	public final static String MaterialType = "//select[@class='select_addvendor']";
	public final static String Add = "//button[@type='submit']";
	
	//Add Client XPaths
	public final static String Client = "//button[contains(text(),'Client')]";
	public final static String AddClient = "//a[contains(text(),'Add Client')]";
	public final static String Name = "//input[@placeholder='Name']";
	public final static String MobileNo = "//input[@placeholder='Mobile Number']";
	public final static String Email = "//input[@placeholder='Email id']";
	public final static String Submit = "//button[@class='btn-submit']";
	
	//Add Invoice XPaths
	public final static String Invoice = "//button[contains(text(),'Invoice')]";
	public final static String AddInvoice = "//a[contains(text(),'Add Invoice')]";
	public final static String UserType = "//input[@id='usertype']";
	public final static String Suggestion ="//li[@class='MuiAutocomplete-option']";
	public final static String compName = "//input[@id='compName']";
	public final static String InvoiceNo = "//input[@placeholder='Invoice No']";
	public final static String Date = "//input[@name='date']";
	public final static String Amount = "//input[@placeholder='Base Price']";
	public final static String Save = "//button[@class='btn-save']";
}
