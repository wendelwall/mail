package com.yowits.mail.component.operation.service.impl;

import com.yowits.base.dto.Result;
import com.yowits.email.dto.Email;
import com.yowits.base.utils.DateUtils;
import com.yowits.mail.component.operation.model.OperationRecord;
import com.yowits.mail.component.operation.service.OperationRecordService;
import com.yowits.mail.constan.dto.GlobalErrorCode;
import com.yowits.mail.constan.enums.SendStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Component
public class OperationRecordServiceImpl implements OperationRecordService {

	private static final Logger LOG = Logger.getLogger(OperationRecordServiceImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Result<String> addOperationRecord(Email email, SendStatus status) {
		if (StringUtils.isEmpty(email.getAddressees())) {
			return Result.ofParamsError(GlobalErrorCode.CODE_61602.getDesc());
		}

		List<OperationRecord> records = new ArrayList<>();
		Stream.of(email.getAddressees()).forEach(item -> {
			OperationRecord record = new OperationRecord();
			record.setCreateDate(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			record.setFinishDate(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			record.setCurrentTimeMillis(new Date().getTime());
			record.setFrom(email.getSender());
			record.setTo(item);
			record.setStatus(status);
			record.setType(email.getType().getValue());
			record.setSubject(email.getSubject());
			record.setContent(email.getContent());
			records.add(record);
		});

		LOG.debug("insert all operation record , size:" + records.size());
		String collectionName = mongoTemplate.getCollectionName(OperationRecord.class) + LocalDate.now().toString().replace("-", "");
		mongoTemplate.insert(records, collectionName);
		LOG.debug("write login record success.");
		return Result.ofParamsError(GlobalErrorCode.CODE_200.getDesc());
	}
}