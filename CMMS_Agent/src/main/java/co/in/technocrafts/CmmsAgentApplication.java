package co.in.technocrafts;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.objenesis.instantiator.util.ClassUtils;

import co.in.technocrafts.model.ConfigData;
import co.in.technocrafts.service.FileService;
import co.in.technocrafts.service.TS_Tracer;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;



@SpringBootApplication
public class CmmsAgentApplication implements CommandLineRunner{
	
   // private static  Logger objLOGGER=LoggerFactory.getLogger(CmmsAgentApplication.class);
    
    
    @Autowired
    ConfigData objConfigData;
    @Autowired
    TS_Tracer objTS_Tracer;
	public static void main(String[] args) {
		
//		SystemInfo si = new SystemInfo();
//		HardwareAbstractionLayer hal = si.getHardware();
//		OperatingSystem objOS = si.getOperatingSystem();
//
//		long availableMemory = hal.getMemory().getAvailable();
//		
//		String ramData="RAM DATA "+ hal.getMemory().toString();
//		String  diskData="DISK DATA  " + hal.getDiskStores();
//		String networkData="Network DATA  "+hal.getNetworkIFs();
//		String envirmentalVariable = "Environment variable  "+System.getenv("KIOSK_HOME");;
//		
//		
//		System.out.println("********************");
//		
//		//System.out.println(ramData+"\t"+diskData+"\t"+networkData+"\t"+envirmentalVariable);
//		
//		
//		
//		System.out.println("********************");
//
//		
//		//File[] files = new File("/home/TS/KIOSK/cmms/zipfromcmms").listFiles((dir, name) -> name.endsWith(".sh"));
//
//		
//		GregorianCalendar date = new GregorianCalendar();  
//		
//		int year = date.get(Calendar.YEAR);
//		int day = date.get(Calendar.DATE);
//
//		int month = date.get(Calendar.MONTH);
//        month = month+1;
//        
//        String currentDateFormat =""+year+"/"+(month<10?("0"+month):(month))+"/"+(day<10?("0"+day):(day));
//		
  //      System.setProperty("currentDateFormat",currentDateFormat);
        
		
		//test commit
        String KIOSK_HOME= System.getenv("KIOSK_HOME");
        System.setProperty("KIOSK_HOME",KIOSK_HOME);

       // System.out.println(System.getProperty("loghome"));

       // System.out.println(KIOSK_HOME);
        
        
       // objConfigData.setLogFilePath();
     //   objLOGGER=LoggerFactory.getLogger(CmmsAgentApplication.class);
		
		SpringApplication.run(CmmsAgentApplication.class, args);
       System.setProperty("java.awt.headless", "false");
	//	System.out.println("Welcome To Cmms agent");
		//CmmsAgentApplication objCmmsAgentApplication =new CmmsAgentApplication();
		
		//objLOGGER.warn(""+objCmmsAgentApplication.objConfigData);
		
		//System.out.println(objCmmsAgentApplication.objConfigData);
		
		//FileService objFileService=new FileService();
	//	objFileService.createZipfile("/home/TS/cmms/cmmslogdir/cmmsAgent*.txt", "/home/TS/Downloads/jar/cmmsAgent.zip");
		//String s = "2021-07-05T00:00:00";
		//SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		//try {
		//	Date dtIn = inFormat.parse(s);
			
		  //  Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(dtIn.toString());  
		    
		    
		    
		    
		    
		//    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		   // Date today = new Date();

		//    Date todayWithZeroTime = formatter.parse(formatter.format(dtIn));
		//	System.out.println("dddd "+dtIn+"*** "+todayWithZeroTime);
		//} catch (ParseException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
		//} 
		
	}
	@Override
	public void run(String... args) throws Exception {


//Process p = Runtime.getRuntime().exec("shutdown -r now");

		// TODO Auto-generated method stub
	//	objLOGGER.warn(""+objConfigData);;
		
		String filefolder = objConfigData.getKIOSK_HOME()+"/"+objConfigData.getLoghome();
		objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", objConfigData.toString());
		
	}
	
	
	
}
