/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ts.axis.middleware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Akshay
 */
public class AxisTSMJar {
    String pathDirctory = new  String("/home/TS/webservice/");
    
    String baseUrl="";
    String baseUrlrequired="";
    
    String blrUrl="";
    String baseUrlrequiredBlr="";
    
    String crdUrl="";
    String baseUrlrequiredCrd="";
       
    String ackUrl="";
    String baseUrlrequiredAck="";
    
    String BarcodeMappingUrl="";
    String baseUrlrequiredBarcodeMapping="";
    
    String FirstpagePrintingUrl="";
    String baseUrlrequiredFirstpagePrinting="";
       
    String AccountValidationMobileUrl="";
    String baseUrlrequiredAccountValidationMobile="";
   
    String TerminalAuthenticationUrl="";
    String baseUrlrequiredTerminalAuthentication="";
   
    
    
    
    public AxisTSMJar()
    {
       String data= getJsonData();
    }
    
    
    
    
   
   

    String URLTerminalAuthentication = "http://20.197.56.56/AXIS_PBK/api/TerminalAuthentication/PostAsync";
    String URLTransactionRequestMsg = "http://20.197.56.56/AXIS_PBK/api/";
    String URLTransactionMISXML = "";
    String URLTransactionRequestMsgIssuance = "";
    String URLGenerateOTPRequest = "";
    String URLValidateOTPRequest = "";
    String filePathToWriteLog = "/home/TS/TSM";
    String timeoutread="100000";
    String timeoutconnection="100000";
    
    
    
    public  String getJsonData() {
            
            
                        boolean flag = false;

            try
            {

                File homeConfig = new File(pathDirctory+"acu/apiConfig.json");
            if (homeConfig.exists()) 
            {
               flag = true;
              // rootConfig = testConfig;
            }


            if (flag == false) {
                throw new Exception();
            }
            
                   JSONParser parser = new JSONParser();
              Object obj = parser.parse(new FileReader(homeConfig));
              JSONObject jsonObject = (JSONObject)obj;
              
             JSONObject jsonObjApi_Config_File = (JSONObject)jsonObject.get("Api_Config_File");
             JSONObject jsonObjwebSerivce = (JSONObject)jsonObjApi_Config_File.get("webSerivce");
             JSONObject jsonObjTracingUtility_Config = (JSONObject)jsonObjApi_Config_File.get("TracingUtility_Config");
           
            filePathToWriteLog = (String)jsonObjTracingUtility_Config.get("Trace_Path");

            
            
            JSONObject jsonObjbaseUrl = (JSONObject)jsonObjwebSerivce.get("baseUrl");
            JSONObject jsonObjBLRUrl = (JSONObject)jsonObjwebSerivce.get("BLRUrl");
            JSONObject jsonObjCRDUrl = (JSONObject)jsonObjwebSerivce.get("CRDUrl");
            JSONObject jsonObjACKUrl = (JSONObject)jsonObjwebSerivce.get("ACKUrl");
            
            JSONObject jsonObjBarcodeMappingUrl = (JSONObject)jsonObjwebSerivce.get("BarcodeMappingUrl");
            JSONObject jsonObjFirstpagePrintingUrl = (JSONObject)jsonObjwebSerivce.get("FirstpagePrintingUrl");
            JSONObject jsonObjAccountValidationMobileUrl = (JSONObject)jsonObjwebSerivce.get("AccountValidationMobileUrl");
            JSONObject jsonObjTerminalAuthenticationUrl = (JSONObject)jsonObjwebSerivce.get("TerminalAuthenticationUrl");
            
            
            
            
              baseUrlrequired = (String)jsonObjbaseUrl.get("isRequired");
              baseUrl= (String)jsonObjbaseUrl.get("url");
              
               baseUrlrequiredBlr = (String)jsonObjBLRUrl.get("baseUrlisRequired");
              blrUrl= (String)jsonObjBLRUrl.get("url");
             
              
               baseUrlrequiredCrd = (String)jsonObjCRDUrl.get("baseUrlisRequired");
              crdUrl= (String)jsonObjCRDUrl.get("url");
             
              
               baseUrlrequiredAck = (String)jsonObjACKUrl.get("baseUrlisRequired");
              ackUrl= (String)jsonObjACKUrl.get("url");
             
               baseUrlrequiredBarcodeMapping = (String)jsonObjBarcodeMappingUrl.get("baseUrlisRequired");
              BarcodeMappingUrl= (String)jsonObjBarcodeMappingUrl.get("url");
             
               baseUrlrequiredFirstpagePrinting = (String)jsonObjFirstpagePrintingUrl.get("baseUrlisRequired");
              FirstpagePrintingUrl= (String)jsonObjFirstpagePrintingUrl.get("url");
             
               baseUrlrequiredAccountValidationMobile = (String)jsonObjAccountValidationMobileUrl.get("baseUrlisRequired");
              AccountValidationMobileUrl= (String)jsonObjAccountValidationMobileUrl.get("url");
              baseUrlrequiredTerminalAuthentication = (String)jsonObjTerminalAuthenticationUrl.get("baseUrlisRequired");
              TerminalAuthenticationUrl= (String)jsonObjTerminalAuthenticationUrl.get("url");
             
              if(baseUrlrequired.equalsIgnoreCase("true"))
              {
                  if(baseUrlrequiredTerminalAuthentication.equalsIgnoreCase("true"))
                  {
                      TerminalAuthenticationUrl = baseUrl+TerminalAuthenticationUrl;
                  }
                  
                  if(baseUrlrequiredBarcodeMapping.equalsIgnoreCase("true"))
                  {
                      BarcodeMappingUrl=baseUrl+BarcodeMappingUrl;
                  }
                  if(baseUrlrequiredFirstpagePrinting.equalsIgnoreCase("true"))
                  {
                      FirstpagePrintingUrl=baseUrl+FirstpagePrintingUrl;
                  }
                  
                  if(baseUrlrequiredAccountValidationMobile .equalsIgnoreCase("true"))
                  {
                      AccountValidationMobileUrl=baseUrl+AccountValidationMobileUrl;
                  }
                  
                  if(baseUrlrequiredBlr.equalsIgnoreCase("true"))
                  {
                      blrUrl=baseUrl+blrUrl;
                  }
                  
                  if(baseUrlrequiredCrd.equalsIgnoreCase("true"))
                  {
                      crdUrl=baseUrl+crdUrl;
                  }
                  
                  if(baseUrlrequiredAck.equalsIgnoreCase("true"))
                  {
                                  ackUrl=baseUrl+ackUrl;
                  }
                  
                  
              }
              
              
             timeoutread= (String)jsonObjwebSerivce.get("readTimeout");
             timeoutconnection= (String)jsonObjwebSerivce.get("connectionTimeout");

           //  System.out.print(strAuto_Start+strFont+strCPI+strLPI+strLower_Barcode_length+strImage_Print_Path+strPrint_File_Path+strTrace_Path);

            }
            catch(Exception e)
            {
             e.printStackTrace();
            }
        
            return String.valueOf(flag);
        }
    
    
    
    
    
    
    
    

    public String GetDataFromMiddleware(String urlString, String jsonString) {
        try {
           // WriteToTransactionLogs(filePathToWriteLog, "\n\n");
            WriteToTransactionLogs(filePathToWriteLog, "URL  :  " + urlString);
            WriteToTransactionLogs(filePathToWriteLog, "Request Data :  " + jsonString);
            URL objURL = new URL(urlString);
            HttpURLConnection postConnection = (HttpURLConnection) objURL.openConnection();
            postConnection.setRequestMethod("POST");
             postConnection.setReadTimeout(Integer.parseInt(timeoutread));
             postConnection.setConnectTimeout(Integer.parseInt(timeoutconnection));
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setDoOutput(true);
            OutputStream os = postConnection.getOutputStream();
            os.write(jsonString.getBytes());
            os.flush();
            os.close();
            int responseCode = postConnection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));
                StringBuffer bufferResponse = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    bufferResponse.append(inputLine);
                }
                in.close();
                String response = bufferResponse.toString().replace("\"", "");

                //System.out.println(response);
                WriteToTransactionLogs(filePathToWriteLog,"responseCode : "+responseCode+ " Response Data :  " + response);
                return response;

            } else {
                WriteToTransactionLogs(filePathToWriteLog,"responseCode : "+responseCode+ " Response Data :  " + postConnection.getResponseMessage());

            }

        } catch (Exception e) {
            WriteToTransactionLogs(filePathToWriteLog, "Response Data :  " + e.getMessage());

        }
        return "Unsuccessful";
    }

    public String TerminalAuthentication(String ipAddress, String kioskId) {

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("Param1", ipAddress);
        data.put("Param2", kioskId);
        JSONObject json = new JSONObject();
        json.putAll(data);
        String stringData = GetDataFromMiddleware(URLTerminalAuthentication, json.toString());
        System.out.println(stringData);

        return stringData;
    }

     public String[] TransactionRequestMsg(String accountNumber, String processingCode, String stanNo, String localTransactionTime, String captureDate, String accountIdentification1, String field125, String ipAddress, String msgType) {

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("Param1",ipAddress); //terminal no   ap ipAddress
        data.put("Param2", stanNo); //stanNo ap stan 
        data.put("Param3", accountIdentification1); // ap accountIdentification1   bankcode branch code accountnumber
        data.put("Param4", field125); // kskb1 account number  ap field 125
      //  data.put("Param5", "01A00002");// 
       // data.put("Param6", "");
       
       WriteToTransactionLogs(filePathToWriteLog, "msgType  :  " + msgType);

        System.out.println("msgType "+msgType);
        if(msgType.contains("BLR"))
        {
            URLTransactionRequestMsg=blrUrl;
        }
        else if (msgType.contains("CRD"))
        {
            URLTransactionRequestMsg=crdUrl;;
        }
        else if (msgType.contains("ACK"))
        {
            URLTransactionRequestMsg=ackUrl;
        }
        else
        {
            
        }
        JSONObject json = new JSONObject();
        json.putAll(data);
        String stringData = GetDataFromMiddleware(URLTransactionRequestMsg, json.toString());
        System.out.println(stringData);

        if(stringData.equalsIgnoreCase("Unsuccessful"))
        {
            return new String[]{"Unsuccessful"};
        }
        return stringData.split("\\|");
    }

    public void TransactionMISXML(String kioskId, String branchId, String accountNumber, String accountHolderName, String transactionStatus, String transactionDateTime, String terminalIp, String transactionType) {

    }

//         public void TransactionMISXMLForIssuance(String kioskId, String branchId, String accountNumber, String accountHolderName, String transactionStatus, String transactionDateTime, String terminalIp, String transactionType) {
//         }
    public String TransactionRequestMsgIssuance(String accountNumber, String processingCode, String stanNo, String localTransactionTime, String captureDate, String accountIdentification1, String field125, String ipAddress, String msgType) {

         Map<String, Object> data = new HashMap<String, Object>();
        data.put("Param1",ipAddress); //terminal no   ap ipAddress
        data.put("Param2", stanNo); //stanNo ap stan 
        data.put("Param3", accountIdentification1); // ap accountIdentification1   bankcode branch code accountnumber
        data.put("Param4", field125); // kskb1 account number  ap field 125
      //  data.put("Param5", "01A00002");// 
       // data.put("Param6", "");
               WriteToTransactionLogs(filePathToWriteLog, "\n\nmsgType  :  " + msgType);

        JSONObject json = new JSONObject();
        json.putAll(data);
        
        String urltocurrent= "";
        if(msgType.contains("Barcode_Mapping"))
        {
            urltocurrent=BarcodeMappingUrl;
        }
        else if (msgType.contains("First_Page"))
        {
            urltocurrent=FirstpagePrintingUrl;
        }
        else if (msgType.contains("Account_Validation"))
        {
            urltocurrent=AccountValidationMobileUrl;
        }
        else
        {
            
        }
        String stringData = GetDataFromMiddleware(urltocurrent, json.toString());
       
        
        
        
        
        return stringData;
    }

    public String GenerateOTPRequest(String IP_Trace, String _refId, String _phoneNo) {
        return "";
    }

    public String ValidateOTPRequest(String IP_Trace, String _refId, String _otp) {
        return "";
    }

    public static boolean WriteToTransactionLogs(String pathname, String Data) {
        try {

//            File myFile = new File("/root/forbes/sp_plq22/acu/config.xml");
//
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder;
//
//            dBuilder = dbFactory.newDocumentBuilder();
//
//            Document doc = dBuilder.parse(myFile);
//            doc.getDocumentElement().normalize();
//           // System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//            NodeList nList = doc.getElementsByTagName("TracingUtility_Config");
//            Node nNode = nList.item(0);
//
//            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//                Element eElement = (Element) nNode;
//                tracePath = eElement.getElementsByTagName("Trace_Path").item(0).getTextContent();
            // tracePath = pathname;
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String filename = pathname + date + ".txt";
            File f = new File(pathname);
            File parentpath = f.getParentFile();

            File fDirectory = new File(pathname);
            if (fDirectory.exists() && fDirectory.isDirectory()) {

            } else {
                fDirectory.mkdir();
            }

            if (!parentpath.exists()) {

                return false;

            }

            File file = new File(filename);
            file.createNewFile();
            FileWriter fw = new FileWriter(file, true);
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
            Date now = new Date();
            String strDate = sdfDate.format(now);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\n" + strDate + "\t" + Data);
            bw.close();
            //}
            return true;
        } catch (Exception ex) {

            return false;
        }
    }

}
