package ApplicationFunctions;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class PropertyLoader {

	// properties
		private Properties CONFIG;
		private Properties OR;
		private Properties EXCEL_PATH;
		
		public PropertyLoader(){
			try{
				FileInputStream fs = new FileInputStream(
						"E:\\Selenium\\Workspace\\TestProjectHybrid\\src\\OR\\config.properties");
				CONFIG = new Properties();
				CONFIG.load(fs);

				fs = new FileInputStream("E:\\Selenium\\Workspace\\TestProjectHybrid\\src\\OR\\or.properties");
				OR = new Properties();
				OR.load(fs);
				
				fs = new FileInputStream("E:\\Selenium\\Workspace\\TestProjectHybrid\\src\\OR\\excelPath.properties");
				EXCEL_PATH = new Properties();
				EXCEL_PATH.load(fs);
				
				String log4jConfPath = "E:\\Selenium\\Workspace\\TestProjectHybrid\\src\\OR\\log4j.properties";
				PropertyConfigurator.configure(log4jConfPath);
			}catch(Exception e ){
				
			}
		}

		public Properties getCONFIG() {
			return CONFIG;
		}
		
		public Properties getOR() {
			return OR;
		}

		public Properties getEXCEL_PATH() {
			return EXCEL_PATH;
		}
	
}
