package br.com.caelum.queue.redelivery;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteProdutorRedelivery {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
//		Properties props = new Properties(); 
//		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory"); 
//		props.setProperty(Context.PROVIDER_URL, "vm://localhost:8161"); 
//		javax.naming.Context context = new InitialContext(props);

		//inicializa o context JNI
		InitialContext context = new InitialContext();
		//pega a fabrica configurada no jni
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		//cria a conexao com o activemq
//		Connection connection = factory.createConnection("user", "senha");
		Connection connection = factory.createConnection();
		//inicia a conexão
		connection.start();
		
		//cria a session para interagir com a fila
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//criando o produtor, utilizando o lookup financeiro (queue.financeiro) do arquivo jndi.properties
		Destination fila = (Destination) context.lookup("redelivery");
		MessageProducer producer = session.createProducer(fila);
		
		for (int i = 0; i < 5; i++) {
			Message message = session.createTextMessage("<pedido><id>"+i+"</id></pedido>");
			producer.send(message);
		}
		
		session.close();
		connection.close();
		context.close();
	}

}
