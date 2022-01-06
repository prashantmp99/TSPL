package in.co.kalupur;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CommunicationToCBS {
   private String strIpAddress = "";
   private int intPort;
   private int intServertimeout;
   private String xmlACUPath = "/home/TS/PBK/acu/ApiConfig.json";
   public String TracePath = "";
   public String Bitmap_path="";
   public String Transaction_Request_Path="";
   public String Transaction_Response_Path="";
   private Socket socket = new Socket();
   public String strSslUrl = "";
   public String strSsltimeOut = "";
   public boolean isRequestTimeout = false;
   
   
   
      public void getconfigDetails(String TransType) {
          try
          {
          JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(new File(xmlACUPath)));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject jsonObjCONFIGURATION = (JSONObject) jsonObject.get("CONFIGURATION");
            JSONObject jsonObjMFK_ApplicationManager_Config = (JSONObject) jsonObjCONFIGURATION.get("MFK_ApplicationManager_Config");
            JSONObject jsonObjMFK_COMMUNICATION_CONFIG = (JSONObject) jsonObjMFK_ApplicationManager_Config.get("MFK_COMMUNICATION_CONFIG");

            intPort = Integer.parseInt((String)(jsonObjMFK_COMMUNICATION_CONFIG.get("Network_Port_Passbook")));
            intServertimeout = Integer.parseInt((String)(jsonObjMFK_COMMUNICATION_CONFIG.get("ServerRequestTimeOut")));
            TracePath = (String)(jsonObjMFK_COMMUNICATION_CONFIG.get("Trace_Path"));
            Bitmap_path = (String)(jsonObjMFK_COMMUNICATION_CONFIG.get("Bitmap_path"));
            Transaction_Request_Path = (String)(jsonObjMFK_COMMUNICATION_CONFIG.get("Transaction_Request_Path"));
            Transaction_Response_Path = (String)(jsonObjMFK_COMMUNICATION_CONFIG.get("Transaction_Response_Path"));
            strSslUrl = (String)(jsonObjMFK_COMMUNICATION_CONFIG.get("SslUrl"));
            strSsltimeOut = (String)(jsonObjMFK_COMMUNICATION_CONFIG.get("SslRequestTimeOut"));
            strIpAddress = (String)(jsonObjMFK_COMMUNICATION_CONFIG.get("IP_Address_Passbook"));


            
          }
          catch(Exception e)
          {
              e.printStackTrace();
          }
      }

   public void getconfigDetails1(String TransType) {
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
               Element FirstParentElement = (Element)FirstParent;
               NodeList MFKAppList = FirstParentElement.getElementsByTagName("MFK_ApplicationManager_Config");
               Node MFKAppNode = MFKAppList.item(0);
               if (MFKAppNode.getNodeType() == 1) {
                  Element MFKAppElement = (Element)MFKAppNode;
                  NodeList MFKCommList = MFKAppElement.getElementsByTagName("MFK_COMMUNICATION_CONFIG");
                  Node MFKCommNode = MFKCommList.item(0);
                  Element MFKCommElement = (Element)MFKCommNode;
                  NodeList IPList;
                  Element IPElement;
                  NodeList IPChildList;
                  NodeList PortList;
                  Element PortElement;
                  NodeList PortChildList;
                  if (!TransType.equals("CashDeposit_BLR") && !TransType.equals("CashDeposit_CRD") && !TransType.equals("ChequeDeposit_BLR") && !TransType.equals("ChequeDeposit_CRD")) {
                     if (TransType.equals("PassbookPrinting_BLR") || TransType.equals("PassbookPrinting_CRD") || TransType.equals("PassbookPrinting_ACK")) {
                        IPList = MFKCommElement.getElementsByTagName("IP_Address_Passbook");
                        IPElement = (Element)IPList.item(0);
                        IPChildList = IPElement.getChildNodes();
                        this.strIpAddress = IPChildList.item(0).getNodeValue();
                        PortList = MFKCommElement.getElementsByTagName("Network_Port_Passbook");
                        PortElement = (Element)PortList.item(0);
                        PortChildList = PortElement.getChildNodes();
                        this.intPort = Integer.parseInt(PortChildList.item(0).getNodeValue());
                     }
                  } else {
                     IPList = MFKCommElement.getElementsByTagName("IP_Address_Cash");
                     IPElement = (Element)IPList.item(0);
                     IPChildList = IPElement.getChildNodes();
                     this.strIpAddress = IPChildList.item(0).getNodeValue();
                     PortList = MFKCommElement.getElementsByTagName("Network_Port_Cash");
                     PortElement = (Element)PortList.item(0);
                     PortChildList = PortElement.getChildNodes();
                     this.intPort = Integer.parseInt(PortChildList.item(0).getNodeValue());
                  }

                  IPList = MFKCommElement.getElementsByTagName("ServerRequestTimeOut");
                  IPElement = (Element)IPList.item(0);
                  IPChildList = IPElement.getChildNodes();
                  this.intServertimeout = Integer.parseInt(IPChildList.item(0).getNodeValue());
                  PortList = MFKCommElement.getElementsByTagName("Trace_Path");
                  PortElement = (Element)PortList.item(0);
                  PortChildList = PortElement.getChildNodes();
                  this.TracePath = PortChildList.item(0).getNodeValue();
               }
            }
         } else {
            this.isRequestTimeout = true;
            this.WriteToLogs("TCP_Comm", "ACUConfig does not exist.");
            System.out.println("ACUConfig does not exist.");
         }
      } catch (Exception var21) {
         this.isRequestTimeout = true;
         this.WriteToLogs("TCP_Comm", "Exception occured in ReadACU : " + var21.toString());
         System.out.println("Exception occured in ReadACU : " + var21.toString());
      }

   }

   public byte[] getDataFromCBS(String TransType, byte[] bytesToServer) {
      int ConnectCount = 0;
      boolean DataAvailable = true;
      int DataAvailableCount = 0;
      byte[] bytesFromServer = new byte[10025];
      this.socket = new Socket();

      try {
         try {
            this.getconfigDetails(TransType);
            this.WriteToLogs("TCP_Comm", "Entered into SendReceive Method");
            this.WriteToLogs("TCP_Comm", "Server IP : " + this.strIpAddress);
            this.WriteToLogs("TCP_Comm", "Server Port : " + this.intPort);
            this.WriteToLogs("TCP_Comm", "Server Timeout : " + this.intServertimeout);
            this.WriteToLogs("TCP_Comm", "Tracepath : " + this.TracePath);
            System.out.println("Tracepath : " + this.TracePath);

            while(!this.socket.isConnected()) {
               try {
                  if (ConnectCount >= 5) {
                     this.WriteToLogs("TCP_Comm", "Connection Failed after 5 attempts.");
                     this.isRequestTimeout = true;
                     break;
                  }

                  ++ConnectCount;
                  if (ConnectCount != 1) {
                     this.socket = new Socket();
                  }

                  this.socket = new Socket(this.strIpAddress, this.intPort);
                  this.socket.setSoTimeout(this.intServertimeout * 1000);
                  this.WriteToLogs("TCP_Comm", "Connected to host successfully. IP_Address : " + this.strIpAddress + " Port : " + this.intPort);
                  this.WriteToLogs("TCP_Comm", "Local Endpoint IP_Address : " + this.socket.getLocalAddress().toString());
                  this.WriteToLogs("TCP_Comm", "Local Endpoint Port : " + this.socket.getLocalPort());
               } catch (Exception var17) {
                  this.isRequestTimeout = true;
                  this.WriteToLogs("TCP_Comm", "Exception occured in connect : " + var17.toString());
               }
            }

            if (this.socket.isConnected()) {
               DataOutputStream toServer = new DataOutputStream(this.socket.getOutputStream());
               DataInputStream fromServer = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
               toServer.write(bytesToServer);

               int length;
               while(DataAvailable) {
                  try {
                     if (DataAvailableCount >= 30) {
                        this.WriteToLogs("TCP_Comm", "Data is not available after 5 attempts..");
                        this.isRequestTimeout = true;
                        break;
                     }

                     Thread.sleep(500L);
                     ++DataAvailableCount;
                     length = fromServer.available();
                     if (length > 0) {
                        System.out.println("Total Data Available : " + length);
                        this.WriteToLogs("TCP_Comm", "Total Data Available : " + length);
                        System.out.println("Data available in " + DataAvailableCount + " attempt");
                        this.WriteToLogs("TCP_Comm", "Data available in " + DataAvailableCount + " attempt");
                        bytesFromServer = new byte[length];
                        DataAvailable = false;
                        break;
                     }
                  } catch (Exception var16) {
                     this.isRequestTimeout = true;
                     this.WriteToLogs("TCP_Comm", "Exception occured in Data Available : " + var16.toString());
                  }
               }

               do {
                  length = fromServer.read(bytesFromServer, 0, bytesFromServer.length);
                  Thread.sleep(1L);
               } while(bytesFromServer.length == fromServer.available());

               String DataToCheckForEmpty = (new String(bytesFromServer)).trim();
               if (DataToCheckForEmpty.equals("")) {
                  this.WriteToLogs("TCP_Comm", "Null Response from terminal");
                  this.isRequestTimeout = true;
               }

               this.WriteToLogs("TCP_Comm", "Data received from server of length : " + String.valueOf(length));
               toServer.close();
               fromServer.close();
               this.socket.close();
               this.WriteToLogs("TCP_Comm", "Connection Closed Properly");
            } else {
               this.WriteToLogs("TCP_Comm", "Else condition for isconnect");
               this.isRequestTimeout = true;
            }
         } catch (Exception var18) {
            this.isRequestTimeout = true;
            this.WriteToLogs("TCP_Comm", "Exception in SendReceive : " + var18.toString());
            System.out.println("Exception in SendReceive : " + var18.toString());
         }

         return bytesFromServer;
      } finally {
         ;
      }
   }
   
    
    public static boolean WriteToLogs(final String pathname, final String Data) {
        try {
            final String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            final String filename = pathname + date + ".txt";
            final File f = new File(pathname);
            final File parentpath = f.getParentFile();
//            final File fDirectory = new File(TS_Tracer.tracePath);
//            if (!fDirectory.exists() || !fDirectory.isDirectory()) {
//                fDirectory.mkdir();
//            }
//            if (!parentpath.exists()) {
//                return false;
//            }
            final File file = new File(filename);
            file.createNewFile();
            final FileWriter fw = new FileWriter(file, true);
            final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final Date now = new Date();
            final String strDate = sdfDate.format(now);
            final BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\n" + strDate + "\t" + Data);
            bw.close();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    

//   public void WriteToLogs(String Name, String string) {
//      DateFormat dataformat = new SimpleDateFormat("ddMMyy");
//      DateFormat timeformat = new SimpleDateFormat("dd/MM/yy hh:mm:sss a");
//      Date date = new Date();
//      String s = timeformat.format(date) + "    " + Name + " : " + string + "\r\n";
//      byte[] data = s.getBytes();
//      File traceFilePath = new File(this.TracePath);
//      if (!traceFilePath.exists()) {
//         traceFilePath.mkdir();
//      }
//
//      Path p = Paths.get(this.TracePath + "\\Trace File_" + dataformat.format(date) + ".txt");
//
//      try {
//         OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, StandardOpenOption.CREATE, StandardOpenOption.APPEND));
//         Throwable var11 = null;
//
//         try {
//            out.write(data, 0, data.length);
//         } catch (Throwable var21) {
//            var11 = var21;
//            throw var21;
//         } finally {
//            if (out != null) {
//               if (var11 != null) {
//                  try {
//                     out.close();
//                  } catch (Throwable var20) {
//                     var11.addSuppressed(var20);
//                  }
//               } else {
//                  out.close();
//               }
//            }
//
//         }
//      } catch (Exception var23) {
//         this.isRequestTimeout = true;
//         System.out.println("Exception occured in WriteToTrace : " + var23.toString());
//      }
//
//   }
//

}
