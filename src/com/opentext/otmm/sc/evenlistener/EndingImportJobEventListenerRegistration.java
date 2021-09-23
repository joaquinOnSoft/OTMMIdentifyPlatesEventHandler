/*
 * (C) Copyright 2019 Joaqu�n Garz�n (http://opentext.com) and others.
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

import javax.servlet.ServletContextEvent;

import com.artesia.common.exception.BaseTeamsException;
import com.artesia.event.services.EventServices;
import com.artesia.security.SecuritySession;

public class EndingImportJobEventListenerRegistration extends AbstractEventListenerRegistration {

	public EndingImportJobEventListenerRegistration() {
		super();
		clientId = "Ending-import-job";
	}
		
	@Override
	public void contextInitialized(ServletContextEvent event) {
		clientId = "Ending-import-job";
		
		log.info(">>> " + getClassName() + " >> contextInitialized() Start >>>");

		try {		
			SecuritySession session = com.opentext.otmm.sc.evenlistener.util.EventListenerUtils.getLocalSession(USER_ALIAS_TSUPER);
			EndingImportJobEventListener eijEventListener = new EndingImportJobEventListener(OTMMEvent.IMPORT_JOB_ENDED);
			EventServices.getInstance().addEventListener(clientId, eijEventListener, session);
			
		} catch (BaseTeamsException e) {
			log.error("<<< ERROR in class " + getClassName() + " >> contextInitialized() <<< " + e.getMessage());	
		}		
		
		log.info("<<< " + getClassName() + " >> contextInitialized() End <<<");				
	}
}
