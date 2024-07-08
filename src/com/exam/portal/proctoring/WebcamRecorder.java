package com.exam.portal.proctoring;

import com.exam.portal.models.Image;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

public class WebcamRecorder {
    private long recordTime;
    private File file;

    private volatile int totalCheck;   //total images sends to server for checking.
    private volatile int cheatFound;   //total times server false in not cheat status.
    private volatile boolean isThreadRunning;

    public WebcamRecorder(){
        try{
            file = File.createTempFile("webcam-recording",".mp4");
            file.deleteOnExit();
            //file = new File("E:/webcam.mp4");
        }catch (Exception e){
            e.printStackTrace();
        }
        isThreadRunning = false;
        totalCheck = 0;
        cheatFound = 0;
    }

    public WebcamRecorder(int recordTime) {
        this();
        this.recordTime = (long) recordTime *60*1000;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    //returns true if webcam is working otherwise return false.
    public boolean isWebcamAvailable(){
        Webcam webcam = Webcam.getDefault();
        return webcam != null;
    }

    //start recording student's video and sending image to server for proctoring.
    public void start(){
        isThreadRunning = true;
        new Thread(()->{
            Webcam webcam = Webcam.getDefault();
            webcam.open(true);
            Server server = ServerHandler.getInstance();
            System.out.println("Recording started");
            while (isThreadRunning){
                try {
                    BufferedImage image = webcam.getImage();
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ImageIO.write(image,"jpg",os);
                    String bytes = Base64.getEncoder().encodeToString(os.toByteArray());
                    new Thread(()->{
                        Image image1 = new Image();
                        image1.setStudentId(StudentController.student.getStudentId());
                        image1.setBytes(bytes);
                        if(!server.getProctorResult(image1))
                            cheatFound++;
                        totalCheck++;
                    }).start();

                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            webcam.close();
        }).start();
    }

    public void finish(){
        isThreadRunning = false;
        System.out.println("finished");
    }

    public boolean getCheatStatus(){
       return cheatFound>5;
    }

    public void startRecording(){
        new Thread(()->{
            try {
                Thread.sleep(recordTime);      //sleeping this thread for recordTime milliseconds after that stopping recording.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }).start();

        this.start();
    }
}
