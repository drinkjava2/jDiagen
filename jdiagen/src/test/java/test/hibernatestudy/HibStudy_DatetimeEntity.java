/*
 * jDialects, a tiny SQL dialect tool
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later. See
 * the lgpl.txt file in the root directory or
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import test.TestBase;

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
public class HibStudy_DatetimeEntity extends TestBase {

	private static Configuration buildConfig(String dialect) {
		Configuration c = new Configuration();
		c.setProperty("hibernate.dialect", dialect);
		c.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false");
		c.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		c.setProperty("hibernate.connection.username", "root");
		c.setProperty("hibernate.connection.password", "root888");
		c.addAnnotatedClass(DateBean.class);
		return c;
	}

	private void createTablesByjSqlBox() {
		dao.setAllowShowSQL(true);
		 dao.quiteExecute(dao.toDropAndCreateDDL(DateBean.class));
		//dao.quiteExecute("drop table if exists DateBean");
		//dao.quiteExecute("create table DateBean ( d1 timestamp not null default now(),d2 date,d3 time,d4 datetime(6),id varchar(250), primary key (id)) engine=InnoDB");
		new DateBean().insert();
	}

	private static void insertDataByHibernate() throws ParseException {
		try {
			Configuration c = buildConfig(MySQL55Dialect.class.getName()); 
			SessionFactory sf = c.buildSessionFactory();
			openHibernateLog(sf); 

			Session session = sf.openSession();
			Transaction tx = session.beginTransaction();

			DateBean bean = new DateBean();
			bean.setId("id");
			Date d = new SimpleDateFormat("yyyy-MM-dd").parse("2000-10-11");
			bean.setD1(d);
			session.save(bean);
			System.out.println("d5="+bean.getD5());
//			bean.setD3(null);
//			session.update(bean);
//			System.out.println("d5 2="+bean.getD5());
			
//			DateBean bean2= session.load(DateBean.class, bean.getId());
//			bean2.setD3(null);
//			session.update(bean2);
//			System.out.println("d5="+bean.getD5());

			tx.commit();
			session.close(); 
			 
			
			sf.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void doTest() throws ParseException {
		createTablesByjSqlBox();
		insertDataByHibernate();
	}

}