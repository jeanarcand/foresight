package ca.appbox.jira.plugins.issuedependencyviewer.servlets.response;

import java.util.Iterator;
import java.util.Map.Entry;

import ca.appbox.jira.plugins.issuedependencyviewer.settings.ForesightSettings;

/**
 * Builds the json representation of the add-on's configurations.
 * 
 * @author Jean Arcand
 */
public class SettingsResponseBuilder {

	/**
	 * Poor man's implementation of Gson.
	 * 
	 * @return Json string representation of the add-on's configurations.
	 */
	public String toJson(ForesightSettings pluginSettings) {
	
		StringBuilder json = new StringBuilder();
		
		json.append("{")
			.append("  \"nodecolors\":{");
		
		Iterator<Entry<String, String>> colorCodesEntriesIter = 
				pluginSettings.getColorCodesByIssueTypes().entrySet().iterator();
		
		while (colorCodesEntriesIter.hasNext()) {
			
			Entry<String, String> currentColorCodeSetting = (Entry<String, String>) colorCodesEntriesIter.next();
			String issueType = currentColorCodeSetting.getKey();
			String colorCode = currentColorCodeSetting.getValue();
			
			json.append("\"")
				.append(issueType)
				.append("\": \"").append(colorCode).append("\"");
			
			if (colorCodesEntriesIter.hasNext()) {
				json.append(",");
			}
		}
		
		json.append("  }")
			.append("}");
		
		return json.toString();
	}
}