package com.yowits.mail.listener;

import com.yowits.base.dto.KafkaMessage;
import com.yowits.base.dto.Result;
import com.yowits.email.dto.Email;
import com.yowits.email.service.EmailValidate;
import com.yowits.mail.component.mail.service.MailerTemplate;
import com.yowits.mail.component.operation.service.OperationRecordService;
import com.yowits.mail.constan.enums.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;


@Component
@Slf4j
public class MailListener {

    @Autowired
    private MailerTemplate template;

    @Autowired
    private OperationRecordService recordService;

    @KafkaListener(topics = "${banbu.kafka.email.topic:banbu-mail}")
    public void receive(KafkaMessage message) {
        try {
            Email email = (Email)message.getContent();
            EmailValidate.Validate(email);
            Stream.of(email.getAddressees()).forEach(item -> {
                log.info("prepare for send mail to " + item);
            });
            boolean flag = template.send(email);
            log.info("send mail result:{}",flag);
            log.info("prepare persistent operation record.");
            Result<String> result = recordService.addOperationRecord(email, flag ? SendStatus.finish : SendStatus.exception);
            if(result.getSuccess()) {
                log.error("persistent operation record fail. error:{}" , result.getMessage());
            } else {
                log.info("persistent operation record success.");
            }
        } catch (Exception e) {
            log.error("send mail error : " + e.getMessage(), e);
        }
    }

}
