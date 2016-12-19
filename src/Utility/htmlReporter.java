package Utility;

import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class htmlReporter {

	ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extent.html");
	ExtentXReporter extentx ;
	
	public void extentXReportConfif(){
		// project name
		extentx.config().setProjectName("ProjectName");

		// report or build name
		extentx.config().setReportName("Clove Dental");

		/*// server URL
		// ! must provide this to be able to upload snapshots
		extentx.config().setServerUrl("http://localhost:1337");*/
	}
	
	
	public void htmlReportConfig(){
		// make the charts visible on report open
		htmlReporter.config().setChartVisibilityOnOpen(true);

		
		// report title
		htmlReporter.config().setDocumentTitle("aventstack - ExtentReports");

		// encoding, default = UTF-8
		htmlReporter.config().setEncoding("UTF-8");

		// protocol (http, https)
		htmlReporter.config().setProtocol(Protocol.HTTPS);

		// report or build name
		htmlReporter.config().setReportName("Build-1224");

		// chart location - top, bottom
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);

		// theme - standard, dark
		htmlReporter.config().setTheme(Theme.STANDARD);

		
	}
	
	
}
