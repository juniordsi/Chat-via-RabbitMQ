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

public class Encoder
{
  /* Padrão Singleton para garantir a instância de só um objeto do tipo Encoder */
  private static Encoder instance;
  
  private Encoder(){}
  
  public static Encoder getInstance()
  {
      if (instance == null)
      {
        instance = new Encoder();
      }
      return instance;
  }
  
  public byte[] compressor(String emissor, String message) throws Exception
  {
      // Data e Hora
      Date date = new Date();
      SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
      String dateTime = dateformat.format(date); // Data e hora atual
      String data = dateTime.substring(0,10);
      String hora = dateTime.substring(11,16);
      
      // Serializando a mensagem
      
      MessageProto.Conteudo.Builder conteudo = MessageProto.Conteudo.newBuilder(); // Montar mensagem-conteudo
      conteudo.setTipo("text/plain");
      conteudo.setCorpo(message);
      conteudo.setNome("");
      
      MessageProto.Mensagem.Builder builderMensagem = MessageProto.Mensagem.newBuilder(); // Montar mensagem-cabeçalho
      builderMensagem.setEmissor(emissor.replace("@", ""));
      builderMensagem.setData(data);
      builderMensagem.setHora(hora);
      builderMensagem.setGrupo("");
      builderMensagem.setConteudo(conteudo);
      
      // Obtendo a mensagem
      MessageProto.Mensagem Mensagem = builderMensagem.build();
      
      return Mensagem.toByteArray(); // Retorna a mensagem serializada
  }
  
  public byte[] compressor(String emissor, String group_name, String message) throws Exception
  {
      // Data e Hora
      Date date = new Date();
      SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
      String dateTime = dateformat.format(date); // Data e hora atual
      String data = dateTime.substring(0,9);
      String hora = dateTime.substring(11,15);
      
      // Serializando a mensagem
      
      MessageProto.Conteudo.Builder conteudo = MessageProto.Conteudo.newBuilder(); // Montar mensagem-conteudo
      conteudo.setTipo("text/plain");
      conteudo.setCorpo(message);
      conteudo.setNome("");
      
      MessageProto.Mensagem.Builder builderMensagem = MessageProto.Mensagem.newBuilder(); // Montar mensagem-cabeçalho
      builderMensagem.setEmissor(emissor.replace("@", ""));
      builderMensagem.setData(data);
      builderMensagem.setHora(hora);
      builderMensagem.setGrupo(group_name);
      builderMensagem.setConteudo(conteudo);
      
      // Obtendo a mensagem
      MessageProto.Mensagem Mensagem = builderMensagem.build();
      
      return Mensagem.toByteArray(); // Retorna a mensagem serializada
  }
}