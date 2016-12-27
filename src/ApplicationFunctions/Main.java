package ApplicationFunctions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class Main {
    public static void main(String[] args) {
        // start reporters
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extent.html");
    
        // create ExtentReports and attach reporter(s)
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        // creates a toggle for the given test, adds all log events under it    
        ExtentTest test = extent.createTest("MyFirstTest", "Sample description");
        ExtentTest child = test.createNode("First Child Test ", "test discription");
        ExtentTest grandChild = child.createNode("Grand Child", "Grand Child discription");
        // log(Status, details)
      //  test.log(Status.INFO, "This step shows usage of log(status, details)");

        
        
        child.log(Status.PASS, "Child test passed");
        child.log(Status.ERROR, "Child Failed");
        grandChild.log(Status.FAIL, "Grand Child Failed");
        grandChild.log(Status.PASS,"grand child pass");

        
     // info(details)
       // test.info("This step shows usage of info(details)");
        
        // log with snapshot
       // test.fail("details", MediaBuilder.createScreenCaptureFromPath("screenshot.png").build());
        
        // test with snapshot
      //  test.addScreenCaptureFromPath("screenshot.png");
        
        // calling flush writes everything to the log file
        extent.flush();
    }
}