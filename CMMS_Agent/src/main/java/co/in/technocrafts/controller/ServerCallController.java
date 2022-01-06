package co.in.technocrafts.controller;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import java.util.ArrayList;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import co.in.technocrafts.CmmsAgentApplication;
import co.in.technocrafts.model.ConfigData;
import co.in.technocrafts.model.RequestFromServer;
import co.in.technocrafts.service.FileService;
import co.in.technocrafts.service.TS_Tracer;
import co.in.technocrafts.service.ZipFilePushService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;


import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;




@EnableScheduling
@RestController
public class ServerCallController {

	@Autowired
	public ConfigData objConfigData;

	@Autowired
	public FileService objFileService;

	@Autowired
	public ZipFilePushService objFilePushService;

	//private static  Logger objLOGGER = LoggerFactory.getLogger(ServerCallController.class);
	
	int firstCheckOnrestart =0;
	
	int sendVersionToCMMS = 0;
	
	@Autowired
    TS_Tracer objTS_Tracer;
	
	
	

	// @Scheduled(fixedRate = "${fixedRate.in.milliseconds}")
	
	@Scheduled(fixedRateString = "${fixedRate}")
//	@Scheduled(fixedRateString="5000")
	@GetMapping(value = "/ServerRequest")
	public void startServer() {
		String filefolder = objConfigData.getKIOSK_HOME()+"/"+objConfigData.getLoghome();

		try {
			
			//Thread.sleep(200);
			
			//objFileService.updatePropertyRunTime();
			//objConfigData.setLogFilePath();
			//String filefolder = objConfigData.getKIOSK_HOME()+"/"+objConfigData.getLoghome();

			Thread.sleep(200);
			
			//objLOGGER = LoggerFactory.getLogger(ServerCallController.class);
		

			//objLOGGER.warn(
				//	"*******************************************************************************************************");
			objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "*******************************************************************************************************");


			//objLOGGER.warn(
			//		"          ******************************************************************************           ");
			objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "*******************************************************************************************************");

			// Thread.sleep(60000);
		//	System.out.println("WE ARE IN SERVER !!!");
		//	System.out.println(objConfigData.getCmmsServerUrl());
			
			
			
			
			
		/*	HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	        httpRequestFactory.setConnectionRequestTimeout(Integer.parseInt(objConfigData.getConnectionRequestTimeout())*1000);
	        httpRequestFactory.setConnectTimeout(Integer.parseInt(objConfigData.getConnectTimeout())*1000);
	        httpRequestFactory.setReadTimeout(Integer.parseInt(objConfigData.getReadTimeout())*1000);*/
//	        
//	        
//	        
//	        
//	        RestTemplateBuilder objRestTemplateBuilder   = new RestTemplateBuilder();
//	        objRestTemplateBuilder.setConnectTimeout(Duration.ofMillis(Integer.parseInt(objConfigData.getConnectTimeout())*1000));
//	        objRestTemplateBuilder.setReadTimeout(Duration.ofMillis(Integer.parseInt(objConfigData.getReadTimeout())*1000));
//	        objRestTemplateBuilder.build();
//
//			
//			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//
//		    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
//		                    .loadTrustMaterial(null, acceptingTrustStrategy)
//	                    .build();
//			
//			
////			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
////                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
////                    .build();
//
//		    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
//
//		    CloseableHttpClient httpClient = HttpClients.custom()
//		                    .setSSLSocketFactory(csf)
//		                    .build();
//
//		    HttpComponentsClientHttpRequestFactory requestFactory =
//		                    new HttpComponentsClientHttpRequestFactory();
//
//		    requestFactory.setHttpClient(httpClient);
	        //allowAllSSL();
	        
	        
	        TrustManager[] trustAllCerts = new TrustManager[] {
	                new X509TrustManager() {
	                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                        return new X509Certificate[0];
	                    }
	                    public void checkClientTrusted(
	                            java.security.cert.X509Certificate[] certs, String authType) {
	                    }
	                    public void checkServerTrusted(
	                            java.security.cert.X509Certificate[] certs, String authType) {
	                    }
	                }
	        };  
	        SSLContext sslContext = SSLContext.getInstance("SSL");
	        sslContext.init(null, trustAllCerts, new java.security.SecureRandom()); 
	        CloseableHttpClient httpClient = HttpClients.custom()
	                .setSSLContext(sslContext)
	                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
	                .build();   
	        HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
	        customRequestFactory.setHttpClient(httpClient);
	        
	        
	        
	        
	        
	        customRequestFactory.setConnectionRequestTimeout(Integer.parseInt(objConfigData.getConnectionRequestTimeout())*1000);
	        customRequestFactory.setConnectTimeout(Integer.parseInt(objConfigData.getConnectTimeout())*1000);
	        customRequestFactory.setReadTimeout(Integer.parseInt(objConfigData.getReadTimeout())*1000);

	        
	        
	        
	        
	        
	        
	        
    		RestTemplate restTemplate = new RestTemplate(customRequestFactory);

	        
	        
	        
	        
	        
	        
	        
	        /*
		    
	        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
	        
    		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                    		.loadTrustMaterial(null, acceptingTrustStrategy)
                    		.build();
 
    		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
 
    		CloseableHttpClient httpClient = HttpClients.custom()
                    		.setSSLSocketFactory(csf)
                    		.build();
 
    		HttpComponentsClientHttpRequestFactory requestFactory =
                    		new HttpComponentsClientHttpRequestFactory();
 
    		requestFactory.setHttpClient(httpClient);
    		RestTemplate restTemplate = new RestTemplate(requestFactory);
			*/
			
		   // allowAllSSL();

		//	RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		//	RestTemplate restTemplate = new RestTemplate();

    		
    		
    		
    		
    		
    		
    		if(sendVersionToCMMS==0)
    		{
    			//start send version to server
				String requestbodyUpdateAgentVersion = "{\"TerminalID\":" + "\"" + objConfigData.getTerminalId() + "\"" +","+"\"VersionNo\":" + "\"" + objConfigData.getAppVersion() + "\""+ "}";// "{\"TerminalID\":\"HDFC_04\"}";
				
				objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "requestbody " + requestbodyUpdateAgentVersion);
				objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "URL " + objConfigData.getCmmsServerUpdateAgentVersion());


		
				HttpHeaders headerstoRequestUpdateAgentVersion = new HttpHeaders();
				headerstoRequestUpdateAgentVersion.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> entityToRequestUpdateAgentVersion = new HttpEntity<String>(requestbodyUpdateAgentVersion, headerstoRequestUpdateAgentVersion);
				
				
	    		RestTemplate restTemplateUpdateAgentVersion = new RestTemplate(customRequestFactory);

				ResponseEntity<String> responseEntityUpdateAgentVersion = restTemplateUpdateAgentVersion.exchange(
						objConfigData.getCmmsServerUpdateAgentVersion(), HttpMethod.POST, entityToRequestUpdateAgentVersion,
						String.class);
				
				
				if(responseEntityUpdateAgentVersion.getStatusCode()==HttpStatus.OK)
				{
					sendVersionToCMMS++;
				}
				
				objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeSendResult " + responseEntityUpdateAgentVersion.getStatusCode());
				objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseHeaders " + responseEntityUpdateAgentVersion.getHeaders());
				objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseBody " + responseEntityUpdateAgentVersion.getBody());

			//END send version to server	
				

    		}

			LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<>();
			param.add("TerminalId", objConfigData.getTerminalId());

			HttpHeaders reqheaders = new HttpHeaders();
			reqheaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			//objLOGGER = LoggerFactory.getLogger(ServerCallController.class);

			Map<String, String> reqparams = new HashMap<String, String>();
			reqparams.put("TerminalId", new String(objConfigData.getTerminalId()));
			;

			String requestbody = "{\"TerminalID\":" + "\"" + objConfigData.getTerminalId() + "\"" + "}";// "{\"TerminalID\":\"HDFC_04\"}";
			//objLOGGER.warn("requestbody " + requestbody);
			//objLOGGER.warn("URL " + objConfigData.getCmmsServerUrl());
			
			objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "requestbody " + requestbody);
			objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "URL " + objConfigData.getCmmsServerUrl());


		//	System.out.println(requestbody);
			// set headers
			HttpHeaders headerstoRequest = new HttpHeaders();
			headerstoRequest.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entityToRequest = new HttpEntity<String>(requestbody, headerstoRequest);

			// send request and parse result
			ResponseEntity<RequestFromServer[]> serverResponse = restTemplate.exchange(objConfigData.getCmmsServerUrl(),
					HttpMethod.POST, entityToRequest, RequestFromServer[].class);

			HttpStatus statusCode = serverResponse.getStatusCode();
			//objLOGGER.warn("statusCode " + statusCode);
			objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCode " + statusCode);


			if (statusCode == HttpStatus.OK) {
				RequestFromServer[] fromServers = serverResponse.getBody();
				List<RequestFromServer> objRequestFromServers = new ArrayList<>(Arrays.asList(fromServers));
				//objLOGGER.warn("ResponseBody " + objRequestFromServers);
				objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseBody " + objRequestFromServers);


			//	System.out.println(objRequestFromServers);
		//		System.out.println(objRequestFromServers.size());
				
				String pushtxtFiledata = objFileService.getFileData(objConfigData.getPushResult());
				objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "pushtxtFiledata " + pushtxtFiledata);

				if(pushtxtFiledata.length()>0)
				{
					
					objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "PUSH result present ");

					
					RequestFromServer lastPushData=objFileService.getPushDataFromJsonFile(objConfigData.getPushDataFile());
					int getPushData =0;
					while(objRequestFromServers.size()>getPushData)
					{
						RequestFromServer objData = objRequestFromServers.get(getPushData);
						
						if((objData.getActivityID()==8 || objData.getActivityID()==13 )&& (objData.getJobID()==lastPushData.getJobID() && objData.getDetailId()==lastPushData.getDetailId()) )
						{
							
							
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "SAME PUSH DATA");
	
							
							String absulutezipFileName=objConfigData.getZipToCMMS()+"pushResult"+".zip" ;
							objFileService.compressAllFiles(objConfigData.getPushResult().substring(0, objConfigData.getPushResult().lastIndexOf(File.separator)),absulutezipFileName,objConfigData.getZipPassword());
							
							RestTemplate restTemplateSendResult = new RestTemplate(customRequestFactory);

							

							

							HttpEntity<MultiValueMap<String, Object>> requestSendResult = objFilePushService
									.pushParmWithHeader(objData, absulutezipFileName);


					//		objLOGGER.warn("" + objConfigData.getCmmsServerPushUrl());
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", objConfigData.toString());


							ResponseEntity<String> responseEntitySendResult = restTemplateSendResult.exchange(
									objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestSendResult,
									String.class);

							HttpStatus statusCodeSendResult = responseEntitySendResult.getStatusCode();
						//	objLOGGER.warn("statusCodeSendResult " + statusCodeSendResult);
							//objLOGGER.warn("ResponseHeaders " + responseEntitySendResult.getHeaders());

							//objLOGGER.warn("ResponseBody " + responseEntitySendResult.getBody());
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeSendResult " + statusCodeSendResult);
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseHeaders " + responseEntitySendResult.getHeaders());
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", responseEntitySendResult.getBody());



							if(statusCodeSendResult == HttpStatus.OK)
							{
								objFileService.deleteFilesIndirectory(objConfigData.getPushResult().substring(0, objConfigData.getPushResult().lastIndexOf(File.separator)));
								
								objFileService.deleteFilesIndirectory(objConfigData.getPushDataFile().substring(0, objConfigData.getPushDataFile().lastIndexOf(File.separator)));
							
								if(objData.getActivityID()==13)
								{
									
									
									//objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "objData.getActivityID() " + objData.getActivityID());

									
									
									String requestbodyUpdateAgentVersion = "{\"TerminalID\":" + "\"" + objConfigData.getTerminalId() + "\"" +"\"VersionNo\":" + "\"" + objConfigData.getCmmsServerUpdateAgentVersion() + "\""+ "}";// "{\"TerminalID\":\"HDFC_04\"}";
								
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "requestbody " + requestbodyUpdateAgentVersion);
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "URL " + objConfigData.getCmmsServerUpdateAgentVersion());


							
									HttpHeaders headerstoRequestUpdateAgentVersion = new HttpHeaders();
									headerstoRequestUpdateAgentVersion.setContentType(MediaType.APPLICATION_JSON);
									HttpEntity<String> entityToRequestUpdateAgentVersion = new HttpEntity<String>(requestbodyUpdateAgentVersion, headerstoRequestUpdateAgentVersion);
									
									
						    		RestTemplate restTemplateUpdateAgentVersion = new RestTemplate(customRequestFactory);

									ResponseEntity<String> responseEntityUpdateAgentVersion = restTemplateUpdateAgentVersion.exchange(
											objConfigData.getCmmsServerUpdateAgentVersion(), HttpMethod.POST, entityToRequestUpdateAgentVersion,
											String.class);
									
									
									
									
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeSendResult " + responseEntityUpdateAgentVersion.getStatusCode());
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseHeaders " + responseEntityUpdateAgentVersion.getHeaders());
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseBody " + responseEntityUpdateAgentVersion.getBody());

									

								}
							
							
							}
							
							objRequestFromServers.remove(getPushData);

							
							
							
							
							
							break;
						}
						
						
						getPushData++;
					}
				}
				
				
				
				
				

				if (objRequestFromServers.size() > 0) {

					objFileService.deleteFilesIndirectory(objConfigData.getZipToCMMS());
					objFileService.deleteFilesIndirectory(objConfigData.getScreenshot());
					int requestdata = 0;
					// for (int requestdata = 0; requestdata < objRequestFromServers.size();
					// requestdata++) {

					
				//	objLOGGER.warn("firstCheckOnrestaart "+firstCheckOnrestaart);
					objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "firstCheckOnrestaart "+firstCheckOnrestart);

					if(firstCheckOnrestart==0)
					{
						
						
						
												
						
						
						
						
						
						
						
						
						
						
						
						
						
						while (objRequestFromServers.size() > 0 && firstCheckOnrestart==0) {
							firstCheckOnrestart++;

							requestdata = 0;
							//objLOGGER.warn(
								//	"*******************************************************************************************************");

							//objLOGGER.warn(" objRequestFromServers.size()  "+objRequestFromServers.size());
							
							
							//objLOGGER.warn("firstCheckOnrestaart "+firstCheckOnrestaart);
							
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "*******************************************************************************************************");
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " objRequestFromServers.size()  "+objRequestFromServers.size());
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "firstCheckOnrestaart "+firstCheckOnrestart);

							switch (objRequestFromServers.get(requestdata).getActivityID()) {
							
							
							case 13:	
								
								try {
								//	objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + objRequestFromServers.get(requestdata));


									RestTemplate pullRestTemplate8 = new RestTemplate(customRequestFactory);

									String strDoublequa = "\"";
									String strColan = ":";
									String strComma = ",";

									
									 String requestbody5 = "{" + strDoublequa + "TerminalID" + strDoublequa +
									  strColan + strDoublequa + objConfigData.getTerminalId() + strDoublequa +
									  strComma + strDoublequa + "JobID" + strDoublequa + strColan + strDoublequa +
									  objRequestFromServers.get(requestdata).getJobID() + strDoublequa + strComma +
									  strDoublequa + "DetailId" + strDoublequa + strColan + strDoublequa +
									  objRequestFromServers.get(requestdata).getDetailId() + strDoublequa + "}";
									 

//									String requestbody5 = "{" + strDoublequa + "TerminalID" + strDoublequa + strColan
//											+ strDoublequa + objConfigData.getTerminalId() + strDoublequa + strComma
//											+ strDoublequa + "JobID" + strDoublequa + strColan + strDoublequa + "2"
//											+ strDoublequa + strComma + strDoublequa + "DetailId" + strDoublequa + strColan
//											+ strDoublequa + "3" + strDoublequa + "}";

							//		System.out.println("requestbody5 " + requestbody5);
									HttpHeaders headerstoRequest8 = new HttpHeaders();
									headerstoRequest8.setContentType(MediaType.APPLICATION_JSON);

									HttpEntity<String> entityToRequest8 = new HttpEntity<String>(requestbody5,
											headerstoRequest8);

									// send request and parse result

							//		System.out.println(objConfigData.getCmmsServerPullUrl());
									//objLOGGER.warn("URL " + objConfigData.getCmmsServerPullUrl());
									
									
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "URL " + objConfigData.getCmmsServerPullUrl());


									ResponseEntity<byte[]> serverResponse8 = pullRestTemplate8.exchange(
											objConfigData.getCmmsServerPullUrl(), HttpMethod.POST, entityToRequest8,
											byte[].class);
							//		System.out.println(" #### "+serverResponse8.getStatusCodeValue());
									//objLOGGER.warn("byte  " + serverResponse8.getStatusCode());
									//objLOGGER.warn("getHeaders" + serverResponse8.getHeaders());
									//objLOGGER.warn(" byte  " + serverResponse8.getBody().length);
									
									
									
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", objConfigData.toString());
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", objConfigData.toString());
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", objConfigData.toString());


									if (serverResponse8.getStatusCode() == HttpStatus.OK) {

										String zipfileFromServer8 = objConfigData.getZipFromCMMS();
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Create ZIP file from Data");

										boolean resultFileFromServer=			objFileService.createZipFile(zipfileFromServer8, serverResponse8.getBody());
										
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Create Copy Of ZIP file ");

									// boolean bolCopyOfZipFile =	objFileService.createCopyOfZipFile(zipfileFromServer8,objConfigData.getCopyZipFromCMMS());
									 
									 
									 Path pathzipfileFromServer8 = Paths.get(zipfileFromServer8);
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Unzip zip file");
										//System.out.println("befor unzip");
										boolean bolUnzipFile = objFileService.Unzipfile(zipfileFromServer8, pathzipfileFromServer8.getParent().toString());
									 File filezipfileFromServer8= new File(zipfileFromServer8); 
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Delete Zip file");
	                                   if( bolUnzipFile)
									 filezipfileFromServer8.delete();
								objFileService.savePushDataToJsonFile(objRequestFromServers.get(requestdata), objConfigData.getPushDataFile());
								//objLOGGER.warn("resultFileFromServer  " + resultFileFromServer);
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "resultFileFromServer  " + resultFileFromServer);
								
								
								try
								{
									Thread.sleep(20000);
									System.out.println(pathzipfileFromServer8.getParent().toString());
								}
								catch(Exception e)
								{
									
									
								}
								
								

										File[] shFiles = new File(pathzipfileFromServer8.getParent().toString()).listFiles((dir, name) -> name.endsWith(".sh"));
	System.out.println(" "+shFiles[0].toString());
								//		System.out.println("shFiles  "+shFiles[0]);
									Boolean commandReturn=	objFileService.excuteCommand(shFiles[0].toString());
									System.out.println("before system exit");				
									System.exit(1);
									
									if (commandReturn)
									{
										
									}
									
									try
									{
										Thread.sleep(2000);
									}
									catch(Exception e)
									{
										
										
									}
									
										
										//objLOGGER.warn("fileDirectory  " + zipfileFromServer8);
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "fileDirectory  " + zipfileFromServer8);

										String absulutezipFileName=objConfigData.getZipToCMMS()+"pushResult"+".zip" ;
										objFileService.compressAllFiles(objConfigData.getPushResult().substring(0, objConfigData.getPushResult().lastIndexOf(File.separator)),absulutezipFileName,objConfigData.getZipPassword());
										
										RestTemplate restTemplateSendResult = new RestTemplate(customRequestFactory);

										

										

										HttpEntity<MultiValueMap<String, Object>> requestSendResult = objFilePushService
												.pushParmWithHeader(objRequestFromServers.get(requestdata), absulutezipFileName);


										//objLOGGER.warn("" + objConfigData.getCmmsServerPushUrl());
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "" + objConfigData.getCmmsServerPushUrl());

										ResponseEntity<String> responseEntitySendResult = restTemplateSendResult.exchange(
												objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestSendResult,
												String.class);

										HttpStatus statusCodeSendResult = responseEntitySendResult.getStatusCode();
										//objLOGGER.warn("statusCodeSendResult " + statusCodeSendResult);
										//objLOGGER.warn("ResponseHeaders " + responseEntitySendResult.getHeaders());

										//objLOGGER.warn("ResponseBody " + responseEntitySendResult.getBody());

										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeSendResult " + statusCodeSendResult);
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseHeaders " + responseEntitySendResult.getHeaders());
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseBody " + responseEntitySendResult.getBody());

										if(statusCodeSendResult == HttpStatus.OK)
										{
											objFileService.deleteFilesIndirectory(objConfigData.getPushResult().substring(0, objConfigData.getPushResult().lastIndexOf(File.separator)));
											
											objFileService.deleteFilesIndirectory(objConfigData.getPushDataFile().substring(0, objConfigData.getPushDataFile().lastIndexOf(File.separator)));
										}


									}

								} catch (Exception e) {

									e.printStackTrace();
									//objLOGGER.warn(" " + e.getMessage());
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + e.getMessage());

								}
								
								
								objRequestFromServers.remove(requestdata);

								break;
							
							
							
							
							
							case 8:

								try {
								//	objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + objRequestFromServers.get(requestdata));


									RestTemplate pullRestTemplate8 = new RestTemplate(customRequestFactory);

									String strDoublequa = "\"";
									String strColan = ":";
									String strComma = ",";

									
									 String requestbody5 = "{" + strDoublequa + "TerminalID" + strDoublequa +
									  strColan + strDoublequa + objConfigData.getTerminalId() + strDoublequa +
									  strComma + strDoublequa + "JobID" + strDoublequa + strColan + strDoublequa +
									  objRequestFromServers.get(requestdata).getJobID() + strDoublequa + strComma +
									  strDoublequa + "DetailId" + strDoublequa + strColan + strDoublequa +
									  objRequestFromServers.get(requestdata).getDetailId() + strDoublequa + "}";
									 

//									String requestbody5 = "{" + strDoublequa + "TerminalID" + strDoublequa + strColan
//											+ strDoublequa + objConfigData.getTerminalId() + strDoublequa + strComma
//											+ strDoublequa + "JobID" + strDoublequa + strColan + strDoublequa + "2"
//											+ strDoublequa + strComma + strDoublequa + "DetailId" + strDoublequa + strColan
//											+ strDoublequa + "3" + strDoublequa + "}";

							//		System.out.println("requestbody5 " + requestbody5);
									HttpHeaders headerstoRequest8 = new HttpHeaders();
									headerstoRequest8.setContentType(MediaType.APPLICATION_JSON);

									HttpEntity<String> entityToRequest8 = new HttpEntity<String>(requestbody5,
											headerstoRequest8);

									// send request and parse result

							//		System.out.println(objConfigData.getCmmsServerPullUrl());
									//objLOGGER.warn("URL " + objConfigData.getCmmsServerPullUrl());
									
									
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "URL " + objConfigData.getCmmsServerPullUrl());


									ResponseEntity<byte[]> serverResponse8 = pullRestTemplate8.exchange(
											objConfigData.getCmmsServerPullUrl(), HttpMethod.POST, entityToRequest8,
											byte[].class);
							//		System.out.println(" #### "+serverResponse8.getStatusCodeValue());
									//objLOGGER.warn("byte  " + serverResponse8.getStatusCode());
									//objLOGGER.warn("getHeaders" + serverResponse8.getHeaders());
									//objLOGGER.warn(" byte  " + serverResponse8.getBody().length);
									
									
									
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", objConfigData.toString());
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", objConfigData.toString());
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", objConfigData.toString());


									if (serverResponse8.getStatusCode() == HttpStatus.OK) {

										String zipfileFromServer8 = objConfigData.getZipFromCMMS();
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Create ZIP file from Data");

										boolean resultFileFromServer=			objFileService.createZipFile(zipfileFromServer8, serverResponse8.getBody());
										
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Create Copy Of ZIP file ");

									 boolean bolCopyOfZipFile =	objFileService.createCopyOfZipFile(zipfileFromServer8,objConfigData.getCopyZipFromCMMS());
									 
									 
									 Path pathzipfileFromServer8 = Paths.get(zipfileFromServer8);
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Unzip zip file");

										boolean bolUnzipFile = objFileService.Unzipfile(zipfileFromServer8, pathzipfileFromServer8.getParent().toString());
									 File filezipfileFromServer8= new File(zipfileFromServer8); 
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Delete Zip file");
                                       if(bolCopyOfZipFile && bolUnzipFile)
									 filezipfileFromServer8.delete();
								objFileService.savePushDataToJsonFile(objRequestFromServers.get(requestdata), objConfigData.getPushDataFile());
								//objLOGGER.warn("resultFileFromServer  " + resultFileFromServer);
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "resultFileFromServer  " + resultFileFromServer);


										//File[] shFiles = new File(zipfileFromServer8).listFiles((dir, name) -> name.endsWith(".sh"));
								File[] shFiles = new File(filezipfileFromServer8.getParent()).listFiles((dir, name) -> name.endsWith(".sh"));

								//		System.out.println("shFiles  "+shFiles[0]);
									Boolean commandReturn=	objFileService.excuteCommand(shFiles[0].toString());
									
									if (commandReturn)
									{
										
									}
									
									try
									{
										Thread.sleep(2000);
									}
									catch(Exception e)
									{
										
										
									}
									
										
										//objLOGGER.warn("fileDirectory  " + zipfileFromServer8);
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "fileDirectory  " + zipfileFromServer8);

										String absulutezipFileName=objConfigData.getZipToCMMS()+"pushResult"+".zip" ;
										objFileService.compressAllFiles(objConfigData.getPushResult().substring(0, objConfigData.getPushResult().lastIndexOf(File.separator)),absulutezipFileName,objConfigData.getZipPassword());
										
										RestTemplate restTemplateSendResult = new RestTemplate(customRequestFactory);

										

										

										HttpEntity<MultiValueMap<String, Object>> requestSendResult = objFilePushService
												.pushParmWithHeader(objRequestFromServers.get(requestdata), absulutezipFileName);


										//objLOGGER.warn("" + objConfigData.getCmmsServerPushUrl());
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "" + objConfigData.getCmmsServerPushUrl());

										ResponseEntity<String> responseEntitySendResult = restTemplateSendResult.exchange(
												objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestSendResult,
												String.class);

										HttpStatus statusCodeSendResult = responseEntitySendResult.getStatusCode();
										//objLOGGER.warn("statusCodeSendResult " + statusCodeSendResult);
										//objLOGGER.warn("ResponseHeaders " + responseEntitySendResult.getHeaders());

										//objLOGGER.warn("ResponseBody " + responseEntitySendResult.getBody());

										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeSendResult " + statusCodeSendResult);
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseHeaders " + responseEntitySendResult.getHeaders());
										objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseBody " + responseEntitySendResult.getBody());

										if(statusCodeSendResult == HttpStatus.OK)
										{
											objFileService.deleteFilesIndirectory(objConfigData.getPushResult().substring(0, objConfigData.getPushResult().lastIndexOf(File.separator)));
											
											objFileService.deleteFilesIndirectory(objConfigData.getPushDataFile().substring(0, objConfigData.getPushDataFile().lastIndexOf(File.separator)));
										}


									}

								} catch (Exception e) {

									//objLOGGER.warn(" " + e.getMessage());
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + e.getMessage());

								}

								objRequestFromServers.remove(requestdata);

								break;

							
							}
							requestdata++;
						}
					}
					
					
					
					
					while (objRequestFromServers.size() > 0) {
						requestdata = 0;
						//objLOGGER.warn(
							//	"*******************************************************************************************************");

						//objLOGGER.warn(" objRequestFromServers.size()  "+objRequestFromServers.size());
						
						//objLOGGER.warn("");

						objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "*******************************************************************************************************");
						objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " objRequestFromServers.size()  "+objRequestFromServers.size());
						objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "");

						
						
						switch (objRequestFromServers.get(requestdata).getActivityID()) {

						case 1:

							try {
								RestTemplate restTemplateScreenshot = new RestTemplate(customRequestFactory);

								//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + objRequestFromServers.get(requestdata));

								String screenshotFileName = objConfigData.getScreenshot()
										+ objConfigData.getScreenshotFileName() + "."
										+ objConfigData.getScreenshotFileFormat();

								objFileService.fullScreenShot(screenshotFileName,
										objConfigData.getScreenshotFileFormat());

								
								
								String nameTozipFile = objRequestFromServers.get(requestdata).getActivity().replaceAll("\\s", "")+".zip";
								String screenshotZipFileName=objConfigData.getZipToCMMS()+nameTozipFile ;
								
								objFileService.compressAllFiles(objConfigData.getScreenshot(),screenshotZipFileName,objConfigData.getZipPassword());
								HttpEntity<MultiValueMap<String, Object>> requestScreenshot = objFilePushService
										.pushParmWithHeader(objRequestFromServers.get(requestdata), screenshotZipFileName);

								//objLOGGER.warn("URL " + objConfigData.getCmmsServerPushUrl());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "URL " + objConfigData.getCmmsServerPushUrl());

								ResponseEntity<String> responseEntityScreenshot = restTemplateScreenshot.exchange(
										objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestScreenshot, String.class);
								HttpStatus statusCodeScreenShot = responseEntityScreenshot.getStatusCode();
								
								
					//			System.out.println(" #### "+ statusCodeScreenShot);

								//objLOGGER.warn("statusCodeScreenShot " + statusCodeScreenShot);
								//objLOGGER.warn("HeadersScreenShot " + responseEntityScreenshot.getHeaders());

								//objLOGGER.warn(" ResponseBody  ScreenShot " + responseEntityScreenshot.getBody());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeScreenShot " + statusCodeScreenShot);
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "HeadersScreenShot " + responseEntityScreenshot.getHeaders());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " ResponseBody  ScreenShot " + responseEntityScreenshot.getBody());

								

							} catch (Exception e) {

								//objLOGGER.warn(" " + e.getMessage());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + e.getMessage());

							
							}


							objRequestFromServers.remove(requestdata);
							break;
						case 2:
							//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
							objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + objRequestFromServers.get(requestdata));

							if (objRequestFromServers.size() > 1) {
								objRequestFromServers.add(objRequestFromServers.remove(requestdata));
							} else {
								//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t"," " + objRequestFromServers.get(requestdata));
								
								objFileService.createfile(objConfigData.getReStartResult(),"1");
								
								
								
								
								String absulutezipFileName=objConfigData.getZipToCMMS()+"reStartResult"+".zip" ;
								objFileService.compressAllFiles(objConfigData.getReStartResult().substring(0, objConfigData.getReStartResult().lastIndexOf(File.separator)),absulutezipFileName,objConfigData.getZipPassword());
								
								RestTemplate restTemplateSendResult = new RestTemplate(customRequestFactory);

								

								

								HttpEntity<MultiValueMap<String, Object>> requestSendResult = objFilePushService
										.pushParmWithHeader(objRequestFromServers.get(requestdata), absulutezipFileName);


								//objLOGGER.warn("" + objConfigData.getCmmsServerPushUrl());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "" + objConfigData.getCmmsServerPushUrl());

								ResponseEntity<String> responseEntitySendResult = restTemplateSendResult.exchange(
										objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestSendResult,
										String.class);

								HttpStatus statusCodeSendResult = responseEntitySendResult.getStatusCode();
								//objLOGGER.warn("statusCodeSendResult " + statusCodeSendResult);
								//objLOGGER.warn("ResponseHeaders " + responseEntitySendResult.getHeaders());

								//objLOGGER.warn("ResponseBody " + responseEntitySendResult.getBody());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeSendResult " + statusCodeSendResult);
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseHeaders " + responseEntitySendResult.getHeaders());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseBody " + responseEntitySendResult.getBody());

								
								objRequestFromServers.remove(requestdata);

								
								if(statusCodeSendResult == HttpStatus.OK)
								{
									objFileService.deleteFilesIndirectory(objConfigData.getReStartResult().substring(0, objConfigData.getReStartResult().lastIndexOf(File.separator)));
									
									//objFileService.deleteFilesIndirectory(objConfigData.getPushDataFile().substring(0, objConfigData.getPushDataFile().lastIndexOf(File.separator)));
								}
								
								
								
								
								Process p = Runtime.getRuntime().exec("shutdown -r now");
							}
							break;

//						case 3:
//
//							try {
//								//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
//
//								//objLOGGER.warn("");
//
//								RestTemplate restTemplateDeviceLog = new RestTemplate();
//
//								/*
//								 * String strDate = objRequestFromServers.get(requestdata).getFileDate();
//								 * SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//								 * Date dtedate = inFormat.parse(strDate); int strYear=dtedate.getYear(); int
//								 * strMonth=dtedate.getMonth(); int strDay = dtedate.getDay();
//								 * //objLOGGER.warn(""+strYear+" "+strMonth+" "+strDay);
//								 */
//								
//								
//								String nameTozipFile = objRequestFromServers.get(requestdata).getActivity().replaceAll("\\s", "")+".zip";
//								String DevicezipFileName=objConfigData.getZipToCMMS()+nameTozipFile ;
//								String DeviceLogDate=objRequestFromServers.get(requestdata).getFileDate().substring(0, 10);
//								
//								
//								String [] dateParts = DeviceLogDate.split("-");
//								String day = dateParts[2];
//								String month = dateParts[1];
//								String year = dateParts[0];
//
//								
//								
//								
//								String fileDeviceLog = objConfigData.getFileToPushDeviceLog() +"/"+year+"/"+month+"/"+day+"/";
//										
//								//objLOGGER.warn("fileDeviceLog  " + fileDeviceLog);
//
//								
//								objFileService.compressAllFiles(fileDeviceLog,DevicezipFileName);
//								
//								
//								
//
//								
//
//								HttpEntity<MultiValueMap<String, Object>> requestDeviceLog = objFilePushService
//										.pushParmWithHeader(objRequestFromServers.get(requestdata), DevicezipFileName);
//
//
//								//objLOGGER.warn("" + objConfigData.getCmmsServerPushUrl());
//
//								ResponseEntity<String> responseEntityDeviceLog = restTemplateDeviceLog.exchange(
//										objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestDeviceLog,
//										String.class);
//
//								HttpStatus statusCodeDeviceLog = responseEntityDeviceLog.getStatusCode();
//								//objLOGGER.warn("statusCodeDeviceLog " + statusCodeDeviceLog);
//								//objLOGGER.warn("ResponseHeaders " + responseEntityDeviceLog.getHeaders());
//
//								//objLOGGER.warn("ResponseBody " + responseEntityDeviceLog.getBody());
//
//								
//
//							} catch (Exception e) {
//								//objLOGGER.warn(" " + e.getMessage());
//							}
//
//							objRequestFromServers.remove(requestdata);
//
//							break;
						case 44:

							try {

								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + objRequestFromServers.get(requestdata));

								//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
								RestTemplate restTemplateConfigFile = new RestTemplate(customRequestFactory);

								// //objLOGGER.warn(" " + objRequestFromServers.get(requestdata));

								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "");
								//objLOGGER.warn("");

								String filenameconfig = objConfigData.getBaseDirectory() +"/" +objRequestFromServers.get(requestdata).getFilePath().replace("\\","/");

								HttpEntity<MultiValueMap<String, Object>> requestconfig = objFilePushService
										.pushParmWithHeader(objRequestFromServers.get(requestdata), filenameconfig);
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "URL " + objConfigData.getCmmsServerPushUrl());

								//objLOGGER.warn("URL " + objConfigData.getCmmsServerPushUrl());

								ResponseEntity<String> responseEntityconfig = restTemplateConfigFile.exchange(
										objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestconfig,
										String.class);

								HttpStatus statusCodeconfig = responseEntityconfig.getStatusCode();
								//objLOGGER.warn("statusCodeconfig " + statusCodeconfig);
								//objLOGGER.warn("ResponseHeaders " + responseEntityconfig.getHeaders());
								//objLOGGER.warn("ResponseBody " + responseEntityconfig.getBody());

								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeconfig " + statusCodeconfig);
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseHeaders " + responseEntityconfig.getHeaders());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseBody " + responseEntityconfig.getBody());

								
							} catch (Exception e) {

								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + e.getMessage());

								//objLOGGER.warn(" " + e.getMessage());
							}
							objRequestFromServers.remove(requestdata);

							break;
//						case 5:
//
//							try {
//								//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
//
//								//objLOGGER.warn("");
//								RestTemplate restTemplateReports = new RestTemplate();
//
//								String filenameReports = objConfigData.getFileToPushReports()
//										+ objRequestFromServers.get(requestdata).getFileDate().substring(0, 10)
//										+ ".csv";
//
//								HttpEntity<MultiValueMap<String, Object>> requestReports = objFilePushService
//										.pushParmWithHeader(objRequestFromServers.get(requestdata), filenameReports);
//
//								System.out.println("@@@@@@" + filenameReports);
//								//objLOGGER.warn("URL " + objConfigData.getCmmsServerPushUrl());
//
//								ResponseEntity<String> responseEntityReports = restTemplateReports.exchange(
//										objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestReports,
//										String.class);
//
//								HttpStatus statusCodeReports = responseEntityReports.getStatusCode();
//								//objLOGGER.warn("statusCodeReports " + statusCodeReports);
//								//objLOGGER.warn("ResponseHeaders " + responseEntityReports.getHeaders());
//								//objLOGGER.warn("ResponseBody " + responseEntityReports.getBody());
//
//								
//
//							} catch (Exception e) {
//								//objLOGGER.warn(" " + e.getMessage());
//							}
//
//							objRequestFromServers.remove(requestdata);
//
//							break;
//
//						case 6:
//							try {
//								//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
//
//								//objLOGGER.warn("");
//								RestTemplate restTemplateApplicationErrorLog = new RestTemplate();
//
//
//								String filenameApplicationErrorLog = objConfigData.getFileToPushApplicationErrorLog()
//										+ objRequestFromServers.get(requestdata).getFileDate().substring(0, 10)
//										+ ".txt";
//
//								HttpEntity<MultiValueMap<String, Object>> requestApplicationErrorLog = objFilePushService
//										.pushParmWithHeader(objRequestFromServers.get(requestdata),
//												filenameApplicationErrorLog);
//
//								//objLOGGER.warn("URL " + objConfigData.getCmmsServerPushUrl());
//
//								ResponseEntity<String> responseEntityApplicationErrorLog = restTemplateApplicationErrorLog.exchange(
//										objConfigData.getCmmsServerPushUrl(), HttpMethod.POST,
//										requestApplicationErrorLog, String.class);
//
//								HttpStatus statusCodeApplicationErrorLog = responseEntityApplicationErrorLog
//										.getStatusCode();
//								//objLOGGER.warn("statusCodeApplicationErrorLog " + statusCodeApplicationErrorLog);
//								//objLOGGER.warn("ResponseHeaders " + responseEntityApplicationErrorLog.getHeaders());
//
//								//objLOGGER.warn("ResponseBody " + responseEntityApplicationErrorLog.getBody());
//								
//
//							} catch (Exception e) {
//								//objLOGGER.warn(" " + e.getMessage());
//							}
//							objRequestFromServers.remove(requestdata);
//
//							break;
//						case 7:
//
//							try {
//								//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
//
//								//objLOGGER.warn("");
//
//								String strDate = objRequestFromServers.get(requestdata).getFileDate();
//								SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//								Date dtedate = inFormat.parse(strDate);
//								Calendar date = Calendar.getInstance();
//								date.setTime(dtedate);
//								int strYear = date.get(Calendar.YEAR); // 2012
//								int strMonth = date.get(Calendar.MONTH); // 9 - October!!!
//								int strDay = date.get(Calendar.DAY_OF_MONTH); // 5
//								//objLOGGER.warn(dtedate + "dtedate " + strYear + " " + strMonth + " " + strDay);
//
//								
//
//								
//								
//								
//								
//								String nameTozipFile = objRequestFromServers.get(requestdata).getActivity().replaceAll("\\s", "")+".zip";
//								String ApplicationFileName=objConfigData.getZipToCMMS()+nameTozipFile ;
//								String ApplicationLogDate=objRequestFromServers.get(requestdata).getFileDate().substring(0, 10);
//								
//								
//								String [] dateParts = ApplicationLogDate.split("-");
//								String day = dateParts[2];
//								String month = dateParts[1];
//								String year = dateParts[0];
//
//								
//								
//								
//								String fileApplicationLog = objConfigData.getFileToPushApplicationLog() +"/"+year+"/"+month+"/"+day+"/";
//										
//								//objLOGGER.warn("fileApplicationLog  " + fileApplicationLog);
//
//								
//								objFileService.compressAllFiles(fileApplicationLog,ApplicationFileName);
//
//								
//								
//								
//								
//								
//								
//								
//								
//								HttpEntity<MultiValueMap<String, Object>> requestAppLog = objFilePushService
//										.pushParmWithHeader(objRequestFromServers.get(requestdata), ApplicationFileName);
//
//							//	System.out.println("@@@@@@" + filename);
////					ResponseEntity<String> responsesend = restTemplate.postForEntity(objConfigData.getCmmsServerPushUrl(), request,
////			    	        String.class);
//								//objLOGGER.warn("URL " + objConfigData.getCmmsServerPushUrl());
//
//								ResponseEntity<String> responseEntityAppLog = restTemplate.exchange(
//										objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestAppLog,
//										String.class);
//
//								HttpStatus statusCodeAppLog = responseEntityAppLog.getStatusCode();
//								//objLOGGER.warn("statusCodeAppLog " + statusCodeAppLog);
//								//objLOGGER.warn("APPLOGResponseHeaders " + responseEntityAppLog.getHeaders());
//
//								//objLOGGER.warn("AppLOGResponseBody " + responseEntityAppLog.getBody());
//
//								
//
//							} catch (Exception e) {
//								//objLOGGER.warn(" " + e.getMessage());
//							}
//
//							objRequestFromServers.remove(requestdata);
//
//							break;
//
//						case 88:
//							//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
//
//							HttpHeaders pullRequestHeader = new HttpHeaders();
//							// pullRequestHeader.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//							RestTemplate pullRestTemplate = new RestTemplate();
//
//							// request body parameters
//							Map<String, Object> mapPullRequest = new HashMap<>();
//							mapPullRequest.put("TerminalID", objConfigData.getTerminalId());
//							mapPullRequest.put("JobId", objRequestFromServers.get(requestdata).getJobID());
//							mapPullRequest.put("DetailId", objRequestFromServers.get(requestdata).getDetailId());
//
//							// build the request
//							HttpEntity<Map<String, Object>> entity = new HttpEntity<>(mapPullRequest,
//									pullRequestHeader);
//
//							ResponseEntity<byte[]> response = pullRestTemplate.exchange(
//									objConfigData.getCmmsServerPullUrl(), HttpMethod.POST, entity, byte[].class);
//
//							System.out.println(response.getStatusCodeValue());
//
//							String zipfileFromServer = objConfigData.getZipFromCMMS() + "pullfile.zip";
//							objFileService.createZipFile(zipfileFromServer, response.getBody());
//
//							break;
//
//						case 80:
//							//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
//
//							//objLOGGER.warn("");
//
//							HttpHeaders pullRequestHeader1 = new HttpHeaders();
//							pullRequestHeader1.setContentType(MediaType.MULTIPART_FORM_DATA);
//							RestTemplate pullRestTemplate1 = new RestTemplate();
//
//							// request body parameters
//							MultiValueMap<String, Object> param1 = new LinkedMultiValueMap<>();
//							// param.add("formFiles", resource);
//							param1.add("JobId", new Integer(objRequestFromServers.get(requestdata).getJobID()));
//							param1.add("DetailId", new Integer(objRequestFromServers.get(requestdata).getDetailId()));
//
//							HttpEntity<MultiValueMap<String, Object>> request1 = new HttpEntity<MultiValueMap<String, Object>>(
//									param1, pullRequestHeader1);
//
//							ResponseEntity<byte[]> response1 = pullRestTemplate1.exchange(
//									objConfigData.getCmmsServerPullUrl(), HttpMethod.POST, request1, byte[].class);
//
//							System.out.println(response1.getStatusCodeValue());
//
//							String zipfileFromServer1 = objConfigData.getZipFromCMMS() + "pullfile.zip";
//							objFileService.createZipFile(zipfileFromServer1, response1.getBody());
//
//							break;
						/*
						 * case 8:
						 * 
						 * try { //objLOGGER.warn(" " + objRequestFromServers.get(requestdata));
						 * 
						 * RestTemplate pullRestTemplate8 = new RestTemplate();
						 * 
						 * String strDoublequa = "\""; String strColan = ":"; String strComma = ",";
						 * 
						 * 
						 * String requestbody5 = "{" + strDoublequa + "TerminalID" + strDoublequa +
						 * strColan + strDoublequa + objConfigData.getTerminalId() + strDoublequa +
						 * strComma + strDoublequa + "JobID" + strDoublequa + strColan + strDoublequa +
						 * objRequestFromServers.get(requestdata).getJobID() + strDoublequa + strComma +
						 * strDoublequa + "DetailId" + strDoublequa + strColan + strDoublequa +
						 * objRequestFromServers.get(requestdata).getDetailId() + strDoublequa + "}";
						 * 
						 * 
						 * // String requestbody5 = "{" + strDoublequa + "TerminalID" + strDoublequa +
						 * strColan // + strDoublequa + objConfigData.getTerminalId() + strDoublequa +
						 * strComma // + strDoublequa + "JobID" + strDoublequa + strColan + strDoublequa
						 * + "2" // + strDoublequa + strComma + strDoublequa + "DetailId" + strDoublequa
						 * + strColan // + strDoublequa + "3" + strDoublequa + "}";
						 * 
						 * System.out.println("requestbody5 " + requestbody5); HttpHeaders
						 * headerstoRequest8 = new HttpHeaders();
						 * headerstoRequest8.setContentType(MediaType.APPLICATION_JSON);
						 * 
						 * HttpEntity<String> entityToRequest8 = new HttpEntity<String>(requestbody5,
						 * headerstoRequest8);
						 * 
						 * // send request and parse result
						 * 
						 * System.out.println(objConfigData.getCmmsServerPullUrl());
						 * //objLOGGER.warn("URL " + objConfigData.getCmmsServerPullUrl());
						 * 
						 * ResponseEntity<byte[]> serverResponse8 = pullRestTemplate8.exchange(
						 * objConfigData.getCmmsServerPullUrl(), HttpMethod.POST, entityToRequest8,
						 * byte[].class); System.out.println(serverResponse8.getStatusCodeValue());
						 * //objLOGGER.warn("byte  " + serverResponse8.getStatusCode());
						 * //objLOGGER.warn("getHeaders" + serverResponse8.getHeaders());
						 * //objLOGGER.warn(" byte" + serverResponse8.getBody().length);
						 * 
						 * if (serverResponse8.getStatusCode() == HttpStatus.OK) {
						 * 
						 * String zipfileFromServer8 = objConfigData.getZipFromCMMS();
						 * objFileService.createZipFile(zipfileFromServer8, serverResponse8.getBody());
						 * 
						 * }
						 * 
						 * } catch (Exception e) { //objLOGGER.warn(" " + e.getMessage()); }
						 * 
						 * objRequestFromServers.remove(requestdata);
						 * 
						 * break;
						 */
							
							
													
							
							
						default:
							
							
							try {
								//objLOGGER.warn(" " + objRequestFromServers.get(requestdata));

								//objLOGGER.warn("");
								
								if(objRequestFromServers.get(requestdata).getActivityID()==8) {
									objRequestFromServers.remove(requestdata);
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Push Patch At Running " );

									break;
								}
								if(objRequestFromServers.get(requestdata).getActivityID()==13) {
									objRequestFromServers.remove(requestdata);
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "Agent Push Patch At Running " );

									break;
								}

								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + objRequestFromServers.get(requestdata));
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "");

								RestTemplate restTemplateDeviceLog = new RestTemplate(customRequestFactory);

								/*
								 * String strDate = objRequestFromServers.get(requestdata).getFileDate();
								 * SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
								 * Date dtedate = inFormat.parse(strDate); int strYear=dtedate.getYear(); int
								 * strMonth=dtedate.getMonth(); int strDay = dtedate.getDay();
								 * //objLOGGER.warn(""+strYear+" "+strMonth+" "+strDay);
								 */
								
								
								String nameTozipFile = objRequestFromServers.get(requestdata).getActivity().replaceAll("\\s", "")+".zip";
								String absulutezipFileName=objConfigData.getZipToCMMS()+nameTozipFile ;
								String logDate=objRequestFromServers.get(requestdata).getFileDate().substring(0, 10);
								
								
								String [] dateParts = logDate.split("-");
								String day = dateParts[2];
								String month = dateParts[1];
								String year = dateParts[0];
								String logfileDirectory = new String();
								
								objRequestFromServers.get(requestdata).setFilePath(objRequestFromServers.get(requestdata).getFilePath()== null ? "" : objRequestFromServers.get(requestdata).getFilePath());
								
								if(objRequestFromServers.get(requestdata).getActivityID()==4) {

									 logfileDirectory = objConfigData.getKIOSK_HOME()+objRequestFromServers.get(requestdata).getFilePath().replace("\\","/") ;

								}
								else if(objRequestFromServers.get(requestdata).getActivityID()==14) {

									 logfileDirectory = objConfigData.getKIOSK_HOME()+"/"+objConfigData.getLoghome()+"/"+year+"/"+month+"/"+day+"/";

								}else if(objRequestFromServers.get(requestdata).getActivityID()==15 || objRequestFromServers.get(requestdata).getActivityID()==16) {

									//String otherDataPath= Paths.get(objConfigData.getOtherData()).getParent().toString();
									//String otherDataFileName = objRequestFromServers.get(requestdata).getActivity()+".txt";
									//objFileService.deleteFilesIndirectory(otherDataPath);
									//objFileService.excuteCommand(objConfigData.getSystemEventLogCommand());
									//String strSystemData=objFileService.getSystemData();
									//objFileService.createfile(otherDataPath+"/"+otherDataFileName, strSystemData);
									//logfileDirectory = otherDataPath;
									
									logfileDirectory = objConfigData.getReStartResult().substring(0, objConfigData.getReStartResult().lastIndexOf(File.separator));

									objFileService.deleteFilesIndirectory(logfileDirectory);

									objFileService.createfile(objConfigData.getReStartResult(),  objRequestFromServers.get(requestdata).getActivity()+"\t"+" This feature is not availabe for Linux");

								}
								
								
								
								
								else if(objRequestFromServers.get(requestdata).getActivityID()==17) {
									
									String otherDataPath= Paths.get(objConfigData.getOtherData()).getParent().toString();
									String otherDataFileName = objRequestFromServers.get(requestdata).getActivity()+".txt";
									objFileService.deleteFilesIndirectory(otherDataPath);
									String strSystemData=objFileService.getSystemData();
									objFileService.createfile(otherDataPath+"/"+otherDataFileName, strSystemData);
									logfileDirectory = otherDataPath;

								}
							
								else
								{
									 logfileDirectory = objConfigData.getKIOSK_HOME()+objRequestFromServers.get(requestdata).getFilePath().replace("\\","/") +"/"+year+"/"+month+"/"+day+"/";
	
								}
								
								/*
								 * if(objRequestFromServers.get(requestdata).getActivityID()!=4) {
								 * 
								 * logfileDirectory =
								 * objConfigData.getKIOSK_HOME()+objRequestFromServers.get(requestdata).
								 * getFilePath().replace("\\","/") +"/"+year+"/"+month+"/"+day+"/"; } else {
								 * logfileDirectory =
								 * objConfigData.getKIOSK_HOME()+objRequestFromServers.get(requestdata).
								 * getFilePath().replace("\\","/") ;
								 * 
								 * }
								 */
										
								//objLOGGER.warn("logfileDirectory  " + logfileDirectory);
								
								
						        File dir = new File(logfileDirectory);
						        File[] files = dir.listFiles();
						    
						        
						        
    
						        if(files==null || files.length <1)
						        {
									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "logfileDirectory  length  " + files);

						        	objFileService.createfile(objConfigData.getReStartResult(),"File Not Found  :"+logfileDirectory);
						        	
						        	
						        //	objFileService.createfile(objConfigData.getReStartResult()," This feature is not availabe for Linux");
									logfileDirectory = objConfigData.getReStartResult().substring(0, objConfigData.getReStartResult().lastIndexOf(File.separator));

									objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " new logfileDirectory    " + logfileDirectory);

						        }


								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "logfileDirectory  " + logfileDirectory);

								System.out.println("  start compress ");
								objFileService.compressAllFiles(logfileDirectory,absulutezipFileName,objConfigData.getZipPassword());
								
								
								

								

								HttpEntity<MultiValueMap<String, Object>> requestDeviceLog = objFilePushService
										.pushParmWithHeader(objRequestFromServers.get(requestdata), absulutezipFileName);


								//objLOGGER.warn("" + objConfigData.getCmmsServerPushUrl());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "" + objConfigData.getCmmsServerPushUrl());

								ResponseEntity<String> responseEntityDeviceLog = restTemplateDeviceLog.exchange(
										objConfigData.getCmmsServerPushUrl(), HttpMethod.POST, requestDeviceLog,
										String.class);

								HttpStatus statusCodeDeviceLog = responseEntityDeviceLog.getStatusCode();
								//objLOGGER.warn("statusCodeLog " + statusCodeDeviceLog);
								//objLOGGER.warn("ResponseHeaders " + responseEntityDeviceLog.getHeaders());

								//objLOGGER.warn("ResponseBody " + responseEntityDeviceLog.getBody());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "statusCodeLog " + statusCodeDeviceLog);
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseHeaders " + responseEntityDeviceLog.getHeaders());
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "ResponseBody " + responseEntityDeviceLog.getBody());

								

							} catch (Exception e) {
								//objLOGGER.warn(" " + e.getMessage());
								System.out.println("Default  :::");	
							
								objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", " " + e.getMessage());

							
							}

							objRequestFromServers.remove(requestdata);

							break;

							
						}

					}
				}
				else
				{
					firstCheckOnrestart++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//objLOGGER.warn(" " + e.getMessage());
			objTS_Tracer.WriteToLogs(filefolder, objConfigData.getLogfilename(), org.springframework.util.ClassUtils.getUserClass(this.getClass()).getName(), "\t", "   " + "Outer Try "+e.getMessage());

		}
	}
	
	
    private static TrustManager[] trustManagers;

    public static class X509TrustManager implements javax.net.ssl.X509TrustManager {

        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }

    public static void allowAllSSL() throws KeyManagementException {

        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        javax.net.ssl.SSLContext context;

        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new X509TrustManager()};
        }

        try {
            context = javax.net.ssl.SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {

        } catch (KeyManagementException e) {

        }
    }


}
