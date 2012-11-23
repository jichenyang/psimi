package psidev.psi.mi.jami.model;

import java.util.Collection;
import java.util.Set;

/**
 * Interaction involving some molecules in a specific experiment
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>23/11/12</pre>
 */

public interface Interaction {

    public Experiment getExperiment();
    public void setExperiment(Experiment experiment);

    public String getShortName();
    public void setShortName(String name);

    public String getFullName();
    public void setFullName(String name);

    public Set<Xref> getXrefs();

    public Set<Annotation> getAnnotations();

    public CvTerm getType();
    public void setType(CvTerm term);

    public Source getSource();
    public void setSource(Source source);

    public String getAvailability();
    public void setAvailability(String availability);

    public Collection<Participant> getParticipants();

    public Set<Confidence> getConfidences();

    public Set<Parameter> getParameters();

    public boolean isNegative();
    public void setNegative(boolean negative);

    public boolean isModelled();
    public void setModelled(boolean modelled);
}
