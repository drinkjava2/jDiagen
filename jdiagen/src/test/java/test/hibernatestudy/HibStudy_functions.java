/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

/**
 * This is for study Hibernate's function
 *
 * @author Yong Zhu
 * @since 1.0.2
 */
public class HibStudy_functions {

	public static void main(String[] args) {
		SessionFactoryImplementor si = null;
		SQLFunction fun = new SQLFunctionTemplate( StandardBasicTypes.STRING, "trim(?1 ?2 ?3 ?4)" );
		List<String> l = new ArrayList<>();
		l.add("Abc1");
		l.add("Abc2");
		l.add("Abc3"); 	l.add("Abc4"); 	l.add("Abc5"); 
		String result = fun.render(StringType.INSTANCE, l, si);
		System.out.println(result);
	}
}
