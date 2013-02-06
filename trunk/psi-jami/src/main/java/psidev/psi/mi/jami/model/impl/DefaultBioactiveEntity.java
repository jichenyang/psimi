package psidev.psi.mi.jami.model.impl;

import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.ChecksumUtils;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.utils.comparator.ComparatorUtils;
import psidev.psi.mi.jami.utils.comparator.interactor.UnambiguousExactBioactiveEntityComparator;
import psidev.psi.mi.jami.utils.factory.CvTermFactory;

import java.util.*;

/**
 * Default implementation for bioactive entity
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>24/01/13</pre>
 */

public class DefaultBioactiveEntity extends DefaultInteractor implements BioactiveEntity {

    private ExternalIdentifier chebi;
    private Checksum smile;
    private Checksum standardInchi;
    private Checksum standardInchiKey;

    public DefaultBioactiveEntity(String name, CvTerm type) {
        super(name, type);
    }

    public DefaultBioactiveEntity(String name, String fullName, CvTerm type) {
        super(name, fullName, type);
    }

    public DefaultBioactiveEntity(String name, CvTerm type, Organism organism) {
        super(name, type, organism);
    }

    public DefaultBioactiveEntity(String name, String fullName, CvTerm type, Organism organism) {
        super(name, fullName, type, organism);
    }

    public DefaultBioactiveEntity(String name, CvTerm type, ExternalIdentifier uniqueId) {
        super(name, type, uniqueId);
    }

    public DefaultBioactiveEntity(String name, String fullName, CvTerm type, ExternalIdentifier uniqueId) {
        super(name, fullName, type, uniqueId);
    }

    public DefaultBioactiveEntity(String name, CvTerm type, Organism organism, ExternalIdentifier uniqueId) {
        super(name, type, organism, uniqueId);
    }

    public DefaultBioactiveEntity(String name, String fullName, CvTerm type, Organism organism, ExternalIdentifier uniqueId) {
        super(name, fullName, type, organism, uniqueId);
    }

    @Override
    protected void initializeIdentifiers() {
        this.identifiers = new BioctiveEntityIdentifierList();
    }

    @Override
    protected void initializeChecksums() {
        this.checksums = new BioctiveEntityChecksumList();
    }

    public String getChebi() {
        return chebi != null ? chebi.getId() : null;
    }

    public void setChebi(String id) {
        // add new chebi if not null
        if (id != null){
            CvTerm chebiDatabase = CvTermFactory.createChebiDatabase();
            // first remove old chebi if not null
            if (this.chebi != null){
                identifiers.remove(this.chebi);
            }
            this.chebi = new DefaultExternalIdentifier(chebiDatabase, id);
            this.identifiers.add(this.chebi);
        }
        // remove all chebi if the collection is not empty
        else if (!this.identifiers.isEmpty()) {
            ((BioctiveEntityIdentifierList) identifiers).removeAllChebiIdentifiers();
        }
    }

    public String getSmile() {
        return smile != null ? smile.getValue() : null;
    }

    public void setSmile(String smile) {
        if (smile != null){
            CvTerm smileMethod = CvTermFactory.createSmile();
            // first remove old smile
            if (this.smile != null){
                this.checksums.remove(this.smile);
            }
            this.smile = new DefaultChecksum(smileMethod, smile);
            this.checksums.add(this.smile);
        }
        // remove all smiles if the collection is not empty
        else if (!this.checksums.isEmpty()) {
            ((BioctiveEntityChecksumList) checksums).removeAllSmiles();
        }
    }

    public String getStandardInchiKey() {
        return standardInchiKey != null ? standardInchiKey.getValue() : null;
    }

    public void setStandardInchiKey(String key) {
        if (standardInchiKey != null){
            CvTerm inchiKeyMethod = CvTermFactory.createStandardInchiKey();
            // first remove old standard inchi key
            if (this.standardInchiKey != null){
                this.checksums.remove(this.standardInchiKey);
            }
            this.standardInchiKey = new DefaultChecksum(inchiKeyMethod, key);
            this.checksums.add(this.standardInchiKey);
        }
        // remove all standard inchi keys if the collection is not empty
        else if (!this.checksums.isEmpty()) {
            ((BioctiveEntityChecksumList) checksums).removeAllStandardInchiKeys();
        }
    }

    public String getStandardInchi() {
        return standardInchi != null ? standardInchi.getValue() : null;
    }

    public void setStandardInchi(String inchi) {
        if (standardInchi != null){
            CvTerm inchiMethod = CvTermFactory.createStandardInchi();
            // first remove standard inchi
            if (this.standardInchi != null){
                this.checksums.remove(this.standardInchi);
            }
            this.standardInchi = new DefaultChecksum(inchiMethod, inchi);
            this.checksums.add(this.standardInchi);
        }
        // remove all standard inchi if the collection is not empty
        else if (!this.checksums.isEmpty()) {
            ((BioctiveEntityChecksumList) checksums).removeAllStandardInchi();
        }
    }

    @Override
    public String toString() {
        return chebi != null ? chebi.getId() : (standardInchiKey != null ? standardInchiKey.getValue() : (smile != null ? smile.getValue() : (standardInchi != null ? standardInchi.getValue() : super.toString())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (!(o instanceof BioactiveEntity)){
            return false;
        }

        // use UnambiguousExactBioactiveEntity comparator for equals
        return UnambiguousExactBioactiveEntityComparator.areEquals(this, (BioactiveEntity) o);
    }

    /**
     * Comparator which sorts external identifiers so chebi identifiers are always first
     */
    private class BioactiveEntityIdentifierComparator implements Comparator<ExternalIdentifier>{

        public int compare(ExternalIdentifier externalIdentifier1, ExternalIdentifier externalIdentifier2) {
            int EQUAL = 0;
            int BEFORE = -1;
            int AFTER = 1;

            if (externalIdentifier1 == null && externalIdentifier2 == null){
                return EQUAL;
            }
            else if (externalIdentifier1 == null){
                return AFTER;
            }
            else if (externalIdentifier2 == null){
                return BEFORE;
            }
            else {
                // compares databases first : chebi is before
                CvTerm database1 = externalIdentifier1.getDatabase();
                CvTerm database2 = externalIdentifier2.getDatabase();
                String databaseId1 = database1.getMIIdentifier();
                String databaseId2 = database2.getMIIdentifier();

                // if external id of database is set, look at database id only otherwise look at shortname
                int comp;
                if (databaseId1 != null && databaseId2 != null){
                    // both are chebi, sort by id
                    if (Xref.CHEBI_ID.equals(databaseId1) && Xref.CHEBI_ID.equals(databaseId2)){
                        return ComparatorUtils.compareIdentifiersWithDefaultIdentifier(externalIdentifier1.getId(), externalIdentifier2.getId(), chebi != null ? chebi.getId() : null);
                    }
                    // CHEBI is first
                    else if (Xref.CHEBI_ID.equals(databaseId1)){
                        return BEFORE;
                    }
                    else if (Xref.CHEBI_ID.equals(databaseId2)){
                        return AFTER;
                    }
                    // both databases are not chebi
                    else {
                        comp = databaseId1.compareTo(databaseId2);
                    }
                }
                else {
                    String databaseName1 = database1.getShortName().toLowerCase().trim();
                    String databaseName2 = database2.getShortName().toLowerCase().trim();
                    // both are chebi, sort by id
                    if (Xref.CHEBI.equals(databaseName1) && Xref.CHEBI.equals(databaseName2)){
                        return ComparatorUtils.compareIdentifiersWithDefaultIdentifier(externalIdentifier1.getId(), externalIdentifier2.getId(), chebi != null ? chebi.getId() : null);
                    }
                    // CHEBI is first
                    else if (Xref.CHEBI.equals(databaseName1)){
                        return BEFORE;
                    }
                    else if (Xref.CHEBI.equals(databaseName2)){
                        return AFTER;
                    }
                    // both databases are not chebi
                    else {
                        comp = databaseName1.compareTo(databaseName2);
                    }
                }

                if (comp != 0){
                    return comp;
                }
                // check identifiers which cannot be null
                String id1 = externalIdentifier1.getId();
                String id2 = externalIdentifier2.getId();

                return id1.compareTo(id2);
            }
        }
    }

    private class BioctiveEntityIdentifierList extends TreeSet<ExternalIdentifier>{
        public BioctiveEntityIdentifierList(){
            super(new BioactiveEntityIdentifierComparator());
        }

        @Override
        public boolean add(ExternalIdentifier externalIdentifier) {
            boolean added = super.add(externalIdentifier);

            // set chebi if not done
            if (chebi == null && added){
                ExternalIdentifier firstChebi = first();

                if (XrefUtils.isXrefFromDatabase(firstChebi, Xref.CHEBI_ID, Xref.CHEBI)){
                    chebi = firstChebi;
                }
            }

            return added;
        }

        @Override
        public boolean remove(Object o) {
            if (super.remove(o)){
                // check chebi
                ExternalIdentifier firstChebi = first();

                // first identifier is from chebi
                if (XrefUtils.isXrefFromDatabase(firstChebi, Xref.CHEBI_ID, Xref.CHEBI)){
                    chebi = firstChebi;
                }
                // the first element is not chebi, the old chebi needs to be reset
                else if (chebi != null){
                    chebi = null;
                }
                return true;
            }

            return false;
        }

        @Override
        public void clear() {
            super.clear();
            chebi = null;
        }

        public void removeAllChebiIdentifiers(){

            ExternalIdentifier first = first();
            while (XrefUtils.isXrefFromDatabase(first, Xref.CHEBI_ID, Xref.CHEBI)){
                remove(first);
                first = first();
            }
        }
    }

    /**
     * Comparator which sorts checksums so standard inchi keys are always first, then smile then standard inchi
     */
    private class BioactiveEntityChecksumComparator implements Comparator<Checksum>{

        public int compare(Checksum checksum1, Checksum checksum2) {
            int EQUAL = 0;
            int BEFORE = -1;
            int AFTER = 1;

            if (checksum1 == null && checksum2 == null){
                return EQUAL;
            }
            else if (checksum1 == null){
                return AFTER;
            }
            else if (checksum2 == null){
                return BEFORE;
            }
            else {
                // compares methods first
                CvTerm method1 = checksum1.getMethod();
                CvTerm method2 = checksum2.getMethod();
                String methodId1 = method1.getMIIdentifier();
                String methodId2 = method2.getMIIdentifier();

                // if external id of method is set, look at method id only otherwise look at shortname
                int comp;
                if (methodId1 != null && methodId2 != null){
                    // both are standard inchi keys, sort by id
                    if (Checksum.INCHI_KEY_ID.equals(methodId1) && Checksum.INCHI_KEY_ID.equals(methodId2)){
                        return ComparatorUtils.compareIdentifiersWithDefaultIdentifier(checksum1.getValue(), checksum2.getValue(), standardInchiKey != null ? standardInchiKey.getValue() : null);
                    }
                    // standard inchi key is first
                    else if (Checksum.INCHI_KEY_ID.equals(methodId1)){
                        return BEFORE;
                    }
                    else if (Checksum.INCHI_KEY_ID.equals(methodId2)){
                        return AFTER;
                    }
                    else if (Checksum.SMILE_ID.equals(methodId1) && Checksum.SMILE_ID.equals(methodId2)){
                        return ComparatorUtils.compareIdentifiersWithDefaultIdentifier(checksum1.getValue(), checksum2.getValue(), smile != null ? smile.getValue() : null);
                    }
                    // smile is second
                    else if (Checksum.SMILE_ID.equals(methodId1)){
                        return BEFORE;
                    }
                    else if (Checksum.SMILE_ID.equals(methodId2)){
                        return AFTER;
                    }
                    else if (Checksum.INCHI_ID.equals(methodId1) && Checksum.INCHI_ID.equals(methodId2)){
                        return ComparatorUtils.compareIdentifiersWithDefaultIdentifier(checksum1.getValue(), checksum2.getValue(), standardInchi != null ? standardInchi.getValue() : null);
                    }
                    // standard inchi is third
                    else if (Checksum.INCHI_ID.equals(methodId1)){
                        return BEFORE;
                    }
                    else if (Checksum.INCHI_ID.equals(methodId2)){
                        return AFTER;
                    }
                    // both databases are not standard checksums
                    else {
                        comp = methodId1.compareTo(methodId2);
                    }
                }
                else {
                    String methodName1 = method1.getShortName().toLowerCase().trim();
                    String methodName2 = method2.getShortName().toLowerCase().trim();
                    // both are standard inchi key, sort by id
                    if (Checksum.INCHI_KEY.equals(methodName1) && Checksum.INCHI_KEY.equals(methodName2)){
                        return ComparatorUtils.compareIdentifiersWithDefaultIdentifier(checksum1.getValue(), checksum2.getValue(), standardInchiKey != null ? standardInchiKey.getValue() : null);
                    }
                    // standard inchi key is first
                    else if (Checksum.INCHI_KEY.equals(methodName1)){
                        return BEFORE;
                    }
                    else if (Checksum.INCHI_KEY.equals(methodName2)){
                        return AFTER;
                    }
                    else if (Checksum.SMILE.equals(methodName1) && Checksum.SMILE.equals(methodName2)){
                        return ComparatorUtils.compareIdentifiersWithDefaultIdentifier(checksum1.getValue(), checksum2.getValue(), smile != null ? smile.getValue() : null);
                    }
                    // smile is second
                    else if (Checksum.SMILE.equals(methodName1)){
                        return BEFORE;
                    }
                    else if (Checksum.SMILE.equals(methodName2)){
                        return AFTER;
                    }
                    else if (Checksum.INCHI.equals(methodName1) && Checksum.INCHI.equals(methodName2)){
                        return ComparatorUtils.compareIdentifiersWithDefaultIdentifier(checksum1.getValue(), checksum2.getValue(), standardInchi != null ? standardInchi.getValue() : null);
                    }
                    // smile is second
                    else if (Checksum.INCHI.equals(methodName1)){
                        return BEFORE;
                    }
                    else if (Checksum.INCHI.equals(methodName2)){
                        return AFTER;
                    }
                    // both databases are not standard checksum
                    else {
                        comp = methodName1.compareTo(methodName2);
                    }
                }

                if (comp != 0){
                    return comp;
                }
                // check values which cannot be null
                String id1 = checksum1.getValue();
                String id2 = checksum2.getValue();

                return id1.compareTo(id2);
            }
        }
    }

    private class BioctiveEntityChecksumList extends TreeSet<Checksum>{
        public BioctiveEntityChecksumList(){
            super(new BioactiveEntityChecksumComparator());
        }

        @Override
        public boolean add(Checksum checksum) {
            boolean added = super.add(checksum);

            // set standard inchi key, smile or standard inchi if not done
            if (added && (standardInchiKey == null || smile == null || standardInchi == null)){
                Iterator<Checksum> checksumIterator = iterator();
                Checksum firstChecksum = checksumIterator.next();

                // starts with standard inchi key
                if (standardInchiKey == null){
                    if (ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.INCHI_KEY_ID, Checksum.INCHI_KEY)){
                        standardInchiKey = firstChecksum;
                        if (checksumIterator.hasNext()){
                            firstChecksum = checksumIterator.next();
                        }
                        else {
                            firstChecksum = null;
                        }
                    }
                }

                // process smile
                if (smile == null){
                    // go through all inchi keys before finding smiles
                    while (checksumIterator.hasNext() &&
                            ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.INCHI_KEY_ID, Checksum.INCHI_KEY)){
                        firstChecksum = checksumIterator.next();
                    }

                    if (ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.SMILE_ID, Checksum.SMILE)){
                        smile = firstChecksum;
                        if (checksumIterator.hasNext()){
                            firstChecksum = checksumIterator.next();
                        }
                        else {
                            firstChecksum = null;
                        }
                    }
                }

                // process standard inchi
                if (standardInchi == null){
                    // go through all smiles before finding standard inchi
                    while (checksumIterator.hasNext() &&
                            ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.SMILE_ID, Checksum.SMILE)){
                        firstChecksum = checksumIterator.next();
                    }

                    if (ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.INCHI_ID, Checksum.INCHI)){
                        standardInchi = firstChecksum;
                    }
                }
            }

            return added;
        }

        @Override
        public boolean remove(Object o) {
            if (super.remove(o)){
                // we have nothing left in checksums, reset standard values
                if (isEmpty()){
                    standardInchi = null;
                    standardInchiKey = null;
                    smile = null;
                }
                else {

                    Iterator<Checksum> checksumIterator = iterator();
                    Checksum firstChecksum = checksumIterator.next();

                    // first checksum is standard inchi key
                    if (ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.INCHI_KEY_ID, Checksum.INCHI_KEY)){
                        standardInchiKey = firstChecksum;
                    }
                    // process smile
                    // go through all inchi keys before finding smiles
                    while (checksumIterator.hasNext() &&
                            ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.INCHI_KEY_ID, Checksum.INCHI_KEY)){
                        firstChecksum = checksumIterator.next();
                    }

                    if (ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.SMILE_ID, Checksum.SMILE)){
                        smile = firstChecksum;
                    }

                    // process standard inchi
                    // go through all inchi keys before finding smiles
                    while (checksumIterator.hasNext() &&
                            ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.SMILE_ID, Checksum.SMILE)){
                        firstChecksum = checksumIterator.next();
                    }

                    if (ChecksumUtils.doesChecksumHaveMethod(firstChecksum, Checksum.INCHI_ID, Checksum.INCHI)){
                        standardInchi = firstChecksum;
                    }
                }

                return true;
            }
            return false;
        }

        @Override
        public void clear() {
            super.clear();
            standardInchiKey = null;
            standardInchi = null;
            smile = null;
        }

        public void removeAllStandardInchiKeys(){

            Checksum first = first();
            while (ChecksumUtils.doesChecksumHaveMethod(first, Checksum.INCHI_KEY_ID, Checksum.INCHI_KEY)){
                remove(first);
                first = first();
            }
        }

        public void removeAllSmiles(){

            if (!isEmpty()){
                Iterator<Checksum> checksumIterator = iterator();
                Checksum first = checksumIterator.next();
                // skip the standard inchi keys
                while (checksumIterator.hasNext() && ChecksumUtils.doesChecksumHaveMethod(first, Checksum.INCHI_KEY_ID, Checksum.INCHI_KEY)){
                    first = checksumIterator.next();
                }

                while (checksumIterator.hasNext() && ChecksumUtils.doesChecksumHaveMethod(first, Checksum.SMILE_ID, Checksum.SMILE)){
                    checksumIterator.remove();
                    first = checksumIterator.next();
                }
            }
        }

        public void removeAllStandardInchi(){

            if (!isEmpty()){
                Iterator<Checksum> checksumIterator = iterator();
                Checksum first = checksumIterator.next();
                // skip the standard inchi keys and smiles
                while (checksumIterator.hasNext() &&
                        ( ChecksumUtils.doesChecksumHaveMethod(first, Checksum.INCHI_KEY_ID, Checksum.INCHI_KEY) ||
                                ChecksumUtils.doesChecksumHaveMethod(first, Checksum.SMILE_ID, Checksum.SMILE) )){
                    first = checksumIterator.next();
                }

                while (checksumIterator.hasNext() && ChecksumUtils.doesChecksumHaveMethod(first, Checksum.INCHI_ID, Checksum.INCHI)){
                    checksumIterator.remove();
                    first = checksumIterator.next();
                }
            }
        }
    }
}
