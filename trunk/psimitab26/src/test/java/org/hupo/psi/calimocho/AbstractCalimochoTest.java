package org.hupo.psi.calimocho;

import org.hupo.psi.calimocho.io.formatter.KeyValueFieldFormatter;
import org.hupo.psi.calimocho.io.formatter.LiteralFieldFormatter;
import org.hupo.psi.calimocho.io.parser.KeyValueFieldParser;
import org.hupo.psi.calimocho.io.parser.LiteralFieldParser;
import org.hupo.psi.calimocho.model.CalimochoKeys;
import org.hupo.psi.calimocho.model.ColumnBasedDocumentDefinition;
import org.hupo.psi.calimocho.model.ColumnDefinitionBuilder;
import org.hupo.psi.calimocho.model.DocumentDefinitionBuilder;

/**
 * TODO document this !
 *
 * @author Christine Jandrasits (cjandras@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public abstract class AbstractCalimochoTest {

    public static final String NEW_LINE = System.getProperty( "line.separator" );

    public ColumnBasedDocumentDefinition buildGeneListDefinition() {
        return new DocumentDefinitionBuilder()
                .addColumnDefinition( new ColumnDefinitionBuilder()
                                              .setKey( "gene" )
                                              .setPosition( 0 )
                                              .setEmptyValue( "" )
                                              .setIsAllowsEmpty( false )
                                              .setFieldSeparator( "," )
                                              .setFieldDelimiter( "" )
                                              .setFieldParser( new LiteralFieldParser() )
                                              .setFieldFormatter( new LiteralFieldFormatter() )
                                              .build() )
                .addColumnDefinition( new ColumnDefinitionBuilder()
                                              .setKey( "taxid" )
                                              .setPosition( 1 )
                                              .setEmptyValue( "" )
                                              .setIsAllowsEmpty( false )
                                              .setFieldSeparator( "," )
                                              .setFieldDelimiter( "" )
                                              .setFieldParser( new LiteralFieldParser() )
                                              .setFieldFormatter( new LiteralFieldFormatter() )
                                              .build() )
                .setColumnSeparator( "|" )
                .build();
    }

    public ColumnBasedDocumentDefinition buildAnotherGeneListDefinition() {
        return new DocumentDefinitionBuilder()
                .addColumnDefinition( new ColumnDefinitionBuilder()
                                              .setKey( "taxid" )
                                              .setPosition( 0 )
                                              .setEmptyValue( "" )
                                              .setIsAllowsEmpty( false )
                                              .setFieldSeparator( "," )
                                              .setFieldParser( new KeyValueFieldParser(":") )
                                              .setFieldFormatter(new KeyValueFieldFormatter(":") )
                                              .addDefaultValue( CalimochoKeys.KEY, "taxid" )
                                              .build() )
                .addColumnDefinition( new ColumnDefinitionBuilder()
                                              .setKey( "gene" )
                                              .setPosition( 1 )
                                              .setEmptyValue( "" )
                                              .setIsAllowsEmpty( false )
                                              .setFieldSeparator( "," )
                                              .setFieldParser( new KeyValueFieldParser(":") )
                                              .setFieldFormatter( new KeyValueFieldFormatter(":") )
                                              .addDefaultValue( CalimochoKeys.KEY, "gene" )
                                              .build() )
                .setColumnSeparator( "," )
                .setColumnDelimiter( "'" )
                .build();
    }

    public ColumnBasedDocumentDefinition buildYetAnotherGeneListDefinition() {
        return new DocumentDefinitionBuilder()
                .addColumnDefinition( new ColumnDefinitionBuilder()
                                              .setKey( "taxid" )
                                              .setPosition( 0 )
                                              .setEmptyValue( "" )
                                              .setIsAllowsEmpty( false )
                                              .setFieldSeparator( "," )
                                              .setFieldParser( new KeyValueFieldParser(":") )
                                              .setFieldFormatter(new KeyValueFieldFormatter(":") )
                                              .addDefaultValue( CalimochoKeys.KEY, "taxid" )
                                              .build() )
                .addColumnDefinition( new ColumnDefinitionBuilder()
                                              .setKey( "gene" )
                                              .setPosition( 1 )
                                              .setEmptyValue( "" )
                                              .setIsAllowsEmpty( false )
                                              .setFieldSeparator( "," )
                                              .setFieldDelimiter( "/" )
                                              .setFieldParser( new KeyValueFieldParser(":") )
                                              .setFieldFormatter( new KeyValueFieldFormatter(":") )
                                              .addDefaultValue( CalimochoKeys.KEY, "gene" )
                                              .build() )
                .setColumnSeparator( "\t" )
                .build();
    }
}
