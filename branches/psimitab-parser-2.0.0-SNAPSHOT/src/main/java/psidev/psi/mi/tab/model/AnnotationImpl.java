package psidev.psi.mi.tab.model;

import psidev.psi.mi.jami.datasource.FileSourceContext;
import psidev.psi.mi.jami.model.CvTerm;
import psidev.psi.mi.jami.model.impl.DefaultCvTerm;

/**
 * Created with IntelliJ IDEA.
 * User: ntoro
 * Date: 12/06/2012
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class AnnotationImpl implements Annotation, psidev.psi.mi.jami.model.Annotation, FileSourceContext {

    /**
     * Generated with IntelliJ plugin generateSerialVersionUID.
     * To keep things consistent, please use the same thing.
     */
    private static final long serialVersionUID = 4162569906234270001L;

    private CvTerm topic;
    private String value;

    private MitabSourceLocator locator;

    private static final String UNSPECIFIED = "unspecified";
    /////////////////////////////////
    // Constructor

    public AnnotationImpl(String topic) {
        this.topic = new DefaultCvTerm(topic != null ? topic : UNSPECIFIED);
    }

    public AnnotationImpl(String topic, String text) {
        this.topic = new DefaultCvTerm(topic != null ? topic : UNSPECIFIED);
        this.value = text;
    }

    /////////////////////////////////
    // Getters and setters

    /**
     * {@inheritDoc}
     */
    public String getAnnotationTopic() {
        return this.topic.getShortName();
    }

    /**
     * {@inheritDoc}
     */
    public void setTopic(String topic) {
        if (topic == null) {
            throw new IllegalArgumentException("Topic name cannot be null.");
        }
        this.topic = new DefaultCvTerm(topic);
    }

    /**
     * {@inheritDoc}
     */
    public String getText() {
        return this.value != null ? this.value : null;
    }

    /**
     * {@inheritDoc}
     */
    public void setText(String text) {
        if (text != null) {
            // ignore empty string
            this.value = text.trim();
            if (text.length() == 0) {
                this.value = null;
            }
        }
        else {
            this.value = null;
        }
    }

    ///////////////////////////////
    // Object's override

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Annotation");
        sb.append("{topic='").append(this.topic != null ? this.topic.getShortName() : "-").append('\'');
        sb.append(", text='").append(this.value != null ? this.value : "-").append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AnnotationImpl that = (AnnotationImpl) o;

        if (!topic.getShortName().equals(that.topic.getShortName())) {
            return false;
        }
        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }

        return true;
    }

    public CvTerm getTopic() {
        return topic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public MitabSourceLocator getSourceLocator() {
        return locator;
    }

    public void setLocator(MitabSourceLocator locator) {
        this.locator = locator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result;
        result = (value != null ? value.hashCode() : 0);
        result = 29 * result + topic.getShortName().hashCode();
        return result;
    }

}