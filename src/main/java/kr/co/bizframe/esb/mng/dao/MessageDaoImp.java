package kr.co.bizframe.esb.mng.dao;

import static kr.co.bizframe.esb.mng.type.Constants.SESSIONFACTORY_NAME;
import static kr.co.bizframe.esb.mng.type.Constants.TRACE_DB_KEY;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
import kr.co.bizframe.esb.mng.model.trace.TraceMessage;
import kr.co.bizframe.esb.mng.utils.PagingUtil;
import kr.co.bizframe.esb.mng.utils.Strings;

@Repository
public class MessageDaoImp implements MessageDao {

	private Logger logger = Logger.getLogger(getClass());

	
	@Autowired
	@Qualifier(value = TRACE_DB_KEY + SESSIONFACTORY_NAME)
	private EntityManagerFactory emf;

	@Override
	public TraceMessage get(String id) {
		EntityManager session = emf.createEntityManager();
		TraceMessage info = session.find(TraceMessage.class, id);
		session.close();
		return info;
	}

	@Override
	public PagingModel<TraceMessage> paging(SearchOptions options) {
		PagingModel<TraceMessage> vo = new PagingModel<>();
		try {
			PagingUtil.getPageArgs(options);

			EntityManager session = emf.createEntityManager();
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
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.and(cb.greaterThanOrEqualTo(root4.<Date>get("timestamp"), fromDate),cb.lessThan(root4.<Date>get("timestamp"), toDate)));
			if (search) {
				predicates.add(cb.equal(root4.get(searchKey), options.getStrSearch()));
			}
			
			countCq.select(cb.countDistinct(root4));
			countCq.where(predicates.toArray(new Predicate[]{}));
			
	        TypedQuery<Long> query4 = session.createQuery(countCq);
	        long distinct = query4.getSingleResult();
	        vo.setCount(distinct);
	        
			CriteriaQuery<TraceMessage> cq = cb.createQuery(TraceMessage.class);
			Root<TraceMessage> root = cq.from(TraceMessage.class);
			predicates = new ArrayList<Predicate>();
			predicates.add(cb.and(cb.greaterThanOrEqualTo(root.<Date>get("timestamp"), fromDate),cb.lessThan(root.<Date>get("timestamp"), toDate)));
			if (search) {
				predicates.add(cb.equal(root.get(searchKey), options.getStrSearch()));
			}
			cq.select(root);
			cq.where(predicates.toArray(new Predicate[]{}));
			cq.orderBy(cb.desc(root.get("timestamp")));
			TypedQuery<TraceMessage> query = session.createQuery(cq);
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

	@Override
	public List<TraceMessage> exchangeTraces(SearchOptions options) {
		try {
			EntityManager session = emf.createEntityManager();
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
			cq.orderBy(cb.asc(root.get("timestamp")));
			TypedQuery<TraceMessage> query = session.createQuery(cq);
			List<TraceMessage> list = query.getResultList();
			session.close();
			return list;

		} catch (Throwable e) {
			logger.error("exchangeTraces " + options + " error " + e.getMessage(), e);
			return null;
		}
	}

}
