/*
 * (C) Copyright 2020 Joaqu�n Garz�n (http://opentext.com) and others.
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
 *   Joaqu�n Garz�n - initial implementation
 */
package com.opentext.otmm.sc.evenlistener;

import com.artesia.entity.TeamsIdentifier;
import com.artesia.event.Event;
import com.opentext.otmm.sc.evenlistener.handler.PlateIndentificationOnAnalysisDataFromAzureIsDeleted;
import com.opentext.otmm.sc.evenlistener.handler.OTMMEventHandler;

public class EndingImportJobEventListener extends AbstractEventLister {

	public EndingImportJobEventListener() {
		super();
	}

	public EndingImportJobEventListener(String events) {
		super(events);		
	}


	@Override
	public void onEvent(Event event) {
		displayEventObject(event);
		
		if (event.getEventId().equals(new TeamsIdentifier(OTMMEvent.IMPORT_JOB_ENDED))) {
			log.info("Ids match for 'Ending Import Job (2229148)' event. Event Id: " + event.getEventId());
			
			OTMMEventHandler handler = new PlateIndentificationOnAnalysisDataFromAzureIsDeleted();
			handler.handle(event);
		}
	}

}