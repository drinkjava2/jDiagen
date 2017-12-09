/*
 * jDialects, a tiny SQL dialect tool
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later. See
 * the lgpl.txt file in the root directory or
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.codegenerator;

import static com.github.drinkjava2.jdbpro.inline.InlineQueryRunner.param;
import static com.github.drinkjava2.jdbpro.inline.InlineQueryRunner.param0;
import static com.github.drinkjava2.jdbpro.inline.InlineQueryRunner.valuesQuesions;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Level;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.TypeNames;
import org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.Test;

import com.github.drinkjava2.jbeanbox.springsrc.ReflectionUtils;

import test.TestBase;
import util.StrUtily;

/**
 * This is not a unit test class, it's a code generator tool to create source
 * code for jDialects
 *
 * @author Yong Zhu
 * @since 1.0.0
 */
@SuppressWarnings({ "unchecked" })
public class TypeMappingCodeGenerator extends TestBase {

	public static Dialect buildDialectByName(Class<?> dialect) {
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
	public void transferTypeNames() {
		org.apache.log4j.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		String createSQL = "create table tb_typeNames ("// Save TypeNames into
														// DB
				+ "line integer,"//
				+ "dialect varchar(100),"//
				+ "t_BIGINT varchar(300),"//
				+ "t_BINARY varchar(300),"//
				+ "t_BIT varchar(300),"//
				+ "t_BLOB varchar(300),"//
				+ "t_BOOLEAN varchar(300),"//
				+ "t_CHAR varchar(300),"//
				+ "t_CLOB varchar(300),"//
				+ "t_DATE varchar(300),"//
				+ "t_DECIMAL varchar(300),"//
				+ "t_DOUBLE varchar(300),"//
				+ "t_FLOAT varchar(300),"//
				+ "t_INTEGER varchar(300),"//
				+ "t_JAVA_OBJECT varchar(300),"//
				+ "t_LONGNVARCHAR varchar(300),"//
				+ "t_LONGVARBINARY varchar(300),"//
				+ "t_LONGVARCHAR varchar(300),"//
				+ "t_NCHAR varchar(300),"//
				+ "t_NCLOB varchar(300),"//
				+ "t_NUMERIC varchar(300),"//
				+ "t_NVARCHAR varchar(300),"//
				+ "t_OTHER varchar(300),"//
				+ "t_REAL varchar(300),"//
				+ "t_SMALLINT varchar(300),"//
				+ "t_TIME varchar(300),"//
				+ "t_TIMESTAMP varchar(300),"//
				+ "t_TINYINT varchar(300),"//
				+ "t_VARBINARY varchar(300),"//
				+ "t_VARCHAR varchar(300)"//
				+ ")";
		dao.iExecuteQuiet("drop table tb_typeNames");
		dao.nExecute(createSQL);
		exportDialectTypeNames();
	}

	public void exportDialectTypeNames() {
		int line = 0;
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> class1 : dialects) {
			Dialect dia = buildDialectByName(class1);
			TypeNames t = (TypeNames) findFieldObject(dia, "typeNames");
			String insertSQL = "insert into tb_typeNames ("//
					+ "line," + param0(++line)//
					+ "dialect," + param(dia.getClass().getSimpleName())//
					+ "t_BIGINT," + param(getTypeNameDefString(t, (Types.BIGINT)))//
					+ "t_BINARY," + param(getTypeNameDefString(t, (Types.BINARY)))//
					+ "t_BIT," + param(getTypeNameDefString(t, (Types.BIT)))//
					+ "t_BLOB," + param(getTypeNameDefString(t, (Types.BLOB)))//
					+ "t_BOOLEAN," + param(getTypeNameDefString(t, (Types.BOOLEAN)))//
					+ "t_CHAR," + param(getTypeNameDefString(t, (Types.CHAR)))//
					+ "t_CLOB," + param(getTypeNameDefString(t, (Types.CLOB)))//
					+ "t_DATE," + param(getTypeNameDefString(t, (Types.DATE)))//
					+ "t_DECIMAL," + param(getTypeNameDefString(t, (Types.DECIMAL)))//
					+ "t_DOUBLE," + param(getTypeNameDefString(t, (Types.DOUBLE)))//
					+ "t_FLOAT," + param(getTypeNameDefString(t, (Types.FLOAT)))//
					+ "t_INTEGER," + param(getTypeNameDefString(t, (Types.INTEGER)))//
					+ "t_JAVA_OBJECT," + param(getTypeNameDefString(t, (Types.JAVA_OBJECT)))//
					+ "t_LONGNVARCHAR," + param(getTypeNameDefString(t, (Types.LONGNVARCHAR)))//
					+ "t_LONGVARBINARY," + param(getTypeNameDefString(t, (Types.LONGVARBINARY)))//
					+ "t_LONGVARCHAR," + param(getTypeNameDefString(t, (Types.LONGVARCHAR)))//
					+ "t_NCHAR," + param(getTypeNameDefString(t, (Types.NCHAR)))//
					+ "t_NCLOB," + param(getTypeNameDefString(t, (Types.NCLOB)))//
					+ "t_NUMERIC," + param(getTypeNameDefString(t, (Types.NUMERIC)))//
					+ "t_NVARCHAR," + param(getTypeNameDefString(t, (Types.NVARCHAR)))//
					+ "t_OTHER," + param(getTypeNameDefString(t, (Types.OTHER)))//
					+ "t_REAL," + param(getTypeNameDefString(t, (Types.REAL)))//
					+ "t_SMALLINT," + param(getTypeNameDefString(t, (Types.SMALLINT)))//
					+ "t_TIME," + param(getTypeNameDefString(t, (Types.TIME)))//
					+ "t_TIMESTAMP," + param(getTypeNameDefString(t, (Types.TIMESTAMP)))//
					+ "t_TINYINT," + param(getTypeNameDefString(t, (Types.TINYINT)))//
					+ "t_VARBINARY," + param(getTypeNameDefString(t, (Types.VARBINARY)))//
					+ "t_VARCHAR" + param(getTypeNameDefString(t, (Types.VARCHAR)))//
					+ ")" //
					+ valuesQuesions();
			dao.iExecute(insertSQL);
		}

		// ============now start generate source code=======
		StringBuilder sb = new StringBuilder();
		sb.append("private void initializeTypeMappings() {").append("\n");
		sb.append("switch (this) {\n");
		List<Map<String, Object>> lst = dao.nQuery(new MapListHandler(), "select * from tb_typeNames");
		for (Map<String, Object> map : lst) {
			String dialect = (String) map.get("dialect");
			sb.append("case " + dialect + ": {\n");

			for (Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey();
				key = StrUtily.replace(key, "T_", "");
				key = StrUtily.replace(key, "t_", "");
				String value = "" + entry.getValue();
				if (!"LINE".equals(key) && !"line".equals(key) && !"DIALECT".equals(key) && !"dialect".equals(key)) {
					sb.append("typeMappings.put(Type." + key + ", \"" + value + "\");\n");
				}
			}
			if (StrUtily.containsIgnoreCase(dialect, "innoDB"))
				sb.append("typeMappings.put(Type.ENGINE, \"engine=innoDB\");\n");
			if (StrUtily.containsIgnoreCase(dialect, "MyISAM"))
				sb.append("typeMappings.put(Type.ENGINE, \"engine=MyISAM\");\n");
			sb.append("}\n");
			sb.append("break;\n");
		}
		sb.append("default:\n");
		sb.append("}\n");
		sb.append("}\n");
		System.out.println(sb.toString());
	}

	private static String getTypeNameDefString(TypeNames t, int typeCode) {
		String s = "";
		Map<Integer, Map<Long, String>> weighted = (Map<Integer, Map<Long, String>>) findFieldObject(t, "weighted");
		Map<Long, String> map = weighted.get(typeCode);
		if (map != null && map.size() > 0) {
			for (Map.Entry<Long, String> entry : map.entrySet()) {
				s += entry.getValue() + "<" + entry.getKey() + "|";
			}
		}
		String defaultValue = "N/A";
		try {
			defaultValue = t.get(typeCode);
		} catch (Exception e) {
		}
		return s + defaultValue;
	}

	private static Object findFieldObject(Object obj, String fieldname) {
		try {
			Field field = ReflectionUtils.findField(obj.getClass(), fieldname);
			field.setAccessible(true);
			Object o = field.get(obj);
			return o;
		} catch (Exception e) {
			return null;
		}
	}

}