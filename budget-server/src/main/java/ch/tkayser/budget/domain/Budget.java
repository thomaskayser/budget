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
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "BUDGET")
public class Budget implements Serializable  {

	private static final long serialVersionUID = 886862890199450070L;

    public static String NAME_ATTRIBUT = "m_name";
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_SEQ")
    @SequenceGenerator(name = "GEN_SEQ", sequenceName = "BUDGET_SEQ", allocationSize = 10, initialValue = 1)
    @Column(name = "BUD_ID")
    private Long          m_id;

    @Column(name = "BUD_NAME", nullable=false)
    private String        m_name;

    @Column(name = "BUD_MONTHLY_AMOUNT", nullable=false)    
    private BigDecimal m_amountPerMonth;

    @Version
    @Column(name = "BUD_VERSION")
    private Integer m_version;

    public Budget() {
        super();
    }

	public Long getId() {
		return m_id;
	}

	public void setId(Long id) {
		m_id = id;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}

	public BigDecimal getAmountPerMonth() {
		return m_amountPerMonth;
	}

	public void setAmountPerMonth(BigDecimal amount) {
		m_amountPerMonth = amount;
	}

	public Integer getVersion() {
		return m_version;
	}

	public void setVersion(Integer version) {
		m_version = version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_amountPerMonth == null) ? 0 : m_amountPerMonth.hashCode());
		result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
		result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Budget other = (Budget) obj;
		if (m_amountPerMonth == null) {
			if (other.m_amountPerMonth != null)
				return false;
		} else if (!m_amountPerMonth.equals(other.m_amountPerMonth))
			return false;
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

	

}
