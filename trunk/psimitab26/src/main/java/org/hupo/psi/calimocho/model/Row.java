package org.hupo.psi.calimocho.model;

import java.util.Collection;

/**
 * TODO document this !
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 * @since TODO add POM version
 */
public interface Row {

    boolean addField(String columnKey, Field field);

    boolean addFields(String columnKey, Collection<Field> fields);

    Collection<Field> getFields(String columnKey);

    Collection<Field> getFields(ColumnDefinition columnDefinition);

    Collection<Field> getAllFields();

}
