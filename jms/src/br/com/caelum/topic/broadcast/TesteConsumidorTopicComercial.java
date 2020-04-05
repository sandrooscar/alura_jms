package br.com.caelum.topic.broadcast;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.InitialContext;

public class TesteConsumidorTopicComercial {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
//		Properties props = new Properties(); 
//		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory"); 
//		props.setProperty(Context.PROVIDER_URL, "vm://localhost:8161"); 
//		properties.setProperty("queue.financeiro", "fila.financeiro");
//		javax.naming.Context context = new InitialContext(props);

		//inicializa o context JNI
		InitialContext context = new InitialContext();
		//pega a fabrica configurada no jni
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		//cria a conexao com o activemq
		Connection connection = factory.createConnection();
		//identifica a conexão para o tópico
		connection.setClientID("consumidorTopicoComercial");
		//inicia a conexão
		connection.start();
		
		//cria a session para interagir com a fila
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//criando o consumidor, utilizando o lookup financeiro (queue.financeiro) do arquivo jndi.properties
		Topic topico = (Topic) context.lookup("loja");
		MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
		
		//adicionando listener para receber uma ou mais mensagens do tipo texto
		consumer.setMessageListener(new MessageListener(){

			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage)message;
				try {
					System.out.println("Message Listener - Recebendo mensagem: "+textMessage.getText());
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}

}
