 /*
 * Software is written by:
 *
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2009
 * 
 */
package ch.tkayser.budget.base.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ch.tkayser.budget.base.dao.BaseDAO;

/**
 * @author isc-kat
 * 
 */
public class BaseDAOImpl implements BaseDAO {

    @PersistenceContext
    protected EntityManager m_em;

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.dao.BudgetBaseDAO#deleteEntity(java.lang.Object)
     */
    public <T> void deleteEntity(T entity) {
	entity = m_em.merge(entity);
	m_em.remove(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.dao.BudgetBaseDAO#saveEntity(java.lang.Object)
     */
    public <T> T saveEntity(T entity) {
	entity = m_em.merge(entity);
	return entity;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.tkayser.budget.dao.BudgetBaseDAO#findAllentitties(java.lang.Class)
     */
    public <T> List<T> findAllentitties(Class<T> entityClass) {
    	return findAllentitties(entityClass, null);
    }
    
    

    /* (non-Javadoc)
	 * @see ch.tkayser.budget.base.dao.BaseDAO#findAllentitties(java.lang.Class, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAllentitties(Class<T> entityClass, String orderByClause) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT e from " + entityClass.getSimpleName() + " e ");
		if (orderByClause != null) {
			query.append(" order by "+orderByClause);
		}
		return m_em.createQuery(query.toString()).getResultList();
	}

	/*
     * (non-Javadoc)
     * 
     * @see ch.tkayser.budget.dao.BudgetBaseDAO#findEntityById(java.lang.Class,
     * java.lang.Long)
     */
    public <T> T findEntityById(Class<T> entityClass, Long id) {
	return m_em.find(entityClass, id);
    }

}
