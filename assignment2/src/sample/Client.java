package sample;

import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args){
        File TestFile=new File("./TestFile");
        try{
            ServerSocket soc=null;
            Socket sock=null;
            BufferedOutputStream bufferedoutput=null;
            while(true){
                System.out.println("waiting");
                soc=new ServerSocket(8080);
                sock=soc.accept();
                bufferedoutput=new BufferedOutputStream(sock.getOutputStream());
                for(File inputFile:TestFile.listFiles()){
                    byte[] buf=new byte[(int)inputFile.length()];
                    FileInputStream inputStream=new FileInputStream(inputFile);
                    BufferedInputStream bufferedinput=new BufferedInputStream(inputStream);
                    bufferedinput.read(buf, 0,buf.length);
                    bufferedoutput.write(buf,0,buf.length);
                    System.out.println("File: "+inputFile.getName()+" sent");
                }
                bufferedoutput.flush();
                bufferedoutput.close();
                sock.close();

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
