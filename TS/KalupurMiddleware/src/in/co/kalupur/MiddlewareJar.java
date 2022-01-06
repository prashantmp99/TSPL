package in.co.kalupur;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;
import org.xml.sax.InputSource;
import java.net.HttpURLConnection;
import java.net.URL;
public class MiddlewareJar {

    private String strXmlPath = "/home/TS/microbanker/config/Bitmap.xml";
    private String strRequestXmlPath = "/home/TS/microbanker/transactions/Transaction_Request.xml";
    private String strResponse = "Successful";
    private String strServerResponse = "";
    private String strModuleName = "ISO8583";
    private String strTracePath = "";
    private CommunicationToCBS objCommunicationToCBS = new CommunicationToCBS();
    private String strBitmapHex = "True";
    File strResponseXmlPath = new File("/home/TS/microbanker/transactions/Transaction_Response.xml");

    public static void main(String[] args) {
        MiddlewareJar objMiddlewareJar = new MiddlewareJar();
        objMiddlewareJar.TransactionRequestMsgToCBS("PassbookPrinting_BLR");
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

    private String CreateRequestXmlToSend(String requestUUID, String AccountNo, String TranID, String partTranNo, String TranDt) {
        String reqXmlDataToSend = "";
        reqXmlDataToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<FIXML xsi:schemaLocation=\"http://www.finacle.com/fixml executeFinacleScript.xsd\" \n"
                + "xmlns=\"http://www.finacle.com/fixml\" xmlns:xsi=\"https://www.w3.org/2001/XMLSchema-instance\"><Header>\n"
                + "<RequestHeader>\n"
                + "<MessageKey>\n"
                + "<RequestUUID>" + requestUUID + "</RequestUUID>\n"
                + "<ServiceRequestId>executeFinacleScript</ServiceRequestId>\n"
                + "<ServiceRequestVersion>10.2</ServiceRequestVersion>\n"
                + "<ChannelId>COR</ChannelId>\n"
                + "<LanguageId></LanguageId>\n"
                + "</MessageKey>\n"
                + "<RequestMessageInfo>\n"
                + "<BankId>KCCB</BankId>\n"
                + "<TimeZone></TimeZone>\n"
                + "<EntityId></EntityId>\n"
                + "<EntityType></EntityType>\n"
                + "<ArmCorrelationId></ArmCorrelationId>\n"
                + "<MessageDateTime>2018-09-09T13:42:30.612</MessageDateTime>\n"
                + "</RequestMessageInfo>\n"
                + "<Security>\n"
                + "<Token>\n"
                + "<PasswordToken>\n"
                + "<UserId></UserId>\n"
                + "<Password></Password>\n"
                + "</PasswordToken>\n"
                + "</Token>\n"
                + "<FICertToken></FICertToken>\n"
                + "<RealUserLoginSessionId></RealUserLoginSessionId>\n"
                + "<RealUser></RealUser>\n"
                + "<RealUserPwd></RealUserPwd>\n"
                + "<SSOTransferToken></SSOTransferToken>\n"
                + "</Security>\n"
                + "</RequestHeader>\n"
                + "</Header>\n"
                + "<Body>\n"
                + "	<executeFinacleScriptRequest>\n"
                + "		<ExecuteFinacleScriptInputVO>\n"
                + "			<requestId>pdfacno.scr</requestId>\n"
                + //pdfacno.scr
                "		</ExecuteFinacleScriptInputVO>\n"
                + "		<executeFinacleScript_CustomData>\n"
                + "			<Accnt>" + AccountNo + "</Accnt>\n"
                + //01310103048   //changes by pratik @04032020//removed trim()
                "			<tranid>" + TranID + "</tranid>\n"
                + //S726864       //changes by pratik //removed trim()
                "			<parttranno>" + partTranNo + "</parttranno>\n"
                + //14
                "			<trandt>" + TranDt + "</trandt>\n"
                + //21-04-2018  //new node added 14022020
                "		</executeFinacleScript_CustomData>\n"
                + "	</executeFinacleScriptRequest>\n"
                + "</Body>\n"
                + "</FIXML>";

        return reqXmlDataToSend;
    }

    public String customApiRequest(String requestID, String AccountNo, String TranID, String partTranNo, String TranDt) {
        String requestString = "", responseString = "", outputString = "";
        try {

            this.objCommunicationToCBS.getconfigDetails("");
            this.strTracePath = this.objCommunicationToCBS.TracePath;
            //ReadACU("");
            //ReadApiConfig();
            // TracePath = TracePath;
            this.WriteToLogs(this.strTracePath, "Entered into customApiRequest Method.");
            this.WriteToLogs(this.strTracePath, "Request ID : " + requestID);
            this.WriteToLogs(this.strTracePath, "Account No : " + AccountNo);
            this.WriteToLogs(this.strTracePath, "Tran ID : " + TranID);
            this.WriteToLogs(this.strTracePath, "partTranNo : " + partTranNo);
            this.WriteToLogs(this.strTracePath, "Trandt : " + TranDt);
            requestString = CreateRequestXmlToSend(requestID, AccountNo, TranID, partTranNo, TranDt);
            this.WriteToLogs(this.strTracePath, "Request String : " + requestString);
            URL objURL = new URL(this.objCommunicationToCBS.strSslUrl);
            this.WriteToLogs(this.strTracePath, "URL : " + this.objCommunicationToCBS.strSslUrl);
            this.WriteToLogs(this.strTracePath, "Url Timeout : " + this.objCommunicationToCBS.strSsltimeOut);

            allowAllSSL();

            HttpURLConnection postConnection = (HttpURLConnection) objURL.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setReadTimeout(Integer.parseInt( this.objCommunicationToCBS.strSsltimeOut));
            // WriteToTrace("Timeout : " + ServiceTimeout);
            postConnection.setRequestProperty("Content-Type", "application/xml");
            postConnection.setDoOutput(true);
            OutputStream os = postConnection.getOutputStream();
            os.write(requestString.getBytes());
            os.flush();
            os.close();
            int responseCode = postConnection.getResponseCode();
            this.WriteToLogs(this.strTracePath, "POST Response Code :  " + Integer.toString(responseCode));
            System.out.println("POST Response Code :  " + responseCode);
        this.WriteToLogs(this.strTracePath,"POST Response Message : " + postConnection.getResponseMessage());
            System.out.println("POST Response Message : " + postConnection.getResponseMessage());
            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        postConnection.getInputStream()));
                String inputLine;
                StringBuffer bufferResponse = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    bufferResponse.append(inputLine);
                }
                in.close();
                responseString = bufferResponse.toString().replace("\"", "");
                this.WriteToLogs(this.strTracePath, "Response xml : " + responseString);
                responseString = getDataFromResponseXml(responseString);
                this.WriteToLogs(this.strTracePath, "Response Message from xml : " + responseString);
            }

        } catch (Exception ex) {
            responseString = "Unsuccessful";
            this.WriteToLogs(this.strTracePath, "Exception occured in customApi : " + ex.toString());
        }
        this.WriteToLogs(this.strTracePath, "Exit from customApiRequest Method.");
        return responseString;
    }

    private String getDataFromResponseXml(String xmlData) {
        String response = "";
        String status = "", desig = "";
        String ErrorCode = "", ErrorDesc = "", ErrorSource = "", ErrorType = "";
        DOMParser parser = new DOMParser();
        try {
            int indexOfBodyStart = xmlData.indexOf("<Body>");
            int indexOfBodyEnd = xmlData.indexOf("</FIXML>");
            xmlData = xmlData.substring(indexOfBodyStart, indexOfBodyEnd);
            parser.parse(new InputSource(new java.io.StringReader(xmlData)));
            Document doc = parser.getDocument();
            doc.getDocumentElement().normalize();
            NodeList nodeDataList = doc.getElementsByTagName("executeFinacleScript_CustomData");
            Node nodeData = nodeDataList.item(0);
            Element dataElement = (Element) nodeData;
            if (nodeData.getNodeType() == Node.ELEMENT_NODE) {
                NodeList statusList = dataElement.getElementsByTagName("successOrFailure");
                Element statusElement = (Element) statusList.item(0);
                NodeList statusChildList = statusElement.getChildNodes();
                status = ((Node) statusChildList.item(0)).getNodeValue();
                NodeList desigList = dataElement.getElementsByTagName("desig");
                Element desigElement = (Element) desigList.item(0);
                NodeList desigChildList = desigElement.getChildNodes();
                //desig = ((Node)desigChildList.item(0)).getNodeValue();
                desig = ((desigElement.getChildNodes().getLength() > 0) ? desigElement.getChildNodes().item(0).getNodeValue() : "");
            }
            response = "Successful|" + status + "|" + desig;

            this.WriteToLogs(this.strTracePath, "Response : " + response);
        } catch (Exception ex) {
            this.WriteToLogs(this.strTracePath, "Exception occured in getResposneFromXml : " + ex.toString());
            try {
//            int indexOfBodyStart=xmlData.indexOf("<Body>");
//            int indexOfBodyEnd = xmlData.indexOf("</FIXML>");
//            xmlData=xmlData.substring(indexOfBodyStart, indexOfBodyEnd);
                parser.parse(new InputSource(new java.io.StringReader(xmlData)));
                Document doc = parser.getDocument();
                doc.getDocumentElement().normalize();
                NodeList nodeDataList = doc.getElementsByTagName("ErrorDetail");
                Node nodeData = nodeDataList.item(0);
                Element dataElement = (Element) nodeData;
                if (nodeData.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList ErrorCodeList = dataElement.getElementsByTagName("ErrorCode");
                    Element ErrorCodeElement = (Element) ErrorCodeList.item(0);
                    NodeList ErrorCodeChildList = ErrorCodeElement.getChildNodes();
                    ErrorCode = ((ErrorCodeElement.getChildNodes().getLength() > 0) ? ErrorCodeElement.getChildNodes().item(0).getNodeValue() : "");

                    NodeList ErrorDescList = dataElement.getElementsByTagName("ErrorDesc");
                    Element ErrorDescElement = (Element) ErrorDescList.item(0);
                    ErrorDesc = ((ErrorDescElement.getChildNodes().getLength() > 0) ? ErrorDescElement.getChildNodes().item(0).getNodeValue() : "");

                    NodeList ErrorSourceList = dataElement.getElementsByTagName("ErrorSource");
                    Element ErrorSourceElement = (Element) ErrorSourceList.item(0);
                    ErrorSource = ((ErrorSourceElement.getChildNodes().getLength() > 0) ? ErrorSourceElement.getChildNodes().item(0).getNodeValue() : "");

                    NodeList ErrorTypeList = dataElement.getElementsByTagName("ErrorType");
                    Element ErrorTypeElement = (Element) ErrorTypeList.item(0);
                    ErrorType = ((ErrorTypeElement.getChildNodes().getLength() > 0) ? ErrorTypeElement.getChildNodes().item(0).getNodeValue() : "");
                }
                response = "Unsuccessful|" + ErrorCode + "|" + ErrorDesc + "|" + ErrorSource + "|" + ErrorType;
                this.WriteToLogs(this.strTracePath, "Response : " + response);
                //ex.toString();
            } catch (Exception e) {
                response = "Unsuccessful|";
                this.WriteToLogs(this.strTracePath, "Exception occured in getResposneFromXml : " + e.toString());
            }

        }
        return response;
    }

    public String TransactionRequestMsgToCBS(String strTransTypeFromApp) {
        byte[] bytesToServerCBS = new byte[10025];
        byte[] bytesFromServerCBS = new byte[10025];

        try {
            this.objCommunicationToCBS.getconfigDetails(strTransTypeFromApp);
            this.strTracePath = this.objCommunicationToCBS.TracePath;
            this.strXmlPath = this.objCommunicationToCBS.Bitmap_path;
            this.strRequestXmlPath = this.objCommunicationToCBS.Transaction_Request_Path;
            String strNewResponseXmlPath = this.objCommunicationToCBS.Transaction_Response_Path;
            this.strModuleName = this.strTracePath;
            File strResponseXmlPath = new File(strNewResponseXmlPath);
            this.WriteToLogs(this.strModuleName, "Entered into TransactionRequestMsg method");
            System.out.println("Transaction Type : " + strTransTypeFromApp);
            byte[] arrMTI = this.CreateMTI(strTransTypeFromApp).getBytes();
            this.WriteToLogs(this.strModuleName, "arrMTI : " + new String(arrMTI, "UTF-8"));
            if (this.strResponse.equals("Successful")) {
                byte[] arrBITMAP;
                if (this.strBitmapHex.equals("True")) {
                    arrBITMAP = this.HexToAscii(this.CreateBitmap(strTransTypeFromApp));
                } else {
                    arrBITMAP = this.CreateBitmap(strTransTypeFromApp).getBytes();
                }

                if (this.strResponse.equals("Successful")) {
                    byte[] arrDATA = this.CreateData(strTransTypeFromApp).getBytes();
                    if (this.strResponse.equals("Successful")) {
                        File BitmapConfig = new File(this.strXmlPath);
                        if (BitmapConfig.exists()) {
                            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                            DocumentBuilder db = dbf.newDocumentBuilder();
                            Document doc = db.parse(BitmapConfig);
                            doc.getDocumentElement().normalize();
                            NodeList MFKList = doc.getElementsByTagName("MFK");
                            Node firstMFK = MFKList.item(0);
                            if (firstMFK.getNodeType() == 1) {
                                Element firstMFKElement = (Element) firstMFK;
                                NodeList HeaderList = firstMFKElement.getElementsByTagName("HEADER");
                                Node firstHeader = HeaderList.item(0);
                                String bitPresent = firstHeader.getAttributes().getNamedItem("Present").getNodeValue();
                                ByteBuffer bytebuffer;
                                if (bitPresent.equals("True")) {
                                    String lengthPresent = firstHeader.getAttributes().getNamedItem("Length").getNodeValue();
                                    String additionalHeaderPresent = firstHeader.getAttributes().getNamedItem("Additional_header").getNodeValue();
                                    String additionalHeaderValue = firstHeader.getAttributes().getNamedItem("Value").getNodeValue();
                                    byte[] arrAdditionalData = additionalHeaderValue.getBytes();
                                    if (lengthPresent.equals("True")) {
                                        byte[] arrDATA_LENGTH;
                                        if (additionalHeaderPresent.equals("True")) {
                                            arrDATA_LENGTH = this.padLeft(String.valueOf(arrAdditionalData.length + arrMTI.length + arrBITMAP.length + arrDATA.length), 4).getBytes();
                                            bytebuffer = ByteBuffer.allocate(arrDATA_LENGTH.length + arrAdditionalData.length + arrMTI.length + arrBITMAP.length + arrDATA.length);
                                            bytebuffer.put(arrDATA_LENGTH);
                                            bytebuffer.put(arrAdditionalData);
                                            bytebuffer.put(arrMTI);
                                            bytebuffer.put(arrBITMAP);
                                            bytebuffer.put(arrDATA);
                                            bytesToServerCBS = new byte[bytebuffer.array().length];
                                            bytesToServerCBS = bytebuffer.array();
                                        } else {
                                            arrDATA_LENGTH = this.padLeft(String.valueOf(arrMTI.length + arrBITMAP.length + arrDATA.length), 4).getBytes();
                                            bytebuffer = ByteBuffer.allocate(arrDATA_LENGTH.length + arrMTI.length + arrBITMAP.length + arrDATA.length);
                                            bytebuffer.put(arrDATA_LENGTH);
                                            bytebuffer.put(arrMTI);
                                            bytebuffer.put(arrBITMAP);
                                            bytebuffer.put(arrDATA);
                                            bytesToServerCBS = new byte[bytebuffer.array().length];
                                            bytesToServerCBS = bytebuffer.array();
                                        }
                                    } else if (additionalHeaderPresent.equals("True")) {
                                        bytebuffer = ByteBuffer.allocate(arrAdditionalData.length + arrMTI.length + arrBITMAP.length + arrDATA.length);
                                        bytebuffer.put(arrAdditionalData);
                                        bytebuffer.put(arrMTI);
                                        bytebuffer.put(arrBITMAP);
                                        bytebuffer.put(arrDATA);
                                        bytesToServerCBS = new byte[bytebuffer.array().length];
                                        bytesToServerCBS = bytebuffer.array();
                                    }
                                } else {
                                    bytebuffer = ByteBuffer.allocate(arrMTI.length + arrBITMAP.length + arrDATA.length);
                                    bytebuffer.put(arrMTI);
                                    bytebuffer.put(arrBITMAP);
                                    bytebuffer.put(arrDATA);
                                    int a = bytebuffer.array().length;
                                    this.WriteToLogs(this.strModuleName, "ByteBuffer length : " + String.valueOf(a));
                                    bytesToServerCBS = new byte[bytebuffer.array().length];
                                    bytesToServerCBS = bytebuffer.array();
                                }
                            }
                        } else {
                            this.strResponse = "Unsuccessful";
                            this.WriteToLogs(this.strModuleName, "Bitmap config does not exist");
                            System.out.println("Bitmap config does not exist");
                        }

                        if (this.strResponse.equals("Successful")) {
                            bytesFromServerCBS = this.objCommunicationToCBS.getDataFromCBS(strTransTypeFromApp, bytesToServerCBS);
                        } else {
                            this.strResponse = "Unsuccessful";
                        }

                        if (this.objCommunicationToCBS.isRequestTimeout) {
                            this.strResponse = "Unsuccessful";
                            this.WriteToLogs(this.strModuleName, "Server Request Timeout");
                        } else {
                            if (this.strBitmapHex.equals("True")) {
                                this.strServerResponse = this.ByteArrayToHex(bytesFromServerCBS).trim();
                            } else {
                                this.strServerResponse = (new String(bytesFromServerCBS)).trim();
                            }

                            this.strResponse = this.TransactionResponseFromCBS(strTransTypeFromApp, this.strServerResponse);
                        }
                    }
                }
            }
        } catch (Exception var25) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in TransactionRequestMsg : " + var25.toString());
            System.out.println("Exception occured in TransactionRequestMsg : " + var25.toString());
        }

        return this.strResponse;
    }

    private String CreateMTI(String TransType) {
        String strMTI = "";

        try {
            File fileBitmapConfig = new File(this.strXmlPath);
            if (fileBitmapConfig.exists()) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(fileBitmapConfig);
                doc.getDocumentElement().normalize();
                NodeList MFKList = doc.getElementsByTagName("MFK");
                Node firstMFK = MFKList.item(0);
                if (firstMFK.getNodeType() == 1) {
                    Element firstMFKElement = (Element) firstMFK;
                    NodeList MTIList = firstMFKElement.getElementsByTagName("MTI");
                    Node firstMTI = MTIList.item(0);
                    if (firstMTI.getNodeType() == 1) {
                        Element firstMTIElement = (Element) firstMTI;
                        NodeList TransList = firstMTIElement.getElementsByTagName(TransType);
                        Element firstTransElement = (Element) TransList.item(0);
                        NodeList ResList = firstTransElement.getChildNodes();
                        strMTI = ResList.item(0).getNodeValue().trim();
                        this.WriteToLogs(this.strModuleName, "Request MIT : " + ResList.item(0).getNodeValue().trim());
                        System.out.println("Request MIT : " + ResList.item(0).getNodeValue().trim());
                    }
                }
            } else {
                this.strResponse = "Unsuccessful";
                this.WriteToLogs(this.strModuleName, "Bitmap config does not exist");
                System.out.println("Bitmap config does not exist");
            }
        } catch (Exception var16) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in CreateMTI : " + var16.toString());
            System.out.println("Exception occured in CreateMTI : " + var16.toString());
        }

        return strMTI;
    }

    private String CreateBitmap(String TransType) {
        String strBitmap = "";
        String[] strarrBitPresent = new String[128];
        String[] strarrBit = new String[32];
        int intCount = 0;

        try {
            File fileBitmapConfig = new File(this.strXmlPath);
            if (fileBitmapConfig.exists()) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(fileBitmapConfig);
                doc.getDocumentElement().normalize();
                NodeList MFKList = doc.getElementsByTagName("MFK");
                Node firstMFK = MFKList.item(0);
                if (firstMFK.getNodeType() == 1) {
                    Element firstMFKElement = (Element) firstMFK;
                    NodeList MTIList = firstMFKElement.getElementsByTagName("REQUEST");
                    Node firstMTI = MTIList.item(0);
                    if (firstMTI.getNodeType() == 1) {
                        Element firstMTIElement = (Element) firstMTI;
                        NodeList TransList = firstMTIElement.getElementsByTagName(TransType);
                        Node firstTrans = TransList.item(0);
                        if (firstTrans.getNodeType() == 1) {
                            Element firstTransElement = (Element) firstTrans;

                            int i;
                            for (i = 1; i < 129; ++i) {
                                NodeList BitList = firstTransElement.getElementsByTagName("Bit_" + i);
                                Node currentBit = BitList.item(0);
                                String bitPresent = currentBit.getAttributes().getNamedItem("Present").getNodeValue();
                                if (bitPresent.equals("True")) {
                                    strarrBitPresent[i - 1] = "1";
                                } else if (bitPresent.equals("False")) {
                                    strarrBitPresent[i - 1] = "0";
                                }
                            }

                            for (i = 0; i < 32; ++i) {
                                strarrBit[i] = "";
                            }

                            int j;
                            if (strarrBitPresent[0].equals("1")) {
                                for (i = 0; i < 128; ++i) {
                                    for (j = 0; j < 4; ++j) {
                                        strarrBit[intCount] = strarrBit[intCount] + strarrBitPresent[i];
                                        ++i;
                                    }

                                    --i;
                                    ++intCount;
                                }
                            } else if (strarrBitPresent[0].equals("0")) {
                                for (i = 0; i < 64; ++i) {
                                    for (j = 0; j < 4; ++j) {
                                        strarrBit[intCount] = strarrBit[intCount] + strarrBitPresent[i];
                                        ++i;
                                    }

                                    --i;
                                    ++intCount;
                                }
                            }

                            for (i = 0; i < strarrBit.length; ++i) {
                                strBitmap = strBitmap + this.RequestDicBitmap(strarrBit[i]);
                            }

                            this.WriteToLogs(this.strModuleName, "Request Bitmap : " + strBitmap);
                            System.out.println("Request Bitmap : " + strBitmap);
                        }
                    }
                }
            } else {
                this.strResponse = "Unsuccessful";
                this.WriteToLogs(this.strModuleName, "Bitmap config does not exist");
                System.out.println("Bitmap config does not exist");
            }
        } catch (Exception var23) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in CreateBitmap : " + var23.toString());
            System.out.println("Exception occured in CreateBitmap : " + var23.toString());
        }

        return strBitmap;
    }

    private String RequestDicBitmap(String str) {
        Hashtable objDic = new Hashtable();

        try {
            objDic.put("0000", "0");
            objDic.put("0001", "1");
            objDic.put("0010", "2");
            objDic.put("0011", "3");
            objDic.put("0100", "4");
            objDic.put("0101", "5");
            objDic.put("0110", "6");
            objDic.put("0111", "7");
            objDic.put("1000", "8");
            objDic.put("1001", "9");
            objDic.put("1010", "A");
            objDic.put("1011", "B");
            objDic.put("1100", "C");
            objDic.put("1101", "D");
            objDic.put("1110", "E");
            objDic.put("1111", "F");
        } catch (Exception var4) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in DicBitmap : " + var4.toString());
            System.out.println("Exception occured in DicBitmap : " + var4.toString());
        }

        return objDic.get(str).toString();
    }

    private String CreateData(String TransType) {
        String strData = "";
        String strBitValue = "";
        this.WriteToLogs(this.strModuleName, "**********Transaction Request Message**********");

        try {
            File fileBitmapConfig = new File(this.strXmlPath);
            if (fileBitmapConfig.exists()) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(fileBitmapConfig);
                doc.getDocumentElement().normalize();
                NodeList MFKList = doc.getElementsByTagName("MFK");
                Node firstMFK = MFKList.item(0);
                if (firstMFK.getNodeType() == 1) {
                    Element firstMFKElement = (Element) firstMFK;
                    NodeList MTIList = firstMFKElement.getElementsByTagName("REQUEST");
                    Node firstMTI = MTIList.item(0);
                    if (firstMTI.getNodeType() == 1) {
                        Element firstMTIElement = (Element) firstMTI;
                        NodeList TransList = firstMTIElement.getElementsByTagName(TransType);
                        Node firstTrans = TransList.item(0);
                        if (firstTrans.getNodeType() == 1) {
                            Element firstTransElement = (Element) firstTrans;
                            File RequestXml = new File(this.strRequestXmlPath);
                            if (RequestXml.exists()) {
                                Document doc1 = db.parse(RequestXml);
                                doc1.getDocumentElement().normalize();
                                NodeList RMFList = doc1.getElementsByTagName("REQUEST_MESSAGE_FIELDS");
                                Node firstRMF = RMFList.item(0);

                                for (int i = 2; i < 129; ++i) {
                                    NodeList BitList = firstTransElement.getElementsByTagName("Bit_" + i);
                                    Node currentBit = BitList.item(0);
                                    String bitPresent = currentBit.getAttributes().getNamedItem("Present").getNodeValue();
                                    if (bitPresent.equals("True")) {
                                        String dic = currentBit.getAttributes().getNamedItem("Dic").getNodeValue();
                                        String[] dicValue = dic.split(",");
                                        strBitValue = currentBit.getAttributes().getNamedItem("Value").getNodeValue();
                                        if (firstMFK.getNodeType() == 1) {
                                            Element firstRMFElement = (Element) firstRMF;
                                            NodeList FieldList = firstRMFElement.getElementsByTagName(strBitValue);
                                            Element FieldListElement = (Element) FieldList.item(0);
                                            NodeList nodelist = FieldListElement.getChildNodes();
                                            String firstFieldValue = nodelist.item(0).getNodeValue();
                                            this.WriteToLogs(this.strModuleName, "Field_" + i + " : " + firstFieldValue);
                                            if (dicValue[0].equals("F")) {
                                                strData = strData + firstFieldValue;
                                            } else {
                                                String length;
                                                if (dicValue[0].equals("V") && String.valueOf(dicValue[1].length()).equals("2")) {
                                                    length = this.padLeft(String.valueOf(firstFieldValue.length()), 2);
                                                    strData = strData + length + firstFieldValue;
                                                } else if (dicValue[0].equals("V") && String.valueOf(dicValue[1].length()).equals("3")) {
                                                    length = this.padLeft(String.valueOf(firstFieldValue.length()), 3);
                                                    strData = strData + length + firstFieldValue;
                                                }
                                            }
                                        }
                                    }
                                }

                                this.WriteToLogs(this.strModuleName, "Request Data : " + strData);
                                System.out.println("Request Data : " + strData);
                            } else {
                                this.strResponse = "Unsuccessful";
                                this.WriteToLogs(this.strModuleName, "Request config does not exist");
                                System.out.println("Request config does not exist");
                            }
                        }
                    }
                }
            } else {
                this.strResponse = "Unsuccessful";
                this.WriteToLogs(this.strModuleName, "Bitmap config does not exist");
                System.out.println("Bitmap config does not exist");
            }
        } catch (Exception var33) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in CreateData : " + var33.toString());
            System.out.println("Exception occured in CreateData : " + var33.toString());
        }

        return strData;
    }

    private String padLeft(String s, int n) {
        return String.format("%0$" + n + "s", s).replace(' ', '0');
    }

    private String TransactionResponseFromCBS(String TransType, String ResponseData) {
        String strResponse = "";
        String strResLength = "";
        String strResMTI = "";
        String strResBitmap = "";
        String strResData = "";
        String strBit1 = "";
        String[] strRespBit = new String[129];
        String strDicBitmap = "";
        String[] strDic = new String[129];
        String[] strBitValue = new String[129];
        String[] strDataValue = new String[129];
        int index = 0;

        try {
            this.WriteToLogs(this.strModuleName, "Entered into TransactionResponse method");
            strResponse = "31323130" + ResponseData;
            int BitmapLength;
            if (this.strBitmapHex.equals("True")) {
                this.WriteToLogs(this.strModuleName, "Entered into IF codition");
                strResLength = strResponse.substring(0, 8);
                strResLength = this.HexToPlain(strResLength);
                strResMTI = strResponse.substring(8, 16);
                strResMTI = this.HexToPlain(strResMTI);
                strBit1 = strResponse.substring(16, 17);
                strBit1 = this.ResponseDicBitmap(strBit1.toUpperCase());
                if (strBit1.substring(0, 1).equals("1")) {
                    strResBitmap = strResponse.substring(16, 48);
                } else {
                    strResBitmap = strResponse.substring(16, 32);
                }

                BitmapLength = strResBitmap.length();
                strResData = strResponse.substring(16 + BitmapLength, strResponse.length());
                strResData = this.HexToPlain(strResData);
            } else {
                strResMTI = strResponse.substring(0, 4);
                strBit1 = strResponse.substring(4, 5);
                strBit1 = this.ResponseDicBitmap(strBit1.toUpperCase());
                if (strBit1.substring(0, 1).equals("1")) {
                    strResBitmap = strResponse.substring(4, 36);
                } else {
                    strResBitmap = strResponse.substring(4, 20);
                }

                BitmapLength = strResBitmap.length();
                strResData = strResponse.substring(4 + BitmapLength, strResponse.length());
            }

            if (BitmapLength == 32 && strResBitmap.equals("00000000000000000000000000000000") || BitmapLength == 16 && strResBitmap.equals("0000000000000000")) {
                this.WriteToLogs(this.strModuleName, "Empty Response to termial. Bitmap value is : " + strResBitmap);
                this.strResponse = "Unsuccessful";
                return this.strResponse;
            }

            this.WriteToLogs(this.strModuleName, "Response Data Length : " + strResLength);
            System.out.println("Response Data Length : " + strResLength);
            this.WriteToLogs(this.strModuleName, "Response MTI : " + strResMTI);
            System.out.println("Response MTI : " + strResMTI);
            this.WriteToLogs(this.strModuleName, "Response Bitmap : " + strResBitmap);
            System.out.println("Response Bitmap : " + strResBitmap);
            this.WriteToLogs(this.strModuleName, "Response Data : " + strResData);
            System.out.println("Response Data : " + strResData);

            //int ii;
            for (int i = 0; i < strResBitmap.length(); ++i) {
                strDicBitmap = strDicBitmap + this.ResponseDicBitmap(strResBitmap.substring(i, i + 1).toUpperCase());
            }

            for (int i = 0; i < 129; ++i) {
                strRespBit[i] = "";
                strDic[i] = "";
                strBitValue[i] = "";
            }

            for (int i = 1; i < strDicBitmap.length() + 1; ++i) {
                strRespBit[i] = strDicBitmap.substring(i - 1, i);
            }

            File BitmapConfig = new File(this.strXmlPath);
            if (BitmapConfig.exists()) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(BitmapConfig);
                doc.getDocumentElement().normalize();
                NodeList MFKList = doc.getElementsByTagName("MFK");
                Node firstMFK = MFKList.item(0);
                if (firstMFK.getNodeType() == 1) {
                    Element firstMFKElement = (Element) firstMFK;
                    NodeList MTIList = firstMFKElement.getElementsByTagName("RESPONSE");
                    Node firstMTI = MTIList.item(0);
                    if (firstMTI.getNodeType() == 1) {
                        Element firstMTIElement = (Element) firstMTI;
                        NodeList TransList = firstMTIElement.getElementsByTagName(TransType);
                        Node firstTrans = TransList.item(0);
                        if (firstTrans.getNodeType() == 1) {
                            Element firstTransElement = (Element) firstTrans;

                            for (int i = 2; i < 129; ++i) {
                                if (strRespBit[i].equals("1")) {
                                    NodeList BitList = firstTransElement.getElementsByTagName("Bit_" + i);
                                    Node currentBit = BitList.item(0);
                                    strBitValue[i] = currentBit.getAttributes().getNamedItem("Value").getNodeValue();
                                    strDic[i] = currentBit.getAttributes().getNamedItem("Dic").getNodeValue();
                                    String[] dicValue = strDic[i].split(",");
                                    String Length = "";
                                    if (dicValue[0].equals("F")) {
                                        strDataValue[i] = strResData.substring(index, index + Integer.parseInt(dicValue[1]));
                                        index += Integer.parseInt(dicValue[1]);
                                    } else if (dicValue[0].equals("V")) {
                                        if (String.valueOf(dicValue[1].length()).equals("2")) {
                                            Length = strResData.substring(index, index + 2);
                                            index += 2;
                                            strDataValue[i] = strResData.substring(index, index + Integer.parseInt(Length));
                                            index += Integer.parseInt(Length);
                                        } else if (String.valueOf(dicValue[1].length()).equals("3")) {
                                            Length = strResData.substring(index, index + 3);
                                            index += 3;
                                            strDataValue[i] = strResData.substring(index, index + Integer.parseInt(Length));
                                            index += Integer.parseInt(Length);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                this.strResponse = "Unsuccessful";
                this.WriteToLogs(this.strModuleName, "Bitmap config does not exist");
                System.out.println("Bitmap config does not exist");
            }

            this.strResponse = this.CreateResponseXml(strRespBit, strBitValue, strDataValue);
        } catch (Exception var34) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in TransactionResponse : " + var34.toString());
            System.out.println("Exception occured in TransactionResponse : " + var34.toString());
        }

        return this.strResponse;
    }

    private String CreateResponseXml(String[] ResponseBit, String[] BitValue, String[] DataValue) {

        try {
            this.WriteToLogs(this.strModuleName, "**********Transaction Response Message**********");
            if (strResponseXmlPath.exists()) {
                strResponseXmlPath.delete();
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element RootElement = doc.createElement("RESPONSE_MESSAGE_FIELDS");
            doc.appendChild(RootElement);

            for (int i = 2; i < 129; ++i) {
                if (ResponseBit[i].equals("1")) {
                    RootElement.appendChild(doc.createTextNode("\n\t"));
                    this.WriteToLogs(this.strModuleName, "Field_" + i + " : " + DataValue[i]);
                    Element ChildElement = doc.createElement(BitValue[i]);
                    if (DataValue[i].contains("&")) {
                        DataValue[i] = DataValue[i].replace("&", "&");
                    }

                    ChildElement.appendChild(doc.createTextNode(DataValue[i]));
                    RootElement.appendChild(ChildElement);
                }
            }

            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult((strResponseXmlPath));
            tf.transform(source, result);
            System.out.println("XML file Saved");
            this.strResponse = "Successful";
        } catch (Exception var14) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occure in CreateResponseXml : " + var14.toString());
            System.out.println("Exception occure in CreateResponseXml : " + var14.toString());
        }

        return this.strResponse;
    }

    private String ResponseDicBitmap(String str) {
        Hashtable objDic = new Hashtable();

        try {
            objDic.put("0", "0000");
            objDic.put("1", "0001");
            objDic.put("2", "0010");
            objDic.put("3", "0011");
            objDic.put("4", "0100");
            objDic.put("5", "0101");
            objDic.put("6", "0110");
            objDic.put("7", "0111");
            objDic.put("8", "1000");
            objDic.put("9", "1001");
            objDic.put("A", "1010");
            objDic.put("B", "1011");
            objDic.put("C", "1100");
            objDic.put("D", "1101");
            objDic.put("E", "1110");
            objDic.put("F", "1111");
        } catch (Exception var4) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in DicBitmap : " + var4.toString());
            System.out.println("Exception occured in DicBitmap : " + var4.toString());
        }

        return objDic.get(str).toString();
    }

    public static boolean WriteToLogs(final String pathname, final String Data) {
        try {
            final String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            final String filename = pathname + date + ".txt";
            final File f = new File(pathname);
            final File parentpath = f.getParentFile();
            // final File fDirectory = new File(TS_Tracer.tracePath);
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
        } catch (Exception ex) {
            return false;
        }
    }

//   private void WriteToLogs(String Name, String string) {
//      DateFormat dataformat = new SimpleDateFormat("ddMMyy");
//      DateFormat timeformat = new SimpleDateFormat("dd/MM/yy hh:mm:sss a");
//      Date date = new Date();
//      String s = timeformat.format(date) + "    " + Name + " : " + string + "\r\n";
//      byte[] data = s.getBytes();
//      File traceFilePath = new File(this.strTracePath);
//      if (!traceFilePath.exists()) {
//         traceFilePath.mkdir();
//      }
//
//      Path p = Paths.get(this.strTracePath + "Trace File_" + dataformat.format(date) + ".txt");
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
//         this.strResponse = "Unsuccessful";
//         System.out.println("Exception occured in WriteToTrace : " + var23.toString());
//      }
//
//   }
    private String hexToASCII(String hexValue) {
        StringBuilder output = new StringBuilder("");

        try {
            for (int i = 0; i < hexValue.length(); i += 2) {
                String str = hexValue.substring(i, i + 2);
                output.append((char) Integer.parseInt(str, 16));
            }
        } catch (Exception var5) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in hexToASCII : " + var5.toString());
        }

        return output.toString();
    }

    private byte[] HexToAscii(String hexValue) {
        byte[] raw = new byte[100];

        try {
            raw = new byte[hexValue.length() / 2];

            for (int i = 0; i < raw.length; ++i) {
                int a = i * 2;
                String str = hexValue.substring(a, a + 2);
                raw[i] = (byte) Integer.parseInt(str, 16);
            }
        } catch (Exception var6) {
            this.WriteToLogs(this.strModuleName, "Exception occured in hexToASCII : " + var6.toString());
        }

        return raw;
    }

    private String ByteArrayToHex(byte[] bytesFromServer) {
        Formatter formatter = new Formatter();
        String result = "";

        try {
            byte[] var4 = bytesFromServer;
            int var5 = bytesFromServer.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                byte b = var4[var6];
                formatter.format("%02x", b);
            }

            result = formatter.toString();
        } catch (Exception var11) {
            this.WriteToLogs(this.strModuleName, "Exception occured in ByteArrayToHex : " + var11.toString());
            this.strResponse = "Unsuccessful";
        } finally {
            formatter.close();
        }

        return result;
    }

    private String HexToPlain(String Hex) {
        StringBuilder output = new StringBuilder();

        try {
            for (int i = 0; i < Hex.length(); i += 2) {
                String str = Hex.substring(i, i + 2);
                output.append((char) Integer.parseInt(str, 16));
            }
        } catch (Exception var5) {
            this.strResponse = "Unsuccessful";
            this.WriteToLogs(this.strModuleName, "Exception occured in HexToPlain : " + var5.toString());
        }

        return output.toString();
    }
}
