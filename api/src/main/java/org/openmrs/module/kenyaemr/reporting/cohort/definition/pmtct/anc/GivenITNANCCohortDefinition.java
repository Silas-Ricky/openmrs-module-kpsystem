package org.openmrs.module.kenyaemr.reporting.cohort.definition.pmtct.anc;

import org.openmrs.module.reporting.cohort.definition.BaseCohortDefinition;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.definition.configuration.ConfigurationPropertyCachingStrategy;
import org.openmrs.module.reporting.evaluation.caching.Caching;

/**
 * RDQA cohort definition
 */
@Caching(strategy = ConfigurationPropertyCachingStrategy.class)
@Localized("reporting.GivenITNANCCohortDefinition")
public class GivenITNANCCohortDefinition extends BaseCohortDefinition {

}