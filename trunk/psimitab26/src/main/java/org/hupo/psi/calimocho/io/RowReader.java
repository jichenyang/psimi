package org.hupo.psi.calimocho.io;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.hupo.psi.calimocho.model.*;
import org.hupo.psi.calimocho.util.ParseUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO document this !
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 * @since TODO add POM version
 */
public class RowReader {

    private DocumentDefinition documentDefinition;

    public RowReader( DocumentDefinition documentDefinition ) {
        this.documentDefinition = documentDefinition;
    }

    public List<Row> read(Reader reader) throws IOException, IllegalRowException {


        List<Row> rows = new ArrayList<Row>();

        BufferedReader in = new BufferedReader(reader);
        String str;

        int lineNumber = 0;

        while ((str = in.readLine()) != null) {
            lineNumber ++;

            final String commentPrefix = documentDefinition.getCommentPrefix();

            if (commentPrefix != null && str.trim().startsWith( commentPrefix )) {
                continue;
            }

            try {
                rows.add(readLine(str));
            } catch ( Throwable t ) {
                throw new IllegalRowException( "Problem in line: "+lineNumber+"  /  LINE: "+str, str, lineNumber, t );
            }

        }
        in.close();

        return rows;
    }

     public Row readLine(String line) throws IllegalRowException, IllegalColumnException, IllegalFieldException {
        Row row = new DefaultRow();

         if (documentDefinition.getColumnSeparator() == null) {
             throw new NullPointerException( "Document definition does not have column separator" );
         }

         // split the lines using the column separator
         // TODO we may use other characters than quotes - should be defined in columnDefinition
        String[] cols = ParseUtils.quoteAwareSplit( line, new String[]{documentDefinition.getColumnSeparator()}, false );

         // iterate through the columns to parse the fields
        for (int i=0; i<cols.length; i++) {
            ColumnDefinition columnDefinition = documentDefinition.getColumnByPosition(i);

            if (columnDefinition == null) {
                continue;
            }

            String col = cols[i];

            // strip column delimiters
            String colDelimiter = documentDefinition.getColumnDelimiter();
            if (colDelimiter != null && colDelimiter.length() > 0) {
                if (col.startsWith(colDelimiter) && col.endsWith(colDelimiter)) {
                    col = StringUtils.removeStart( col, colDelimiter );
                    col = StringUtils.removeEnd( col, colDelimiter );
                }
            }

            // check if the column is empty
            boolean allowEmpty = columnDefinition.isAllowsEmpty();
            String emptyValue = columnDefinition.getEmptyValue() == null? "" : columnDefinition.getEmptyValue();

            if (!allowEmpty && col.equals(emptyValue)) {


                throw new IllegalColumnException( "Empty column not allowed: " + columnDefinition.getKey() + ", pos=" + columnDefinition.getPosition(), col, columnDefinition );
//                throw new IllegalRowException( "Invalid row: "+line, line, null, columnException );
            }

            String[] strFields = ParseUtils.columnSplit( col, columnDefinition.getFieldSeparator() );

            for (String strField : strFields) {
                FieldParser fieldParser = (FieldParser) newInstance(columnDefinition.getFieldParserClassName());

//                Field[] fieldArray = ParseUtils.createField(fieldParser, strField);
//
//                if (fieldArray != null) {
//                    fields.addAll( Arrays.asList( fieldArray ));
//                }

                Field field = fieldParser.parse( strField, columnDefinition );

                row.addField( columnDefinition.getKey(), field );
            }
        }


        return row;
    }

    private Object newInstance( String className ) {
        Object instance;

        try {
            Class<?> clazz = ClassUtils.getClass( className );

            instance = clazz.newInstance();
        } catch ( Throwable t ) {
            throw new RuntimeException( "Problem instantiating class: "+className, t );
        }

        return instance;
    }

    public List<Row> read( InputStream is ) throws IOException, IllegalRowException {
        return read(new BufferedReader( new InputStreamReader( is ) ));
    }
}
