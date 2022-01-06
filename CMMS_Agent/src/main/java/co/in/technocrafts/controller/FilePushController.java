package co.in.technocrafts.controller;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.MediaType;

import co.in.technocrafts.model.FileRequestData;
import co.in.technocrafts.model.RequestFromServer;
import co.in.technocrafts.model.ServerRequestData;
import co.in.technocrafts.service.FileService;

@RestController
@RequestMapping("/file")
public class FilePushController {
	
	@Autowired
	FileService objFileService;
	
	
	private void zipFolder(Path sourceFolderPath, Path zipPath) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
        Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                Files.copy(file, zos);
                zos.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });
        zos.close();
    }
	
	@RequestMapping(value="/zip", produces="application/zip")
	public void zipFiles(HttpServletResponse response) throws IOException {

	    //setting headers  
	    response.setStatus(HttpServletResponse.SC_OK);
	    response.addHeader("Content-Disposition", "attachment; filename=\"test.zip\"");

	    ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

	    // create a list to add files to be zipped
	    ArrayList<File> files = new ArrayList<>(2);
	    files.add(new File("/home/TS/Downloads/jar/1.jpg"));

	    // package files
	    for (File file : files) {
	        //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
	        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
	        FileInputStream fileInputStream = new FileInputStream(file);

	        IOUtils.copy(fileInputStream, zipOutputStream);

	        fileInputStream.close();
	        zipOutputStream.closeEntry();
	    }    

	    zipOutputStream.close();
	}
	
	
	
	
	
	
	 @GetMapping("/downloadfile/{folderpath}/{fileName}")
	 public ResponseEntity<UrlResource> downloadFile(@PathVariable(value = "folderpath") String folderpath,
	         @PathVariable String fileName, HttpServletRequest request) {
	     // Load file as Resource
	     UrlResource resource;

	     String fileBasePath = "/home/TS/Downloads/" + folderpath
	             + "/";
	     Path path = Paths.get(fileBasePath + fileName);
	     try {
	         resource = new UrlResource(path.toUri());
	     } catch (MalformedURLException e) {
	         e.printStackTrace();
	         return null;
	     }

	     // Try to determine file's content type
	     String contentType = null;
	     try {
	         contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	     } catch (IOException ex) {
	         System.out.println("Could not determine file type.");
	     }

	     // Fallback to the default content type if type could not be determined
	     if (contentType == null) {
	         contentType = "application/octet-stream";
	     }

	     return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
	             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	             .body(resource);
	 }

	
	/*
	 * // // // @GetMapping("/download/{fileName:.+}") // public
	 * ResponseEntity<Resource> downloadFileFromLocal(@PathVariable String fileName)
	 * { // Path path = Paths.get("/home/TS/Downloads/jar/" + fileName); // Resource
	 * resource = null; // try { // resource = (Resource) new
	 * UrlResource(path.toUri()); // } catch (MalformedURLException e) { //
	 * e.printStackTrace(); // } // return ResponseEntity.ok() // //
	 * .contentType(MediaType.parseMediaType(contentType)) //
	 * .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
	 * ((FileSystemResource) resource).getFilename() + "\"") // .body(resource); //
	 * } //
	 */	
//	@GetMapping(value = "/zip-download", produces="application/zip")
//	public void zipDownload(@RequestParam ArrayList<String> name, HttpServletResponse response) throws IOException {
//		ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
//		for (String fileName : name) {
//			FileSystemResource resource = new FileSystemResource("/home/TS/Downloads/jar/" + fileName);
//			ZipEntry zipEntry = new ZipEntry(resource.getFilename());
//			zipEntry.setSize(resource.contentLength());
//			zipOut.putNextEntry(zipEntry);
//			StreamUtils.copy(resource.getInputStream(), zipOut);
//			zipOut.closeEntry();
//		}
//		zipOut.finish();
//		zipOut.close();
//		response.setStatus(HttpServletResponse.SC_OK);
//		response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "zipFileName.zip" + "\"");
//	}
//	
//	
	
	
	/**
	 * @param objFileRequestData
	 * @return
	 */
	@RequestMapping("/data")
	public HashMap<String, java.util.List<String>> getData(@RequestBody FileRequestData objFileRequestData)
	{
		
		
		
        HashMap<String, java.util.List<String>> data = new HashMap<>();
		String lastSendData=objFileRequestData.getLastLineData();
		
		ArrayList<String> dataTosend = new ArrayList<String>();
		
		Scanner input1;
		try {
			input1 = new Scanner(new File(objFileRequestData.getFileName()));
			
			
			while(input1.hasNextLine()){
				 String   first = input1.nextLine();   

				    if(!lastSendData.equals(first)){
				    	
				    	dataTosend.add(first);
				    }
				    else
				    {
				    	dataTosend = new ArrayList<String>();
				    }
				}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		try {
			//zipFolder(Paths.get("/home/TS/Downloads/jar"), Paths.get("/home/TS/Downloads/jar.zip"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data.put(objFileRequestData.getFileName(), dataTosend);
		
		return data;
	}

	
	
	
	@RequestMapping("/serverRequest")
	public String requestFromServer(@RequestBody ServerRequestData objServerRequestData)
	{
		
		System.out.println(objServerRequestData);
		
       // HashMap<String, java.util.List<String>> data = new HashMap<>();
		//String lastSendData=objFileRequestData.getLastLineData();
		
	//	ArrayList<String> dataTosend = new ArrayList<String>();
		
		objFileService.fullScreenShot(objServerRequestData.filename);
        byte[] bytesData;
		try {
			bytesData = Files.readAllBytes(Paths.get(objServerRequestData.filename));
			objServerRequestData.setBytesDataToServer(bytesData);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	data.put(objServerRequestData.getFilename(), objServerRequestData);
		
		return objServerRequestData.toString();
	}
	
	
	@RequestMapping("/serverRequest2")
	public String requestFromServer2(@RequestBody ServerRequestData objServerRequestData)
	{
		
		System.out.println(objServerRequestData);
		// File upload service upload interface
	      String url = "http://localhost:5992/file/save";
	      // Files to be uploaded (with client local disk)
	      String filePath = "/home/TS/Downloads/55.zip";
		    RestTemplate restTemplate = new RestTemplate();

	      // Encapsulate request parameters
	      FileSystemResource resource = new FileSystemResource(new File(filePath));
	      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
	      param.add("image", resource);  //Server MultipartFile uploadFile
	      //param.add("param1", "test"); / / if the server accepts additional parameters, it can pass


	      // Send the request and output the result
	      System.out.println("--- Start uploading files ---");
	      String result = restTemplate.postForObject(url, param, String.class);
	      System.out.println("--- Visit address:" + result);
       
		
		return objServerRequestData.toString();
	}
	
	
	
	

	@RequestMapping("/serverRequest3")
	public String requestFromServer3(@RequestBody ServerRequestData objServerRequestData)
	{
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		
		System.out.println(objServerRequestData);
		// File upload service upload interface
	      String url = "http://localhost:5992/file/save";
	      // Files to be uploaded (with client local disk)
	      String filePath = "/home/TS/Downloads/55.zip";
		    RestTemplate restTemplate = new RestTemplate();

	      // Encapsulate request parameters
	      FileSystemResource resource = new FileSystemResource(new File(filePath));
	      MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
	      param.add("image", resource);  //Server MultipartFile uploadFile
	      //param.add("param1", "test"); / / if the server accepts additional parameters, it can pass


	      // Send the request and output the result
//	      System.out.println("--- Start uploading files ---");
//	      String result = restTemplate.postForObject(url, param, String.class);
//	      System.out.println("--- Visit address:" + result);
//       
	      HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(
	    		  param, headers);

	    	    ResponseEntity<String> response = restTemplate.postForEntity(url, request,
	    	        String.class);
	      
	      
	      
	      
	      
	      
	      
		return objServerRequestData.toString();
	}
	
	
	
	
	@RequestMapping("/serverData")
	public String requestFromServer(@RequestBody RequestFromServer objRequestFromServer)
	{
		
		System.out.println(objRequestFromServer);
		
      
		
		return "";
	}

	
	
	
	
	
	
	
	@PostMapping("/save")
    public String saveUser( @RequestParam("image") MultipartFile multipartFile) throws IOException {
         System.out.println("@@@@@@@@@");
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println(fileName);
       // user.setPhotos(fileName);
         
       // User savedUser = repo.save(user);
 
      //  String uploadDir = "user-photos/" + savedUser.getId();
        String fileNameWithDirectory="/home/TS/Downloads/jar/" + fileName;
 
        FileService.saveFile("/home/TS/Downloads/jar/", fileName, multipartFile);
        
        String unzipDirectory="/home/TS/Downloads/jar/unzip/";
        FileUtils.cleanDirectory(new File(unzipDirectory)); 
        
       FileService.Unzipfile(fileNameWithDirectory, unzipDirectory);
       
		File[] files = new File(unzipDirectory).listFiles((dir, name) -> name.endsWith(".sh"));

       
     //  FileService.excuteCommand(unzipDirectory+"*.sh");
         
		
		  FileService.excuteCommand(files[0].toString());
	        
		
        return "";
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
}
