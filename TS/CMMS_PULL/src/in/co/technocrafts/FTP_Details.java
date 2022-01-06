/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.technocrafts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author testbuild
 */
public class FTP_Details {

    FTPClient ftp = null;
    All_FileUtility objAll_FileUtility = null;
    UnzipUtility objUnzipUtility = null;
    RunShellScript objRunShellScript = null;

    String strpathDirctory = new String("/home/TS/CMMS/");

    String strServer_IP = (String) "";
    String strServer_Port = (String) "";
    String strServer_Username = (String) "";
    String strServer_Password = (String) "";
    String strServer_FilePath = (String) "";
    String strLocal_FilePath = (String) "";
    String strVersion = "";
    String strTrace_Path = "";
    String strPatch_installedList = "";
    String strUnzipfilePth = "";

    int intPull_time_interval = 0;

    public String getConfigDetails() {
        boolean boolFlag = false;

        try {

            File filehomeConfig = new File(strpathDirctory + "acu/config.json");
            if (filehomeConfig.exists()) {
                boolFlag = true;
            }

            if (boolFlag == false) {
                throw new Exception();
            }

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filehomeConfig));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject jsonObjACU_Config_File = (JSONObject) jsonObject.get("ACU_Config_File");
            JSONObject CMMS = (JSONObject) jsonObjACU_Config_File.get("CMMS");
            JSONObject jsonObjTracingUtility_Config = (JSONObject) jsonObjACU_Config_File.get("TracingUtility_Config");

            String Server_IP = (String) CMMS.get("Server_IP");
            String Server_Port = (String) CMMS.get("Server_Port");
            String Server_Username = (String) CMMS.get("Server_Username");
            String Server_Password = (String) CMMS.get("Server_Password");
            String Server_FilePath = (String) CMMS.get("Server_FilePath");
            String Local_FilePath = (String) CMMS.get("Local_FilePath");
            String Version = (String) CMMS.get("Version");
            String Trace_Path = (String) jsonObjTracingUtility_Config.get("Trace_Path");

            String Patch_installedList = (String) CMMS.get("Patch_installedList");
            String Local_UnzipPath = (String) CMMS.get("Local_UnzipPath");
            String Pull_time_interval = (String) CMMS.get("Pull_time_interval");

            strServer_IP = Server_IP;
            strServer_Port = Server_Port;
            strServer_Username = Server_Username;
            strServer_Password = Server_Password;
            strServer_FilePath = Server_FilePath;
            strLocal_FilePath = Local_FilePath;
            strVersion = Version;
            strTrace_Path = Trace_Path;
            strPatch_installedList = Patch_installedList;
            intPull_time_interval = Integer.parseInt(Pull_time_interval);
            strUnzipfilePth = Local_UnzipPath;

            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Version: " + strVersion);
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Server_IP:  " + strServer_IP);
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Server_Port: " + strServer_Port);
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Server_Username:  " + strServer_Username);
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Server_Password: " + strServer_Password);
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Server_FilePath:  " + strServer_FilePath);
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Local_FilePath:  " + strLocal_FilePath);
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Local_UnzipPath:  " + strUnzipfilePth);

            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Patch_installedList:  " + strPatch_installedList);
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Pull_time_interval:  " + Pull_time_interval);

            return "true";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "MISC";
    }

    public FTP_Details() {
        getConfigDetails();

        objUnzipUtility = new UnzipUtility();
        objAll_FileUtility = new All_FileUtility();
        objRunShellScript = new RunShellScript();
    }

    public boolean connectToFTPServer(String serverIP, String serverPort, String serverUserName, String serverPassword) {
        try {

            ftp = new FTPClient();
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            int reply;
            ftp.connect(serverIP, Integer.parseInt(serverPort));
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Exception :  " + "Exception in connecting to FTP Server");

            }
            ftp.login(serverUserName, serverPassword);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();

            return true;
        } catch (Exception e) {
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Exception :  " + e.getMessage());

            return false;
        }
    }

    public String readInstallFileList(String fileName) {

        try {
            String data = "";
            data = new String(Files.readAllBytes(Paths.get(fileName)));

            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "List of Patch file  " + data);

            return data;
        } catch (Exception e) {
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, e.getMessage());

        }
        return "";
    }

    public String[] listFTPFilesOnServer(String directory) {
        // lists files and directories in the current working directory
        TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Files on Server");

        try {
            FTPFile[] files = ftp.listFiles(directory);
            String[] fileList = new String[files.length];
            int fileCount = 0;
            for (FTPFile file : files) {

                String strFileName = file.getName();
                fileList[fileCount++] = strFileName;
                TS_Tracer.WriteToDeviceLogs(strTrace_Path, strFileName);

            }

            return fileList;
        } catch (Exception e) {

        }
        return new String[0];
    }

    public boolean downloadFTPFileFromServer(String serverFile, String localFile) {

        TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Download file from Server ");
        TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Server File Path " + serverFile);
        TS_Tracer.WriteToDeviceLogs(strTrace_Path, "Local File Path " + localFile);

        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            this.ftp.retrieveFile(serverFile, fos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updatePatchListFile(String patchListFile, String updatepatchName) {
        try {
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "update patch list " + updatepatchName);

            File file = new File(patchListFile);
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);

            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("," + updatepatchName);
            bw.close();
        } catch (Exception e) {
            TS_Tracer.WriteToDeviceLogs(strTrace_Path, "update patch list " + e.getMessage());

        }

    }

    public void disconnectToFTPServer() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
            }
        }
    }

    public void downloadDataFromServer() {
        if (connectToFTPServer(strServer_IP, strServer_Port, strServer_Username, strServer_Password)) {

            String[] filesOnServer = listFTPFilesOnServer(strServer_FilePath);

            if (filesOnServer.length > 0) {
                String strPatchInstalledOnLocal = readInstallFileList(strPatch_installedList);

                if (!(strPatchInstalledOnLocal.contains(filesOnServer[0]))) {
                    if (downloadFTPFileFromServer(strServer_FilePath + "/" + filesOnServer[0], strLocal_FilePath + "/" + filesOnServer[0])) {
                       // updatePatchListFile(strPatch_installedList, filesOnServer[0]);
                    }
                }

            }
        }

        disconnectToFTPServer();
    }

    public void runPatchFile() {
        String[] listOfFiles = objAll_FileUtility.getcontentsInDirectory(strLocal_FilePath, strTrace_Path);

        if (listOfFiles.length > 0) {
             objAll_FileUtility.deletefilesInDirectory(strUnzipfilePth, strTrace_Path);

            if (objUnzipUtility.unzip(strLocal_FilePath + "/" + listOfFiles[0], strUnzipfilePth, strTrace_Path)) {
                
                objAll_FileUtility.deletefilesInDirectory(strLocal_FilePath, strTrace_Path);
                        updatePatchListFile(strPatch_installedList, listOfFiles[0]);

                String[] listOfFilesinUnzipFolder = objAll_FileUtility.getcontentsInDirectory(strUnzipfilePth, strTrace_Path);

                String shFilename = "";

                for (String runFileName : listOfFilesinUnzipFolder) {
                    if (runFileName.contains(".sh")) {
                        shFilename = runFileName;
                        break;
                    }
                }

                objRunShellScript.runScript("sh " + strUnzipfilePth + shFilename, strTrace_Path);
            }
        }
    }

    public void runRMMSPull() {
        String[] listOfFiles = objAll_FileUtility.getcontentsInDirectory(strLocal_FilePath, strTrace_Path);

        if (listOfFiles.length > 0) {
            runPatchFile();
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               // String[] listOfFilesInUnzipPath = objAll_FileUtility.getcontentsInDirectory(strUnzipfilePth, strTrace_Path);
                String[] listOfFilesInLocalPath = objAll_FileUtility.getcontentsInDirectory(strLocal_FilePath, strTrace_Path);

                if (( listOfFilesInLocalPath.length <1)) {
                    downloadDataFromServer();
                }
            }
        }, 0, intPull_time_interval * 60 * 1000);//wait 0 ms before doing the action and do it evry 1000ms (1second)

    }

    public static void main(String args[]) {
        FTP_Details objFTP_Details = new FTP_Details();

        objFTP_Details.runRMMSPull();

//      
//      
//      Timer objTimer =new Timer();
//      
//      TimerTask delayedThreadStartTask = new TimerTask() {
//        @Override
//        public void run() {
//
//            
//           new Thread(new Runnable() {
//                @Override
//                public void run() {
//                        System.out.println("hii");
//                    try
//                    {
//                        objFTP_Details.downloadDataFromServer();
//                    }
//                    catch(Exception e)
//                    {
//                        
//                    }
//                }
//            }).start();
//        }
//    };
//      
//          objTimer.schedule(delayedThreadStartTask, objFTP_Details.intPull_time_interval * 60 * 1000); //1 minute
    }

}
