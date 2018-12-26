package com.yowits.mail.component.mail.service;

import com.yowits.email.dto.Email;
import com.yowits.mail.component.jmbc.connection.ConnectionPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.activation.*;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

/*
* Copyright 2012-2014 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

/**
 * Mail Template class for email support and template engine.
 * @since 1.2.1
 */

@Configuration
/*@ConditionalOnBean(value = JavaMailSender.class)
@AutoConfigureAfter(value = JavaMailSender.class)*/
@Component
@Slf4j
public class MailerTemplate {

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${spring.mail.nick}")
    private String nick;

    /**
     * 单独发送
     * 类型：TEXT
     *
     * @param email
     * @return
     * @throws javax.mail.MessagingException
     */
    public boolean send(Email email) throws javax.mail.MessagingException {
        boolean isSent = false;

        JavaMailSender mailSender = ConnectionPoolManager.getInstance().getConnection();
        JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        Constans.mailSendFlag.set(impl.getUsername());

        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, mailProperties.getDefaultEncoding());
        message.setValidateAddresses(true);
        message.setTo(email.getAddressees());
//        String nickName = initNickName();
//        message.setFrom(nickName + "<" + impl.getUsername() + ">");
        message.setFrom(email.getSender() + "<" + impl.getUsername() + ">");
        message.setSubject(email.getSubject());
        message.setText(email.getContent(), true);
        message.setPriority(1);

        try {
            log.debug("Sending Plain Mail with Java-Mail and SpringBoot-MailSenderAutoConfiguration");

            setAffix(email, message);
            mailSender.send(mimeMessage);
            log.debug("Mail sent to " + email.getAddressees());
            isSent = true;
        } catch (Exception e) {
            log.error("Can't send mail, Root Cause : " + e.getLocalizedMessage(), e);
        } finally {
            message = null;
            mimeMessage = null;
        }
        return isSent;
    }

    private String initNickName() {
        String nickName = "";
        try {
            nickName = javax.mail.internet.MimeUtility.encodeText(nick);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return nickName;
    }

    private void setAffix(Email email, MimeMessageHelper message) throws MessagingException {
        String affixName = email.getAffixName();
        byte[] affixContent = email.getAffixContent();
        if (StringUtils.isNotBlank(affixName) && affixContent!=null && affixContent.length > 0) {
            MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
            DataSource source = new ByteArrayDataSource(email.getAffixContent(), typeMap.getContentType(email.getAffixName()));
            message.addAttachment(email.getAffixName(), source);
        }
    }

    private boolean hasProtocol(String path) {
        return (path.contains("file:") || path.contains("classpath:"));
    }

    /**
     * 单独发送
     * 类型：HTML
     *
     * @param email
     * @param templateSource
     * @param contentMappingMap
     * @return
     * @throws MessagingException
     */
    public boolean sendWithTemplate(Email email, String templateSource, Map<String, String> contentMappingMap) throws MessagingException {
        String body = (hasProtocol(templateSource)) ?
                getReplacedText(templateSource, contentMappingMap, "pre").toString() : getReplacedText(mailProperties.getPrefix() + "/" + templateSource, contentMappingMap, "pre").toString();
        if (body.length() != 0) {
            email.setContent(body);
            return send(email);
        } else {
            log.info("Could not send mail, body-content length = 0 ");
            return false;
        }
    }

    private StringBuilder getReplacedText(String templateSource, Map<String, String> contentMappingMap, String... escapeTags) {
        Resource resource = resourceLoader.getResource(templateSource);
        InputStream in;
        StringBuilder sb = new StringBuilder();
        try {

            in = resource.getInputStream();
            sb.append(StreamUtils.copyToString(in, Charset.forName(mailProperties.getDefaultEncoding())));
            for (String key : contentMappingMap.keySet()) {
                replaceAll(sb, key, contentMappingMap.get(key));
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }

        return sb;
    }

    private void replaceAll(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = builder.indexOf(from, index);
        }
    }

}
