/*
 * (C) Copyright 2021 Joaquín Garzón (http://opentext.com) and others.
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
package com.opentext.otmm.sc.evenlistener.handler;

import com.artesia.asset.AssetIdentifier;
import com.artesia.entity.TeamsIdentifier;
import com.artesia.event.Event;
import com.artesia.metadata.MetadataCollection;
import com.artesia.metadata.MetadataValue;
import com.opentext.otmm.sc.evenlistener.OTMMField;
import com.opentext.otmm.sc.evenlistener.helper.MetadataHelper;
import com.opentext.otmm.sc.modules.plates.Plate;

/**
 * Fraud Media Analysis (FMA) No metadata: The picture doesn’t include any
 * metadata
 */
public class PlateIndentificationOnMetadataUpdate extends AbstractOTMMEventHandler implements OTMMField {
	@Override
	public boolean handle(Event event) {
		boolean handled = false;

		log.info("PlateIndentificationOnEndingImportJob.handle()");

		String strAssetId = event.getObjectId();

		log.info("assetId: " + strAssetId);

		if (strAssetId != null) {
			AssetIdentifier assetId = new AssetIdentifier(strAssetId);
			log.info("Initializing plate indentification on asset: " + assetId.getId());

			MetadataCollection mc = retrieveMetadataForAsset(assetId, new TeamsIdentifier[] {
				new TeamsIdentifier(ARTESIA_FIELD_MEDIAANALYSIS_OCR_TEXT),
				new TeamsIdentifier(ARTESIA_FIELD_MEDIAANALYSIS_BRAND_NAME),
				new TeamsIdentifier(CUSTOM_FIELD_CAR_PLATE_NUMBER),
				new TeamsIdentifier(CUSTOM_FIELD_CAR_PLATE_COUNTRY),
				new TeamsIdentifier(CUSTOM_FIELD_CAR_BRAND)
			});

			if (mc != null) {
				
				MetadataValue metadataOCRText = mc.getValueForField(new TeamsIdentifier(ARTESIA_FIELD_MEDIAANALYSIS_OCR_TEXT));
				if (metadataOCRText != null) {
					String ocrText = metadataOCRText.getStringValue();

					log.info("OCR text: " + ocrText);
					if (ocrText != null) {
						if (Plate.containsPlate(ocrText)){
							String plate = Plate.findFirstPlate(ocrText);
							log.info("\tOCR text contains plate: " + plate);
							
							if (plate != null && plate.compareTo("") != 0) {
								MetadataHelper.saveMetadata(assetId, CUSTOM_FIELD_CAR_PLATE_NUMBER, plate);
							}
							
							
							String countryName = Plate.findFirstCountryCode(ocrText);
							if (countryName != null && countryName.compareTo("") !=  0) {
								MetadataHelper.saveMetadata(assetId, CUSTOM_FIELD_CAR_PLATE_COUNTRY, countryName);
							}
						}
					}
				} else {
					log.info("OCR text NOT FOUND!!!");
				}
				
				MetadataValue metadataBrand[] = mc.getValuesForTabularField(new TeamsIdentifier(ARTESIA_FIELD_MEDIAANALYSIS_BRAND_NAME));
				if(metadataBrand != null && metadataBrand.length > 0) {
					String brand = metadataBrand[0].getStringValue();
					log.info("Brand: " + brand);
					
					if (brand != null && brand.compareTo("") != 0) {
						MetadataHelper.saveMetadata(assetId, CUSTOM_FIELD_CAR_BRAND, brand);	
					}
				}

				handled = true;

			} else {
				log.info("Assets 'Text on Image' metadata NOT FOUND for asset: " + assetId.getId());
			}
		} else {
			log.info("Asset Id was NULL!!!");
		}

		return handled;
	}
}
