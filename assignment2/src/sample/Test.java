package sample;

import java.io.File;

/**
 * Created by 100490365 on 3/27/2016.
 */
public class Test {
    private File f;
    private String fname;

    public Test (File f, String fname){
        this.f = f;
        this.fname = fname;
    }
    public File getF() {
        return f;
    }

    public void setF(File f) {
        this.f = f;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}
