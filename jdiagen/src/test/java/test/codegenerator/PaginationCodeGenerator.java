/*
 * jDialects, a tiny SQL dialect tool
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later. See
 * the lgpl.txt file in the root directory or
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.codegenerator;

import static com.github.drinkjava2.jsqlbox.JSQLBOX.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.engine.jdbc.dialect.internal.DialectFactoryImpl;
import org.hibernate.engine.spi.RowSelection;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.Test;

import com.github.drinkjava2.jdialects.annotation.jpa.Id;
import com.github.drinkjava2.jsqlbox.ActiveRecord;

import test.TestBase;

/**
 * This is not a unit test class, it's a code generator tool to create source
 * code for jDialects
 *
 * @author Yong Zhu
 * @since 1.0.0
 */
public class PaginationCodeGenerator extends TestBase {
	private static final String SKIP_ROWS = "$SKIP_ROWS";
	private static final String PAGESIZE = "$PAGESIZE";
	private static final String TOTAL_ROWS = "$TOTAL_ROWS";
	private static final String SKIP_ROWS_PLUS1 = "$SKIPROWS_PLUS1";
	private static final String TOTAL_ROWS_PLUS1 = "$TOTALROWS_PLUS1";
	private static final String DISTINCT_TAG = "($DISTINCT)";

	@Test
	public void transferPagination() {
		String createSQL = "create table tb_pagination ("//
				+ "dialect varchar(100),"//
				+ "pagination varchar(500),"//
				+ "limits varchar(30),"//
				+ "sortorder int,"//
				+ "paginationFirstOnly varchar(500),"//
				+ "limits2 varchar(30),"//
				+ "sortorder2 int,"//
				+ "supportsLimit varchar(10),"//
				+ "supportLimitOffset varchar(10),"//
				+ "supportsVariableLimit varchar(10),"//
				+ "bindLimitParametersInReverseOrder varchar(10),"//
				+ "bindLimitParametersFirst varchar(10),"//
				+ "useMaxForLimit varchar(10),"//
				+ "forceLimitUsage varchar(10),"//
				+ "firstRowValue varchar(10)"//
				+ ")";
		dao.iExecuteQuiet("drop table tb_pagination");
		dao.nExecute(createSQL);
		exportHibernateDialectPaginations();
		exportHibernateDialectPaginationFirstOnly();

		System.out.println("//====================================================");
		System.out.println("//====================================================");

		generatePaginationSourceCode();
		generatePaginationFirstOnlySourceCode();
		System.out.println("//====================================================");
		System.out.println("//====================================================");

	}

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

	public static class TB_pagination extends ActiveRecord<TB_pagination> {
		@Id
		private String dialect;
		private String pagination;
		private String paginationFirstOnly;
		private Integer sortorder;
		private Integer sortorder2;

		public String getDialect() {
			return dialect;
		}

		public void setDialect(String dialect) {
			this.dialect = dialect;
		}

		public String getPagination() {
			return pagination;
		}

		public void setPagination(String pagination) {
			this.pagination = pagination;
		}

		public String getPaginationFirstOnly() {
			return paginationFirstOnly;
		}

		public void setPaginationFirstOnly(String paginationFirstOnly) {
			this.paginationFirstOnly = paginationFirstOnly;
		}

		public Integer getSortorder() {
			return sortorder;
		}

		public void setSortorder(Integer sortorder) {
			this.sortorder = sortorder;
		}

		public Integer getSortorder2() {
			return sortorder2;
		}

		public void setSortorder2(Integer sortorder2) {
			this.sortorder2 = sortorder2;
		}

	}

	private void exportHibernateDialectPaginations() {
		System.out.println("exportDialectPaginations========================");
		RowSelection r = new RowSelection();
		r.setFirstRow(3);// OFFSET 3
		r.setMaxRows(9);// PAGESIZE 9
		r.setFetchSize(100);// no use
		r.setTimeout(1000);// no use
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> class1 : dialects) {
			Dialect dia = buildDialectByName(class1);
			LimitHandler l = dia.getLimitHandler();
			AbstractLimitHandler l2 = null;
			if (AbstractLimitHandler.class.isInstance(l))
				l2 = (AbstractLimitHandler) l;

			String dialect = class1.getSimpleName();
			String limits = "";
			String pagination = "NOT_SUPPORT";
			try {
				String baitSqlBody = "a.c1 as ac1, b.c2 as bc2 from tba a, tbb b where a.c2 like 'a%' group by a.c1 order by a.c1, b.c3";
				String fullBaitSQL = "select distinct " + baitSqlBody;
				pagination = l.processSql(fullBaitSQL, r);
				limits = PrepareStatementUtils.prepareQueryStatement(r, dia, l);
				pagination = replaceOffsetAndLimit(dialect, pagination, baitSqlBody, limits);

			} catch (Exception e) {
			}

			//============hard patch for SqlServer2005 and 2008
			if ("SQLServer2005Dialect".equals(dialect) || "SQLServer2008Dialect".equals(dialect))
				pagination="WITH query AS (SELECT TMP_.*, ROW_NUMBER() OVER (ORDER BY CURRENT_TIMESTAMP) as ROW_NUM_ FROM ( select ($DISTINCT) TOP($TOTAL_ROWS) $BODY ) TMP_ ) SELECT * FROM query WHERE ROW_NUM_ >$SKIP_ROWS AND ROW_NUM_ <= $TOTAL_ROWS";

			
			if (l2 != null)
				dao.iExecute("insert into tb_pagination (" //
						, "dialect ,", param(dialect)//
						, "supportsLimit ,", param(l.supportsLimit())//
						, "supportLimitOffset ,", param(l.supportsLimitOffset())//
						, "supportsVariableLimit ,", param(l2.supportsVariableLimit())//
						, "bindLimitParametersInReverseOrder ,", param(l2.bindLimitParametersInReverseOrder())//
						, "bindLimitParametersFirst ,", param(l2.bindLimitParametersFirst())//
						, "useMaxForLimit ,", param(l2.useMaxForLimit())//
						, "forceLimitUsage ,", param(l2.forceLimitUsage())//
						, "firstRowValue ,", param(l2.convertToFirstRowValue(0))//
						, "limits ,", param(limits)//
						, "pagination ) ", param(pagination)//
						, valuesQuestions());
			else
				dao.iExecute("insert into tb_pagination (" //
						, "dialect ,", param(dialect)//
						, "supportsLimit ,", param(l.supportsLimit())//
						, "supportLimitOffset ,", param(l.supportsLimitOffset())//
						, "limits ,", param(limits)//
						, "pagination )", param(pagination)//
						, valuesQuestions());
		}
	}

	private void exportHibernateDialectPaginationFirstOnly() {
		System.out.println("exportDialectPaginations========================");
		RowSelection r = new RowSelection();
		r.setFirstRow(0);// OFFSET 0
		r.setMaxRows(55);// PAGESIZE 55
		r.setFetchSize(100);// no use
		r.setTimeout(1000);// no use
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> class1 : dialects) {
			Dialect dia = buildDialectByName(class1);
			LimitHandler l = dia.getLimitHandler();

			String dialect = class1.getSimpleName();
			String limits2 = "";
			String pagination = "NOT_SUPPORT";
			try {
				String baitSqlBody = "a.c1 as ac1, b.c2 as bc2 from tba a, tbb b where a.c2 like 'a%' group by a.c1 order by a.c1, b.c3";
				String fullBaitSQL = "select distinct " + baitSqlBody;
				pagination = l.processSql(fullBaitSQL, r);
				limits2 = PrepareStatementUtils.prepareQueryStatement(r, dia, l);
				pagination = replaceOffsetAndLimit(dialect, pagination, baitSqlBody, limits2);
			} catch (Exception e) {
			}
			dao.iExecute("update tb_pagination  " //
					, " set paginationFirstOnly=?", param(pagination)//
					, ", limits2 =?", param(limits2)//
					, " where dialect=? ", param(dialect)//
			);
		}

	}

	private static String replaceDialectStr(String dialectName, String SQL, String strOld, String strNew,
			String... dialects) {
		if (StringUtils.isEmpty(dialectName) || dialects == null || dialects.length == 0)
			return StringUtils.replace(SQL, strOld, strNew);

		String newSQL = SQL;
		for (String dia : dialects) {
			if (StringUtils.containsIgnoreCase(dialectName, dia))
				newSQL = StringUtils.replace(SQL, strOld, strNew);
		}
		return newSQL;
	}

	/**
	 * $0BASE_OFFSET=0, $1BASE_ROW_START=$0BASE_OFFSET+1
	 * 
	 * <pre>
	 *  Here is a good article:
	 *  https://blog.jooq.org/2014/06/09/stop-trying-to-emulate-sql-offset-pagination-with-your-in-house-db-framework/
	 * </pre>
	 */
	private String replaceOffsetAndLimit(String dialectName, String sql, String baitSqlBody, String limits) {

		sql = replaceDialectStr(dialectName, sql, baitSqlBody, "$BODY");
		sql = replaceDialectStr(dialectName, sql, " distinct ", " " + DISTINCT_TAG + " ");
		sql = replaceDialectStr(dialectName, sql, DISTINCT_TAG + " $BODY", "$BODY");
		sql = replaceDialectStr(dialectName, sql, " __hibernate_row_nr__", " ROW_NUM_", "SQLServer2005",
				"SQLServer2008");
		sql = replaceDialectStr(dialectName, sql, " inner_query", " TMP_", "SQLServer2005", "SQLServer2008");
		sql = replaceDialectStr(dialectName, sql, " ac1, bc2", " $FIELDS_OR_ALIAS", "SQLServer2005", "SQLServer2008");

		Map<String, String> rep = new HashMap<String, String>();
		rep.put("3", SKIP_ROWS);
		rep.put("9", PAGESIZE);
		rep.put("12", TOTAL_ROWS);
		rep.put("4", SKIP_ROWS_PLUS1);
		rep.put("13", TOTAL_ROWS_PLUS1);
		rep.put("55", PAGESIZE);
		rep.put("56", TOTAL_ROWS_PLUS1);

		sql = replaceDialectStr(dialectName, sql, "3", rep.get("3").toLowerCase());
		sql = replaceDialectStr(dialectName, sql, "9", rep.get("9").toLowerCase());
		sql = replaceDialectStr(dialectName, sql, "12", rep.get("12").toLowerCase());
		sql = replaceDialectStr(dialectName, sql, "4", rep.get("4").toLowerCase());
		sql = replaceDialectStr(dialectName, sql, "13", rep.get("13").toLowerCase());

		sql = replaceDialectStr(dialectName, sql, "55", rep.get("55").toLowerCase());// no
																						// offset
		sql = replaceDialectStr(dialectName, sql, "56", rep.get("56").toLowerCase());// no
																						// offset

		sql = replaceDialectStr(dialectName, sql, " FIRST ", " first ");
		sql = replaceDialectStr(dialectName, sql, " TOP ", " top ");
		sql = replaceDialectStr(dialectName, sql, "  ", " ");

		// Analyze limits string like: 2=3,1=9,
		Map<String, String> mp = new HashMap<String, String>();
		if (!StringUtils.isEmpty(limits)) {
			String[] lms = StringUtils.split(limits, ",");
			for (String keyValue : lms) {
				if (!",".equals(keyValue)) {
					String[] k_v = StringUtils.split(keyValue, "=");
					mp.put(k_v[0], k_v[1]);
				}
			}
		}
		// use $xxx to replace bait values
		for (int i = 1; i < 5; i++) {
			String v = mp.get("" + i);
			if (!StringUtils.isEmpty(v))
				sql = StringUtils.replaceOnce(sql, "?", rep.get(v));
		} 
		return sql;
	}

	private void generatePaginationSourceCode() {
		// Dao.getDefaultContext().setShowSql(true);
		// Now delete repeat pagination
		List<TB_pagination> l = dao.iQueryForEntityList(TB_pagination.class,
				"select t.* from tb_pagination t order by t.pagination, t.dialect ");

		// Delete repeat pagination test
		TB_pagination lastLine = null;
		int sortorder = 1;
		for (TB_pagination thisLine : l) {
			thisLine.setSortorder(sortorder++);
			thisLine.update();
			if (lastLine != null && lastLine.getPagination().equals(thisLine.getPagination())) {
				lastLine.setPagination("");
				lastLine.update();
			}
			lastLine = thisLine;
		}

		// Now generate Java source code to console
		StringBuilder sb = new StringBuilder();

		sb.append("/**\n");
		sb.append("* Return pagination template of this Dialect\n");
		sb.append("*/\n");
		sb.append("protected static String initializePaginSQLTemplate(Dialect d) {\n");
		sb.append("switch (d.type) {\n");
		l = dao.iQueryForEntityList(TB_pagination.class, "select t.* from tb_pagination t order by t.sortorder");

		for (TB_pagination t : l) {
			String	pagin=t.getPagination(); 
			sb.append("case ").append(t.getDialect()).append(":\n");
			if (!StringUtils.isEmpty(pagin )) {
				sb.append("return ").append("NOT_SUPPORT".equals(pagin) ? "Dialect.NOT_SUPPORT"
						: "\"" + pagin + "\"").append(";\n");
			}
		}
		sb.append("default:  \n");
		sb.append("	return Dialect.NOT_SUPPORT;\n");
		sb.append("}\n");
		sb.append("}\n");

		System.out.println();
		System.out.println(sb.toString());
		System.out.println();
	}

	private void generatePaginationFirstOnlySourceCode() {
		// Dao.getDefaultContext().setShowSql(true);
		// Now delete repeat pagination
		List<TB_pagination> l = dao.iQueryForEntityList(TB_pagination.class,
				" select t.* from tb_pagination t order by t.paginationFirstOnly, t.dialect ");

		// Delete repeat pagination test
		TB_pagination lastLine = null;
		int sortorder = 1;
		for (TB_pagination thisLine : l) {
			thisLine.setSortorder2(sortorder++);
			thisLine.update();
			if (lastLine != null && lastLine.getPaginationFirstOnly().equals(thisLine.getPaginationFirstOnly())) {
				lastLine.setPaginationFirstOnly("");
				lastLine.update();
			}
			lastLine = thisLine;
		}

		// Now generate Java source code to console
		StringBuilder sb = new StringBuilder();

		sb.append("/**\n");
		sb.append(" * Return top limit sql template of this Dialect\n");
		sb.append("*/\n");
		sb.append("protected static String initializeTopLimitSqlTemplate(Dialect d) {\n");
		sb.append("switch (d.type) {\n");
		l = dao.iQueryForEntityList(TB_pagination.class, " select t.* from tb_pagination t order by t.sortorder2 ");

		for (TB_pagination t : l) {
			sb.append("case ").append(t.getDialect()).append(":\n");
			if (!StringUtils.isEmpty(t.getPaginationFirstOnly())) {
				sb.append("return ").append("NOT_SUPPORT".equals(t.getPaginationFirstOnly()) ? "Dialect.NOT_SUPPORT"
						: "\"" + t.getPaginationFirstOnly() + "\"").append(";\n");
			}
		}
		sb.append("default:  \n");
		sb.append("	return Dialect.NOT_SUPPORT;\n");
		sb.append("}\n");
		sb.append("}\n");

		System.out.println();
		System.out.println(sb.toString());
		System.out.println();
	}
}