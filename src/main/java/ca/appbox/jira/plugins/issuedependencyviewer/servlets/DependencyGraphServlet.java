package ca.appbox.jira.plugins.issuedependencyviewer.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.appbox.jira.plugins.issuedependencyviewer.graph.GraphBuilder;
import ca.appbox.jira.plugins.issuedependencyviewer.servlets.response.GraphResponseBuilder;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.link.IssueLinkManager;

/**
 * Servlet for everything related to the dependency graph.
 * 
 * @author Jean Arcand
 */
public final class DependencyGraphServlet extends HttpServlet {

	private static final String INCLUDE_OUTWARD_PARAM_KEY = "includeOutward";
	private static final String CURRENT_ISSUE_ID_PARAM_KEY = "currentIssueId";
	private static final String INCLUDE_INWARD_PARAM_KEY = "includeInward";
	private static final String INCLUDE_SYSTEM_LINKS = "includeSystemLinks";

	private static final long serialVersionUID = -5512021564484143035L;

	private IssueManager issueManager;
	private IssueLinkManager issueLinkManager;
	
	public DependencyGraphServlet(IssueManager issueManager, 
			IssueLinkManager issueLinkManager) {
		super();
		this.issueManager = issueManager;
		this.issueLinkManager = issueLinkManager;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Long currentIssueKeyParameter = parseLongParam(CURRENT_ISSUE_ID_PARAM_KEY, req.getParameterMap());
		
		boolean considerInward = parseBooleanParam(INCLUDE_INWARD_PARAM_KEY, req.getParameterMap());
		boolean considerOutward = parseBooleanParam(INCLUDE_OUTWARD_PARAM_KEY, req.getParameterMap());
		boolean includeSystemLinks = parseBooleanParam(INCLUDE_SYSTEM_LINKS, req.getParameterMap());

		// FIXME : further validations needed.
		if (currentIssueKeyParameter != null) {

			// build the graph
			GraphBuilder graphBuilder = new GraphBuilder(issueManager,issueLinkManager)
				.setIncludeInwardLinks(considerInward)
				.setIncludeOutwardLinks(considerOutward)
				.setIncludeSystemLinks(includeSystemLinks);
			
			// output the response
			String graphResponse = new GraphResponseBuilder().toJson(
					graphBuilder.buildGraph(Long.valueOf(currentIssueKeyParameter)));
			
			resp.getWriter().write(graphResponse.toString());
		}
	}

	//FIXME : move to parser/static util
	@SuppressWarnings("rawtypes")
	private boolean parseBooleanParam(String parameter, Map parameterMap) {
		
		boolean booleanValue = false;
		
		if (parameterMap.containsKey(parameter)) {
			try {
				String[] paramValues = (String[]) parameterMap.get(parameter);
				booleanValue = Boolean.parseBoolean(paramValues[0]);
			} catch (NumberFormatException nfe) {
				//
			}
		}
	
		return booleanValue;
	}

	//FIXME : move to parser/static util	
	@SuppressWarnings("rawtypes")
	private Long parseLongParam(String parameter, Map parameterMap) {

		Long longValue = null;
		
		if (parameterMap.containsKey(parameter)) {
			try {
				String[] paramValues = (String[]) parameterMap.get(parameter);
				longValue = Long.parseLong(paramValues[0]);
			} catch (NumberFormatException nfe) {
				//
			}
		}
			
		return longValue;
	}
}