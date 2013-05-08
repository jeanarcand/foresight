package ca.appbox.jira.plugins.issuedependencyviewer.graph;

import com.atlassian.jira.issue.MutableIssue;


/**
 * Node of the graph (jira issue).
 * 
 * @author Jean Arcand
 *
 */
public final class Node {

	private final MutableIssue correspondingIssue;
	
	public Node(MutableIssue issue) {
		super();
		this.correspondingIssue = issue;
	}

	public String getName() {
		return this.correspondingIssue.getKey();
	}

	public String getGroup() {
		return "1";
	}

	public Long getId() {
		return this.correspondingIssue.getId();
	}
	
	public String getType() {
		return this.correspondingIssue.getIssueTypeObject().getName();
	}

	public String getSummary() {
		return this.correspondingIssue.getSummary();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((correspondingIssue.getId() == null) ? 0 : correspondingIssue.getId()
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (correspondingIssue.getId() == null) {
			if (other.correspondingIssue.getId() != null)
				return false;
		} else if (!correspondingIssue.getId().equals(other.correspondingIssue.getId()))
			return false;
		return true;
	}
}
