/**
 *
 * @author prashant
 */ 
package in.co.technocrafts;

import java.io.File;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SmartPlugDeviceTS {

    String strAuto_Start;

    String strDevice_Simulator_json;
    String strTrace_Path;
    String strAcuConfigFile;
    String strPort_ID;
 public   String version;
    SmartPlugSimulator objSmartPlugSimulator;
    SmartPlug_Device objSmartPlug_Grayhats_Device;
    String strNormalReset;
    String strpathDirctory = new String("/home/TS/sp_smartPlug/");

    public SmartPlugDeviceTS() {
        this.strAuto_Start = new String("");

        this.strTrace_Path = new String("");
        this.strPort_ID = new String("");

        this.objSmartPlugSimulator = new SmartPlugSimulator();

        checkAutoStartJson();
        this.objSmartPlug_Grayhats_Device = new SmartPlug_Device();
        this.objSmartPlug_Grayhats_Device.strPort_ID = new String(this.strPort_ID);
        
            TS_Tracer.WriteToLogs(this.strTrace_Path, "Auto_Start--->  " + strAuto_Start);
            TS_Tracer.WriteToLogs(this.strTrace_Path, "Port--->  " + strPort_ID);
            TS_Tracer.WriteToLogs(this.strTrace_Path, "NormalReset--->  " + strNormalReset);
            TS_Tracer.WriteToLogs(this.strTrace_Path, "Version--->  " + version);

    }

    public String checkAutoStartJson() {

        boolean flag = false;

        try {

            File filehomeConfig = new File(strpathDirctory + "acu/config.json");
            this.strDevice_Simulator_json = strpathDirctory + "data/Device_Simulator.json";
            if (filehomeConfig.exists()) {
                flag = true;
            }

            if (flag == false) {
                throw new Exception();
            }

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(filehomeConfig));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject jsonObjACU_Config_File = (JSONObject) jsonObject.get("ACU_Config_File");
            JSONObject jsonObjSmart_plug = (JSONObject) jsonObjACU_Config_File.get("Smart_Plug");
            JSONObject jsonObjTracingUtility_Config = (JSONObject) jsonObjACU_Config_File.get("TracingUtility_Config");

            this.strAuto_Start = (String) jsonObjSmart_plug.get("Auto_Start");
            this.strPort_ID = (String) jsonObjSmart_plug.get("Port");
            this.strNormalReset = (String) jsonObjSmart_plug.get("NormalReset");

            this.strTrace_Path = (String) jsonObjTracingUtility_Config.get("Trace_Path");
             version= (String) jsonObjSmart_plug.get("Version");

            
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Do Not Configured";
    }

    public String offSmartPlug() {
        String strvalue = new String("");
        if (this.objSmartPlugSimulator.getSimulatorMode(this.strDevice_Simulator_json).equalsIgnoreCase("true")) {
            strvalue = this.objSmartPlugSimulator.setSwitchOff(this.strDevice_Simulator_json);
        } else if (this.strAuto_Start.equalsIgnoreCase("Start")) {
            strvalue = this.objSmartPlug_Grayhats_Device.offSmartPlugDevice();
            TS_Tracer.WriteToLogs(this.strTrace_Path, "from Device  setSwitchOff()--->  " + strvalue);
            if (strvalue.contains("SWITCH TURNED OFF")) {
                strvalue = new String("SWITCH TURNED OFF");
            } else if (strvalue.contains("SWITCH TURNED ON")) {
                strvalue = new String("SWITCH TURNED ON");
            } else {
                strvalue = new String("OFFLINE");
            }
        } else {
            strvalue = "Do Not Configured";
        }
        
        TS_Tracer.WriteToLogs(this.strTrace_Path, " SwitchOff()--->  " + strvalue);
        return strvalue;
    }

    public String onSmartPlug() {
        String strvalue = new String("");
        if (this.objSmartPlugSimulator.getSimulatorMode(this.strDevice_Simulator_json).equalsIgnoreCase("true")) {
            strvalue = this.objSmartPlugSimulator.setSwitchOn(this.strDevice_Simulator_json);
        } else if (this.strAuto_Start.equalsIgnoreCase("Start")) {
            strvalue = this.objSmartPlug_Grayhats_Device.onSmartPlugDevice();
            TS_Tracer.WriteToLogs(this.strTrace_Path, "from Device  setSwitchOn()--->  " + strvalue);
            if (strvalue.contains("SWITCH TURNED OFF")) {
                strvalue = new String("SWITCH TURNED OFF");
            } else if (strvalue.contains("SWITCH TURNED ON")) {
                strvalue = new String("SWITCH TURNED ON");
            } else {
                strvalue = new String("OFFLINE");
            }
        } else {
            strvalue = "Do Not Configured";
        }
        
        TS_Tracer.WriteToLogs(this.strTrace_Path, " SwitchOn()--->  " + strvalue);
        return strvalue;
    }

    public String[] statusSmartPlug() {
        String[] strvalue = {"OFFLINE", "OFFLINE", "OFFLINE"};
        if (this.objSmartPlugSimulator.getSimulatorMode(this.strDevice_Simulator_json).equalsIgnoreCase("true")) {
            strvalue = this.objSmartPlugSimulator.getSwitchStatus(this.strDevice_Simulator_json);
        } else if (this.strAuto_Start.equalsIgnoreCase("Start")) {
            final String[] returnvalue = this.objSmartPlug_Grayhats_Device.getSmartPlugDeviceStatus();
            strvalue[0] = returnvalue[0];
            strvalue[1] = returnvalue[1];
            strvalue[2] = returnvalue[2];
        } else {
            strvalue[0] = "Do Not Configured";
            strvalue[2] = (strvalue[1] = "Do Not Configured");
        }
        
        TS_Tracer.WriteToLogs(this.strTrace_Path, " getSwitchStatus()--->  Switch status " + strvalue[0] + "  Version " + strvalue[1] + " Device id " + strvalue[2]);
        return strvalue;
    }

    public String resetSmartPlug() {
        String strvalue = new String("");
        if (strNormalReset.equalsIgnoreCase("1")) {
            if (this.objSmartPlugSimulator.getSimulatorMode(this.strDevice_Simulator_json).equalsIgnoreCase("true")) {
                strvalue = this.objSmartPlugSimulator.setSwitchReset(this.strDevice_Simulator_json);
            } else if (this.strAuto_Start.equalsIgnoreCase("Start")) {
                strvalue = this.objSmartPlug_Grayhats_Device.resetSmartPlugDevice();
                TS_Tracer.WriteToLogs(this.strTrace_Path, "from Device  setSwitchReset()--->  " + strvalue);
                if (strvalue.contains("SWITCH TURNED OFF")) {
                    strvalue = new String("SWITCH TURNED OFF");
                } else if (strvalue.contains("SWITCH TURNED ON")) {
                    strvalue = new String("SWITCH TURNED ON");
                } else {
                    strvalue = new String("OFFLINE");
                }
            } else {
                strvalue = "Do Not Configured";
            }
            
        TS_Tracer.WriteToLogs(this.strTrace_Path, " SwitchReset()--->  " + strvalue);

        } else {
            strvalue = reset12SmartPlug();
        }
        return strvalue;
    }

    public String reset12SmartPlug() {
        String strvalue = new String("");
        if (this.objSmartPlugSimulator.getSimulatorMode(this.strDevice_Simulator_json).equalsIgnoreCase("true")) {
            strvalue = this.objSmartPlugSimulator.setSwitchReset12(this.strDevice_Simulator_json);
        } else if (this.strAuto_Start.equalsIgnoreCase("Start")) {
            strvalue = this.objSmartPlug_Grayhats_Device.resetSmartPlugDevice12();
            TS_Tracer.WriteToLogs(this.strTrace_Path, "from Device  setSwitchReset12()--->  " + strvalue);
            if (strvalue.contains("SWITCH TURNED OFF")) {
                strvalue = new String("SWITCH TURNED OFF");
            } else if (strvalue.contains("SWITCH TURNED ON")) {
                strvalue = new String("SWITCH TURNED ON");
            } else {
                strvalue = new String("OFFLINE");
            }
        } else {
            strvalue = "Do Not Configured";
        }
        
        TS_Tracer.WriteToLogs(this.strTrace_Path, " SwitchReset()--->  " + strvalue);
        return strvalue;
    }

    
        public static void main(String args[]) {
            
            SmartPlugDeviceTS objSmartPlugDeviceTS=new SmartPlugDeviceTS();
            objSmartPlugDeviceTS.checkAutoStartJson();
            objSmartPlugDeviceTS.reset12SmartPlug();
        }
}
