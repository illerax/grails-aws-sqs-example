package grails.aws.sqs.example.config

import com.amazon.sqs.javamessaging.ProviderConfiguration
import com.amazon.sqs.javamessaging.SQSConnectionFactory
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import grails.util.Holders
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate

/**
 * Created by Evgeny Smirnov <illerax@gmail.com>
 */

@Configuration
@EnableJms
class JmsConfig {

    /*
    * SQSConnectionFactory class implements the ConnectionFactory interface as defined by the JMS standard,
    * allowing it to be used with standard JMS interfaces and classes to connect to SQS
    * */
    SQSConnectionFactory connectionFactory =
            new SQSConnectionFactory(
                    new ProviderConfiguration(),
                    AmazonSQSClientBuilder.standard()
                            .withRegion(Regions.US_WEST_2)
                            .withCredentials(new AWSStaticCredentialsProvider(
                            [
                                    getAWSAccessKeyId: { Holders.config.amazon.key },
                                    getAWSSecretKey  : { Holders.config.amazon.secret }
                            ] as AWSCredentials))
            )

    //Will be used to create listeners
    @Bean
    DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        new DefaultJmsListenerContainerFactory(
                connectionFactory: this.connectionFactory
        )
    }

    //Will be used to send messages
    @Bean
    JmsTemplate defaultJmsTemplate() {
        new JmsTemplate(this.connectionFactory)
    }
}
