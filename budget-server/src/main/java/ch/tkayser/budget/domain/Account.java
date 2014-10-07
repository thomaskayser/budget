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
package ch.tkayser.budget.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * @author isc-kat
 * 
 */
@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable  {

    private static final long serialVersionUID = -6304084081046243170L;
    
    public static String NAME_ATTRIBUT = "m_name";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_SEQ")
    @SequenceGenerator(name = "GEN_SEQ", sequenceName = "ACCOUNT_SEQ", allocationSize = 10, initialValue = 1)
    @Column(name = "ACT_ID")
    private Long          m_id;

    @Column(name = "ACT_NAME", nullable=false)
    private String        m_name;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "ACT_PARENT_FK")
    private Account       m_parent;

    @OneToMany(mappedBy = "m_parent", fetch=FetchType.EAGER)
    private List<Account> m_children;

    @Version
    @Column(name = "ACT_VERSION")
    private Integer m_version;

    public Account() {
        super();
        m_children = new ArrayList<Account>();
    }

    /* (non-Javadoc)
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
	Account other = (Account) obj;
	if (m_id == null) {
	    if (other.m_id != null)
		return false;
	} else if (!m_id.equals(other.m_id))
	    return false;
	if (m_name == null) {
	    if (other.m_name != null)
		return false;
	} else if (!m_name.equals(other.m_name))
	    return false;
	return true;
    }

    
    /**
     * @return the children
     */
    public List<Account> getChildren() {
        return m_children;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return m_id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * @return the parent
     */
    public Account getParent() {
        return m_parent;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return m_version;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
	result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
	return result;
    }

    /**
     * @param children
     *            the children to set
     */
    public void setChildren(List<Account> children) {
        m_children = children;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        m_id = id;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(Account parent) {
        // remove us from the old parent if there was none
        if (m_parent != null) {
            m_parent .m_children.remove(this);
        }
        // set the new parent
        m_parent = parent;
        // add us to the children of the other one
        parent.m_children.add(this);
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParentForDozer(Account parent) {
        // set the parent
        m_parent = parent;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.m_version = version;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getId());
        out.append(": ");
        out.append(getName());
        if (getParent() != null) {
            out.append(" [" + getParent().getName() + "]");
        }
        return out.toString();
    }

}
