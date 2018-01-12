package Hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class OSU {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "//home/xiangfeng/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("https://www.osu.edu/findpeople/");
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		String pattern = "\\b[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9.-]+\\b"; //Email Address Pattern
	    

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
		for(int i=0;i<26;i++) {
			driver.navigate().to("https://www.osu.edu/findpeople/");
			WebElement firstName = driver.findElement(By.id("firstname"));
			char leI = (char) ('a' + i);
			String letterI = String.valueOf(leI)+"*";
			firstName.sendKeys(letterI);
			WebElement StuOnly = driver.findElement(By.id("filterOnlyStudents"));
			StuOnly.click();
			driver.findElement(By.xpath("//input[@value='Submit']")).click(); // Submit
			List<WebElement> TRs = driver.findElements(By.className("record-collapsed"));
			for(int j=0;j<TRs.size();j++) {
				List<WebElement> Name = TRs.get(j).findElements(By.className("results-name"));
				List<WebElement> Email = TRs.get(j).findElements(By.className("record-data-email"));
				List<WebElement> Major = TRs.get(j).findElements(By.className("record-data-major"));
				if(Name.size()!=1||Email.size()!=1||Major.size()!=1) continue;
				
				try {
					PreparedStatement pstmt = conn
							.prepareStatement("INSERT OR IGNORE INTO osu (email,name, major) VALUES(?,?,?);");
					pstmt.setString(1, Email.get(0).getText());
					pstmt.setString(2,  Name.get(0).getText());
					pstmt.setString(3,  Major.get(0).getText());
					pstmt.execute();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(TRs.size());
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
