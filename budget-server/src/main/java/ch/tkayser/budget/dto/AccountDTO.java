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
package ch.tkayser.budget.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import ch.tkayser.budget.base.dto.BaseDTO;

/**
 * @author isc-kat
 * 
 */
public class AccountDTO extends BaseDTO {

    //@formatter:off
    public static final String PROP_NAME     = "name";
    public static final String PROP_PARENT   = "parent";
    public static final String PROP_CHILDREN = "children";
    //@formatter:on

    private static final long serialVersionUID = -2196478642506690109L;

    private Long id;

    @NotNull
    @Length(min = 1, max = 255)
    private String name;

    private AccountDTO parent;

    private List<AccountDTO> children = new ArrayList<AccountDTO>();

    private Integer version;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccountDTO other = (AccountDTO)obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    /**
     * @return the children
     */
    public List<AccountDTO> getChildren() {
        return children;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parent
     */
    public AccountDTO getParent() {
        return parent;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(List<AccountDTO> children) {
        List<AccountDTO> oldChildren = this.children;
        this.children = children;
        firePropertyChange(PROP_CHILDREN, oldChildren, this.children);
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        firePropertyChange(PROP_NAME, oldName, this.name);
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(AccountDTO parent) {
        AccountDTO oldValue = this.parent;
        // remove us from the old parent if there was none
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        // set the other as parent
        this.parent = parent;
        if (this.parent != null) {
            // add us to the others children
            this.parent.children.add(this);
        }
        firePropertyChange(PROP_PARENT, oldValue, this.parent);
    }

    /**
     * set the parent. method for dozer to prevent problems with bi-directional relation
     * 
     * @param parent the parent to set
     */
    public void setParentForDozer(AccountDTO parent) {
        // set the other as parent
        this.parent = parent;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getId());
        out.append(": ");
        out.append(getName());
        if (getParent() != null) {
            out.append(" [").append(getParent().getName()).append("]");
        }
        return out.toString();

    }

}
