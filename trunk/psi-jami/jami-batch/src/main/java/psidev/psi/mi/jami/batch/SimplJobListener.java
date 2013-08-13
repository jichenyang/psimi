package psidev.psi.mi.jami.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Enricher job listener
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>02/08/13</pre>
 */

public class SimplJobListener implements JobExecutionListener {

    private static final Log log = LogFactory.getLog(SimplJobListener.class);

    public void beforeJob(JobExecution jobExecution) {
        log.info("\nJOB STARTED: " + jobExecution + "\n");
    }

    public void afterJob(JobExecution jobExecution) {
        log.info("\nJOB FINISHED: "+jobExecution+"\n");
    }
}
