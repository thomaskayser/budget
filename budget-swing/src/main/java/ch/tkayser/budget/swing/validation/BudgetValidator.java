/*
 * Software is written by:
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2011
 * 
 */
package ch.tkayser.budget.swing.validation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.TraversableResolver;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tkayser.budget.base.dto.BaseDTO;
import ch.tkayser.budget.swing.formating.ValidationMessages;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Observable;

/**
 * Validator
 * 
 * Listeners can list to changes on the following atributes:
 * 
 * <ul>
 * <li>VALIDATION_OK</li>
 * <li>{ATTRIBUTENAME}</li>
 * 
 * @author tom
 * 
 */
public class BudgetValidator<B extends BaseDTO> implements Observable {

    // validation ok property.
    public static final String                              PROPERTY_VALIDATION_OK = "validationOk";

    // property for all violations. There is also a property for each attribute
    public static final String                              PROPERTY_VIOLATIONS    = "violations";

    // a logger
    private Logger                                          logger                 = LoggerFactory
                                                                                           .getLogger(BudgetValidator.class);

    // The violations.
    // the key of the map is the attribute
    // the values is a map with the violations for the attribut
    // the key of the violations is the source and the list is a list of
    // (translated) violation strings

    private Map<String, Map<ViolationSource, List<String>>> allViolations          = Collections
                                                                                           .synchronizedMap(new HashMap<String, Map<ViolationSource, List<String>>>());

    // support for property changes
    private PropertyChangeSupport                           changes                = new PropertyChangeSupport(this);

    // the presentation model
    private final PresentationModel<B>                      model;

    // a validator
    private Validator                                       validator;

    /**
     * Validator erstellen
     * 
     */
    public BudgetValidator(PresentationModel<B> model) {
        this.model = model;
        // add us a listener to the propertys of the bean in the model
        model.addBeanPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                validateBean();
            }
        });

        // liste for changes of the bean of the model
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(PresentationModel.PROPERTYNAME_AFTER_BEAN)) {
                    validateBean();
                }
            }
        });

        // create a validator. dont use the default traversable resolver as this one uses a jpaTraversable resolver
        Configuration<?> configuration = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = configuration.traversableResolver(new TraversableResolver() {
            @Override
            public boolean isReachable(Object traversableObject, Node traversableProperty, Class<?> rootBeanType,
                    Path pathToTraversableObject, ElementType elementType) {
                return true;
            }

            @Override
            public boolean isCascadable(Object traversableObject, Node traversableProperty, Class<?> rootBeanType,
                    Path pathToTraversableObject, ElementType elementType) {
                return true;
            }
        }).buildValidatorFactory();
        validator = factory.getValidator();

        // validate the bean
        validateBean();

    }

    /**
     * remove a property change listener
     * 
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    /**
     * clear the input Violations for an aatribute
     * 
     * @param attributeName
     * @param violation
     */
    public void clearInputViolations(String attributeName) {
        setInputViolations(attributeName, null);
    }

    /**
     * Prueft ob eine Liste leer oder null ist
     * 
     * @param existingViolations
     * @return
     */
    private boolean collectionEmptyOrNull(Collection<String> violations) {
        if (violations == null || violations.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * holt den Attribut Namen aus dem Path
     * 
     * @param propertyPath
     * @return
     */
    private String getAttributeName(Path propertyPath) {
        Iterator<Node> iterator = propertyPath.iterator();
        StringBuilder attributeName = new StringBuilder();
        while (iterator.hasNext()) {
            if (attributeName.length() > 0) {
                attributeName.append(".");
            }
            attributeName.append(iterator.next().getName());
        }
        return attributeName.toString();

    }

    /**
     * gibt alle Violations fuer ein Attribut zurueck
     * 
     * @param attributName
     * @return
     */
    public Collection<String> getViolations(String attributeName) {
        // get all Violations for an attribut
        List<String> violations = new ArrayList<String>();
        synchronized (allViolations) {
            Map<ViolationSource, List<String>> attributViolations = allViolations.get(attributeName);
            if (attributViolations != null) {
                for (List<String> sourceViolations : attributViolations.values()) {
                    violations.addAll(sourceViolations);
                }
            }
        }
        return violations;
    }

    /**
     * liefert alle violations zurueck
     * 
     * @return
     */
    public Collection<String> getViolations() {
        List<String> violations = new ArrayList<String>();
        synchronized (allViolations) {
            for (String attributeName : allViolations.keySet()) {
                violations.addAll(getViolations(attributeName));
            }
        }
        return violations;
    }

    /**
     * add a property change listener
     * 
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }

    /**
     * set the input violations for an attribut
     * 
     * @param attributeName
     * @param violation
     */
    public void setInputViolation(String attributeName, String violation) {
        List<String> violations = new ArrayList<String>();
        if (violation != null) {
            violations.add(violation);
        }
        setInputViolations(attributeName, violations);
    }

    /**
     * Set the input violations for an attribut
     * 
     * @param attributeName
     *            das objekt
     * @param violations
     *            the liste mit violations. Null oder ein Leeres Violation Set
     *            wenn keine Violations vorliegen
     */
    public void setInputViolations(String attributeName, List<String> violations) {
        setViolations(attributeName, ViolationSource.INPUT, violations);
    }

    /**
     * Set the validations for an attribut.
     * 
     * the method fires propertyChange Events for the PROPERTY_VALIDATION_OK and
     * the attribute
     * 
     * @param attributeName
     * @param source
     * @param violations
     */
    private void setViolations(String attributeName, ViolationSource source, List<String> violations) {

        // get state before change:
        // violations
        Collection<String> beforeViolations = getViolations();
        Collection<String> afterViolations = null;
        // attribut violations
        Collection<String> beforeAttributViolations = getViolations(attributeName);
        Collection<String> afterAttributViolations = null;
        // validationOk
        boolean beforeValidationOk = getValidationOk();
        boolean afterValidationOk = beforeValidationOk;

        synchronized (allViolations) {

            // bestehende attribut violations holen (oder erstellen)
            Map<ViolationSource, List<String>> attributViolations = allViolations.get(attributeName);
            if (attributViolations == null) {
                attributViolations = new HashMap<ViolationSource, List<String>>();
                allViolations.put(attributeName, attributViolations);
            }
            // violations ersetzen/entfernen
            if (violations == null || violations.size() == 0) {
                removeViolations(attributeName, ViolationSource.INPUT);
            } else {
                // set violations for the source
                attributViolations.put(source, violations);
            }

            logViolations();

            // get new stats
            afterAttributViolations = getViolations(attributeName);
            afterViolations = getViolations();
            afterValidationOk = getValidationOk();
        }
        // fire changes

        // validation ok
        changes.firePropertyChange(PROPERTY_VALIDATION_OK, beforeValidationOk, afterValidationOk);

        // violations
        changes.firePropertyChange(PROPERTY_VIOLATIONS, beforeViolations, afterViolations);

        // changed attribute
        changes.firePropertyChange(attributeName, beforeAttributViolations, afterAttributViolations);

    }

    /**
     * 
     */
    private void logViolations() {
        logger.debug(allViolations.toString());
    }

    /**
     * bean validieren
     * @return 
     * 
     */
    public Set<ConstraintViolation<BaseDTO>> validateBean() {

        boolean beforeValidationOk = getValidationOk();
        Collection<String> beforeViolations = getViolations();

        // alle violations mit ViolationSource Business entfernen
        clearBusinessViolations();

        // bean validieren
        BaseDTO bean = model.getBean();
        Set<ConstraintViolation<BaseDTO>> violations = validator.validate(bean);

        // violations nach attribut gruppieren
        Map<String, List<ConstraintViolation<BaseDTO>>> groupedViolations = new HashMap<String, List<ConstraintViolation<BaseDTO>>>();
        for (ConstraintViolation<BaseDTO> violation : violations) {
            String attributeName = getAttributeName(violation.getPropertyPath());
            List<ConstraintViolation<BaseDTO>> violationList = groupedViolations.get(attributeName);
            if (violationList == null) {
                violationList = new ArrayList<ConstraintViolation<BaseDTO>>();
                groupedViolations.put(attributeName, violationList);
            }
            violationList.add(violation);
        }

        // violates setzen
        for (String attributeName : groupedViolations.keySet()) {
            List<String> violationMessages = new ArrayList<String>();
            for (ConstraintViolation<BaseDTO> constraintViolation : groupedViolations.get(attributeName)) {
                violationMessages.add(ValidationMessages.getMessage(constraintViolation.getMessage(), attributeName));
                setViolations(attributeName, ViolationSource.BUSINESS, violationMessages);
            }
        }

        boolean afterValidationOk = getValidationOk();
        Collection<String> afterViolations = getViolations();

        // Property Change feuern: validation ok
        changes.firePropertyChange(PROPERTY_VALIDATION_OK, beforeValidationOk, afterValidationOk);

        // Property Change: violations
        changes.firePropertyChange(PROPERTY_VIOLATIONS, beforeViolations, afterViolations);

        logViolations();
        
        // gibt die violations zurueck
        return violations;

    }

    /**
     * remove violations for an attribute and source
     * 
     * @param attributeName
     * @param source
     */
    private void removeViolations(String attributeName, ViolationSource source) {
        synchronized (allViolations) {
            // source entfernen
            allViolations.get(attributeName).remove(source);
            // wenn keine source mehr vorhanden attribute komplett entfernen
            if (allViolations.get(attributeName).values().size() == 0) {
                allViolations.remove(attributeName);
            }
        }
        logViolations();
    }

    /**
     * Entfernt alle violations mit der ViolationSource.Business
     * 
     */
    private void clearBusinessViolations() {
        synchronized (allViolations) {
            // remove business violations for all attribues. store attribute
            // names in list to prevent concurrentmodificationException

            List<String> attributes = new ArrayList<String>(allViolations.keySet());
            for (String attributeName : attributes) {
                List<String> existingViolations = allViolations.get(attributeName).get(ViolationSource.BUSINESS);
                removeViolations(attributeName, ViolationSource.BUSINESS);
                changes.firePropertyChange(attributeName, existingViolations, null);
            }
        }
    }

    /**
     * is the validation ok?
     * 
     * @return
     */
    public boolean getValidationOk() {
        return collectionEmptyOrNull(allViolations.keySet());
    }

}
