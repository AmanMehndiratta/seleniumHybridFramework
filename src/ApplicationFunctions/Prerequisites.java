package ApplicationFunctions;

import static ApplicationFunctions.DriverScript.APP_LOGS;

import org.openqa.selenium.WebDriver;

import Constants.ConstantPaths;
import Constants.ConstantVerificationElements;
import Constants.Constants;

public class Prerequisites extends Keywords{

	
	public static String data;
	public static String object;
	public WebDriver driver;
	//public FirefoxDriver driver;
	public static String url;
	
	
	Keywords keyword = new Keywords();
	
	public String loginIntoEMR(String data, String object, String url,String Username, String Password){
		keyword.navigate(object, url);
		
		keyword.waitForPageLoad(object, "50000");
		
		keyword.writeInInput("emr_username_login_input", Username);
		keyword.writeInInput("emr_passwd_login_input", "123456");
		keyword.clickButton("emr_button_login", data);
		
		return Constants.KEYWORD_PASS + " -- Logged into EMR";
	}
	
	
	public String addAppointment(String data, String object, String url){
		
		//navigating user to doctor DB
		String loadHomePageURL = keyword.navigate(object, url);
		APP_LOGS.debug("Checking if user is logged in" + loadHomePageURL);
		
		String title;
		title = keyword.verifyTitle(ConstantVerificationElements.prm_Login_Screen_Title, data);
		
		if(title.equals(ConstantVerificationElements.prm_Login_Screen_Title)){
			
			//write code for login and then go to add apt.
			
		}
		
		
		//verifying if user is logged in
		if(keyword.getText(ConstantPaths.doctor_DB_TodaysAppointment, data).equals(ConstantVerificationElements.doctor_DB_TodaysAppt_Text)){
			APP_LOGS.debug("User is logged in");
			
			//keyword.clickButton(, data)
		}
		
		
		
		return "static return";
	}
	
}
