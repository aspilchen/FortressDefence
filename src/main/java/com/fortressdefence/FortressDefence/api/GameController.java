package com.fortressdefence.FortressDefence.api;

import com.fortressdefence.FortressDefence.model.CellLocation;
import com.fortressdefence.FortressDefence.model.CellState;
import com.fortressdefence.FortressDefence.model.Game;
import com.fortressdefence.FortressDefence.restapi.ApiBoardWrapper;
import com.fortressdefence.FortressDefence.restapi.ApiGameWrapper;
import com.fortressdefence.FortressDefence.restapi.ApiLocationWrapper;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {
    private final int N_TANKS_IN_GAME = 5;
    private List<Game> games = new ArrayList<>();
    private List<Boolean> cheatStates = new ArrayList<>();

    @GetMapping("/api/about")
    public ResponseEntity<String> getName() {
        return new ResponseEntity<>("Adam Spilchen", HttpStatus.OK);
    }

    @GetMapping("/api/games")
    public ResponseEntity<List<ApiGameWrapper>> getGames() {
        List<ApiGameWrapper> gameWrappers = new ArrayList<>();
        int id = 0;
        for (Game g: games) {
            gameWrappers.add(ApiGameWrapper.fromGame(g, id));
            id++;
        }
        return new ResponseEntity<>(gameWrappers, HttpStatus.OK);
    }

    @GetMapping("/api/games/{id}")
    public ResponseEntity<ApiGameWrapper> getGame(@PathVariable("id") int id) {
        checkGameId(id);
        Game g = games.get(id);
        ApiGameWrapper wrapper = ApiGameWrapper.fromGame(g, id);
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PostMapping("/api/games")
    public ResponseEntity<ApiGameWrapper> postGame(@RequestBody ApiGameWrapper gameWrapper) {
        Game g = new Game(N_TANKS_IN_GAME);
        ApiGameWrapper wrapper = ApiGameWrapper.fromGame(g, games.size());
        games.add(g);
        cheatStates.add(false);
        return new ResponseEntity<>(wrapper, HttpStatus.CREATED);
    }

    @GetMapping("/api/games/{id}/board")
    public ResponseEntity<ApiBoardWrapper> getBoard(@PathVariable("id") int id) {
        checkGameId(id);
        Game g = games.get(id);
        ApiBoardWrapper wrapper = ApiBoardWrapper.fromGame(g, cheatStates.get(id));
        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    @PostMapping("/api/games/{id}/moves")
    public ResponseEntity<String> postMove(
            @PathVariable("id") int id
            ,@RequestBody ApiLocationWrapper locationWrapper
    ) {
        checkGameId(id);
        Game game = games.get(id);
        CellLocation location = locationWrapper.toCellLocation();
        game.recordPlayerShot(location);
        game.fireTanks();
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    @PostMapping("/api/games/{id}/cheatstate")
    public ResponseEntity<String> cheatState(
            @PathVariable("id") int id
            ,@RequestBody String bodyStr
    ) {
        checkGameId(id);
        if(!bodyStr.equals("SHOW_ALL")) {
            throw new InvalidParameterException();
        }
        cheatStates.set(id, true);
        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }

    private void checkGameId(int id) {
        if(id < 0 || id >= games.size()) {
            throw new IllegalArgumentException();
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Game ID not found.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler() {}

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Location out of bounds")
    @ExceptionHandler(InvalidParameterException.class)
    public void invalidLocationExceptionHandler() {}

}
