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

public class EMU {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "//home/xiangfeng/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("http://www.google.com");
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();

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

		for (int i = 1; i < 26; i++) {
			char upper = (char) ('a' + i);
			String letter = String.valueOf(upper);
			String URL = "http://www.emich.edu/directory/?last-name=" + letter + "&student=on";
			driver.navigate().to(URL);

			List<WebElement> ele3 = driver.findElements(By.cssSelector("h3"));
			if (ele3.size() == 0)
				continue;
			System.out.println(ele3.get(0).getText().split(" ")[0]);
			hmap.put(letter, Integer.valueOf(ele3.get(0).getText().split(" ")[0]));

			/*
			 * try { PreparedStatement pstmt = conn
			 * .prepareStatement("INSERT OR IGNORE INTO mtu2 (email,name,degree) VALUES(?,?,?);"
			 * ); pstmt.setString(1, ele3.get(0).getText()); pstmt.setString(2,
			 * ele1.getText()); pstmt.setString(3, ele2.getText());
			 * 
			 * pstmt.execute();
			 * 
			 * } catch (SQLException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}
		for (int i = 1; i < 26; i++) {
			char upper = (char) ('a' + i);
			String letter = String.valueOf(upper);
			int maxpage = hmap.get(letter) / 10 +1;
			for (int j = 1; j <= maxpage; j++) {
				String URLPage = "http://www.emich.edu/directory/?page=" + String.valueOf(j) + "&last-name=" + letter
						+ "&student=on";
				driver.navigate().to(URLPage);
				List<WebElement> TRs = driver.findElements(By.cssSelector("tr"));
				for (int k = 1; k < TRs.size(); k++) {
					List<WebElement> TDs = TRs.get(k).findElements(By.cssSelector("td"));
					if(TDs.size()<5) continue;
					System.out.println("j:" + j + " k: " + k+ " email "+TDs.get(6).getText()+ " name "+TDs.get(1).getText());
					try {
						PreparedStatement pstmt = conn
								.prepareStatement("INSERT OR IGNORE INTO emulist (email,firstname, lastname) VALUES(?,?,?);");
						pstmt.setString(1, TDs.get(6).getText());
						pstmt.setString(2, TDs.get(1).getText());
						pstmt.setString(2, TDs.get(2).getText());
						pstmt.execute();

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

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
