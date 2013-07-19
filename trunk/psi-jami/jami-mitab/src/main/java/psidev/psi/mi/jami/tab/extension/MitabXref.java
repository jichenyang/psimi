package psidev.psi.mi.jami.tab.extension;

import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.utils.comparator.xref.UnambiguousXrefComparator;

/**
 * Mitab extension for Xref.
 * It contains a FileSourceLocator
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>14/06/13</pre>
 */

public class MitabXref implements Xref,FileSourceContext {

    private FileSourceLocator sourceLocator;
    private CvTerm database;
    private String id;
    private String version;
    private CvTerm qualifier;

    public MitabXref(CvTerm database, String id, CvTerm qualifier){
        this(database, id);
        this.qualifier = qualifier;
    }

    public MitabXref(CvTerm database, String id, String version, CvTerm qualifier){
        this(database, id, version);
        this.qualifier = qualifier;
    }

    public MitabXref(CvTerm database, String id, String version){
        this(database, id);
        this.version = version;
    }

    public MitabXref(CvTerm database, String id){
        if (database == null){
            throw new IllegalArgumentException("The database is required and cannot be null");
        }
        this.database = database;
        if (id == null || (id != null && id.length() == 0)){
            throw new IllegalArgumentException("The id is required and cannot be null or empty");
        }
        this.id = id;
    }

    public MitabXref(String database, String id, String qualifier){
        this(database, id);
        this.qualifier = new MitabCvTerm(qualifier);
    }

    public MitabXref(String database, String id, CvTerm qualifier){
        this(database, id);
        this.qualifier = qualifier;
    }

    public MitabXref(String database, String id){
        if (database == null){
            throw new IllegalArgumentException("The database is required and cannot be null");
        }
        this.database = new MitabCvTerm(database);

        if (id == null || (id != null && id.length() == 0)){
            throw new IllegalArgumentException("The id is required and cannot be null or empty");
        }
        this.id = id;
    }

    public CvTerm getDatabase() {
        return database;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public CvTerm getQualifier() {
        return this.qualifier;
    }


    public void setQualifier(CvTerm qualifier){
        this.qualifier = qualifier;
    }

    public FileSourceLocator getSourceLocator() {
        return sourceLocator;
    }

    public void setSourceLocator(FileSourceLocator sourceLocator) {
        this.sourceLocator = sourceLocator;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o){
            return true;
        }

        // Xrefs are different and it has to be ExternalIdentifier
        if (!(o instanceof Xref)){
            return false;
        }

        return UnambiguousXrefComparator.areEquals(this, (Xref) o);
    }

    @Override
    public int hashCode() {
        return UnambiguousXrefComparator.hashCode(this);
    }

    @Override
    public String toString() {
        return database.toString() + ":" + id.toString() + (qualifier != null ? " (" + qualifier.toString() + ")" : "");
    }
}
