package com.tieto.wro.java.a17.weather;

import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


@Ignore
public class JspSeleniumTest {

	private static WebDriver driver;
	private final String BASE_URL = "http://localhost:8080/view/weather";

	@BeforeClass
	public static void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		driver = new ChromeDriver();
	}

	@Test
	public void Given_ClickCity_Expect_ShowDetails() {
		driver.get(BASE_URL);

		driver.findElement(By.id("b0")).click();

		assertTrue(driver.findElement(By.id("w0")).getAttribute("aria-expanded").equals("true"));
	}

	@Test
	public void Given_TypeCity_Expect_FilterCities() {
		driver.get(BASE_URL);

		driver.findElement(By.id("city-search")).sendKeys("wroclaw");
		List<WebElement> buttons = driver.findElements(By.tagName("button"));

		long numberOfVisibleButtons = buttons.stream()
				.filter((e) -> !"none".equals(e.getCssValue("display")))
				.count();

		assertTrue(numberOfVisibleButtons == 1);
	}

	@Test
	public void Given_DoubleClickCity_Expect_ShowAndHideDetails() throws InterruptedException {
		driver.get(BASE_URL);

		WebElement button = driver.findElement(By.id("b0"));
		WebElement details = driver.findElement(By.id("w0"));
		button.click();

		assertTrue(details.getAttribute("aria-expanded").equals("true"));

		Thread.sleep(2000);
		button.click();

		assertTrue(details.getAttribute("aria-expanded").equals("false"));

	}

	@AfterClass
	public static void afterTests() {
		driver.close();
	}
}
