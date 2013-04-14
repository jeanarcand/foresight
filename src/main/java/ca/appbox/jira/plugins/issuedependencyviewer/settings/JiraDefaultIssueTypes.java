package ca.appbox.jira.plugins.issuedependencyviewer.settings;

/**
 * An enum with the default jira issue types.
 * 
 * @author Jean Arcand
 */
public enum JiraDefaultIssueTypes {

	NEW_FEATURE("New Feature", "#3ADF00"), 
	TASK("Task", "#F7FE2E"), 
	IMPROVEMENT("Improvement", "#0040FF"), 
	BUG("Bug", "#FF0000"), 
	SUB_TASK("Sub-task", "#F7FE2E"), 
	UNKNOWN("Unknown", "#CCCCCC");

	private String defaultJiraName;
	private String colorCode;

	private JiraDefaultIssueTypes(String defaultJiraName, String colorCode) {
		this.defaultJiraName = defaultJiraName;
		this.colorCode = colorCode;
	}

	public static JiraDefaultIssueTypes fromJiraName(String issueTypeName) {
			
		JiraDefaultIssueTypes  matchingValue = JiraDefaultIssueTypes.UNKNOWN;
		
		for (JiraDefaultIssueTypes  currentEnumValue : JiraDefaultIssueTypes.values()) {
		
			if (currentEnumValue.getDefaultJiraName().equals(issueTypeName)) {
				matchingValue = currentEnumValue;
				break;
			}
		}
		
		return matchingValue;
	}

	public String getDefaultJiraName() {
		return defaultJiraName;
	}

	public void setDefaultJiraName(String defaultJiraName) {
		this.defaultJiraName = defaultJiraName;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
}
