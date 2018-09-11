package kr.co.bizframe.esb.mng.dao;

import static kr.co.bizframe.esb.mng.type.Constants.SESSIONFACTORY_NAME;
import static kr.co.bizframe.esb.mng.type.Constants.TRACE_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.TRANSACTIONMANAGER_NAME;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bizframe.esb.mng.model.PagingModel;
import kr.co.bizframe.esb.mng.model.SearchOptions;
import kr.co.bizframe.esb.mng.model.trace.ExchangeInfo;
import kr.co.bizframe.esb.mng.model.trace.ExchangeStatisticInfo;
import kr.co.bizframe.esb.mng.utils.PagingUtil;
import kr.co.bizframe.esb.mng.utils.Strings;

@Repository
@Transactional(transactionManager = TRACE_DB_KEY + TRANSACTIONMANAGER_NAME)
public class ExchangeInfoDao {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	@Qualifier(value = TRACE_DB_KEY + SESSIONFACTORY_NAME)
	private SessionFactory sessionFactory;

	public void save(ExchangeInfo msg) {
		sessionFactory.getCurrentSession().save(msg);		
	}

	public ExchangeInfo get(String id) {		
		return sessionFactory.getCurrentSession().get(ExchangeInfo.class, id);
	}

	public List<ExchangeInfo> list() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<ExchangeInfo> cq = cb.createQuery(ExchangeInfo.class);
		Root<ExchangeInfo> root = cq.from(ExchangeInfo.class);
		cq.select(root);
		Query<ExchangeInfo> query = session.createQuery(cq);
		return query.getResultList();
	}
	
	private Predicate getPredicate(CriteriaBuilder cb, SearchOptions options, Date fromDate, Date toDate,
			Root<ExchangeInfo> root) {
		String searchKey = options.getSearchKey();
		boolean search = false;
		Object searchValue = options.getStrSearch();

		if (Strings.trim(searchKey) != null && Strings.trim(options.getStrSearch()) != null) {
			search = true;
			if ("success".equals(searchKey)) {
				searchValue = Boolean.parseBoolean(options.getStrSearch());
			}
		}
		Predicate pd0 = cb.and(cb.greaterThanOrEqualTo(root.<Date>get("created"), fromDate),
				cb.lessThan(root.<Date>get("created"), toDate));
		if (search) {
			pd0.getExpressions().add(cb.equal(root.get(searchKey), searchValue));
		}
		
		if (Strings.trim(options.getAgentId()) != null) {
			pd0.getExpressions().add(cb.equal(root.get("agentId"), options.getAgentId()));
		}

		if (Strings.trim(options.getRouteId()) != null) {
			pd0.getExpressions().add(cb.equal(root.get("routeId"), options.getRouteId()));
		}
		return pd0;
	}
	
	public PagingModel<ExchangeInfo> pagedList(SearchOptions options) {
		PagingModel<ExchangeInfo> vo = new PagingModel<>();
		try {
			PagingUtil.getPageArgs(options);

			Session session = sessionFactory.getCurrentSession();
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
			countCq.where(getPredicate(cb, options, fromDate, toDate, root4));
	        countCq.select(cb.countDistinct(root4));
	        Query<Long> query4 = session.createQuery(countCq);
	        long distinct = query4.getSingleResult();
	        vo.setCount(distinct);
	        
			CriteriaQuery<ExchangeInfo> cq = cb.createQuery(ExchangeInfo.class);
			Root<ExchangeInfo> root = cq.from(ExchangeInfo.class);			
			cq.where(getPredicate(cb, options, fromDate, toDate, root));			
			cq.select(root);
			cq.orderBy(cb.desc(root.get("created")));
			Query<ExchangeInfo> query = session.createQuery(cq);
			query.setFirstResult(options.getIndex());
			query.setMaxResults(options.getLimit());
			vo.setModels(query.getResultList());
			return vo;
			
		} catch (Throwable e) {
			logger.error("list " + options + " error " + e.getMessage(), e);
			return null;
		}
	}
	
	public void update(String id, ExchangeInfo msg) {
		Session session = sessionFactory.getCurrentSession();
		session.byId(ExchangeInfo.class).load(id);
		session.flush();
	}

	public void delete(String id) {
		Session session = sessionFactory.getCurrentSession();
		ExchangeInfo msg = session.byId(ExchangeInfo.class).load(id);
		session.delete(msg);
	}
	
	public Collection<ExchangeStatisticInfo> getExchageStatisticInfos(SearchOptions options) {
		try {
			PagingUtil.getPageArgs(options);

			String searchKey = options.getSearchKey();
			boolean search = false;

			if (Strings.trim(searchKey) != null) {
				search = true;
			}

			List<ExchangeStatisticInfo> list = getExchageStatisticInfos(options.getFromDate(), options.getToDate());

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
	
	public List<ExchangeStatisticInfo> getExchageStatisticInfos(String fromDate, String toDate) {
		StringBuilder sb = new StringBuilder();
		sb.append("select");
		sb.append("   count(*) as count, agentid as agentId, routeid as routeId, success, date(CREATED) as createDate");
		sb.append("  from BIZFRAME_CAMEL_FINISHED_EXCHANGE");
		sb.append(" where date(CREATED)");
		if (toDate == null) {
			sb.append(" = :date");
		} else {
			sb.append(" between :date and :date1");	
		}		
		sb.append("   group by agentid, routeid, success, date(CREATED)");
		String sql = sb.toString();

		Session session = sessionFactory.getCurrentSession();
		Query<ExchangeStatisticInfo> query = session.createSQLQuery(sql).
				addEntity(ExchangeStatisticInfo.class)
				.setParameter("date", fromDate);
		
		if (toDate != null) {
			query.setParameter("date1", toDate);
		}
		return query.list();
	}
	
	public List<ExchangeStatisticInfo> getExchageStatisticInfos(String date) {
		StringBuilder sb = new StringBuilder();
		sb.append("select tbl.*, ");
		sb.append(date);
		sb.append("  as createDate");
		sb.append("  from (");
		sb.append("		select");
		sb.append("   	   count(*) as count, agentid as agentId, routeid as routeId, success");
		sb.append("  	  from BIZFRAME_CAMEL_FINISHED_EXCHANGE");
		sb.append("      where date(CREATED) = :date");
		sb.append("   		group by agentid, routeid, success");
		sb.append(") as tbl");
		String sql = sb.toString();

		Session session = sessionFactory.getCurrentSession();
		Query<ExchangeStatisticInfo> query = session.createSQLQuery(sql).
				addEntity(ExchangeStatisticInfo.class)
				.setParameter("date", date);
		return query.list();
	}
}
