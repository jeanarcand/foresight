package ca.appbox.jira.plugins.issuedependencyviewer;

import java.util.List;

import ca.appbox.jira.plugins.issuedependencyviewer.graph.Graph;
import ca.appbox.jira.plugins.issuedependencyviewer.graph.Link;
import ca.appbox.jira.plugins.issuedependencyviewer.graph.Node;

/**
 * Json related utilities.
 * 
 * @author Jean Arcand
 */
public class JsonUtils {

	/**
	 * Poor man's implementation of Gson.
	 * 
	 * @param graph The graph to generate the json.
	 * 
	 * @return Json string representation of the graph.
	 */
	public static String toJson(Graph graph) {
	
		StringBuilder json = new StringBuilder();
		
		json.append("{")
			.append("  \"nodes\":[");
		
		List<Node> nodes = graph.getNodes();
		for (int i=0;i<nodes.size();i++) {

			json.append("{\"key\":")
				.append(nodes.get(i).getId())
			    .append(",\"name\":\"")
				.append(nodes.get(i).getName())
				.append("\",\"group\":\"")
				.append(nodes.get(i).getGroup())
				.append("\"}");
			
			if (i != nodes.size() - 1) {
				json.append(",");
			}
		}
		
		json.append("  ],");
		json.append("  \"links\":[");
		
		List<Link> links = graph.getLinks();
		for (int i=0;i<links.size();i++) {
			
			json.append("{\"source\":")
				.append(nodes.indexOf(new Node(links.get(i).getSource(),null, null)))
				.append(",\"target\":")
				.append(nodes.indexOf(new Node(links.get(i).getTarget(), null, null)))
				.append(",\"type\":\"")
				.append(links.get(i).getValue())
				.append("\"}");
			
			if (i != links.size() - 1) {
				json.append(",");
			}
		}
		
		json.append("  ]")
			.append("}");
		
		return json.toString();
	}
}