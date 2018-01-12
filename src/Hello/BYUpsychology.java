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

public class BYUpsychology {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "//home/xiangfeng/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("http://www.google.com");
		driver.navigate().to("https://psychology.byu.edu/Pages/GradStudents.aspx");
		List<WebElement> eleDivs = driver.findElements(By.xpath("//td[@class='ms-stylebox']"));
		//List<WebElement> eleDivs = driver.findElements(By.xpath("//div[@class='items-row']"));
		//List<WebElement> eleDivs = allElements.findElements(By.cssSelector("p"));
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
		int count =0;
		
		for (int i = 0; i < eleDivs.size(); i++) {

			List<WebElement> TRs = eleDivs.get(i).findElements(By.cssSelector("tr"));
			List<WebElement> eleMail = eleDivs.get(i).findElements(By.partialLinkText("@"));
			if (eleMail.size() == 0 || TRs.size()<=3) continue;
			count++;
			System.out.println("Count: " + count+" i: "+i);
			String name = TRs.get(1).findElement(By.className("ms-stylebody")).getText()+" "+TRs.get(2).findElement(By.className("ms-stylebody")).getText();
			try {
				PreparedStatement pstmt = conn
						.prepareStatement("INSERT OR IGNORE INTO byu (email,name,major) VALUES(?,?,?);");
				pstmt.setString(1, eleMail.get(0).getText());
				pstmt.setString(2, name);
				pstmt.setString(3, "Psychology");
				System.out.println(eleMail.get(0).getText());
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
