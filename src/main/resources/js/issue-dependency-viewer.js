function update_description_types() {
	var descriptionType = AJS.$('#dependencyDescriptionTypes').val();
	d3.selectAll(".dependency-link")
		.text(function(data) {
			var linkLabel;
			switch(descriptionType) {
			case 'none':
				linkLabel = "";
				break;
			case 'inward':
				linkLabel = data.inward;
				break;
			case 'outward':
				linkLabel = data.outward;
				break;	  
			}
			return linkLabel;
		});
}

function show_graph() {

	  var issue_id=AJS.$("input[name=id]").val();
	  var includeInwardLinks=AJS.$("#issue-dependency-viewer-form input[name=includeInward]").is(':checked');
	  var includeOutwardLinks=AJS.$("#issue-dependency-viewer-form input[name=includeOutward]").is(':checked');
	  AJS.$.get(contextPath + "/plugins/servlet/issue-dependency-viewer-servlet" 
			  + "?currentIssueId="+issue_id
			  + "&includeOutward="+includeOutwardLinks
			  + "&includeInward="+includeInwardLinks,
		function(data) {
			
		  	// clean up old svg object
		    d3.select("#issue-dependency-viewer-graph").remove();
		    var container = d3.select('#issue-dependency-viewer-graph-container');
		   
		    var svg = container.append('svg:svg')
		        .attr("id", "issue-dependency-viewer-graph");
		  
			var graph = JSON.parse(data);
			
			var links = graph.links;
			var nodes = graph.nodes;
			
			// link the actual nodes
			links.forEach(function(link) {
				  link.source = nodes[link.source];
				  link.target = nodes[link.target];
			});
			
			// create the layout
			var force = d3.layout.force()
			    .nodes(nodes)
			    .links(links)
			    .size([900, 500])
			    .linkDistance(120)
			    .charge(-150)
			    .on("tick", tick)
			    .start();

			svg.append("svg:defs").selectAll("marker")
			    .data(["resolved"])
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
			    .attr("class", function(d) { return "link normal" })
			    .attr("marker-end", function(d) { return "url(#" + d.type + ")"; });

			var circle = svg.append("svg:g").selectAll("circle")
			    .data(force.nodes())
			    .enter().append("svg:circle")
			    .attr("class", function(d) {
			    	if (issue_id == d.key) {
			    		return "circle-current";
			    	}
			    	else {
			    		return "circle";
			    	}
			    	})
			    .attr("r", 7)
			    .call(force.drag);

			var labels = svg.selectAll('text')
			    .data(graph.links)
			  .enter().append('text')
			    .attr("x", function(d) { return (d.source.y + d.target.y) / 2; }) 
			    .attr("y", function(d) { return (d.source.x + d.target.x) / 2; }) 
			    .attr("text-anchor", "middle")
			    .attr("class", "dependency-link");
//			    .text(function(d) {return d.outward;});
				
			var link = svg.append("svg:g").selectAll("g")
			    .data(force.nodes())
			    .enter().append("svg:g");
			
			var text = svg.append("svg:g").selectAll("g")
			    .data(force.nodes())
			  .enter().append("svg:g");
			
			// show the task key
			text.append("svg:text")
			    .attr("x", 8)
			    .attr("y", ".31em")
			    .attr("class", "shadow")
			    .text(function(d) { return d.name; });
			
			text.append("svg:text")
			    .attr("x", 8)
			    .attr("y", ".31em")
			    .text(function(d) { return d.name; });

			update_description_types();
			
			// curve the arcs between the nodes to correctly show cycles.
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
			  
			  labels.attr("x", function(d) { return (d.source.x + d.target.x) / 2; }) 
		        .attr("y", function(d) { return (d.source.y + d.target.y) / 2; }) 
			}
	  });
}

// on change functions of the show inward/outward checkboxes
AJS.$(document).ready(function() {
	AJS.$("#issue-dependency-viewer-form input[name=includeInward]").change(function(){
		show_graph();
	});
	AJS.$("#issue-dependency-viewer-form input[name=includeOutward]").change(function(){
		show_graph();
	});
	AJS.$("#dependencyDescriptionTypes").change(function(){
		update_description_types();
	});
});