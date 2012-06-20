package psidev.psi.mi.calimocho.solr.converter;

import java.util.Set;
import org.apache.solr.common.SolrInputDocument;
import org.hupo.psi.calimocho.key.CalimochoKeys;
import org.hupo.psi.calimocho.model.Field;


/**
 *
 * @author kbreuer
 */
public class SingleBooleanFieldConverter implements SolrFieldConverter {

    public void indexFieldValues(Field field, SolrFieldName name, SolrInputDocument doc, boolean storeOnly, Set<String> uniques) {

        String value = field.get(CalimochoKeys.VALUE);
        String nameField = name.toString();

        //if ((db == null || db.isEmpty()) && (value == null || value.isEmpty()) && (text == null || text.isEmpty())){
        if (value == null || value.isEmpty()) {
            if (!uniques.contains("false")) {
                doc.addField(nameField, false);
                if (!storeOnly) {
                    doc.addField(nameField + "_s", "false");
                }
                uniques.add("false");
            }
        } else {
            if (!uniques.contains("true")) {
                doc.addField(nameField, true);
                if (!storeOnly) {
                    doc.addField(nameField + "_s", "true");
                }
                uniques.add("true");
            }
        }

    }

}
