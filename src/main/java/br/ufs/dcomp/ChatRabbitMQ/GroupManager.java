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

public class GroupManager {
    
    private static GroupManager instance;
    private static Channel channel;
    
    private GroupManager(Channel user_channel){
        this.channel = user_channel;
    }
    
    // Padrão SIngleton para garantir a instância de apenas um objeto 
    public static GroupManager getInstance(Channel user_channel)
    {
        if (instance == null)
        {
            instance = new GroupManager(user_channel);
        }
        return instance;
    }
    
    public void sendMessage(String user, String group_name, String message)
    {
        try {
            Encoder compress = Encoder.getInstance();
            byte[] mensagem = compress.compressor(user, group_name, message);
            channel.basicPublish(group_name, "", null, mensagem);
        } catch (Exception e) {
            System.out.println("Erro ao enviar mensagem.");
        }
        
    }
    
    public List<String> listGroups(String user) // Lista todos os grupos que um usuário está
    {
        RestClient request = new RestClient();
        List<String> groups = request.getGroups(user);
        return groups;
    }
    
    
    public List<String> listUsers(String group_name) // Lista todos usuários de um grupo
    {
        RestClient request = new RestClient();
        List<String> users = request.getUserGroups(group_name);
        return users;
    }
    
    
    public void addGroup(String user, String group_name) throws Exception // Cria um novo grupo
    {
        channel.exchangeDeclare(group_name, "fanout");
        this.addUser(user, group_name);
    }
    
    public void addUser(String user, String group_name) throws Exception// Adiciona usuário em um grupo
    {
        channel.queueBind(user, group_name, "");
    }
    
    public void delFromGroup(String user, String group_name) throws Exception// Remove usuário de um grupo
    {
        channel.queueUnbind(user, group_name, "");
    }
    
    public void removeGroup(String group_name) throws Exception // Exclui um grupo
    {
        channel.exchangeDelete(group_name, false);
    }
}