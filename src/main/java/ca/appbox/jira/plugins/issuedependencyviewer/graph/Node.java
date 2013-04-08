package ca.appbox.jira.plugins.issuedependencyviewer.graph;

public final class Node {

	private final Long id;
	private final String name;
	private final String group;
	
	public Node(Long id, String name, String group) {
		super();
		this.id = id;
		this.name = name;
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
