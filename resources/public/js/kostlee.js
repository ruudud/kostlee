var KOSTLEE_API_URL = '/daymoneys';

var $br = document.createElement('br');

var renderMoneySummary = function(amountOfMoney) {
  var $container = document.getElementById('summary-money');
  var $fact = $container.querySelector('.fact h2');
  var $desc = document.createElement('small');
  $desc.textContent = 'Sum opptjent';

  $fact.textContent = amountOfMoney + 'kr';
  $fact.appendChild($br.cloneNode());
  $fact.appendChild($desc);
};
var renderPeopleSummary = function(numberOfPeople) {
  var $container = document.getElementById('summary-people');
  var $fact = $container.querySelector('.fact h2');
  var $factContent = $container.querySelector('.factContent');
  var $desc = document.createElement('small');
  $desc.textContent = 'Aktive givere';

  $fact.textContent = numberOfPeople;
  $fact.appendChild($br.cloneNode());
  $fact.appendChild($desc);
  
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
  var valueFormatter = function(y) {
    return y + '%';
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
  var totalSum = data.reduce(function(memo, val) {
    return memo + val;
  }, 0);
  return data.map(function(dowValue, i) {
    var share = ((dowValue / totalSum) * 100).toFixed(2);
    return { label: dowMap[i], value: share};
  });
};

var graphAvg = function(data) {
  var $day = document.getElementById('avgPerDay');
  var $people = document.getElementById('avgPerPeople');
  $day.textContent = data.perDay.toFixed(2) + 'kr';
  $people.textContent = data.perPeople.toFixed(2) + 'kr';
};

var errorHandler = function(error) {
  console.error("Error when graphing data", error);
};

xhr.getJSON(KOSTLEE_API_URL)
   .then(formatData)
   .then(renderSummary)
   .then(graphHistoricData)
   .catch(errorHandler);

xhr.getJSON(KOSTLEE_API_URL + '?transform=per-weekday')
   .then(formatWeekdayData)
   .then(graphWeekdays)
   .catch(errorHandler);

xhr.getJSON(KOSTLEE_API_URL + '?transform=avg')
   .then(graphAvg)
   .catch(errorHandler);
