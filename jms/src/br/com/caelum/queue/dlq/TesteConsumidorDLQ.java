package br.com.caelum.queue.dlq;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

import br.com.caelum.modelo.Pedido;

public class TesteConsumidorDLQ {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		//informa quais pacotes podem ser lidos
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
		//inicializa o context JNI
		InitialContext context = new InitialContext();
		//pega a fabrica configurada no jni
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		//cria a conexao com o activemq
		//Connection connection = factory.createConnection("user", "senha");
		Connection connection = factory.createConnection();
		//identifica a conexão para o consumidor
		connection.setClientID("consumidorDLQ");
		//inicia a conexão
		connection.start();
		
		//cria a session para interagir com a fila
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		//criando o consumidor, utilizando o lookup financeiro (queue.DLQ) do arquivo jndi.properties
		Destination filaDLQ = (Destination) context.lookup("DLQ");
		MessageConsumer consumer = session.createConsumer(filaDLQ);
		
		//adicionando listener para receber uma ou mais mensagens do tipo texto
		consumer.setMessageListener(new MessageListener(){

			@Override
			public void onMessage(Message message) {
				ObjectMessage objectMessage = (ObjectMessage)message;
				try {
					Pedido pedido = (Pedido)objectMessage.getObject();
					System.out.println("Message Listener - Recebendo mensagem: "+pedido.getCodigo());
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
