/*
 * jDialects, a tiny SQL dialect tool
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later. See
 * the lgpl.txt file in the root directory or
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.After;
import org.junit.Before;

import com.github.drinkjava2.jbeanbox.BeanBox;
import com.github.drinkjava2.jsqlbox.SqlBoxContext;
import com.github.drinkjava2.jtinynet.TinyNet;

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

		public void iExecuteQuiet(String... sqls) {
			try {
				super.iExecute(sqls);
			} catch (Exception e) {
			}
		}

		public <T> List<T> queryForEntityList(Class<T> clazz, String... sqls) {
			List<Map<String, Object>> mapList1 = this.iQuery(new MapListHandler(netProcessor(clazz)), sqls);
			TinyNet net = this.netCreate(mapList1);
			return net.getAllEntityList(clazz);
		}

	}

	/**
	 * Set up DataSource pool, default SqlBoxContext, insert test data into database
	 */
	@Before
	public void setup() {
		BeanBox.defaultContext.close();
		dao = new DaoCtx(BeanBox.getBean(DataSourceBox.class));
		System.out.println("=============Testing " + this.getClass().getName() + "================"); 
		SqlBoxContext.setDefaultContext(dao);
	}

	/**
	 * Clean up, close DataSource pool, close default SqlBoxContext
	 */
	@After
	public void cleanUp() {
		BeanBox.defaultContext.close();
	}

}