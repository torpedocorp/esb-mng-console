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

import kr.co.bizframe.esb.mng.model.config.Agent;
import kr.co.bizframe.esb.mng.model.config.MonitoringAgentInfo;
import kr.co.bizframe.esb.mng.model.config.MonitoringData;
import kr.co.bizframe.esb.mng.service.AgentService;

@RestController
public class AgentController {

	@Autowired
	private AgentService agentService;

	/*---Add new agent---*/
	@PostMapping("/agent")
	public ResponseEntity<?> save(@RequestBody MonitoringAgentInfo agent) {
		agentService.save(agent);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("result", 1);
		return ResponseEntity.ok().body(response);
	}

	/*---Get a agent by id---*/
	@GetMapping("/agent/{id}")
	public ResponseEntity<Agent> get(@PathVariable("id") String id) {
		Agent agent = agentService.get(id);
		return ResponseEntity.ok().body(agent);
	}

	/*---get all agents---*/
	@GetMapping("/agent")
	public ResponseEntity<Map<String, Object>> list() throws Throwable {
		List<Agent> vo = agentService.list();
		if (vo == null) {
			throw new Exception("/agent api error ");
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("messages", vo);
		return ResponseEntity.ok().body(response);
	}

	/*---Update a agent by id---*/
	@PutMapping("/agent/{id}")
	public ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody Agent agent) {
		agent.setAgentId(id);
		boolean insert = false;
		agentService.save(insert, agent);
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("result", 1);
		return ResponseEntity.ok().body(response);
	}

	/*---Delete a agent by id---*/
	@DeleteMapping("/agent/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		agentService.delete(id);
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("result", 1);
		return ResponseEntity.ok().body(response);
	}
	
	@GetMapping("/monitoring/agent")
	public ResponseEntity<Map<String, Object>> monitoringList() throws Throwable {
		List<MonitoringData> vo = agentService.getMonitoringAgents();
		if (vo == null) {
			throw new Exception("/monitoring/agent error, agents is null");
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("agents", vo);
		response.put("routeDatas", agentService.getRouteDatas());
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/monitoring/agent/{id}")
	public ResponseEntity<MonitoringData> monitoringAgent(@PathVariable("id") String id) {
		MonitoringData agent = agentService.getMonitoringAgent(id);
		return ResponseEntity.ok().body(agent);
	}
	
}