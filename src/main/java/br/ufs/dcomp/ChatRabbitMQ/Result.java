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

@Generated("org.jsonschema2pojo")

public class Result 
{ 
    @SerializedName("bindings")
    @Expose
    private List<Binding> fromJson = new ArrayList<Binding>();

    public List<Binding> getFromJson() {
        return fromJson;
    }

    public void setFromJson(List<Binding> fromJson) {
        this.fromJson = fromJson;
    }
}