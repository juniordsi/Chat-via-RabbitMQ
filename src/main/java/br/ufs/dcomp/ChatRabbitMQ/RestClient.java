package br.ufs.dcomp.ChatRabbitMQ;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;


public class RestClient
{
    private String usernameAndPassword = "chatrabbitmq:chatrabbitmq"; // Login do servidor rabbit (username:password)
    private String authorizationHeaderName = "Authorization";
    private String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
    private String restResource = "http://tcp-web-chat-ba38ba4b682f5bc7.elb.us-east-1.amazonaws.com"; // DNS do balanceador HTTP

    Client client;
    
    public RestClient() 
    {
        client = ClientBuilder.newClient();
    }
    
    
    public List<String> getGroups(String user) // RETORNAR TODOS OS EXCHANGES NOS QUAIS A FILA ESTÁ CONECTADA
    {
        List<String> groups =  new ArrayList<String>();
        try {
            Response resposta = client.target( restResource )
                .path("/api/queues/%2f/"+user+"/bindings")
            	.request(MediaType.APPLICATION_JSON)
                .header( authorizationHeaderName, authorizationHeaderValue ) // The basic authentication header goes here
                .get();     // Perform a post with the form values
               

            if (resposta.getStatus() == 200)
            {
            	String json = resposta.readEntity(String.class);
            	String jsonFile = "{bindings:"+json+"}";
                //System.out.println(json);

                // Converter o json em um objeto Java
                Gson gson = new Gson();
                //BufferedReader br = null;
                
                try {
                    //br = new BufferedReader(new FileReader(json));
                    Result result = gson.fromJson(jsonFile, Result.class);
                    
                    if (result != null) {
                        for (Binding binding : result.getFromJson()) {
                            if (!(binding.getSource().isEmpty())) {
                                //System.out.println(binding.getDestination()+" "+binding.getSource());
                                groups.add(binding.getSource().replace("#", ""));
                            }
                        }
                    }
                } catch (Exception e) {
                        e.printStackTrace();
                }
                
                
            }
            else
            {
                System.out.println(resposta.getStatus());
            }  
        } catch (Exception e) {
            System.out.println("Erro!");
			e.printStackTrace();
		}
        
        return groups;
    }
    

    public List<String> getUserGroups(String group_name) // RETORNAR TODAS AS FILAS NAS QUAIS UM GRUPO ESTÁ CONECTADO
    {
        List<String> users_group = new ArrayList<String>();
        try {
            Response resposta = client.target(restResource)
                    .path("/api/bindings/%2f")
                	.request(MediaType.APPLICATION_JSON)
                    .header( authorizationHeaderName, authorizationHeaderValue ) // The basic authentication header goes here
                    .get();     // Perform a post with the form values
            
    
            if (resposta.getStatus() == 200) 
            {
            	String json = resposta.readEntity(String.class);
            	String jsonFile = "{bindings:"+json+"}";
                //System.out.println(json);

                // Converter o json em um objeto Java
                Gson gson = new Gson();
                //BufferedReader br = null;
                
                try {
                    //br = new BufferedReader(new FileReader(json));
                    Result result = gson.fromJson(jsonFile, Result.class);
                    
                    if (result != null) {
                        for (Binding binding : result.getFromJson()) {
                            if (binding.getSource().equals(group_name)) {
                                //System.out.println(binding.getDestination()+" "+binding.getSource());
                                users_group.add(binding.getDestination().replace("@", ""));
                            }
                        }
                    }
                } catch (Exception e) {
                        e.printStackTrace();
                }
         
            }
            else
            {
                System.out.println(resposta.getStatus());
            }
        } catch (Exception e) {
            System.out.println("Erro!");
            e.printStackTrace();
        }
        
        return users_group;
    }

}


