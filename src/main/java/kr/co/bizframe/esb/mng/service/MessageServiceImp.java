package kr.co.bizframe.esb.mng.service;

import static kr.co.bizframe.esb.mng.type.Constants.TRACE_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.TRANSACTIONMANAGER_NAME;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bizframe.esb.mng.dao.MessageDao;
import kr.co.bizframe.esb.mng.model.PagingModel;
import kr.co.bizframe.esb.mng.model.SearchOptions;
import kr.co.bizframe.esb.mng.model.trace.TraceMessage;

@Service
public class MessageServiceImp implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Transactional(transactionManager=TRACE_DB_KEY + TRANSACTIONMANAGER_NAME)
	@Override
	public String save(TraceMessage msg) {
		return messageDao.save(msg);
	}

	@Override
	public TraceMessage get(String id) {
		return messageDao.get(id);
	}

	@Override
	public PagingModel<TraceMessage> paging(SearchOptions options) {
		return messageDao.paging(options);
	}

	@Transactional(transactionManager=TRACE_DB_KEY + TRANSACTIONMANAGER_NAME)
	@Override
	public void update(String id, TraceMessage msg) {
		messageDao.update(id, msg);
	}

	@Transactional(transactionManager=TRACE_DB_KEY + TRANSACTIONMANAGER_NAME)
	@Override
	public void delete(String id) {
		messageDao.delete(id);
	}

	@Override
	public List<TraceMessage> exchangeTraces(SearchOptions options) {
		return messageDao.exchangeTraces(options);
	}

}
