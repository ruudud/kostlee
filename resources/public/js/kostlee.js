var KOSTLEE_API_URL = 'http://localhost:3000/daymoneys';

var renderGraph = function(data, $el, name, color) {
  var _createYAxisEl = function($sibling) {
    var $parent = $sibling.parentNode;
    var $yAxis = document.createElement('div');
    $yAxis.className = 'rightAxisY';
    $parent.appendChild($yAxis);
    return $yAxis;
  };
  var graph = new Rickshaw.Graph( {
    element: $el, 
    renderer: 'line',
    series: [{
      color: color,
      name: name,
      data: data
    }]
  });
  var hoverDetail = new Rickshaw.Graph.HoverDetail({
    graph: graph
  });
  var axes = new Rickshaw.Graph.Axis.Time({
    graph: graph
  });
  var yAxis = new Rickshaw.Graph.Axis.Y({
    graph: graph,
    orientation: 'left',
    tickFormat: Rickshaw.Fixtures.Number.formatKMBT,
    element: _createYAxisEl($el),
  });
  graph.render();
  return graph;
};
var renderPeopleSummary = function(numberOfPeople) {
  console.log('num', numberOfPeople);
  var $el = document.getElementById('summary-people');
  var $man = document.createElement('span');
  $man.className = 'icon-man';
  for (var i = 0; i<numberOfPeople; i++) {
    $el.appendChild($man.cloneNode());
  }
};

var graphNumberOfPeople = function(data) {
  var $el = document.getElementById('graph-numberOfPeople');
  renderGraph(data[1], $el, 'Givere', '#c05020');
  return data;
};
var graphAmountOfMoney = function(data) {
  var $el = document.getElementById('graph-amountOfMoney');
  renderGraph(data[0], $el, 'Donert', '#6060c0');
};
var renderSummary = function(data) {
  var people = data[1][data[1].length - 1].y;
  var share = data[0][data[0].length - 1].y;
  
  renderPeopleSummary(people);
  return data;
};

var formatData = function(data) {
  var formatted = [ [], [] ];
  data.forEach(function(obj) {
    var date = +(new Date(obj.date))/1000;
    formatted[0].push({ x: date, y: Number(obj.amount) });
    formatted[1].push({ x: date, y: Number(obj.people) });
  });
  return formatted;
};

var xhrError = function(error) {
  console.error("Error when getting graph data", error);
};

xhr.getJSON(KOSTLEE_API_URL)
   .then(formatData)
   .then(renderSummary)
   .then(graphNumberOfPeople)
   .then(graphAmountOfMoney)
   .catch(xhrError);
