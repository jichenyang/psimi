package psidev.psi.mi.validator.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.validator.ValidatorReport;
import psidev.psi.mi.validator.extension.rules.DatabaseAccessionRule;
import psidev.psi.mi.validator.extension.rules.PsimiXmlSchemaRule;
import psidev.psi.mi.validator.extension.rules.cvmapping.MICvRuleManager;
import psidev.psi.mi.xml.PsimiXmlLightweightReader;
import psidev.psi.mi.xml.PsimiXmlReaderException;
import psidev.psi.mi.xml.model.*;
import psidev.psi.mi.xml.xmlindex.IndexedEntry;
import psidev.psi.tools.cvrReader.mapping.jaxb.CvMapping;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.ontology_manager.OntologyManagerContext;
import psidev.psi.tools.ontology_manager.impl.local.OntologyLoaderException;
import psidev.psi.tools.validator.*;
import psidev.psi.tools.validator.preferences.UserPreferences;
import psidev.psi.tools.validator.rules.Rule;
import psidev.psi.tools.validator.rules.codedrule.ObjectRule;
import psidev.psi.tools.validator.schema.SaxMessage;
import psidev.psi.tools.validator.schema.SaxReport;
import psidev.psi.tools.validator.schema.SaxValidatorHandler;

import java.io.*;
import java.util.*;

/**
 * <b> PSI-MI 2.5.2 Specific Validator </b>.
 *
 * @author Samuel Kerrien (skerrien@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class Mi25Validator extends Validator {

    /**
     * Sets up a logger for that class.
     */
    public static final Log log = LogFactory.getLog( Mi25Validator.class );

    ///**
    //* Plateform specific break line.
    //*/
    //public static final String NEW_LINE = System.getProperty( "line.separator" );

    /**
     * counter used for creating unique IDs.
     * <p/>
     * These are used to create temp files.
     */
    private long uniqId = 0;

    private UserPreferences userPreferences;

    private ValidatorReport validatorReport;

    /**
     * Creates a MI 25 validator with the default user userPreferences.
     *
     * @param ontologyconfig   configuration for the ontologies.
     * @param cvMappingConfig  configuration for the cv mapping rules.
     * @param objectRuleConfig configuration for the object rules.
     * @throws ValidatorException
     */
    public Mi25Validator( InputStream ontologyconfig,
                          InputStream cvMappingConfig,
                          InputStream objectRuleConfig ) throws ValidatorException, OntologyLoaderException {

        super( ontologyconfig, cvMappingConfig, objectRuleConfig );
        validatorReport = new ValidatorReport();
    }

    public Mi25Validator( OntologyManager ontologyManager,
                          CvMapping cvMapping,
                          Collection<ObjectRule> objectRules) {
        super( ontologyManager, cvMapping, objectRules);
        validatorReport = new ValidatorReport();
    }

    ///////////////////////////
    // Getters and Setters

    public void setUserPreferences( UserPreferences userPreferences ) {
        this.userPreferences = userPreferences;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    @Override
    protected void instantiateCvRuleManager(OntologyManager manager, CvMapping cvMappingRules) {
        super.setCvRuleManager(new MICvRuleManager(manager, cvMappingRules));
    }

    //////////////////////////
    // Utility

    /**
     * Return a unique ID.
     *
     * @return a unique id.
     */
    synchronized private long getUniqueId() {
        return ++uniqId;
    }

    /**
     * Store the content of the given input stream into a temporary file and return its descriptor.
     *
     * @param is the input stream to store.
     * @return a File descriptor describing a temporary file storing the content of the given input stream.
     * @throws IOException if an IO error occur.
     */
    private File storeAsTemporaryFile( InputStream is ) throws IOException {

        if ( is == null ) {
            throw new IllegalArgumentException( "You must give a non null InputStream" );
        }
        // Create a temp file and write URL content in it.
        File tempDirectory = new File( System.getProperty( "java.io.tmpdir", "tmp" ) );
        if ( !tempDirectory.exists() ) {
            if ( !tempDirectory.mkdirs() ) {
                throw new IOException( "Cannot create temp directory: " + tempDirectory.getAbsolutePath() );
            }
        }

        long id = getUniqueId();

        File tempFile = File.createTempFile( "validator." + id, ".xml", tempDirectory );

        BufferedReader in = new BufferedReader( new InputStreamReader( is ) );
        try{

            log.info( "The file is temporary store as: " + tempFile.getAbsolutePath() );

            BufferedWriter out = new BufferedWriter( new FileWriter( tempFile ) );

            try{
                String line;
                while ( ( line = in.readLine() ) != null ) {
                    out.write( line );
                }


                out.flush();
            }
            finally {
                out.close();
            }
        }
        finally {
            in.close();
        }

        return tempFile;
    }

    /////////////////////////////
    // Validator

    /**
     * Validate a file and use the user preferences to decide if run semantic and syntax validation or not
     * @param file
     * @return
     * @throws ValidatorException
     */
    public ValidatorReport validate( File file ) throws ValidatorException {
        this.validatorReport.clear();

        try {

            // 1. Validate XML input using Schema (MIF)
            if ( userPreferences.isSaxValidationEnabled() ) {
                if (false == validateSyntax(file)) {
                    return this.validatorReport;
                }
            }

            validateSemantic(file);

            return this.validatorReport;

        } catch ( Exception e ) {
            throw new ValidatorException( "Unable to process input file:" + file.getAbsolutePath(), e );
        }
        finally {
            clearThreadLocals();
        }
    }

    /**
     * Validate the syntax of a file.
     * @param file
     * @return false if syntax not valid
     */
    public boolean validateSyntax(File file){
        this.validatorReport.getSyntaxMessages().clear();

        return runSyntaxValidation(this.validatorReport, file);
    }

    /**
     * validate the syntax of an inputStream
     * @param streamToValidate
     * @return false if not valid
     */
    public boolean validateSyntax(InputStream streamToValidate){
        this.validatorReport.getSyntaxMessages().clear();

        return runSyntaxValidation(this.validatorReport, streamToValidate);
    }

    /**
     * validate the semantic of a file and write the results in the report of this validator
     * @param file
     * @throws ValidatorException
     * @throws PsimiXmlReaderException
     */
    public void validateSemantic(File file) throws ValidatorException, PsimiXmlReaderException {
        this.validatorReport.getSemanticMessages().clear();
        this.validatorReport.setInteractionCount(0);

        runSemanticValidation(this.validatorReport, file);
    }

    /**
     * validate the semantic of an inputstream and write the results in the report of this validator
     * @param streamToValidate
     * @throws ValidatorException
     * @throws PsimiXmlReaderException
     */
    public void validateSemantic(InputStream streamToValidate) throws ValidatorException, PsimiXmlReaderException {
        this.validatorReport.getSyntaxMessages().clear();
        this.validatorReport.setInteractionCount(0);

        runSemanticValidation(this.validatorReport, streamToValidate);
    }

    /**
     * Validates a PSI-MI 2.5 InputStream.
     *
     * @param is the input stream to validate.
     * @return a collection of messages
     * @throws ValidatorException
     */
    public ValidatorReport validate( InputStream is ) throws ValidatorException {

        this.validatorReport.clear();

        try {
            File tempFile = storeAsTemporaryFile( is );

            // close the original inputStream as it has been successfully read
            is.close();


            // 1. Validate XML input using Schema (MIF)
            if ( userPreferences.isSaxValidationEnabled() ) {
                if (false == validateSyntax(tempFile)) {
                    return this.validatorReport;
                }
            }

            validateSemantic(tempFile);

            tempFile.delete();

            return this.validatorReport;

        } catch ( Exception e ) {
            throw new ValidatorException( "Unable to process input stream", e );
        } finally{
            clearThreadLocals();
        }
    }

    /**
     * Validates a PSI-MI 2.5 EntrySet.
     *
     * @param es the entrySet to validate.
     * @return a collection of messages
     * @throws ValidatorException
     */
    public ValidatorReport validate( EntrySet es ) throws ValidatorException {

        this.validatorReport.clear();

        try{
            for ( Entry entry : es.getEntries() ) {
                boolean hasExperimentList = false;
                boolean hasInteractorList = false;

                if (entry.hasExperiments()){
                    hasExperimentList = true;
                    for ( ExperimentDescription experiment : entry.getExperiments() ) {

                        // cv mapping
                        Collection<ValidatorMessage> messages = checkCvMapping( experiment, "/experiment/" );

                        if( ! messages.isEmpty() ){
                            checkExperiment(messages, experiment);
                        }
                        else{
                            checkExperiment(messages, experiment);
                        }

                        this.validatorReport.getSemanticMessages().addAll(messages);
                    }
                }

                if (entry.hasInteractors()){
                    hasInteractorList = true;
                    for ( Interactor interactor : entry.getInteractors() ) {
                        // cv mapping
                        Collection<ValidatorMessage> messages = checkCvMapping( interactor, "/interactor/" );

                        if( ! messages.isEmpty() ){
                            checkInteractor(messages, interactor);
                        }
                        else{
                            checkInteractor(messages, interactor);
                        }

                        this.validatorReport.getSemanticMessages().addAll(messages);
                    }
                }

                for ( Interaction interaction : entry.getInteractions() ) {

                    // cv mapping
                    Collection<ValidatorMessage> messages = checkCvMapping( interaction, "/interactionEvidence/" );
                    if( ! messages.isEmpty() )
                        messages = convertToMi25Messages( messages, interaction );

                    // object rule
                    checkInteraction(messages, interaction, hasExperimentList, hasInteractorList);

                    this.validatorReport.getSemanticMessages().addAll(messages);
                }
            }

            // cluster all messages!!
            Collection<ValidatorMessage> clusteredValidatorMessages = clusterByMessagesAndRules(this.validatorReport.getSemanticMessages());
            this.validatorReport.setSemanticMessages(clusteredValidatorMessages);
        }
        finally{
            clearThreadLocals();
        }

        return this.validatorReport;
    }

    //////////////////////////////
    // Private

    /**
     * Runs the semantic validation and append messages to the given report.
     *
     * @param report the report to be completed.
     * @param nis the data stream to be validated.
     * @throws PsimiXmlReaderException
     * @throws ValidatorException
     */
    private void runSemanticValidation(ValidatorReport report, InputStream nis) throws PsimiXmlReaderException, ValidatorException {

        // Build the collection of messages in which we will accumulate the output of the validator
        Collection<ValidatorMessage> messages = report.getSemanticMessages();

        List<IndexedEntry> entries = null;

        try {
            // then run the object rules (if any)
            final Set<ObjectRule> rules = getObjectRules();
            if ( (rules != null && !rules.isEmpty()) || getCvRuleManager() != null) {
                if( entries == null ) {
                    PsimiXmlLightweightReader psiReader = new PsimiXmlLightweightReader( nis );
                    entries = psiReader.getIndexedEntries();
                }

                processSemanticValidation(report, messages, entries);
            }

        } catch(Exception e){
            PsimiXmlSchemaRule schemaRule = new PsimiXmlSchemaRule(this.ontologyMngr);

            StringBuffer messageBuffer = schemaRule.createMessageFromException(e);
            messageBuffer.append("\n The validator reported at least " + messages.size() + " messages but the error in the xml file need " +
                    "to be fixed before finishing the validation of the file.");

            Mi25ClusteredContext context = new Mi25ClusteredContext();

            messages.clear();
            messages.add( new ValidatorMessage( messageBuffer.toString(),
                    MessageLevel.FATAL,
                    context,
                    schemaRule ) );
        }
        finally {
            clearThreadLocals();
        }
    }

    /**
     * Runs the semantic validation and append messages to the given report.
     *
     * @param report the report to be completed.
     * @param file the file to be validated.
     * @throws PsimiXmlReaderException
     * @throws ValidatorException
     */
    private void runSemanticValidation(ValidatorReport report, File file) throws PsimiXmlReaderException, ValidatorException {

        // Build the collection of messages in which we will accumulate the output of the validator
        Collection<ValidatorMessage> messages = report.getSemanticMessages();

        List<IndexedEntry> entries = null;

        try {
            // then run the object rules (if any)
            final Set<ObjectRule> rules = getObjectRules();
            if ( (rules != null && !rules.isEmpty()) || getCvRuleManager() != null) {
                if( entries == null ) {
                    PsimiXmlLightweightReader psiReader = new PsimiXmlLightweightReader( file );
                    entries = psiReader.getIndexedEntries();
                }

                processSemanticValidation(report, messages, entries);
            }

        } catch(Exception e){
            PsimiXmlSchemaRule schemaRule = new PsimiXmlSchemaRule(this.ontologyMngr);

            StringBuffer messageBuffer = schemaRule.createMessageFromException(e);
            messageBuffer.append("\n The validator reported at least " + messages.size() + " messages but the error in the xml file need " +
                    "to be fixed before finishing the validation of the file.");

            Mi25Context context = new Mi25Context();

            messages.clear();
            messages.add( new ValidatorMessage( messageBuffer.toString(),
                    MessageLevel.FATAL,
                    context,
                    schemaRule ) );
        }
    }

    private void processSemanticValidation(ValidatorReport report, Collection<ValidatorMessage> messages, List<IndexedEntry> entries) throws PsimiXmlReaderException, ValidatorException {
        for ( IndexedEntry entry : entries ) {
            boolean hasExperimentList = false;
            boolean hasInteractorList = false;
            final Iterator<ExperimentDescription> experimentIterator = entry.unmarshallExperimentIterator();
            if (experimentIterator.hasNext()){
                hasExperimentList = true;

                while ( experimentIterator.hasNext() ) {
                    ExperimentDescription experiment = experimentIterator.next();

                    // check using cv mapping rules
                    Collection<ValidatorMessage> validatorMessages =
                            super.checkCvMapping( experiment, "/experiment/" );

                    if (validatorMessages != null){
                        validatorMessages = convertToMi25Messages( validatorMessages, experiment );

                        validatorMessages.addAll(checkExperiment(validatorMessages, experiment));
                    }
                    else {
                        validatorMessages = checkExperiment(validatorMessages, experiment);
                    }

                    if( !validatorMessages.isEmpty() ) {
                        updateLineNumber( validatorMessages, experiment.getLineNumber() );
                    }

                    // append messages to the global collection
                    messages.addAll( validatorMessages );
                }
            }

            // now process interactors
            final Iterator<Interactor> interactorIterator = entry.unmarshallInteractorIterator();

            if (interactorIterator.hasNext()){
                hasInteractorList = true;

                while ( interactorIterator.hasNext() ) {
                    Interactor interactor = interactorIterator.next();

                    // check using cv mapping rules
                    Collection<ValidatorMessage> validatorMessages =
                            super.checkCvMapping( interactor, "/interactor/" );

                    if (validatorMessages != null){
                        validatorMessages = convertToMi25Messages( validatorMessages, interactor );

                        validatorMessages.addAll(checkInteractor(validatorMessages, interactor));
                    }
                    else {
                        validatorMessages = checkInteractor(validatorMessages, interactor);
                    }

                    if( !validatorMessages.isEmpty() ) {
                        updateLineNumber( validatorMessages, interactor.getLineNumber() );
                    }

                    // append messages to the global collection
                    messages.addAll( validatorMessages );
                }
            }

            // now process interactions
            Iterator<Interaction> interactionIterator = entry.unmarshallInteractionIterator();
            while ( interactionIterator.hasNext() ) {
                Interaction interaction = interactionIterator.next();

                // check using cv mapping rules
                Collection<ValidatorMessage> interactionMessages =
                        super.checkCvMapping( interaction, "/interactionEvidence/" );

                if (interactionMessages != null){
                    interactionMessages = convertToMi25Messages( interactionMessages, interaction );

                    interactionMessages.addAll(checkInteraction(interactionMessages, interaction, hasExperimentList, hasInteractorList));
                }
                else {
                    interactionMessages = checkInteraction(interactionMessages, interaction, hasExperimentList, hasInteractorList);
                }

                // add line number
                if( !interactionMessages.isEmpty() ) {
                    long lineNumber = entry.getInteractionLineNumber( interaction.getId() );
                    updateLineNumber( interactionMessages, interaction.getLineNumber() );
                }

                // append messages to the global collection
                messages.addAll( interactionMessages );
            }
        }

        // cluster all messages!!
        Collection<ValidatorMessage> clusteredValidatorMessages = clusterByMessagesAndRules(report.getSemanticMessages());
        report.setSemanticMessages(clusteredValidatorMessages);
    }

    private Collection<ValidatorMessage> checkInteraction(Collection<ValidatorMessage> messages, Interaction interaction, boolean hasExperimentList, boolean hasInteractorList) throws ValidatorException {
        // run the interaction specialized rules
        Collection<ValidatorMessage> interactionMessages = super.validate( interaction );

        if (!hasExperimentList){
            for (ExperimentDescription experiment : interaction.getExperiments()){
                checkExperiment(interactionMessages, experiment);
            }
        }

        for (Confidence c : interaction.getConfidencesList()){
            checkConfidence(interactionMessages, c);
        }

        for (InteractionType it : interaction.getInteractionTypes()){
            // run the interaction type specialized rules
            interactionMessages.addAll(super.validate( it ));
        }

        for (Participant p : interaction.getParticipants()){
            checkParticipant(interactionMessages, p, hasInteractorList);

            for (Feature f : p.getFeatures()){
                checkFeature(interactionMessages, f);
            }
        }

        // append messages to the global collection
        if( ! interactionMessages.isEmpty() )
            messages.addAll( convertToMi25Messages( interactionMessages, interaction ) );
        return interactionMessages;
    }

    private Collection<ValidatorMessage> checkExperiment(Collection<ValidatorMessage> messages, ExperimentDescription experiment) throws ValidatorException {
        // run the experiment specialized rules
        Collection<ValidatorMessage> validatorMessages = super.validate( experiment );

        // run the bibref specialized rules
        validatorMessages.addAll(super.validate( experiment.getBibref() ));

        // run the feature detection method specialized rules
        validatorMessages.addAll(super.validate( experiment.getFeatureDetectionMethod() ));

        // run the interaction detection method specialized rules
        validatorMessages.addAll(super.validate( experiment.getInteractionDetectionMethod() ));

        // run the participant identification method specialized rules
        validatorMessages.addAll(super.validate( experiment.getParticipantIdentificationMethod() ));

        for (Confidence c : experiment.getConfidences()){
            checkConfidence(validatorMessages, c);
        }

        for (Organism o : experiment.getHostOrganisms()){
            checkOrganism(validatorMessages, o);
        }

        if( ! validatorMessages.isEmpty() )
            messages.addAll( convertToMi25Messages( validatorMessages, experiment ) );
        return validatorMessages;
    }

    private Collection<ValidatorMessage> checkInteractor(Collection<ValidatorMessage> messages, Interactor interactor) throws ValidatorException {
        // run the interactor specialized rules
        final Collection<ValidatorMessage> validatorMessages = super.validate( interactor );

        // run the interactor type specialized rules
        validatorMessages.addAll(super.validate( interactor.getInteractorType() ));

        if (interactor.getOrganism() != null){
            Organism o = interactor.getOrganism();

            checkOrganism(validatorMessages, o);
        }

        if( ! validatorMessages.isEmpty() )
            messages.addAll( convertToMi25Messages( validatorMessages, interactor ) );

        return validatorMessages;
    }

    private void checkParticipant(Collection<ValidatorMessage> validatorMessages, Participant p, boolean hasInteractorList) throws ValidatorException {
        // run the participant specialized rules
        validatorMessages.addAll(super.validate( p ));

        if (p.getInteractor() != null && !hasInteractorList){
            checkInteractor(validatorMessages, p.getInteractor());
        }

        // run the biological roles specialized rules
        validatorMessages.addAll(super.validate( p.getBiologicalRole() ));

        for (ParticipantIdentificationMethod m : p.getParticipantIdentificationMethods()){
            // run the participant identification specialized rules
            validatorMessages.addAll(super.validate( m ));
        }
        for (ExperimentalRole role : p.getExperimentalRoles()){
            // run the experimental role specialized rules
            validatorMessages.addAll(super.validate( role ));
        }
        for (Confidence c : p.getConfidenceList()){
            // run the confidence specialized rules
            validatorMessages.addAll(super.validate( c ));
        }

        for (ExperimentalPreparation ep : p.getParticipantExperimentalPreparations()){
            // run the experimental preparation specialized rules
            validatorMessages.addAll(super.validate( ep ));
        }

        for (ExperimentalInteractor ei : p.getExperimentalInteractors()){
            // run the experimental interactor specialized rules
            if (ei.getInteractor() != null && !hasInteractorList){
                checkInteractor(validatorMessages, ei.getInteractor());
            }
        }

        for (HostOrganism o : p.getHostOrganisms()){
            checkOrganism(validatorMessages, o.getOrganism());
        }
    }

    private void checkFeature(Collection<ValidatorMessage> validatorMessages, Feature f ) throws ValidatorException {
        // run the feature specialized rules
        validatorMessages.addAll(super.validate( f ));

        // run the feature detection method specialized rules
        validatorMessages.addAll(super.validate( f.getFeatureDetectionMethod() ));

        // run the feature type specialized rules
        validatorMessages.addAll(super.validate( f.getFeatureType() ));

        for (Range r : f.getFeatureRanges()){
            // run the start status specialized rules
            validatorMessages.addAll(super.validate( r.getStartStatus() ));

            // run the end status specialized rules
            validatorMessages.addAll(super.validate( r.getEndStatus() ));
        }
    }

    private void checkConfidence(Collection<ValidatorMessage> validatorMessages, Confidence c ) throws ValidatorException {
        if (c.getUnit() != null){
            // run the unit specialized rules
            validatorMessages.addAll(super.validate( c.getUnit() ));
        }
    }

    private void checkOrganism(Collection<ValidatorMessage> validatorMessages, Organism o ) throws ValidatorException {

        // run the organism specialized rules
        validatorMessages.addAll(super.validate( o ));

        if (o.getCellType() != null){
            // run the celltype specialized rules
            validatorMessages.addAll(super.validate( o.getCellType() ));
        }

        if (o.getTissue() != null){
            // run the tissue specialized rules
            validatorMessages.addAll(super.validate( o.getTissue() ));
        }

        if (o.getCompartment() != null){
            // run the compartment specialized rules
            validatorMessages.addAll(super.validate( o.getCompartment() ));
        }
    }

    /**
     * Runs the XML syntax validation and append messages to the report if any.
     *
     * @param report report in which errors should be reported.
     * @param nis stream of data to be validated.
     * @return true is the validation passed, false otherwise.
     */
    private boolean runSyntaxValidation(ValidatorReport report, InputStream nis) {

        if (log.isDebugEnabled()) log.debug("[SAX Validation] enabled via user preferences" );
        SaxReport saxReport = null;
        try {
            saxReport = SaxValidatorHandler.validate( nis );
        } catch (Exception e) {
            log.error("SAX error while running syntax validation", e);
            String msg = "An unexpected error occured while running the syntax validation";
            if( e.getMessage() != null ) {
                msg = msg + ": " + e.getMessage();
            }
            final ValidatorMessage message = new ValidatorMessage(msg, MessageLevel.FATAL );
            report.getSyntaxMessages().add(message);

            return false; // abort
        }

        return processSyntaxValidation(report, saxReport);
    }

    /**
     * Runs the XML syntax validation and append messages to the report if any.
     *
     * @param report report in which errors should be reported.
     * @param file file to be validated.
     * @return true is the validation passed, false otherwise.
     */
    private boolean runSyntaxValidation(ValidatorReport report, File file) {

        if (log.isDebugEnabled()) log.debug("[SAX Validation] enabled via user preferences" );
        SaxReport saxReport = null;
        try {
            saxReport = SaxValidatorHandler.validate( file );
        } catch (Exception e) {
            log.error("SAX error while running syntax validation", e);
            String msg = "An unexpected error occured while running the syntax validation";
            if( e.getMessage() != null ) {
                msg = msg + ": " + e.getMessage();
            }
            final ValidatorMessage message = new ValidatorMessage(msg, MessageLevel.FATAL );
            report.getSyntaxMessages().add(message);

            return false; // abort
        }

        return processSyntaxValidation(report, saxReport);
    }

    private boolean processSyntaxValidation(ValidatorReport report, SaxReport saxReport) {
        if ( !saxReport.isValid() ) {

            if (log.isDebugEnabled()) log.debug( "[SAX Validation] File not PSI 2.5 valid." );

            // show an error message
            Collection<SaxMessage> messages = saxReport.getMessages();
            if (log.isDebugEnabled()) {
                log.debug( messages.size() + " message" + ( messages.size() > 1 ? "s" : "" ) + "" );
            }
            Collection<ValidatorMessage> validatorMessages = new ArrayList<ValidatorMessage>( messages.size() );
            for ( SaxMessage saxMessage : messages ) {
                // convert SaxMessage into a ValidatorMessage
                ValidatorMessage vm = new ValidatorMessage( saxMessage, MessageLevel.FATAL );
                validatorMessages.add( vm );
            }

            report.getSyntaxMessages().addAll( validatorMessages );

            if (log.isDebugEnabled()) log.debug( "Abort semantic validation." );
            return false;
        } else {
            if (log.isDebugEnabled()) log.debug( "[SAX Validation] File is valid." );
        }

        return true;
    }

    private Collection<ValidatorMessage> convertToMi25Messages( Collection<ValidatorMessage> messages, Interaction interaction ) {
        Collection<ValidatorMessage> convertedMessages = new ArrayList<ValidatorMessage>( messages.size() );

        for ( ValidatorMessage message : messages ) {
            Mi25Context context = null;

            if (message.getContext() instanceof Mi25Context){
                context = (Mi25Context) message.getContext();
            }
            else {
                context = new Mi25Context();
            }

            context.setId( interaction.getId() );
            context.setObjectLabel("interaction");
            convertedMessages.add( new ValidatorMessage( message.getMessage(), message.getLevel(), context, message.getRule() ) );
        }

        return convertedMessages;
    }

    private Collection<ValidatorMessage> convertToMi25Messages( Collection<ValidatorMessage> messages, ExperimentDescription experiment ) {
        Collection<ValidatorMessage> convertedMessages = new ArrayList<ValidatorMessage>( messages.size() );

        for ( ValidatorMessage message : messages ) {
            Mi25Context context = null;

            if (message.getContext() instanceof Mi25Context){
                context = (Mi25Context) message.getContext();
            }
            else {
                context = new Mi25Context();
            }

            context.setId( experiment.getId() );
            context.setObjectLabel("experiment");
            convertedMessages.add( new ValidatorMessage( message.getMessage(), message.getLevel(), context, message.getRule() ) );
        }

        return convertedMessages;
    }

    private Collection<ValidatorMessage> convertToMi25Messages( Collection<ValidatorMessage> messages, Interactor interactor ) {
        Collection<ValidatorMessage> convertedMessages = new ArrayList<ValidatorMessage>( messages.size() );

        for ( ValidatorMessage message : messages ) {
            Mi25Context context = null;

            if (message.getContext() instanceof Mi25Context){
                context = (Mi25Context) message.getContext();
            }
            else {
                context = new Mi25Context();
            }

            context.setId( interactor.getId() );
            context.setObjectLabel("interactor");
            convertedMessages.add( new ValidatorMessage( message.getMessage(), message.getLevel(), context, message.getRule() ) );
        }

        return convertedMessages;
    }

    private void updateLineNumber( Collection<ValidatorMessage> validatorMessages, int lineNumber ) {
        if( lineNumber > 0 ) {
            for ( ValidatorMessage msg : validatorMessages ) {
                if( msg.getContext() instanceof Mi25Context ) {
                    ((Mi25Context) msg.getContext() ).setLineNumber( lineNumber );
                }
            }
        }
    }

    public Collection<ValidatorMessage> clusterByMessagesAndRules (Collection<ValidatorMessage> messages){
        Collection<ValidatorMessage> clusteredMessages = new ArrayList<ValidatorMessage>( messages.size() );

        // build a first clustering by message and rule
        Map<String, Map<Rule, Set<ValidatorMessage>>> clustering = new HashMap<String, Map<Rule, Set<ValidatorMessage>>>();
        for (ValidatorMessage message : messages){

            if (clustering.containsKey(message.getMessage())){
                Map<Rule, Set<ValidatorMessage>> messagesCluster = clustering.get(message.getMessage());

                if (messagesCluster.containsKey(message.getRule())){
                    messagesCluster.get(message.getRule()).add(message);
                }
                else{
                    Set<ValidatorMessage> validatorContexts = new HashSet<ValidatorMessage>();
                    validatorContexts.add(message);
                    messagesCluster.put(message.getRule(), validatorContexts);
                }
            }
            else {
                Map<Rule, Set<ValidatorMessage>> messagesCluster = new HashMap<Rule, Set<ValidatorMessage>>();

                Set<ValidatorMessage> validatorContexts = new HashSet<ValidatorMessage>();
                validatorContexts.add(message);
                messagesCluster.put(message.getRule(), validatorContexts);

                clustering.put(message.getMessage(), messagesCluster);
            }
        }

        // build a second cluster by message level
        Map<MessageLevel, Mi25ClusteredContext> clusteringByMessageLevel = new HashMap<MessageLevel, Mi25ClusteredContext>();

        for (Map.Entry<String, Map<Rule, Set<ValidatorMessage>>> entry : clustering.entrySet()){

            String message = entry.getKey();
            Map<Rule, Set<ValidatorMessage>> ruleCluster = entry.getValue();

            // cluster by message level and create proper validatorMessage
            for (Map.Entry<Rule, Set<ValidatorMessage>> ruleEntry : ruleCluster.entrySet()){
                clusteringByMessageLevel.clear();

                Rule rule = ruleEntry.getKey();
                Set<ValidatorMessage> validatorMessages = ruleEntry.getValue();

                for (ValidatorMessage validatorMessage : validatorMessages){

                    if (clusteringByMessageLevel.containsKey(validatorMessage.getLevel())){
                        Mi25ClusteredContext clusteredContext = clusteringByMessageLevel.get(validatorMessage.getLevel());

                        clusteredContext.getContexts().add(validatorMessage.getContext());
                    }
                    else{
                        Mi25ClusteredContext clusteredContext = new Mi25ClusteredContext();

                        clusteredContext.getContexts().add(validatorMessage.getContext());

                        clusteringByMessageLevel.put(validatorMessage.getLevel(), clusteredContext);
                    }
                }

                for (Map.Entry<MessageLevel, Mi25ClusteredContext> levelEntry : clusteringByMessageLevel.entrySet()){

                    ValidatorMessage validatorMessage = new ValidatorMessage(message, levelEntry.getKey(), levelEntry.getValue(), rule);
                    clusteredMessages.add(validatorMessage);

                }
            }
        }

        return clusteredMessages;
    }

    public ValidatorReport getMIValidatorReport() {
        return validatorReport;
    }

    private void clearThreadLocals(){
        // remove validator CvContext
        ValidatorCvContext.removeInstance();
        // close GeneralCacheAdministrator for DatabaseAccessionRule
        DatabaseAccessionRule.closeGeneralCacheAdministrator();
        // close GeneralCacheAdministrator for OntologyManagerContext
        OntologyManagerContext.removeInstance();
    }
}