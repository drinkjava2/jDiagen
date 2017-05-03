/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.hibernatestudy;

import java.util.Properties;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
//import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.id.enhanced.TableGenerator;
import org.hibernate.mapping.Table;
import org.hibernate.type.IntegerType;
import org.junit.Assert;
 

/**
 * This is for study Hibernate, not related to this project, but just keep here,
 * some day may need this
 *
 * @author Yong Zhu 
 * @since 1.0.0
 */ 
public class HibernateTableExportStudy { 
	private static void assertContains(String subStr, String str) {
		if ( !str.contains( subStr ) ) {
			Assert.fail( "String [" + str + "] did not contain expected substring [" + subStr + "]" );
		}
	}
	
	public static void main(String[] args) {
		StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
				.applySetting( AvailableSettings.DIALECT, MySQLDialect.class.getName() )
				.build();

		try {
			Metadata metadata = new MetadataSources( ssr )
					.buildMetadata();

			Assert.assertEquals( 0, metadata.getDatabase().getDefaultNamespace().getTables().size() );

			TableGenerator generator = new TableGenerator();

			Properties properties = new Properties();
			generator.configure( IntegerType.INSTANCE, properties, ssr );

			generator.registerExportables( metadata.getDatabase() );

			Assert.assertEquals( 1, metadata.getDatabase().getDefaultNamespace().getTables().size() );

			final Table table = metadata.getDatabase().getDefaultNamespace().getTables().iterator().next();
			System.out.println(table.getName());
			final String[] createCommands = new MySQLDialect().getTableExporter().getSqlCreateStrings( table, metadata );
			for (String string : createCommands) {
				System.out.println(string);
			}
			//assertContains( "sequence_name varchar(255) not null", createCommands[0] );
		}
		finally {
			StandardServiceRegistryBuilder.destroy( ssr );
		}
	}
}