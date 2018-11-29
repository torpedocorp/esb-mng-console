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

package kr.co.bizframe.esb.mng.type;

import java.util.EnumSet;

public enum Status {
	Disabled(1),
	Enabled(0);

	private int val;

	private Status(int val) {
		this.val = val;
	}

	public int getValue() {
		return val;
	}

	public static Status valueOfCode(int code) {
		for (Status type : EnumSet.allOf(Status.class)) {
			if (type.getValue() == code) {
				return type;
			}
		}
		return Status.Disabled;
	}

}
