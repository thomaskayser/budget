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
package ch.tkayser.budget.base.service.impl;

import org.dozer.DozerBeanMapperSingletonWrapper;

public class BaseServiceImpl {

    /**
     * map an object to another one
     */
    public <E> E map(Object sourceObject, E targetObject) {
        DozerBeanMapperSingletonWrapper.getInstance().map(sourceObject, targetObject);
        return targetObject;
    }
}
