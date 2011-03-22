package org.hupo.psi.calimocho.model;

/**
 * TODO document this !
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 * @since TODO add POM version
 */
public interface ColumnDefinition {

    int getPosition();

    String getKey();

    boolean isAllowsEmpty();

    String getEmptyValue();

    String getFieldClassName();

    String getFieldParserClassName();

    String getFieldFormatterClassName();

    String getFieldSeparator();

    String getFieldDelimiter();
}
