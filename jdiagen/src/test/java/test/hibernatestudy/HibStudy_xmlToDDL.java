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
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.HANAColumnStoreDialect;
import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.junit.Test;

import test.codegenerator.HibernateDialectsList;
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
//@formatter:off
public class HibStudy_xmlToDDL {
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
	
	public static class XmlHead extends TextSupport {
/*<?xml version="1.0" encoding="utf-8"?>
		<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD//EN"
		 "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping> 
    <class name="test.config.po.Customer" table="customertable">		  
*/}
	
	public static class XmlEnd extends TextSupport {
/*	</class> 
</hibernate-mapping>
*/}
	
	public static class CustomerXML extends TextSupport {
/*		  <id name="id" type="java.lang.String">
		  	  <column name="id" length="32" />
			  <generator class="uuid2"/> 
		  </id>
		  <property name="customerName" type="java.lang.String">
			  <column name="customer_name" length="30" />
		  </property>
*/}

	public static String getConfigXML( TextSupport t){
		return "" + new XmlHead() + t+ new XmlEnd();
	}
	
	@Test
	public void testCustomerXML() throws IOException {
		 FileUtils.writeStringToFile(new File(fileName), "");
		 ddlExport(HANAColumnStoreDialect.class, getConfigXML(new CustomerXML()));
		 ddlExport(MySQL55Dialect.class, getConfigXML(new CustomerXML()));
		 System.exit(0);
	} 
	
	public static class CompondPKey extends TextSupport {
/*<composite-id>
	        <key-property name="name" column="name"  type="java.lang.String"/>
	        <key-property name="phone" column="phone" type="java.lang.String"/>
	    </composite-id>
*/}
	
	@Test
	public void testCompondPKey() throws IOException { 
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass, getConfigXML(new CompondPKey()));
		}
		System.exit(0);
	}
    
	public static class NotNullString extends TextSupport {
/*	 <id name="id" type="java.lang.String">
	  	  <column name="id" length="32" not-null="false" />
		  <generator class="uuid2"/> 
	  </id>
            <property name="name" column="NAME" type="string" length="25" not-null="true" />  
            <property name="email" column="EMAIL" type="string" not-null="true" />  
            <property name="password" column="PASSWORD" type="string" not-null="true" />  
            <property name="phone" column="PHONE" type="int"  not-null="true" />  
            <property name="address" column="ADDRESS" type="string"  not-null="true" />  
            <property name="sex" column="SEX" type="character"  not-null="true"  />  
            <property name="married" column="IS_MARRIED" type="boolean"  not-null="true" />  
            <property name="description" column="DESCRIPTION" type="text"  not-null="true" />  
            <property name="image" column="IMAGE" type="binary"  not-null="true" />  
            <property name="birthday" column="BIRTHDAY" type="date"  not-null="true" />  
            <property name="registeredTime" column="REGISTERED_TIME" type="timestamp" not-null="true"  /> 
*/}
	
	@Test
	public void testNotNull() throws IOException { 
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass, getConfigXML(new NotNullString()));
		}
		System.exit(0);
	}
    
	public static class UniqueString extends TextSupport {
/*	 <id name="id" type="java.lang.String">
	  	  <column name="id" length="32" not-null="false"  unique="true" />
		  <generator class="uuid2"/> 
	  </id>
            <property name="name1" column="NAME1" type="string" length="25" not-null="true" unique="true" />   
            <property name="name2" column="NAME2" type="string" length="25" not-null="false" unique="true" />   
*/}
	
	@Test
	public void testUnique() throws IOException { 
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass, getConfigXML(new UniqueString()));
		}
		System.exit(0);
	}
    
 
	 

}