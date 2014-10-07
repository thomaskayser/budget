package ch.tkayser.budget.test;

import java.sql.Connection;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;

import ch.tkayser.budget.dao.BudgetDAO;
import ch.tkayser.budget.service.BudgetService;


public class BudgetEJBBaseTest extends BudgetBaseTest {

    // the initial context
    protected InitialContext    m_context;

    // the budget DAO
    protected BudgetDAO         m_budgetDAO;

    // the budget Service
    protected BudgetService     m_budgetService;

    // user name and password for test
    private static final String TEST_USER     = "testuser";
    private static final String TEST_PASSWORD = "testpassword";
    
    // EntityManager
    

    @Before
    public void setUpTest() throws Exception {

        // init the inital context
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
        //p.put("openejb.embedded.initialcontext.close", "destroy");
        p.put("openejb.deployments.classpath.include", ".*classes.*");
        p.put("openejb.deployments.classpath.exclude", ".*");
        p.put("openejb.deployments.classpath.filter.descriptors", "true");

        p.put("log4j.org.hibernate.type", "trace");
        
        p.put("jdbc/budget", "new://Resource?type=DataSource");
        p.put("jdbc/budget.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("jdbc/budget.JdbcUrl", "jdbc:hsqldb:mem:budgetdb");

        p.put("budgetPU.hibernate.show_sql", "true");
        p.put("budgetPU.hibernate.hbm2ddl.auto", "create-drop");
        p.put("budgetPU.hibernate.dialect", "org.hibernate.dialect.HSQLDialect");

        // security
        p.put(Context.SECURITY_PRINCIPAL, TEST_USER);
        p.put(Context.SECURITY_CREDENTIALS, TEST_PASSWORD);
        
        m_context = new InitialContext(p);
        
        // cleanDB
        cleanDB();

        // the the services
        m_budgetDAO = (BudgetDAO) m_context.lookup("BudgetDAOImplLocal");
        m_budgetService = (BudgetService) m_context.lookup("BudgetServiceImplRemote");

    }

    private void cleanDB() throws Exception {
        
        // tabellen leeren
        DataSource s = (DataSource) m_context.lookup("openejb:Resource/jdbc/budget");
        Connection connection = s.getConnection();
        connection.createStatement().executeUpdate("delete from TRANSACTION;");
        connection.commit();
        connection.createStatement().executeUpdate("update account set act_parent_fk = null;");
        connection.commit();
        connection.createStatement().executeUpdate("delete from ACCOUNT;");
        connection.commit();
        connection.createStatement().executeUpdate("delete from BUDGET;");
        connection.commit();
    }

    @After
    public void tearDown() throws NamingException {
        m_context.close();
    }

}
