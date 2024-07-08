package com.exam.portal.proctoring;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ProcessesDetails {

    public int getProcessesCount(){
        try {
            Process process = new ProcessBuilder("powershell","\"gps| ? {$_.mainwindowtitle.length -ne 0} | Format-Table -HideTableHeaders name, ID").start();
            Scanner sc = new Scanner(process.getInputStream());
            int count = 0;
            if(sc.hasNextLine())sc.nextLine();
            while (sc.hasNextLine()){
                String name = sc.nextLine();
                if(!name.equals(""))
                    count++;
                //System.out.println(name);
            }
          //  System.out.println(count);
            return count-5;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public boolean checkInternet(){
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}


