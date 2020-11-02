let wsUri = "ws://localhost:8080/websocket";
let output;
let websocket;
let categorizedData = [];
let goingToRender = false;

function init() {
    output = document.getElementById("output");
    initWebSocket();
}

// from here: https://stackoverflow.com/a/57051556/718849
const groupBy = (arr, key) =>
    arr.reduce(
        (r, v, _, __, k = v[key]) => ((r[k] || (r[k] = [])).push(v), r),
        {}
    );

function initWebSocket() {
    websocket = new WebSocket(wsUri);
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
}

function onOpen(evt) {
    console.log("CONNECTED");
    doSend("Connect Websocket");
}

function onClose(evt) {
    console.log("DISCONNECTED");
    doSend("Disconnect Websocket");
}

function onMessage(evt) {
    categorizedData.push(JSON.parse(evt.data));
    if (!goingToRender) {
        goingToRender = true;
        setTimeout(createCharts, 1500);
    }
}

let monthNames = {
    "JANUARY": 1,
    "FEBRUARY": 2,
    "MARCH": 3,
    "APRIL": 4,
    "MAY": 5,
    "JUNE": 6,
    "JULY": 7,
    "AUGUST": 8,
    "SEPTEMBER": 9,
    "OCTOBER": 10,
    "NOVEMBER": 11,
    "DECEMBER": 12
};

let categoryColors = {
    "SALARY": "#5da5da",
    "MORTGAGE": "#faa43a",
    "TAX": "#60bd68",
    "INSURANCE": "#f17cb0",
    "FOOD": "#b2912f",
    "UTILITIES": "#b276b2",
    "MEDICAL": "#3e95cd",
    "SUBSCRIPTIONS": "#8e5ea2",
    "SPORT": "#3cba9f",
    "SAVINGS": "#e8c3b9",
    "REST": "#c45850"
};

function createCharts() {
    const elements = document.getElementsByTagName('canvas');
    while (elements.length > 0) elements[0].remove();

    let grouped = groupBy(categorizedData, 'month');
    let months = Object.keys(grouped).sort((a,b) => monthNames[a] - monthNames[b]);
    months
        .map((key, index) => [index, grouped[key]])
        .forEach(pair => {
            let afBij = splitData(pair[1]);
            drawChart(afBij[0], "Af, " + months[pair[0]]);
            drawChart(afBij[1], "Bij, " + months[pair[0]]);
        });

    goingToRender = false;
}

function splitData(data) {
    let af = data.filter(li => li['transactionType'] === "AF");
    let bij = data.filter(li => li['transactionType'] === "BIJ");

    return [af, bij];
}

function drawChart(data, label) {
    let grouped = groupBy(data, 'category');
    console.log(label);
    console.log(grouped);
    let categories = Object.keys(grouped);
    let values = categories
        .map(key => grouped[key])
        .map(group => group.reduce((a, c) => a + c.amount, 0));
    let total = values.reduce((a, c) => a + c, 0);

    let totalFormatted = (Math.round(total * 100) / 100).toFixed(2);
    let valuesFormatted = values
        .map(num => (Math.round(num * 100) / 100).toFixed(2));

    let thisChartBackgroundColors = categories.map(category => categoryColors[category]);

    let div = document.createElement("div");

    let canvas = document.createElement("canvas");
    canvas.id = uuidv4();
    canvas.width = 400;
    canvas.height = 100;

    div.appendChild(canvas);
    let table = document.createElement('table')

    div.appendChild(table);

    let tableData = {
        headings: Object.keys(data[0]),
        data: []
    };

    for ( let i = 0; i < data.length; i++ ) {
        tableData.data[i] = [];
        for (let p in data[i]) {
            if( data[i].hasOwnProperty(p) ) {
                tableData.data[i].push(data[i][p]);
            }
        }
    }

    new simpleDatatables.DataTable(table, {
        data: tableData
    });

    document.body.appendChild(div);

    new Chart(canvas, {
        type: 'pie',
        data: {
            labels: categories,
            datasets: [{
                backgroundColor: thisChartBackgroundColors,
                data: valuesFormatted
            }]
        },
        options: {
            title: {
                display: true,
                text: '€' + totalFormatted + ' (' + label + ')'
            }
        }
    });
}

function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

function onError(evt) {
    writeToScreen('<span style="color: red;">ERROR:</span> ' + evt.data);
}

function doSend(message) {
    console.log("SENT: " + message);
    websocket.send(message);
}

function writeToScreen(message) {
    let pre = document.createElement("p");
    pre.style.wordWrap = "break-word";
    pre.innerHTML = message;
    output.appendChild(pre);
}

window.addEventListener("load", init, false);