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

package kr.co.bizframe.esb.mng.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MonitorThreadFactory implements ThreadFactory {
	private final String name;
	private final boolean daemon;
	private final AtomicInteger threadNumber = new AtomicInteger(1);

	public MonitorThreadFactory(String name) {
		this(name, false);
	}

	public MonitorThreadFactory(String name, boolean daemon) {
		this.name = name;
		this.daemon = daemon;
	}

	public Thread newThread(Runnable r) {
		int num = this.threadNumber.getAndIncrement();
		String threadName = this.name + "_" + num;
		Thread t = new Thread(r, threadName);
		if (this.daemon) {
			t.setDaemon(true);
		}
		return t;
	}

}
