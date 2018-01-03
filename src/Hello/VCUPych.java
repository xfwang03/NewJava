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

public class VCUPych {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "//home/xiangfeng/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("http://www.google.com");
		driver.navigate().to("https://psychology.vcu.edu/people/graduate-students/"
				+ "");
		List<WebElement>  eleDivs = driver.findElements(By.xpath("//div[@class='media listing']"));
		//List<WebElement> eleDivs = table.findElements(By.cssSelector("tr"));
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
			
			 WebElement eleName = eleDivs.get(i).findElement(By.cssSelector("h4"));
			 WebElement eleMail = eleDivs.get(i).findElement(By.partialLinkText("edu"));
			
			 try {
					PreparedStatement pstmt = conn
							.prepareStatement("INSERT OR IGNORE INTO vcu (email,name,major) VALUES(?,?,?);");
					pstmt.setString(2, eleName.getText());
					pstmt.setString(1, eleMail.getText());
					pstmt.setString(3, "Psychology");

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
