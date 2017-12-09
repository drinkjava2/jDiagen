/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import java.util.List;

import org.apache.log4j.Level;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.junit.Test;

import util.StrUtily;
import util.TextSupport;

/**
 * This is for study Hibernate how to build HQL functions
 *
 * @author Yong Zhu
 *
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class HibStudy_HQL_Study {
	private static String fileName = "f:/export.sql";

	public static void openHibernateLog(SessionFactory sf) {
		JdbcServices serv = sf.getSessionFactory().getJdbcServices();
		SqlStatementLogger log = serv.getSqlStatementLogger();
		org.apache.log4j.Logger.getLogger("org.hibernate").setLevel(Level.TRACE);
		org.apache.log4j.Logger.getLogger("log4j.logger.org.hibernate.SQL").setLevel(Level.DEBUG);
		org.apache.log4j.Logger.getLogger("log4j.logger.org.hibernate.hql").setLevel(Level.DEBUG);
		log.setLogToStdout(true);
	}

	public static SessionFactory buildMySqlSessionFactory() {
		Configuration c = new Configuration().configure();
		c.setProperty("hibernate.dialect", MySQL55Dialect.class.getName());
		c.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false");
		c.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		c.setProperty("hibernate.connection.username", "root");
		c.setProperty("hibernate.connection.password", "root888");
		c.addInputStream(StrUtily.getStringInputStream(new CustomerXML().toString()));
		return c.buildSessionFactory();
	}

	@Test
	public void listHQLQuery() {
		SessionFactory sf = buildMySqlSessionFactory();
		openHibernateLog(sf);
		Session session = sf.openSession();
		@SuppressWarnings("rawtypes")
		List list = session.createQuery("select second(current_date()),c.customerName from Customer c ").list();

		for (Object objects : list) {
			Object[] objs = (Object[]) objects;
			for (Object object : objs) {
				System.out.print(object + ",");
			}
			System.out.println();
		}
		session.close();
		sf.close();
	}

	//@formatter:off ==================================================	
	public static class CustomerXML extends TextSupport {
/*<?xml version="1.0" encoding="utf-8"?>
		<!DOCTYPE hibernate-mapping PUBLIC 
		 "-//Hibernate/Hibernate Mapping DTD//EN"
		 "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd"> 

		<hibernate-mapping> 
		    <class name="test.config.po.Customer" table="customertable" catalog="test">
				  <id name="id" type="java.lang.String">
				  	  <column name="id" length="32" />
					  <generator class="uuid2"/> 
				  </id>
				  <property name="customerName" type="java.lang.String">
					  <column name="customer_name" length="30" />
				  </property>
			</class> 
		</hibernate-mapping>		
	}
*/}


}