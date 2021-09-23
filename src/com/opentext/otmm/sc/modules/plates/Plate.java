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
package com.opentext.otmm.sc.modules.plates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Plate {
	private static final String PATTERN_SPECIAL_PLATE = "[CMEDGPNATFSHMORW]{1,5}(\\s|-)*[0-9]{4}";
	private static final String PATTERN_OLD_PLATE = "[A-Z]{1,3}(\\s|-)*[0-9]{4}(\\s|-)*[A-Z]{2}";
	private static final String PATTERN_NEW_PLATE = "[0-9]{4}(\\s|-)*[BCDFGHJKLMNPRSTVWXYZ]{3}";

	private static final String PATTERN_ALL_PLATES = PATTERN_NEW_PLATE + "|" + 
			PATTERN_SPECIAL_PLATE + "|" +
			PATTERN_OLD_PLATE;

	public static String findFirstPlate(String txt) {
		String plate = null;
		String pattern= PATTERN_NEW_PLATE;
		
		if(isNew(txt)) {
			pattern = PATTERN_NEW_PLATE;
		}
		else if (isSpecial(txt)) {
			pattern = PATTERN_SPECIAL_PLATE;
		}
		else if (isOld(txt)){
			pattern = PATTERN_OLD_PLATE;
		}
		
		Matcher m = Pattern.compile(pattern).matcher(txt);
		if(m.find()) {
			plate = m.group();
		}
		
		return plate;
	}
	
	public static boolean matchPatter(String txt, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(txt);

		return matcher.matches();
	}	

	/**
	 * Validate if a given text contains a valid Spanish plate number
	 * 
	 * Valid Spanish plate number:
	 *    1234BCD
	 *    1234 FGH
	 *    2345-JKL
	 *    A 0849 CS
	 *    A-0849 CS
	 *    B 1234 BL
	 *    M-1234 BL
	 *    SO 1234 BL
	 *    B 1234 BL
	 *    M 1234 BL
	 *    SO-1234 BL
	 * Old plate number:   
	 *    One letter code:
	 *       A 0849 CS
	 *    
	 *    Two letter code:
	 *       GI-1234-BL
	 *    
	 *    Two/three-letter special code (such as ET for army cars and DGP for police cars)
	 *       DGP 1234 BL
	 *       DGP 3874
	 *       CNP-5764
	 *       E8720
	 *    
	 * DGP (Dirección General de Policia) - Spanish Police
	 *    DGP1234
	 *    
	 * Corps of the Mossos d'Esquadra"
	 *    CME1234
	 * 
	 * @param plateNumber - Text that might contain a plate number
	 * @return true if is a valid plate number, false in other case
	 * 
	 * @see https://stackoverflow.com/questions/41146180/how-to-build-a-regex-pattern-to-verify-a-spanish-car-enrollment
	 * @see https://github.com/singuerinc/spanish-car-plate 
	 * @see https://regex101.com/
	 */
	public static boolean containsPlate(String plateNumber) {
		return matchPatter(plateNumber, PATTERN_ALL_PLATES);
	}
	
	/**
	 * Check if a text includes a special plate, i.e. Police, Air force, Army, Navy, etc.
	 * Some examples:
	 * 	CME 2342
	 * 	E 1660
	 * @param txt - Text to evaluate looking for special plate numbers 
	 * @return Returns true if is a valid special car plate
	 */
	public static boolean isSpecial(String txt) {
		return matchPatter(txt, PATTERN_SPECIAL_PLATE);
	}

	/** Check if a text includes a valid old system (1971-2000) car plate
	 * Some examples:
	 *   one-letter code: A 0849 CS
	 *   two-letter code: GI-1234-BL
	 *   two/three-letter special code (such as ET for army cars and DGP for police cars): DGP 1234 BL
	 * @param txt - Text to evaluate looking for old plate numbers 
	 * @return Returns true if is a valid (old system 1971-2000) car plate
	 */
	public static boolean isOld(String txt) {
		return matchPatter(txt, PATTERN_OLD_PLATE);
	}

	/** Check if a text includes a valid "new" car plate (2000 to present date)
	 * Some examples:
	 *    1234BCD
	 *    1234 FGH
	 *    2345-JKL
	 * @param txt - Text to evaluate looking for new plate numbers 
	 * @return Returns true if is a valid "new" (2000 to present date)  car plate
	 */
	public static boolean isNew(String txt) {
		return matchPatter(txt, PATTERN_NEW_PLATE);
	}	
}