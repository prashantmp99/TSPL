/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package axistsm;

import com.ts.axis.middleware.AxisTSMJar;

/**
 *
 * @author Akshay
 */
public class AxisTSM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        AxisTSMJar objAxisTSMJar=new AxisTSMJar();
     //   objAxisTSMJar.TerminalAuthentication("10.0.1.4", "TCS1002");
       // objAxisTSMJar.TransactionRequestMsg("", "", "", "", "", "", "", "", "BLR");
       objAxisTSMJar.TransactionRequestMsg("", "", "270320192233", "" , "", "ICI        0004    914010006330150", "KSKB1914010006330150", "1","BLR");
     // objAxisTSMJar.TransactionRequestMsg("", "", "270320192233", "" , "", "ICI        0004    914010006330144", "2017062420201215                                                  20AB20170624S78831736  9320170624161139         -20992.8", "1","CRD");
       // objAxisTSMJar.TransactionRequestMsg("", "", "270320192233", "" , "", "ICI        0004    914010006330144", "KSKB2S78831736  9320170624161139        -20992.80 0 4 1", "1","ACK");
      // objAxisTSMJar.TransactionRequestMsgIssuance("", "", "270320192233", "" , "", "211        111     914010006330150", "KSKB5914010006330150", "1","Barcode_Mapping");

       // objAxisTSMJar.TransactionRequestMsgIssuance("", "", "270320192233", "" , "", "211        111     914010006330150", "KSKB3914010006330150", "1","First_Page");


        // objAxisTSMJar.TransactionRequestMsgIssuance("", "", "270320192233", "" , "", "211        111     914010006330150", "KSKB4", "1","Account_Validation");



        
    }
    
}
