//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.03 at 12:49:45 PM BST 
//


package psidev.psi.mi.jami.xml.model.extension;

import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;
import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.datasource.FileSourceLocator;
import psidev.psi.mi.jami.model.Annotation;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.Publication;
import psidev.psi.mi.jami.model.Xref;
import psidev.psi.mi.jami.model.impl.DefaultCvTerm;
import psidev.psi.mi.jami.utils.AnnotationUtils;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.collection.AbstractListHavingProperties;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;


/**
 * Desciption of the source of the entry, usually an organisation
 *             
 * 
 * <p>Java class for source complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * The JAXB binding is designed to be read-only and is not designed for writing
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class AbstractXmlSource extends AbstractXmlCvTerm implements ExtendedPsiXmlSource
{
    private Publication bibRef;
    private String release;
    private XMLGregorianCalendar releaseDate;

    @XmlLocation
    @XmlTransient
    protected Locator locator;

    public AbstractXmlSource(){
        super();
    }

    public AbstractXmlSource(String shortName) {
        super(shortName);
    }

    public AbstractXmlSource(String shortName, Xref ontologyId) {
        super(shortName, ontologyId);
    }

    public AbstractXmlSource(String shortName, String fullName, Xref ontologyId) {
        super(shortName, fullName, ontologyId);
    }

    public AbstractXmlSource(String shortName, String url, String address, Publication bibRef) {
        super(shortName);
        setUrl(url);
        setPostalAddress(address);
        this.bibRef = bibRef;
    }

    public AbstractXmlSource(String shortName, Xref ontologyId, String url, String address, Publication bibRef) {
        super(shortName, ontologyId);
        setUrl(url);
        setPostalAddress(address);
        this.bibRef = bibRef;
    }

    public AbstractXmlSource(String shortName, String fullName, Xref ontologyId, String url, String address, Publication bibRef) {
        super(shortName, fullName, ontologyId);
        setUrl(url);
        setPostalAddress(address);
        this.bibRef = bibRef;
    }

    public AbstractXmlSource(String shortName, String miId) {
        super(shortName, miId);
    }

    public AbstractXmlSource(String shortName, String fullName, String miId) {
        super(shortName, fullName, miId);
    }

    public AbstractXmlSource(String shortName, String miId, String url, String address, Publication bibRef) {
        super(shortName, miId);
        setUrl(url);
        setPostalAddress(address);
        this.bibRef = bibRef;
    }

    public AbstractXmlSource(String shortName, String fullName, String miId, String url, String address, Publication bibRef) {
        super(shortName, fullName, miId);
        setUrl(url);
        setPostalAddress(address);
        this.bibRef = bibRef;
    }

    @Override
    public FileSourceLocator getSourceLocator() {
        if (super.getSourceLocator() == null && locator != null){
            super.setSourceLocator(new PsiXmLocator(locator.getLineNumber(), locator.getColumnNumber(), null));
        }
        return super.getSourceLocator();
    }

    @Override
    public void setSourceLocator(FileSourceLocator sourceLocator) {
        if (sourceLocator == null){
            super.setSourceLocator(null);
        }
        else{
            super.setSourceLocator(new PsiXmLocator(sourceLocator.getLineNumber(), sourceLocator.getCharNumber(), null));
        }
    }

    public String getUrl() {
        Annotation url = getAttributeWrapper().url;
        return url != null ? url.getValue() : null;
    }

    public void setUrl(String url) {
        JAXBAttributeWrapper.SourceAnnotationList sourceAnnotationList = (JAXBAttributeWrapper.SourceAnnotationList)getAnnotations();
        Annotation urlAnnot = getAttributeWrapper().url;

        // add new url if not null
        if (url != null){
            CvTerm urlTopic = CvTermUtils.createMICvTerm(Annotation.URL, Annotation.URL_MI);
            // first remove old url if not null
            if (urlAnnot != null){
                sourceAnnotationList.removeOnly(urlAnnot);
            }
            getAttributeWrapper().url = new XmlAnnotation(urlTopic, url);
            sourceAnnotationList.addOnly(getAttributeWrapper().url);
        }
        // remove all url if the collection is not empty
        else if (!sourceAnnotationList.isEmpty()) {
            AnnotationUtils.removeAllAnnotationsWithTopic(sourceAnnotationList, Annotation.URL_MI, Annotation.URL);
            getAttributeWrapper().url = null;
        }
    }

    public String getPostalAddress() {
        Annotation postal = getAttributeWrapper().postalAddress;
        return postal != null ? postal.getValue() : null;
    }

    public void setPostalAddress(String address) {
        JAXBAttributeWrapper.SourceAnnotationList sourceAnnotationList = (JAXBAttributeWrapper.SourceAnnotationList)getAnnotations();
        Annotation postalAnnot = getAttributeWrapper().postalAddress;

        // add new url if not null
        if (address != null){
            CvTerm postalTopic = new DefaultCvTerm(Annotation.POSTAL_ADDRESS);
            // first remove old url if not null
            if (postalAnnot != null){
                sourceAnnotationList.removeOnly(postalAnnot);
            }
            getAttributeWrapper().postalAddress = new XmlAnnotation(postalTopic, address);
            sourceAnnotationList.addOnly(getAttributeWrapper().postalAddress);
        }
        // remove all url if the collection is not empty
        else if (!sourceAnnotationList.isEmpty()) {
            AnnotationUtils.removeAllAnnotationsWithTopic(sourceAnnotationList, null, Annotation.POSTAL_ADDRESS);
            getAttributeWrapper().postalAddress = null;
        }
    }

    public Publication getPublication() {
        return this.bibRef;
    }

    public void setPublication(Publication ref) {
        this.bibRef = ref;
    }

    @XmlElement(name = "bibRef", type = BibRef.class)
    public void setJAXBBibRef(BibRef ref) {
        this.bibRef = ref;
    }

    /**
     * Gets the value of the release property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRelease() {
        return release;
    }

    /**
     * Sets the value of the release property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelease(String value) {
        this.release = value;
    }

    @XmlAttribute(name = "release")
    public void setJAXBRelease(String value) {
        this.release = value;
    }

    /**
     * Gets the value of the releaseDate property.
     *
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets the value of the releaseDate property.
     *
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setReleaseDate(XMLGregorianCalendar value) {
        this.releaseDate = value;
    }

    @XmlAttribute(name = "releaseDate")
    @XmlSchemaType(name = "date")
    public void setJAXBReleaseDate(XMLGregorianCalendar value) {
        this.releaseDate = value;
    }

    @XmlElement(name="attributeList")
    public void setJAXBAttributeWrapper(JAXBAttributeWrapper wrapper){
        super.setAttributeWrapper(wrapper);
    }

    @Override
    protected JAXBAttributeWrapper getAttributeWrapper() {
        return (JAXBAttributeWrapper) super.getAttributeWrapper();
    }

    @Override
    protected void initialiseAnnotationWrapper() {
        super.setAttributeWrapper(new JAXBAttributeWrapper());
    }

    @Override
    public String toString() {
        return "Source: "+(getSourceLocator() != null ? getSourceLocator().toString():super.toString());
    }

    /**
     * Sets the value of the names property.
     *
     * @param value
     *     allowed object is
     *     {@link psidev.psi.mi.jami.xml.model.extension.NamesContainer }
     *
     */
    @XmlElement(name = "names", required = true)
    @Override
    public void setJAXBNames(NamesContainer value) {
        super.setJAXBNames(value);
    }

    /**
     * Sets the value of the xrefContainer property.
     *
     * @param value
     *     allowed object is
     *     {@link psidev.psi.mi.jami.xml.model.extension.XrefContainer }
     *
     */
    @XmlElement(name = "xref", required = true)
    @Override
    public void setJAXBXref(CvTermXrefContainer value) {
        super.setJAXBXref(value);
    }

    //////////////////////////////// class wrapper

    @XmlAccessorType(XmlAccessType.NONE)
    @XmlType(name="sourceAnnotationWrapper")
    public static class JAXBAttributeWrapper extends AbstractXmlCvTerm.JAXBAttributeWrapper implements Locatable, FileSourceContext {
        private Annotation url;
        private Annotation postalAddress;

        @Override
        public Locator sourceLocation() {
            return (Locator)getSourceLocator();
        }

        @XmlElement(type=XmlAnnotation.class, name="attribute", required = true)
        public List<Annotation> getJAXBAttributes() {
            return super.getJAXBAttributes();
        }

        @Override
        protected void initialiseAnnotations() {
            super.initialiseAnnotationsWith(new SourceAnnotationList());
        }

        private void processAddedAnnotationEvent(Annotation added) {
            if (url == null && AnnotationUtils.doesAnnotationHaveTopic(added, Annotation.URL_MI, Annotation.URL)){
                url = added;
            }
            else if (postalAddress == null && AnnotationUtils.doesAnnotationHaveTopic(added, null, Annotation.POSTAL_ADDRESS)){
                postalAddress = added;
            }
        }

        private void processRemovedAnnotationEvent(Annotation removed) {
            if (url != null && url.equals(removed)){
                url = null;
            }
            else if (postalAddress != null && postalAddress.equals(removed)){
                postalAddress = null;
            }
        }

        private void clearPropertiesLinkedToAnnotations() {
            url = null;
            postalAddress = null;
        }

        private class SourceAnnotationList extends AbstractListHavingProperties<Annotation> {
            public SourceAnnotationList(){
                super();
            }

            @Override
            protected void processAddedObjectEvent(Annotation added) {
                processAddedAnnotationEvent(added);
            }

            @Override
            protected void processRemovedObjectEvent(Annotation removed) {
                processRemovedAnnotationEvent(removed);
            }

            @Override
            protected void clearProperties() {
                clearPropertiesLinkedToAnnotations();
            }
        }

        @Override
        public String toString() {
            return "Source Attribute List: "+(getSourceLocator() != null ? getSourceLocator().toString():super.toString());
        }
    }
}
