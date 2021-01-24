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

public class Binding
{

    @SerializedName("source")
    @Expose
    private String source;
    
    @SerializedName("vhost")
    @Expose
    private String vhost;
    
    @SerializedName("destination")
    @Expose
    private String destination;
   
    @SerializedName("destination_type")
    @Expose
    private String destinationType;
    
    @SerializedName("routing_key")
    @Expose
    private String routingKey;
    
    @SerializedName("arguments")
    @Expose
    private Arguments arguments;
    
    @SerializedName("properties_key")
    @Expose
    private String propertiesKey;
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getVhost() {
        return vhost;
    }
    
    public void setVhost(String vhost) {
        this.vhost = vhost;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public String getDestinationType() {
        return destinationType;
    }
    
    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }
    
    public String getRoutingKey() {
        return routingKey;
    }
    
    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
    
    public Arguments getArguments() {
        return arguments;
    }
    
    public void setArguments(Arguments arguments) {
        this.arguments = arguments;
    }
    
    public String getPropertiesKey() {
        return propertiesKey;
    }
    
    public void setPropertiesKey(String propertiesKey) {
        this.propertiesKey = propertiesKey;
    }

}