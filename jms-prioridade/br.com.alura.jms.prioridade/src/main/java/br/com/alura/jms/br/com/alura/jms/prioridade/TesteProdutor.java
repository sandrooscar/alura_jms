package br.com.alura.jms.br.com.alura.jms.prioridade;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

public class TesteProdutor {

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
		//inicia a conex�o
		connection.start();
		
		//cria a session para interagir com a fila
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//criando o produtor, utilizando o lookup financeiro (queue.financeiro) do arquivo jndi.properties
		Destination fila = (Destination) context.lookup("prioridade");
		MessageProducer producer = session.createProducer(fila);

		Message message = session.createTextMessage("INFO | Apache ActiveMQ 5.15.12 (localhost, ID:BUGNOTE-51624-1586539344325-0:1) uptime 1 hour 48 minutes");
		producer.send(message,DeliveryMode.NON_PERSISTENT, 1, 20000);

		message = session.createTextMessage("WARNING| Apache ActiveMQ 5.15.12 (localhost, ID:BUGNOTE-51624-1586539344325-0:1) uptime 1 hour 48 minutes");
		producer.send(message,DeliveryMode.NON_PERSISTENT, 7, 20000);

		message = session.createTextMessage("ERROR| Apache ActiveMQ 5.15.12 (localhost, ID:BUGNOTE-51624-1586539344325-0:1) uptime 1 hour 48 minutes");
		producer.send(message,DeliveryMode.NON_PERSISTENT, 9, 20000);

		session.close();
		connection.close();
		context.close();
	}

}
