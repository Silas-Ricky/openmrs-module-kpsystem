/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.reporting.data.converter.definition.evaluator.kp;

import org.openmrs.annotation.Handler;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.kp.ReceivedPostViolenceSupportDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.kp.StatusInProgramDataDefinition;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates a PersonDataDefinition
 */
@Handler(supports= StatusInProgramDataDefinition.class, order=50)
public class StatusInProgramDataEvaluator implements PersonDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

        String qry = "select r.client_id,\n" +
                "       (case r.dead when 0 then (coalesce(IFNULL((case when timestampdiff(Month,max(date(p.visit_date)),date(:endDate))<=3  then \"A\" when timestampdiff(Month,max(date(p.visit_date)),date(:endDate)) between 4 and 9 then \"DT\"\n" +
                "                                                 when timestampdiff(Month,max(date(p.visit_date)),date(:endDate)) > 9 then \"LTFU\" else null end),(case when timestampdiff(Month,max(date(v.visit_date)),date(:endDate))<=3  then \"A\" when timestampdiff(Month,max(date(v.visit_date)),date(:endDate)) between 4 and 9 then \"DT\"\n" +
                "                                     when timestampdiff(Month,max(date(v.visit_date)),date(:endDate)) > 9 then \"LTFU\" else \"\" end)))) when 1 then \"D\" else null end) as status_in_program from kp_etl.etl_client_registration r left join etl_peer_calendar p on r.client_id = p.client_id\n" +
                "                                                                                                                                                      left join kp_etl.etl_clinical_visit v\n" +
                "                                                                                                                                                      on r.client_id = v.client_id where p.client_id is not null or v.client_id is not null group by r.client_id;";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}