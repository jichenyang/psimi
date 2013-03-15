package psidev.psi.mi.xml.io;

import org.apache.commons.lang.exception.ExceptionUtils;
import psidev.psi.mi.xml.PsimiXmlReaderException;
import psidev.psi.mi.xml.events.InvalidXmlEvent;
import psidev.psi.mi.xml.listeners.PsiXml25ParserListener;
import psidev.psi.mi.xml.model.ExperimentDescription;
import psidev.psi.mi.xml.xmlindex.IndexedEntry;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator for Experiments within the xml
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>15/03/13</pre>
 */

public class XmlExperimentIterator implements Iterator<ExperimentDescription> {

    private Iterator<IndexedEntry> indexedEntriesIterator;
    private Iterator<ExperimentDescription> experimentIterator;
    private ExperimentDescription nextExperiment;
    private List<PsiXml25ParserListener> listeners;

    public XmlExperimentIterator(List<IndexedEntry> indexedEntries, List<PsiXml25ParserListener> listeners){
        if (indexedEntries == null){
            throw new IllegalArgumentException("The indexed entries is mandatory and cannot be null");
        }
        this.indexedEntriesIterator = indexedEntries.iterator();
        if (listeners == null){
            this.listeners = Collections.EMPTY_LIST;
        }

        processNextExperiment();
    }

    private void processNextExperiment(){
        ExperimentDescription desc = null;
        while (this.experimentIterator.hasNext() && desc == null){
            desc = this.experimentIterator.next();
        }
        while (this.indexedEntriesIterator.hasNext() && desc == null){
            IndexedEntry entry = indexedEntriesIterator.next();
            try {
                experimentIterator = entry.unmarshallExperimentIterator();

                while (experimentIterator.hasNext() && desc == null){
                   desc = experimentIterator.next();
                }

            } catch (PsimiXmlReaderException e) {
                InvalidXmlEvent evt = new InvalidXmlEvent("Error while reading the next entry. " + ExceptionUtils.getFullStackTrace(e));
                evt.setColumnNumber(0);
                evt.setLineNumber((int) entry.getEntryIndexElement().getLineNumber());
                for (PsiXml25ParserListener l : listeners){
                    l.fireOnInvalidXmlSyntax(evt);
                }
            }
        }

        this.nextExperiment = desc;
    }

    public boolean hasNext() {
        return nextExperiment != null;
    }

    public ExperimentDescription next() {
        if (!hasNext()){
            throw new NoSuchElementException();
        }

        ExperimentDescription current = this.nextExperiment;
        processNextExperiment();
        return current;
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove experiment description");
    }
}
