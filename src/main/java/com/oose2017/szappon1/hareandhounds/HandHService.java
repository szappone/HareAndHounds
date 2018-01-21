//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.szappon1.hareandhounds;

import com.google.gson.Gson;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import javax.sql.DataSource;
import java.util.List;
import java.util.ArrayList;

public class HandHService {

  private final List<HandHGame> allGames = new ArrayList<HandHGame>(); 
  List<Coordinate> coordinates;
  List<Piece> hounds;
  int idCntr = 0;

  public HandHGame createNewGame(String body) throws HandHServiceException {
    HandHGame game = new Gson().fromJson(body, HandHGame.class);
    game.setGameId(Integer.toString(idCntr));
    idCntr++;
    game.setPlayerId1("1");
    game.setGameState("WAITING_FOR_SECOND_PLAYER");
    if (body.contains("HARE")) {
       game.setPlayerPiece1("HARE");
       game.setPlayer1Turn(false);
       game.setHare(new Hare("1", new Coordinate(4, 1)));
       game.setInitHounds("2");
    } else if (body.contains("HOUND")) {
        game.setPlayerPiece1("HOUND");
        game.setPlayer1Turn(true);
        game.setInitHounds("1");
        game.setHare(new Hare("2", new Coordinate(4, 1))); 
    }    
    allGames.add(game);
    List<State> stateList = new ArrayList<State>();
    List<Piece> houndPieces = new ArrayList<Piece>();
    houndPieces.add(new Piece("HOUND", 0, 1));
    houndPieces.add(new Piece("HOUND", 1, 2));
    houndPieces.add(new Piece("HOUND", 1, 0));
    stateList.add(new State(new Piece("HARE", 4, 1), houndPieces));
    game.setAllStates(stateList);
    return game;
  }


  public List<HandHGame> getAllGames() { 
    return allGames;
  }

  public List<String> getAllGameIds() { 
    List<String> gameIds = new ArrayList<String>();
    for (HandHGame game : allGames)
        gameIds.add(game.getGameId());
    return gameIds;
  }

    public boolean isValidGameId(String id) { 
      return getAllGameIds().contains(id);
  }


  public HandHGame updateStateList(HandHGame game) throws HandHServiceException {
    coordinates = game.getCoordinates();
    hounds = new ArrayList<Piece>();
    Piece hare = null;
    //get curr state
    for (Coordinate c : coordinates) {
      if (c.getTaken() == 1)
        hare = new Piece("HARE", c.getX(), c.getY());
      else if (c.getTaken() == 2)
        hounds.add(new Piece("HOUND", c.getX(), c.getY()));
    }
    State newState = new State(hare, hounds);

    //update state list with curr state
    List<State> allStates = game.getAllStates();
    List<State> newStateList = new ArrayList<State>();
    int occ;
    boolean added = false;
    for (State state : allStates) {
      if (! newState.equals(state) ) {
         newStateList.add(state);
      } else if (newState.equals(state)) {
        occ = state.getOccurrences() + 1;
        newState.setOccurrences(occ);
        newStateList.add(newState);
        added = true;
      } 
      if (!added)
       newStateList.add(newState);
    }
    game.setAllStates(newStateList);
    return game;
  }

  public HandHGame makeTurnHound(String gameId, String playerId, Move move) throws HandHServiceException {
    HandHGame game = getGame(gameId);
    game.updateCoordinate("HOUND", move);
    game = updateStateList(game);
    return game;
  }

  public HandHGame makeTurnHare(String gameId, String playerId, Move move) throws HandHServiceException {
    HandHGame game = getGame(gameId);
    game.updateCoordinate("HARE", move);
    return game;
  }

  public boolean didHoundsStall(String gameId, String playerId) throws HandHServiceException {
    HandHGame game = getGame(gameId);
    List<State> allStates = game.getAllStates();
    for (State state : allStates) {
        if (state.getOccurrences() == 3)
          return true;
    }
    return false;
  }

  //The hare is trapped such that it has no valid move. The hounds win in this case.
  public boolean didHoundTrapHare(String gameId, String playerId) throws HandHServiceException {
    HandHGame game = getGame(gameId);
    List<Hound> hounds = game.getHounds(playerId);
    Coordinate hareLoc = game.getHare(playerId).getCoord();
    boolean possiblyTrapped = false;

    //check if hare is in 1 of the 4 places possible to get trapped
    if (hareLoc.equals(new Coordinate(0,1)) || hareLoc.equals(new Coordinate(2,0)) || hareLoc.equals(new Coordinate(2,2)) || hareLoc.equals(new Coordinate(4,1)))
      possiblyTrapped =  true;

    if (!possiblyTrapped)
      return false;

      if (hareLoc.equals(new Coordinate(0,1))) {
        for (Hound h : hounds) {
          if (! (h.getCoord().equals(new Coordinate(1,0)) || h.getCoord().equals(new Coordinate(1,1)) || h.getCoord().equals(new Coordinate(1,2))))
            return false;
        }
      }

      if (hareLoc.equals(new Coordinate(2,0))) {
        for (Hound h : hounds) {
          if (!(h.getCoord().equals(new Coordinate(1,0)) || h.getCoord().equals(new Coordinate(2,1)) || h.getCoord().equals(new Coordinate(3,0))))
            return false;
        }
      }

      if (hareLoc.equals(new Coordinate(2,2))) {
        for (Hound h : hounds) {
          if (!(h.getCoord().equals(new Coordinate(1,2)) || h.getCoord().equals(new Coordinate(2,1)) || h.getCoord().equals(new Coordinate(3,2))))
            return false;
        }
      }

      if (hareLoc.equals(new Coordinate(4,1))) {
        for (Hound h : hounds) {
          if (!(h.getCoord().equals(new Coordinate(3,2)) || h.getCoord().equals(new Coordinate(3,1)) || h.getCoord().equals(new Coordinate(3,0))))
            return false;
        }
      }
      return true;
}

public boolean didHareEscape(String gameId, String playerId) throws HandHServiceException {
    HandHGame game = getGame(gameId);
    int hareXCoord = game.getHare(playerId).getCoord().getX();
    List<Hound> hounds = game.getHounds(playerId);
    for (Hound hound : hounds ) {
      if (hound.getCoord().getX() < hareXCoord)
        return false;
    }
    return true;
  }

  public boolean movingFromEmptyCellOrToTakenCell(String gameId, Move move) throws HandHServiceException{
    HandHGame game = getGame(gameId);   
    Coordinate startingCoordinate = new Coordinate(move.getFromX(), move.getFromY());
    Coordinate coordFrom = game.getCoordinate(startingCoordinate);
    Coordinate endingCoordinate = new Coordinate(move.getToX(), move.getToY());
    Coordinate coordTo = game.getCoordinate(endingCoordinate);
    if (coordFrom.getTaken() == 0)
      return true;
    else if (coordTo.getTaken() == 1 || coordTo.getTaken() == 2)
      return true;
    else
      return false;
  }

  public boolean movesBackwards(String gameId, Move move) throws HandHServiceException{
    HandHGame game = getGame(gameId); 
    int startingX = move.getFromX();
    int targetX =  move.getToX(); 
    if (startingX > targetX)
      return true;
    else
      return false;
  }


  public HandHGame getGame(String id) throws HandHServiceException {
    if (allGames == null)
      return null;
    for (HandHGame game : allGames) {
      if (game != null && game.getGameId() != null) {
        if (game.getGameId().equals(id))
            return game;
      }
    }
    return null;
  }

  // HandHServiceException
  public static class HandHServiceException extends Exception {
    public HandHServiceException(String message, Throwable cause) {
      super(message, cause);
    }
  }

}
