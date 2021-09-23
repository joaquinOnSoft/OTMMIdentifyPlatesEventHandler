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

import java.util.List;

import com.artesia.asset.AssetIdentifier;
import com.artesia.entity.TeamsIdentifier;
import com.artesia.event.Event;
import com.artesia.metadata.MetadataCollection;
import com.artesia.metadata.MetadataValue;
import com.opentext.job.Job;
import com.opentext.otmm.sc.evenlistener.OTMMField;
import com.opentext.otmm.sc.evenlistener.helper.JobHelper;
import com.opentext.otmm.sc.modules.plates.Plate;

/**
 * Fraud Media Analysis (FMA) No metadata: The picture doesn’t include any
 * metadata
 */
public class PlateIndentificationOnEndingImportJob extends AbstractOTMMEventHandler implements OTMMField {
	@Override
	public boolean handle(Event event) {
		boolean handled = false;

		Job job = JobHelper.retrieveJob(event.getObjectId());
		List<AssetIdentifier> assetIds = job.getAssetIds();

		log.info(assetIds);

		if (assetIds != null && assetIds.size() > 0) {

			for (AssetIdentifier assetId : assetIds) {
				log.debug("Initializing plate indentification on asset: " + assetId.getId());
				
				MetadataCollection mc = retrieveMetadataForAsset(assetId,
						ARTESIA_FIELD_MEDIAANALYSIS_OCR_TEXT);

				if (mc != null) {
					
					MetadataValue metadataOCRText = mc.getValueForField(new TeamsIdentifier(ARTESIA_FIELD_MEDIAANALYSIS_OCR_TEXT));
					if(metadataOCRText != null) {
						String ocrText = metadataOCRText.getStringValue();
						
						log.debug("OCR text: " + ocrText);
						if(ocrText != null) {
							log.debug("OCR text contains plate: " + Plate.containsPlate(ocrText));;
						}
					}
					else {
						log.debug("OCR text NOT FOUND!!!");
					}					
					/*
					 * log.debug("Asset Metadata (size): " + assetEmbeddedPictureFields.size());
					 * 
					 * int rows = 0; MetadataValue fieldValue = null;
					 * 
					 * for(TeamsIdentifier identifier: EMBEDDED_PICTURE_FIELDS){ MetadataTableField
					 * field = (MetadataTableField)
					 * assetEmbeddedPictureFields.findElementById(identifier);
					 * log.info(field.getName() + " ===================================");
					 * 
					 * rows = field.getRowCount(); for(int i=0; i<rows; i++) { fieldValue =
					 * field.getValueAt(i); log.debug("\t" + field.getName() + "[" + i + "]: " +
					 * field.getValueAt(i));
					 * 
					 * if(!fieldValue.isNullValue()) { emptyMetadata = false; } } }
					 * 
					 */

					// Recovering Custom metadata
					/*
					 * MetadataTableField fmaNoMetadata = (MetadataTableField)
					 * assetOCRTextField.findElementById(new
					 * TeamsIdentifier(CUSTOM_FRAUD_MEDIA_ANALYSIS_NO_METADATA));
					 * fmaNoMetadata.setValue(new MetadataValue(emptyMetadata));
					 * 
					 * MetadataField[] metadataFields = new MetadataField[] { fmaNoMetadata };
					 * 
					 * MetadataHelper.lockAsset(assetId);
					 * MetadataHelper.saveMetadataForAsset(assetId, metadataFields);
					 * MetadataHelper.unlockAsset(assetId);
					 */
					handled = true;

				} else {
					log.info("Assets 'Text on Image' metadata NOT FOUND for asset: " + assetId.getId());
				}
			}
		} else {
			log.info("Assets list was EMPTY!!!");
		}

		return handled;
	}
}
