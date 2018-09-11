package kr.co.bizframe.esb.mng.dao;

import static kr.co.bizframe.esb.mng.type.Constants.SESSIONFACTORY_NAME;
import static kr.co.bizframe.esb.mng.type.Constants.TRACE_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.TRANSACTIONMANAGER_NAME;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import kr.co.bizframe.esb.mng.model.trace.TraceMessage;
import kr.co.bizframe.esb.mng.utils.PagingUtil;
import kr.co.bizframe.esb.mng.utils.Strings;

@Repository
@Transactional(readOnly = true, transactionManager=TRACE_DB_KEY + TRANSACTIONMANAGER_NAME)
public class MessageDaoImp implements MessageDao {

	private Logger logger = Logger.getLogger(getClass());

	
	@Autowired
	@Qualifier(value = TRACE_DB_KEY + SESSIONFACTORY_NAME)
	private SessionFactory sessionFactory;

	@Override
	public String save(TraceMessage msg) {
		sessionFactory.getCurrentSession().save(msg);
		return msg.getId();
	}

	@Override
	public TraceMessage get(String id) {
		return sessionFactory.getCurrentSession().get(TraceMessage.class, id);
	}

	@Override
	public PagingModel<TraceMessage> paging(SearchOptions options) {
		PagingModel<TraceMessage> vo = new PagingModel<>();
		try {
			PagingUtil.getPageArgs(options);

			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder cb = session.getCriteriaBuilder();			
			logger.debug(options);
			String searchKey = options.getSearchKey();
			boolean search = false;
			if (Strings.trim(searchKey) != null && Strings.trim(options.getStrSearch()) != null) {
				search = true;				
			}
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date fromDate = df.parse(options.getFromDate());
			Date toDate = df.parse(options.getToDate());
			Calendar cal = Calendar.getInstance(Locale.KOREAN);
			cal.setTime(toDate);
			cal.add(Calendar.DATE, 1);
			toDate = cal.getTime();
			
			CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
			Root<TraceMessage> root4 = countCq.from(TraceMessage.class);
			Predicate pd0 = cb.and(cb.greaterThanOrEqualTo(root4.<Date>get("timestamp"), fromDate),cb.lessThan(root4.<Date>get("timestamp"), toDate));
			if (search) {
				pd0.getExpressions().add(cb.equal(root4.get(searchKey), options.getStrSearch()));
			}
			countCq.where(pd0);
	        countCq.select(cb.countDistinct(root4));
	        Query<Long> query4 = session.createQuery(countCq);
	        long distinct = query4.getSingleResult();
	        vo.setCount(distinct);
	        
			CriteriaQuery<TraceMessage> cq = cb.createQuery(TraceMessage.class);
			Root<TraceMessage> root = cq.from(TraceMessage.class);
			Predicate pd = cb.and(cb.greaterThanOrEqualTo(root.<Date>get("timestamp"), fromDate),cb.lessThan(root.<Date>get("timestamp"), toDate));
			if (search) {
				pd.getExpressions().add(cb.equal(root.get(searchKey), options.getStrSearch()));
			}
			cq.where(pd);
			cq.select(root);
			cq.orderBy(cb.desc(root.get("timestamp")));
			Query<TraceMessage> query = session.createQuery(cq);
			query.setFirstResult(options.getIndex());
			query.setMaxResults(options.getLimit());
			vo.setModels(query.getResultList());
			return vo;
			
		} catch (Throwable e) {
			logger.error("list " + options + " error " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void update(String id, TraceMessage msg) {
		Session session = sessionFactory.getCurrentSession();
		session.byId(TraceMessage.class).load(id);
		session.flush();
	}

	@Override
	public void delete(String id) {
		Session session = sessionFactory.getCurrentSession();
		TraceMessage msg = session.byId(TraceMessage.class).load(id);
		session.delete(msg);
	}

	@Override
	public List<TraceMessage> exchangeTraces(SearchOptions options) {
		try {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder cb = session.getCriteriaBuilder();
			logger.debug(options);

			CriteriaQuery<TraceMessage> cq = cb.createQuery(TraceMessage.class);
			Root<TraceMessage> root = cq.from(TraceMessage.class);

			Object searchValue = options.getStrSearch();
			String searchKey = options.getSearchKey();
			Predicate pd = cb.and(cb.equal(root.get(searchKey), searchValue));

			if (Strings.trim(options.getAgentId()) != null) {
				pd.getExpressions().add(cb.equal(root.get("agentId"), options.getAgentId()));
			}

			if (Strings.trim(options.getRouteId()) != null) {
				pd.getExpressions().add(cb.equal(root.get("routeId"), options.getRouteId()));
			}

			cq.where(pd);
			cq.select(root);
			cq.orderBy(cb.desc(root.get("timestamp")));
			Query<TraceMessage> query = session.createQuery(cq);
			return query.getResultList();

		} catch (Throwable e) {
			logger.error("exchangeTraces " + options + " error " + e.getMessage(), e);
			return null;
		}
	}

}
