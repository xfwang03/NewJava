package TestinWin.Hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class CleU {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "M:\\workspace\\db\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("https://my.clemson.edu/#/directory/advanced-search");
		WebElement FirstName = driver.findElement(By.id("dojox_mobile_TextBox_1"));
		FirstName.sendKeys("a");
		WebElement optionList = driver.findElement(By.id("dijit__WidgetBase_8_select"));
		optionList.isSelected();
		driver.findElement(By.xpath("//option[@value='student']")).click();
		WebElement optionListMajor = driver.findElement(By.id("uniqName_1_13-text"));
		optionListMajor.click();
		driver.findElement(By.xpath("//li[@data-value='AAH']")).click();
		driver.findElement(By.className("searchButton")).click();
		List<WebElement> RLs= driver.findElements(By.className("resultsList"));
		List<WebElement> LIs= RLs.get(0).findElements(By.cssSelector("a"));
		System.out.println(RLs.size());
		for(int k=0;k<LIs.size();k++) {
			System.out.println(LIs.get(k).getAttribute("href"));
		}
		Connection conn = null;
		try {
			// db parameters
			String urlDB = "jdbc:sqlite:M:\\workspace\\db\\mqwindows.sqlite";
			// create a connection to the database
			conn = DriverManager.getConnection(urlDB);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			
		}
		
		
		/*for(int j =0;j<50;j++) {
			System.out.println("inside loop "+j);
			WebElement mainDiv = driver.findElement(By.id("txtPERSON"));
			driver.findElements(By.xpath("//img[@alt='next']")).get(0).click();				
			driver.findElements(By.xpath("//img[@alt='next']")).get(0).click();	
			
			WebElement mainDiv = driver.findElement(By.id("txtPERSON"));
			List<WebElement> RLs = mainDiv.findElements(By.cssSelector("ul"));
			System.out.println("RLs size: "+RLs.size());
			for(int i=0;i<RLs.size();i++) {
				System.out.println("inside loop above "+i);
				List<WebElement> name = RLs.get(i).findElements(By.cssSelector("b"));
				List<WebElement> email = RLs.get(i).findElements(By.cssSelector("a"));
				System.out.println("inside loop below "+i);
				if(name.size()!=1||email.size()!=1) continue;
				try {
					PreparedStatement pstmt = conn
							.prepareStatement("INSERT OR IGNORE INTO hu (email,name) VALUES(?,?);");
					pstmt.setString(1, email.get(0).getText());
					pstmt.setString(2,  name.get(0).getText());				
					pstmt.execute();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			List<WebElement> nextEle = mainDiv.findElement(By.cssSelector("p")).findElements(By.cssSelector("a"));
			nextEle.get(nextEle.size()-1).click();
			
		}
	*/
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		System.out.println("Done!");
		driver.quit();

		
		
		
		
		
		//driver.close();

	}

}
