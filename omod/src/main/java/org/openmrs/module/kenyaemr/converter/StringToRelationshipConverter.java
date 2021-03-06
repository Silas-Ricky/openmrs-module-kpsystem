/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.converter;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Relationship;
import org.openmrs.api.context.Context;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Convert from {@link String} to {@link org.openmrs.Relationship}, interpreting it as a Relationship.id
 */
@Component("kenyaemrRelationshipConverter")
public class StringToRelationshipConverter implements Converter<String, Relationship> {

	/**
	 * @see org.springframework.core.convert.converter.Converter#convert(Object)
	 */
	@Override
	public Relationship convert(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		return Context.getPersonService().getRelationship(Integer.valueOf(source));
	}
}