package br.com.caelum.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.naming.InitialContext;

public class TesteConsumidor {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		//inicializa o context JNI
		InitialContext context = new InitialContext();
		
//		Properties props = new Properties(); 
//		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory"); 
//		props.setProperty(Context.PROVIDER_URL, "vm://localhost:8161"); 
//		javax.naming.Context context = new InitialContext(props);
//		
		//pega a fabrica configurada no jni
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		//cria a conexao com o activemq
		Connection connection = factory.createConnection();
		//inicia a conexão
		connection.start();
		
		new Scanner(System.in).nextLine();
		
		connection.close();
		context.close();
	}

}
