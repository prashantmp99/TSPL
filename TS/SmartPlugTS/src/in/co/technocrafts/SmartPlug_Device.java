
/**
 *
 * @author prashant
 */

package in.co.technocrafts;

import java.util.Enumeration;
import gnu.io.SerialPort;
import java.io.InputStream;
import gnu.io.CommPortIdentifier;
import java.io.OutputStream;

public class SmartPlug_Device
{
    OutputStream ObjOutputToPort;
    CommPortIdentifier ObjPortIdentifier;
    InputStream objInputFromPort;
    SerialPort objserialPort;
    String strPort_ID;
    int statusConnectionTOPort;
    
    public SmartPlug_Device() {
        this.strPort_ID = new String("");
        this.statusConnectionTOPort = 0;
        System.gc();
    }
    
    public int ConnectionToSmartPlugPort() {
        try {
            if (this.statusConnectionTOPort == 1) {
                return 1;
            }
            final Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                //final CommPortIdentifier portId = portList.nextElement();
                portList.nextElement();
                //if (portId.getPortType() == 2) {}
            }
            this.ObjPortIdentifier = CommPortIdentifier.getPortIdentifier(this.strPort_ID);
            if (this.ObjPortIdentifier.isCurrentlyOwned()) {
                return -1;
            }
            System.out.println(this.strPort_ID);
            (this.objserialPort = (SerialPort)this.ObjPortIdentifier.open(this.getClass().getName(), 200)).setSerialPortParams(115200, 8, 1, 0);
            this.ObjOutputToPort = this.objserialPort.getOutputStream();
            this.objInputFromPort = this.objserialPort.getInputStream();
            this.objserialPort.enableReceiveTimeout(10000);
            return this.statusConnectionTOPort = 1;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.statusConnectionTOPort = 0;
            return -1;
        }
    }
    
    public String resetSmartPlugDevice() {
        if (this.ConnectionToSmartPlugPort() == 1) {
            String s = "";
            try {
                final byte[] status = { 83, 87, 45, 82, 83, 84, 10 };
                final byte[] mBytesIn = new byte[50];
                this.ObjOutputToPort.write(status);
                this.ObjOutputToPort.flush();
                try {
                    Thread.sleep(2000L);
                }
                catch (Exception ex) {}
                this.objInputFromPort.read(mBytesIn);
                StringBuffer stemp = new StringBuffer();
                for (int i = 0; i < 46; ++i) {
                    if (Math.abs(mBytesIn[i]) > 31) {
                        stemp.append((char)((mBytesIn[i] > 0) ? mBytesIn[i] : (256 + mBytesIn[i])));
                    }
                    else {
                        stemp.append(mBytesIn[i]);
                    }
                }
                s = stemp.toString();
                final byte[] mBytesIn2 = new byte[50];
                try {
                    Thread.sleep(8000L);
                }
                catch (Exception ex2) {}
                this.objInputFromPort.read(mBytesIn2);
                stemp = new StringBuffer();
                for (int j = 0; j < 46; ++j) {
                    if (Math.abs(mBytesIn2[j]) > 31) {
                        stemp.append((char)((mBytesIn2[j] > 0) ? mBytesIn2[j] : (256 + mBytesIn2[j])));
                    }
                    else {
                        stemp.append(mBytesIn2[j]);
                    }
                }
                s = stemp.toString();
            }
            catch (Exception e) {
                try {
                    this.objserialPort.close();
                    this.statusConnectionTOPort = 0;
                }
                catch (Exception ex3) {}
                e.printStackTrace();
            }
            return s.trim();
        }
        try {
            this.objserialPort.close();
            this.statusConnectionTOPort = 0;
        }
        catch (Exception ex4) {}
        return "OFFLINE";
    }
    
    public String resetSmartPlugDevice12() {
        if (this.ConnectionToSmartPlugPort() == 1) {
            String s = "";
            try {
                System.out.print("Inside reset Device");
                final byte[] status = { 83, 87, 45, 82, 83, 84, 49, 10 };
                final byte[] mBytesIn = new byte[50];
                this.ObjOutputToPort.write(status);
                this.ObjOutputToPort.flush();
                try {
                    Thread.sleep(2000L);
                }
                catch (Exception ex) {}
                System.out.print(" reset DONE");
                this.objInputFromPort.read(mBytesIn);
                StringBuffer stemp = new StringBuffer();
                for (int i = 0; i < 46; ++i) {
                    if (Math.abs(mBytesIn[i]) > 31) {
                        stemp.append((char)((mBytesIn[i] > 0) ? mBytesIn[i] : (256 + mBytesIn[i])));
                    }
                    else {
                        stemp.append(mBytesIn[i]);
                    }
                }
                s = stemp.toString();
                final byte[] mBytesIn2 = new byte[50];
                try {
                    Thread.sleep(12000L);
                }
                catch (Exception ex2) {}
                System.out.print(" reset DONE");
                this.objInputFromPort.read(mBytesIn2);
                stemp = new StringBuffer();
                for (int j = 0; j < 46; ++j) {
                    if (Math.abs(mBytesIn2[j]) > 31) {
                        stemp.append((char)((mBytesIn2[j] > 0) ? mBytesIn2[j] : (256 + mBytesIn2[j])));
                    }
                    else {
                        stemp.append(mBytesIn2[j]);
                    }
                }
                s = stemp.toString();
                System.out.println(s);
            }
            catch (Exception e) {
                try {
                    this.objserialPort.close();
                    this.statusConnectionTOPort = 0;
                }
                catch (Exception ex3) {}
                System.out.print("********");
                e.printStackTrace();
            }
            return s.trim();
        }
        try {
            this.objserialPort.close();
            this.statusConnectionTOPort = 0;
        }
        catch (Exception ex4) {}
        return "OFFLINE";
    }
    
    public String onSmartPlugDevice() {
        if (this.ConnectionToSmartPlugPort() == 1) {
            String s = "";
            try {
                final byte[] status = { 83, 87, 49, 45, 49, 10 };
                final byte[] mBytesIn = new byte[50];
                System.out.println("*******");
                for (int i = 0; i < status.length; ++i) {
                    System.out.print(" " + status[i]);
                }
                System.out.println("*******");
                this.ObjOutputToPort.write(status);
                this.ObjOutputToPort.flush();
                try {
                    Thread.sleep(2000L);
                }
                catch (Exception ex) {}
                this.objInputFromPort.read(mBytesIn);
                final StringBuffer stemp = new StringBuffer();
                for (int j = 0; j < 46; ++j) {
                    if (Math.abs(mBytesIn[j]) > 31) {
                        stemp.append((char)((mBytesIn[j] > 0) ? mBytesIn[j] : (256 + mBytesIn[j])));
                    }
                    else {
                        stemp.append(mBytesIn[j]);
                    }
                }
                s = stemp.toString();
                System.out.println("\n" + s);
            }
            catch (Exception e) {
                try {
                    this.objserialPort.close();
                    this.statusConnectionTOPort = 0;
                }
                catch (Exception ex2) {}
                System.out.print("********");
                e.printStackTrace();
            }
            return s.trim();
        }
        try {
            this.objserialPort.close();
            this.statusConnectionTOPort = 0;
        }
        catch (Exception ex3) {}
        return "OFFLINE";
    }
    
    public String offSmartPlugDevice() {
        if (this.ConnectionToSmartPlugPort() == 1) {
            String s = "";
            try {
                final byte[] status = { 83, 87, 49, 45, 48, 10 };
                final byte[] mBytesIn = new byte[50];
                this.ObjOutputToPort.write(status);
                this.ObjOutputToPort.flush();
                try {
                    Thread.sleep(2000L);
                }
                catch (Exception ex) {}
                this.objInputFromPort.read(mBytesIn);
                final StringBuffer stemp = new StringBuffer();
                for (int i = 0; i < 46; ++i) {
                    System.out.println(mBytesIn[i]);
                    if (Math.abs(mBytesIn[i]) > 31) {
                        stemp.append((char)((mBytesIn[i] > 0) ? mBytesIn[i] : (256 + mBytesIn[i])));
                    }
                    else {
                        stemp.append(mBytesIn[i]);
                    }
                }
                s = stemp.toString();
                System.out.println("\n" + s);
            }
            catch (Exception e) {
                try {
                    this.objserialPort.close();
                    this.statusConnectionTOPort = 0;
                }
                catch (Exception ex2) {}
                System.out.print("********");
                e.printStackTrace();
            }
            return s.trim();
        }
        try {
            this.objserialPort.close();
            this.statusConnectionTOPort = 0;
        }
        catch (Exception ex3) {}
        return "OFFLINE";
    }
    
    public String[] getSmartPlugDeviceStatus() {
        if (this.ConnectionToSmartPlugPort() == 1) {
            String s = "";
            System.out.println("Inside statuss");
            try {
                final byte[] status = { 83, 87, 45, 83, 84, 65, 84, 10 };
                final byte[] mBytesIn = new byte[100];
                this.ObjOutputToPort.write(status);
                this.ObjOutputToPort.flush();
                try {
                    Thread.sleep(3000L);
                }
                catch (Exception ex) {}
                this.objInputFromPort.read(mBytesIn);
                final StringBuffer stemp = new StringBuffer();
                for (int i = 0; i < 100; ++i) {
                    if (Math.abs(mBytesIn[i]) > 31) {
                        stemp.append((char)((mBytesIn[i] > 0) ? mBytesIn[i] : (256 + mBytesIn[i])));
                    }
                    else {
                        stemp.append(mBytesIn[i]);
                    }
                }
                s = stemp.toString();
                System.out.println(s);
                final String delimiter = "1310";
                final String[] tempArray = s.split(delimiter);
                System.out.println("\n" + tempArray[5] + "" + tempArray[3] + "" + tempArray[4]);
                if (tempArray.length > 3) {
                    final String[] returnvalue = { tempArray[5].substring(tempArray[5].indexOf(":") + 1), tempArray[4].substring(tempArray[4].indexOf(":") + 1), tempArray[3].substring(tempArray[3].indexOf(":") + 1) };
                    return returnvalue;
                }
            }
            catch (Exception e) {
                try {
                    this.objserialPort.close();
                }
                catch (Exception ex2) {}
                System.out.print("********");
                e.printStackTrace();
            }
            final String[] returnvalue2 = { "OFFLINE", "OFFLINE", "OFFLINE" };
            return returnvalue2;
        }
        try {
            this.objserialPort.close();
        }
        catch (Exception ex3) {}
        final String[] returnvalue3 = { "OFFLINE", "OFFLINE", "OFFLINE" };
        return returnvalue3;
    }
}
