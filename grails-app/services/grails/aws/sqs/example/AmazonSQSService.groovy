package grails.aws.sqs.example

import org.springframework.jms.annotation.JmsListener

/**
 * Created by Evgeny Smirnov <illerax@gmail.com>
 */
class AmazonSQSService {

    def defaultJmsTemplate

    /*
    * Send message to Amazon SQS Queue
    * */
    void sendMessage(String queueName, String payload) {
        log.debug "Send message to SQS, queue:$queueName, message:$payload"
        defaultJmsTemplate.convertAndSend(queueName, payload)
    }

    /*
    * Recieve message from Amazon SQS Queue
    * */
    @JmsListener(destination = '${amazon.sqs.queue}')
    void listenSyncManager(String requestString) {
        log.info "Received from SQS: $requestString"
    }
}
