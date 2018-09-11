package kr.co.bizframe.esb.mng.service;

import java.util.List;

import kr.co.bizframe.esb.mng.model.PagingModel;
import kr.co.bizframe.esb.mng.model.SearchOptions;
import kr.co.bizframe.esb.mng.model.trace.TraceMessage;

public interface MessageService {

   String save(TraceMessage msg);
   TraceMessage get(String id);
   PagingModel<TraceMessage> paging(SearchOptions options);
   List<TraceMessage> exchangeTraces(SearchOptions options);
   void update(String id, TraceMessage msg);
   void delete(String id);
}
