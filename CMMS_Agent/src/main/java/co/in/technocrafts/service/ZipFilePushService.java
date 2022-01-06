package co.in.technocrafts.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import co.in.technocrafts.controller.ServerCallController;
import co.in.technocrafts.model.ConfigData;
import co.in.technocrafts.model.RequestFromServer;

@Service
public class ZipFilePushService {
	
	
	@Autowired
	ConfigData objConfigData;
	
	
    private static final Logger objLOGGER=LoggerFactory.getLogger(ZipFilePushService.class);
	
	public HttpEntity<MultiValueMap<String, Object>> pushParmWithHeader(RequestFromServer objRequestFromServer)
	{
        byte[] bytesData;

		File[] files = new File(objConfigData.getZipToCMMS()).listFiles((dir, name) -> name.endsWith(".zip"));

	//	System.out.println("pushParmWithHeader");
		try {
			bytesData = Files.readAllBytes(Paths.get(files[0].toString()));
		} catch (IOException e) {
			bytesData=new byte[0];
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    headers.add("zipFileLength", String.valueOf(bytesData.length));
		 FileSystemResource resource = new FileSystemResource(files[0]);
	      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
	      param.add("formFiles", resource);
          param.add("JobId", new Integer(objRequestFromServer.getJobID()));
          param.add("DetailId", new Integer(objRequestFromServer.getDetailId()));

	      
	      HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(
	    		  param, headers);
	      
	//      System.out.println("end of push method " + resource.toString());
	  //    System.out.println("JobId "+objRequestFromServer.getJobID());
	    //  System.out.println("DetailId"+objRequestFromServer.getDetailId());
	      return request;

		
	}
	
	public HttpEntity<MultiValueMap<String, Object>> pushParmWithHeader(RequestFromServer objRequestFromServer,String filetoSend)
	{
        byte[] bytesData;

		File files = new File(filetoSend);

//		System.out.println("pushParmWithHeader");
		try {
			bytesData = Files.readAllBytes(Paths.get(files.toString()));
		} catch (IOException e) {
			bytesData=new byte[0];
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	    headers.add("zipFileLength", String.valueOf(bytesData.length));
		 FileSystemResource resource = new FileSystemResource(files);
	      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
	      param.add("formFiles", resource);
          param.add("JobId", new Integer(objRequestFromServer.getJobID()));
          param.add("DetailId", new Integer(objRequestFromServer.getDetailId()));

	      
	      HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(
	    		  param, headers);
	      
	     // System.out.println("end of push method " + resource.toString());
	    //  System.out.println("JobId "+objRequestFromServer.getJobID());
	    //  System.out.println("DetailId"+objRequestFromServer.getDetailId());
	      return request;

		
	}

}
