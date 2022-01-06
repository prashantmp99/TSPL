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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TS_Tracer {

    public static String tracePath = "";

    public static void main(String[] args) {
    }

    public static boolean WriteToDeviceLogs(String pathname, String Data) {
        try {


                tracePath = pathname;
                String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String filename = pathname + date + ".txt";
                File f = new File(pathname);
                File parentpath = f.getParentFile();

                File fDirectory = new File(tracePath);

          
                File file = new File(filename);
                file.createNewFile();
                FileWriter fw = new FileWriter(file, true);
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
                Date now = new Date();
                String strDate = sdfDate.format(now);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("\n" + strDate + "\t" + Data);
                bw.close();
            //}
            return true;
        }catch (Exception ex) {

            return false;
        }
    }
}
