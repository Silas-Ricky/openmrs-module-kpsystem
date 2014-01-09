/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyaemr.wrapper;

import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.module.kenyacore.wrapper.AbstractCustomizableWrapper;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;

/**
 * A facility wrapper for a location
 */
public class Facility extends AbstractCustomizableWrapper<Location, LocationAttribute> {

	/**
	 * Creates a new facility wrapper for a location
	 * @param location the location
	 */
	public Facility(Location location) {
		super(location);
	}

	/**
	 * Gets the province
	 * @return the province
	 */
	public String getProvince() {
		return target.getStateProvince();
	}

	/**
	 * Gets the county
	 * @return the county
	 */
	public String getCounty() {
		return target.getCountyDistrict();
	}

	/**
	 * Gets the district
	 * @return the district
	 */
	public String getDistrict() {
		return target.getAddress6();
	}

	/**
	 * Gets the division
	 * @return the division
	 */
	public String getDivision() {
		return target.getAddress5();
	}

	/**
	 * Gets the master facility code
	 * @return the code
	 */
	public String getMflCode() {
		return (String) getFirstAttributeValue(CommonMetadata._LocationAttributeType.MASTER_FACILITY_CODE);
	}
}