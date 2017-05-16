/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.HANAColumnStoreDialect;
import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import util.StrUtily;
import util.TextSupport;

/**
 * This is for study Hibernate how to build create table ddl
 *
 * @author Yong Zhu
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class HibernateCreateTableStudy {
	private static String fileName = "f:/export.sql";

	private static void ddlExport(Class<?> dialect, String oneTableHbmXml) {
		Properties p = new Properties();
		p.setProperty("hibernate.dialect", dialect.getName());
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(p).build();
		Metadata metadata = new MetadataSources(sr).addInputStream(StrUtily.getStringInputStream(oneTableHbmXml))
				.buildMetadata();
		System.out.println("=======dialect=" + metadata.getDatabase().getDialect() + "\n");
		StrUtily.appendFileWithText(fileName, "=======dialect=" + metadata.getDatabase().getDialect() + "\n");
		try {
			EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT, TargetType.STDOUT);
			SchemaExport export = new SchemaExport();
			export.setDelimiter(";");
			export.setFormat(true);
			export.setOutputFile(fileName);
			export.execute(targetTypes, SchemaExport.Action.CREATE, metadata);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		 FileUtils.writeStringToFile(new File(fileName), "");
		 ddlExport(HANAColumnStoreDialect.class, "" + new DTDString() + new
		 CustomerXML()); 
		
		 FileUtils.writeStringToFile(new File(fileName), "");
		 ddlExport(MySQL55Dialect.class, "" + new DTDString() + new
		 CustomerXML());
		 System.exit(0);
		
		

//		FileUtils.writeStringToFile(new File(fileName), "");
//		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
//		for (Class<? extends Dialect> diaClass : dialects) {
//			ddlExport(diaClass, "" + new DTDString() + new CustomerXML());
//		}
//		System.exit(0);
	}

	//@formatter:off
	public static class DTDString extends TextSupport {
/*<?xml version="1.0" encoding="utf-8"?>
		<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD//EN"
		 "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd"> 
*/
	}
	
	public static class CompondPKey extends TextSupport {
/*
 <hibernate-mapping> 
	<class name="test.config.po.Customer" table="customertable" catalog="test">
	    <composite-id>
	        <key-property name="name" column="name"  type="java.lang.String"/>
	        <key-property name="phone" column="phone" type="java.lang.String"/>
	    </composite-id>
	</class> 
</hibernate-mapping>
*/
	}
    
    
    
	public static class CustomerXML extends TextSupport {
/*
<hibernate-mapping> 
    <class name="test.config.po.Customer" table="customertable">
		  <id name="id" type="java.lang.String">
		  	  <column name="id" length="32" />
			  <generator class="uuid2"/> 
		  </id>
		  <property name="customerName" type="java.lang.String">
			  <column name="customer_name" length="30" />
		  </property>
	</class> 
</hibernate-mapping>
*/
	}
	
 	public static class EndTag{}

}