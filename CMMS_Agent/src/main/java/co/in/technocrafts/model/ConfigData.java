package co.in.technocrafts.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.MDC;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@PropertySource("file:${KIOSK_HOME}Setting/cmms_agent.properties")
public class ConfigData {

	
	String baseDirectory;
	String zipToCMMS;
	String zipFromCMMS;
	String copyZipFromCMMS;
	String unzipPatch;
	String terminalId;
	String cmmsServerUrl;
	String cmmsServerPushUrl;
	String cmmsServerPullUrl;
	String screenshot;
	String screenshotFileName;
	String screenshotFileFormat;
	String configFileToPush;
	
	String loghome;
	
	String KIOSK_HOME;
	String logfilename;
	String pushDataFile;
	String pushResult;
	String reStartResult;
	String AppVersion;
	String otherData;
	String connectionRequestTimeout;
	String connectTimeout;
	String readTimeout;
	String systemEventLogCommand;
	String cmmsServerUpdateAgentVersion;
	String zipPassword;
	

	public String getZipPassword() {
		return zipPassword;
	}
	public void setZipPassword(String zipPassword) {
		this.zipPassword = zipPassword;
	}
	public String getCmmsServerUpdateAgentVersion() {
		return cmmsServerUpdateAgentVersion;
	}
	public void setCmmsServerUpdateAgentVersion(String cmmsServerUpdateAgentVersion) {
		this.cmmsServerUpdateAgentVersion = cmmsServerUpdateAgentVersion;
	}
	public String getSystemEventLogCommand() {
		return systemEventLogCommand;
	}
	public void setSystemEventLogCommand(String systemEventLogCommand) {
		this.systemEventLogCommand = systemEventLogCommand;
	}
	public String getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}
	public void setConnectionRequestTimeout(String connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}
	public String getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(String connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public String getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(String readTimeout) {
		this.readTimeout = readTimeout;
	}
	public String getOtherData() {
		return otherData;
	}
	public void setOtherData(String otherData) {
		this.otherData = otherData;
	}
	public String getCopyZipFromCMMS() {
		return copyZipFromCMMS;
	}
	public void setCopyZipFromCMMS(String copyZipFromCMMS) {
		this.copyZipFromCMMS = copyZipFromCMMS;
	}
	public String getAppVersion() {
		return AppVersion;
	}
	public void setAppVersion(String appVersion) {
		AppVersion = appVersion;
	}
	public String getReStartResult() {
		return reStartResult;
	}
	public void setReStartResult(String reStartResult) {
		this.reStartResult = reStartResult;
	}
	public String getPushResult() {
		return pushResult;
	}
	public void setPushResult(String pushResult) {
		this.pushResult = pushResult;
	}
	public String getPushDataFile() {
		return pushDataFile;
	}
	public void setPushDataFile(String pushDataFile) {
		this.pushDataFile = pushDataFile;
	}
	public String getLogfilename() {
		return logfilename;
	}
	public void setLogfilename(String logfilename) {
		this.logfilename = logfilename;
	}
	public String getKIOSK_HOME() {
		return KIOSK_HOME;
	}
	public void setKIOSK_HOME(String kIOSK_HOME) {
		KIOSK_HOME = kIOSK_HOME;
	}
	public String getLoghome() {
		return loghome;
	}
	public void setLoghome(String loghome) {
		this.loghome = loghome;
	}
	


	String fileToPushApplicationLog;
	
	String fileToPushDeviceLog;

	String fileToPushApplicationErrorLog;
String fileToPushReports;
	

	public String getFileToPushApplicationLog() {
		return fileToPushApplicationLog;
	}
	public void setFileToPushApplicationLog(String fileToPushApplicationLog) {
		this.fileToPushApplicationLog = fileToPushApplicationLog;
	}
	public String getFileToPushDeviceLog() {
		return fileToPushDeviceLog;
	}
	public void setFileToPushDeviceLog(String fileToPushDeviceLog) {
		this.fileToPushDeviceLog = fileToPushDeviceLog;
	}
	public String getFileToPushApplicationErrorLog() {
		return fileToPushApplicationErrorLog;
	}
	public void setFileToPushApplicationErrorLog(String fileToPushApplicationErrorLog) {
		this.fileToPushApplicationErrorLog = fileToPushApplicationErrorLog;
	}
	public String getFileToPushReports() {
		return fileToPushReports;
	}
	public void setFileToPushReports(String fileToPushReports) {
		this.fileToPushReports = fileToPushReports;
	}


	
	
	

	public String getBaseDirectory() {
		return baseDirectory;
	}
	public void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	public String getCmmsServerPullUrl() {
		return cmmsServerPullUrl;
	}
	public void setCmmsServerPullUrl(String cmmsServerPullUrl) {
		this.cmmsServerPullUrl = cmmsServerPullUrl;
	}
	public String getCmmsServerPushUrl() {
		return cmmsServerPushUrl;
	}
	public void setCmmsServerPushUrl(String cmmsServerPushUrl) {
		this.cmmsServerPushUrl = cmmsServerPushUrl;
	}
	public String getScreenshot() {
		return screenshot;
	}
	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}
	public String getScreenshotFileName() {
		return screenshotFileName;
	}
	public void setScreenshotFileName(String screenshotFileName) {
		this.screenshotFileName = screenshotFileName;
	}
	public String getScreenshotFileFormat() {
		return screenshotFileFormat;
	}
	public void setScreenshotFileFormat(String screenshotFileFormat) {
		this.screenshotFileFormat = screenshotFileFormat;
	}
	public String getCmmsServerUrl() {
		return cmmsServerUrl;
	}
	public void setCmmsServerUrl(String cmmsServerUrl) {
		this.cmmsServerUrl = cmmsServerUrl;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	
	public String getZipToCMMS() {
		return zipToCMMS;
	}
	public void setZipToCMMS(String zipToCMMS) {
		this.zipToCMMS = zipToCMMS;
	}
	public String getZipFromCMMS() {
		return zipFromCMMS;
	}
	public void setZipFromCMMS(String zipFromCMMS) {
		this.zipFromCMMS = zipFromCMMS;
	}
	public String getUnzipPatch() {
		return unzipPatch;
	}
	public void setUnzipPatch(String unzipPatch) {
		this.unzipPatch = unzipPatch;
	}
	

	public String getConfigFileToPush() {
		return configFileToPush;
	}
	public void setConfigFileToPush(String configFileToPush) {
		this.configFileToPush = configFileToPush;
	}
	
	
	public void setLogFilePath()
	{
        GregorianCalendar date = new GregorianCalendar();  
		
		int year = date.get(Calendar.YEAR);
		int day = date.get(Calendar.DATE);

		int month = date.get(Calendar.MONTH);
        month = month+1;
        
        String currentDateFormat ="/"+year+"/"+(month<10?("0"+month):(month))+"/"+"0"+day+"/";
		
        System.setProperty("currentDateFormat",currentDateFormat);
        
     //   String logFilePath=System.getProperty("baseDirectory")+System.getProperty("loghome")+System.getProperty("currentDateFormat")+System.getProperty("logfilename");
       
        
        String logFilePath=getBaseDirectory()+getLoghome()+System.getProperty("currentDateFormat")+getLogfilename();
        
		MDC.put("logging.file.name",logFilePath+".txt");
		MDC.put("logging.pattern.rolling-file-name",logFilePath+"-%d{yyyy-MM-dd}.%i.txt");

        
       // System.setProperty("logging.file.name",logFilePath+".txt");
        //System.setProperty("logging.pattern.rolling-file-name",logFilePath+"-%d{yyyy-MM-dd}.%i.txt");

System.out.println(logFilePath);
	}
	
	@Override
	public String toString() {
		return "ConfigData [baseDirectory=" + baseDirectory + ", zipToCMMS=" + zipToCMMS + ", zipFromCMMS="
				+ zipFromCMMS + ", copyZipFromCMMS=" + copyZipFromCMMS + ", unzipPatch=" + unzipPatch + ", terminalId="
				+ terminalId + ", cmmsServerUrl=" + cmmsServerUrl + ", cmmsServerPushUrl=" + cmmsServerPushUrl
				+ ", cmmsServerPullUrl=" + cmmsServerPullUrl + ", screenshot=" + screenshot + ", screenshotFileName="
				+ screenshotFileName + ", screenshotFileFormat=" + screenshotFileFormat + ", configFileToPush="
				+ configFileToPush + ", loghome=" + loghome + ", KIOSK_HOME=" + KIOSK_HOME + ", logfilename="
				+ logfilename + ", pushDataFile=" + pushDataFile + ", pushResult=" + pushResult + ", reStartResult="
				+ reStartResult + ", AppVersion=" + AppVersion + ", otherData=" + otherData
				+ ", connectionRequestTimeout=" + connectionRequestTimeout + ", connectTimeout=" + connectTimeout
				+ ", readTimeout=" + readTimeout + ", systemEventLogCommand=" + systemEventLogCommand
				+ ", cmmsServerUpdateAgentVersion=" + cmmsServerUpdateAgentVersion + ", zipPassword=" + zipPassword
				+ ", fileToPushApplicationLog=" + fileToPushApplicationLog + ", fileToPushDeviceLog="
				+ fileToPushDeviceLog + ", fileToPushApplicationErrorLog=" + fileToPushApplicationErrorLog
				+ ", fileToPushReports=" + fileToPushReports + "]";
	}
	
	
	
}
