package psidev.psi.mi.jami.utils.comparator;

import psidev.psi.mi.jami.model.Annotation;

/**
 * Default annotation comparator
 * It compares first the topics and then the value (case insensitive)
 * - Two annotations which are null are equals
 * - The annotation which is not null is before null.
 * - use DefaultCvTermComparator to compare the topics. If they are equals, compares the values (case insensitive)
 * - If both annotations have same topic, the one with a null value is always after the one with a non null value.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>18/12/12</pre>
 */

public class DefaultAnnotationComparator extends AnnotationComparator {

    /**
     * Creates a new AnnotationComparator with DefaultCvTermComparator
     *
     */
    public DefaultAnnotationComparator() {
        super(new DefaultCvTermComparator());
    }

    @Override
    public DefaultCvTermComparator getTopicComparator() {
        return (DefaultCvTermComparator) topicComparator;
    }

    /**
     * It compares first the topics and then the value (case insensitive)
     * - Two annotations which are null are equals
     * - The annotation which is not null is before null.
     * - use DefaultCvTermComparator to compare the topics. If they are equals, compares the values (case insensitive)
     * - If both annotations have same topic, the one with a null value is always after the one with a non null value.
     * @param annotation1
     * @param annotation2
     * @return
     */
    public int compare(Annotation annotation1, Annotation annotation2) {
        return super.compare(annotation1, annotation2);
    }
}
