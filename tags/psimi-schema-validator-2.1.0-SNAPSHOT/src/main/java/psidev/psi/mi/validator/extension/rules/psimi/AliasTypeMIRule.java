package psidev.psi.mi.validator.extension.rules.psimi;

import psidev.psi.mi.jami.model.Alias;
import psidev.psi.mi.validator.extension.Mi25Context;
import psidev.psi.mi.validator.extension.MiAliasRule;
import psidev.psi.mi.validator.extension.rules.RuleUtils;
import psidev.psi.tools.ontology_manager.OntologyManager;
import psidev.psi.tools.ontology_manager.interfaces.OntologyAccess;
import psidev.psi.tools.ontology_manager.interfaces.OntologyTermI;
import psidev.psi.tools.validator.MessageLevel;
import psidev.psi.tools.validator.ValidatorException;
import psidev.psi.tools.validator.ValidatorMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Rule to check that an alias type has a valid alias type if not null
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>26/03/13</pre>
 */

public class AliasTypeMIRule extends MiAliasRule{
    public AliasTypeMIRule(OntologyManager ontologyManager) {
        super(ontologyManager);
        setName( "Alias type MI check" );

        setDescription( "Check that the MI identifier for each alias is a valid MI identifier when it exists." );
        addTip( "You can find all existing alias types at http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI&termId=MI%3A0300&termName=alias%20type" );
    }

    @Override
    public Collection<ValidatorMessage> check(Alias alias) throws ValidatorException {

        // list of messages to return
        List<ValidatorMessage> messages = new ArrayList<ValidatorMessage>();

        if (alias.getType() != null && alias.getType().getMIIdentifier() != null){
            final OntologyAccess access = ontologyManager.getOntologyAccess("MI");
            final OntologyTermI dbTerm = access.getTermForAccession(alias.getType().getMIIdentifier());

            if (dbTerm == null){
                Mi25Context context = RuleUtils.buildContext(alias, "alias");

                messages.add( new ValidatorMessage( "The alias type is not a valid MI term. The valid MI terms for alias types are available here: http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI&termId=MI%3A0300&termName=alias%20type",
                        MessageLevel.ERROR,
                        context,
                        this ) );
            }
            else {
                Collection<OntologyTermI> parents = access.getAllParents(dbTerm);

                boolean foundParent = false;

                for (OntologyTermI p : parents){
                    if ("MI:0300".equals(p.getTermAccession())){
                        foundParent = true;
                        break;
                    }
                }

                if (!foundParent){
                    Mi25Context context = RuleUtils.buildContext(alias, "alias");

                    messages.add( new ValidatorMessage( "The alias type is not a valid MI term. The valid MI terms for alias types are available here: http://www.ebi.ac.uk/ontology-lookup/browse.do?ontName=MI&termId=MI%3A0300&termName=alias%20type",
                            MessageLevel.ERROR,
                            context,
                            this ) );
                }
            }
        }

        return messages;
    }

    public String getId() {
        return "R16";
    }
}