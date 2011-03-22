package org.hupo.psi.calimocho.model;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO document this !
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 * @since TODO add POM version
 */
public class AbstractDocumentDefinition extends AbstractDefined implements DocumentDefinition {

    private List<ColumnDefinition> columns;
    private String name;
    private String definition;
    private String columnSeparator;
    private String columnDelimiter;
    private String commentPrefix;
    private boolean partial;

    public AbstractDocumentDefinition() {
        columns = new ArrayList<ColumnDefinition>();
    }

    public List<ColumnDefinition> getColumns() {
        return columns;
    }

    public void setColumns( List<ColumnDefinition> columns ) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition( String definition ) {
        this.definition = definition;
    }

    public String getColumnSeparator() {
        return columnSeparator;
    }

    public void setColumnSeparator( String columnSeparator ) {
        this.columnSeparator = columnSeparator;
    }

    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    public void setColumnDelimiter( String columnDelimiter ) {
        this.columnDelimiter = columnDelimiter;
    }

    public String getCommentPrefix() {
        return commentPrefix;
    }

    public boolean isPartial() {
        return partial;
    }

    public void setPartial( boolean partial ) {
        this.partial = partial;
    }

    public ColumnDefinition getColumnByPosition( int position ) {
        // TODO this can be optimized
        for (ColumnDefinition columnDefinition : columns) {
            if (position == columnDefinition.getPosition()) {
                return columnDefinition;
            }
        }

        return null;
    }

    public void setCommentPrefix( String commentPrefix ) {
        this.commentPrefix = commentPrefix;
    }
}
