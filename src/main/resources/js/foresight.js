var pluginSettings;

function update_plugin_settings() {
	AJS.$.get(contextPath + "/plugins/servlet/foresight-settings", function(data) {
		pluginSettings = JSON.parse(data);
	});
}

function update_description_types() {
	var descriptionType = AJS.$('#dependencyDescriptionTypes').val();
	d3.selectAll(".foresight-text").text(function(data) {
		var linkLabel;
		switch (descriptionType) {
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
	
	update_plugin_settings();
	  
	var issue_id=AJS.$("input[name=id]").val();
	var includeInwardLinks=AJS.$("#issue-dependency-viewer-form input[name=includeInward]").is(':checked');
	var includeOutwardLinks=AJS.$("#issue-dependency-viewer-form input[name=includeOutward]").is(':checked');

	AJS.$.get(contextPath + "/plugins/servlet/foresight-dependency-graph" 
			+ "?currentIssueId="+issue_id
			+ "&includeOutward="+includeOutwardLinks
			+ "&includeInward="+includeInwardLinks, function(data) {
		
		// clean up old svg object
		d3.select("#issue-dependency-viewer-graph").remove();
		var container = d3.select('#issue-dependency-viewer-graph-container');
		  
		var svg = container.append('svg:svg')
			.attr("id", "issue-dependency-viewer-graph")
			.attr("width", 1000)
			.attr("height", 500);
		  
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
		    .gravity(.05)
		    .distance(100)
		    .on("tick", tick)
		    .start();

		var node_drag = d3.behavior.drag()
			.on("dragstart", dragstart)
			.on("drag", dragmove)
			.on("dragend", dragend);
		
	    function dragstart(d, i) {
	        force.stop();
	    }

	    function dragmove(d, i) {
	        d.px += d3.event.dx;
	        d.py += d3.event.dy;
	        d.x += d3.event.dx;
	        d.y += d3.event.dy; 
	        tick();
	    }

	    function dragend(d, i) {
	        d.fixed = true; // of course set the node to fixed so the force doesn't include the node in its auto positioning stuff
	        tick();
	        force.resume();
	    }
	    
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
	        .attr("class", "foresight-path-link")
	        .attr("marker-end", function(d) { return "url(#" + d.type + ")"; });

		var circle = svg.append("svg:g").selectAll("circle")
		    .data(force.nodes())
		    .enter().append("svg:circle")
		    .attr("class", "foresight-circle")
		    .attr("fill", function(data) {
		    	if (issue_id == data.key) {
		    		return "#4552E6";
		    	}
		    	else {
		    		return pluginSettings.nodecolors[data.type];
		    	}
		    	})
		    .attr("r", 7)
		    .call(node_drag);

		var labels = svg.selectAll('text')
		    .data(graph.links)
		  .enter().append('text')
		    .attr("x", function(d) { return (d.source.y + d.target.y) / 2; }) 
		    .attr("y", function(d) { return (d.source.x + d.target.x) / 2; }) 
		    .attr("text-anchor", "middle")
		    .attr("class", "foresight-text");
			
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
		    .attr("class", "foresight-shadow")
		    .text(function(d) { return d.name; });
		
		text.append("svg:text")
		    .attr("x", 8)
		    .attr("y", ".31em")
		    .text(function(d) { return d.name; })
			.append("svg:title")
			.text(function(d) { return d.summary; });
		
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

AJS.$(document).ready(function() {
	
	// on change functions of the show inward/outward checkboxes
	AJS.$("#issue-dependency-viewer-form input[name=includeInward]").change(function(){
		show_graph();
	});
	AJS.$("#issue-dependency-viewer-form input[name=includeOutward]").change(function(){
		show_graph();
	});
	
	// on change function for the description types drop-down.
	AJS.$("#dependencyDescriptionTypes").change(function(){
		update_description_types();
	});
});