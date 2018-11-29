/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe esb-mng-console project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

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
	@DeleteMapping("/agent/{agentId}/{routeId}")
	public ResponseEntity<?> delete(@PathVariable("agentId") String agentId, @PathVariable("routeId") String routeId) {
		agentService.delete(agentId, routeId);
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