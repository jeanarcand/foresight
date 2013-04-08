package ca.appbox.jira.plugins.issuedependencyviewer.graph;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;

public final class GraphBuilder {

	private IssueManager issueManager;
	private IssueLinkManager issueLinkManager;

	private List<Long> existingNodes = new ArrayList<Long>();
	
	public GraphBuilder(IssueManager issueManager,
			IssueLinkManager issueLinkManager) {
		
		this.issueManager = issueManager;
		this.issueLinkManager = issueLinkManager;
	}

	public Graph buildGraph(Long currentIssueId, boolean considerInwardDep, boolean considerOutwardDep) {
		
		final Graph graph = new Graph();

		MutableIssue issueObject = issueManager.getIssueObject(currentIssueId);
		
		addAdjacentNodesToGraph(graph, issueObject.getId(), considerInwardDep, considerOutwardDep);
		
//		issueManager.
		
//		issueLinkManager.getOutwardLinks(0);
		
//		graph.getNodes().add(new Node("GT-123", "1"));
//		graph.getNodes().add(new Node("GT-456", "2"));
//		graph.getNodes().add(new Node("GT-789", "3"));
		
//		graph.getLinks().add(new Link(0, 1, "resolved"));
//		graph.getLinks().add(new Link("GT-123", "GT-789", "resolved"));
//		graph.getLinks().add(new Link("GT-456", "GT-789", "resolved"));
//		graph.getLinks().add(new Link("GT-789", "GT-123", "resolved"));
		
		return graph;
	}

	private void addAdjacentNodesToGraph(Graph graph, Long issueId, boolean considerInwardDep, boolean considerOutwardDep) {

		if (!existingNodes.contains(issueId)) {
			MutableIssue issueObject = issueManager.getIssueObject(issueId);
			Node newNode = new Node(issueId, issueObject.getKey(),"1");
			existingNodes.add(issueId);
			graph.addNode(newNode);
		}
		
		if (considerOutwardDep) {
			for (IssueLink currentOutwardLink : issueLinkManager.getOutwardLinks(issueId)) {
				
				if (!currentOutwardLink.isSystemLink()) {
					if (!existingNodes.contains(currentOutwardLink.getDestinationId())) {
						addAdjacentNodesToGraph(graph, currentOutwardLink.getDestinationId(), considerInwardDep, considerOutwardDep);
					}
					
					Link potentialNewLink = new Link(currentOutwardLink.getSourceId(),currentOutwardLink.getDestinationId(),"resolved");
					
					if (!graph.containsLink(potentialNewLink)) {
						graph.addLink(potentialNewLink);
					}
				}
			}
		}
		
		if (considerInwardDep) {
			for (IssueLink currentInwardLink : issueLinkManager.getInwardLinks(issueId)) {

				if (!currentInwardLink .isSystemLink()) {
					if (!existingNodes.contains(currentInwardLink.getSourceId())) {
						addAdjacentNodesToGraph(graph, currentInwardLink.getSourceId(), considerInwardDep, considerOutwardDep);
					}
					
					Link potentialNewLink = new Link(currentInwardLink.getDestinationId(),currentInwardLink.getSourceId(),"resolved");
					
					if (!graph.containsLink(potentialNewLink)) {
						graph.addLink(potentialNewLink);
					}
				}
			}
		}
	}
}
