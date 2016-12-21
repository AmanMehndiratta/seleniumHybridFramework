package ApplicationFunctions;

import static ApplicationFunctions.DriverScript.APP_LOGS;

import org.openqa.selenium.firefox.FirefoxDriver;

public class Prerequisites {

	
	public static String data;
	public static String object;
	//public WebDriver driver;
	public FirefoxDriver driver;
	public static String url;
	
	
	Keywords keyword = new Keywords();
	
	
	
	public String addAppointment(String data, String object, String url){
		data=url;
	
		
		String a = keyword.navigate(object, data);
		APP_LOGS.debug(a);
		return a;
	}
	
}
