package Utility;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class TestConfig{

	static monitoringMail mail = new monitoringMail();
	public static void mailSender() throws AddressException, MessagingException{
		mail.sendMail(server, from, to, subject, messageBody, attachmentPath, attachmentName);
	}
	
	public static String server="smtp.gmail.com";
	public static String from = "shareid02@gmail.com";
	public static String password = "share123";
	public static String[] to ={"amanmehndiratt@gmail.com"};
	public static String subject = "Clove Dental PRM - Automation Scheduled Report";
	
	public static String messageBody ="PFA";
	public static String attachmentPath=System.getProperty("user.dir")+"\\Reports.zip";
	public static String attachmentName="reports.zip";
	
	
	
	//SQL DATABASE DETAILS	
	public static String driver="net.sourceforge.jtds.jdbc.Driver"; 
	public static String dbConnectionUrl="jdbc:jtds:sqlserver://192.101.44.22;DatabaseName=monitor_eval"; 
	public static String dbUserName="sa"; 
	public static String dbPassword="$ql$!!1"; 
	
	
	//MYSQL DATABASE DETAILS
	public static String mysqldriver="com.mysql.jdbc.Driver";
	public static String mysqluserName = "root";
	public static String mysqlpassword = "selenium";
	public static String mysqlurl = "jdbc:mysql://localhost:3306/8thmay2016";
	
	
	
	
	
	
	
	
	
}
