package ca.appbox.jira.plugins.issuedependencyviewer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.link.IssueLinkManager;

import ca.appbox.jira.plugins.issuedependencyviewer.graph.GraphBuilder;

/**
 * Servlet for all actions of the plugin.
 * 
 * @author Jean Arcand
 */
public final class IssueDependencyServlet extends HttpServlet {

	private static final long serialVersionUID = -5512021564484143035L;

	private IssueManager issueManager;
	private IssueLinkManager issueLinkManager;
	
	public IssueDependencyServlet(IssueManager issueManager, 
			IssueLinkManager issueLinkManager) {
		super();
		this.issueManager = issueManager;
		this.issueLinkManager = issueLinkManager;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String currentIssueKeyParameter = req.getParameter("currentIssueKey");
		
		// FIXME : accept those as input param.
		boolean considerInward = false;
		boolean considerOutward = true;
		
		// FIXME : further validations needed.
		if (currentIssueKeyParameter != null) {
			resp.getWriter().write(JsonUtils.toJson(
					new GraphBuilder(issueManager,issueLinkManager)
						.buildGraph(Long.valueOf(currentIssueKeyParameter),considerInward, considerOutward)));
		}
	}
}