package co.in.technocrafts.service;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.ant.compress.taskdefs.Unzip;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import co.in.technocrafts.model.ConfigData;
import co.in.technocrafts.model.RequestFromServer;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

@Service
public class FileService {
	

	@Autowired
	public ConfigData objConfigData;
	
	public Boolean fullScreenShot(String filenameWithPath)
	{
		try {
            Robot robot = new Robot();
            String format = "jpg";
            String fileName = "FullScreenshot." + format;
             
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, format, new File(filenameWithPath));
             
          //  System.out.println("A full screenshot saved!");
            
            return true;
        } catch (Exception ex) {
            System.err.println(ex);
            return false;
        }
		
	}
	
	
	public Boolean fullScreenShot(String filename,String fileformat)
	{
		try {
            Robot robot = new Robot();
           /// String format = "jpg";
          ///  String fileName = "FullScreenshot." + format;
             
           // System.out.println(filename + "  "+fileformat);
            
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            ImageIO.write(screenFullImage, fileformat, new File(filename));
             
        //    System.out.println("A full screenshot saved!");
            
            return true;
        } catch (Exception ex) {
            System.err.println(ex);
            return false;
        }
		
	}
	
	
	public static void saveFile(String uploadDir, String fileName,
            MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }

	
	public static boolean Unzipfile(String theZIPFile,String theTargetFolder)
	{
		System.out.println(theZIPFile);
		System.out.println(theTargetFolder);
		File filetheZIPFile= new File(theZIPFile);
		File filetheTargetFolder =new File(theTargetFolder);
		
		Unzip unzipper = new Unzip();
		unzipper.setSrc(filetheZIPFile);
		unzipper.setDest(filetheTargetFolder);
		unzipper.execute();
		
		return true;
	}
	
	
	public static boolean excuteCommand(String filePath) throws IOException{
		//System.out.println(filePath);
		
	    //File file = new File(filePath);
	    
	    String[] cmdScript = new String[]{"/bin/bash", filePath};  
	    Process procScript = Runtime.getRuntime().exec(cmdScript); 
//	    if(!file.isFile()){
//	        throw new IllegalArgumentException("The file " + filePath + " does not exist");
//	    }
//	    if(isLinux()){
//	        Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", filePath}, null);
//	    }else if(isWindows()){
//	        Runtime.getRuntime().exec("cmd /c start " + filePath);
//	    }
	    
	    return true;
	}
	
	public static boolean excuteOnlyCommand(String filePath) throws IOException{
		//System.out.println(filePath);
		
	  //  File file = new File(filePath);
	    
	   // String[] cmdScript = new String[]{"/bin/bash", "-c", filePath.split(",")};  
		 String[] cmdScript = filePath.split(",");
	    Process procScript = Runtime.getRuntime().exec(cmdScript); 
//	    if(!file.isFile()){
//	        throw new IllegalArgumentException("The file " + filePath + " does not exist");
//	    }
//	    if(isLinux()){
//	        Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", filePath}, null);
//	    }else if(isWindows()){
//	        Runtime.getRuntime().exec("cmd /c start " + filePath);
//	    }
	    
	    return true;
	}
	
	
	
	
	public static boolean isLinux(){
	    String os = System.getProperty("os.name");  
	    return os.toLowerCase().indexOf("linux") >= 0;
	}

	public static boolean isWindows(){
	    String os = System.getProperty("os.name");
	    return os.toLowerCase().indexOf("windows") >= 0;
	}
	
	
	public void test2()
	{
		File[] files = new File("path/to/dir").listFiles((dir, name) -> name.endsWith(".sh"));
	}
	
	public boolean createZipfile(String fromfile,String tofile)
	{
		try
		{
			
			FileOutputStream fos = null;
	        ZipOutputStream zipOut = null;
	        FileInputStream fis = null;
	        try {
	            fos = new FileOutputStream(tofile);
	            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
	            File input = new File(fromfile);
	            fis = new FileInputStream(input);
	            ZipEntry ze = new ZipEntry(input.getName());
	        //    System.out.println("Zipping the file: "+input.getName());
	            zipOut.putNextEntry(ze);
	            byte[] tmp = new byte[4*1024];
	            int size = 0;
	            while((size = fis.read(tmp)) != -1){
	                zipOut.write(tmp, 0, size);
	            }
	            zipOut.flush();
	            zipOut.close();
	        } catch (FileNotFoundException e) {
	           
	            e.printStackTrace();
	        } catch (IOException e) {
	           
	            e.printStackTrace();
	        } finally{
	            try{
	                if(fos != null) fos.close();
	                if(fis != null) fis.close();
	            } catch(Exception ex){
	                 
	            }
	        }
			
			return true;

		}
		catch(Exception e)
		{
			return false;
		}
		
		
		
		
	}
	public boolean deleteFilesIndirectory(String directory)
	{
		File file=new File(directory);
		
		try {
			FileUtils.cleanDirectory(file);
			return true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;

		} 
		
	}
	
	
	
	public static void main(String args)
	{
		FileService objFileService=new FileService();
		//objFileService.createZipfile("/home/TS/Downloads/1.jpg", "/home/TS/Downloads/55.zip");
		
	}
	
	public boolean createZipFileworking(String filename, byte[] input)
	{
		/*
		 * filename = filename+"abc.zip";
		 * 
		 * System.out.println("filename "+filename + " input "+input.length); try {
		 * 
		 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); ZipOutputStream zos
		 * = new ZipOutputStream(baos); ZipEntry entry = new ZipEntry(filename);
		 * entry.setSize(input.length); zos.putNextEntry(entry); zos.write(input);
		 * zos.closeEntry(); zos.close(); return true; } catch(Exception e) {
		 * e.printStackTrace(); return false; }
		 */
		
		
		
		try
		{
		
			
			
			
			
		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(input));
		ZipEntry entry = null;
		while ((entry = zipStream.getNextEntry()) != null) {

		    String entryName = entry.getName();

		    FileOutputStream out = new FileOutputStream(filename+entryName);

		    byte[] byteBuff = new byte[4096];
		    int bytesRead = 0;
		    while ((bytesRead = zipStream.read(byteBuff)) != -1)
		    {
		        out.write(byteBuff, 0, bytesRead);
		    }

		    out.close();
		    zipStream.closeEntry();
		}
		zipStream.close(); 
		
		
		return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	
	public boolean createCopyOfZipFile(String zipfile,String distDirectory)
	{
	
		
		
		try
		{
		
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
         File fDirectory = new File(distDirectory+"/"+currentDateFolder+"/");
        if (!fDirectory.exists()) {
            fDirectory.mkdirs();
          //  System.out.println("$$$ make directory ");
        }
        
        
        
        String[] pathnames;
        pathnames = fDirectory.list();
        
        
        FileUtils.copyFileToDirectory(new File(zipfile),new File(distDirectory+"/"+currentDateFolder+"/"+((pathnames.length)+1)+"/"));

		}
		
		
		
		catch(Exception e)
		{
			
			e.printStackTrace();
			
		}
		
		
		return true;
	}
	
	
	public boolean createZipFile(String filename, byte[] input)
	{
		
		
		
		try
		{
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ZipOutputStream zos = new ZipOutputStream(baos);
	    ZipEntry entry = new ZipEntry("filename");
	    entry.setSize(input.length);
	    zos.putNextEntry(entry);
	    zos.write(input);
	    zos.closeEntry();
	    zos.close();
	    byte[] zipBytes= baos.toByteArray();
	    
	    
	   // InputStream is = new ByteArrayInputStream(baos.toByteArray());
      //  StreamedContent zipedFile= new DefaultStreamedContent(is,   "application/zip", fileName+".zip", Charsets.UTF_8.name());
        
		
	    
	 //   ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(input));
	   // ZipParameters parameters = new ZipParameters();
	   // parameters.setFileNameInZip("bank.zip");
	   // new ZipFile(filename)

	    FileUtils.writeByteArrayToFile(new File(filename), input);
	    
       
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		return true;
	}
	
	
	
	
public  void compressAllFiles04_11_21(String dirPathForLog,String zipFileName) {
		
		String zipFile = zipFileName ;//+"/output.zip";
        String srcDir = dirPathForLog;
         
        try {
             
            // create byte buffer
            byte[] buffer = new byte[1024];
 
            FileOutputStream fos = new FileOutputStream(zipFile);
 
            ZipOutputStream zos = new ZipOutputStream(fos);
            
            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            zipParameters.setPassword("tspl");
            File dir = new File(srcDir);
 
            File[] files = dir.listFiles();
 
            for (int i = 0; i < files.length; i++) {
                 
               // System.out.println("Adding file: " + files[i].getName());
 
                FileInputStream fis = new FileInputStream(files[i]);
 
                // begin writing a new ZIP entry, positions the stream to the start of the entry data
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                 
                int length;
 
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
 
                zos.closeEntry();
 
                // close the InputStream
                fis.close();
            }
 
            // close the ZipOutputStream
            zos.close();
             
        }
        catch (Exception ioe) {
        	
        	
        	this.createfile(objConfigData.getReStartResult(),"File Not Found  :"+dirPathForLog);
			
			
			
			
			//String absulutezipFileName=objConfigData.getZipToCMMS()+"reStartResult"+".zip" ;
			this.compressAllFiles(objConfigData.getReStartResult().substring(0, objConfigData.getReStartResult().lastIndexOf(File.separator)),zipFileName,"");
			
            System.out.println("Error creating zip file" + ioe);
        }      
	
	
	}






public  void compressAllFiles(String dirPathForLog,String zipFileName,String Password) {
	
	String zipFile = zipFileName ;//+"/output.zip";
    String srcDir = dirPathForLog;
     
    try {
         
        // create byte buffer
       // byte[] buffer = new byte[1024];

        //FileOutputStream fos = new FileOutputStream(zipFile);

        //ZipOutputStream zos = new ZipOutputStream(fos);
        
     // Creating encryption zipParameters
        // for passward protection
        ZipParameters zipParameters = new ZipParameters();

        // Setting encryption files
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Setting encryption of files to true
        zipParameters.setEncryptFiles(true);

        // Setting encryption method
        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
         
        // Set the key strength
        zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

        // Set the password
        zipParameters.setPassword(Password);

        
        
        ArrayList<File> list = new ArrayList<File>();
        File dir = new File(srcDir);
        ZipFile zipFile2 = new ZipFile(zipFileName);

        File[] files = dir.listFiles();

        
        for (int i = 0; i < files.length; i++) {
             list.add(files[i]);
           
        }

        // close the ZipOutputStream
        zipFile2.addFiles(list, zipParameters);    
        
        
    }
    catch (Exception ioe) {
    	
    	//System.exit(0);
    	//ioe.printStackTrace();
    	this.createfile(objConfigData.getReStartResult(),"File Not Found  :"+dirPathForLog);
		
		
		
		
		//String absulutezipFileName=objConfigData.getZipToCMMS()+"reStartResult"+".zip" ;
		this.compressAllFiles(objConfigData.getReStartResult().substring(0, objConfigData.getReStartResult().lastIndexOf(File.separator)),zipFileName,Password);
		
        System.out.println("Error creating zip file" + ioe);
    }      


}



public void updatePropertyRunTime()
{
	GregorianCalendar dateGC = new GregorianCalendar();  
	
	int year = dateGC.get(Calendar.YEAR);
	int day = dateGC.get(Calendar.DATE);

	int month = dateGC.get(Calendar.MONTH);
    month = month+1;
    
    String currentDateFormat =""+year+"/"+(month<10?("0"+month):(month))+"/"+day;
	
    System.setProperty("currentDateFormat",currentDateFormat);
    
    
    //System.out.println(System.getProperty("loghome"));
    
    String loghome=objConfigData.getLoghome();
    String logname=objConfigData.getLogfilename();
    String baseDirectory=objConfigData.getBaseDirectory();
    String logging_file_name=baseDirectory+"/"+loghome+"/"+currentDateFormat+"/"+logname+".txt";
    String logging_pattern_rolling_file_name=baseDirectory+"/"+loghome+"/"+currentDateFormat+"/"+logname+"-%d{yyyy-MM-dd}.%i"+".txt";
    
    System.setProperty("logging.file.name", logging_file_name);
    System.setProperty("logging.pattern.rolling-file-name", logging_pattern_rolling_file_name);
    
    
    
   // System.out.println(logging_file_name);

}

public boolean savePushDataToJsonFile(RequestFromServer objRequestFromServer,String jsonFileName)
{
	try
	{
	ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    
    
    FileOutputStream fileOutputStream = new FileOutputStream(jsonFileName);
    mapper.writeValue(fileOutputStream, objRequestFromServer);
    fileOutputStream.close();
    return true;
    
	}
	catch(Exception e)
	{
		
	}
	return false;
}

public RequestFromServer getPushDataFromJsonFile(String jsonFileName)
{
	try
	{
	ObjectMapper mapper = new ObjectMapper();

    // Read JSON file and convert to java object
    FileInputStream fileInputStream = new FileInputStream(jsonFileName);
    RequestFromServer objRequestFromServer = mapper.readValue(fileInputStream, RequestFromServer.class);
    fileInputStream.close();
    return objRequestFromServer;
	}
	catch(Exception e)
	{
		
	}
	return new RequestFromServer();
}

public boolean createfile(String fileName,String fileData)
{
	try {
	      FileWriter myWriter = new FileWriter(fileName);
	      myWriter.write(fileData);
	      myWriter.close();
	      return true;
	      
	    } catch (Exception e) {
	      
	      e.printStackTrace();

	    }
	return false;
}


public String  getSystemData()
{
	
	try
	{
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		OperatingSystem objOS = si.getOperatingSystem();
		
		String ramData="RAM DATA "+ hal.getMemory().toString();
		String  diskData="DISK DATA  " + hal.getDiskStores();
		String networkData="Network DATA  "+hal.getNetworkIFs();
		String envirmentalVariable = "Environment variable  "+System.getenv("KIOSK_HOME");;
		
		String returnString = "\n***************************************\n"+
		ramData+
		"\n***************************************\n"+
		"\n***************************************\n"+
		diskData+
		"\n***************************************\n"+
		"\n***************************************\n"+
		networkData+
		"\n***************************************\n"+
		"\n***************************************\n"+
		envirmentalVariable+
		"\n***************************************\n";
		
		
		return returnString;
	}
	catch (Exception e)
	{
		
	}
	
	return "";
}

public String getFileData(String filename)
{
    String strData=new String("");

	try
	{
		File file = new File(filename);
	        BufferedReader br
	            = new BufferedReader(new FileReader(file));
	 
	        // Declaring a string variable
	        // Consition holds true till
	        // there is character in a string
	        while ((strData = br.readLine()) != null)
	        {
	        	//System.out.println("strData "+strData);
	        	//strData=strData;
	        	break;
	        }
	 
	         // System.exit(0);
	        return strData.trim();
	}
	catch (Exception e) {
		// TODO: handle exception
	}
	return "";
}

	
}
