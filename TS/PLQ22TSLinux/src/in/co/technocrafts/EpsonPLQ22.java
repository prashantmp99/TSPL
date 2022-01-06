/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.technocrafts;

import in.co.technocrafts.Simulator;
import in.co.technocrafts.TS_Tracer;
import epson.plq20.PLQ20JNIWrapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import epson.plq20.RefValue;
import epson.plq20.ScanParam;
import epson.plq20.PLQ20APIConst;
import epson.plq20.ScanOption;

import java.io.LineNumberReader;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author prashant
 */
public class EpsonPLQ22 {

    private Simulator objPLQ22LinuxSimulator = new Simulator();
    private PLQ20JNIWrapper objwrapper = new PLQ20JNIWrapper();
    private RefValue objrefValue = new RefValue(0);
    private String strDirectionStatus;
    private ScanOption strScanOption = new ScanOption();
    private BufferedImage imgimage;
    private int intWidth;
    private int intHeight;

    private BufferedImage imgPrintimage;
    private int intPrintimagewidth;
    private int intPrintimageheight;
    private ScanParam objscanParam = new ScanParam();
    private int intStatus;
    private String strDeviceStatus1, straAutoStart, strdDeviceStatus;
    private String strLogPath;
    private String strCompleteScanPath, strDevice_Health_xml;
    public String strimageStorePath, strversion;
    private String strlower_Barcode_length, strupper_Barcode_Length, strBarcode_Length;
    private int intbarcodeLengthValue, intupperBarcodeValue, intlowerBarcodeValue;
    private String strfatalStatus = "MISC";
    private String strcheckAutoStartStatus, strscanPortion, strPassbook_Type;
    private String strpbkAlign, strPrint_File_Path, strPrint_Image_File, strImage_Print_Start_Pos;
    private int intPrint_Image_Time = 0;
    private String[] strscanArray = new String[2];
    private String strfont, strLPI, strCPI, strTrace_Path;
    private int intlpi, intcpi;
    private int intconfigState = 0;
    int intcount = 0;
    private int intflagcount = 0;
    private int intscanPortionValue;
    private int intunknownErrorCounter = 0;

    String strpathDirctory = new String("/home/TS/sp_plq22/");

    public static void main(String[] args) {

        EpsonPLQ22 plql = new EpsonPLQ22();

    }

    public EpsonPLQ22() {
        checkAutoStartJson();
        TS_Tracer.WriteToDeviceLogs(strLogPath, "PLQ22LinuxTS --->  " + strversion);
        objPLQ22LinuxSimulator = new Simulator();
        TS_Tracer.WriteToDeviceLogs(strLogPath, "Simulator Mode  " + objPLQ22LinuxSimulator.getsimulator_mode());
    }

    public String scanBarcodeEpson() {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            String strvalue = new String("");
            strvalue = objPLQ22LinuxSimulator.scanBarcode();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "ScanBarCode status ____ " + strvalue);

            return strvalue;

        }

        String nData = "";
        if (checkAutoStartJson().equalsIgnoreCase("MISC")) {
            strdDeviceStatus = "MISC";

            TS_Tracer.WriteToDeviceLogs(strLogPath, "Device Status --> " + strdDeviceStatus);
            return strdDeviceStatus;
        }
        if (!straAutoStart.equalsIgnoreCase("Start")) {
            strdDeviceStatus = "NOT_CONFIGURED";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "Device Status --> " + strdDeviceStatus);

            return strdDeviceStatus;
        } else {

            String printerStatus = devStatusEpson();
            if (printerStatus.equalsIgnoreCase("ONLINE")) {
                String paperStatus = papStatusEpson();
                if (paperStatus.equalsIgnoreCase("DOCPRESENT") || paperStatus.equalsIgnoreCase("DOCREADY")) {
                    String paperLoadStatus = prtLoadEpson();
                    if (paperLoadStatus.equalsIgnoreCase("SUCCESS")) {

                        RefValue lpData = new RefValue("");
                        RefValue lpLen = new RefValue(0);
                        RefValue lpDirection = new RefValue(0);

                        strScanOption.dwScanOption1 = intscanPortionValue;
                        strScanOption.dwScanOption2 = 0;
                        strScanOption.dwScanOption3 = 0;

                        int scanOptionStatus = objwrapper.PrtSetScanOption(strScanOption);
                        System.out.println("scanOptionStatus " + scanOptionStatus);

                        intStatus = objwrapper.PrtBarRead(lpData, lpLen, lpDirection);
                        TS_Tracer.WriteToDeviceLogs(strLogPath, "Scan Barcode status ____ " + intStatus);
                        System.out.println("Barcode Status: " + intStatus);

                        switch (intStatus) {
                            case 0:
                                strdDeviceStatus = "NO_ERROR";

                                int nDirection = lpDirection.toInteger();
                                switch (nDirection) {

                                    case PLQ20APIConst.PRT_BAR_DIRECTION_LEFTRIGHT:
                                        strDirectionStatus = "0";
                                        break;
                                    case PLQ20APIConst.PRT_BAR_DIRECTION_BOTTOMTOP:
                                        strDirectionStatus = "270";
                                        break;
                                    case PLQ20APIConst.PRT_BAR_DIRECTION_RIGHTLEFT:
                                        strDirectionStatus = "180";
                                        break;
                                    case PLQ20APIConst.PRT_BAR_DIRECTION_TOPBOTTOM:
                                        strDirectionStatus = "90";
                                        break;
                                    default:
                                        strDirectionStatus = "";
                                        break;
                                }

                                nData = lpData.toStringValue();
                                if (nData.length() < intlowerBarcodeValue || nData.length() > intupperBarcodeValue) {
                                    strdDeviceStatus = "INVALID_BARCODE";
                                }

                                break;

                            case 13:
                                closeDeviceEpson();
                                intStatus = 13;

                                strdDeviceStatus = "OFFLINE";

                                break;
                            case 14:
                                closeDeviceEpson();
                                intStatus = 14;

                                strdDeviceStatus = "OFFLINE";

                                break;

                            case 30:
                                strdDeviceStatus = "PRINTER_BUSY";
                                break;
                            case 32:
                                strdDeviceStatus = "COVER_OPEN";
                                break;
                            case 39:
                                strdDeviceStatus = "UNKNOWN_ERROR";
                                break;
                            case 51:
                                strdDeviceStatus = "BARCODE_READ_ERROR";
                                break;
                            case 52:
                                strdDeviceStatus = "BARCODE_NOT_FOUND";
                                break;
                            case 60:
                                strdDeviceStatus = "SCANNING_ERROR";
                                break;
                            default:
                                strdDeviceStatus = "UNKNOWN_ERROR";
                                break;

                        }

                        if (strdDeviceStatus.equals("COVER_OPEN")) {
                            intStatus = objwrapper.PrtRestart();
                            closeDeviceEpson();
                            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~COVER_OPEN Printer Restart~~~~~~~3");
                            strdDeviceStatus = "COVER_OPEN";
                        } else if (strdDeviceStatus.equals("UNKNOWN_ERROR")) {
                            intunknownErrorCounter = intunknownErrorCounter + 1;

                        }

                        if (intunknownErrorCounter >= 3) {

                            intStatus = objwrapper.PrtRestart();
                            closeDeviceEpson();
                            intunknownErrorCounter = 0;
                            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
                            strdDeviceStatus = "UNKNOWN_ERROR";
                        }
                    } else {
                        return paperLoadStatus;
                    }
                } else {
                    return paperStatus;
                }
            } else {
                return printerStatus;
            }

            TS_Tracer.WriteToDeviceLogs(strLogPath, "Scan Barcode");
            if (intStatus == 13) {
                TS_Tracer.WriteToDeviceLogs(strLogPath, "scanBarcode---> " + strdDeviceStatus + " Actual scanBarcode status--> " + " WRITE_PORT_ERROR");
            } else if (intStatus == 14) {
                TS_Tracer.WriteToDeviceLogs(strLogPath, "scanBarcode---> " + strdDeviceStatus + " Actual scanBarcode status--> " + " READ_PORT_ERROR");
            }
            TS_Tracer.WriteToDeviceLogs(strLogPath, "Lower Barcode value: " + intlowerBarcodeValue + " Barcode Length -> " + nData.length() + " Upper Barcode Value: " + intupperBarcodeValue + " scanBarcode---> " + strdDeviceStatus);

            if (strdDeviceStatus.equals("NO_ERROR")) {
                return nData + " | " + strDirectionStatus;
            } else {
                TS_Tracer.WriteToDeviceLogs(strLogPath, "Scan Barcode-->" + strdDeviceStatus);
                return strdDeviceStatus;
            }

        }
    }

    public String checkAutoStartJson() {

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
            JSONObject jsonObjEpsonPLQ22_PassbookPrinter = (JSONObject) jsonObjACU_Config_File.get("EpsonPLQ22_PassbookPrinter");
            JSONObject jsonObjTracingUtility_Config = (JSONObject) jsonObjACU_Config_File.get("TracingUtility_Config");

            String strAuto_Start = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Auto_Start");
            String strFont = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Font");
            String strCPI = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("CPI");
            String strLPI = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("LPI");
            String strLower_Barcode_length = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Lower_Barcode_length");
            String strUpper_Barcode_Length = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Upper_Barcode_Length");
            String strScan_Portion = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Scan_Portion");
            String strPassbook_Type = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Passbook_Type");
            String strImage_Store_Path = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Image_Store_Path");
            String strPrint_File_Path = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Print_File_Path");
            String strImage_Print_Path = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Image_Print_Path");
            String strImage_Print_Time = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Image_Print_Time");
            String strPrint_Start_Position = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Print_Start_Position");
            String strVersion = (String) jsonObjEpsonPLQ22_PassbookPrinter.get("Version");

            String strTrace_Path = (String) jsonObjTracingUtility_Config.get("Trace_Path");

            straAutoStart = strAuto_Start;

            strlower_Barcode_length = strLower_Barcode_length;
            strupper_Barcode_Length = strUpper_Barcode_Length;

            strscanPortion = strScan_Portion;
            this.strPassbook_Type = strPassbook_Type;

            intupperBarcodeValue = Integer.parseInt(strupper_Barcode_Length);
            intlowerBarcodeValue = Integer.parseInt(strlower_Barcode_length);
            strimageStorePath = strImage_Store_Path;
            intscanPortionValue = Integer.parseInt(strscanPortion);
            this.strPrint_File_Path = strPrint_File_Path;
            strPrint_Image_File = strImage_Print_Path;
            strImage_Print_Start_Pos = strPrint_Start_Position;
            intPrint_Image_Time = Integer.parseInt(strImage_Print_Time);
            strfont = strFont;
            this.strLPI = strLPI;
            this.strCPI = strCPI;
            intlpi = Integer.parseInt(this.strLPI);
            intcpi = Integer.parseInt(this.strCPI);
            strversion = strVersion;
            strLogPath = strTrace_Path;

            return straAutoStart;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "MISC";
    }

    public void closeDeviceEpson() {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }

            TS_Tracer.WriteToDeviceLogs(strLogPath, "closeDevice");

            return;
        }

        String strcheckStatus = checkAutoStartJson();

        intStatus = objwrapper.PrtClose();
        TS_Tracer.WriteToDeviceLogs(strLogPath, "closeDevice status ____ " + intStatus);
        switch (intStatus) {
            case 0:
                strdDeviceStatus = "NO_ERROR";
                break;
            case 10:
                strdDeviceStatus = "PORT_IS_NOT_OPEN_YET";
                break;
            case 12:

                strdDeviceStatus = "PRINTER_NOT_CONNECTED";
                break;
            case 15:
                strdDeviceStatus = "OFFLINE";
                break;
            case 30:
                strdDeviceStatus = "PRINTER_BUSY";
                break;
            case 31:
                strdDeviceStatus = "OFFLINE";
                break;
            case 32:
                strdDeviceStatus = "COVER_OPEN";
                break;
            case 33:
                strdDeviceStatus = "PAPER_JAM";
                break;
            case 39:
                strdDeviceStatus = "UNKNOWN_ERROR";
                break;
            default:
                strdDeviceStatus = "UNKNOWN_ERROR";
                break;

        }
        TS_Tracer.WriteToDeviceLogs(strLogPath, "closeDevice---> " + strdDeviceStatus);

    }

    public String ejectEpson() {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            String strvalue = new String("");
            strvalue = objPLQ22LinuxSimulator.eject();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "eject status ____ " + strvalue);

            return strvalue;

        }

        String strCheckStatus = checkAutoStartJson();
        if (strCheckStatus.equalsIgnoreCase("MISC")) {
            strdDeviceStatus = "MISC";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "eject---> " + strdDeviceStatus);
            return strdDeviceStatus;
        }
        if (!straAutoStart.equalsIgnoreCase("Start")) {
            strdDeviceStatus = "NOT_CONFIGURED";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "eject---> " + strdDeviceStatus);
            return strdDeviceStatus;
        } else {
            String printerStatus = devStatusEpson();
            if (printerStatus.equalsIgnoreCase("ONLINE") || printerStatus.equalsIgnoreCase("PAPER_JAM")) {

                intStatus = objwrapper.PrtEject();
                TS_Tracer.WriteToDeviceLogs(strLogPath, "eject status ____ " + intStatus);
                System.out.println("Eject Status: " + intStatus);
                switch (intStatus) {
                    case 0:
                        strdDeviceStatus = "SUCCESS";
                        break;

                    case 10:
                        strdDeviceStatus = "PORT_IS_NOT_OPEN_YET";
                        break;

                    case 12:

                        strdDeviceStatus = "PRINTER_NOT_CONNECTED";
                        break;
                    case 13:
                        closeDeviceEpson();
                        intStatus = 13;

                        strdDeviceStatus = "OFFLINE";

                        break;
                    case 14:
                        closeDeviceEpson();
                        intStatus = 14;
                        strdDeviceStatus = "OFFLINE";
                        break;
                    case 15:
                        strdDeviceStatus = "OFFLINE";
                        break;
                    case 20:
                        strdDeviceStatus = "INVALID_PARAMETER";
                        break;
                    case 21:
                        strdDeviceStatus = "PARAMETER_NOT_SUPPORTED";
                        break;
                    case 22:
                        strdDeviceStatus = "INSUFFICIENT_BUFFER_SIZE";
                        break;
                    case 30:
                        strdDeviceStatus = "PRINTER_BUSY";
                        break;
                    case 31:
                        strdDeviceStatus = "OFFLINE";
                        break;
                    case 32:
                        strdDeviceStatus = "COVER_OPEN";
                        break;
                    case 33:
                        strdDeviceStatus = "PAPER_JAM";
                        break;
                    case 39:
                        strdDeviceStatus = "UNKNOWN_ERROR";
                        break;

                    default:
                        strdDeviceStatus = "UNKNOWN_ERROR";
                        break;
                }
                if (strdDeviceStatus.equals("UNKNOWN_ERROR")) {
                    intunknownErrorCounter = intunknownErrorCounter + 1;

                } else if (intunknownErrorCounter >= 3) {

                    intStatus = objwrapper.PrtRestart();
                    closeDeviceEpson();
                    strdDeviceStatus = "UNKNOWN_ERROR";
                    intunknownErrorCounter = 0;
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
                }

            } else {
                return printerStatus;
            }

            switch (intStatus) {
                case 13:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "eject---> " + strdDeviceStatus + " Actual eject--> " + " WRITE_PORT_ERROR");
                    break;
                case 14:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "eject---> " + strdDeviceStatus + " Actual eject--> " + " READ_PORT_ERROR");
                    break;
                default:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "eject---> " + strdDeviceStatus);
                    break;
            }

            return strdDeviceStatus;
        }
    }

    public String prtLoadEpson() {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            String strvalue = new String("");
            strvalue = objPLQ22LinuxSimulator.prtLoad();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "prtLoad status ____ " + strvalue);

            return strvalue;

        }

        strcheckAutoStartStatus = checkAutoStartJson();
        if (strcheckAutoStartStatus.equalsIgnoreCase("MISC")) {
            strdDeviceStatus = "MISC";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "prtLoad---> " + strdDeviceStatus);
            return strdDeviceStatus;
        }
        if (!straAutoStart.equalsIgnoreCase("Start")) {
            strdDeviceStatus = "NOT_CONFIGURED";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "prtLoad---> " + strdDeviceStatus);

            return strdDeviceStatus;
        } else {
            String printerStatus = devStatusEpson();
            if (printerStatus.equalsIgnoreCase("ONLINE")) {
                String paperStatus = papStatusEpson();
                if (paperStatus.equalsIgnoreCase("DOCPRESENT") || paperStatus.equalsIgnoreCase("DOCREADY")) {

                    intStatus = objwrapper.PrtLoad();
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "prtLoad status ____ " + intStatus);
                    String paperStatus1 = papStatusEpson();
                    if (paperStatus1.equalsIgnoreCase("DOCPRESENT") || paperStatus1.equalsIgnoreCase("DOCREADY")) {
                        System.out.println(intStatus);
                        switch (intStatus) {
                            case 0:
                                strdDeviceStatus = "SUCCESS";
                                break;

                            case 13:
                                closeDeviceEpson();
                                intStatus = 13;

                                strdDeviceStatus = "OFFLINE";

                                break;
                            case 14:
                                closeDeviceEpson();
                                intStatus = 14;

                                strdDeviceStatus = "OFFLINE";

                                break;
                            case 32:
                                strdDeviceStatus = "COVER_OPEN";
                                break;
                            case 39:
                                strdDeviceStatus = "UNKNOWN_ERROR";
                                break;
                            case 10:
                                strdDeviceStatus = "PORT_IS_NOT_OPEN_YET";
                                break;

                            case 30:
                                strdDeviceStatus = "PRINTER_BUSY";
                                break;

                            case 60:
                                strdDeviceStatus = "SCANNING_ERROR";
                                break;

                            case 31:
                                strdDeviceStatus = "OFFLINE";
                                break;

                            case 33:
                                strdDeviceStatus = "PAPER_JAM";
                                break;

                            case 12:

                                strdDeviceStatus = "PRINTER_NOT_CONNECTED";
                                break;

                            case 15:
                                strdDeviceStatus = "OFFLINE";

                                break;
                            case 20:
                                strdDeviceStatus = "INVALID_PARAMETER";
                                break;
                            case 21:
                                strdDeviceStatus = "PARAMETER_NOT_SUPPORTED";
                                break;
                            case 22:
                                strdDeviceStatus = "INSUFFICIENT_BUFFER_SIZE";
                                break;
                            default:
                                strdDeviceStatus = "UNKNOWN_ERROR";
                                break;

                        }

                        if (strdDeviceStatus.equals("UNKNOWN_ERROR")) {
                            intunknownErrorCounter = intunknownErrorCounter + 1;

                        } else if (intunknownErrorCounter >= 3) {

                            intStatus = objwrapper.PrtRestart();
                            closeDeviceEpson();
                            strdDeviceStatus = "UNKNOWN_ERROR";
                            intunknownErrorCounter = 0;
                            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
                        }

                    }
                } else {

                    return paperStatus;
                }
            } else {

                return printerStatus;
            }

            switch (intStatus) {
                case 13:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "prtLoad---> " + strdDeviceStatus + " Actual prtLoad status--> " + " WRITE_PORT_ERROR");
                    break;
                case 14:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "prtLoad---> " + strdDeviceStatus + " Actual prtLoad status--> " + " READ_PORT_ERROR");
                    break;
                default:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "prtLoad---> " + strdDeviceStatus);
                    break;
            }

            return strdDeviceStatus;
        }

    }

    public String resetDeviceEpson() {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            String strvalue = new String("");
            strvalue = objPLQ22LinuxSimulator.resetDevice();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "resetDevice status ____ " + strvalue);

            return strvalue;

        }

        strcheckAutoStartStatus = checkAutoStartJson();
        if (strcheckAutoStartStatus.equalsIgnoreCase("MISC")) {
            strdDeviceStatus = "MISC";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "resetDevice---> " + strdDeviceStatus);
            return strdDeviceStatus;
        }
        if (!straAutoStart.equalsIgnoreCase("Start")) {
            strdDeviceStatus = "NOT_CONFIGURED";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "resetDevice---> " + strdDeviceStatus);

            return strdDeviceStatus;
        } else {

            String printerStatus = devStatusEpson();

            if (printerStatus.equalsIgnoreCase("ONLINE")) {
                intStatus = objwrapper.PrtReset();
                switch (intStatus) {
                    case 0:
                        strdDeviceStatus = "ONLINE";

                        break;
                    case 10:
                        strdDeviceStatus = "PORT_IS_NOT_OPEN_YET";
                        break;
                    case 13:
                        closeDeviceEpson();
                        intStatus = 13;

                        strdDeviceStatus = "OFFLINE";

                        break;
                    case 14:
                        closeDeviceEpson();
                        intStatus = 14;

                        strdDeviceStatus = "OFFLINE";

                        break;
                    case 32:
                        strdDeviceStatus = "COVER_OPEN";
                        break;
                    case 33:
                        strdDeviceStatus = "PAPER_JAM";
                        break;
                    case 39:
                        strdDeviceStatus = "UNKNOWN_ERROR";
                        break;

                    case 31:
                        strdDeviceStatus = "OFFLINE";
                        break;

                    case 12:

                        strdDeviceStatus = "PRINTER_NOT_CONNECTED";
                        break;

                    case 15:
                        strdDeviceStatus = "OFFLINE";

                        break;
                    case 20:
                        strdDeviceStatus = "INVALID_PARAMETER";
                        break;
                    case 21:
                        strdDeviceStatus = "PARAMETER_NOT_SUPPORTED";
                        break;
                    case 22:
                        strdDeviceStatus = "INSUFFICIENT_BUFFER_SIZE";
                        break;
                    case 30:
                        strdDeviceStatus = "PRINTER_BUSY";
                        break;

                }

                if (strdDeviceStatus.equals("UNKNOWN_ERROR")) {
                    intunknownErrorCounter = intunknownErrorCounter + 1;

                } else if (intunknownErrorCounter >= 3) {

                    intStatus = objwrapper.PrtRestart();
                    closeDeviceEpson();
                    strdDeviceStatus = "UNKNOWN_ERROR";
                    intunknownErrorCounter = 0;
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
                }

            } else {

                return printerStatus;
            }

            if (intStatus == 13) {
                TS_Tracer.WriteToDeviceLogs(strLogPath, "resetDevice---> " + strdDeviceStatus + " Actual resetDeviceStatus--> " + " WRITE_PORT_ERROR");
            } else if (intStatus == 14) {
                TS_Tracer.WriteToDeviceLogs(strLogPath, "resetDevice---> " + strdDeviceStatus + " Actual resetDeviceStatus--> " + " READ_PORT_ERROR");
            } else {
                TS_Tracer.WriteToDeviceLogs(strLogPath, "resetDevice---> " + strdDeviceStatus);
            }

            return strdDeviceStatus;
        }
    }

    public String papStatusEpson() {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            String strvalue = new String("");
            strvalue = objPLQ22LinuxSimulator.papStatus();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "papStatus status ____ " + strvalue);

            return strvalue;

        }

        String strMCheckStatus = checkAutoStartJson();
        if (strMCheckStatus.equalsIgnoreCase("MISC")) {
            strdDeviceStatus = "MISC";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "papStatus---> " + strdDeviceStatus);
            return strdDeviceStatus;
        }
        if (!straAutoStart.equalsIgnoreCase("Start")) {
            strdDeviceStatus = "NOT_CONFIGURED";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "papStatus---> " + strdDeviceStatus);

            return strdDeviceStatus;
        } else {

            String strMPrinterStatus = devStatusEpson();
            if (strMPrinterStatus.equalsIgnoreCase("ONLINE")) {

                intStatus = objwrapper.PrtPaperStatus(objrefValue);
                TS_Tracer.WriteToDeviceLogs(strLogPath, "papStatus status ____ " + intStatus);
                Integer value = objrefValue.toInteger();
                TS_Tracer.WriteToDeviceLogs(strLogPath, "papStatus status value" + value);
                if (value == 0 && intStatus == 0) {
                    strdDeviceStatus = "DOCABSENT";

                } else if (value == 1 && intStatus == 0) {
                    strdDeviceStatus = "DOCEJECTED";
                } else if (value == 2 && intStatus == 0) {
                    strdDeviceStatus = "DOCPRESENT";
                } else if (value == 3 && intStatus == 0) {
                    strdDeviceStatus = "DOCREADY";
                } else if (value == 0 && intStatus == 10) {
                    strdDeviceStatus = "PORT_IS_NOT_OPEN_YET";
                } else if (value == 0 && intStatus == 13) {
                    closeDeviceEpson();
                    intStatus = 13;

                    strdDeviceStatus = "OFFLINE";

                } else if (value == 0 && intStatus == 14) {
                    closeDeviceEpson();
                    intStatus = 14;
                    strdDeviceStatus = "OFFLINE";

                } else if (value == 0 && intStatus == 39) {
                    closeDeviceEpson();
                    intStatus = 39;
                    strdDeviceStatus = "UNKNOWN_ERROR";

                } else if (strdDeviceStatus.equals("UNKNOWN_ERROR")) {
                    intunknownErrorCounter = intunknownErrorCounter + 1;

                }
                if (intunknownErrorCounter >= 3) {

                    intStatus = objwrapper.PrtRestart();
                    closeDeviceEpson();
                    strdDeviceStatus = "UNKNOWN_ERROR";
                    intunknownErrorCounter = 0;
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
                }

                System.out.println("Paper's Status: " + strdDeviceStatus);

            } else {

                return strMPrinterStatus;
            }
        }

        switch (intStatus) {
            case 13:
                TS_Tracer.WriteToDeviceLogs(strLogPath, "papStatus---> " + strdDeviceStatus + " Actual papStatus--> " + " WRITE_PORT_ERROR");
                break;
            case 14:
                TS_Tracer.WriteToDeviceLogs(strLogPath, "papStatus---> " + strdDeviceStatus + " Actual papStatus--> " + " READ_PORT_ERROR");
                break;
            default:
                TS_Tracer.WriteToDeviceLogs(strLogPath, "papStatus---> " + strdDeviceStatus);
                break;
        }

        return strdDeviceStatus;

    }

    public String printPBEpson() {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            String strvalue = new String("");
            strvalue = objPLQ22LinuxSimulator.printPB();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "printPB status ____ " + strvalue);

            return strvalue;

        }

        String strMStoreData = "";
        int intMCount = 0;
        String strMLineRead = "";

        strcheckAutoStartStatus = checkAutoStartJson();

        if (strcheckAutoStartStatus.equalsIgnoreCase("MISC")) {
            strdDeviceStatus = strfatalStatus;
            TS_Tracer.WriteToDeviceLogs(strLogPath, "Device Status --> " + strdDeviceStatus);

            return strdDeviceStatus;
        }
        if (!straAutoStart.equalsIgnoreCase("Start")) {
            strdDeviceStatus = "NOT_CONFIGURED";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "Device Status --> " + strdDeviceStatus);

            return strdDeviceStatus;
        } else {

            String printerStatus = devStatusEpson();
            if (printerStatus.equalsIgnoreCase("ONLINE")) {

                String paperLoadStatus = prtLoadEpson();
                if (paperLoadStatus.equalsIgnoreCase("SUCCESS")) {

                    switch (strfont) {
                        case "PRT_FONT_HSD":
                            objwrapper.PrtSetFont(1);
                            break;
                        case "PRT_FONT_DRAFT":
                            objwrapper.PrtSetFont(2);
                            break;
                        case "PRT_FONT_NLQ1":
                            objwrapper.PrtSetFont(3);
                            break;
                        case "PRT_FONT_NLQ2":
                            objwrapper.PrtSetFont(4);
                            break;
                        case "PRT_FONT_OCRB":
                            objwrapper.PrtSetFont(5);
                            break;
                        case "PRT_FONT_OCRA":
                            objwrapper.PrtSetFont(6);
                            break;
                        case "PRT_FONT_VHSD":
                            objwrapper.PrtSetFont(7);
                            break;
                        case "PRT_FONT_LQ2":
                            objwrapper.PrtSetFont(8);
                            break;
                        case "PRT_FONT_DRAFT_IT":
                            objwrapper.PrtSetFont(12);
                            break;
                        case "PRT_FONT_NLQ1_IT":
                            objwrapper.PrtSetFont(13);
                            break;
                        case "PRT_FONT_NLQ2_IT":
                            objwrapper.PrtSetFont(14);
                            break;
                        case "PRT_FONT_LQ2_IT":
                            objwrapper.PrtSetFont(18);
                            break;
                        default:

                            System.out.println("Default font :" + objwrapper.PrtSetFont(0));

                    }

                    switch (intcpi) {
                        case 5:
                            objwrapper.PrtSetCPI(5);
                            break;
                        case 10:
                            objwrapper.PrtSetCPI(10);
                            break;
                        case 12:
                            objwrapper.PrtSetCPI(12);
                            break;
                        case 13:
                            objwrapper.PrtSetCPI(13);
                            break;
                        case 15:
                            objwrapper.PrtSetCPI(15);
                            break;
                        case 16:
                            objwrapper.PrtSetCPI(16);
                            break;
                        case 17:
                            objwrapper.PrtSetCPI(17);
                            break;
                        case 18:
                            objwrapper.PrtSetCPI(18);
                            break;
                        case 22:
                            objwrapper.PrtSetCPI(22);
                            break;
                        default:
                            objwrapper.PrtSetCPI(0);

                    }

                    switch (intlpi) {
                        case 3:
                            objwrapper.PrtSetLPI(80);
                            break;
                        case 4:
                            objwrapper.PrtSetLPI(60);
                            break;
                        case 5:
                            objwrapper.PrtSetLPI(48);
                            break;
                        case 6:
                            objwrapper.PrtSetLPI(40);
                            break;
                        case 8:
                            objwrapper.PrtSetLPI(30);
                            break;
                        default:
                            objwrapper.PrtSetLPI(0);
                    }

                    String data = "";

                    BufferedReader brMFiledata = null;

                    File fileMPrintfile = new File(strPrint_File_Path);

                    if (!fileMPrintfile.exists()) {
                        strdDeviceStatus = "MISC";

                        TS_Tracer.WriteToDeviceLogs(strLogPath, "Print Status ____ " + strdDeviceStatus);
                        return strdDeviceStatus;
                    }

                    try {
                        brMFiledata = new BufferedReader(new FileReader(fileMPrintfile));
                        LineNumberReader lnr = new LineNumberReader(new FileReader(fileMPrintfile));
                        while ((strMLineRead = lnr.readLine()) != null) {

                        }
                        intMCount = lnr.getLineNumber();
                        System.out.println("The count of File is: " + intMCount);
                        lnr.close();

                        while ((data = brMFiledata.readLine()) != null) {
                            strMStoreData = strMStoreData + data;

                            byte[] addByte = {0x0D, 0x0A};
                            String addStr = new String(addByte);

                            strMStoreData = strMStoreData + addStr;

                        }

                        intStatus = objwrapper.PrtWrite(strMStoreData, strMStoreData.length());

                        TS_Tracer.WriteToDeviceLogs(strLogPath, "Print Status ____ " + intStatus);
                        System.out.println("Printing Status :" + intStatus);

                        if (intMCount == 0) {
                            strdDeviceStatus = "NO_DATA_TO_PRINT";
                        }

                        switch (intStatus) {
                            case 0:
                                strdDeviceStatus = "SUCCESS";
                                break;
                            case 39:
                                strdDeviceStatus = "UNKNOWN_ERROR";
                                break;
                            default:
                                strdDeviceStatus = devStatusEpson();
                                if (strdDeviceStatus.equals("PAPER_JAM")) {

                                    strdDeviceStatus = "PAPER_JAM";
                                }
                                break;
                        }

                        if (strdDeviceStatus.equals("COVER_OPEN")) {
                            intStatus = objwrapper.PrtRestart();
                            closeDeviceEpson();
                            strdDeviceStatus = "COVER_OPEN";
                            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~COVER_OPEN Printer Restart~~~~~~~");

                        } else if (strdDeviceStatus.equals("UNKNOWN_ERROR")) {
                            intunknownErrorCounter = intunknownErrorCounter + 1;

                        }

                        if (intunknownErrorCounter >= 3) {

                            intStatus = objwrapper.PrtRestart();
                            closeDeviceEpson();
                            strdDeviceStatus = "UNKNOWN_ERROR";
                            intunknownErrorCounter = 0;
                            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
                        }
                    } catch (Exception e) {
                        Logger.getLogger(EpsonPLQ22.class
                                .getName()).log(Level.SEVERE, null, e);
                    }

                } else {

                    return paperLoadStatus;
                }
            } else {

                return printerStatus;
            }

        }

        TS_Tracer.WriteToDeviceLogs(strLogPath, "printPB---> " + strdDeviceStatus + " No. of lines in given file: " + intMCount + " Font Is: " + strfont + " LPI Is: " + intlpi + " CPI is: " + intcpi);

        return strdDeviceStatus;

    }

    public String PrintImageEpson(int intNoOfLines) {
        TS_Tracer.WriteToDeviceLogs(strLogPath, "imgPrintimage number of lines ____ " + intNoOfLines);
        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            String strvalue = new String("");
            strvalue = objPLQ22LinuxSimulator.PrintImage();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "imgPrintimage status ____ " + strvalue);

            return strvalue;

        }

        String strStoreData = "";
        int intCount = 0;
        String intLineRead = "";

        strcheckAutoStartStatus = checkAutoStartJson();

        if (strcheckAutoStartStatus.equalsIgnoreCase("MISC")) {
            strdDeviceStatus = strfatalStatus;
            TS_Tracer.WriteToDeviceLogs(strLogPath, "Device Status --> " + strdDeviceStatus);

            return strdDeviceStatus;
        }
        if (!straAutoStart.equalsIgnoreCase("Start")) {
            strdDeviceStatus = "NOT_CONFIGURED";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "Device Status --> " + strdDeviceStatus);

            return strdDeviceStatus;
        } else {

            String printerStatus = devStatusEpson();
            if (printerStatus.equalsIgnoreCase("ONLINE")) {

                String paperLoadStatus = prtLoadEpson();
                if (paperLoadStatus.equalsIgnoreCase("SUCCESS")) {

                    File ImgPath = new File(strPrint_Image_File);

                    if (!ImgPath.exists()) {
                        strdDeviceStatus = "MISC";

                        TS_Tracer.WriteToDeviceLogs(strLogPath, "Print imgimage Status ____ " + strdDeviceStatus);
                        return strdDeviceStatus;
                    }

                    try {

                        imgPrintimage = ImageIO.read(ImgPath);
                        intPrintimagewidth = imgPrintimage.getWidth();
                        intPrintimageheight = imgPrintimage.getHeight();

                        long start = System.currentTimeMillis();

                        int position = 1440 - Integer.parseInt(strImage_Print_Start_Pos);
                        if (0 <= Integer.parseInt(strImage_Print_Start_Pos) || Integer.parseInt(strImage_Print_Start_Pos) <= position) {
                            intStatus = objwrapper.PrtWriteImage(strPrint_Image_File, Integer.parseInt(strImage_Print_Start_Pos));
                        } else {
                            intStatus = 9999;

                        }

                        long end = System.currentTimeMillis();

                        float sec = (end - start) / 1000F;
                        System.out.println("total time" + sec);

                        int tmptime = intNoOfLines / 5;
                        int remtime = intNoOfLines % 5;

                        if (remtime > 0) {
                            tmptime++;
                        }

                        while (sec < (tmptime * intPrint_Image_Time)) {
                            end = System.currentTimeMillis();
                            sec = (end - start) / 1000F;
                        }
                        TS_Tracer.WriteToDeviceLogs(strLogPath, "Print imgimage Status ____ " + intStatus);
                        System.out.println("Printing Status :" + intStatus);
                        switch (intStatus) {
                            case 0:
                                String prtdeviceStatus = "SUCCESS";
                                String tmpdevStatus = new String(devStatusEpson());
                                if (tmpdevStatus.equals("ONLINE")) {
                                    strdDeviceStatus = new String(prtdeviceStatus);
                                } else {
                                    strdDeviceStatus = new String(tmpdevStatus);
                                }
                                break;
                            case 9999:
                                strdDeviceStatus = "PRT_ERR_INVALIDSIZE";
                                break;
                            case 20:
                                strdDeviceStatus = "PRT_ERR_INVALIDPARAM";
                                break;
                            case 21:
                                strdDeviceStatus = "PRT_ERR_NOTSUPPORTED";
                                break;
                            case 22:
                                strdDeviceStatus = "PRT_ERR_BUFFERSIZE";
                                break;
                            case 23:
                                strdDeviceStatus = "PRT_ERR_UNSUPPORTEDFORMAT";
                                break;
                            case 40:
                                strdDeviceStatus = "PRT_ERR_MAGWRITE";
                                break;
                            case 41:
                                strdDeviceStatus = "PRT_ERR_MAGREAD";
                                break;
                            case 42:
                                strdDeviceStatus = "PRT_ERR_NOMAGDATA";
                                break;
                            case 39:
                                strdDeviceStatus = "UNKNOWN_ERROR";
                                break;
                            default:
                                strdDeviceStatus = devStatusEpson();
                                if (strdDeviceStatus.equalsIgnoreCase("OFFLINE")) {

                                    TimeUnit.MILLISECONDS.sleep(1800);
                                    strdDeviceStatus = devStatusEpson();
                                }
                                if (strdDeviceStatus.equals("PAPER_JAM")) {

                                    strdDeviceStatus = "PAPER_JAM";
                                }
                                break;
                        }

                        if (strdDeviceStatus.equals("COVER_OPEN")) {
                            intStatus = objwrapper.PrtRestart();
                            closeDeviceEpson();
                            strdDeviceStatus = "COVER_OPEN";
                            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~COVER_OPEN Printer Restart~~~~~~~");

                        } else if (strdDeviceStatus.equals("UNKNOWN_ERROR")) {
                            intunknownErrorCounter = intunknownErrorCounter + 1;

                        }

                        if (intunknownErrorCounter >= 3) {

                            intStatus = objwrapper.PrtRestart();
                            closeDeviceEpson();
                            strdDeviceStatus = "UNKNOWN_ERROR";
                            intunknownErrorCounter = 0;
                            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
                        }
                    } catch (Exception e) {
                        Logger.getLogger(EpsonPLQ22.class
                                .getName()).log(Level.SEVERE, null, e);
                    }

                } else {

                    return paperLoadStatus;
                }
            } else {

                return printerStatus;
            }

        }

        TS_Tracer.WriteToDeviceLogs(strLogPath, "imgPrintimage---> " + strdDeviceStatus);

        return strdDeviceStatus;

    }

    public String[] scanPBEpson(String strMinputdata) {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {

            } catch (Exception e) {

            }
            intcount = 1;

            String[] strvalue = objPLQ22LinuxSimulator.scanPB(strMinputdata);
            TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB status ____ " + strvalue[0] + " " + strvalue[1]);

            return strvalue;

        }

        String nData = "";
        strMinputdata = "";

        try {

            String[] printerStatus = {devStatusEpson()};
            if (printerStatus[0].equalsIgnoreCase("ONLINE")) {
                String[] paperStatus = {papStatusEpson()};
                if (paperStatus[0].equalsIgnoreCase("DOCPRESENT") || paperStatus[0].equalsIgnoreCase("DOCREADY")) {
                    System.out.println("inside scan PB----> Paper Status");
                    /*for(String str1 : strscanArray){
                    
                     str1 = "";
                    
                     }*/

                    String[] paperLoadStatus = {prtLoadEpson()};
                    if (paperLoadStatus[0].equalsIgnoreCase("SUCCESS")) {

                        System.out.println("inside scan PB----> Paper Load");
                        objscanParam.dwScanSide = PLQ20APIConst.PRT_SCANSIDE_FRONT;
                        objscanParam.dwResolution = PLQ20APIConst.PRT_SCAN_DPI_200;
                        objscanParam.dwImageType = PLQ20APIConst.PRT_IMAGE_TYPE_COLOR;
                        objscanParam.dwFileFormat = PLQ20APIConst.PRT_FFMT_JPEGNORMAL;

                        objscanParam.dwThreshold = 0;

                        objscanParam.dwAreaXs = 0;
                        objscanParam.dwAreaYs = 0;

                        objscanParam.dwAreaXe = 2100;

                        if (intscanPortionValue == 1) {

                            objscanParam.dwAreaYe = 1000;

                        } else {

                            objscanParam.dwAreaYe = 2970;

                        }

                        RefValue lpData = new RefValue("");
                        RefValue lpLen = new RefValue(0);
                        RefValue lpDirection = new RefValue(0);

                        TS_Tracer.WriteToDeviceLogs(strLogPath, "-----START ScanPB------ ");

                        intStatus = objwrapper.PrtBarRead_ScanToFile(lpData, lpLen, lpDirection, objscanParam, strimageStorePath);
                        TS_Tracer.WriteToDeviceLogs(strLogPath, "ScanPB status ____ " + intStatus);
                        System.out.println("scan Status: " + intStatus);

                        switch (intStatus) {
                            case 0:
                                strdDeviceStatus = "NO_ERROR";

                                int nDirection = lpDirection.toInteger();

                                switch (nDirection) {

                                    case PLQ20APIConst.PRT_BAR_DIRECTION_LEFTRIGHT:
                                        strDirectionStatus = "0";
                                        break;
                                    case PLQ20APIConst.PRT_BAR_DIRECTION_BOTTOMTOP:
                                        strDirectionStatus = "270";
                                        break;
                                    case PLQ20APIConst.PRT_BAR_DIRECTION_RIGHTLEFT:
                                        strDirectionStatus = "180";
                                        break;
                                    case PLQ20APIConst.PRT_BAR_DIRECTION_TOPBOTTOM:
                                        strDirectionStatus = "90";
                                        break;
                                    default:
                                        strDirectionStatus = "";
                                        break;
                                }
                                File input = new File(strimageStorePath + ".jpg");
                                imgimage = ImageIO.read(input);
                                intWidth = imgimage.getWidth();
                                intHeight = imgimage.getHeight();
                                System.out.println("Scanned Files' intWidth: " + intWidth + " & Heigth: " + intHeight);
                                nData = lpData.toStringValue();
                                TS_Tracer.WriteToDeviceLogs(strLogPath, "Barcode---> " + nData);
                                if (strPassbook_Type.equalsIgnoreCase("Width")) {
                                    strpbkAlign = compareHeigthGreaterWidthEpson(intWidth, intHeight);
                                    System.out.println("***************" + strpbkAlign);
                                    if (strpbkAlign.equalsIgnoreCase("IMPROPER_PASSBOOK_INSERTION")) {
                                        strscanArray[0] = "SUCCESS";
                                        strscanArray[1] = "IMPROPER_PASSBOOK_INSERTION";

                                    } else {
                                        if (nData.isEmpty()) {

                                            strscanArray[0] = "SUCCESS";
                                            strscanArray[1] = "NULL";

                                        } else if (nData.length() < intlowerBarcodeValue || nData.length() > intupperBarcodeValue) {
                                            strscanArray[0] = "SUCCESS";
                                            strscanArray[1] = "UNSATISFIED_LENGTH";

                                        } else if (!nData.isEmpty()) {
                                            strscanArray[0] = "SUCCESS";
                                            strscanArray[1] = nData;
                                        }
                                    }

                                } else if (strPassbook_Type.equalsIgnoreCase("Height")) {
                                    strpbkAlign = compareWidthGreaterHeigthEpson(intWidth, intHeight);
                                    if (strpbkAlign.equalsIgnoreCase("IMPROPER_PASSBOOK_INSERTION")) {
                                        strscanArray[0] = "SUCCESS";
                                        strscanArray[1] = "IMPROPER_PASSBOOK_INSERTION";

                                    } else {

                                        if (nData.isEmpty()) {

                                            strscanArray[0] = "SUCCESS";
                                            strscanArray[1] = "NULL";

                                        } else if (nData.length() < intlowerBarcodeValue || nData.length() > intupperBarcodeValue) {
                                            strscanArray[0] = "SUCCESS";
                                            strscanArray[1] = "UNSATISFIED_LENGTH";

                                        } else if (!nData.isEmpty()) {
                                            strscanArray[0] = "SUCCESS";
                                            strscanArray[1] = nData;
                                        }
                                    }
                                }

                                int nLen = lpLen.toInteger();

                                break;

                            case 13:
                                closeDeviceEpson();

                                intStatus = 13;
                                strscanArray[0] = "OFFLINE";
                                strscanArray[1] = "OFFLINE";

                                break;
                            case 14:
                                closeDeviceEpson();
                                intStatus = 14;

                                strscanArray[0] = "OFFLINE";
                                strscanArray[1] = "OFFLINE";

                                break;
                            case 15:
                                strscanArray[0] = "OFFLINE";
                                strscanArray[1] = "OFFLINE";

                                break;

                            case 30:
                                strscanArray[0] = "PRINTER_BUSY";
                                strscanArray[1] = "SCAN_ERROR";

                                break;
                            case 32:
                                strscanArray[0] = "COVER_OPEN";
                                strscanArray[1] = "SCAN_ERROR";

                                break;
                            case 33:
                                strscanArray[0] = "PAPER_JAM";
                                strscanArray[1] = "SCAN_ERROR";

                                break;
                            case 39:
                                strscanArray[0] = "MISC_ERROR";
                                strscanArray[1] = "SCAN_ERROR";

                                intunknownErrorCounter = intunknownErrorCounter + 1;
                                break;
                            case 60:
                                strscanArray[0] = "MISC_ERROR";
                                strscanArray[1] = "SCAN_ERROR";

                                break;

                            case 61:
                                strscanArray[0] = "MISC_ERROR";
                                strscanArray[1] = "SCAN_ERROR";

                                break;

                            default:
                                strscanArray[0] = "MISC_ERROR";
                                strscanArray[1] = "SCAN_ERROR";
                                intunknownErrorCounter = intunknownErrorCounter + 1;
                                break;

                        }

                    } else {
                        switch (paperLoadStatus[0]) {
                            case "OFFLINE":
                                strscanArray[0] = "OFFLINE";
                                strscanArray[1] = "OFFLINE";
                                break;
                            case "COVER_OPEN":
                                strscanArray[0] = "COVER_OPEN";
                                strscanArray[1] = "SCAN_ERROR";
                                break;
                            case "UNKNOWN_ERROR":
                                strscanArray[0] = "MISC_ERROR";
                                strscanArray[1] = "SCAN_ERROR";
                                intunknownErrorCounter = intunknownErrorCounter + 1;
                                break;
                            default:
                                strscanArray[0] = "DOC_NOT_FOUND";
                                strscanArray[1] = "SCAN_ERROR";
                                break;
                        }
                        strdDeviceStatus = strscanArray[0];
                        strDeviceStatus1 = strscanArray[1];

                        TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1);
                        return strscanArray;
                    }
                } else {
                    if (paperStatus[0].equals("DOCABSENT")) {
                        strscanArray[0] = "DOC_NOT_FOUND";
                        strscanArray[1] = "SCAN_ERROR";
                    } else if (paperStatus[0].equals("DOCEJECTED")) {
                        strscanArray[0] = "NOT_ALIGNED";
                        strscanArray[1] = "SCAN_ERROR";
                    }
                    strdDeviceStatus = strscanArray[0];
                    strDeviceStatus1 = strscanArray[1];
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1);
                    return strscanArray;
                }

            } else {
                switch (printerStatus[0]) {
                    case "OFFLINE":
                        strscanArray[0] = "OFFLINE";
                        strscanArray[1] = "OFFLINE";
                        break;
                    case "COVER_OPEN":
                        strscanArray[0] = "COVER_OPEN";
                        strscanArray[1] = "SCAN_ERROR";
                        break;
                    case "PAPER_JAM":
                        strscanArray[0] = "PAPER_JAM";
                        strscanArray[1] = "SCAN_ERROR";
                        break;
                    case "UNKNOWN_ERROR":
                        strscanArray[0] = "MISC_ERROR";
                        strscanArray[1] = "SCAN_ERROR";
                        intunknownErrorCounter = intunknownErrorCounter + 1;
                        break;
                    case "MISC":
                        strscanArray[0] = "MISC_ERROR";
                        strscanArray[1] = "SCAN_ERROR";
                        break;
                    case "NOT_CONFIGURED":
                        strscanArray[0] = "NOT_CONFIGURED";
                        strscanArray[1] = "";
                        intcount = 1;
                        break;
                    default:
                        break;
                }
                strdDeviceStatus = strscanArray[0];
                strDeviceStatus1 = strscanArray[1];
                TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1);
                return strscanArray;
            }

        } catch (Exception e) {

            strscanArray[0] = "MISC_ERROR";
            strscanArray[1] = "SCAN_ERROR";
            strdDeviceStatus = strscanArray[0];
            strDeviceStatus1 = strscanArray[1];
            TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1);
            return strscanArray;

        }

        strdDeviceStatus = strscanArray[0];
        strDeviceStatus1 = strscanArray[1];

        switch (intStatus) {
            case 13:
                TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1 + " Actual scanPB--> " + " WRITE_PORT_ERROR");
                break;
            case 14:
                TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1 + " Actual scanPB--> " + " READ_PORT_ERROR");
                break;
            case 39:
                TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1 + " Actual scanPB--> " + " UNKNOWN_ERROR");
                break;
            case 60:
                TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1 + " Actual scanPB--> " + " SCANNING_ERROR");
                break;
            default:
                break;
        }
        TS_Tracer.WriteToDeviceLogs(strLogPath, "scanPB---> " + strdDeviceStatus + "," + strDeviceStatus1);
        TS_Tracer.WriteToDeviceLogs(strLogPath, "<--- Exiting From scanPB --->");

        if (strdDeviceStatus.equals("COVER_OPEN")) {
            intStatus = objwrapper.PrtRestart();
            closeDeviceEpson();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~COVER_OPEN Printer Restart~~~~~~~2");
            strdDeviceStatus = "COVER_OPEN";

        }

        if (intunknownErrorCounter >= 3) {

            intStatus = objwrapper.PrtRestart();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
            closeDeviceEpson();
            intunknownErrorCounter = 0;
            strdDeviceStatus = "UNKNOWN_ERROR";

        }
        return strscanArray;

    }

    public String compareWidthGreaterHeigthEpson(int intWidth, int intHeight) {

        if (objscanParam.dwAreaYe == 2970) {
            if (intHeight < 900 || intWidth < 900 || intWidth > intHeight) {
                strpbkAlign = "IMPROPER_PASSBOOK_INSERTION";
            } else {
                strpbkAlign = "PROPER_PASSBOOK_INSERTION";
            }
        } else if (objscanParam.dwAreaYe == 1000) {
            if (intHeight < 500 || intWidth < 700 || intWidth > intHeight) {
                strpbkAlign = "IMPROPER_PASSBOOK_INSERTION";
            } else {
                strpbkAlign = "PROPER_PASSBOOK_INSERTION";
            }

        }

        return strpbkAlign;

    }

    public String compareHeigthGreaterWidthEpson(int intWidth, int intHeight) {

        if (objscanParam.dwAreaYe == 2970) {
            if (intHeight < 900 || intWidth < 900 || intHeight > intWidth) {
                strpbkAlign = "IMPROPER_PASSBOOK_INSERTION";
            } else {
                strpbkAlign = "PROPER_PASSBOOK_INSERTION";
            }
        } else if (objscanParam.dwAreaYe == 1000) {

            if (intHeight < 500 || intWidth < 700 || intHeight > intWidth) {
                strpbkAlign = "IMPROPER_PASSBOOK_INSERTION";
            } else {
                strpbkAlign = "PROPER_PASSBOOK_INSERTION";
            }
        }

        return strpbkAlign;

    }

    public String configureDeviceEpson() {

        String strcheckStatus = checkAutoStartJson();
        if (strcheckStatus.equalsIgnoreCase("MISC")) {
            strdDeviceStatus = "MISC";
            return strdDeviceStatus;

        }
        if (!straAutoStart.equalsIgnoreCase("Start") || !(strPassbook_Type.equalsIgnoreCase("Width") || strPassbook_Type.equalsIgnoreCase("Height"))) {
            strdDeviceStatus = "NOT_CONFIGURED";

            return strdDeviceStatus;
        } else {
            String str = "USB";
            intStatus = objwrapper.PrtOpen(str);
            TS_Tracer.WriteToDeviceLogs(strLogPath, "configureDevice status ____ " + intStatus);
            switch (intStatus) {
                case 0:

                    strdDeviceStatus = "ONLINE";
                    break;
                case 10:
                    strdDeviceStatus = "PORT_IS_NOT_OPEN_YET";
                    break;
                case 11:

                    strdDeviceStatus = "PORT_ALREADY_OPENED";
                    break;
                case 12:

                    strdDeviceStatus = "PRINTER_NOT_CONNECTED";
                    break;

                case 13:
                    closeDeviceEpson();
                    intStatus = 13;
                    strdDeviceStatus = "OFFLINE";
                    break;
                case 14:
                    closeDeviceEpson();
                    intStatus = 14;
                    strdDeviceStatus = "OFFLINE";
                    break;

                case 15:
                    strdDeviceStatus = "OFFLINE";
                    break;
                case 20:
                    strdDeviceStatus = "INVALID_PARAMETER";
                    break;
                case 21:
                    strdDeviceStatus = "PARAMETER_NOT_SUPPORTED";
                    break;
                case 22:
                    strdDeviceStatus = "INSUFFICIENT_BUFFER_SIZE";
                    break;
                case 30:
                    strdDeviceStatus = "PRINTER_BUSY";
                    break;
                case 31:
                    strdDeviceStatus = "OFFLINE";
                    break;
                case 32:
                    strdDeviceStatus = "COVER_OPEN";
                    break;
                case 39:
                    strdDeviceStatus = "UNKNOWN_ERROR";
                    break;
                default:
                    strdDeviceStatus = "UNKNOWN_ERROR";
                    break;

            }
            if (strdDeviceStatus.equals("UNKNOWN_ERROR")) {
                intunknownErrorCounter = intunknownErrorCounter + 1;

            } else if (intunknownErrorCounter >= 3) {

                intStatus = objwrapper.PrtRestart();
                closeDeviceEpson();
                strdDeviceStatus = "UNKNOWN_ERROR";
                intunknownErrorCounter = 0;
                TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
            }

            if (intconfigState == 0) {
                intconfigState = 1;

                if (intStatus == 15) {
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "configureDevice---> " + strdDeviceStatus + " Actual configureDevice Status--> " + " TIMEOUT");
                } else {
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "configureDevice---> " + strdDeviceStatus);
                }
            }
            return strdDeviceStatus;
        }

    }

    public String devStatusEpson() {

        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }
            String strvalue = new String("");
            strvalue = objPLQ22LinuxSimulator.devStatus();
            TS_Tracer.WriteToDeviceLogs(strLogPath, "devStatus status ____ " + strvalue);

            return strvalue;
        }

        strcheckAutoStartStatus = checkAutoStartJson();
        if (strcheckAutoStartStatus.equalsIgnoreCase("MISC")) {
            strdDeviceStatus = "MISC";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "devStatus---> " + strdDeviceStatus);
            return strdDeviceStatus;
        }
        if (!straAutoStart.equalsIgnoreCase("Start")) {
            strdDeviceStatus = "NOT_CONFIGURED";
            TS_Tracer.WriteToDeviceLogs(strLogPath, "devStatus---> " + strdDeviceStatus);

            return strdDeviceStatus;
        } else {

            String portOpenStatus = configureDeviceEpson();

            if (portOpenStatus.equalsIgnoreCase("ONLINE") || portOpenStatus.equalsIgnoreCase("PORT_ALREADY_OPENED")) {

                intStatus = objwrapper.PrtStatus();
                System.out.println("Printer's Status: " + intStatus);
                TS_Tracer.WriteToDeviceLogs(strLogPath, "devStatus status ____ " + intStatus);
                switch (intStatus) {
                    case 0:
                        strdDeviceStatus = "ONLINE";
                        break;

                    case 10:
                        strdDeviceStatus = "PORT_IS_NOT_OPEN_YET";
                        break;
                    case 12:

                        strdDeviceStatus = "PRINTER_NOT_CONNECTED";
                        break;
                    case 13:
                        closeDeviceEpson();
                        intStatus = 13;
                        strdDeviceStatus = "OFFLINE";
                        break;
                    case 14:
                        closeDeviceEpson();
                        intStatus = 14;
                        strdDeviceStatus = "OFFLINE";
                        break;
                    case 15:
                        strdDeviceStatus = "OFFLINE";
                        break;
                    case 20:
                        strdDeviceStatus = "INVALID_PARAMETER";
                        break;
                    case 21:
                        strdDeviceStatus = "PARAMETER_NOT_SUPPORTED";
                        break;
                    case 22:
                        strdDeviceStatus = "INSUFFICIENT_BUFFER_SIZE";
                        break;
                    case 30:
                        strdDeviceStatus = "PRINTER_BUSY";
                        break;
                    case 31:
                        strdDeviceStatus = "OFFLINE";
                        break;
                    case 32:
                        strdDeviceStatus = "COVER_OPEN";
                        break;
                    case 33:
                        strdDeviceStatus = "PAPER_JAM";
                        break;
                    case 39:
                        strdDeviceStatus = "UNKNOWN_ERROR";
                        intunknownErrorCounter = intunknownErrorCounter + 1;
                        break;
                    default:
                        strdDeviceStatus = "UNKNOWN_ERROR";
                        intunknownErrorCounter = intunknownErrorCounter + 1;
                        break;

                }

                if (strdDeviceStatus.equals("COVER_OPEN")) {
                    intStatus = objwrapper.PrtRestart();
                    closeDeviceEpson();
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~COVER_OPEN Printer Restart~~~~~~~1");
                    strdDeviceStatus = "COVER_OPEN";

                } else if (intunknownErrorCounter >= 3) {

                    intStatus = objwrapper.PrtRestart();
                    closeDeviceEpson();
                    strdDeviceStatus = "UNKNOWN_ERROR";
                    intunknownErrorCounter = 0;
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "~~~~~~~UNKNOWN_ERROR Printer Restart~~~~~~~");
                }

            } else {
                System.out.println("devStatus");

                TS_Tracer.WriteToDeviceLogs(strLogPath, "configureDevice---> " + portOpenStatus);

            }

            switch (intStatus) {
                case 13:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "devStatus---> " + strdDeviceStatus + " Actual devStatus--> " + " WRITE_PORT_ERROR");
                    break;
                case 14:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "devStatus---> " + strdDeviceStatus + " Actual devStatus--> " + " READ_PORT_ERROR");
                    break;
                default:
                    TS_Tracer.WriteToDeviceLogs(strLogPath, "devStatus---> " + strdDeviceStatus);
                    break;
            }

            return strdDeviceStatus;
        }

    }

}
