package ca.appbox.jira.plugins.issuedependencyviewer.graph;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;

/**
 * Builder of the dependency graph.
 * 
 * @author Jean Arcand
 */
public final class GraphBuilder {

	private IssueManager issueManager;
	private IssueLinkManager issueLinkManager;
	private boolean includeInwardLinks = true;
	private boolean includeOutwardLinks = true;

	private List<Long> existingNodes = new ArrayList<Long>();
	
	public GraphBuilder(IssueManager issueManager,
			IssueLinkManager issueLinkManager) {
		
		this.issueManager = issueManager;
		this.issueLinkManager = issueLinkManager;
	}

	public Graph buildGraph(Long currentIssueId, boolean considerInwardDep, boolean considerOutwardDep) {
		
		final Graph graph = new Graph();

		MutableIssue issueObject = issueManager.getIssueObject(currentIssueId);
		
		addAdjacentNodesToGraph(graph, issueObject.getId());
		
		return graph;
	}

	private void addAdjacentNodesToGraph(Graph graph, Long issueId) {

		if (!existingNodes.contains(issueId)) {
			MutableIssue issueObject = issueManager.getIssueObject(issueId);
			Node newNode = new Node(issueId, issueObject.getKey(),"1");
			existingNodes.add(issueId);
			graph.addNode(newNode);
		}
		
		if (this.includeOutwardLinks) {
			for (IssueLink currentOutwardLink : issueLinkManager.getOutwardLinks(issueId)) {
				
				if (!currentOutwardLink.isSystemLink()) {
					if (!existingNodes.contains(currentOutwardLink.getDestinationId())) {
						addAdjacentNodesToGraph(graph, currentOutwardLink.getDestinationId());
					}
					
					Link potentialNewLink = new Link(currentOutwardLink.getSourceId(),currentOutwardLink.getDestinationId(),"resolved");
					
					if (!graph.containsLink(potentialNewLink)) {
						graph.addLink(potentialNewLink);
					}
				}
			}
		}
		
		if (this.includeInwardLinks) {
			for (IssueLink currentInwardLink : issueLinkManager.getInwardLinks(issueId)) {

				if (!currentInwardLink .isSystemLink()) {
					if (!existingNodes.contains(currentInwardLink.getSourceId())) {
						addAdjacentNodesToGraph(graph, currentInwardLink.getSourceId());
					}
					
					Link potentialNewLink = new Link(currentInwardLink.getSourceId(),currentInwardLink.getDestinationId(),"resolved");
					
					if (!graph.containsLink(potentialNewLink)) {
						graph.addLink(potentialNewLink);
					}
				}
			}
		}
	}
	
	public GraphBuilder setIncludeInwardLinks(boolean includeInwardLinks) {
		this.includeInwardLinks = includeInwardLinks;
		return this;
	}
	
	public GraphBuilder setIncludeOutwardLinks(boolean includeOutwardLinks) {
		this.includeOutwardLinks = includeOutwardLinks;
		return this;
	}
}