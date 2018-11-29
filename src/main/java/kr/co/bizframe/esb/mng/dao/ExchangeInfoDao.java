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

package kr.co.bizframe.esb.mng.dao;

import static kr.co.bizframe.esb.mng.type.Constants.SESSIONFACTORY_NAME;
import static kr.co.bizframe.esb.mng.type.Constants.TRACE_DB_KEY;
import static kr.co.bizframe.esb.mng.utils.Strings.trim;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import kr.co.bizframe.esb.mng.model.PagingModel;
import kr.co.bizframe.esb.mng.model.SearchOptions;
import kr.co.bizframe.esb.mng.model.trace.ExchangeInfo;
import kr.co.bizframe.esb.mng.model.trace.ExchangeStatisticInfo;
import kr.co.bizframe.esb.mng.utils.PagingUtil;

@Repository
public class ExchangeInfoDao {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	@Qualifier(value = TRACE_DB_KEY + SESSIONFACTORY_NAME)
	private EntityManagerFactory emf;

	public void save(ExchangeInfo msg) {
		EntityManager session = emf.createEntityManager();
		session.getTransaction().begin();
		session.persist(msg);
		session.getTransaction().commit();
		session.close();
	}

	public ExchangeInfo get(String id) {
		EntityManager session = emf.createEntityManager();
		ExchangeInfo info = session.find(ExchangeInfo.class, id);
		session.close();
		return info;
	}

	public List<ExchangeInfo> list() {
		EntityManager session = emf.createEntityManager();
		CriteriaBuilder cb = emf.getCriteriaBuilder();
		CriteriaQuery<ExchangeInfo> cq = cb.createQuery(ExchangeInfo.class);
		Root<ExchangeInfo> root = cq.from(ExchangeInfo.class);
		cq.select(root);
		TypedQuery<ExchangeInfo> query = session.createQuery(cq);
		List<ExchangeInfo> list = query.getResultList();
		session.close();
		return list;
	}

	private Predicate[] getPredicate(CriteriaBuilder cb, SearchOptions options, Date fromDate, Date toDate,
			Root<ExchangeInfo> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		String searchKey = options.getSearchKey();
		boolean search = false;
		Object searchValue = options.getStrSearch();

		if (trim(searchKey) != null && trim(options.getStrSearch()) != null) {
			search = true;
			if ("success".equals(searchKey)) {
				searchValue = Boolean.parseBoolean(options.getStrSearch());
			}
		}
		predicates.add(cb.and(cb.greaterThanOrEqualTo(root.<Date>get("created"), fromDate),
				cb.lessThan(root.<Date>get("created"), toDate)));
		if (search) {
			predicates.add(cb.equal(root.get(searchKey), searchValue));
		}

		if (trim(options.getAgentId()) != null) {
			predicates.add(cb.equal(root.get("agentId"), options.getAgentId()));
		}

		if (trim(options.getRouteId()) != null) {
			predicates.add(cb.equal(root.get("routeId"), options.getRouteId()));
		}
		return predicates.toArray(new Predicate[]{});
	}

	public PagingModel<ExchangeInfo> pagedList(SearchOptions options) {
		PagingModel<ExchangeInfo> vo = new PagingModel<>();
		try {
			PagingUtil.getPageArgs(options);
			EntityManager session = emf.createEntityManager();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			logger.debug(options);

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date fromDate = df.parse(options.getFromDate());
			Date toDate = df.parse(options.getToDate());
			Calendar cal = Calendar.getInstance(Locale.KOREAN);
			cal.setTime(toDate);
			cal.add(Calendar.DATE, 1);
			toDate = cal.getTime();

			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<ExchangeInfo> root4 = countCq.from(ExchangeInfo.class);
			countCq.select(cb.countDistinct(root4));
			countCq.where(getPredicate(cb, options, fromDate, toDate, root4));			
			TypedQuery<Long> query4 = session.createQuery(countCq);
			long distinct = query4.getSingleResult();
			vo.setCount(distinct);

			CriteriaQuery<ExchangeInfo> cq = cb.createQuery(ExchangeInfo.class);
			Root<ExchangeInfo> root = cq.from(ExchangeInfo.class);
			cq.select(root);
			cq.where(getPredicate(cb, options, fromDate, toDate, root));
			cq.orderBy(cb.desc(root.get("created")));
			TypedQuery<ExchangeInfo> query = session.createQuery(cq);
			query.setFirstResult(options.getIndex());
			query.setMaxResults(options.getLimit());
			vo.setModels(query.getResultList());
			session.close();
			return vo;

		} catch (Throwable e) {
			logger.error("list " + options + " error " + e.getMessage(), e);
			return null;
		}
	}

	public Collection<ExchangeStatisticInfo> getExchageStatisticInfos(SearchOptions options) {
		try {
			PagingUtil.getPageArgs(options);

			String searchKey = options.getSearchKey();
			boolean search = false;

			if (trim(searchKey) != null) {
				search = true;
			}

			String searchValue = options.getStrSearch();
			List<ExchangeStatisticInfo> list = getExchageStatisticInfos(options.getFromDate(), options.getToDate(),
					searchKey, searchValue);

			if (search) {
				// <date_key, <groupbyKeyValue, info>>
				Map<String, ExchangeStatisticInfo> result = new HashMap<>();

				for (ExchangeStatisticInfo info : list) {
					String value = info.getCreateDate() + "_";
					if ("success".equals(searchKey)) {
						value += info.isSuccess();
					} else if ("agentId".equals(searchKey)) {
						value += info.getAgentId();
					} else if ("routeId".equals(searchKey)) {
						value += info.getRouteId();
					}

					ExchangeStatisticInfo info0 = result.get(value);

					if (info0 == null) {
						info0 = new ExchangeStatisticInfo();

						if ("success".equals(searchKey)) {
							info0.setAgentId("");
							info0.setRouteId("");
							info0.setSuccess(info.isSuccess());

						} else if ("agentId".equals(searchKey)) {
							info0.setAgentId(info.getAgentId());
							info0.setRouteId("");
							info0.setSuccess(false);

						} else if ("routeId".equals(searchKey)) {
							info0.setAgentId("");
							info0.setRouteId(info.getRouteId());
							info0.setSuccess(false);
						}

						info0.setCount(info.getCount());
						info0.setCreateDate(info.getCreateDate());
					} else {
						info0.setCount(info0.getCount() + info.getCount());
					}

					result.put(value, info0);
				}

				return result.values();

			} else {
				return list;
			}

		} catch (Throwable e) {
			logger.error("getExchageStatisticInfos " + options + " error " + e.getMessage(), e);
		}

		return null;
	}

	public List<ExchangeStatisticInfo> getExchageStatisticInfos(String fromDate, String toDate, String searchKey,
			String searchValue) {
		boolean search = false;
		if (trim(searchKey) != null && trim(searchValue) != null) {
			search = true;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("select");
		sb.append("   count(*) as count, agentid as agentId, routeid as routeId, success, date(CREATED) as createDate");
		sb.append("  from BIZFRAME_CAMEL_FINISHED_EXCHANGE");
		sb.append(" where date(CREATED) between ?1 and ?2");
		if (search) {
			sb.append(" and ");
			sb.append(searchKey);
			sb.append(" = ?3 ");
		}
		sb.append("   group by agentid, routeid, success, date(CREATED)");
		String sql = sb.toString();

		EntityManager session = emf.createEntityManager();
		Query query = session.createNativeQuery(sql, ExchangeStatisticInfo.class).setParameter(1, fromDate).setParameter(2, toDate);
		if (search) {
			query.setParameter(3, searchValue);
		}
		List<ExchangeStatisticInfo> list = query.getResultList();
		session.close();
		return list;
	}

	public List<ExchangeStatisticInfo> getExchageStatisticInfos(String date) {
		StringBuilder sb = new StringBuilder();
		sb.append("select tbl.*, '");
		sb.append(date);
		sb.append("' as createDate");
		sb.append("  from (");
		sb.append("		select");
		sb.append("   	   count(*) as count, agentid as agentId, routeid as routeId, success");
		sb.append("  	  from BIZFRAME_CAMEL_FINISHED_EXCHANGE");
		sb.append("      where date(CREATED) = ?1 ");
		sb.append("   		group by agentid, routeid, success");
		sb.append(") as tbl");
		String sql = sb.toString();

		EntityManager session = emf.createEntityManager();
		Query query = session.createNativeQuery(sql, ExchangeStatisticInfo.class).setParameter(1, date);
		List<ExchangeStatisticInfo> list = query.getResultList();
		session.close();
		return list;
	}
}
