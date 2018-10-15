package kr.co.bizframe.esb.mng.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.co.bizframe.esb.mng.dao.ExchangeInfoDao;
import kr.co.bizframe.esb.mng.model.PagingModel;
import kr.co.bizframe.esb.mng.model.SearchOptions;
import kr.co.bizframe.esb.mng.model.trace.ExchangeInfo;
import kr.co.bizframe.esb.mng.model.trace.TraceMessage;
import kr.co.bizframe.esb.mng.service.MessageService;

@RestController
public class MessageController {

   @Autowired
   private MessageService messageService;

	@Autowired
	private ExchangeInfoDao exchangeInfoDao;
	
   /*---Add new msg---*/
   @PostMapping("/msg")
   public ResponseEntity<?> save(@RequestBody TraceMessage msg) {
      String id = messageService.save(msg);
      return ResponseEntity.ok().body("New BizFrameJpaTraceEventMessage has been saved with ID:" + id);
   }

   /*---Get a msg by id---*/
   @GetMapping("/msg/{id}")
   public ResponseEntity<TraceMessage> get(@PathVariable("id") String id) {
      TraceMessage msg = messageService.get(id);
      return ResponseEntity.ok().body(msg);
   }

	/*---get all msgs---*/
	@PostMapping("/msgs")
	public ResponseEntity<Map<String, Object>> list(@RequestBody SearchOptions options) throws Throwable {
		PagingModel<TraceMessage> vo = messageService.paging(options);
		if (vo == null) {
			throw new Exception("/msgs api error ");
		}
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		jsonResponse.put("messages", vo.getModels());
		jsonResponse.put("totalRows", vo.getCount());
		return ResponseEntity.ok().body(jsonResponse);
	}

	/*---exchangeId msgs---*/
	@PostMapping("/exchange/traces")
	public ResponseEntity<Map<String, Object>> exchangelist(@RequestBody SearchOptions options) throws Throwable {
		List<TraceMessage> vo = messageService.exchangeTraces(options);
		ExchangeInfo exchange = exchangeInfoDao.get(options.getId());
		if (vo == null) {
			throw new Exception("/exchange/traces api error ");
		}
		Map<String, Object> jsonResponse = new HashMap<String, Object>();
		jsonResponse.put("messages", vo);
		jsonResponse.put("exchange", exchange);
		return ResponseEntity.ok().body(jsonResponse);
	}
	
   /*---Update a msg by id---*/
   @PutMapping("/msg/{id}")
   public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody TraceMessage msg) {
      messageService.update(id, msg);
      return ResponseEntity.ok().body("BizFrameJpaTraceEventMessage has been updated successfully.");
   }

   /*---Delete a msg by id---*/
   @DeleteMapping("/msg/{id}")
   public ResponseEntity<?> delete(@PathVariable("id") String id) {
      messageService.delete(id);
      return ResponseEntity.ok().body("BizFrameJpaTraceEventMessage has been deleted successfully.");
   }
}