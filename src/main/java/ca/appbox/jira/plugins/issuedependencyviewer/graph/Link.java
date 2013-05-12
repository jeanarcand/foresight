package ca.appbox.jira.plugins.issuedependencyviewer.graph;

import com.atlassian.jira.issue.link.IssueLink;

/**
 * Link between 2 nodes (issue dependency).
 * 
 * @author Jean Arcand
 */
public final class Link {

	private final IssueLink correspondingLink;

	public Link(IssueLink correspondingLink) {
		super();
		this.correspondingLink = correspondingLink;
	}

	public Long getSource() {
		return this.correspondingLink.getSourceId();
	}
	
	public Long getTarget() {
		return this.correspondingLink.getDestinationId();
	}

	public String getOutward() {
		return this.correspondingLink.getIssueLinkType().getOutward();
	}

	public String getInward() {
		return this.correspondingLink.getIssueLinkType().getInward();
	}
	
	public boolean isSystemLink() {
		return this.correspondingLink.isSystemLink();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((correspondingLink.getId() == null) ? 0 : correspondingLink.getId().hashCode());
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
		Link other = (Link) obj;
		if (correspondingLink.getId() == null) {
			if (other.correspondingLink.getId() != null)
				return false;
		} else if (!correspondingLink.getId().equals(other.correspondingLink.getId()))
			return false;
		return true;
	}
}
