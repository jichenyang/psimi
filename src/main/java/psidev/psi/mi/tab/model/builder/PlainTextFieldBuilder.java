/**
 * Copyright 2008 The European Bioinformatics Institute, and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package psidev.psi.mi.tab.model.builder;

/**
 * Field builder for plain text.
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
@Deprecated
public class PlainTextFieldBuilder extends AbstractFieldBuilder {

    public Field createField(String str) {

        str = removeLineReturn(str);
        
        if (str.startsWith("\"") && str.endsWith("\"")) {
            str = str.substring(1, str.length()-1);
        }

        Field field = null;
        if(!String.valueOf( Column.EMPTY_COLUMN ).equals( str )) {
            field = new Field(str);
        }

        return field;
    }
}
