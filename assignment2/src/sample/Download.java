package sample;
import java.io.*;
import java.net.*;
/**
 * Created by 100490365 on 3/27/2016.
 */
public class Download {

    private File f;
    public void run(File f){
        try{
            ServerSocket soc=new ServerSocket(8080);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Download(File file){
        this.f=file;
    }
}
