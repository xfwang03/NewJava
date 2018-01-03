package Hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class VCUEnglish {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "//home/xiangfeng/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("http://www.google.com");
		driver.navigate().to("https://english.vcu.edu/people/graduate-assistants/"
				+ "");
		WebElement table = driver.findElement(By.cssSelector("tbody"));
		List<WebElement> eleDivs = table.findElements(By.cssSelector("tr"));
		System.out.println(eleDivs.size());
		Connection conn = null;
		try {
			// db parameters
			String urlDB = "jdbc:sqlite:/home/xiangfeng/eclipse-workspace/SqliteDB/minqi.sqlite";
			// create a connection to the database
			conn = DriverManager.getConnection(urlDB);

			System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		for(int i =0; i<eleDivs.size();i++) {
			
			 List<WebElement> ele3 = eleDivs.get(i).findElements(By.cssSelector("td"));
			 if(ele3.size()<2) continue;
			 System.out.println(ele3.size());
			 try {
					PreparedStatement pstmt = conn
							.prepareStatement("INSERT OR IGNORE INTO vcu (email,name,major) VALUES(?,?,?);");
					pstmt.setString(2, ele3.get(0).getText());
					pstmt.setString(1, ele3.get(2).getText());
					pstmt.setString(3, "English");

					pstmt.execute();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	}

}
