// Copyright 2012 Google Inc. All Rights Reserved.
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class FileUtil {

  public static String readFileAsString(String filePath) throws IOException
  {
      StringBuffer fileData = new StringBuffer(1000);
      BufferedReader reader = new BufferedReader(
              new FileReader(filePath));
      char[] buf = new char[1024];
      int numRead=0;
      while((numRead=reader.read(buf)) != -1){
          String readData = String.valueOf(buf, 0, numRead);
          fileData.append(readData);
          buf = new char[1024];
      }
      reader.close();
      return fileData.toString();
  }
  
  public static void createFile(String path) throws IOException
  {
      File file = new File(path);
      file.createNewFile();
  }
  
  public static String createTempFile(String prefix, String suffix) throws IOException
  {
    return File.createTempFile(prefix, suffix).getAbsolutePath();
  }
  
  
  public static void copyfile(String srFile, String dtFile){
    try{
      File f1 = new File(srFile);
      File f2 = new File(dtFile);
      InputStream in = new FileInputStream(f1);
      OutputStream out = new FileOutputStream(f2);

      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0){
         out.write(buf, 0, len);
      }
      in.close();
      out.close();
      System.out.println("File copied.");
    }
    catch(FileNotFoundException ex){
      System.out.println(ex.getMessage() + " in the specified directory.");
      System.exit(0);
    }
    catch(IOException e){
      System.out.println(e.getMessage());  
   }
  }
  
  public static void save(String path, String str) 
  {
      try{
          File file = new File (path);
          file.createNewFile();
          BufferedWriter writer = new BufferedWriter(new FileWriter(path));
          writer.write(str);
          writer.close();
      }catch (Exception e)
      {
          e.printStackTrace();
      }
  }
  
   public static void delete(String file) 
   {
      String fileName = file;
      File f = new File(fileName);
      if (!f.exists())
        throw new IllegalArgumentException(
            "Delete: no such file or directory: " + fileName);
      if (!f.canWrite())
        throw new IllegalArgumentException("Delete: write protected: "
            + fileName);
      if (f.isDirectory()) {
        String[] files = f.list();
        if (files.length > 0)
          throw new IllegalArgumentException(
              "Delete: directory not empty: " + fileName);
      }
      boolean success = f.delete();
          if (!success)
            throw new IllegalArgumentException("Delete: deletion failed");
   }
   
   public static void deleteFolder(String path) {
          File folder = new File(path);
          File[] files = folder.listFiles();
          if(files!=null) { 
              for(File f: files) {
                  if(f.isDirectory()) {
                      deleteFolder(f.getAbsolutePath());
                  } else {
                      f.delete();
                  }
              }
          }
          folder.delete();
      }

}
