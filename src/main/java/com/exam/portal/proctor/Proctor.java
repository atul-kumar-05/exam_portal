package com.exam.portal.proctor;

import com.exam.portal.entities.Image;
import com.exam.portal.entities.jsonclasses.FaceDetectionResponse;
import com.exam.portal.entities.jsonclasses.TagDetectionResponse;
import com.exam.portal.entities.jsonclasses.Tags;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Proctor {
    private static final String API_KEY = "acc_0509d1b79051d66";
    private static final String API_SECRET = "c2b18824b72e63d25249bccdafbe5d1b";

    //returns true if  there is no content in image which signifies cheating.
    public boolean getResult(Image image){
        if(!faceResult(image))
            return false;
        image.setStudentId(image.getStudentId()+"1");
        return objectResult(image);
    }

    //return true if there is only single face in the image
    private boolean faceResult(Image image){
        try {
            String response = getResponse("/faces/detections",image);
           // System.out.println(response);
            Gson gson = new Gson();
            FaceDetectionResponse faceDetectionResponse = gson.fromJson(response,FaceDetectionResponse.class);
            if(faceDetectionResponse.getResult().getFaces().size() != 1){
                System.out.println(response);
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    //check all the tags of image and there is tag like mobile,laptop then there may be chance of cheating.
    private boolean objectResult(Image image){
        try{
            String response = getResponse("/tags",image);
          //  System.out.println(response);
            Gson gson = new Gson();
            TagDetectionResponse tagDetectionResponse = gson.fromJson(response,TagDetectionResponse.class);
            for(Tags tags:tagDetectionResponse.getResult().getTags()){
                String tag = tags.getTag().getEn().toLowerCase();
                if((tag.equals("mobile") || tag.equals("laptop")) && tags.getConfidence()>25){
                    System.out.println(response);
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    private String getResponse(String endpoint,Image image){
        try{
            String credentialsToEncode = API_KEY + ":" +API_SECRET;
            String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary =  "Image Upload";

            URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + basicAuth);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + image.getStudentId() + "\"" + crlf);
            request.writeBytes(crlf);

            request.write(image.decodeBytes());
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            // System.out.println(response);

            responseStream.close();
            connection.disconnect();

            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
