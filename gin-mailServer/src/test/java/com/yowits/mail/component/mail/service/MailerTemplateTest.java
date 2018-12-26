package com.yowits.mail.component.mail.service;

import com.yowits.base.constants.EmailType;
import com.yowits.email.dto.Email;
import com.yowits.mail.MailApplication;
import com.yowits.mail.component.operation.service.OperationRecordService;
import com.yowits.mail.constan.enums.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@SpringBootTest(classes = MailApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class MailerTemplateTest {
    @Autowired
    private MailerTemplate template;
    @Autowired
    private OperationRecordService recordService;

    @Test
    public void testSendMail() {
        try {
            Email email = createEmailDto();
            Stream.of(email.getAddressees()).forEach(item -> {
                log.info("prepare for send mail to " + item);
            });
            boolean flag = template.send(email);
            log.info("send mail result:{}",flag);
            log.info("prepare persistent operation record .");
            recordService.addOperationRecord(email, flag ? SendStatus.finish : SendStatus.exception);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Email createEmailDto() throws IOException {
        String fname = this.getClass().getClassLoader().getResource("").getPath() + "birthday.xlsx";
        byte[] bytes = FileUtils.readFileToByteArray(new File(fname));

        Email email = new Email();
        email.setSender("hefw");
        email.setAddressees("hefawen@aliyun.com");
        email.setType(EmailType.HTML);
        email.setSubject("这是一封通过kafka发送的一封测试邮件");
        email.setContent("content <br> test 11111111 <br> test 22222222");
        email.setAffixName("birthday.xlsx");
        email.setAffixContent(bytes);
        return email;
    }

}
