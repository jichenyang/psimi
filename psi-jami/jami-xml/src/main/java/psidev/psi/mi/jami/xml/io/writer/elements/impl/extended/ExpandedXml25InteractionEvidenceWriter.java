package psidev.psi.mi.jami.xml.io.writer.elements.impl.extended;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.xml.PsiXml25ObjectCache;
import psidev.psi.mi.jami.xml.extension.InferredInteraction;
import psidev.psi.mi.jami.xml.io.writer.elements.*;
import psidev.psi.mi.jami.xml.io.writer.elements.impl.expanded.ExpandedXml25ParticipantEvidenceWriter;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Expanded XML 2.5 writer for an extended interaction evidence (having modelled, intramolecular properties, list
 * of experiments, list of interaction types, etc.).
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>18/11/13</pre>
 */

public class ExpandedXml25InteractionEvidenceWriter extends AbstractXml25InteractionEvidenceWriter<InteractionEvidence, ParticipantEvidence>
        implements ExpandedPsiXml25ElementWriter<InteractionEvidence> {
    public ExpandedXml25InteractionEvidenceWriter(XMLStreamWriter writer, PsiXml25ObjectCache objectIndex) {
        super(writer, objectIndex, new psidev.psi.mi.jami.xml.io.writer.elements.impl.expanded.ExpandedXml25ParticipantEvidenceWriter(writer, objectIndex));
    }

    public ExpandedXml25InteractionEvidenceWriter(XMLStreamWriter writer, PsiXml25ObjectCache objectIndex, PsiXml25XrefWriter primaryRefWriter, PsiXml25XrefWriter secondaryRefWriter, PsiXml25ParticipantWriter<ParticipantEvidence> participantWriter, PsiXml25ElementWriter<CvTerm> interactionTypeWriter, PsiXml25ElementWriter<Annotation> attributeWriter, PsiXml25ElementWriter<Experiment> experimentWriter, PsiXml25ElementWriter<Alias> aliasWriter, PsiXml25ElementWriter<InferredInteraction> inferredInteractionWriter1, PsiXml25ElementWriter<String> availabilityWriter, PsiXml25ElementWriter<Confidence> confidenceWriter, PsiXml25ParameterWriter parameterWriter) {
        super(writer, objectIndex, primaryRefWriter, secondaryRefWriter, participantWriter != null ? participantWriter : new ExpandedXml25ParticipantEvidenceWriter(writer, objectIndex)
                , interactionTypeWriter, attributeWriter, experimentWriter, aliasWriter, inferredInteractionWriter1, availabilityWriter, confidenceWriter, parameterWriter);
    }

    @Override
    protected void writeAvailability(InteractionEvidence object) throws XMLStreamException {
        if (object.getAvailability() != null){
            writeAvailabilityDescription(object.getAvailability());
        }
    }

    @Override
    protected void writeExperiments(InteractionEvidence object) throws XMLStreamException {
        writeExperimentDescription(object);
    }
}