/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import test.codegenerator.HibernateDialectsList;
import util.StrUtily;

/**
 * This is for study Hibernate, not related to this project, but just keep here,
 * some day may need this
 *
 * @author Yong Zhu
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class HibernateCreateTableStudy {

	private static String customerXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?> <!DOCTYPE hibernate-mapping PUBLIC   \"-//Hibernate/Hibernate Mapping DTD//EN\"  \"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">   <hibernate-mapping>      <class name=\"test.config.po.Customer\" table=\"customertable\" catalog=\"test\"> 		  <id name=\"id\" type=\"java.lang.String\"> 		  	  <column name=\"id\" length=\"32\" /> 			  <generator class=\"uuid2\"/>  		  </id> 		  <property name=\"customerName\" type=\"java.lang.String\"> 			  <column name=\"customer_name\" length=\"30\" /> 		  </property> 	</class>  </hibernate-mapping>";

	private static void ddlExport(Class<?> dialect) {
		Properties p = new Properties();
		p.setProperty("hibernate.dialect", dialect.getName());
//		p.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false");
//		p.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
//		p.setProperty("hibernate.connection.username", "root");
//		p.setProperty("hibernate.connection.password", "root888");

		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(p).build();
		Metadata metadata = new MetadataSources(sr).addInputStream(StrUtily.getStringInputStream(customerXML))
				.buildMetadata();
		   
		String fileName = "f:/export.sql";
		StrUtily.appendFileWithText(fileName, "=======dialect=" + metadata.getDatabase().getDialect()+"\n");
		try {
			EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT, TargetType.STDOUT);
			SchemaExport export = new SchemaExport();
			export.setDelimiter(";");
			export.setFormat(true);
			export.setOutputFile(fileName);
			export.execute(targetTypes, SchemaExport.Action.BOTH, metadata);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass);
		}
		System.exit(0);
	}

}