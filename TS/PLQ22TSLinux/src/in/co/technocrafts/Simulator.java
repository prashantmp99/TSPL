/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.technocrafts;

/**
 *
 * @author prashant
 */
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Simulator {

    public String getsimulator_mode() {

        try {
            System.gc();
            Thread.sleep(100);
        } catch (Exception e) {

        }

        return getJsonNodeValue("simulator_mode");
    }

    public String prtLoad() {

        return getJsonNodeValue("prtLoad");
    }

    public String eject() {
        return getJsonNodeValue("eject");
    }

    public String resetDevice() {
        return getJsonNodeValue("resetDevice");
    }

    public String devStatus() {
        return getJsonNodeValue("devStatus");
    }

    public String papStatus() {
        return getJsonNodeValue("papStatus");
    }

    public String printPB() {
        return getJsonNodeValue("printPB");
    }

    public String scanBarcode() {
        return getJsonNodeValue("scanBarcode");
    }

    public String PrintImage() {
        return getJsonNodeValue("PrintImage");
    }

    public String[] scanPB(String str) {
        String[] returnScanPB = getJsonNodeValue("scanPB").split(",");
        return returnScanPB;
    }

    public String getJsonNodeValue(String jsonNodeName) {
        String returnNodeValue = new String("");

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("/home/TS/sp_plq22/data/Device_Simulator.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject jsonObjSimulator = (JSONObject) jsonObject.get("simulator");
            JSONObject jsonObjprinter = (JSONObject) jsonObjSimulator.get("printer");
            JSONObject jsonObjplq22 = (JSONObject) jsonObjprinter.get("plq22");

            returnNodeValue = new String((String) jsonObjplq22.get(jsonNodeName));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnNodeValue.trim();

    }

    public static void main(String args[]) {

        Simulator objPLQ22LinuxSimulator = new Simulator();
        if (objPLQ22LinuxSimulator.getsimulator_mode().equalsIgnoreCase("true")) {
        }

    }
}
