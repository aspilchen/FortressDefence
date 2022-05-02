"use strict";

// Handle back-end server interactions fro game UI.
const myAppObj = new Vue({
    el: "#gameApp",
    data: {
        authorName: "(waiting for server...)",
        game: null,
        board: null,
    },

    methods: {
        newGame: makeNewGame,
        cheat1Cheese: send1CheeseCheat,
        cheatShowAll: sendShowAll,

        // Testing error handling
        getBadGame: testGetBadGame,
        getBadBoard: testGetBadBoard,
        doBadCheat: testDoBadCheat,
        doBadMove: testDoBadMove,
        clickCell: sendClick,

        locationMatches: function(loc, x, y) {
            return loc.x == x && loc.y == y;
        },
    }
});

// Have Axios send body as plain text (not JSON) when this config is passed to a POST request.
var plainTextConfig = {
    headers: { 'Content-Type': 'text/plain'},
    responseType: 'text'
};


// Refresh UI at start
$(document).ready(function() {
    loadAbout();
});

function loadAbout() {
    axios.get('/api/about', {})
        .then(function (response) {
            console.log("GET About returned:", response);
            myAppObj.authorName = response.data;

            alertOnWrongStatus("GET about", 200, response.status);
        })
        .catch(function (error) {
            console.log("GET About ERROR:", error);
        });
}

function makeNewGame() {
    axios.post('api/games', {})
        .then(function (response) {
            console.log("POST new game returned:", response);
            myAppObj.game = response.data;
            loadGameBoard();

            alertOnWrongStatus("POST games", 201, response.status);
        })
        .catch(function (error) {
            console.log("POST new game ERROR:", error);
        });
}

function loadGame() {
    axios.get('/api/games/' + myAppObj.game.gameNumber, {})
      .then(function (response) {
        console.log("Load game returned:", response);
        myAppObj.game = response.data;

        alertOnWrongStatus("GET Game", 200, response.status);
      })
      .catch(function (error) {
        console.log("Load game ERROR: ", error);
      });
}
function loadGameBoard() {
    axios.get('/api/games/' + myAppObj.game.gameNumber + "/board", {})
      .then(function (response) {
        console.log("Load Board returned: ", response);
        myAppObj.board = response.data;

        alertOnWrongStatus("GET board", 200, response.status);
      })
      .catch(function (error) {
        console.log("Load Board ERROR: ", error);
      });
}
function send1CheeseCheat() {
    axios.post('/api/games/' + myAppObj.game.gameNumber + "/cheatstate", "1_CHEESE", plainTextConfig)
        .then(function (response) {
            console.log("Cheat returned: ", response);
            loadGame();
            alertOnWrongStatus("POST Cheese Cheat", 202, response.status);
        })
        .catch(function (error) {
            console.log("Cheat ERROR: ", error);
        });
}
function sendShowAll() {
    axios.post('/api/games/' + myAppObj.game.gameNumber + "/cheatstate", "SHOW_ALL", plainTextConfig)
        .then(function (response) {
            console.log("Cheat returned: ", response);
            loadGameBoard();
            alertOnWrongStatus("POST Show All Cheat", 202, response.status);
        })
        .catch(function (error) {
            console.log("Cheat ERROR: ", error);
        });
}


// Source: https://www.w3schools.com/graphics/game_sound.asp
var mySound = new sound("res/BONK.WAV");
function sound(src) {
    this.sound = document.createElement("audio");
    this.sound.src = src;
    this.sound.setAttribute("preload", "auto");
    this.sound.setAttribute("controls", "none");
    this.sound.style.display = "none";
    document.body.appendChild(this.sound);
    this.play = function(){
        this.sound.play();
    }
    this.stop = function(){
        this.sound.pause();
    }
}
function playSound() {
    mySound.play();
}


// Move
function sendClick(rowIdx, colIdx) {
    console.log("Clicked on (" + rowIdx + ", " + colIdx + ")")

    if (myAppObj.game.isGameLost || myAppObj.game.isGameWon) {
        console.log("Unable to make move after game has ended.");
        return;
    }

    let body = {row: rowIdx, col: colIdx}

    axios.post('/api/games/' + myAppObj.game.gameNumber + "/moves", body)
        .then(function (response) {
            console.log("POST /moves:", response);
            loadGameBoard();
            loadGame();
        })
        .catch(function (error) {
            // Did they bump the wall?
            if (error.response.status == 400) {
                console.log("Move player: hit the wall.");
                playSound();
            } else {
                console.log("Move player ERROR:", error);
            }
        });

}



// Testing Functions
function testGetBadGame() {
    testErrorHandling(
        "Test Get Bad Game",
        "GET",
        '/api/games/' + 2352523,
        "",
        404);
}
function testGetBadBoard() {
    testErrorHandling(
        "Test Get Bad Board",
        "GET",
        '/api/games/' + 2352523 + "/board",
        "",
        404);
}
function testDoBadCheat() {
    testErrorHandling(
        "Test Cheat on bad game",
        "POST",
        '/api/games/' + 2352523 + "/cheatstate",
        "1_CHEESE",
        404);

    testErrorHandling(
        "Test Bad Move",
        "POST",
        '/api/games/' + myAppObj.game.gameNumber + "/moves",
        "NoSuchCheat",
        400);
}
function testDoBadMove() {
    testErrorHandling(
        "Test Move on bad game",
        "POST",
        '/api/games/' + 2352523 + "/moves",
        "MOVE_UP",
        404);

    testErrorHandling(
        "Test Bad Move",
        "POST",
        '/api/games/' + myAppObj.game.gameNumber + "/moves",
        "NoSuchMove",
        400);
}
function testErrorHandling(name, method, url, data, result) {
    axios( {
        method: method,
        url: url,
        data: data,
        config: plainTextConfig
    })
        .then(function (response) {
            alert(name + ": Did *not* fail when it should have! (expected " + result + ")");
        })
        .catch(function (error) {
            if (error.response.status != result) {
                console.log(name + ": Returned incorrect error response code (expected " + result + "): ", error.response)
                alert(name + ": Returned incorrect error response code (expected " + result + ")")
            } else {
                console.log(name + ": returned the correct response code: ", error)
            }
        });
}



function alertOnWrongStatus(description, expectedStatus, actualStatus) {
    if (actualStatus != expectedStatus) {
        alert("ERROR: Incorrect HTTP status returned for ["
            + description
            + "]; expected " + expectedStatus
            + " but server returned " + actualStatus)
    }
}


