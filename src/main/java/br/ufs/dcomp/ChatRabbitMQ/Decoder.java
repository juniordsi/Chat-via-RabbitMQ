package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.String;
import java.io.*;
import java.util.*;

public class Decoder
{
  /* Padrão Singleton para garantir a instância de só um objeto do tipo Decoder */
  private static final Decoder instance = new Decoder();
  
  private Decoder() {}
  
  public static Decoder getInstance()
  {
      return instance;
  }
  
  public String decompressor(byte[] buffer) throws Exception
  {
      // Mapeando bytes para a mensagem protobuf
      MessageProto.Mensagem message = MessageProto.Mensagem.parseFrom(buffer);
      
      // Extraindo dados da mensagem
      String emissor = message.getEmissor();
      String data = message.getData();
      String hora = message.getHora();
      String grupo = message.getGrupo();
      
      MessageProto.Conteudo conteudo = message.getConteudo();
      String tipo = conteudo.getTipo();
      String corpo = conteudo.getCorpo();
      String nome = conteudo.getNome();
      
      String mensagem = "\n("+data+" às "+hora+") "+emissor+grupo+" diz: "+corpo;

      return mensagem;
  }
}