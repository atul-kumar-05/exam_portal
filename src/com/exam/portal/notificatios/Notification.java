package com.exam.portal.notificatios;

import com.exam.portal.models.Credentials;
import com.exam.portal.models.Exam;
import com.exam.portal.models.ExamUpdate;
import com.exam.portal.models.Student;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Notification {
    private final String title;
    private final String message;

    public Notification(String title,String message){
        this.title = title;
        this.message = message;
    }

    //this function shows the notification with the help of system tray.
    private void show(TrayIcon.MessageType type){
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image,"Tray Demo");
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
            trayIcon.setToolTip("Exam Portal");
            trayIcon.displayMessage(title,message,type);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void runThread(){
        new Thread(()->{
            while (true){
                try {
                    //sleeping thread for 5 seconds.
                    Thread.sleep(5000);
                    //fetching credentials from file.
                    String dirPath = File.listRoots()[1]+"\\Exam_Portal\\";
                    String filePath = "cd.ep";

                    if(Files.notExists(Paths.get(dirPath+filePath)))
                        return;

                    ObjectInputStream is = new ObjectInputStream(new FileInputStream(dirPath+filePath));
                    Credentials credentials = (Credentials) is.readObject();
                    is.close();

                    if(credentials.getType().equals("teacher"))
                        return;

                    ExamUpdate update = new ExamUpdate(credentials.getStudentId(),credentials.getExamCount());
                    Server server = ServerHandler.getInstance();
                    update = server.checkExamUpdate(update);

                    if(update!=null && update.isUpdate()){
                        credentials.setExamCount(update.getPrevCount());
                        //saving credential again in file.
                        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(dirPath+filePath,false));
                        os.writeObject(credentials);
                        os.flush();
                        os.close();

                        setTimerForExams(update.getExams(),true);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void setTimerForExams(ArrayList<Exam> exams,boolean create){
        try {
            for(Exam exam:exams){
                if(create){   //showing exam create notification.
                    String msg = "New exam created with name "+exam.getTitle();
                    Notification notification = new Notification("Exam Portal",msg);
                    notification.show(TrayIcon.MessageType.INFO);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date examDate = dateFormat.parse(exam.getExamDate()+" "+exam.getTime());
                long diff = examDate.getTime()-System.currentTimeMillis();
                if(diff<=0)
                    continue;
                if(diff<=15*60*1000){
                    String msg = exam.getTitle()+" will be starts in less than 15 min.";
                    Notification notification = new Notification("Exam Portal",msg);
                    notification.show(TrayIcon.MessageType.INFO);
                }else{
                    setTimer(exam,diff-(15*60*1000));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void setTimer(Exam exam,long diff){
        new Thread(()->{
            try {
                Thread.sleep(diff);
                String msg = exam.getTitle()+" will be starts in less than 15 min.";
                Notification notification = new Notification("Exam Portal",msg);
                notification.show(TrayIcon.MessageType.INFO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
