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

<%@ page contentType="text/html; charset=UTF-8" language="java"%>
        <div id="tm-offcanvas" class="uk-offcanvas">
        <div class="uk-offcanvas-bar">
            <ul class="uk-nav uk-nav-offcanvas uk-nav-parent-icon" data-uk-nav="{multiple:true}">
                <li class="uk-parent uk-active"><a href="#">Menu</a>
                    <ul class="uk-nav-sub">
	                    <li><a href="dashboard.jsp">대시보드</a></li>
	                    <li><a href="apis.jsp">APIS</a></li>
	                    <li><a href="plugins.jsp">PLUGINS</a></li>
	                    <li><a href="consumers.jsp">CONSUMERS</a></li>                                        
	                    <li><a href="logs.jsp">로그</a></li>
	                    <li><a href="statistics.jsp">통계</a></li>
                    </ul>
                </li>
                </ul>
        </div>
        </div>        
        
        <div class="tm-footer">
            <div class="uk-container uk-container-center uk-text-center">
                <div class="uk-panel">
                    <p>&copy; 토피도 주식회사 서울시 송파구 법원로 114 엠스테이트 B동 501호 Copyright(c) 2009 Allright Reserved</p>
                    <a href="index.jsp">Torpedo</a>
                </div>

            </div>
        </div>
		<div id="loadingModel" class="uk-modal uk-open" aria-hidden="false" style="display: none; overflow-y: scroll;">
        	<div class="uk-modal-spinner"></div>
        </div>