package psidev.psi.mi.jami.utils.comparator.participant;

import psidev.psi.mi.jami.model.Participant;
import psidev.psi.mi.jami.utils.comparator.cv.UnambiguousCvTermComparator;
import psidev.psi.mi.jami.utils.comparator.feature.UnambiguousFeatureComparator;
import psidev.psi.mi.jami.utils.comparator.interactor.UnambiguousInteractorBaseComparator;
import psidev.psi.mi.jami.utils.comparator.parameter.UnambiguousParameterComparator;

/**
 * Unambiguous participant comparator
 * It will first compare the interactors using UnambiguousInteractorBaseComparator. If both interactors are the same,
 * it will compare the biological roles using UnambiguousCvTermComparator. If both biological roles are the same, it
 * will look at the stoichiometry (participant with lower stoichiometry will come first). If the stoichiometry is the same for both participants,
 * it will compare the features using a UnambiguousFeatureComparator. If both participants have the same features, it will look at
 * the participant parameters using UnambiguousParameterComparator.
 *
 * This comparator will ignore all the other properties of a participant.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>16/01/13</pre>
 */

public class UnambiguousParticipantComparator extends ParticipantComparator{
    private static UnambiguousParticipantComparator unambiguousParticipantComparator;

    /**
     * Creates a new UnambiguousParticipantComparator. It will use a UnambiguousInteractorBaseComparator to compare
     * interactors, a UnambiguousCvTermComparator to compare biological roles, a UnambiguousFeatureComparator to
     * compare features and a UnambiguousParameterComparator to compare parameters.
     */
    public UnambiguousParticipantComparator() {
        super(new UnambiguousInteractorBaseComparator(), new UnambiguousCvTermComparator(), new UnambiguousFeatureComparator(), new UnambiguousParameterComparator());
    }

    @Override
    public UnambiguousInteractorBaseComparator getInteractorComparator() {
        return (UnambiguousInteractorBaseComparator) this.interactorComparator;
    }

    @Override
    public UnambiguousCvTermComparator getCvTermComparator() {
        return (UnambiguousCvTermComparator) this.cvTermComparator;
    }

    @Override
    /**
     * It will first compare the interactors using UnambiguousInteractorBaseComparator. If both interactors are the same,
     * it will compare the biological roles using UnambiguousCvTermComparator. If both biological roles are the same, it
     * will look at the stoichiometry (participant with lower stoichiometry will come first). If the stoichiometry is the same for both participants,
     * it will compare the features using a UnambiguousFeatureComparator. If both participants have the same features, it will look at
     * the participant parameters using UnambiguousParameterComparator.
     *
     * This comparator will ignore all the other properties of a participant.
     */
    public int compare(Participant participant1, Participant participant2) {
        return super.compare(participant1, participant2);
    }

    /**
     * Use UnambiguousParticipantComparator to know if two participants are equals.
     * @param participant1
     * @param participant2
     * @return true if the two participants are equal
     */
    public static boolean areEquals(Participant participant1, Participant participant2){
        if (unambiguousParticipantComparator == null){
            unambiguousParticipantComparator = new UnambiguousParticipantComparator();
        }

        return unambiguousParticipantComparator.compare(participant1, participant2) == 0;
    }
}
