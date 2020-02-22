/*
 * jDialects, a tiny SQL dialect tool
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later. See
 * the lgpl.txt file in the root directory or
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.query.Query;
import org.junit.Test;

import test.TestBase;
import test.config.po.Customer;

/**
 * This is for study Hibernate, not related to this project, but just keep here,
 * some day may need this
 *
 * @author Yong Zhu
 *
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings({ "all" })
public class HibStudy_SessionFactoryFromConfig extends TestBase {

	private void createTablesByjSqlBox() {
		dao.setAllowShowSQL(true); 
		dao.quiteExecute(dao.toDropAndCreateDDL(Customer.class));
		for (int i = 0; i < 5; i++) {
			Customer customer = new Customer();
			customer.setId("jbeanbox"+i);
			customer.setCustomerName("Tom"); 
			customer.insert();
		}
	} 

	private static void insertDataByHibernate() {
		try {
			Configuration c = new Configuration().configure();
			SessionFactory sf = c.buildSessionFactory();

			Session session = sf.openSession();
			Transaction tx = session.beginTransaction();
			for (int i = 0; i < 10; i++) {
				Customer customer = new Customer();
				customer.setCustomerName("Tom");
				session.save(customer);
			}
			tx.commit();
			session.close();
			sf.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	private static Configuration buildConfig(String dialect) {
		Configuration c = new Configuration();
		c.setProperty("hibernate.dialect", dialect);
		c.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false");
		c.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		c.setProperty("hibernate.connection.username", "root");
		c.setProperty("hibernate.connection.password", "root888");
		// c.addResource("Customer.hbm.xml");
		// c.addAnnotatedClass(annotatedClass)
		return c;
	}

	private static void nativeQuery() {
		Configuration c = buildConfig(MySQL55Dialect.class.getName());
		SessionFactory sf = c.buildSessionFactory();// old style

		Session session = sf.openSession();
		Query query = session
				.createNativeQuery("select a.* from customertb a, customertb b where a.id=b.id order by a.id");
		query.setFirstResult(2);
		query.setMaxResults(3);
		openHibernateLog(sf);
		List l = query.list();
		for (Object object : l) {
			Object[] objs = (Object[]) object;
			for (Object object2 : objs) {
				System.out.print(object2 + " , ");
			}
			System.out.println();
		}
		session.close();
		sf.close();
	}

	@Test
	public void doTest() {
		createTablesByjSqlBox();
		insertDataByHibernate();
		nativeQuery();
	}

}