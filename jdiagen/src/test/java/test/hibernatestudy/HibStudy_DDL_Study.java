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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.apache.commons.io.FileUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.DB2390Dialect;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.FirebirdDialect;
import org.hibernate.dialect.HANAColumnStoreDialect;
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.dialect.PostgresPlusDialect;
import org.hibernate.dialect.RDMSOS2200Dialect;
import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.dialect.SybaseASE15Dialect;
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
public class HibStudy_DDL_Study {
	private static String fileName = "f:/export.sql";

	private static void ddlExport(Class<?> dialect, String... oneTableHbmXml) {
		Properties p = new Properties();
		p.setProperty("hibernate.dialect", dialect.getName());
		p.setProperty("hibernate.id.new_generator_mappings", "true"); 
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(p).build();
		MetadataSources mes= new MetadataSources(sr);
		for (String oneStr : oneTableHbmXml) {
			mes.addInputStream(StrUtily.getStringInputStream(oneStr));
		}
		Metadata  metadata=mes.buildMetadata();
		System.out.println("=======dialect=" + metadata.getDatabase().getDialect() + "\n");
		StrUtily.appendFileWithText(fileName, "=======dialect=" + metadata.getDatabase().getDialect() + "\n");
		try {
			EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT, TargetType.STDOUT);
			SchemaExport export = new SchemaExport();
			export.setDelimiter(";");
			export.setFormat(true);
			export.setOutputFile(fileName);
			export.execute(targetTypes, SchemaExport.Action.BOTH, metadata);
		} catch (Exception e) {
			System.out.println("Not support");
			StrUtily.appendFileWithText(fileName,"No support");
			e.printStackTrace();
		}
	}
	
	private static void ddlExport(Class<?> dialect, Class<?>... annotatedEntityClass) {
		Properties p = new Properties();
		p.setProperty("hibernate.dialect", dialect.getName());
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(p).build();
		MetadataSources mes= new MetadataSources(sr);
		for (Class<?> clazz : annotatedEntityClass) {
			mes.addAnnotatedClass(clazz);
		}
		Metadata  metadata=mes.buildMetadata();
		System.out.println("=======dialect=" + metadata.getDatabase().getDialect() + "\n");
		StrUtily.appendFileWithText(fileName, "=======dialect=" + metadata.getDatabase().getDialect() + "\n");
		
//		System.out.println("YZ=================");
//		Database database= metadata.getDatabase(); 
//		for ( Namespace namespace : database.getNamespaces() ) { 
//			System.out.println("aa");
//			// sequences
//			System.out.println("bb="+namespace.getSequences());
//			for ( Sequence sequence : namespace.getSequences() ) {
//				System.out.println("cc"); 
//				} 
//		}
//		System.out.println("YZ=================");
		
		
		try {
			EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT, TargetType.STDOUT);
			SchemaExport export = new SchemaExport();
			export.setDelimiter(";");
			export.setFormat(true);
			export.setOutputFile(fileName);
			
			export.execute(targetTypes, SchemaExport.Action.BOTH, metadata);
		} catch (Exception e) {
			System.out.println("Not support");
			StrUtily.appendFileWithText(fileName,"No support");
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
    
    
	public static class CheckString extends TextSupport {
/*	 <id name="id" type="java.lang.String">
	  	  <column name="id" length="32" not-null="false"  unique="true" />
		  <generator class="uuid2"/> 
	  </id>
<property name="foo" type="integer">
    <column name="foo" check="foo> 10"/>
</property> 
*/}
	
	@Test
	public void testCheck() throws IOException { 
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass, getConfigXML(new CheckString()));
		}
		System.exit(0);
	}
	
	public static class CheckString2 extends TextSupport {
/*<?xml version="1.0" encoding="utf-8"?>
		<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD//EN"
		 "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping> 
    <class name="test.config.po.Customer" table="customertable" check="foo> 10">		  
<id name="id" type="java.lang.String">
	  	  <column name="id" length="32" not-null="false"  unique="true" />
		  <generator class="uuid2"/> 
	  </id>
<property name="foo" type="integer">
    <column name="foo"  />
</property>
   
*/}
	
	@Test
	public void testCheck2() throws IOException { 
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass,  ""+new CheckString2()+new XmlEnd());
		}
		System.exit(0);
	}
	
	
	
	public static class sequenceString extends TextSupport {
/*	   <id name="id" column="id">
	     <generator class="sequence">
	     <param name="seq1"/>
	     </generator>
	    </id>
*/}
	
	@Test
	public void testSequence() throws IOException { 
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass, getConfigXML(new sequenceString()));
		}
		System.exit(0);
	}    
	
	
	public static class identityString extends TextSupport {
/*	   <id name="id" column="id" type="long" >
       <generator class="identity"/>
       </id>        
*/}
	
	@Test
	public void testIdentity() throws IOException {  
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass, getConfigXML(new identityString()));
		}
		System.exit(0);
	} 
	
	
	public static class identityString2 extends TextSupport {
/*	 <id name="id" type="java.lang.String">
	  	  <column name="id" length="32" not-null="true"  unique="true" />
		  <generator class="identity"/>
	  </id>
<property name="foo" type="integer">
  <column name="foo" check="foo> 10"/>
</property> 
*/}
	
	@Test
	public void testIdentity2() throws IOException {  
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass, getConfigXML(new identityString2()));
		}
		System.exit(0);
	} 
	
	@Test
	public void testIdentity3() throws IOException {  
		FileUtils.writeStringToFile(new File(fileName), ""); 
		ddlExport(SybaseASE15Dialect.class, getConfigXML(new identityString2())); 
		System.exit(0);
	} 
 
	public static class CommentString extends TextSupport {
/*		  <id name="id" type="java.lang.String">
		  	  <column name="id" length="32" />
			  <generator class="uuid2"/> 
		  </id>
        <property name="usName" type="java.lang.String">  
            <column name="us_name" length="128">  
              <comment>this is user name</comment>
            </column>  
        </property>  
        <property name="usPwd" type="java.lang.String">  
            <column name="us_pwd" length="128">  
                <comment>this is password</comment>  
            </column>  
        </property>  
*/}	
	 
	@Test
	public void testComments() throws IOException {  
		FileUtils.writeStringToFile(new File(fileName), "");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> diaClass : dialects) {
			ddlExport(diaClass, getConfigXML(new CommentString()));
		}
		System.exit(0);
	} 
	
	@Test
	public void testComments2() throws IOException {  
		FileUtils.writeStringToFile(new File(fileName), ""); 
		ddlExport(DB2Dialect.class, getConfigXML(new CommentString())); 
		ddlExport(MariaDBDialect.class, getConfigXML(new CommentString()));
		ddlExport(SQLServer2012Dialect.class, getConfigXML(new CommentString())); 
		System.exit(0);
	} 
	
	 
		public static class SequencyString extends TextSupport {
	/* <id name="userId" type="java.lang.Long">
            <column name="USER_ID" precision="9" scale="0" />
            <generator class="sequence">
            <param name="sequence">PUB_ID_SEQ</param>
            </generator>
        </id>    
	        <property name="foo" type="java.lang.String">  
	            <column name="us_pwd" length="128"/>
	        </property>  
	*/}	
		 
		@Test
		public void testSequency() throws IOException {  
			FileUtils.writeStringToFile(new File(fileName), "");
			List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
			for (Class<? extends Dialect> diaClass : dialects) {
				ddlExport(diaClass, getConfigXML(new SequencyString()));
			}
			System.exit(0);
		} 
		
		@Entity 
		@Table(name="SequencySampleTable",catalog="",schema="")
		public static class EntitySequencySample{
			@Id
			@Column(name = "EMAIL_ID")
			//@GeneratedValue(strategy = GenerationType.SEQUENCE)
			@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailSeq")
			@SequenceGenerator(initialValue = 0, name = "emailSeq", sequenceName = "EMAIL_SEQUENCE",allocationSize=20)
			private Integer id;

			@Id
			@Column(name = "name")
			@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailSeq")
			@SequenceGenerator(initialValue = 0, name = "emailSeq", sequenceName = "EMAIL_SEQUENCE",allocationSize=20)
			private String name;
		}
		
		@Entity 
		@Table(name="SequencySampleTable2",catalog="",schema="")
		public static class EntitySequencySample2{
			@Id
			@Column(name = "EMAIL_ID2")
			//@GeneratedValue(strategy = GenerationType.SEQUENCE)
			@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emailSeq2")
			@SequenceGenerator(initialValue = 0, name = "emailSeq2", sequenceName = "EMAIL_SEQUENCE",allocationSize=20)
			private Integer id;

			@Column(name = "name")
			private String name;
		}
  
		@Test
		public void testSequency2() throws IOException {  
			FileUtils.writeStringToFile(new File(fileName), "");
			List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
			for (Class<? extends Dialect> diaClass : dialects) {
				ddlExport(diaClass, EntitySequencySample.class, EntitySequencySample2.class ); 
			}
			ddlExport(MySQL5Dialect.class, EntitySequencySample.class);
			ddlExport(FirebirdDialect.class, EntitySequencySample.class);
			ddlExport(DB2390Dialect.class, EntitySequencySample.class);
			ddlExport(Oracle12cDialect.class, EntitySequencySample.class);
			ddlExport(RDMSOS2200Dialect.class, EntitySequencySample.class); 
			System.exit(0);
		} 
		

		@Entity 
		@Table(name="SequencySampleTable",catalog="",schema="")
		public static class AutoGeneratorSample{
			@Id
			@Column(name = "EMAIL_ID")
			@GeneratedValue(strategy = GenerationType.AUTO)
			private Integer id;

			@Column(name = "name")
			private String name;
		}
		
		
		@Test
		public void testAutoGeneratorSample() throws IOException {  
			FileUtils.writeStringToFile(new File(fileName), "");
			List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
			for (Class<? extends Dialect> diaClass : dialects) {
				ddlExport(diaClass, AutoGeneratorSample.class); 
			}
			System.exit(0);
		}
		
		
		@Test
		public void testAnnotatedUnionKey() throws IOException {  
			FileUtils.writeStringToFile(new File(fileName), "");
			List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
			for (Class<? extends Dialect> diaClass : dialects) {
				ddlExport(diaClass, Students.class, IdCard.class, IdCardPK.class); 
			}
			System.exit(0);
		} 
		
		
		
		
		public static class UnionKeyString extends TextSupport {
/*<?xml version="1.0" encoding="utf-8"?>
		<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD//EN"
		 "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping> 
   <class name="test.hibernatestudy.IdCard" table="idcard">
   <composite-id name="pk" class="test.hibernatestudy.IdCardPK">
      <key-property name="pid" column="pid" type="string"/>
      <key-property name="bloodType" type="string"/>
      <generator class="assigned"/>
   </composite-id>
   <property name="province" column="province" type="string"/>
  </class>
</hibernate-mapping>*/}	
		 
		public static class UnionKeyString2 extends TextSupport {
/*<?xml version="1.0" encoding="utf-8"?>
		<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD//EN"
		 "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping> 
  <class name="test.hibernatestudy.Students" table="students">
    <id name="sid" column="sid" type="int">
      <generator class="assigned"/>
    </id>
    <property name="sname" column="sname" type="string"/>

    <many-to-one name="cardId">
      <column name="pid" unique="true"/>
      <column name="bloodType"/>
    </many-to-one>
  </class>
</hibernate-mapping>
*/}	
		
		
		@Test
		public void testUnionKey() throws IOException {  
			FileUtils.writeStringToFile(new File(fileName), "");
			List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
//			for (Class<? extends Dialect> diaClass : dialects) {
//				ddlExport(diaClass, ""+new UnionKeyString(),""+new UnionKeyString2());
//			}
			ddlExport(PostgresPlusDialect.class, ""+new UnionKeyString(),""+new UnionKeyString2());
			System.exit(0);
			
			
		} 		
		
 
		@Entity 
		@Table(name="SequencySampleTable",catalog="",schema="")
		public static class TableGeneratorSample{ 
			@Id
			@Column(name = "ID1")
			@GeneratedValue(strategy = GenerationType.TABLE,generator="gentable")
			@TableGenerator(name="gentable",initialValue=1,pkColumnName="pkcol",allocationSize=25,pkColumnValue="pkcolvalue1",table="tb1",valueColumnName="valCol1" )
 			private Integer id; 
		}
		
		@Entity 
		@Table(name="SequencySampleTable2",catalog="",schema="")
		public static class TableGeneratorSample2{ 
			@Id
			@Column(name = "ID1")
			@GeneratedValue(strategy = GenerationType.TABLE,generator="gentable")
			@TableGenerator(name="gentable",initialValue=1,pkColumnName="pkcol2",allocationSize=25,pkColumnValue="pkcolvalue2",table="tb1",valueColumnName="valCol2" )
 			private Integer id; 
		}
		
		
		@Test
		public void testTableGeneratorSample() throws IOException {  
			FileUtils.writeStringToFile(new File(fileName), "");
			List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
			for (Class<? extends Dialect> diaClass : dialects) {
				ddlExport(diaClass, TableGeneratorSample.class,TableGeneratorSample2.class); 
			}
			System.exit(0);
		}

}