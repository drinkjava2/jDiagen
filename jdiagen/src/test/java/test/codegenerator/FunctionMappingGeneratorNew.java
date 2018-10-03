/*
 * jDialects, a tiny SQL dialect tool
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later. See
 * the lgpl.txt file in the root directory or
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.codegenerator;

import static com.github.drinkjava2.jdbpro.JDBPRO.param;
import static com.github.drinkjava2.jdbpro.JDBPRO.question;
import static test.codegenerator.RefUtils.findFieldObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.CastFunction;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.type.IntegerType;
import org.junit.Test;

import test.TestBase;
import util.StrUtily;

/**
 * This is not a unit test, it's a code generator tool to create source code for
 * jDialects's DialectFunctionTemplate.java
 *
 * @author Yong Zhu
 * @since 1.0.0
 */
@SuppressWarnings({ "unchecked" })
public class FunctionMappingGeneratorNew extends TestBase {

	@Test
	public void transferFunctions() {
		String createSQL = "create table tb_functions ("//
				+ "fn_name varchar(500) default '' " //
				+ ",percentage float" //
				+ ", constraint const_fn_name primary key (fn_name)" //
				+ ")";
		dao.iExecuteQuiet("drop table tb_functions");
		dao.nExecute(createSQL);
		exportDialectFunctionsToDatabase();
		countFunctionPercent();
		generateFunctionTemplateSourceCode();
		//generateDialectSourceCode();
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
			Dialect dia = HibernateDialectsList.buildDialectByName(class1);
			String diaName = dia.getClass().getSimpleName();
			dao.nExecute("alter table tb_functions add  " + diaName + " varchar(200)");
			dao.iExecuteQuiet("insert into tb_functions (" + diaName + ", fn_name) values(?,?)", param(diaName),
					param("FUNCTIONS"));
			Map<String, SQLFunction> sqlFunctions = (Map<String, SQLFunction>) findFieldObject(dia, "sqlFunctions");

			for (Entry<String, SQLFunction> entry : sqlFunctions.entrySet()) {
				String fn_name = entry.getKey();
				dao.iExecuteQuiet("insert into tb_functions (" + diaName + ", fn_name) values(?,?)", param("---"),
						param(fn_name));

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
				dao.iExecute("update tb_functions set " + diaName + "=? where fn_name=?", param(templateValue),
						param(fn_name));
			}
		}
	}

	public void countFunctionPercent() {
		List<Map<String, Object>> l = dao.nQuery(new MapListHandler(), "select * from tb_functions");
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
			dao.iExecute("update tb_functions set percentage=",
					question("" + Math.rint(1.0 * percentage * 100.0 / total)), " where fn_name=", question(fn_name));
		}
	}

	public void generateFunctionTemplateSourceCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("protected static void initFunctionTemplates() {\n");
		sb.append("Map<String, String> mp = new HashMap<String, String>();\n");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		Map<String, String> lastMP = new HashMap<>();

		for (Class<? extends Dialect> hibDialectClass : dialects) {
			Dialect d = HibernateDialectsList.buildDialectByName(hibDialectClass);
			String diaName = d.getClass().getSimpleName();
			List<Map<String, Object>> result = dao.nQuery(new MapListHandler(), "select fn_name, " + diaName
					+ " from tb_functions where percentage>16 order by percentage desc, fn_name");

			Map<String, String> thisMap = new HashMap<>();
			for (Map<String, Object> map : result) {
				String fn_name = (String) map.get("fn_name");
				String template = (String) map.get(diaName);
				if (!StringUtils.isEmpty(template)) {
					// d.fun.put("weekday", "weekday($Params)");
					if (template.equals(fn_name + "($Params)"))
						thisMap.put(fn_name, "*");
					else
						thisMap.put(fn_name, template);
				}
			}

			Set<String> toDelete = new HashSet<>();

			if (lastMP.size() > 0)
				for (Entry<String, String> entry : lastMP.entrySet()) {
					String key = entry.getKey();
					if (!thisMap.containsKey(key))
						toDelete.add(key);
				}

			if (toDelete.size() > 30) {
				lastMP.clear();
				sb.append("mp.clear();\n\n//===========A new dialect family=================\n");
			} else
				for (String key : toDelete) {
					lastMP.remove(key);
					sb.append("mp.remove(\"" + key + "\");\n");
				}

			for (Entry<String, String> entry : thisMap.entrySet()) {
				String thisKey = entry.getKey();
				if (!entry.getValue().equals(lastMP.get(thisKey))) {
					lastMP.put(thisKey, entry.getValue());
					sb.append("mp.put(\"" + thisKey + "\", \"" + entry.getValue() + "\");\n");
				}
			}
			sb.append("copyTo(mp, Dialect." + diaName + ");");
		}

		sb.append(" }\n");

		try {
			FileUtils.writeStringToFile(new File("e:/initFunctionTemplates.txt"), sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("initFunctionTemplates function exported to file 'e:/initFunctionTemplates.txt'\n\n");
	}

	@Deprecated
	public void generateDialectSourceCode() {
		StringBuilder sb = new StringBuilder();
		List<Map<String, Object>> result = dao.nQuery(new MapListHandler(),
				"select fn_name, percentage from tb_functions order by percentage desc, fn_name");
		for (Map<String, Object> map : result) {
			String fn_name = (String) map.get("fn_name");
			Double percentage =  Double.parseDouble(""+ map.get("percentage"));
			String funName;
			if (percentage > 15) {
				sb.append("/** ").append(fn_name.toUpperCase()).append("() function, ");
				if (percentage > 99) {
					funName = "fn_" + fn_name.toUpperCase();
					sb.append("all dialects support this function */").append("\n");
				} else {
					funName = "fn_" + fn_name;
					sb.append(percentage).append("% dialects support this function */").append("\n");
				}

				sb.append("public String ").append(funName)
						.append("(Object... args){return FunctionUtils.render(this, \"").append(fn_name)
						.append("\", args);}\n");
			}
		}
		System.out.println(sb.toString());
	}
}