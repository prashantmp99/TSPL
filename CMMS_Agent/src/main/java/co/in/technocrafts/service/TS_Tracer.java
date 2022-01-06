package co.in.technocrafts.service;


import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.io.File;


@Service
public class TS_Tracer {
	
	
	public  boolean WriteToLogs( String fileRootPath,String filename,String className  ,String methodName ,String dataToWrite) {
        try {
        	
        	GregorianCalendar dateForFolder = new GregorianCalendar();  
    		
    		int year = dateForFolder.get(Calendar.YEAR);
    		int day = dateForFolder.get(Calendar.DATE);

    		int month = dateForFolder.get(Calendar.MONTH);
            month = month+1;
            
            String currentDateFolder =""+year+"/"+(month<10?("0"+month):(month))+"/"+(day<10?("0"+day):(day));

        	
        	
        	
             String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            // String filename = filename + date + ".txt";
             //File f = new File(filename);
             //File parentpath = f.getParentFile();
             File fDirectory = new File(fileRootPath+"/"+currentDateFolder);
            if (!fDirectory.exists()) {
                fDirectory.mkdirs();
              //  System.out.println("$$$ make directory ");
            }
filename = fileRootPath+"/"+currentDateFolder +"/" +filename +".txt";
//System.out.println("##"+filename);
             File file = new File(filename);
            file.createNewFile();
             FileWriter fw = new FileWriter(file, true);
             SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
             Date now = new Date();
             String strDate = sdfDate.format(now);
             BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\n" + strDate + "\t" + className + "\t" + methodName+ "\t" + dataToWrite);
            bw.close();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    

}
