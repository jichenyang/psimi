package psidev.psi.mi.jami.utils.factory;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.model.impl.DefaultCvTerm;
import psidev.psi.mi.jami.model.impl.DefaultExternalIdentifier;

/**
 * Factory for creating CvTerms
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>24/01/13</pre>
 */

public class CvTermFactory {

    private static CvTerm psimi;
    private static CvTerm chebiDatabase;

    public static CvTerm createPsiMiDatabaseNameOnly(){
        return new DefaultCvTerm(CvTerm.PSI_MI);
    }

    public static CvTerm createPsiMiDatabase(){
        return new DefaultCvTerm(CvTerm.PSI_MI, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), CvTerm.PSI_MI_ID));
    }

    public static CvTerm createChebiDatabase(){
        return new DefaultCvTerm(Xref.CHEBI, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.CHEBI_ID));
    }

    public static CvTerm createEnsemblDatabase(){
        return new DefaultCvTerm(Xref.ENSEMBL, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.ENSEMBL_ID));
    }

    public static CvTerm createEnsemblGenomesDatabase(){
        return new DefaultCvTerm(Xref.ENSEMBL_GENOMES, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.ENSEMBL_GENOMES_ID));
    }

    public static CvTerm createEntrezGeneIdDatabase(){
        return new DefaultCvTerm(Xref.ENTREZ_GENE, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.ENTREZ_GENE_ID));
    }

    public static CvTerm createRefseqDatabase(){
        return new DefaultCvTerm(Xref.REFSEQ, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.REFSEQ_ID));
    }

    public static CvTerm createDdbjEmblGenbankDatabase(){
        return new DefaultCvTerm(Xref.DDBJ_EMBL_GENBANK, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.DDBJ_EMBL_GENBANK_ID));
    }

    public static CvTerm createUniprotkbDatabase(){
        return new DefaultCvTerm(Xref.UNIPROTKB, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.UNIPROTKB_ID));
    }

    public static CvTerm createSmile(){
        return new DefaultCvTerm(Checksum.SMILE, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Checksum.SMILE_ID));
    }

    public static CvTerm createStandardInchi(){
        return new DefaultCvTerm(Checksum.INCHI, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Checksum.INCHI_ID));
    }

    public static CvTerm createStandardInchiKey(){
        return new DefaultCvTerm(Checksum.INCHI_KEY, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Checksum.INCHI_KEY_ID));
    }

    public static CvTerm createRogid(){
        return new DefaultCvTerm(Checksum.ROGID, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Checksum.ROGID_ID));
    }

    public static CvTerm createUndeterminedStatus(){
        return new DefaultCvTerm(Position.UNDETERMINED, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Position.UNDETERMINED_MI));
    }

    public static CvTerm createNTerminalRangeStatus(){
        return new DefaultCvTerm(Position.N_TERMINAL_RANGE, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Position.N_TERMINAL_RANGE_MI));
    }

    public static CvTerm createCTerminalRangeStatus(){
        return new DefaultCvTerm(Position.C_TERMINAL_RANGE, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Position.C_TERMINAL_RANGE_MI));
    }

    public static CvTerm createNTerminalStatus(){
        return new DefaultCvTerm(Position.N_TERMINAL, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Position.N_TERMINAL_MI));
    }

    public static CvTerm createRaggedNTerminalStatus(){
        return new DefaultCvTerm(Position.RAGGED_N_TERMINAL, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Position.RAGGED_N_TERMINAL_MI));
    }

    public static CvTerm createGeneInteractorType(){
        return new DefaultCvTerm(Gene.GENE, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Gene.GENE_ID));
    }

    public static CvTerm createGeneNameAliasType(){
        return new DefaultCvTerm(Alias.GENE_NAME, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Alias.GENE_NAME_ID));
    }

    public static CvTerm createUnspecifiedRole(){
        return new DefaultCvTerm(CvTerm.UNSPECIFIED_ROLE, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), CvTerm.UNSPECIFIED_ROLE_ID));
    }

    public static CvTerm createComplexPhysicalProperties(){
        return new DefaultCvTerm(Annotation.COMPLEX_PROPERTIES, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Annotation.COMPLEX_PROPERTIES_ID));
    }

    public static CvTerm createImexDatabase(){
        return new DefaultCvTerm(Xref.IMEX, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.IMEX_ID));
    }

    public static CvTerm createImexPrimaryQualifier(){
        return new DefaultCvTerm(Xref.IMEX_PRIMARY, new DefaultExternalIdentifier(createPsiMiDatabaseNameOnly(), Xref.IMEX_PRIMARY_ID));
    }
}
