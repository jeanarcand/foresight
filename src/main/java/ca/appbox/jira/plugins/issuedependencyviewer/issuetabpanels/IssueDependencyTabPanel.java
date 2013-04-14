package ca.appbox.jira.plugins.issuedependencyviewer.issuetabpanels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.atlassian.jira.plugin.issuetabpanel.IssueAction;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanelModuleDescriptor;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.opensymphony.user.User;

/**
 * Manages the issue tab panel for viewing dependencies between issues.
 * 
 * @author Jean Arcand
 */
public final class IssueDependencyTabPanel extends AbstractIssueTabPanel {

	private static final String ISSUE_TAB_PANEL_VELOCITY_TEMPLATE = "dependency-graph-panel.vm";

	private final WebResourceManager webResourceManager;
	
	private IssueTabPanelModuleDescriptor issueTabPanelModuleDescriptor;
	
	public IssueDependencyTabPanel(WebResourceManager webResourceManager) {
		super();
		this.webResourceManager = webResourceManager;
	}

	@Override
	public List<IssueAction> getActions(Issue currentIssue, User currentUser) {
		
		// forces the download of all the resources when tab is selected.
		webResourceManager.requireResource("ca.appbox.jira.issue-dependency-viewer:foresight-resources");
		
		final List<IssueAction> actions = new ArrayList<IssueAction>();
		actions.add(new IssueAction() {
			
			@Override
			public boolean isDisplayActionAllTab() {
				return false;
			}
			
			@Override
			public Date getTimePerformed() {
				return new Date();
			}
			
			@Override
			public String getHtml() {
				return issueTabPanelModuleDescriptor.getHtml(ISSUE_TAB_PANEL_VELOCITY_TEMPLATE);
			}
		});
		return actions;
	}

	@Override
	public void init(IssueTabPanelModuleDescriptor issueTabPanelModuleDescriptor) {
		this.issueTabPanelModuleDescriptor = issueTabPanelModuleDescriptor;
	}

	@Override
	public boolean showPanel(Issue currentIssue, User currentUser) {
		return true;
	}
}
