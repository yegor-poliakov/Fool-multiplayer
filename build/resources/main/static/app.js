const baseURL = "http://localhost:8080/";
/*const baseURL = "https://foolkyivbased.herokuapp.com/";*/
var deckID = 0;
function render(gameState) {
    deckID = gameState.deckID;
    var cardsRemaining = gameState.remainingCards;
    var cardCounter = $('#cards-remaining');
    cardCounter.empty();
    var trump = gameState.trump;
    if(cardsRemaining != 0){
        var remaining = "";
        remaining += `<div class='cards-remaining'>Cards to go: ${cardsRemaining}</div>`;
        cardCounter.append(remaining);
    } else if (!(trump == undefined)) {
        var remaining = "";
        remaining += `<div class='cards-remaining'>Trump: ${trump}</div>`;
        cardCounter.append(remaining);
    }

    var trumpLayer = $('#trump-layer');
    trumpLayer.empty();
    var trumpDeck = "";
    if(gameState.remainingCards > 0){
        var trump = gameState.trumpCard;
        trumpDeck += `<div id='trump-card' class='trump-card ${trump}'></div>`;
    }
    if (gameState.remainingCards > 1){
        trumpDeck += `<div class='deck-card'></div>`;
    }
    trumpLayer.append(trumpDeck);

    var firstPlayerHand = $("#firstPlayerHand");
    firstPlayerHand.empty();
    var endButton1 = $("#end-button1");
    endButton1.empty();
    if(gameState.firstPlayerActive){
        text = "<div class='button' onclick='makeMove(0, -1)'></div>";
        endButton1.append(text);
    }
    var PlayerOneRow = gameState.firstPlayerCards.length;
    for (var column = 0; column < PlayerOneRow; column++) {
        var cardClass = gameState.firstPlayerCards[column];
        var playerNumber = gameState.firstPlayerNumber;
        var playability = "";
        var clickable = "";
        var firstPlayerPosition = "";
        var calculateFirstPosition = 200 + column * (652 / PlayerOneRow);
        if (PlayerOneRow < 6){
            calculateFirstPosition = 200 + column * 110;
        }

        if (gameState.firstPlayerActive){
            playability += "playable";
            clickable += `onclick='makeMove(${playerNumber}, ${column})'`;
        }
        firstPlayerPosition += `style="left: ${calculateFirstPosition}px; margin-top: 0.8%; z-index=${column}"`;
        text = `<div class='card visible ${cardClass} ${playability}' ${clickable} ${firstPlayerPosition}></div>`;
        firstPlayerHand.append(text);
    }

    var firstPlayerTable = $('#firstPlayerTable');
    var secondPlayerTable = $('#secondPlayerTable');
    firstPlayerTable.empty();
    secondPlayerTable.empty();
    var currentTable;
    var spareTable;
    if (gameState.firstPlayerOffender){
        currentTable = firstPlayerTable;
        spareTable = secondPlayerTable;
    } else {
        currentTable = secondPlayerTable;
        spareTable = firstPlayerTable;
    }
    var tableLength = gameState.table.length;
    for (var i = 0; i < tableLength; i++){
        var cardClass = gameState.table[i];
        var tablePosition = "";
        var calculateTablePosition;
        if (tableLength % 2 == 0){
            calculateTablePosition = 200 + (i - i % 2) * (652 / 2 * tableLength);
        }
        if (tableLength % 2 == 1){
            calculateTablePosition = 200 + (i - i % 2) * (652 / 2 * (tableLength + 1));
        }
        if (tableLength < 16){
            calculateTablePosition = 200 + (i - i % 2) * 110/2;
        }

        if (i % 2 === 0){
            tablePosition += `style="left: ${calculateTablePosition}px; margin-top: 10px; z-index=${i}"`;
            text = `<div class='card visible ${cardClass}' ${tablePosition}></div>`;
            currentTable.append(text);
        } else {
            tablePosition += `style="left: ${calculateTablePosition}px; margin-top: 10px; z-index=${i}"`;
            text = `<div class='card visible ${cardClass}' ${tablePosition}></div>`;
            spareTable.append(text);
        }
    }

    var secondPlayerHand = $("#secondPlayerHand");
    secondPlayerHand.empty();
    var endButton2 = $("#end-button2");
    endButton2.empty();
    if(gameState.secondPlayerActive){
        text = "<div class='button' onclick='makeMove(1, -1)'></div>";
        endButton2.append(text);
    }
    var PlayerTwoRow = gameState.secondPlayerCards.length;
    for (var columnTwo = 0; columnTwo < PlayerTwoRow; columnTwo++) {
        var cardClass = gameState.secondPlayerCards[columnTwo];
        var playerTwoNumber = gameState.secondPlayerNumber;
        var playability = "";
        var clickable = "";
        var secondPlayerPosition = "";
        var calculateSecondPosition = 200 + columnTwo * (652 / PlayerTwoRow);
        if (PlayerTwoRow < 6){
            calculateSecondPosition = 200 + columnTwo * 110;
        }
        text = `<div class='card visible ${cardClass} ${playability}' ${clickable} ${firstPlayerPosition}></div>`;
        secondPlayerPosition += `style="left: ${calculateSecondPosition}px; margin-top: 1%; z-index=${columnTwo}"`;
        if (gameState.secondPlayerActive){
           playability += "playable";
           clickable += `onclick='makeMove(${playerTwoNumber}, ${columnTwo})'`;
        }
        text = `<div class='card invisible' ${clickable} ${secondPlayerPosition}></div>`;
        secondPlayerHand.append(text);
    }

    if (gameState.stage === "Victory") {
        alert("You won!");

    } else if (gameState.stage === "Loss") {
        alert("Human player lost!");
    } else if (gameState.stage === "Draw"){
        alert("Tie!");
    }

    return false;
}

function newGame(){

    $.ajax({
        url: baseURL + "deck",
        crossDomain: true,
        type: "GET",
        success: render
    })
}

function makeMove(playerNumber, cardNumber) {
    var playerNumber = playerNumber;
    var cardNumber = cardNumber;

    $.ajax({
        url: baseURL + "deck",
        crossDomain: true,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            deckID: deckID,
            playerNumber: playerNumber,
            cardNumber: cardNumber,
        }),
        type: "POST",
        success: render
    })
}