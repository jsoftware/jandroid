NB. webd3
NB.
NB. These demos are from https://github.com/mbostock/d3/wiki/Gallery
NB. and demonstrate that webview can run d3 scripts.
NB.
NB. This means that a J application could display graphics by
NB. creating appropriate d3 commands, then calling webview.

coclass 'demoweb3d'

D3=: file2url jpath '~addons/ide/ja/js/d3.v3.min.js'
F0=: file2url jpath '~addons/ide/ja/data/data.tsv'

onStart=: 3 : 0
svgview SS
)

create=: 3 : 0
SS=: y
)

NB. =========================================================
svgview=: 3 : 0
's t'=. y
wd 'pc svgview;wh _1 _1;cc w webview'
h=. '<!DOCTYPE html><html><head><meta charset="utf-8"/>',LF
h=. h,'<script type="text/javascript" src="',D3,'" charset="utf-8"></script>',LF
h=. h,'<style>',s,'</style>',LF
h=. h,'</head><body><script type="text/javascript">'
m=. h,t,'</script></body></html>'
wd 'pshow'
wd 'set w baseurl *', file2url jpath '~addons/'
wd 'set w html *',m
)

NB. =========================================================
NB. Multi-Series Line Chart
S0=: 0 : 0
body {
 font: 10px sans-serif;
}

.axis path,
.axis line {
 fill: none;
 stroke: #000;
 shape-rendering: crispEdges;
}

.x.axis path {
 display: none;
}

.line {
 fill: none;
 stroke: steelblue;
 stroke-width: 1.5px;
}
)

S0=: S0;(0 : 0) rplc 'data.tsv';F0
var margin = {top: 20, right: 80, bottom: 30, left: 50},
 width = 960 - margin.left - margin.right,
 height = 500 - margin.top - margin.bottom;

var parseDate = d3.time.format("%Y%m%d").parse;

var x = d3.time.scale()
 .range([0, width]);

var y = d3.scale.linear()
 .range([height, 0]);

var color = d3.scale.category10();

var xAxis = d3.svg.axis()
 .scale(x)
 .orient("bottom");

var yAxis = d3.svg.axis()
 .scale(y)
 .orient("left");

var line = d3.svg.line()
 .interpolate("basis")
 .x(function(d) { return x(d.date); })
 .y(function(d) { return y(d.temperature); });

var svg = d3.select("body").append("svg")
 .attr("width", width + margin.left + margin.right)
 .attr("height", height + margin.top + margin.bottom)
 .append("g")
 .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

d3.tsv("data.tsv", function(error, data) {
 color.domain(d3.keys(data[0]).filter(function(key) { return key !== "date"; }));

 data.forEach(function(d) {
 d.date = parseDate(d.date);
 });

 var cities = color.domain().map(function(name) {
 return {
 name: name,
 values: data.map(function(d) {
 return {date: d.date, temperature: +d[name]};
 })
 };
 });

 x.domain(d3.extent(data, function(d) { return d.date; }));

 y.domain([
 d3.min(cities, function(c) { return d3.min(c.values, function(v) { return v.temperature; }); }),
 d3.max(cities, function(c) { return d3.max(c.values, function(v) { return v.temperature; }); })
 ]);

 svg.append("g")
 .attr("class", "x axis")
 .attr("transform", "translate(0," + height + ")")
 .call(xAxis);

 svg.append("g")
 .attr("class", "y axis")
 .call(yAxis)
 .append("text")
 .attr("transform", "rotate(-90)")
 .attr("y", 6)
 .attr("dy", ".71em")
 .style("text-anchor", "end")
 .text("Temperature (ºF)");

 var city = svg.selectAll(".city")
 .data(cities)
 .enter().append("g")
 .attr("class", "city");

 city.append("path")
 .attr("class", "line")
 .attr("d", function(d) { return line(d.values); })
 .style("stroke", function(d) { return color(d.name); });

 city.append("text")
 .datum(function(d) { return {name: d.name, value: d.values[d.values.length - 1]}; })
 .attr("transform", function(d) { return "translate(" + x(d.value.date) + "," + y(d.value.temperature) + ")"; })
 .attr("x", 3)
 .attr("dy", ".35em")
 .text(function(d) { return d.name; });
});
)

NB. =========================================================
NB. Chord Diagram
S1=: 0 : 0
body {
 font: 10px sans-serif;
}
.chord path {
 fill-opacity: .67;
 stroke: #000;
 stroke-width: .5px;
}
)

S1=: S1;0 : 0
var matrix = [
 [11975, 5871, 8916, 2868],
 [ 1951, 10048, 2060, 6171],
 [ 8010, 16145, 8090, 8045],
 [ 1013, 990, 940, 6907]
];

var chord = d3.layout.chord()
 .padding(.05)
 .sortSubgroups(d3.descending)
 .matrix(matrix);

var width = 960,
 height = 500,
 innerRadius = Math.min(width, height) * .41,
 outerRadius = innerRadius * 1.1;

var fill = d3.scale.ordinal()
 .domain(d3.range(4))
 .range(["#000000", "#FFDD89", "#957244", "#F26223"]);

var svg = d3.select("body").append("svg")
 .attr("width", width)
 .attr("height", height)
 .append("g")
 .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

svg.append("g").selectAll("path")
 .data(chord.groups)
 .enter().append("path")
 .style("fill", function(d) { return fill(d.index); })
 .style("stroke", function(d) { return fill(d.index); })
 .attr("d", d3.svg.arc().innerRadius(innerRadius).outerRadius(outerRadius))
 .on("mouseover", fade(.1))
 .on("mouseout", fade(1));

var ticks = svg.append("g").selectAll("g")
 .data(chord.groups)
 .enter().append("g").selectAll("g")
 .data(groupTicks)
 .enter().append("g")
 .attr("transform", function(d) {
 return "rotate(" + (d.angle * 180 / Math.PI - 90) + ")"
 + "translate(" + outerRadius + ",0)";
 });

ticks.append("line")
 .attr("x1", 1)
 .attr("y1", 0)
 .attr("x2", 5)
 .attr("y2", 0)
 .style("stroke", "#000");

ticks.append("text")
 .attr("x", 8)
 .attr("dy", ".35em")
 .attr("transform", function(d) { return d.angle > Math.PI ? "rotate(180)translate(-16)" : null; })
 .style("text-anchor", function(d) { return d.angle > Math.PI ? "end" : null; })
 .text(function(d) { return d.label; });

svg.append("g")
 .attr("class", "chord")
 .selectAll("path")
 .data(chord.chords)
 .enter().append("path")
 .attr("d", d3.svg.chord().radius(innerRadius))
 .style("fill", function(d) { return fill(d.target.index); })
 .style("opacity", 1);

// Returns an array of tick angles and labels, given a group.
function groupTicks(d) {
 var k = (d.endAngle - d.startAngle) / d.value;
 return d3.range(0, d.value, 1000).map(function(v, i) {
 return {
 angle: v * k + d.startAngle,
 label: i % 5 ? null : v / 1000 + "k"
 };
 });
}

// Returns an event handler for fading a given chord group.
function fade(opacity) {
 return function(g, i) {
 svg.selectAll(".chord path")
 .filter(function(d) { return d.source.index != i && d.target.index != i; })
 .transition()
 .style("opacity", opacity);
 };
}
)

NB. =========================================================
NB. Voronoi Diagram
S2=: 0 : 0
path {
 stroke: #000;
}

path:first-child {
 fill: yellow !important;
}

circle {
 fill: #fff;
 stroke: #000;
 pointer-events: none;
}

.PiYG .q0-9{fill:rgb(197,27,125)}
.PiYG .q1-9{fill:rgb(222,119,174)}
.PiYG .q2-9{fill:rgb(241,182,218)}
.PiYG .q3-9{fill:rgb(253,224,239)}
.PiYG .q4-9{fill:rgb(247,247,247)}
.PiYG .q5-9{fill:rgb(230,245,208)}
.PiYG .q6-9{fill:rgb(184,225,134)}
.PiYG .q7-9{fill:rgb(127,188,65)}
.PiYG .q8-9{fill:rgb(77,146,33)}
)

S2=: S2;0 : 0
var width = 960,
 height = 500;

var vertices = d3.range(100).map(function(d) {
 return [Math.random() * width, Math.random() * height];
});

var svg = d3.select("body").append("svg")
 .attr("width", width)
 .attr("height", height)
 .attr("class", "PiYG")
 .on("mousemove", function() { vertices[0] = d3.mouse(this); redraw(); });

var path = svg.append("g").selectAll("path");

svg.selectAll("circle")
 .data(vertices.slice(1))
 .enter().append("circle")
 .attr("transform", function(d) { return "translate(" + d + ")"; })
 .attr("r", 2);

redraw();

function redraw() {
 path = path.data(d3.geom.voronoi(vertices).map(function(d) { return "M" + d.join("L") + "Z"; }), String);
 path.exit().remove();
 path.enter().append("path").attr("class", function(d, i) { return "q" + (i % 9) + "-9"; }).attr("d", String);
 path.order();
}
)

NB. =========================================================
wd 'activity ', (>S0 conew 'demoweb3d'), ' fs'
wd 'activity ', (>S1 conew 'demoweb3d'), ' fs'
wd 'activity ', (>S2 conew 'demoweb3d'), ' fs'



