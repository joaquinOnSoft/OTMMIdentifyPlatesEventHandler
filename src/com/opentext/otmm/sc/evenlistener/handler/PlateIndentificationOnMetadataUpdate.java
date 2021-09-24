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

			MetadataCollection mc = retrieveMetadataForAsset(assetId, ARTESIA_FIELD_MEDIAANALYSIS_OCR_TEXT);

			if (mc != null) {

				MetadataValue metadataOCRText = mc
						.getValueForField(new TeamsIdentifier(ARTESIA_FIELD_MEDIAANALYSIS_OCR_TEXT));
				if (metadataOCRText != null) {
					String ocrText = metadataOCRText.getStringValue();

					log.info("OCR text: " + ocrText);
					if (ocrText != null) {
						log.info("OCR text contains plate: " + Plate.containsPlate(ocrText));
						;
					}
				} else {
					log.info("OCR text NOT FOUND!!!");
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
