package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.*;
import java.io.*;
import java.util.*;
import java.nio.*;

public class Chat {
  
  static String receptor = "";
  static String lastUser = "";

  public static void main(String[] argv) throws Exception {

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("rabbit-chat-27f072f1b3fbf05b.elb.us-east-1.amazonaws.com"); // DNS do balanceador rabbit
    factory.setUsername("chatrabbitmq");
    factory.setPassword("chatrabbitmq");
    factory.setVirtualHost("/");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    Scanner scanner = new Scanner(System.in); // scanner para input
    
    System.out.print("\nUser: ");
    String user_name = scanner.nextLine();
    System.out.print("\n");
    
    String user = "@"+user_name;
    
    //(queue-name, durable, exclusive, auto-delete, params); 
    channel.queueDeclare(user, false, false, false, null); // Cria uma fila para o usuário
    
    String input;
    // Primeiro usuário para o qual será enviada mensagem 
    do
    {
      System.out.print(">> ");
      input = scanner.nextLine();
      
      if (!input.isEmpty()) // verifica se foi digitado algo
      {
        if(!(input.charAt(0) == '@')) input = "@"+input;
        receptor = input;
        lastUser = receptor;
        // (queue-name, durable, exclusive, auto-delete, params);
        channel.queueDeclare(receptor, false, false, false, null); // Cria uma fila para o usuário
        break;
      }
      else
      {
        System.out.println("Entrada vazia, digite novamente!");
      }
    } while(true);
    
    /* ------------------------------------------------------- RECEBER ---------------------------------------------------------- */
    
    Consumer consumer = new DefaultConsumer(channel)
    {
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        Decoder decompress = Decoder.getInstance();
        // (emissor, data, hora, grupo, tipo, corpo, nome)
        String mensagem = "";
        
        try {
          mensagem = decompress.decompressor(body); 
          
          /* ---- LIMPAR BUFFER ----- */
          ByteBuffer bytebuffer = ByteBuffer.wrap(body);
          Buffer buffer = (Buffer)bytebuffer; 
          buffer.clear();
          
        } catch (Exception e) {
          System.out.print("Erro ao obter o pacote!");
        }
        System.out.println(mensagem);
        System.out.print("\n" + receptor + " >> ");
      }};
    
    // (queue-name, autoAck, consumer);
    channel.basicConsume(user, true, consumer);
    
     // Aguarda a primeira mensagem ou alteração de usuário receptor
    System.out.print("\n" + receptor + " >> ");
    input = scanner.nextLine();
    
    while(true)
    {
      if (input.isEmpty())
      {
        System.out.println("Entrada vazia, digite novamente!");
        System.out.print("\n" + receptor + " >> ");
        input = scanner.nextLine();
      }
      
      else
      {
        switch(input.charAt(0))
        {
          case '#':
            if (receptor.charAt(0) == '@') lastUser = receptor;
            receptor = input;
            break;
          
          case '@':
            receptor = input;
            // (queue-name, durable, exclusive, auto-delete, params);
            channel.queueDeclare(receptor, false, false, false, null); // fila para o usuário receptor
            break;
            
          case '!':
            String[] operation = input.split(" ");
            GroupManager manager = GroupManager.getInstance(channel);
            
            switch(operation[0])
            {
              case "!addGroup":
                manager.addGroup(user, "#"+operation[1]);
                break;
              
              case "!addUser":
                manager.addUser("@"+operation[1], "#"+operation[2]);
                break;
              
              case "!delFromGroup":
                manager.delFromGroup("@"+operation[1], "#"+operation[2]);
                break;
              
              case "!removeGroup":
                if (receptor.equals("#"+operation[1])) receptor = lastUser;
                manager.removeGroup("#"+operation[1]);
                break;
              
              case "!listGroups":
                List<String> groups = manager.listGroups("@"+operation[1]);
                System.out.println(groups);
                break;
                
              case "!listUsers":
                List<String> users = manager.listUsers("#"+operation[1]);
                System.out.println(users);
                break;
              
              case "!upload":
                break;
              
              default:
                System.out.println("Operação inválida!");
                break;
            }
            break;
          
          default:
            /* ---------------------------- ENVIAR MENSAGEM---------------------------- */
            if (receptor.charAt(0) == '@') // Caso o receptor atual seja um usuário comum
            {
            
              Encoder compress = Encoder.getInstance();
              byte[] mensagem = compress.compressor(user, input);
              //(exchange, routingKey, props, message-body);
              channel.basicPublish("",  receptor, null,  mensagem);
            }
            
            else // Caso o receptor atual seja um grupo
            {
              manager = GroupManager.getInstance(channel);
              manager.sendMessage(user, receptor, input);
            }
            break;
        }
        
      }
      System.out.print("\n" + receptor + " >> ");
      input = scanner.nextLine();
    }
  }
    //channel.close();
    //connection.close();
}