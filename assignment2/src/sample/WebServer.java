package sample;

import java.io.*;
import java.net.*;

public class WebServer extends Thread {

    private File f;
    public void run(File f){
        try{
            ServerSocket soc=new ServerSocket(8080);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public WebServer(File file){
        this.f=file;
    }

    public static void main(String[] args){
        FileInputStream inputStream=null;
        BufferedInputStream bufferedinput=null;
        OutputStream outputStream=null;
        ServerSocket soc=null;

        try{
            soc=new ServerSocket(8080);
            while(true){
                System.out.println("waiting");
                Socket socket=soc.accept();

                System.out.println("Connection secure "+socket);

                File testFiles=new File("./TestFile");

                for(File inputFile:testFiles.listFiles()){
                    //initialzing server to send from client
                    byte[] buffer= new byte[(int)inputFile.length()];
                    inputStream=new FileInputStream(inputFile);
                    bufferedinput=new BufferedInputStream(inputStream);
                    bufferedinput.read(buffer, 0, buffer.length);
                    outputStream=socket.getOutputStream();
                    System.out.println("Sending "+inputFile.getName()+" ("+buffer.length+" bytes)");
                    outputStream.write(buffer, 0, buffer.length);
                    System.out.println("done");
                    outputStream.flush();
                }
                bufferedinput.close();
                outputStream.close();
                socket.close();
                soc.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}