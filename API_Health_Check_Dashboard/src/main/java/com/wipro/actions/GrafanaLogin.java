package com.wipro.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GrafanaLogin {
	WebDriver driver=null; 
	JavascriptExecutor js = (JavascriptExecutor) driver;
	
	//user will login to grafana in headless so that there will not be any additional login prompts when accessing grafana panels
	public void login()
	{
		
		String projectPath=System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", projectPath+"/src/main/resources/Drivers/chromedriver.exe");
		
		//adding heaedless execution
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		driver = new ChromeDriver(options);
		
		//driver=new ChromeDriver();
		
		driver.manage().window().maximize();
		
		driver.get("http:localhost:3000");
		
		
		
		WebDriverWait wait = new WebDriverWait(driver,30);
		
		String username="viewer";
		String password="viewer123";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='email or username']")));
		driver.findElement(By.xpath("//input[@placeholder='email or username']")).sendKeys(username);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='password']")));
		driver.findElement(By.xpath("//input[@placeholder='password']")).sendKeys(password);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Login button']")));
		driver.findElement(By.xpath("//button[@aria-label='Login button']")).click();
	}
	//will load the spring boot webapp home page
	public void Dashboard_Home()
	{
		driver.get("http:localhost:8081/home");
	}
}
