package psidev.psi.mi.jami.enricher;

import psidev.psi.mi.jami.enricher.impl.featureevidence.listener.FeatureEvidenceEnricherListener;
import psidev.psi.mi.jami.model.FeatureEvidence;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Gabriel Aldam (galdam@ebi.ac.uk)
 * Date: 13/06/13
 */
public interface FeatureEvidenceEnricher {

    public boolean enrichFeatureEvidence(
            FeatureEvidence featureEvidenceToEnrich, String sequenceOld, String sequenceNew);

    public void setFeatureEvidenceEnricherListener(FeatureEvidenceEnricherListener listener);
    public FeatureEvidenceEnricherListener getFeatureEvidenceEnricherListener();

    public void setCvTermEnricher(CvTermEnricher cvTermEnricher);
    public CvTermEnricher getCvTermEnricher();
}
