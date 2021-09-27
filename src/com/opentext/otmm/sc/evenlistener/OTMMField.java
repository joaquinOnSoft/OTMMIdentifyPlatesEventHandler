/*
 * (C) Copyright 2020 Joaquín Garzón (http://opentext.com) and others.
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
 *
 * Contributors:
 *   Joaquín Garzón - initial implementation
 */
package com.opentext.otmm.sc.evenlistener;

import com.artesia.entity.TeamsIdentifier;

public interface OTMMField {

	public static final String CUSTOM_FIELD_CAR_PLATE_NUMBER = "CUSTOM.FIELD.CAR.PLATE.NUMBER";
	public static final String CUSTOM_FIELD_CAR_PLATE_COUNTRY = "CUSTOM.FIELD.CAR.PLATE.COUNTRY";
	public static final String CUSTOM_FIELD_CAR_BRAND = "CUSTOM.FIELD.CAR.BRAND";

	public static final TeamsIdentifier[] CUSTOM_CAR_FIELDS = new TeamsIdentifier[] {	
			new TeamsIdentifier(CUSTOM_FIELD_CAR_PLATE_NUMBER),
			new TeamsIdentifier(CUSTOM_FIELD_CAR_PLATE_COUNTRY),
			new TeamsIdentifier(CUSTOM_FIELD_CAR_BRAND)
	};

	/**
	 * Id: ARTESIA.FIELD.MEDIAANALYSIS.OCR TEXT
	 * Name: Text on Image
	 */
	public static final String ARTESIA_FIELD_MEDIAANALYSIS_OCR_TEXT = "ARTESIA.FIELD.MEDIAANALYSIS.OCR TEXT";
	
	/**
	 * Id: ARTESIA.TABLE.MEDIA ANALYSIS.BRAND INFO
	 * Name:	Embedded Brand Info
	 * Database Table: OTMM_MA_BRANDS
	 * 	Tabular Fields
	 * 		ARTESIA.FIELD.MEDIAANALYSIS.BRAND.NAME - Embedded Brand Name
	 * 		ARTESIA.FIELD.MEDIAANALYSIS.BRAND.CONFIDENCE - Embedded Brand Confidence		
	 * 		ARTESIA.FIELD.MEDIAANALYSIS.BRAND.COORDINATES - Embedded Brand Coordinates
	 */
	public static final String ARTESIA_TABLE_MEDIA_ANALYSIS_BRAND_INFO = "ARTESIA.TABLE.MEDIA ANALYSIS.BRAND INFO";
	// Embedded Brand Name
	public static final String ARTESIA_FIELD_MEDIAANALYSIS_BRAND_NAME = "ARTESIA.FIELD.MEDIAANALYSIS.BRAND.NAME";
	// Embedded Brand Confidence
	public static final String ARTESIA_FIELD_MEDIAANALYSIS_BRAND_CONFIDENCE = "ARTESIA.FIELD.MEDIAANALYSIS.BRAND.CONFIDENCE";	
	//Embedded Brand Coordinates
	public static final String ARTESIA_FIELD_MEDIAANALYSIS_BRAND_COORDINATES = "ARTESIA.FIELD.MEDIAANALYSIS.BRAND.COORDINATES"; 
}
