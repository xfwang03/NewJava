package TestinWin.Hello;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class HU {

	public static void main(String[] args) throws StaleElementReferenceException {
		System.setProperty("webdriver.chrome.driver", "M:\\workspace\\db\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("https://directory.hampshire.edu");
		WebElement optionList = driver.findElement(By.id("HC_dept"));
		optionList.isSelected();
		driver.findElement(By.xpath("//option[@value='ALLSTU']")).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
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
		
		
		for(int j =0;j<50;j++) {
			System.out.println("inside loop "+j);
			WebElement mainDiv = driver.findElement(By.id("txtPERSON"));
			/*driver.findElements(By.xpath("//img[@alt='next']")).get(0).click();				
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
			*/
			List<WebElement> nextEle = mainDiv.findElement(By.cssSelector("p")).findElements(By.cssSelector("a"));
			nextEle.get(nextEle.size()-1).click();
			
		}
	
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
