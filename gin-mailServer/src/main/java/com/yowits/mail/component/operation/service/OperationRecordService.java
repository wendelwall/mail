package com.yowits.mail.component.operation.service;

import com.yowits.base.dto.Result;
import com.yowits.email.dto.Email;
import com.yowits.mail.constan.enums.SendStatus;
import org.springframework.stereotype.Service;

@Service
public interface OperationRecordService {

	Result<String> addOperationRecord(Email email, SendStatus status);

}
