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
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.kp.ARTOutcomeDataDefinition;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * Evaluates a PersonDataDefinition
 */
@Handler(supports= ARTOutcomeDataDefinition.class, order=50)
public class ARTOutcomeDataEvaluator implements PersonDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

        String qry = "select r.client_id,\n" +
                "       if( r.dead= 0,(coalesce((case when timestampdiff(Month,max(date(v.visit_date)),date(:endDate))<=3  then \"A\" when timestampdiff(Month,max(date(v.visit_date)),date(:endDate)) between 4 and 9 then \"DT\"\n" +
                "        when timestampdiff(Month,max(date(v.visit_date)),date(:endDate)) > 9 then \"LTFU\" else \"\" end),(case when timestampdiff(Month,max(date(p.visit_date)),date(:endDate))<=3  then \"A\" when timestampdiff(Month,max(date(p.visit_date)),date(:endDate)) between 4 and 9 then \"DT\"\n" +
                "                                                                                                         when timestampdiff(Month,max(date(p.visit_date)),date(:endDate)) > 9 then \"LTFU\" else \"\" end))),\"D\") as status_in_program from kp_etl.etl_client_registration r\n" +
                "    inner join kp_etl.etl_contact c on r.client_id = c.client_id\n" +
                "        left outer join kp_etl.etl_clinical_visit v\n" +
                "        on v.client_id = r.client_id\n" +
                "        left outer  join kp_etl.etl_peer_calendar p on r.client_id = p.client_id group by v.client_id;";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        Date startDate = (Date)context.getParameterValue("startDate");
        Date endDate = (Date)context.getParameterValue("endDate");
        queryBuilder.addParameter("endDate", endDate);
        queryBuilder.addParameter("startDate", startDate);
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}