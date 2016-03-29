package sample;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.security.ntlm.Server;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Observable;

public class Main extends Application {
    private static String label, directory;
    private int bufferSize = 1024;
    public File f;
    private int countS=0;
    private int countC=0;
    private Test choosefile=null;

    @Override
    public void start(Stage primaryStage) throws Exception{

        //setting up client column
        TableView user = new TableView();
        TableColumn<Test,String> ClientCol = new TableColumn<>();
        ClientCol.setMinWidth(400);
        ClientCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        user.getColumns().add(ClientCol);

        //setting up server column
        TableView serverview = new TableView();
        TableColumn<Test,String> serverCol = new TableColumn<>();
        serverCol.setMinWidth(400);
        serverCol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        serverview.getColumns().add(serverCol);

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("File Sharer");
        BorderPane layout=new BorderPane();
        directory = "TestFile";
        File TestFile = new File("./"+directory);



        ObservableList<Test> tFiles = FXCollections.observableArrayList();
        // add files
        for(File inputFile:TestFile.listFiles()){
            Test tfile = new Test(inputFile,inputFile.getName());
            tFiles.add(countC,tfile);
            countC++;

        }
        user.setItems(tFiles);
        user.setEditable(true);

        File server = new File("./Server");



        ObservableList<Test> sFiles = FXCollections.observableArrayList();
        // add files
        for(File inputFile:server.listFiles()){
            Test tfile = new Test(inputFile,inputFile.getName());
            sFiles.add(countS,tfile);
            countS++;


        }
        serverview.setItems(sFiles);
        serverview.setEditable(true);

        // letting user select which document he wants to send over from client
        user.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (user.getSelectionModel().getSelectedItem()!=null){
                    choosefile=(Test) newValue;
                    f=choosefile.getF();
                    System.out.println(observable.getValue());
                }
            }
        });

        // giving user the option to select which file is either uploaded or downloaded
        serverview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (serverview.getSelectionModel().getSelectedItem()!=null){
                    choosefile=(Test) newValue;
                    f=choosefile.getF();
                    System.out.println(choosefile.getFname());
                }
            }
        });
        //creating button upload
        GridPane select = new GridPane();
        Button upload = new Button("Upload");
        upload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(f!=null){
                    try{
                        byte[] buf = new byte[1];
                        int bytesRead;
                        Socket soc=new Socket("localhost",8080);
                        InputStream inputStream=soc.getInputStream();
                        ByteArrayOutputStream arrayOutputStream=new ByteArrayOutputStream();
                        if (inputStream!=null){
                            FileOutputStream outputStream=null;
                            BufferedOutputStream bufferedoutput =null;
                            outputStream=new FileOutputStream(f);
                            bufferedoutput=new BufferedOutputStream(outputStream);
                            bytesRead=inputStream.read(buf,0,buf.length);
                            do{
                                arrayOutputStream.write(buf);
                                bytesRead=inputStream.read(buf);
                            }while(bytesRead!=-1);
                            bufferedoutput.write(arrayOutputStream.toByteArray());
                            System.out.println("File"+f.getName()+" has been sent");

                            Test testFile=new Test(f, f.getName());
                            sFiles.add(countS,testFile);
                            countS++;
                            tFiles.remove(testFile);


                            f=new File("./Server/"+f.getName());//move File



                            //close everything
                            bufferedoutput.flush();
                            inputStream.close();
                            soc.close();


                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("No file selected");
                }

            }
        });

        Button download = new Button ("Download");
        download.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (f!=null){
                    WebServer webServer=new WebServer(f);

                    int br=0;
                    int present=0;
                    FileOutputStream outputStream=null;
                    BufferedOutputStream bufferedoutput=null;
                    Socket socket=null;
                    try{
                        socket=new Socket("localhost",8080);
                        System.out.println("Connecting...");


                        Test tFile=new Test(f,f.getName());
                        tFiles.add(countC,tFile);
                        countC++;
                        sFiles.remove(tFile);
                        present=0;
                        br=0;
                        byte[] buffer=new byte[6022386];
                        InputStream inputStream=socket.getInputStream();
                        outputStream=new FileOutputStream(f);
                        bufferedoutput=new BufferedOutputStream(outputStream);
                        br=inputStream.read(buffer,0,buffer.length);
                        present=br;

                        do{
                            br=inputStream.read(buffer,present,buffer.length-present);
                            if(br>=0) present+=br;
                        }while(br>-1);
                        bufferedoutput.write(buffer,0,buffer.length);
                        bufferedoutput.flush();
                        System.out.println("File" +f.getName() + " has been downloaded");
                        f=new File("./TestFile/"+f.getName());//move the file



                        outputStream.close();
                        bufferedoutput.close();
                        socket.close();


                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }else{
                    System.out.println("No file selected");
                }

            }
        });

        select.add(upload,0,0);
        select.add(download,1,0);

        layout.setTop(select);
        layout.setLeft(user);
        layout.setRight(serverview);


        primaryStage.setScene(new Scene(layout, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args){


        launch(args);
    }
}
