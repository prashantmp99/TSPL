/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.technocrafts;

import java.io.File;

/**
 *
 * @author testbuild
 */
public class All_FileUtility {
    
    public String[] getcontentsInDirectory(String directoryPath,String traceFilePath)
    {
        try
        {
        File fileDirectoryPath = new File(directoryPath);
      //List of all files and directories
      String contents[] = fileDirectoryPath.list();
      return contents;
        }
        catch(Exception e)
        {
            
        }
        
         return new String[0];  
    }
    
    public boolean deletefilesInDirectory(String directoryPath,String traceFilePath)
    {
        try
        {
          File fileDirectoryPath = new File(directoryPath);

         for(File file: fileDirectoryPath.listFiles()) 
         {
        file.delete();      
         }

        return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
}
