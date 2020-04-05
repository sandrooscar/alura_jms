package br.com.caelum.topic.broadcast;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

public class TesteProdutorTopicSelector {

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
		Connection connection = factory.createConnection("user", "senha");
		//inicia a conexão
		connection.start();
		
		//cria a session para interagir com a fila
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//criando o produtor, utilizando o lookup financeiro (queue.financeiro) do arquivo jndi.properties
		Destination topico = (Destination) context.lookup("loja");
		MessageProducer producer = session.createProducer(topico);
		
		for (int i = 0; i < 3; i++) {
			Message message = session.createTextMessage("<pedido><id>"+i+"</id></pedido>");
			//message.setBooleanProperty("ebook", false);
			System.out.println("Send message: "+i);
			producer.send(message);
		}
		
		session.close();
		connection.close();
		context.close();
	}

}
