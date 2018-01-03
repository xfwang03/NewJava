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


public class TestHello {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "//home/xiangfeng/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();       
	    driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
	    driver.navigate().to("http://www.google.com");
	    driver.navigate().to("http://www.mtu.edu/biomedical/people/graduate/");
		 List<WebElement> eleDivs = driver.findElements(By.xpath("//div[@class='person_bio']"));
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
			
		 for(int i =3; i<eleDivs.size();i++) {
			 WebElement ele1 = eleDivs.get(i).findElement(By.cssSelector("h2"));
			 WebElement ele2 = eleDivs.get(i).findElement(By.cssSelector("li"));
			 List<WebElement> ele3 = eleDivs.get(i).findElements(By.cssSelector("a[href*='mtu.edu']"));
			 if(ele3.size()==0) continue;
			 System.out.println(ele1.getText());
			 try {
					PreparedStatement pstmt = conn
							.prepareStatement("INSERT OR IGNORE INTO mtu2 (email,name,degree) VALUES(?,?,?);");
					pstmt.setString(1, ele3.get(0).getText());
					pstmt.setString(2, ele1.getText());
					pstmt.setString(3, ele2.getText());

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
