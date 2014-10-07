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
package ch.tkayser.budget.base.dao;

import java.util.List;

public interface BaseDAO {
    
 
    public <T> T saveEntity(T entity);
    public <T> void deleteEntity(T entity);
    public <T> T findEntityById(Class<T> entityClass, Long id);   
    public <T> List<T> findAllentitties(Class<T> entityClass);
    public <T> List<T> findAllentitties(Class<T> entityClass, String orderByClause);
    
    

}
