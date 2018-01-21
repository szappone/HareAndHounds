//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.szappon1.hareandhounds;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import static spark.Spark.*;

public class HandHController {

    private final HandHService hAndHService;
    private static final String HH_API_CONTEXT = "/hareandhounds/api";

    public HandHController(final HandHService hAndHService) {
        this.hAndHService = hAndHService;
        setupEndpoints();
     }
    
    class JsonResponses {
        private String gameId;
        private String playerId;
        private String pieceType;
        private String reason;

        public JsonResponses(String reason) {
            this.reason = reason;
        }

        public JsonResponses(String gameId, String playerId, String pieceType) {
            this.gameId = gameId;
            this.playerId = playerId;
            this.pieceType = pieceType;
        }
    }

    private void setupEndpoints() {

        //start a game
        post(HH_API_CONTEXT + "/games", "application/json", (request, response) -> {
            try{
                String req = request.body();
                String toComp = "\"pieceType\":\"HOUND\"";
                if (!req.contains("\"pieceType\":\"HOUND\"") && !req.contains("\"pieceType\":\"HARE\"")) {
                    response.status(400);
                    return Collections.EMPTY_MAP;
                }
                HandHGame game = hAndHService.createNewGame(request.body());
                response.status(201);
                return new JsonResponses(game.getGameId(), game.getPlayerId1(), game.getPieceType());  
            } catch (HandHService.HandHServiceException ex) {
                response.status(400);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());


        //join a game
        put(HH_API_CONTEXT + "/games/:gameId", "application/json", (request, response) -> {
            try {
                if (!hAndHService.isValidGameId(request.params(":gameId"))) {
                    response.status(404);   // invalid game id
                    return Collections.EMPTY_MAP;
                }
                HandHGame game = hAndHService.getGame(request.params(":gameId"));
                if (!game.getGameState().equals("WAITING_FOR_SECOND_PLAYER")){
                    response.status(410); // 2nd player already joined
                    return Collections.EMPTY_MAP;
                }
                response.status(200);
                game.setPlayerId2("2");
                game.setPlayerPiece2();
                game.setGameState("TURN_HOUND");
                game.setUp();
                // Content: { gameId: <id>, playerId: <id>, pieceType: <type> }
                return new JsonResponses(game.getGameId(), game.getPlayerId2(), game.getPlayerPiece2());
            } catch (HandHService.HandHServiceException ex) {
                response.status(500);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());


        //play a game
        post(HH_API_CONTEXT + "/games/:gameId/turns", "application/json", (request, response) -> {
            try {
                if (! hAndHService.isValidGameId(request.params(":gameId"))) { 
                    response.status(404);   // invalid game id
                    return new JsonResponses("INVALID_GAME_ID"); 
                }
                String gameId = request.params(":gameId");
                HandHGame game = hAndHService.getGame(gameId);
                String req = request.body();
                Move move = new Gson().fromJson(request.body(), Move.class);
     
                if (!game.isValidMove(new Coordinate(move.getToX(), move.getToY())) || hAndHService.movingFromEmptyCellOrToTakenCell(gameId, move)) {
                    response.status(422); // illegal move 
                    return new JsonResponses("ILLEGAL_MOVE");   
                }
                if (!game.isAdjacentConnection(new Connection(move.getFromX(), move.getFromY(), move.getToX(), move.getToY())) && !game.isAdjacentConnection(new Connection(move.getToX(), move.getToY(), move.getFromX(), move.getFromY()))){
                    response.status(422); //illegal move
                    return new JsonResponses("ILLEGAL_MOVE");   
                }
                String playerId = move.getPlayerId();
                if (!(playerId.equals("1") || playerId.equals("2"))){
                    response.status(404);
                    return new JsonResponses("INVALID_PLAYER_ID");
                }

                boolean isPlayer1Turn = game.getPlayer1Turn();
                if (isPlayer1Turn && playerId.equals("1")) {
                    game.setPlayer1Turn(false);
                    if (game.getPlayerPiece1().equals("HOUND")) {
                        if (hAndHService.movesBackwards(gameId, move)){
                            response.status(422);
                            game.setPlayer1Turn(true);
                            return new JsonResponses("ILLEGAL_MOVE"); 
                        }
                        
                        hAndHService.makeTurnHound(gameId, playerId, move);
                        if (hAndHService.didHoundTrapHare(gameId, playerId)) {
                             game.setGameState("WIN_HOUND");
                        } else if (hAndHService.didHoundsStall(gameId, playerId)){
                            game.setGameState("WIN_HARE_BY_STALLING");
                        } else {
                            game.setGameState("TURN_HARE");
                        }  
                    } else {
                        hAndHService.makeTurnHare(gameId, playerId, move);
                        if (hAndHService.didHareEscape(gameId, playerId)) {
                            game.setGameState("WIN_HARE_BY_ESCAPE");
                        } else {
                            game.setGameState("TURN_HOUND");
                        }   
                    } 
                } else if (!isPlayer1Turn && playerId.equals("2")) {
                     game.setPlayer1Turn(true);
                     if (game.getPlayerPiece2().equals("HOUND")) {
                         if (hAndHService.movesBackwards(gameId, move)){
                            response.status(422); //illegal move
                            game.setPlayer1Turn(false);
                            return new JsonResponses("ILLEGAL_MOVE"); 
                        }
                        game.setPlayer1Turn(true);
                        hAndHService.makeTurnHound(gameId, playerId, move);
                        if (hAndHService.didHoundTrapHare(gameId, playerId)) {
                            game.setGameState("WIN_HOUND");
                        } else if (hAndHService.didHoundsStall(gameId, playerId)){
                            game.setGameState("WIN_HARE_BY_STALLING");
                        } else {
                            game.setGameState("TURN_HARE");
                        }  
                     } else {
                        hAndHService.makeTurnHare(gameId, playerId, move);
                        if (hAndHService.didHareEscape(gameId, playerId)) {
                            game.setGameState("WIN_HARE_BY_ESCAPE");
                        } else {
                            game.setGameState("TURN_HOUND");
                        } 
                    }

                } else {
                    response.status(422);
                    return new JsonResponses("INCORRECT_TURN"); 
                }
                response.status(200);
                return new PlayGame(playerId);
            } catch (HandHService.HandHServiceException ex) {
                response.status(500);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());

         //describe game board
        get(HH_API_CONTEXT + "/games/:gameId/board", "application/json", (request, response) -> {
            try {
                String id = request.params(":gameId");
                if (! hAndHService.isValidGameId(id)) {
                    response.status(404);   // invalid game id
                    return new JsonResponses("INVALID_GAME_ID");
                }
                HandHGame game = hAndHService.getGame(id);  
                response.status(200);
                List<Piece> boardDescription = new ArrayList<Piece>();

                if(game.getPlayerPiece2() != null && game.getPlayerPiece2() != null) {
                    String playerId = "1";
                    if (game.getPlayerPiece2().equals("HARE"))
                        playerId = "2";
                    boardDescription.add(new Piece("HARE", game.getHare(playerId).getCoord().getX(), game.getHare(playerId).getCoord().getY()));
        
                    playerId = "1";
                    if (game.getPlayerPiece2().equals("HOUND"))
                        playerId = "2";
                    List<Hound> hounds = game.getHounds(playerId);
                    for (Hound h : hounds)
                        boardDescription.add(new Piece("HOUND", h.getCoord().getX(), h.getCoord().getY()));
                }
                return boardDescription; // 4 x { pieceType: <type>, x: <x>, y: <y> }
            } catch (HandHService.HandHServiceException ex) {
                response.status(500);
                return Collections.EMPTY_MAP;
            }
        }, new JsonTransformer());
    
        //describe game state
        get(HH_API_CONTEXT + "/games/:gameId/state", "application/json", (request, response) -> {
            try {
                String id = request.params(":gameId");
                if ( hAndHService.getGame(id) != null) {
                    HandHGame game = hAndHService.getGame(id); 
                    response.status(200);
                    return new GameState(game.getGameState());
                }
                response.status(404);
             } catch(HandHService.HandHServiceException ex) {
                response.status(500);
                return Collections.EMPTY_MAP;
            }
            return Collections.EMPTY_MAP;
        }, new JsonTransformer());

    }
}
