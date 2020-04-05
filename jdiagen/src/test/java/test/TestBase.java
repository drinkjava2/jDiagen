/*
 * jDialects, a tiny SQL dialect tool
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later. See
 * the lgpl.txt file in the root directory or
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.junit.After;
import org.junit.Before;

import com.github.drinkjava2.jbeanbox.JBEANBOX;
import com.github.drinkjava2.jlogs.ConsoleLog;
import com.github.drinkjava2.jsqlbox.DbContext;

import test.config.JBeanBoxConfig.DataSourceBox;

/**
 * This is the base class for all test cases which need prepare a DataSource for
 * test, default use H2 memory database, if want run on other database need
 * change configuration in test\config\JBeanBoxConfig.java
 *
 * @author Yong Zhu
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class TestBase {
	public DaoCtx dao;

	public static void openHibernateLog(SessionFactory sf) {
		@SuppressWarnings("deprecation")
		JdbcServices serv = sf.getSessionFactory().getJdbcServices();
		SqlStatementLogger log = serv.getSqlStatementLogger();
		log.setLogToStdout(true);
	}

	public static class DaoCtx extends DbContext {
		public DaoCtx(DataSource ds) {
			super(ds);
		}

		public void iExecuteQuiet(Object... params) {
			try {
				super.iExecute(params);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Set up DataSource pool, default SqlBoxContext, insert test data into database
	 */
	@Before
	public void setup() {
		JBEANBOX.close();
		dao = new DaoCtx(JBEANBOX.getBean(DataSourceBox.class));
		//dao.setAllowShowSQL(true);
		//ConsoleLog.setLogLevel(ConsoleLog.INFO);
		System.out.println("=============Testing " + this.getClass().getName() + "================");
		DbContext.setGlobalDbContext(dao);
	}

	/**
	 * Clean up, close DataSource pool, close default SqlBoxContext
	 */
	@After
	public void cleanUp() {
		JBEANBOX.close();
	}

}