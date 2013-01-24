package psidev.psi.mi.jami.utils.comparator.interactor;

import psidev.psi.mi.jami.model.BioactiveEntity;

/**
 * Unambiguous bioactive entity comparator.
 * It will first use UnambiguousInteractorBaseComparator to compare the basic interactor properties.
 * If the basic interactor properties are the same, It will look first for CHEBI identifier (the interactor with a non null CHEBI identifier will always come first). If the CHEBI identifiers are not set, it will look at the
 * smiles (the interactor with a non null smile will always come first). If at the smiles are not set, it will look at the standard Inchi key (the interactor with a non null standard inchi key will always come first). If the standard Inchi keys are not set, it
 * will look at the standard Inchi (the interactor with a non null standard inchi will always come first).
 * This comparator will ignore all the other properties of an interactor.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>15/01/13</pre>
 */

public class UnambiguousBioactiveEntityComparator extends BioactiveEntityComparator {

    private static UnambiguousBioactiveEntityComparator unambiguousBioactiveEntityComparator;

    /**
     * Creates a new UnambiguousBioactiveEntityComparator. It will use an UnambiguousInteractorBaseComparator
     */
    public UnambiguousBioactiveEntityComparator() {
        super(new UnambiguousInteractorBaseComparator());
    }

    @Override
    /**
     * It will first use UnambiguousInteractorBaseComparator to compare the basic interactor properties.
     * If the basic interactor properties are the same, It will look first for CHEBI identifier (the interactor with a non null CHEBI identifier will always come first). If the CHEBI identifiers are not set, it will look at the
     * smiles (the interactor with a non null smile will always come first). If at the smiles are not set, it will look at the standard Inchi key (the interactor with a non null standard inchi key will always come first). If the standard Inchi keys are not set, it
     * will look at the standard Inchi (the interactor with a non null standard inchi will always come first).
     * This comparator will ignore all the other properties of an interactor.
     */
    public int compare(BioactiveEntity bioactiveEntity1, BioactiveEntity bioactiveEntity2) {
        int EQUAL = 0;
        int BEFORE = -1;
        int AFTER = 1;

        if (bioactiveEntity1 == null && bioactiveEntity2 == null){
            return EQUAL;
        }
        else if (bioactiveEntity1 == null){
            return AFTER;
        }
        else if (bioactiveEntity2 == null){
            return BEFORE;
        }
        else {

            // First compares the basic interactor properties
            int comp = interactorComparator.compare(bioactiveEntity1, bioactiveEntity2);
            if (comp != 0){
                return comp;
            }

            // then compares CHEBI identifiers
            String chebi1 = bioactiveEntity1.getChebi();
            String chebi2 = bioactiveEntity2.getChebi();

            if (chebi1 != null && chebi2 != null){
                return chebi1.compareTo(chebi2);
            }
            else if (chebi1 != null){
                return BEFORE;
            }
            else if (chebi2 != null){
                return AFTER;
            }

            // compares smile
            String smile1 = bioactiveEntity1.getSmile();
            String smile2 = bioactiveEntity2.getSmile();

            if (smile1 != null && smile2 != null){
                return smile1.compareTo(smile2);
            }
            else if (smile1 != null){
                return BEFORE;
            }
            else if (smile2 != null){
                return AFTER;
            }

            // compares standard InChi key
            String inchikey1 = bioactiveEntity1.getStandardInchiKey();
            String inchiKey2 = bioactiveEntity2.getStandardInchiKey();

            if (inchikey1 != null && inchiKey2 != null){
                return inchikey1.compareTo(inchiKey2);
            }
            else if (inchikey1 != null){
                return BEFORE;
            }
            else if (inchiKey2 != null){
                return AFTER;
            }

            // compares standard inchi
            String inchi1 = bioactiveEntity1.getStandardInchi();
            String inchi2 = bioactiveEntity2.getStandardInchi();

            if (inchi1 != null && inchi2 != null){
                return inchi1.compareTo(inchi2);
            }
            else if (inchi1 != null){
                return BEFORE;
            }
            else if (inchi2 != null){
                return AFTER;
            }

            return comp;
        }
    }

    @Override
    public UnambiguousInteractorBaseComparator getInteractorComparator() {
        return (UnambiguousInteractorBaseComparator) this.interactorComparator;
    }

    /**
     * Use UnambiguousBioactiveEntityComparator to know if two bioactive entities are equals.
     * @param entity1
     * @param entity2
     * @return true if the two bioactive entities are equal
     */
    public static boolean areEquals(BioactiveEntity entity1, BioactiveEntity entity2){
        if (unambiguousBioactiveEntityComparator == null){
            unambiguousBioactiveEntityComparator = new UnambiguousBioactiveEntityComparator();
        }

        return unambiguousBioactiveEntityComparator.compare(entity1, entity2) == 0;
    }
}
