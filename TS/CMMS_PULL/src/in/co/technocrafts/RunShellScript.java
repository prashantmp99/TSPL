/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.technocrafts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import sun.tools.jar.CommandLine;

/**
 *
 * @author testbuild
 */
public class RunShellScript {
    
 

    public void runScript(String command,String traceFilePath){
                    TS_Tracer.WriteToDeviceLogs(traceFilePath, "command to run:  " + command);

        try
        {
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(command);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line=buf.readLine())!=null) {
        System.out.println(line);
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
   
    
    
    }
    
      public static void main(String args[])
      {
          RunShellScript objRunShellScript=new RunShellScript();
          objRunShellScript.runScript("sh /home/TS/CMMS/unzipfile//*.sh", "");
      }
    
}
