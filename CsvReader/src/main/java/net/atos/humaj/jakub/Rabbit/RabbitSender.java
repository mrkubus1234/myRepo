package net.atos.humaj.jakub.Rabbit;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitSender {

    public void sendDataViaRabbit(String message, String routingKey, RabbitConfiguration configuration) throws IOException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(configuration.getHostName());
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(routingKey,false,false,false,null);

            channel.basicPublish("",routingKey, null, message.getBytes("UTF-8"));

            channel.close();
            connection.close();

        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}