package br.com.caelum.jms;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteMonitoramento {

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
		Connection connection = factory.createConnection();
		//inicia a conexão
		connection.start();
		
		//cria a session para interagir com a fila
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//lookup financeiro (queue.financeiro) do arquivo jndi.properties
		Destination fila = (Destination) context.lookup("financeiro");
		QueueBrowser browser = session.createBrowser((Queue) fila);
		Queue queue = browser.getQueue();
		
		System.out.println(queue.getQueueName());
		System.out.println(queue.toString());
		Enumeration<?> enumeration = browser.getEnumeration();
		while (enumeration.hasMoreElements()){
			Message mensagem = (Message)enumeration.nextElement();
			System.out.println("Mensagem da fila: "+((TextMessage) mensagem).getText());
		}
		
		browser.close();
		session.close();
		connection.close();
		context.close();
	}

}
