package kr.co.bizframe.esb.mng.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.bizframe.esb.mng.dao.MessageDao;
import kr.co.bizframe.esb.mng.model.PagingModel;
import kr.co.bizframe.esb.mng.model.SearchOptions;
import kr.co.bizframe.esb.mng.model.trace.TraceMessage;

@Service
public class MessageServiceImp implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Override
	public TraceMessage get(String id) {
		return messageDao.get(id);
	}

	@Override
	public PagingModel<TraceMessage> paging(SearchOptions options) {
		return messageDao.paging(options);
	}

	@Override
	public List<TraceMessage> exchangeTraces(SearchOptions options) {
		return messageDao.exchangeTraces(options);
	}

}
