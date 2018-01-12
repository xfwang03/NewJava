package Hello;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Lu {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "//home/xiangfeng/Downloads/chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.navigate().to("http://www.google.com");
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

		
		for (int i = 1; i < 26; i++) {
			char leI = (char) ('a' + i);
			String letterI = String.valueOf(leI);
			
			for (int j = 1; j <26; j++) {
				char leJ = (char) ('a' + j);
				String letterJ = String.valueOf(leJ);
				String URLPage = "https://directory.lafayette.edu/search/people?query=" +letterI+letterJ;
				driver.navigate().to(URLPage);
				List<WebElement> Rows = driver.findElements(By.className("views-row"));
				for (int k = 1; k < Rows.size(); k++) {
					List<WebElement> Name = Rows.get(k).findElements(By.cssSelector("h4"));
					if(Name.size()!=1) continue;
					StringBuilder contents = new StringBuilder(); //Stores
					List<WebElement> divs = Rows.get(k).findElements(By.cssSelector("div"));
					for(int l=0;l<divs.size();l++) {
						contents.append(divs.get(l).getText());
						contents.append("  ");
					}
					
					
					Pattern pat = Pattern.compile(pattern);
			        //Matches contents against the given Email Address Pattern
			        Matcher match = pat.matcher(contents);
			        //If match found, append to emailAddresses
			        while(match.find()) {
			        	System.out.println(match.group());
			        	try {
							PreparedStatement pstmt = conn
									.prepareStatement("INSERT OR IGNORE INTO lu (email,name) VALUES(?,?);");
							pstmt.setString(1, match.group());
							pstmt.setString(2,  Name.get(0).getText());
							pstmt.execute();

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	
			        	
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
