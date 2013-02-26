/*
 * Copyright (c) 2002 The European Bioinformatics Institute, and others.
 * All rights reserved. Please see the file LICENSE
 * in the root directory of this distribution.
 */
package psidev.psi.mi.tab.model;

import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.impl.DefaultCvTerm;

/**
 * Interactor's alias.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since <pre>03-Oct-2006</pre>
 */
public class AliasImpl implements Alias, psidev.psi.mi.jami.model.Alias {

    /**
     * Generated with IntelliJ plugin generateSerialVersionUID.
     * To keep things consistent, please use the same thing.
     */
    private static final long serialVersionUID = -3188006290975989586L;

    /////////////////////////
    // Instance variables

    /**
     * Database from which the alias comes from.
     */
    private String dbSource;

    private static String UNSPECIFIED;

    private CvTerm type;
    private String name;

    /////////////////////////////////
    // Constructor

    public AliasImpl() {
        // create unknown alias
        this.name = UNSPECIFIED;
    }

    public AliasImpl( String dbSource, String name ) {
        setDbSource(dbSource);

        if (name == null){
            this.name = UNSPECIFIED;
        }
        else {
            this.name = name;
        }
    }

    public AliasImpl( String dbSource, String name, String aliasType ) {
        this(dbSource, name);

        if (aliasType != null){
            this.type = new DefaultCvTerm(aliasType);
        }
    }

    /////////////////////////////////
    // Getters and setters

    /**
     * {@inheritDoc}
     */
    public String getDbSource() {
        return dbSource;
    }

    /**
     * {@inheritDoc}
     */
    public void setDbSource( String dbSource ) {
        if (dbSource == null){
            dbSource = "unknown";
        }
        this.dbSource = dbSource;
    }

    /**
     * {@inheritDoc}
     */
    public void setName( String name ) {
        if ( name == null ) {
            throw new IllegalArgumentException( "Alias name cannot be null." );
        }
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public String getAliasType() {
        return this.type != null? this.type.getShortName() : null;
    }

    /**
     * {@inheritDoc}
     */
    public void setAliasType( String aliasType ) {
        if ( aliasType != null ) {
            // ignore empty string
            aliasType = aliasType.trim();
            if ( aliasType.length() == 0 ) {
                aliasType = null;
            }
            this.type = new DefaultCvTerm(aliasType);
        }
        else {
            this.type = null;
        }
    }

    ///////////////////////////////
    // Object's override

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append( "Alias" );
        sb.append( "{dbSource='" ).append( dbSource ).append( '\'' );
        sb.append( ", name='" ).append( name ).append( '\'' );
		sb.append( ", type='" ).append( this.type != null ? this.type.getShortName() : "-" ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        final AliasImpl alias = ( AliasImpl ) o;

        if ( !name.equals( alias.name ) ) {
            return false;
        }

        if ( dbSource != null ? !dbSource.equals( alias.dbSource ) : alias.dbSource != null ) {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result;
        result = ( dbSource != null ? dbSource.hashCode() : 0 );
        result = 29 * result + name.hashCode();
        return result;
    }

    public CvTerm getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
