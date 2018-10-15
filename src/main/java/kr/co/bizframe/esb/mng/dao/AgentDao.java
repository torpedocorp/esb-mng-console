package kr.co.bizframe.esb.mng.dao;

import static kr.co.bizframe.esb.mng.type.Constants.CONFIG_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.SESSIONFACTORY_NAME;
import static kr.co.bizframe.esb.mng.type.Constants.TRANSACTIONMANAGER_NAME;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bizframe.esb.mng.model.config.Agent;

@Repository
@Transactional(transactionManager = CONFIG_DB_KEY + TRANSACTIONMANAGER_NAME)
public class AgentDao {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	@Qualifier(value = CONFIG_DB_KEY + SESSIONFACTORY_NAME)
	private SessionFactory sessionFactory;

	public String save(Agent agent) {
		sessionFactory.getCurrentSession().save(agent);
		return agent.getAgentId();
	}

	public Agent get(String id) {
		return sessionFactory.getCurrentSession().get(Agent.class, id);
	}

	public List<Agent> list() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Agent> cq = cb.createQuery(Agent.class);
		Root<Agent> root = cq.from(Agent.class);
		cq.select(root);
		Query<Agent> query = session.createQuery(cq);
		return query.getResultList();
	}

	public void update(Agent agent) {
		Session session = sessionFactory.getCurrentSession();
		session.update(agent.getAgentId(), agent);
	}

	public void delete(String id) {
		Session session = sessionFactory.getCurrentSession();
		Agent agent = session.byId(Agent.class).load(id);
		session.delete(agent);
	}

}
