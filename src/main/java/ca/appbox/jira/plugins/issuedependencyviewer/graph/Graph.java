package ca.appbox.jira.plugins.issuedependencyviewer.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Graph {

	private List<Node> nodes = new ArrayList<Node>();
	private List<Link> links = new ArrayList<Link>();

	public void addNode(Node newNode) {
		if (!this.containsNode(newNode.getName())) {
			nodes.add(newNode);
		}
	}
	
	public boolean containsNode(String nodeKey) {
		return this.nodes.contains(nodeKey);
	}

	public List<Node> getNodes() {
		return Collections.unmodifiableList(this.nodes);
	}
	
	public void addLink(Link newLink) {
		if (!this.containsLink(newLink)) {
			links.add(newLink);
		}
	}
	
	public boolean containsLink(Link link) {
		return this.links.contains(link);
	}
	
	public List<Link> getLinks() {
		return Collections.unmodifiableList(this.links);
	}
}
