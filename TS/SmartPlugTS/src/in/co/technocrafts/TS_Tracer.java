/**
 *
 * @author prashant
 */

package in.co.technocrafts;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;

public class TS_Tracer
{
    public static String tracePath;
    
    public static void main(final String[] args) {
       }
    
    public static boolean WriteToLogs(final String pathname, final String Data) {
        try {
            final String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            final String filename = pathname + date + ".txt";
            final File f = new File(pathname);
            final File parentpath = f.getParentFile();
            final File fDirectory = new File(TS_Tracer.tracePath);
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
    
    static {
        TS_Tracer.tracePath = "";
    }
}
