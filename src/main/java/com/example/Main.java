package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        
    ServerSocket ss = new ServerSocket(8080);

    while(true){
        Socket s = ss.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        
        String firsLine = in.readLine();
        System.out.println(firsLine);
        String [] request = firsLine.split(" ");

        String method = request[0];
        String resource = request[1];
        String version = request[2];
        

        String header;
        do {
            header = in.readLine();
            System.out.println(header);
        }while(!header.isEmpty());

        System.out.println("richiesta terminata");

        File file = new File("htdcocs" + resource );
       

        //risposta
        if (file.exists()){

            out.writeBytes("HTTP/1.1 200 ok\n");
            out.writeBytes("Content-Type:" + getContentType(file) + "\n");
            out.writeBytes("Content-Lenght: " + file.length() + " \n");
            out.writeBytes("\n"); 
            InputStream input = new FileInputStream(file);
            byte[] buf = new byte[8192];
            int n;
            while ((n = input.read(buf)) != -1 ){
                out.write(buf, 0, n);
            }
            input.close();
            
        }else{
            out.writeBytes("HTTP/1.1 404 Not Found\n");
            out.writeBytes("Content-Lenght: 0\n");
            out.writeBytes("\n");
        }    

        s.close();

    }
    }

    private static String getContentType(File f){
        String [] s = f.getName().split("\\ .");
        String ext = s[s.length -1 ];
        switch (ext) {
            case "html":
            case "htm":
                return "text/html";
            case "png":
                return "image/png";
            case "css":
                return "text.css";
            default:
                return "";
        }
    }


}