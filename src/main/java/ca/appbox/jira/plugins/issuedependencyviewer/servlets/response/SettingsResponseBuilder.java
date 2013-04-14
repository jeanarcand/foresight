package ca.appbox.jira.plugins.issuedependencyviewer.servlets.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ca.appbox.jira.plugins.issuedependencyviewer.settings.ForesightSettings;

import com.atlassian.jira.issue.issuetype.IssueType;

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
			.append("  \"configurations\":{")
			.append("    \"node-colors\":{");
		
		Set<Entry<String, String>> colorCodesEntries = 
				pluginSettings.getColorCodesByIssueTypes().entrySet();
		
		while (colorCodesEntries.iterator().hasNext()) {
			
			Entry<String, String> currentColorCodeSetting = (Entry<String, String>) colorCodesEntries.iterator().next();
			String issueType = currentColorCodeSetting.getKey();
			String colorCode = currentColorCodeSetting.getValue();
			
			json.append("\"")
				.append(issueType)
				.append("\": \"").append(colorCode).append("\"");
			
			if (!colorCodesEntries.iterator().hasNext()) {
				json.append(",");
			}
		}
		
		json.append("    }")
			.append("  }")
			.append("}");
		
		return json.toString();
	}
}