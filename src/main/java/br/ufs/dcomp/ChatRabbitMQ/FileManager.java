package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.*;

public class FileManager extends Thread {
    
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("rabbit-chat-27f072f1b3fbf05b.elb.us-east-1.amazonaws.com"); // DNS do balanceador rabbit
    factory.setUsername("chatrabbitmq");
    factory.setPassword("chatrabbitmq");
    factory.setVirtualHost("/");
    
    
    private String url;
    
    
    public FileManager(String url) {
        this.url = url;
    }
    
    public void run(){
        
        try{
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            
            // Arquivo onde o conteúdo da resposta será escrito
            File file = new File(url);
            FileOutputStream fos = new FileOutputStream(file);
            
            System.out.println("Downloading...");
            byte[] buf = new byte[1000];
            int len;
            while ((len = in.read(buf)) > 0){
                fos.write(buf, 0, len);
                fos.flush();
                System.out.println(len);
            }
            fos.close();
            
            for (int i = 1; i <= total; i++){
                System.out.println("Tomar "+ nome + " "+i);
                Thread.sleep(1000*intervalo);
            }
            channel.close();
        } catch (Exception e) {
            System.out.println("Falha ao obter arquivo!");
        }
    }
    
}