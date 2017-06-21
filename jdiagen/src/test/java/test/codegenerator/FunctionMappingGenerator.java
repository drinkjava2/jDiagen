/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.codegenerator;

import static com.github.drinkjava2.jsqlbox.SqlHelper.empty;
import static com.github.drinkjava2.jsqlbox.SqlHelper.q;
import static test.codegenerator.RefUtils.findFieldObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.CastFunction;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.type.IntegerType;
import org.junit.Test;

import com.github.drinkjava2.jsqlbox.Dao;

import test.TestBase;
import util.StrUtily;

/**
 * This is not a unit test, it's a code generator tool to create source code for
 * jDialects
 *
 * @author Yong Zhu
 * @since 1.0.0
 */
@SuppressWarnings({ "unchecked" })
public class FunctionMappingGenerator extends TestBase {

	private static Dialect buildDialectByName(Class<?> dialect) {
		BootstrapServiceRegistry bootReg = new BootstrapServiceRegistryBuilder()
				.applyClassLoader(HibernateDialectsList.class.getClassLoader()).build();
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder(bootReg).build();
		DialectFactoryImpl dialectFactory = new DialectFactoryImpl();
		dialectFactory.injectServices((ServiceRegistryImplementor) registry);
		final Map<String, String> configValues = new HashMap<String, String>();
		configValues.put(Environment.DIALECT, dialect.getName());
		return dialectFactory.buildDialect(configValues, null);
	}

	@Test
	public void transferFunctions() {
		// String createSQL = "create table tb_functions ("//
		// + "fn_name varchar(500) default '' " //
		// + ",percentage int" //
		// + ", constraint const_fn_name primary key (fn_name)" //
		// + ")";
		// Dao.executeQuiet("drop table tb_functions");
		// Dao.execute(createSQL);
		// exportDialectFunctionsToDatabase();
		// countFunctionPercent();
		// generateFunctionTemplateSourceCode();
		generateDialectSourceCode();
	}

	private static List<String> getL(int count) {
		List<String> l = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			l.add("" + (i + 1) * 1111);
		}
		return l;
	}

	private static String getTryValue(SQLFunction fun, int paraCount) {
		String returnResult = "";
		String realValue = "";
		try {
			// only try IntegerType because firstArgumentType not used
			realValue = fun.render(IntegerType.INSTANCE, getL(paraCount), null);
			if (paraCount == 6)
				returnResult = realValue;
			else
				returnResult += paraCount + "=" + realValue;
		} catch (Exception e1) {
		}
		if (paraCount == 0) {
			// to fix a Hibernate bug if used wrong quantity parameters
			if (realValue.length() <= 1 || realValue.startsWith(")") || realValue.startsWith("as ")
					|| realValue.startsWith(" as "))
				returnResult = "";
		}
		return returnResult;
	}

	public void exportDialectFunctionsToDatabase() {
		System.out.println("exportDialectFunctions========================");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> class1 : dialects) {
			System.out.println("Analyze dialect " + class1 + "...");
			Dialect dia = buildDialectByName(class1);
			String diaName = dia.getClass().getSimpleName();
			Dao.execute("alter table tb_functions add  " + diaName + " varchar(200)");
			Dao.executeQuiet("insert into tb_functions (" + diaName + ", fn_name) values(?,?)", empty(diaName),
					empty("FUNCTIONS"));
			Map<String, SQLFunction> sqlFunctions = (Map<String, SQLFunction>) findFieldObject(dia, "sqlFunctions");

			for (Entry<String, SQLFunction> entry : sqlFunctions.entrySet()) {
				String fn_name = entry.getKey();
				Dao.executeQuiet("insert into tb_functions (" + diaName + ", fn_name) values(?,?)", empty("---"),
						empty(fn_name));

				SQLFunction fun = entry.getValue();

				String tryValue = getTryValue(fun, 6);
				// check if used all parameters
				if (tryValue.indexOf("1111") < 0 || tryValue.indexOf("2222") < 0 || tryValue.indexOf("3333") < 0
						|| tryValue.indexOf("4444") < 0 || tryValue.indexOf("5555") < 0
						|| tryValue.indexOf("6666") < 0) {
					tryValue = getTryValue(fun, 0);
					for (int i = 1; i <= 5; i++) {
						String val = getTryValue(fun, i);
						if (!StringUtils.isEmpty(val) && val.indexOf("" + i * 11) >= 0) {
							if (StringUtils.isEmpty(tryValue))
								tryValue = val;
							else
								tryValue += "|" + val;
						}
					}
				}

				if (CastFunction.class.equals(fun.getClass())) {
					try {
						tryValue = "2=cast($P1, $P2)";
					} catch (Exception e5) {
						tryValue = "Exception";
					}
				} else {
					// change result to templates
					tryValue = StrUtily.replace(tryValue, "1111, 2222, 3333, 4444, 5555, 6666", "$Params");
					tryValue = StrUtily.replace(tryValue, "1111,2222,3333,4444,5555,6666", "$Compact_Params");
					tryValue = StrUtily.replace(tryValue, "1111||2222||3333||4444||5555||6666", "$Lined_Params");
					tryValue = StrUtily.replace(tryValue, "1111+2222+3333+4444+5555+6666", "$Add_Params");
					tryValue = StrUtily.replace(tryValue, "1111 in 2222 in 3333 in 4444 in 5555 in 6666", "$IN_Params");
					tryValue = StrUtily.replace(tryValue,
							"1111%pattern2222%pattern3333%pattern4444%pattern5555%pattern6666", "$Pattern_Params");
					tryValue = StrUtily.replace(tryValue,
							"11%startswith2222%startswith3333%startswith4444%startswith5555%startswith6666",
							"$Startswith_Params");
					tryValue = StrUtily.replace(tryValue, "nvl(1111, nvl(2222, nvl(3333, nvl(4444, nvl(5555, 6666)))))",
							"$NVL_Params");
					tryValue = StrUtily.replace(tryValue, "1111", "$P1");
					tryValue = StrUtily.replace(tryValue, "2222", "$P2");
					tryValue = StrUtily.replace(tryValue, "3333", "$P3");
					tryValue = StrUtily.replace(tryValue, "4444", "$P4");
					tryValue = StrUtily.replace(tryValue, "5555", "$P5");
					tryValue = StrUtily.replace(tryValue, "|5=cast($P1 as varchar(255))", "");
					if (StringUtils.isEmpty(tryValue)) {
						System.out.println("diaName=" + diaName);
						System.out.println("class=" + fun.getClass());
						tryValue = "*ERROR";
					}
				}
				String templateValue = tryValue;
				// fun.getClass().getSimpleName() + ">" + templateValue
				Dao.execute("update tb_functions set " + diaName + "=? where fn_name=?", empty(templateValue),
						empty(fn_name));
			}
		}
	}

	public void countFunctionPercent() {
		List<Map<String, Object>> l = Dao.queryForList("select * from tb_functions");
		for (Map<String, Object> map : l) {
			String fn_name = (String) map.get("fn_name");
			int percentage = 0;
			int total = map.size() - 2;
			for (Entry<String, Object> entry : map.entrySet()) {
				if (!"fn_name".equals(entry.getKey()) && !"percentage".equals(entry.getKey())
						&& !StrUtily.isEmpty(entry.getValue())) {
					percentage++;
				}
			}
			Dao.execute("update tb_functions set percentage=" + q("" + Math.rint(1.0 * percentage * 100.0 / total))
					+ " where fn_name=" + q(fn_name));
		}
	}

	public void generateFunctionTemplateSourceCode() {
		StringBuilder sb = new StringBuilder();
		// To avoid 65535 bytes limitation of method
		int methodCount = 1;
		sb.append("protected static void initFunctionTemplates" + methodCount + "(Dialect d) {\n");
		sb.append("switch (d) {\n");

		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		int dialectsCount = 0;
		for (Class<? extends Dialect> hibDialectClass : dialects) {
			Dialect d = TypeMappingCodeGenerator.buildDialectByName(hibDialectClass);
			String diaName = d.getClass().getSimpleName();
			sb.append("case " + diaName + ": {\n");
			List<Map<String, Object>> result = Dao.queryForList(
					"select fn_name, " + diaName + " from tb_functions order by percentage desc, fn_name");
			for (Map<String, Object> map : result) {
				String fn_name = (String) map.get("fn_name");
				String template = (String) map.get(diaName);
				if (!StringUtils.isEmpty(template)) {
					// d.fun.put("weekday", "weekday($Params)");
					if (template.equals(fn_name + "($Params)"))
						sb.append("d.functions.put(\"" + fn_name + "\", \"" + "*" + "\");\n");
					else
						sb.append("d.functions.put(\"" + fn_name + "\", \"" + template + "\");\n");
				}
			}
			sb.append("} break;\n");

			dialectsCount++;
			if (dialectsCount > 15) {
				dialectsCount = 0;
				sb.append("default:\n 	}\n	}\n");
				sb.append("protected static void initFunctionTemplates" + ++methodCount + "(Dialect d) {\n");
				sb.append("switch (d) {\n");
			}
		}
		sb.append("default:\n 	}\n	}\n");
		try {
			FileUtils.writeStringToFile(new File("e:/initFunctionTemplates.txt"), sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("initFunctionTemplates function exported to file 'e:/initFunctionTemplates.txt'\n\n");
	}

	public void generateDialectSourceCode() {
		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> result = Dao
				.queryForList("select fn_name, percentage from tb_functions order by percentage desc, fn_name");
		for (Map<String, Object> map : result) {
			String fn_name = (String) map.get("fn_name");
			Integer percentage = (Integer) map.get("percentage");

			String underlines = "";
			if (fn_name.equals("abs") || fn_name.equals("avg") || fn_name.equals("day") || fn_name.equals("max")
					|| fn_name.equals("min") || fn_name.equals("mod") || fn_name.equals("str") || fn_name.equals("sum")
					|| fn_name.equals("cast") || fn_name.equals("hour") || fn_name.equals("sqrt")
					|| fn_name.equals("trim") || fn_name.equals("year") || fn_name.equals("count")
					|| fn_name.equals("lower") || fn_name.equals("month") || fn_name.equals("upper")
					|| fn_name.equals("length") || fn_name.equals("locate") || fn_name.equals("minute")
					|| fn_name.equals("nullif") || fn_name.equals("second")) {
				underlines = "_";
			} else {
				underlines = "__";
			}
			if (percentage >= 9) {
				sb.append("/** ").append(fn_name.toUpperCase()).append("() function, ").append(percentage)
						.append("% dialects support this function */").append("\n");
				sb.append("public String fn").append(underlines).append(fn_name)
						.append("(Object... args){return FunctionUtils.render(this, \"").append(fn_name)
						.append("\", args);}\n");
			}
		}
		System.out.println(sb.toString());
	}
}