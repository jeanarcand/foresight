package ca.appbox.jira.plugins.issuedependencyviewer.graph;

/**
 * Link between 2 nodes (issue dependency).
 * 
 * @author Jean Arcand
 */
public final class Link {

	private final Long source;
	private final Long target;
	private final String value;
	
	public Link(Long source, Long target, String value) {
		super();
		this.source = source;
		this.target = target;
		this.value = value;
	}

	public Long getSource() {
		return source;
	}
	
	public Long getTarget() {
		return target;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}
