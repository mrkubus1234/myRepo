package net.atos.humaj.jakub.Rabbit;

import java.util.Properties;

public class RabbitConfiguration {
    private String hostName;
    private int port;
    private String exchangeName;
    private String exchangeType;

    private String queueName;

    public RabbitConfiguration(Properties prop){
        hostName = prop.getProperty("rabbitmq.hostname");
        exchangeName = prop.getProperty("rabbitmq.exchange.name");
        exchangeType = prop.getProperty("rabbitmq.exchange.type");
        queueName = prop.getProperty("rabbitmq.queue.name");
        try{
            int port = Integer.parseInt(prop.getProperty("rabbitmq.port"));
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getExchangeType() {
        return exchangeType;
    }
}
