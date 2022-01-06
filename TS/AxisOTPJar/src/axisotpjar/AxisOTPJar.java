package axisotpjar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AxisOTPJar {
   private String responseString = "Successful";
   private String[] responseArray;
   final String nameSpace = "http://tempuri.org/";
   private final String soapAction2 = "http://tempuri.org/IService1/TransactionRequestMsg";
   private final String soapAction3 = "http://tempuri.org/IService1/GenerateOTPRequest";
   private final String soapAction4 = "http://tempuri.org/IService1/ValidateOTPRequest";
   private final String method2 = "TransactionRequestMsg";
   private final String method3 = "GenerateOTPRequest";
   private final String method4 = "ValidateOTPRequest";
   private String xmlACUPath = "/root/forbes/microbanker/acu/ApiConfig.xml";
   private String url = "";
   private String tracePath = "";
   private int urlTimeout;
   private boolean flag = true;

   private void ReadACU() {
      try {
         File ACUCofig = new File(this.xmlACUPath);
         if (ACUCofig.exists()) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(ACUCofig);
            doc.getDocumentElement().normalize();
            NodeList ParentList = doc.getElementsByTagName("CONFIGURATION");
            Node FirstParent = ParentList.item(0);
            if (FirstParent.getNodeType() == 1) {
               Element WebServiceElement = (Element)FirstParent;
               NodeList WebServiceList = WebServiceElement.getElementsByTagName("WebSerivce");
               Node WebServiceNode = WebServiceList.item(0);
               Element WebServiceElement1 = (Element)WebServiceNode;
               NodeList UrlList = WebServiceElement1.getElementsByTagName("URL");
               Element UrlElement = (Element)UrlList.item(0);
               NodeList UrlChildList = UrlElement.getChildNodes();
               this.url = UrlChildList.item(0).getNodeValue();
               NodeList UrlTimeoutList = WebServiceElement1.getElementsByTagName("URL_TIMEOUT");
               Element UrlTimeoutElement = (Element)UrlTimeoutList.item(0);
               NodeList UrlTimeoutChildList = UrlTimeoutElement.getChildNodes();
               this.urlTimeout = Integer.parseInt(UrlTimeoutChildList.item(0).getNodeValue());
               NodeList TracePathList = WebServiceElement1.getElementsByTagName("Trace_Path");
               Element TracePathElement = (Element)TracePathList.item(0);
               NodeList TracePathChildList = TracePathElement.getChildNodes();
               this.tracePath = TracePathChildList.item(0).getNodeValue();
               this.WriteToTrace("AxisOTPJar", "TracePath : " + this.tracePath);
               this.WriteToTrace("AxisOTPJar", "Url : " + this.url);
               this.WriteToTrace("AxisOTPJar", "Url Timeout : " + this.urlTimeout);
            }
         } else {
            this.flag = false;
            this.WriteToTrace("AxisOTPJar", "PBKConfig does not exist.");
            System.out.println("PBKConfig does not exist.");
         }
      } catch (Exception var20) {
         this.flag = false;
         this.WriteToTrace("AxisOTPJar", "Exception occured in ReadACU : " + var20.toString());
         System.out.println("Exception occured in ReadACU : " + var20.toString());
      }

   }

   public String TransactionRequestMsg(String accountNumber, String processingCode, String stanNo, String localTransactionTime, String captureDate, String accountIdentification1, String field125, String ipAddress, String msgType) {
      try {
         this.ReadACU();
         this.WriteToTrace("AxisOTPJar", "--------------------Entered into TransactionRequestMsg--------------------");
         SoapObject request = new SoapObject("http://tempuri.org/", "TransactionRequestMsg");
         if (this.flag) {
            String requestString = msgType + "|" + accountNumber + "|" + processingCode + "|" + stanNo + "|" + localTransactionTime + "|" + captureDate + "|" + accountIdentification1 + "|" + field125;
            this.WriteToTrace("AxisOTPJar", requestString);
            PropertyInfo ipAddressInfo = new PropertyInfo();
            ipAddressInfo.setName("ipAddress");
            ipAddressInfo.setValue(ipAddress);
            ipAddressInfo.setType(String.class);
            request.addProperty(ipAddressInfo);
            this.WriteToTrace("AxisOTPJar", "Ip Address : " + ipAddress);
            PropertyInfo requestInfo = new PropertyInfo();
            requestInfo.setName("requestString");
            requestInfo.setValue(requestString);
            requestInfo.setType(String.class);
            request.addProperty(requestInfo);
            this.WriteToTrace("AxisOTPJar", "Request String : " + requestString);
            PropertyInfo msgTypeInfo = new PropertyInfo();
            msgTypeInfo.setName("msgType");
            msgTypeInfo.setValue(msgType);
            msgTypeInfo.setType(String.class);
            request.addProperty(msgTypeInfo);
            this.WriteToTrace("AxisOTPJar", "Message type : " + msgType);

            try {
               SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
               envelope.dotNet = true;
               envelope.setOutputSoapObject(request);
               HttpTransportSE servieHttpTransport = new HttpTransportSE(this.url, this.urlTimeout);
               servieHttpTransport.call("http://tempuri.org/IService1/TransactionRequestMsg", envelope);
               SoapPrimitive soapResponse = (SoapPrimitive)envelope.getResponse();
               this.responseString = soapResponse.toString();
               this.WriteToTrace("AxisOTPJar", "Response String : " + this.responseString);
               this.responseArray = this.responseString.split("\\|");
            } catch (Exception var18) {
               this.responseArray = new String[1];
               this.responseArray[0] = "Unsuccessful";
               this.WriteToTrace("AxisOTPJar", "Exeption occured into Url connection : " + var18.toString());
            }
         } else {
            this.responseArray = new String[1];
            this.responseArray[0] = "Unsuccessful";
            this.WriteToTrace("AxisOTPJar", "PBK config does not exist");
         }
      } catch (Exception var19) {
         this.WriteToTrace("AxisOTPJar", "Exception : " + var19.toString());
      }

      return this.responseString;
   }

   public String GenerateOTPRequest(String IP_Trace, String _refId, String _phoneNo, String _acNo) {
      try {
         this.ReadACU();
         this.WriteToTrace("AxisOTPJar", "--------------------Entered into GenerateOTPRequest--------------------");
         SoapObject request = new SoapObject("http://tempuri.org/", "GenerateOTPRequest");
         if (this.flag) {
            String requestString = IP_Trace + "|" + _refId + "|" + _phoneNo;
            this.WriteToTrace("AxisOTPJar", requestString);
            PropertyInfo ipTraceInfo = new PropertyInfo();
            ipTraceInfo.setName("IP_Trace");
            ipTraceInfo.setValue(IP_Trace);
            ipTraceInfo.setType(String.class);
            request.addProperty(ipTraceInfo);
            this.WriteToTrace("AxisOTPJar", "Ip Trace : " + IP_Trace);
            PropertyInfo refIDInfo = new PropertyInfo();
            refIDInfo.setName("_refId");
            refIDInfo.setValue(_refId);
            refIDInfo.setType(String.class);
            request.addProperty(refIDInfo);
            this.WriteToTrace("AxisOTPJar", "Ref ID : " + _refId);
            PropertyInfo phoneNoInfo = new PropertyInfo();
            phoneNoInfo.setName("_phoneNo");
            phoneNoInfo.setValue(_phoneNo);
            phoneNoInfo.setType(String.class);
            request.addProperty(phoneNoInfo);
            this.WriteToTrace("AxisOTPJar", "Phone No : " + _phoneNo);
            
            
            PropertyInfo acNoInfo = new PropertyInfo();
            phoneNoInfo.setName("_acNo");
            phoneNoInfo.setValue(_acNo);
            phoneNoInfo.setType(String.class);
            request.addProperty(acNoInfo);
            this.WriteToTrace("AxisOTPJar", "_acNo No : " + _acNo);



            try {
               SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
               envelope.dotNet = true;
               envelope.setOutputSoapObject(request);
               HttpTransportSE servieHttpTransport = new HttpTransportSE(this.url, this.urlTimeout);
               servieHttpTransport.call("http://tempuri.org/IService1/GenerateOTPRequest", envelope);
               SoapPrimitive soapResponse = (SoapPrimitive)envelope.getResponse();
               this.responseString = soapResponse.toString();
               this.WriteToTrace("AxisOTPJar", "Response String : " + this.responseString);
               this.responseArray = this.responseString.split("\\|");
            } catch (Exception var12) {
               this.responseArray = new String[1];
               this.responseArray[0] = "Unsuccessful";
               this.WriteToTrace("AxisOTPJar", "Exeption occured into Url connection : " + var12.toString());
            }
         } else {
            this.responseArray = new String[1];
            this.responseArray[0] = "Unsuccessful";
            this.WriteToTrace("AxisOTPJar", "API config does not exist");
         }
      } catch (Exception var13) {
         this.WriteToTrace("AxisOTPJar", "Exception : " + var13.toString());
      }

      return this.responseString;
   }

   public String ValidateOTPRequest(String IP_Trace, String _refId, String _otp) {
      try {
         this.ReadACU();
         this.WriteToTrace("AxisOTPJar", "--------------------Entered into ValidateOTPRequest--------------------");
         SoapObject request = new SoapObject("http://tempuri.org/", "ValidateOTPRequest");
         if (this.flag) {
            String requestString = IP_Trace + "|" + _refId + "|" + _otp;
            this.WriteToTrace("AxisOTPJar", requestString);
            PropertyInfo ipTraceInfo = new PropertyInfo();
            ipTraceInfo.setName("IP_Trace");
            ipTraceInfo.setValue(IP_Trace);
            ipTraceInfo.setType(String.class);
            request.addProperty(ipTraceInfo);
            this.WriteToTrace("AxisOTPJar", "Ip Trace : " + IP_Trace);
            PropertyInfo refIDInfo = new PropertyInfo();
            refIDInfo.setName("_refId");
            refIDInfo.setValue(_refId);
            refIDInfo.setType(String.class);
            request.addProperty(refIDInfo);
            this.WriteToTrace("AxisOTPJar", "Ref ID : " + _refId);
            PropertyInfo OTPInfo = new PropertyInfo();
            OTPInfo.setName("_otp");
            OTPInfo.setValue(_otp);
            OTPInfo.setType(String.class);
            request.addProperty(OTPInfo);
            this.WriteToTrace("AxisOTPJar", "OTP : " + _otp);

            try {
               SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(110);
               envelope.dotNet = true;
               envelope.setOutputSoapObject(request);
               HttpTransportSE servieHttpTransport = new HttpTransportSE(this.url, this.urlTimeout);
               servieHttpTransport.call("http://tempuri.org/IService1/ValidateOTPRequest", envelope);
               SoapPrimitive soapResponse = (SoapPrimitive)envelope.getResponse();
               this.responseString = soapResponse.toString();
               this.WriteToTrace("AxisOTPJar", "Response String : " + this.responseString);
               this.responseArray = this.responseString.split("\\|");
            } catch (Exception var12) {
               this.responseArray = new String[1];
               this.responseArray[0] = "Unsuccessful";
               this.WriteToTrace("AxisOTPJar", "Exeption occured into Url connection : " + var12.toString());
            }
         } else {
            this.responseArray = new String[1];
            this.responseArray[0] = "Unsuccessful";
            this.WriteToTrace("AxisOTPJar", "API config does not exist");
         }
      } catch (Exception var13) {
         this.WriteToTrace("AxisOTPJar", "Exception : " + var13.toString());
      }

      this.WriteToTrace("AxisOTPJar", "Exit from ValidateOTPRequest");
      return this.responseString;
   }

   private void WriteToTrace(String Name, String string) {
      DateFormat dataformat = new SimpleDateFormat("ddMMyy");
      DateFormat timeformat = new SimpleDateFormat("dd/MM/yy hh:mm:sss a");
      Date date = new Date();
      String s = timeformat.format(date) + "    " + Name + " : " + string + "\r\n";
      byte[] data = s.getBytes();
      File traceFilePath = new File(this.tracePath);
      if (!traceFilePath.exists()) {
         traceFilePath.mkdir();
      }

      Path p = Paths.get(this.tracePath + "Trace File_" + dataformat.format(date) + ".txt");

      try {
         OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, StandardOpenOption.CREATE, StandardOpenOption.APPEND));
         Throwable var11 = null;

         try {
            out.write(data, 0, data.length);
         } catch (Throwable var21) {
            var11 = var21;
            throw var21;
         } finally {
            if (out != null) {
               if (var11 != null) {
                  try {
                     out.close();
                  } catch (Throwable var20) {
                     var11.addSuppressed(var20);
                  }
               } else {
                  out.close();
               }
            }

         }
      } catch (Exception var23) {
         System.out.println("Exception occured in WriteToTrace : " + var23.toString());
      }

   }
}