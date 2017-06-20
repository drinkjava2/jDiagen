/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.SqlGenerator;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.junit.Test;

/**
 * This is for study Hibernate's function
 *
 * @author Yong Zhu
 * @since 1.0.2
 */
public class HibStudy_functions {

	@Test
	public void test1() {
		SessionFactory sf = HibStudy_HQL_Study.buildMySqlSessionFactory();
		SqlGenerator gen = new SqlGenerator((SessionFactoryImplementor) sf);
		String sql = gen.getSQL();
		System.out.println(sql);
	}

	@Test
	public void test2() {
		SessionFactoryImplementor si = null;
		List<String> l = new ArrayList<>();
		l.add("p1");
		l.add("p2");
		l.add("p3");
		l.add("p4");
		l.add("p5");

		SQLFunction fun = new SQLFunctionTemplate(StandardBasicTypes.STRING, "trim(?1 ?2 ?3 ?4)");
		String result = fun.render(StringType.INSTANCE, l, si);
		System.out.println(result);

		SQLFunction fun2 = new StandardSQLFunction("abs");
		result = fun2.render(StringType.INSTANCE, l, si);
		System.out.println(result);
	}
}
