/*
 * (C) Copyright 2019 Joaquín Garzón (http://opentext.com) and others.
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
package com.opentext.otmm.sc.evenlistener.helper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.artesia.asset.AssetIdentifier;
import com.artesia.asset.metadata.services.AssetMetadataServices;
import com.artesia.asset.services.AssetServices;
import com.artesia.common.exception.BaseTeamsException;
import com.artesia.entity.TeamsIdentifier;
import com.artesia.metadata.MetadataCollection;
import com.artesia.metadata.MetadataField;
import com.artesia.metadata.MetadataValue;
import com.artesia.security.SecuritySession;

/**
 * @see com.artesia.examples.client.AssetMetadataExample from
 *      Media_Management_Programmer_Guide_16.5
 **/
public class MetadataHelper {

	private static Log log = LogFactory.getLog(MetadataHelper.class);

	public static MetadataCollection[] getMetadataForAssets(List<AssetIdentifier> assetIds, TeamsIdentifier fieldId) {
		MetadataCollection[] collection = null;
		try {
			collection = AssetMetadataServices.getInstance().retrieveMetadataForAssets(
					(AssetIdentifier[]) assetIds.toArray(new AssetIdentifier[assetIds.size()]),
					fieldId.asTeamsIdArray(), null, SecurityHelper.getAdminSession());
		} catch (BaseTeamsException e) {
			log.error(e.getMessage(), e);
		}
		return collection;
	}

	public static void saveMetadataForAsset(AssetIdentifier assetId, MetadataField[] metadataFields) {
		SecuritySession session = SecurityHelper.getAdminSession();

		try {

			// save the metadata
			AssetMetadataServices.getInstance().saveMetadataForAssets(assetId.asAssetIdArray(), metadataFields,
					session);
		} catch (BaseTeamsException e) {
			log.error("Error saving metadata", e);
		}
	}

	public static void lockAsset(AssetIdentifier assetIds) {
		try {
			AssetServices.getInstance().lockAsset(assetIds, SecurityHelper.getAdminSession());
		} catch (BaseTeamsException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void lockAssets(List<AssetIdentifier> assetIds) {
		try {
			AssetServices.getInstance().lockAssets(
					(AssetIdentifier[]) assetIds.toArray(new AssetIdentifier[assetIds.size()]),
					SecurityHelper.getAdminSession());
		} catch (BaseTeamsException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void unlockAssets(List<AssetIdentifier> assetIds) {
		try {
			AssetServices.getInstance().unlockAssets(
					(AssetIdentifier[]) assetIds.toArray(new AssetIdentifier[assetIds.size()]),
					SecurityHelper.getAdminSession());
		} catch (BaseTeamsException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void unlockAsset(AssetIdentifier assetId) {
		try {
			AssetServices.getInstance().unlockAsset(assetId, SecurityHelper.getAdminSession());
		} catch (BaseTeamsException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void saveMetadata(List<AssetIdentifier> assetIds, MetadataField field) {
		try {
			AssetMetadataServices.getInstance().saveMetadataForAssets(
					(AssetIdentifier[]) assetIds.toArray(new AssetIdentifier[assetIds.size()]),
					new MetadataField[] { field }, SecurityHelper.getAdminSession());
		} catch (BaseTeamsException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static MetadataField getMetadataField(AssetIdentifier assetId, TeamsIdentifier fieldId) {
		return getMetadataField(assetId, fieldId, SecurityHelper.getAdminSession());
	}

	public static MetadataField getMetadataField(AssetIdentifier assetId, TeamsIdentifier fieldId,
			SecuritySession session) {
		try {
			return AssetMetadataServices.getInstance().retrieveMetadataFieldForAsset(assetId, fieldId, session);
		} catch (BaseTeamsException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static MetadataCollection getMetadataField(AssetIdentifier assetId, TeamsIdentifier[] fieldIds,
			SecuritySession session) {
		try {
			return AssetMetadataServices.getInstance().retrieveMetadataForAsset(assetId, fieldIds, session);
		} catch (BaseTeamsException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}


	public static void saveMetadataNotLocking(AssetIdentifier assetIDs, String fieldId, String value) {		
		saveMetadataNotLocking((List<AssetIdentifier>) Arrays.asList(new AssetIdentifier[]{assetIDs}), 
				fieldId, new MetadataValue(value));
	}
	
	public static void saveMetadataNotLocking(List<AssetIdentifier> assetIDs, String fieldId, MetadataValue value) {
		TeamsIdentifier teamsFieldId = new TeamsIdentifier(fieldId);
		MetadataCollection[] metaCol = MetadataHelper.getMetadataForAssets(assetIDs, teamsFieldId);

		log.info("Getting metadatafield");
		MetadataField assetStatusField = (MetadataField) metaCol[0].findElementById(teamsFieldId);

		assetStatusField.setValue(value);
		
		// save the new value
		log.info("Saving '"+ fieldId + "' field");
		MetadataHelper.saveMetadata(assetIDs, assetStatusField);		
	}
	
	public static void saveMetadata(List<AssetIdentifier> assetIDs, String fieldId, MetadataValue value) {
		TeamsIdentifier teamsFieldId = new TeamsIdentifier(fieldId);
		MetadataCollection[] metaCol = MetadataHelper.getMetadataForAssets(assetIDs, teamsFieldId);

		log.info("Getting metadatafield");
		MetadataField assetStatusField = (MetadataField) metaCol[0].findElementById(teamsFieldId);

		assetStatusField.setValue(value);

		// lock the asset before saving
		log.info("Locking assets: " + assetIDs);
		MetadataHelper.lockAssets(assetIDs);

		// save the new value
		log.info("Saving '"+ fieldId + "' field");
		MetadataHelper.saveMetadata(assetIDs, assetStatusField);

		log.info("Unlocking assets: " + assetIDs);
		MetadataHelper.unlockAssets(assetIDs);

	}

	public static void saveMetadata(List<AssetIdentifier> assetIDs, String fieldId, String value) {
		saveMetadata(assetIDs, fieldId, new MetadataValue(value));
	}

	public static void saveMetadata(AssetIdentifier assetID, String fieldId, MetadataValue value) {
		List<AssetIdentifier> assetIDs = new LinkedList<AssetIdentifier>();
		assetIDs.add(assetID);

		saveMetadata(assetIDs, fieldId, value);
	}

	public static void saveMetadata(AssetIdentifier assetID, String fieldId, String value) {
		saveMetadata(assetID, fieldId, new MetadataValue(value));
	}

	public static void saveMetadata(AssetIdentifier assetID, String fieldId, double value) {
		saveMetadata(assetID, fieldId, new MetadataValue(value));
	}
}
