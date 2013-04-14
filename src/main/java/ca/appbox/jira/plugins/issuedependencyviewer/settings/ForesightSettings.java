package ca.appbox.jira.plugins.issuedependencyviewer.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.issuetype.IssueType;

/**
 * Settings of the plugin.
 * 
 * @author Jean Arcand
 */
public class ForesightSettings {

	private ConstantsManager constantsManager;
	
	public ForesightSettings(ConstantsManager constantsManager) {
		super();
		this.constantsManager = constantsManager;
	}

	public Map<String, String> getColorCodesByIssueTypes() {
		
		Map<String, String> colorCodesByIssueTypes = new HashMap<String, String>();
		List<IssueType> allIssueTypes = new ArrayList<IssueType>(constantsManager.getAllIssueTypeObjects());
		
		for (IssueType currentIssueType : allIssueTypes) {
			String currentIssueTypeName = currentIssueType.getName();
			colorCodesByIssueTypes.put(currentIssueTypeName, 
					JiraDefaultIssueTypes.fromJiraName(currentIssueTypeName).getColorCode());
		}
		
		return colorCodesByIssueTypes;
	}
}

