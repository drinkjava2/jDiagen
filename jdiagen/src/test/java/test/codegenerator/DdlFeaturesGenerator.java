/*
 * jDialects, a tiny SQL dialect tool 
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package test.codegenerator;

import static com.github.drinkjava2.jsqlbox.SqlHelper.empty;
import static com.github.drinkjava2.jsqlbox.SqlHelper.q;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.drinkjava2.jsqlbox.Dao;

import test.config.PrepareTestContext;

/**
 * This is to collect Hibernate's Dialect.class features, but seems miss some
 * properties for DDL
 *
 * @author Yong Zhu
 * @since 1.0.0
 */
public class DdlFeaturesGenerator {
	private static final String NOT_SUPPORT = "NOT_SUPPORT";

	@Before
	public void setup() {
		PrepareTestContext.prepareDatasource_setDefaultSqlBoxConetxt_recreateTables();
		// Dao.getDefaultContext().setShowSql(true);
	}

	// Clean up, close DataSource pool, close default SqlBoxContext
	@After
	public void cleanUp() {
		PrepareTestContext.closeDatasource_closeDefaultSqlBoxConetxt();
	}

	@Test
	public void collectDDLFeatures() {
		String createSQL = "create table tb_hibdll ("//
				+ "feature varchar(100)  " //
				+ ", constraint const_other_feature primary key (feature)" //
				+ ")";
		Dao.executeQuiet("drop table tb_hibdll");
		Dao.execute(createSQL);
		Dao.refreshMetaData();
		exportOtherFeatures();
		// a quick bug fix of RDMSOS2200Dialect
		Dao.execute("update tb_hibdll set RDMSOS2200Dialect=?", empty(NOT_SUPPORT), " where feature=?",
				empty("createSequenceStrings"));
		Dao.execute("update tb_hibdll set RDMSOS2200Dialect=?", empty("false"), " where feature=?",
				empty("supportsSequences"));
		generateInitDdlFeaturesSourceCode();
	}

	public void generateInitDdlFeaturesSourceCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("protected static void initDDLFeatures(Dialect dia, DDLFeatures ddl) {\n");
		sb.append("switch (dia) {\n");
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> hibDialectClass : dialects) {
			Dialect d = TestTypeMappingCodeGenerator.buildDialectByName(hibDialectClass);
			String diaName = d.getClass().getSimpleName();
			sb.append("case " + diaName + ": {");
			List<Map<String, Object>> result = Dao
					.queryForList("select feature, " + diaName + " from tb_hibdll order by feature");
			for (Map<String, Object> map : result) {
				String value = (String) map.get(diaName);
				sb.append("ddl.").append(map.get("feature")).append("=");
				if (!NOT_SUPPORT.equals(value) && !"false".equalsIgnoreCase(value) && !"true".equalsIgnoreCase(value))
					sb.append("\"");
				sb.append(value);
				if (!NOT_SUPPORT.equals(value) && !"false".equalsIgnoreCase(value) && !"true".equalsIgnoreCase(value))
					sb.append("\"");
				sb.append(";\n");
			}
			sb.append("} break;\n");
		}
		sb.append("default:\n");
		sb.append("}}");
		System.out.println(sb.toString());
	}

	public void dealOneFeature(Dialect d, String feature, String... featureValue) {
		Dao.executeQuiet("insert into tb_hibdll (feature) values(?)", empty(feature));

		StringBuilder sb = new StringBuilder();
		for (String str : featureValue) {
			sb.append(str);
		}

		String writeValue = sb.toString();
		if (!StringUtils.isEmpty(writeValue) && writeValue.length() > 500)
			writeValue = writeValue.substring(0, 500);
		if ("null".equalsIgnoreCase(writeValue))
			writeValue = NOT_SUPPORT;
		if (StringUtils.containsIgnoreCase(writeValue, "not support"))
			writeValue = NOT_SUPPORT;

		Dao.execute("update tb_hibdll set "//
				, d.getClass().getSimpleName(), "=", q(writeValue), " where feature=", q(feature));
	}

	//@formatter:off  close Eclipse formatter 
	@SuppressWarnings("deprecation")
	public void exportOtherFeatures() { 
		List<Class<? extends Dialect>> dialects = HibernateDialectsList.SUPPORTED_DIALECTS;
		for (Class<? extends Dialect> hibDialectClass : dialects) {
			Dialect d = TestTypeMappingCodeGenerator.buildDialectByName(hibDialectClass); 
			Dao.execute("alter table tb_hibdll add  " + d.getClass().getSimpleName() + " varchar(500)");
		       String[] _FKS={"_FK1","_FK2"};                                                                                                                		
		       String[] _REFS={"_REF1","_REF2"}; 
		       try{dealOneFeature(d,"addColumnString", ""+d.getAddColumnString());}catch(Exception e){dealOneFeature(d,"addColumnString", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"addColumnSuffixString", ""+d.getAddColumnSuffixString());}catch(Exception e){dealOneFeature(d,"addColumnSuffixString", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"addForeignKeyConstraintString", ""+d.getAddForeignKeyConstraintString("_FKEYNAME", _FKS, "_REFTABLE", _REFS, true));}catch(Exception e){dealOneFeature(d,"addForeignKeyConstraintString", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"addPrimaryKeyConstraintString", ""+d.getAddPrimaryKeyConstraintString("_PKEYNAME"));}catch(Exception e){dealOneFeature(d,"addPrimaryKeyConstraintString", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"createCatalogCommand",  d.getCreateCatalogCommand("_CATALOGNAME"));}catch(Exception e){dealOneFeature(d,"createCatalogCommand", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"createPooledSequenceStrings", d.getCreateSequenceStrings("_SEQ", 11, 33));}catch(Exception e){dealOneFeature(d,"createPooledSequenceStrings", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"createSchemaCommand",  d.getCreateSchemaCommand("_SCHEMANAME"));}catch(Exception e){dealOneFeature(d,"createSchemaCommand", NOT_SUPPORT);}                                                                                                                
 		       try{dealOneFeature(d,"createSequenceStrings",  d.getCreateSequenceStrings("_SEQ"));}catch(Exception e){dealOneFeature(d,"createSequenceStrings", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"createTableString", ""+d.getCreateTableString());}catch(Exception e){dealOneFeature(d,"createTableString", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"currentSchemaCommand", ""+d.getCurrentSchemaCommand());}catch(Exception e){dealOneFeature(d,"currentSchemaCommand", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"dropCatalogCommand", d.getDropCatalogCommand("_CATALOGNAME"));}catch(Exception e){dealOneFeature(d,"dropCatalogCommand", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"dropForeignKeyString", ""+d.getDropForeignKeyString());}catch(Exception e){dealOneFeature(d,"dropForeignKeyString", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"dropSchemaCommand",  d.getDropSchemaCommand("_SCHEMANAME"));}catch(Exception e){dealOneFeature(d,"dropSchemaCommand", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"dropSequenceStrings",  d.getDropSequenceStrings("_SEQNAME"));}catch(Exception e){dealOneFeature(d,"dropSequenceStrings", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"dropTableString", ""+d.getDropTableString("_TABLENAME"));}catch(Exception e){dealOneFeature(d,"dropTableString", NOT_SUPPORT);}                                                                                                                
		    
		      
		       try{dealOneFeature(d,"hasAlterTable", ""+d.hasAlterTable());}catch(Exception e){dealOneFeature(d,"hasAlterTable", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"needDropConstraintsBeforeDropTable", ""+d.dropConstraints());}catch(Exception e){dealOneFeature(d,"needDropConstraintsBeforeDropTable", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"supportsCommentOn", ""+d.supportsCommentOn());}catch(Exception e){dealOneFeature(d,"supportsCommentOn", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"supportsIfExistsAfterConstraintName", ""+d.supportsIfExistsAfterConstraintName());}catch(Exception e){dealOneFeature(d,"supportsIfExistsAfterConstraintName", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"supportsIfExistsAfterTableName", ""+d.supportsIfExistsAfterTableName());}catch(Exception e){dealOneFeature(d,"supportsIfExistsAfterTableName", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"supportsIfExistsBeforeConstraintName", ""+d.supportsIfExistsBeforeConstraintName());}catch(Exception e){dealOneFeature(d,"supportsIfExistsBeforeConstraintName", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"supportsIfExistsBeforeTableName", ""+d.supportsIfExistsBeforeTableName());}catch(Exception e){dealOneFeature(d,"supportsIfExistsBeforeTableName", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"supportsPooledSequences", ""+d.supportsPooledSequences());}catch(Exception e){dealOneFeature(d,"supportsPooledSequences", NOT_SUPPORT);}                                                                                                                
		       try{dealOneFeature(d,"supportsSequences", ""+d.supportsSequences());}catch(Exception e){dealOneFeature(d,"supportsSequences", NOT_SUPPORT);}   
		      
		       IdentityColumnSupport ics=d.getIdentityColumnSupport(); 
		       try{dealOneFeature(d,"identityColumnString", ics.getIdentityColumnString(Types.INTEGER));}catch(Exception e){dealOneFeature(d,"identityColumnString", NOT_SUPPORT);}
		       try{dealOneFeature(d,"identityColumnStringBigINT", ics.getIdentityColumnString(Types.BIGINT));}catch(Exception e){dealOneFeature(d,"identityColumnStringBigINT", NOT_SUPPORT);}		       
		       try{dealOneFeature(d,"identityInsertString", ics.getIdentityInsertString());}catch(Exception e){dealOneFeature(d,"identityInsertString",NOT_SUPPORT);}	
		       try{dealOneFeature(d,"identitySelectString", ics.getIdentitySelectString("_table", "_col", 999));}catch(Exception e){dealOneFeature(d,"identitySelectString", NOT_SUPPORT);}
		       try{dealOneFeature(d,"hasDataTypeInIdentityColumn", ""+ics.hasDataTypeInIdentityColumn());}catch(Exception e){dealOneFeature(d,"hasDataTypeInIdentityColumn", NOT_SUPPORT);}
		       try{dealOneFeature(d,"supportsIdentityColumns", ""+ics.supportsIdentityColumns());}catch(Exception e){dealOneFeature(d,"supportsIdentityColumns", NOT_SUPPORT);}
		       try{dealOneFeature(d,"supportsInsertSelectIdentity", ""+ics.supportsInsertSelectIdentity());}catch(Exception e){dealOneFeature(d,"supportsInsertSelectIdentity", NOT_SUPPORT);}

//		       try{dealOneFeature(d,"getUniqueDelegate", ""+d.getUniqueDelegate());}catch(Exception e){dealOneFeature(d,"getUniqueDelegate", e.getMessage());}                                                                                                                
 		       try{dealOneFeature(d,"tableTypeString", ""+d.getTableTypeString());}catch(Exception e){dealOneFeature(d,"tableTypeString", e.getMessage());}                                                                                                                
 		       try{dealOneFeature(d,"createMultisetTableString", ""+d.getCreateMultisetTableString());}catch(Exception e){dealOneFeature(d,"createMultisetTableString", e.getMessage());}
		       try{dealOneFeature(d,"nullColumnString", ""+d.getNullColumnString());}catch(Exception e){dealOneFeature(d,"nullColumnString", e.getMessage());}
		       try{dealOneFeature(d,"supportsColumnCheck", ""+d.supportsColumnCheck());}catch(Exception e){dealOneFeature(d,"supportsColumnCheck", e.getMessage());}
 		       try{dealOneFeature(d,"supportsTableCheck", ""+d.supportsTableCheck());}catch(Exception e){dealOneFeature(d,"supportsTableCheck", e.getMessage());}		       
		       try{dealOneFeature(d,"columnComment", ""+d.getColumnComment("_COMMENT"));}catch(Exception e){dealOneFeature(d,"columnComment", e.getMessage());}
 		       try{dealOneFeature(d,"selectSequenceNextValString", ""+d.getSelectSequenceNextValString("_SEQNAME"));}catch(Exception e){dealOneFeature(d,"selectSequenceNextValString", e.getMessage());}                                                                                                                
 		       try{dealOneFeature(d,"sequenceNextValString", ""+d.getSequenceNextValString("_SEQNAME"));}catch(Exception e){dealOneFeature(d,"sequenceNextValString", e.getMessage());}                                                                                                                
 		  
		       
//		       try{dealOneFeature(d,"getAddUniqueConstraintString(String)", ""+d.getAddUniqueConstraintString("UNIQUECONS"));}catch(Exception e){dealOneFeature(d,"getAddUniqueConstraintString(String)", e.getMessage());}                                                                                                                		
//	           try{dealOneFeature(d,"supportsUniqueConstraintInCreateAlterTable", ""+d.supportsUniqueConstraintInCreateAlterTable());}catch(Exception e){dealOneFeature(d,"supportsUniqueConstraintInCreateAlterTable", e.getMessage());}                                                                                                                		
				   			   			       
   
		       
//below seems not important to DDL		
//		       try{dealOneFeature(d,"canCreateCatalog", ""+d.canCreateCatalog());}catch(Exception e){dealOneFeature(d,"canCreateCatalog", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"canCreateSchema", ""+d.canCreateSchema());}catch(Exception e){dealOneFeature(d,"canCreateSchema", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getAddForeignKeyConstraintString(String, String)", ""+d.getAddForeignKeyConstraintString("CONSNM", "FKEYNAME"));}catch(Exception e){dealOneFeature(d,"getAddForeignKeyConstraintString(String, String)", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getAddForeignKeyConstraintString_false", ""+d.getAddForeignKeyConstraintString("_FKEYNAME", _FKS, "_REFTABLE", _REFS, false));}catch(Exception e){dealOneFeature(d,"getAddForeignKeyConstraintString(String, String[], String, String[], false)", e.getMessage());}                                                                                                                
                                                                                                                
                                                                                                                
//		       try{dealOneFeature(d,"getIdentityColumnSupport", ""+d.getIdentityColumnSupport());}catch(Exception e){dealOneFeature(d,"getIdentityColumnSupport", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getNoColumnsInsertString", ""+d.getNoColumnsInsertString());}catch(Exception e){dealOneFeature(d,"getNoColumnsInsertString", e.getMessage());}                                                                                                                
                                                                                                                
//		       try{dealOneFeature(d,"getQuerySequencesString", ""+d.getQuerySequencesString());}catch(Exception e){dealOneFeature(d,"getQuerySequencesString", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getSequenceInformationExtractor", ""+d.getSequenceInformationExtractor());}catch(Exception e){dealOneFeature(d,"getSequenceInformationExtractor", e.getMessage());}                                                                                                                
 //		       try{dealOneFeature(d,"getTableComment", ""+d.getTableComment("_TABLECOMMENT"));}catch(Exception e){dealOneFeature(d,"getTableComment", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"hasSelfReferentialForeignKeyBug", ""+d.hasSelfReferentialForeignKeyBug());}catch(Exception e){dealOneFeature(d,"hasSelfReferentialForeignKeyBug", e.getMessage());}                                                                                                                

//		       try{dealOneFeature(d,"areStringComparisonsCaseInsensitive", ""+d.areStringComparisonsCaseInsensitive());}catch(Exception e){dealOneFeature(d,"areStringComparisonsCaseInsensitive", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"buildSQLExceptionConversionDelegate", ""+d.buildSQLExceptionConversionDelegate());}catch(Exception e){dealOneFeature(d,"buildSQLExceptionConversionDelegate", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"CLOSED_QUOTE", ""+d.CLOSED_QUOTE);}catch(Exception e){dealOneFeature(d,"CLOSED_QUOTE", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"closeQuote", ""+d.closeQuote());}catch(Exception e){dealOneFeature(d,"closeQuote", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"createCaseFragment", ""+d.createCaseFragment());}catch(Exception e){dealOneFeature(d,"createCaseFragment", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"createOuterJoinFragment", ""+d.createOuterJoinFragment());}catch(Exception e){dealOneFeature(d,"createOuterJoinFragment", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"DEFAULT_BATCH_SIZE", ""+d.DEFAULT_BATCH_SIZE);}catch(Exception e){dealOneFeature(d,"DEFAULT_BATCH_SIZE", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"defaultScrollMode", ""+d.defaultScrollMode());}catch(Exception e){dealOneFeature(d,"defaultScrollMode", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"doesReadCommittedCauseWritersToBlockReaders", ""+d.doesReadCommittedCauseWritersToBlockReaders());}catch(Exception e){dealOneFeature(d,"doesReadCommittedCauseWritersToBlockReaders", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"doesRepeatableReadCauseReadersToBlockWriters", ""+d.doesRepeatableReadCauseReadersToBlockWriters());}catch(Exception e){dealOneFeature(d,"doesRepeatableReadCauseReadersToBlockWriters", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"forUpdateOfColumns", ""+d.forUpdateOfColumns());}catch(Exception e){dealOneFeature(d,"forUpdateOfColumns", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getAuxiliaryDatabaseObjectExporter", ""+d.getAuxiliaryDatabaseObjectExporter());}catch(Exception e){dealOneFeature(d,"getAuxiliaryDatabaseObjectExporter", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getCallableStatementSupport", ""+d.getCallableStatementSupport());}catch(Exception e){dealOneFeature(d,"getCallableStatementSupport", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getCascadeConstraintsString", ""+d.getCascadeConstraintsString());}catch(Exception e){dealOneFeature(d,"getCascadeConstraintsString", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getCaseInsensitiveLike", ""+d.getCaseInsensitiveLike());}catch(Exception e){dealOneFeature(d,"getCaseInsensitiveLike", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getColumnAliasExtractor", ""+d.getColumnAliasExtractor());}catch(Exception e){dealOneFeature(d,"getColumnAliasExtractor", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getCrossJoinSeparator", ""+d.getCrossJoinSeparator());}catch(Exception e){dealOneFeature(d,"getCrossJoinSeparator", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getCurrentTimestampSelectString", ""+d.getCurrentTimestampSelectString());}catch(Exception e){dealOneFeature(d,"getCurrentTimestampSelectString", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getCurrentTimestampSQLFunctionName", ""+d.getCurrentTimestampSQLFunctionName());}catch(Exception e){dealOneFeature(d,"getCurrentTimestampSQLFunctionName", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getDefaultBatchLoadSizingStrategy", ""+d.getDefaultBatchLoadSizingStrategy());}catch(Exception e){dealOneFeature(d,"getDefaultBatchLoadSizingStrategy", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getDefaultMultiTableBulkIdStrategy", ""+d.getDefaultMultiTableBulkIdStrategy());}catch(Exception e){dealOneFeature(d,"getDefaultMultiTableBulkIdStrategy", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getDefaultProperties", ""+d.getDefaultProperties());}catch(Exception e){dealOneFeature(d,"getDefaultProperties", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getDialect", ""+d.getDialect());}catch(Exception e){dealOneFeature(d,"getDialect", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getForeignKeyExporter", ""+d.getForeignKeyExporter());}catch(Exception e){dealOneFeature(d,"getForeignKeyExporter", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getForUpdateString", ""+d.getForUpdateString());}catch(Exception e){dealOneFeature(d,"getForUpdateString", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getFunctions", ""+d.getFunctions());}catch(Exception e){dealOneFeature(d,"getFunctions", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getIndexExporter", ""+d.getIndexExporter());}catch(Exception e){dealOneFeature(d,"getIndexExporter", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getInExpressionCountLimit", ""+d.getInExpressionCountLimit());}catch(Exception e){dealOneFeature(d,"getInExpressionCountLimit", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getLimitHandler", ""+d.getLimitHandler());}catch(Exception e){dealOneFeature(d,"getLimitHandler", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getLobMergeStrategy", ""+d.getLobMergeStrategy());}catch(Exception e){dealOneFeature(d,"getLobMergeStrategy", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getLowercaseFunction", ""+d.getLowercaseFunction());}catch(Exception e){dealOneFeature(d,"getLowercaseFunction", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getMaxAliasLength", ""+d.getMaxAliasLength());}catch(Exception e){dealOneFeature(d,"getMaxAliasLength", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getNameQualifierSupport", ""+d.getNameQualifierSupport());}catch(Exception e){dealOneFeature(d,"getNameQualifierSupport", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getNativeIdentifierGeneratorClass", ""+d.getNativeIdentifierGeneratorClass());}catch(Exception e){dealOneFeature(d,"getNativeIdentifierGeneratorClass", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getNativeIdentifierGeneratorStrategy", ""+d.getNativeIdentifierGeneratorStrategy());}catch(Exception e){dealOneFeature(d,"getNativeIdentifierGeneratorStrategy", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getSchemaNameResolver", ""+d.getSchemaNameResolver());}catch(Exception e){dealOneFeature(d,"getSchemaNameResolver", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getSelectGUIDString", ""+d.getSelectGUIDString());}catch(Exception e){dealOneFeature(d,"getSelectGUIDString", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getSequenceExporter", ""+d.getSequenceExporter());}catch(Exception e){dealOneFeature(d,"getSequenceExporter", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getTableExporter", ""+d.getTableExporter());}catch(Exception e){dealOneFeature(d,"getTableExporter", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getUniqueKeyExporter", ""+d.getUniqueKeyExporter());}catch(Exception e){dealOneFeature(d,"getUniqueKeyExporter", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"getViolatedConstraintNameExtracter", ""+d.getViolatedConstraintNameExtracter());}catch(Exception e){dealOneFeature(d,"getViolatedConstraintNameExtracter", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"isCurrentTimestampSelectStringCallable", ""+d.isCurrentTimestampSelectStringCallable());}catch(Exception e){dealOneFeature(d,"isCurrentTimestampSelectStringCallable", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"isJdbcLogWarningsEnabledByDefault", ""+d.isJdbcLogWarningsEnabledByDefault());}catch(Exception e){dealOneFeature(d,"isJdbcLogWarningsEnabledByDefault", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"isLegacyLimitHandlerBehaviorEnabled", ""+d.isLegacyLimitHandlerBehaviorEnabled());}catch(Exception e){dealOneFeature(d,"isLegacyLimitHandlerBehaviorEnabled", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"isLockTimeoutParameterized", ""+d.isLockTimeoutParameterized());}catch(Exception e){dealOneFeature(d,"isLockTimeoutParameterized", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"NO_BATCH", ""+d.NO_BATCH);}catch(Exception e){dealOneFeature(d,"NO_BATCH", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"openQuote", ""+d.openQuote());}catch(Exception e){dealOneFeature(d,"openQuote", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"qualifyIndexName", ""+d.qualifyIndexName());}catch(Exception e){dealOneFeature(d,"qualifyIndexName", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"QUOTE", ""+d.QUOTE);}catch(Exception e){dealOneFeature(d,"QUOTE", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"quote(String)", ""+d.quote("QUOTE"));}catch(Exception e){dealOneFeature(d,"quote(String)", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"replaceResultVariableInOrderByClauseWithPosition", ""+d.replaceResultVariableInOrderByClauseWithPosition());}catch(Exception e){dealOneFeature(d,"replaceResultVariableInOrderByClauseWithPosition", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"requiresCastingOfParametersInSelectClause", ""+d.requiresCastingOfParametersInSelectClause());}catch(Exception e){dealOneFeature(d,"requiresCastingOfParametersInSelectClause", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"requiresParensForTupleDistinctCounts", ""+d.requiresParensForTupleDistinctCounts());}catch(Exception e){dealOneFeature(d,"requiresParensForTupleDistinctCounts", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsBindAsCallableArgument", ""+d.supportsBindAsCallableArgument());}catch(Exception e){dealOneFeature(d,"supportsBindAsCallableArgument", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsCascadeDelete", ""+d.supportsCascadeDelete());}catch(Exception e){dealOneFeature(d,"supportsCascadeDelete", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsCaseInsensitiveLike", ""+d.supportsCaseInsensitiveLike());}catch(Exception e){dealOneFeature(d,"supportsCaseInsensitiveLike", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsCircularCascadeDeleteConstraints", ""+d.supportsCircularCascadeDeleteConstraints());}catch(Exception e){dealOneFeature(d,"supportsCircularCascadeDeleteConstraints", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsCurrentTimestampSelection", ""+d.supportsCurrentTimestampSelection());}catch(Exception e){dealOneFeature(d,"supportsCurrentTimestampSelection", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsEmptyInList", ""+d.supportsEmptyInList());}catch(Exception e){dealOneFeature(d,"supportsEmptyInList", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsExistsInSelect", ""+d.supportsExistsInSelect());}catch(Exception e){dealOneFeature(d,"supportsExistsInSelect", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsExpectedLobUsagePattern", ""+d.supportsExpectedLobUsagePattern());}catch(Exception e){dealOneFeature(d,"supportsExpectedLobUsagePattern", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsLobValueChangePropogation", ""+d.supportsLobValueChangePropogation());}catch(Exception e){dealOneFeature(d,"supportsLobValueChangePropogation", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsLockTimeouts", ""+d.supportsLockTimeouts());}catch(Exception e){dealOneFeature(d,"supportsLockTimeouts", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsNationalizedTypes", ""+d.supportsNationalizedTypes());}catch(Exception e){dealOneFeature(d,"supportsNationalizedTypes", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsNonQueryWithCTE", ""+d.supportsNonQueryWithCTE());}catch(Exception e){dealOneFeature(d,"supportsNonQueryWithCTE", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsOuterJoinForUpdate", ""+d.supportsOuterJoinForUpdate());}catch(Exception e){dealOneFeature(d,"supportsOuterJoinForUpdate", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsParametersInInsertSelect", ""+d.supportsParametersInInsertSelect());}catch(Exception e){dealOneFeature(d,"supportsParametersInInsertSelect", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsPartitionBy", ""+d.supportsPartitionBy());}catch(Exception e){dealOneFeature(d,"supportsPartitionBy", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsResultSetPositionQueryMethodsOnForwardOnlyCursor", ""+d.supportsResultSetPositionQueryMethodsOnForwardOnlyCursor());}catch(Exception e){dealOneFeature(d,"supportsResultSetPositionQueryMethodsOnForwardOnlyCursor", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsRowValueConstructorSyntax", ""+d.supportsRowValueConstructorSyntax());}catch(Exception e){dealOneFeature(d,"supportsRowValueConstructorSyntax", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsRowValueConstructorSyntaxInInList", ""+d.supportsRowValueConstructorSyntaxInInList());}catch(Exception e){dealOneFeature(d,"supportsRowValueConstructorSyntaxInInList", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsSubqueryOnMutatingTable", ""+d.supportsSubqueryOnMutatingTable());}catch(Exception e){dealOneFeature(d,"supportsSubqueryOnMutatingTable", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsSubselectAsInPredicateLHS", ""+d.supportsSubselectAsInPredicateLHS());}catch(Exception e){dealOneFeature(d,"supportsSubselectAsInPredicateLHS", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsTupleCounts", ""+d.supportsTupleCounts());}catch(Exception e){dealOneFeature(d,"supportsTupleCounts", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsTupleDistinctCounts", ""+d.supportsTupleDistinctCounts());}catch(Exception e){dealOneFeature(d,"supportsTupleDistinctCounts", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsTuplesInSubqueries", ""+d.supportsTuplesInSubqueries());}catch(Exception e){dealOneFeature(d,"supportsTuplesInSubqueries", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsUnboundedLobLocatorMaterialization", ""+d.supportsUnboundedLobLocatorMaterialization());}catch(Exception e){dealOneFeature(d,"supportsUnboundedLobLocatorMaterialization", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsUnionAll", ""+d.supportsUnionAll());}catch(Exception e){dealOneFeature(d,"supportsUnionAll", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"supportsValuesList", ""+d.supportsValuesList());}catch(Exception e){dealOneFeature(d,"supportsValuesList", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"toString", ""+d.toString());}catch(Exception e){dealOneFeature(d,"toString", e.getMessage());}                                                                                                                
//		       try{dealOneFeature(d,"useInputStreamToInsertBlob", ""+d.useInputStreamToInsertBlob());}catch(Exception e){dealOneFeature(d,"useInputStreamToInsertBlob", e.getMessage());}                                                                                                                

//below has mistake or not important		       
//	       try{dealOneFeature(d,"getDialect(Properties)", ""+d.getDialect(Properties));}catch(Exception e){dealOneFeature(d,"getDialect(Properties)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"contributeTypes", ""+d.contributeTypes(TypeContributions, ServiceRegistry));}catch(Exception e){dealOneFeature(d,"contributeTypes(TypeContributions, ServiceRegistry)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getTypeName", ""+d.getTypeName(int));}catch(Exception e){dealOneFeature(d,"getTypeName(int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getTypeName", ""+d.getTypeName(int, long, int, int));}catch(Exception e){dealOneFeature(d,"getTypeName(int, long, int, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getCastTypeName", ""+d.getCastTypeName(int));}catch(Exception e){dealOneFeature(d,"getCastTypeName(int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"cast", ""+d.cast(String, int, int, int, int));}catch(Exception e){dealOneFeature(d,"cast(String, int, int, int, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"cast", ""+d.cast(String, int, int));}catch(Exception e){dealOneFeature(d,"cast(String, int, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"cast", ""+d.cast(String, int, int, int));}catch(Exception e){dealOneFeature(d,"cast(String, int, int, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"remapSqlTypeDescriptor", ""+d.remapSqlTypeDescriptor(SqlTypeDescriptor));}catch(Exception e){dealOneFeature(d,"remapSqlTypeDescriptor(SqlTypeDescriptor)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getHibernateTypeName", ""+d.getHibernateTypeName(int));}catch(Exception e){dealOneFeature(d,"getHibernateTypeName(int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"isTypeNameRegistered", ""+d.isTypeNameRegistered(String));}catch(Exception e){dealOneFeature(d,"isTypeNameRegistered(String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getHibernateTypeName", ""+d.getHibernateTypeName(int, int, int, int));}catch(Exception e){dealOneFeature(d,"getHibernateTypeName(int, int, int, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"supportsLimit", ""+d.supportsLimit());}catch(Exception e){dealOneFeature(d,"supportsLimit", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"supportsLimitOffset", ""+d.supportsLimitOffset());}catch(Exception e){dealOneFeature(d,"supportsLimitOffset", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"supportsVariableLimit", ""+d.supportsVariableLimit());}catch(Exception e){dealOneFeature(d,"supportsVariableLimit", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"bindLimitParametersInReverseOrder", ""+d.bindLimitParametersInReverseOrder());}catch(Exception e){dealOneFeature(d,"bindLimitParametersInReverseOrder", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"bindLimitParametersFirst", ""+d.bindLimitParametersFirst());}catch(Exception e){dealOneFeature(d,"bindLimitParametersFirst", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"useMaxForLimit", ""+d.useMaxForLimit());}catch(Exception e){dealOneFeature(d,"useMaxForLimit", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"forceLimitUsage", ""+d.forceLimitUsage());}catch(Exception e){dealOneFeature(d,"forceLimitUsage", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"convertToFirstRowValue", ""+d.convertToFirstRowValue(1));}catch(Exception e){dealOneFeature(d,"convertToFirstRowValue(int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getLockingStrategy", ""+d.getLockingStrategy(Lockable, LockMode));}catch(Exception e){dealOneFeature(d,"getLockingStrategy(Lockable, LockMode)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getForUpdateString", ""+d.getForUpdateString(LockOptions));}catch(Exception e){dealOneFeature(d,"getForUpdateString(LockOptions)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getForUpdateString", ""+d.getForUpdateString(LockMode));}catch(Exception e){dealOneFeature(d,"getForUpdateString(LockMode)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getWriteLockString", ""+d.getWriteLockString(int));}catch(Exception e){dealOneFeature(d,"getWriteLockString(int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getWriteLockString(", ""+d.getWriteLockString(String, int));}catch(Exception e){dealOneFeature(d,"getWriteLockString(String, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getReadLockString", ""+d.getReadLockString(int));}catch(Exception e){dealOneFeature(d,"getReadLockString(int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getReadLockString", ""+d.getReadLockString(String, int));}catch(Exception e){dealOneFeature(d,"getReadLockString(String, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getForUpdateString", ""+d.getForUpdateString("forUPD"));}catch(Exception e){dealOneFeature(d,"getForUpdateString(String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getForUpdateString", ""+d.getForUpdateString("forUPD", LockOptions));}catch(Exception e){dealOneFeature(d,"getForUpdateString(String, LockOptions)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getForUpdateNowaitString", ""+d.getForUpdateNowaitString());}catch(Exception e){dealOneFeature(d,"getForUpdateNowaitString", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getForUpdateSkipLockedString", ""+d.getForUpdateSkipLockedString());}catch(Exception e){dealOneFeature(d,"getForUpdateSkipLockedString", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getForUpdateNowaitString", ""+d.getForUpdateNowaitString(String));}catch(Exception e){dealOneFeature(d,"getForUpdateNowaitString(String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getForUpdateSkipLockedString", ""+d.getForUpdateSkipLockedString(String));}catch(Exception e){dealOneFeature(d,"getForUpdateSkipLockedString(String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"appendLockHint", ""+d.appendLockHint(LockMode, String));}catch(Exception e){dealOneFeature(d,"appendLockHint(LockMode, String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"appendLockHint", ""+d.appendLockHint(LockOptions, String));}catch(Exception e){dealOneFeature(d,"appendLockHint(LockOptions, String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"applyLocksToSql", ""+d.applyLocksToSql(String, LockOptions, Map<String, String[]>));}catch(Exception e){dealOneFeature(d,"applyLocksToSql(String, LockOptions, Map<String, String[]>)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"registerResultSetOutParameter(CallableStatement, int)", ""+d.registerResultSetOutParameter(CallableStatement, int));}catch(Exception e){dealOneFeature(d,"registerResultSetOutParameter(CallableStatement, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"registerResultSetOutParameter(CallableStatement, String)", ""+d.registerResultSetOutParameter(CallableStatement, String));}catch(Exception e){dealOneFeature(d,"registerResultSetOutParameter(CallableStatement, String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getResultSet(CallableStatement)", ""+d.getResultSet(CallableStatement));}catch(Exception e){dealOneFeature(d,"getResultSet(CallableStatement)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getResultSet(CallableStatement, int)", ""+d.getResultSet(CallableStatement, int));}catch(Exception e){dealOneFeature(d,"getResultSet(CallableStatement, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getResultSet(CallableStatement, String)", ""+d.getResultSet(CallableStatement, String));}catch(Exception e){dealOneFeature(d,"getResultSet(CallableStatement, String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"buildSQLExceptionConverter", ""+d.buildSQLExceptionConverter());}catch(Exception e){dealOneFeature(d,"buildSQLExceptionConverter", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getSelectClauseNullString(int)", ""+d.getSelectClauseNullString(int));}catch(Exception e){dealOneFeature(d,"getSelectClauseNullString(int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"transformSelectString(String)", ""+d.transformSelectString(String));}catch(Exception e){dealOneFeature(d,"transformSelectString(String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"toBooleanValueString(boolean)", ""+d.toBooleanValueString(boolean));}catch(Exception e){dealOneFeature(d,"toBooleanValueString(boolean)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getKeywords", ""+d.getKeywords());}catch(Exception e){dealOneFeature(d,"getKeywords", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"buildIdentifierHelper(IdentifierHelperBuilder, DatabaseMetaData)", ""+d.buildIdentifierHelper(IdentifierHelperBuilder, DatabaseMetaData));}catch(Exception e){dealOneFeature(d,"buildIdentifierHelper(IdentifierHelperBuilder, DatabaseMetaData)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"forceLobAsLastValue", ""+d.forceLobAsLastValue());}catch(Exception e){dealOneFeature(d,"forceLobAsLastValue", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"useFollowOnLocking", ""+d.useFollowOnLocking());}catch(Exception e){dealOneFeature(d,"useFollowOnLocking", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"useFollowOnLocking(QueryParameters)", ""+d.useFollowOnLocking(QueryParameters));}catch(Exception e){dealOneFeature(d,"useFollowOnLocking(QueryParameters)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getNotExpression(String)", ""+d.getNotExpression(String));}catch(Exception e){dealOneFeature(d,"getNotExpression(String)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"supportsUnique", ""+d.supportsUnique());}catch(Exception e){dealOneFeature(d,"supportsUnique", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"supportsNotNullUnique", ""+d.supportsNotNullUnique());}catch(Exception e){dealOneFeature(d,"supportsNotNullUnique", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getQueryHintString(String, List<String>)", ""+d.getQueryHintString(String, List<String>));}catch(Exception e){dealOneFeature(d,"getQueryHintString(String, List<String>)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"augmentRecognizedTableTypes(List<String>)", ""+d.augmentRecognizedTableTypes(List<String>));}catch(Exception e){dealOneFeature(d,"augmentRecognizedTableTypes(List<String>)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"supportsNamedParameters(DatabaseMetaData)", ""+d.supportsNamedParameters(DatabaseMetaData));}catch(Exception e){dealOneFeature(d,"supportsNamedParameters(DatabaseMetaData)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"getLimitString", ""+d.getLimitString("sql", 1, 1));}catch(Exception e){dealOneFeature(d,"getLimitString(String, int, int)", e.getMessage());}                                                                                                                		
//	       try{dealOneFeature(d,"renderOrderByElement(String, String, String, NullPrecedence)", ""+d.renderOrderByElement(String, String, String, NullPrecedence));}catch(Exception e){dealOneFeature(d,"renderOrderByElement(String, String, String, NullPrecedence)", e.getMessage());}                                                                                                                		
  
		
		}
			
	}

}