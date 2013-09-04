package psidev.psi.mi.jami.utils.comparator.interactor;

import psidev.psi.mi.jami.model.Gene;
import psidev.psi.mi.jami.model.Interactor;

import java.util.Comparator;

/**
 * Unambiguous gene comparator.
 * It will first use UnambiguousInteractorBaseComparator to compare the basic interactor properties
 * If the basic interactor properties are the same, It will look at ensembl identifier (the interactor with non null ensembl identifier will always come first). If the ensembl identifiers are not set, it will look at the
 * ensemblGenome identifiers (the interactor with non null ensembl genome identifier will always come first). If the ensemblGemome identifiers are not set,
 * it will look at the entrez/gene id (the interactor with non null entrez/gene id will always come first).
 * If the entrez/gene ids are not set, it will look at the refseq identifiers (the interactor with non null refseq identifier will always come first).
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>15/01/13</pre>
 */

public class UnambiguousGeneComparator implements Comparator<Gene> {

    private static UnambiguousGeneComparator unambiguousGeneComparator;
    protected Comparator<Interactor> interactorBaseComparator;

    /**
     * Creates a new UnambiguousGeneComparator. It will uses a UnambiguousInteractorBaseComparator to compare interactor properties
     */
    public UnambiguousGeneComparator(){
        this.interactorBaseComparator = new UnambiguousInteractorBaseComparator();
    }

    protected UnambiguousGeneComparator(Comparator<Interactor> interactorBaseComparator){
        this.interactorBaseComparator = interactorBaseComparator != null ? interactorBaseComparator : new UnambiguousInteractorBaseComparator();
    }

    public Comparator<Interactor> getInteractorComparator() {
        return interactorBaseComparator;
    }

    /**
     * It will first use UnambiguousInteractorBaseComparator to compare the basic interactor properties
     * If the basic interactor properties are the same, It will look at ensembl identifier (the interactor with non null ensembl identifier will always come first). If the ensembl identifiers are not set, it will look at the
     * ensemblGenome identifiers (the interactor with non null ensembl genome identifier will always come first). If the ensemblGemome identifiers are not set,
     * it will look at the entrez/gene id (the interactor with non null entrez/gene id will always come first).
     * If the entrez/gene ids are not set, it will look at the refseq identifiers (the interactor with non null refseq identifier will always come first).
     *
     */
    public int compare(Gene gene1, Gene gene2) {
        int EQUAL = 0;
        int BEFORE = -1;
        int AFTER = 1;

        if (gene1 == null && gene2 == null){
            return EQUAL;
        }
        else if (gene1 == null){
            return AFTER;
        }
        else if (gene2 == null){
            return BEFORE;
        }
        else {
            // First compares the interactor properties
            int comp = interactorBaseComparator.compare(gene1, gene2);
            if (comp != 0){
                return comp;
            }

            // first compares ensembl identifiers
            String ensembl1 = gene1.getEnsembl();
            String ensembl2 = gene2.getEnsembl();

            if (ensembl1 != null && ensembl2 != null){
                comp = ensembl1.compareTo(ensembl2);
                if (comp != 0){
                    return comp;
                }
            }
            else if (ensembl1 != null){
                return BEFORE;
            }
            else if (ensembl2 != null){
                return AFTER;
            }

            // compares ensemblGenomes identifier
            String ensemblGenome1 = gene1.getEnsemblGenome();
            String ensemblGenome2 = gene2.getEnsemblGenome();

            if (ensemblGenome1 != null && ensemblGenome2 != null){
                comp = ensemblGenome1.compareTo(ensemblGenome2);
                if (comp != 0){
                    return comp;
                }
            }
            else if (ensemblGenome1 != null){
                return BEFORE;
            }
            else if (ensemblGenome2 != null){
                return AFTER;
            }

            // compares entrez/gene Id
            String geneId1 = gene1.getEntrezGeneId();
            String geneId2 = gene2.getEntrezGeneId();

            if (geneId1 != null && geneId2 != null){
                comp = geneId1.compareTo(geneId2);
                if (comp != 0){
                    return comp;
                }
            }
            else if (geneId1 != null){
                return BEFORE;
            }
            else if (geneId2 != null){
                return AFTER;
            }

            // compares refseq identifier
            String refseq1 = gene1.getRefseq();
            String refseq2 = gene2.getRefseq();

            if (refseq1 != null && refseq2 != null){
                return refseq1.compareTo(refseq2);
            }
            else if (refseq1 != null){
                return BEFORE;
            }
            else if (refseq2 != null){
                return AFTER;
            }

            return comp;
        }
    }

    /**
     * Use UnambiguousGeneComparator to know if two genes are equals.
     * @param gene1
     * @param gene2
     * @return true if the two genes are equal
     */
    public static boolean areEquals(Gene gene1, Gene gene2){
        if (unambiguousGeneComparator == null){
            unambiguousGeneComparator = new UnambiguousGeneComparator();
        }

        return unambiguousGeneComparator.compare(gene1, gene2) == 0;
    }
}
