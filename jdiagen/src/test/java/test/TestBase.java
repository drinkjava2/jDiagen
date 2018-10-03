/*
 * jDialects, a tiny SQL dialect tool
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later. See
 * the lgpl.txt file in the root directory or
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;

import com.github.drinkjava2.jbeanbox.BeanBox;
import com.github.drinkjava2.jbeanbox.JBEANBOX;
import com.github.drinkjava2.jsqlbox.SqlBoxContext;

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

	public static class DaoCtx extends SqlBoxContext {
		public DaoCtx(DataSource ds) {
			super(ds);
		}

		public void iExecuteQuiet(Object... sqls) {
			try {
				super.iExecute(sqls);
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
		dao = new DaoCtx(BeanBox.getBean(DataSourceBox.class));
		System.out.println("=============Testing " + this.getClass().getName() + "================"); 
		SqlBoxContext.setGlobalSqlBoxContext(dao);
	}

	/**
	 * Clean up, close DataSource pool, close default SqlBoxContext
	 */
	@After
	public void cleanUp() {
		JBEANBOX.close();
	}

}