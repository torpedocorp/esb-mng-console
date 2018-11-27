package kr.co.bizframe.esb.mng.dao;

import static kr.co.bizframe.esb.mng.type.Constants.CONFIG_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.SESSIONFACTORY_NAME;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import kr.co.bizframe.esb.mng.model.config.Agent;

@Repository
public class AgentDao {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	@Qualifier(value = CONFIG_DB_KEY + SESSIONFACTORY_NAME)
	private EntityManagerFactory emf;

	public String save(Agent agent) {
		EntityManager session = emf.createEntityManager();
		session.getTransaction().begin();
		session.persist(agent);
		session.getTransaction().commit();
		session.close();
		return agent.getAgentId();
	}

	public Agent get(String id) {
		EntityManager session = emf.createEntityManager();
		Agent agent = session.find(Agent.class, id);
		session.close();
		return agent;
	}

	public List<Agent> list() {
		EntityManager session = emf.createEntityManager();
		/*List<Person> persons = em.createQuery("Select t from Person t").getResultList();*/
		CriteriaBuilder cb = emf.getCriteriaBuilder();
		CriteriaQuery<Agent> cq = cb.createQuery(Agent.class);
		Root<Agent> root = cq.from(Agent.class);
		cq.select(root);
		TypedQuery<Agent> query = session.createQuery(cq);
		List<Agent> list = query.getResultList();
		session.close();
		return list;
	}

	public void update(Agent agent) {
		EntityManager session = emf.createEntityManager();
		session.getTransaction().begin();
		session.merge(agent);
		session.getTransaction().commit();
		session.close();
	}

	public void delete(String id) {
		EntityManager em = emf.createEntityManager();
		Agent agent = em.find(Agent.class, id);
		if (agent != null) {
			em.getTransaction().begin();
			em.remove(agent);
			em.getTransaction().commit();
		}
		em.close();
	}

}
