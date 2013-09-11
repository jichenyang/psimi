package psidev.psi.mi.jami.enricher;

import psidev.psi.mi.jami.bridges.fetcher.PublicationFetcher;
import psidev.psi.mi.jami.enricher.exception.EnricherException;
import psidev.psi.mi.jami.enricher.listener.PublicationEnricherListener;
import psidev.psi.mi.jami.model.Publication;

import java.util.Collection;

/**
 * An enricher for publications which can enrich either a single publication or a collection.
 * It must be initiated with a fetcher.
 * Sub enriches: CvTerm.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * @since  31/07/13
 */
public interface PublicationEnricher {

    /**
     * Takes a publication and uses the publication fetcher to add additional details.
     * If the publication is null an illegal state exception is thrown
     * @param publicationToEnrich   The publication to be enriched
     * @throws EnricherException    Thrown if problems are encountered in the fetcher
     * @throws IllegalArgumentException if publicationToEnrich is null
     */
    public void enrichPublication(Publication publicationToEnrich) throws EnricherException;

    /**
     * Takes a collection of publications and enriches each in turn.
     * @param publicationsToEnrich      The publications to be enriched
     * @throws EnricherException        Thrown if problems are encountered in the fetcher
     * @throws IllegalArgumentException if collection is null
     */
    public void enrichPublications(Collection<Publication> publicationsToEnrich) throws EnricherException;

    /**
     * Gets the publication fetcher which is currently being used to retrieve entries
     * @return  the current publication fetcher.
     */
    public PublicationFetcher getPublicationFetcher();

    public void setCvTermEnricher(CvTermEnricher cvTermEnricher);

    public CvTermEnricher getCvTermEnricher();

    /**
     * Sets the listener to report publication changes to.
     * Can be null.
     * @param listener the new publication listener
     */
    public void setPublicationEnricherListener(PublicationEnricherListener listener);

    /**
     * Gets the current publication listener
     * @return  the current publication listener
     */
    public PublicationEnricherListener getPublicationEnricherListener();
}
