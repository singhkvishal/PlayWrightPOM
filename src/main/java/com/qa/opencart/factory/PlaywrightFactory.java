package com.qa.opencart.factory;

import com.microsoft.playwright.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

public class PlaywrightFactory {
	Properties prop;
	Playwright playwright;
	Browser browser;
	BrowserContext browserContext;
	static Page page;

	public static Page getPage() {
		return page;
	}
	public Page initBrowser(Properties prop) {

		 ThreadLocal<Browser> tbrowser = new ThreadLocal<>();
		ThreadLocal<Browser> tPage = new ThreadLocal<>();


		String browserName = prop.getProperty("browser").trim();
		boolean isHeadLess=Boolean.parseBoolean(prop.getProperty("headless"));

		playwright = Playwright.create();

		switch (browserName.toLowerCase()) {
			case "chromium":
				tbrowser=tbrowser.set(playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadLess)));
				break;

			case "chrome":
				browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(isHeadLess));
				break;

			default:
				System.out.println("please pass the right browser name......");
				break;
		}

		browserContext=browser.newContext();
		page=browserContext.newPage();
		page.navigate(prop.getProperty("url").trim());
		return page;
	}

	/**
	 * this method is used to initialize the properties from config file
	 */
	public Properties init_prop() {

		try {
			FileInputStream ip = new FileInputStream("./src/test/resources/config/config.properties");
			prop = new Properties();
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	/**
	 * take screenshot
	 *
	 */

	public static String takeScreenshot() {
		String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";
		//getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));

		byte[] buffer = getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
		String base64Path = Base64.getEncoder().encodeToString(buffer);

		return base64Path;
	}

}
