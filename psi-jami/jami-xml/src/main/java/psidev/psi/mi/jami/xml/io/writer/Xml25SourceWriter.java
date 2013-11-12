package psidev.psi.mi.jami.xml.io.writer;

import org.codehaus.stax2.XMLStreamWriter2;
import psidev.psi.mi.jami.exception.MIIOException;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.utils.XrefUtils;
import psidev.psi.mi.jami.xml.extension.XmlSource;
import psidev.psi.mi.jami.xml.utils.PsiXml25Utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLStreamException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Writer of a source in an entry.
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>11/11/13</pre>
 */

public class Xml25SourceWriter implements PsiXml25ElementWriter<Source> {

    private XMLStreamWriter2 streamWriter;
    private XMLGregorianCalendar calendar;
    private static final Logger logger = Logger.getLogger("Xml25SourceWriter");
    private PsiXml25ElementWriter<Alias> aliasWriter;
    private PsiXml25XrefWriter primaryRefWriter;
    private PsiXml25XrefWriter secondaryRefWriter;
    private PsiXml25ElementWriter<Publication> publicationWriter;
    private PsiXml25ElementWriter<Annotation> attributeWriter;

    public Xml25SourceWriter(XMLStreamWriter2 writer){
        if (writer == null){
            throw new IllegalArgumentException("The XML stream writer is mandatory for the XML25SourceWriter");
        }
        this.streamWriter = writer;
        this.aliasWriter = new Xml25AliasWriter(writer);
        this.primaryRefWriter = new Xml25PrimaryXrefWriter(writer);
        this.publicationWriter = new Xml25PublicationWriter(writer);
        this.attributeWriter = new Xml25AnnotationWriter(writer);
        this.secondaryRefWriter = new Xml25SecondaryXrefWriter(writer);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = null;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
            this.calendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            logger.log(Level.SEVERE, "Cannot create a XMLGregorian calendar. This writer will not write any release date.", e);
            this.calendar = null;
        }
    }

    public Xml25SourceWriter(XMLStreamWriter2 writer, PsiXml25ElementWriter<Alias> aliasWriter,
                             PsiXml25ElementWriter<Publication> publicationWriter, PsiXml25ElementWriter<Annotation> attributeWriter,
                             PsiXml25XrefWriter primaryRefWriter, PsiXml25XrefWriter secondaryRefWriter){
        if (writer == null){
            throw new IllegalArgumentException("The XML stream writer is mandatory for the XML25SourceWriter");
        }
        this.streamWriter = writer;
        this.aliasWriter = aliasWriter!=null ? aliasWriter : new Xml25AliasWriter(writer);
        this.primaryRefWriter = primaryRefWriter!=null ? primaryRefWriter : new Xml25PrimaryXrefWriter(writer);
        this.publicationWriter = publicationWriter!=null ? publicationWriter : new Xml25PublicationWriter(writer);
        this.attributeWriter = attributeWriter!=null? attributeWriter : new Xml25AnnotationWriter(writer);
        this.secondaryRefWriter = secondaryRefWriter!=null?secondaryRefWriter: new Xml25SecondaryXrefWriter(writer);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = null;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
            this.calendar = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            logger.log(Level.SEVERE, "Cannot create a XMLGregorian calendar. This writer will not write any release date.", e);
            this.calendar = null;
        }
    }

    @Override
    public void write(Source object) throws MIIOException{
        try {
            // write start
            this.streamWriter.writeStartElement(PsiXml25Utils.SOURCE_TAG);
            // write release/release date
            if (object instanceof XmlSource){
                XmlSource xmlSource = (XmlSource) object;
                if (xmlSource.getRelease() != null){
                    this.streamWriter.writeAttribute("release", xmlSource.getRelease());
                }
                if (xmlSource.getReleaseDate() != null){
                    this.streamWriter.writeAttribute("releaseDate", xmlSource.getReleaseDate().toXMLFormat());
                }
            }
            else if (this.calendar != null) {
                this.streamWriter.writeAttribute("releaseDate", this.calendar.toXMLFormat());
            }

            // write names
            boolean hasShortName = object.getShortName() != null;
            boolean hasFullName = object.getFullName() != null;
            boolean hasAliases = !object.getSynonyms().isEmpty();
            if (hasShortName || hasFullName || hasAliases){
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                this.streamWriter.writeStartElement("names");
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                // write shortname
                if (hasShortName){
                    this.streamWriter.writeStartElement("shortLabel");
                    this.streamWriter.writeCharacters(object.getShortName());
                    this.streamWriter.writeEndElement();
                    this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                }
                // write fullname
                if (hasFullName){
                    this.streamWriter.writeStartElement("fullName");
                    this.streamWriter.writeCharacters(object.getFullName());
                    this.streamWriter.writeEndElement();
                    this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                }
                // write aliases
                for (Alias alias : object.getSynonyms()){
                    this.aliasWriter.write(alias);
                    this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                }
                // write end names
                this.streamWriter.writeEndElement();
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            // write bibRef
            if (object.getPublication() != null){
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                this.publicationWriter.write(object.getPublication());
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            // write Xref
            if (!object.getIdentifiers().isEmpty()){
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                writeXrefFromSourceIdentifiers(object);
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            else if (!object.getXrefs().isEmpty()){
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                writeXrefFromSourceXrefs(object);
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            // write attributes
            if (!object.getAnnotations().isEmpty()){
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                // write start attribute list
                this.streamWriter.writeStartElement("attributeList");
                for (Annotation ann : object.getAnnotations()){
                    this.attributeWriter.write(ann);
                    this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                }
                // write end attributeList
                this.streamWriter.writeEndElement();
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
            // write end source
            this.streamWriter.writeEndElement();
        } catch (XMLStreamException e) {
            throw new MIIOException("Impossible to write the source of the entry: "+object.toString(), e);
        }
    }

    protected void writeXrefFromSourceXrefs(Source object) throws XMLStreamException {
        Iterator<Xref> refIterator = object.getXrefs().iterator();
        // default qualifier is null as we are not processing identifiers
        this.primaryRefWriter.setDefaultRefType(null);
        this.primaryRefWriter.setDefaultRefTypeAc(null);
        this.secondaryRefWriter.setDefaultRefType(null);
        this.secondaryRefWriter.setDefaultRefTypeAc(null);
        // write start xref
        this.streamWriter.writeStartElement("xref");
        this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);

        int index = 0;
        while (refIterator.hasNext()){
            Xref ref = refIterator.next();
            // write primaryRef
            if (index == 0){
                this.primaryRefWriter.write(ref);
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                index++;
            }
            // write secondaryref
            else{
                this.secondaryRefWriter.write(ref);
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                index++;
            }
        }

        // write end xref
        this.streamWriter.writeEndElement();
    }

    protected void writeXrefFromSourceIdentifiers(Source object) throws XMLStreamException {
        // write start xref
        this.streamWriter.writeStartElement("xref");
        this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);

        // default qualifier is identity
        this.primaryRefWriter.setDefaultRefType(Xref.IDENTITY);
        this.primaryRefWriter.setDefaultRefTypeAc(Xref.IDENTITY_MI);
        this.secondaryRefWriter.setDefaultRefType(Xref.IDENTITY);
        this.secondaryRefWriter.setDefaultRefTypeAc(Xref.IDENTITY_MI);
        String mi = object.getMIIdentifier();
        String par = object.getPARIdentifier();
        Xref miXref = null;
        Xref parXref = null;
        if (miXref != null || parXref != null){
            for (Xref ref : object.getIdentifiers()){
                // ignore MI that is already written
                if (mi != null && mi.equals(ref.getId())
                        && XrefUtils.isXrefFromDatabase(ref, CvTerm.PSI_MI_MI, CvTerm.PSI_MI)){
                    mi = null;
                    miXref = ref;
                }
                // ignore PAR that is already written
                else if (par != null && par.equals(ref.getId())
                        && XrefUtils.isXrefFromDatabase(ref, null, CvTerm.PSI_PAR)){
                    par = null;
                    parXref = ref;
                }
            }
        }

        boolean hasWrittenPrimaryRef = false;
        // write primaryRef
        if (miXref != null){
            this.primaryRefWriter.write(miXref);
            hasWrittenPrimaryRef = true;
            if (parXref != null){
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                this.secondaryRefWriter.write(parXref);
            }
        }
        else if (parXref != null){
            this.primaryRefWriter.write(parXref);
            hasWrittenPrimaryRef = true;
        }

        // write secondaryRefs (and primary ref if not done already)
        Iterator<Xref> refIterator = object.getIdentifiers().iterator();
        while (refIterator.hasNext()){
            Xref ref = refIterator.next();
            // ignore MI that is already written
            // ignore PAR that is already written
            if (ref != miXref && ref != parXref){
                if (!hasWrittenPrimaryRef){
                    hasWrittenPrimaryRef = true;
                    this.primaryRefWriter.write(ref);
                }
                else{
                    this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
                    this.secondaryRefWriter.write(ref);
                }
            }
        }
        this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);

        // write other xrefs
        if (!object.getXrefs().isEmpty()){
            // default qualifier is null
            this.secondaryRefWriter.setDefaultRefType(null);
            this.secondaryRefWriter.setDefaultRefTypeAc(null);
            for (Xref ref : object.getXrefs()){
                this.secondaryRefWriter.write(ref);
                this.streamWriter.writeCharacters(PsiXml25Utils.LINE_BREAK);
            }
        }

        // write end xref
        this.streamWriter.writeEndElement();
    }
}
