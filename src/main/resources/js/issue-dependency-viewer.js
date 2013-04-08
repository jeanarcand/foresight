	
function show_graph() {

	  var issue_key=AJS.$("input[name=id]").val();
	  AJS.$.get("/plugins/servlet/issue-dependency-viewer-servlet?currentIssueKey="+issue_key,
		function(data) {
			     
			var svg = d3.select("#dep-graph");
			var graph = JSON.parse(data);
			
			var links = graph.links;
			var nodes = graph.nodes;

			links.forEach(function(link) {
				  link.source = nodes[link.source];
				  link.target = nodes[link.target];
			});
			
			var force = d3.layout.force()
			    .nodes(nodes)
			    .links(links)
			    .size([900, 500])
			    .linkDistance(120)
			    .charge(-150)
			    .on("tick", tick)
			    .start();

			svg.append("svg:defs").selectAll("marker")
			    .data(["suit", "licensing", "resolved"])
			  .enter().append("svg:marker")
			    .attr("id", String)
			    .attr("viewBox", "0 -5 10 10")
			    .attr("refX", 15)
			    .attr("refY", -1.5)
			    .attr("markerWidth", 6)
			    .attr("markerHeight", 6)
			    .attr("orient", "auto")
			  .append("svg:path")
			    .attr("d", "M0,-5L10,0L0,5");

			var path = svg.append("svg:g").selectAll("path")
			    .data(force.links())
			  .enter().append("svg:path")
			    .attr("class", function(d) { return "link " + d.type; })
			    .attr("marker-end", function(d) { return "url(#" + d.type + ")"; });

			var circle = svg.append("svg:g").selectAll("circle")
			    .data(force.nodes())
			    .enter().append("svg:circle")
			    .attr("class", function(d) {
			    	if (issue_key == d.key) {
			    		return "circle-current";
			    	}
			    	else {
			    		return "circle";
			    	}
			    	})
			    .attr("r", 6)
			    .call(force.drag);

			var link = svg.append("svg:g").selectAll("g")
			    .data(force.nodes())
			    .enter().append("svg:g");
			
			var text = svg.append("svg:g").selectAll("g")
			    .data(force.nodes())
			  .enter().append("svg:g");
			
			text.append("svg:text")
			    .attr("x", 8)
			    .attr("y", ".31em")
			    .attr("class", "shadow")
			    .text(function(d) { return d.name; });
			
			text.append("svg:text")
			    .attr("x", 8)
			    .attr("y", ".31em")
			    .text(function(d) { return d.name; });

			function tick() {
			  path.attr("d", function(d) {
			    var dx = d.target.x - d.source.x,
			        dy = d.target.y - d.source.y,
			        dr = Math.sqrt(dx * dx + dy * dy);
			    return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
			  });

			  circle.attr("transform", function(d) {
			    return "translate(" + d.x + "," + d.y + ")";
			  });

			  text.attr("transform", function(d) {
			    return "translate(" + d.x + "," + d.y + ")";
			  });
			}
	  });
}