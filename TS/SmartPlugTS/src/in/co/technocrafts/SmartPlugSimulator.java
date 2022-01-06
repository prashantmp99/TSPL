/**
 *
 * @author prashant
 */ 

package in.co.technocrafts;


import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SmartPlugSimulator
{
    public String getSimulatorMode(final String fileName) {
        try {
            System.gc();
            Thread.sleep(100L);
        }
        catch (Exception ex) {}
        return this.getNodeValue(fileName, "Grayhats", "simulator_mode");
    }
    
    public String setSwitchOff(final String fileName) {
        return this.getNodeValue(fileName, "Grayhats", "SwitchOff");
    }
    
    public String setSwitchOn(final String fileName) {
        return this.getNodeValue(fileName, "Grayhats", "SwitchOn");
    }
    
    public String setSwitchReset(final String fileName) {
        return this.getNodeValue(fileName, "Grayhats", "SwitchReset");
    }
    
    public String setSwitchReset12(final String fileName) {
        return this.getNodeValue(fileName, "Grayhats", "SwitchReset12");
    }
    
    public String[] getSwitchStatus(final String fileName) {
        return this.getNodeValue(fileName, "Grayhats", "SwitchStatus").split(",");
    }
    
    public String getNodeValue(final String fileName, final String nodeNameP, final String nodeNameS) {
        String returnNodeValue = new String("");
        try {
            
            
            

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(fileName));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject jsonObjSimulator = (JSONObject) jsonObject.get("simulator");
            JSONObject jsonObjSmart_plug = (JSONObject) jsonObjSimulator.get("Smart_Plug");
            JSONObject jsonObjGrayhats = (JSONObject) jsonObjSmart_plug.get(nodeNameP);

            returnNodeValue = (String) jsonObjGrayhats.get(nodeNameS);
            
        }
        catch (Exception e) {
            returnNodeValue = new String("Json Error");
        }
        System.out.println(returnNodeValue);
        return returnNodeValue.trim();
    }
}
