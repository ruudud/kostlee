var KOSTLEE_API_URL = '/daymoneys';

var renderMoneySummary = function(amountOfMoney) {
  var $container = document.getElementById('summary-money');
  var $fact = $container.querySelector('.fact');
  $fact.textContent = amountOfMoney;
};
var renderPeopleSummary = function(numberOfPeople) {
  var $container = document.getElementById('summary-people');
  var $fact = $container.querySelector('.fact');
  var $factContent = $container.querySelector('.factContent');

  $fact.textContent = numberOfPeople;
  
  var $man = document.createElement('img');
  $man.src = 'gfx/man.svg';
  $man.className = 'icon-man';
  for (var i = 0; i<numberOfPeople; i++) {
    $factContent.appendChild($man.cloneNode());
  }
};

var graphHistoricData = function(data) {
  var dateFormater = function(msTimestamp) {
    var date = new Date(msTimestamp);
    return date.toLocaleDateString()
  };
  var graph = new Morris.Line({
    element: 'graph-historic',
    data: data,
    xkey: 'date',
    hideHover: true,
    resize: true,
    lineColors: ['#222', '#666'],
    ykeys: ['amountOfMoney', 'numberOfPeople'],
    labels: ['Opptjent beløp', 'Spillere'],
    dateFormat: dateFormater
  });
};
var renderSummary = function(data) {
  var mostRecent = data[data.length - 1];
  var people = mostRecent.numberOfPeople;
  var money = mostRecent.amountOfMoney;
  
  renderPeopleSummary(people);
  renderMoneySummary(money);
  return data;
};

var formatData = function(data) {
  return data.map(function(obj) {
    var date = +(new Date(obj.date));
    return {
      date: date,
      amountOfMoney: obj.amount,
      numberOfPeople: obj.people
     };
  });
};

var graphWeekdays = function(data) {
  var valueFormatter = function() {
    return '';
  };
  var graph = new Morris.Donut({
    element: 'graph-dayOfWeek',
    data: data,
    formatter: valueFormatter
  });
};
var formatWeekdayData = function(data) {
  var dowMap = ['Mandag', 'Tirsdag', 'Onsdag', 
    'Torsdag', 'Fredag', 'Lørdag', 'Søndag'];
  return data.map(function(dowValue, i) {
    return { label: dowMap[i], value: dowValue};
  });
};

var errorHandler = function(error) {
  console.error("Error when graphing data", error);
};

xhr.getJSON(KOSTLEE_API_URL)
   .then(formatData)
   .then(renderSummary)
   .then(graphHistoricData)
   .catch(errorHandler);

xhr.getJSON(KOSTLEE_API_URL + '?weekday=1')
   .then(formatWeekdayData)
   .then(graphWeekdays)
   .catch(errorHandler);
