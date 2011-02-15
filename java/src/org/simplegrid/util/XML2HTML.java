/**
 *  Copyright (c) 2007-2009 CyberInfrastructure and Geospatial
 *  Information Laboratory (CIGI), University of Illinois at
 *  Urbana-Champaign, All Rights Reserved.
 */

package org.simplegrid.util;

public class XML2HTML {
	/**
	 * Convert a xml string to html. 
	 * Assume that the xml string does not have heading and trailing spaces,
	 * i.e., a raw xml string
	 * @param xml
	 * @return
	 */
	public static String convert(String xml) {
		if (!isValid(xml)) return "";
		String html = "";
		String elem1 = "";
		String elem2 = "";
		int indent = 0;
		boolean pairStart = false;
		boolean elemEndStart = false;
		boolean quoteStart = false;
		char pc = 0;
		for (int i=0; i<xml.length(); i++) {
			char c = xml.charAt(i);
			switch (c) {
				case '<':
					// requirement: < should be in quoted string anyway
					if (!elem2.equals("")) {
						for (int j=0; j<indent; j++) 
							html += "    ";
						html += elem2 + "\n";
						elem2="";
					}
					elem1 += "&lt;";
					pairStart = true;
					break;
				case '"':
					// requirement: any path url in xml must be quoted
					quoteStart = !quoteStart;
					if (pairStart)
						elem1 += c;
					else
						elem2 += c;
					break;
				case '/':
					if (!quoteStart && pairStart) {
						elemEndStart = true;
						if (pc=='<') {
							indent --;
						}
					}
					if (pairStart)
						elem1 += c;
					else
						elem2 += c;
					break;
				case '>':
					elem1 += "&gt;";
					for (int j=0; j<indent; j++)
						html += "    ";
					html += elem1 + "\n";
					elem1 = ""; elem2 ="";
					if (elemEndStart) {
						elemEndStart = false;
					} else {
						indent ++;
					}
					pairStart = false;
					break;
				default:
					if (pairStart)
						elem1 += c;
					else
						elem2 += c;					
				 	break;
			}
			pc = c;
		}
		return html;
	}
	/**
	 * A loose validate util for xml
	 * @param xml
	 * @return
	 */
	private static boolean isValid(String xml) {
		boolean paired = true;
		boolean pairedQ = true;
		for (int i=0; i<xml.length(); i++) {
			switch (xml.charAt(i)) {
			case '<': 
				if (!paired) return false;
				paired = false;
				break;
			case '>':
				if (paired) return false;
				paired = true; 
				break;
			case '"': pairedQ = !pairedQ; break;
			default: break;
			}
		}
		return paired && pairedQ;
	}
	
	public static void main(String[] args) throws Exception {
		String filePath = "/tmp/test.xml";
        java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(filePath));
        String s;
        String content = "";
        while((s = reader.readLine())!= null){
            content += s.trim();
        }
        reader.close();
        System.out.println(XML2HTML.convert(content));
	}
}
