//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.szappon1.hareandhounds;

import java.util.List; 
import java.util.ArrayList; 


public class HandHGame {

    private Hare hare;
    private String gameId;
    private String playerId1;
    private String playerId2;
    private String gameState;
    private String playerPiece1;
    private String playerPiece2;
    private boolean player1Turn;
    private List<Coordinate> coordinates;
    private List<Connection> connections;
    private List<Hound> hounds = new ArrayList<Hound>();
    private List<State> allStates = new ArrayList<State>(); 
   
    public HandHGame(String playerPiece1) {
        this.playerPiece1 = playerPiece1;
        this.gameId = "temp1";
        this.playerId1 = null;
        this.playerId2 = null;              
    }

    public void setUp() {
        coordinates = new ArrayList<Coordinate>(); 
        Coordinate coord;
        //7
        coord = new Coordinate(0, 1);
        coord.houndTakes();
        coordinates.add(coord);
        coord = new Coordinate(1, 0);
        coord.houndTakes();
        coordinates.add(coord);
        coord = new Coordinate(1, 2);
        coord.houndTakes();
        coordinates.add(coord);
        coordinates.add(new Coordinate(1, 1));
        coordinates.add(new Coordinate(2, 0));
        coordinates.add(new Coordinate(2, 1));
        coordinates.add(new Coordinate(2, 2));
        coordinates.add(new Coordinate(3, 0));
        coordinates.add(new Coordinate(3, 1));
        coordinates.add(new Coordinate(3, 2));
        coord = new Coordinate(4,1);
        coord.hareTakes();
        coordinates.add(coord);

         //22
        connections = new ArrayList<Connection>();
        connections.add(new Connection(0, 1, 1, 0));
        connections.add(new Connection(0, 1, 1, 1));
        connections.add(new Connection(0, 1, 1, 2));
        connections.add(new Connection(1, 0, 1, 1));
        connections.add(new Connection(1, 0, 2, 1));
        connections.add(new Connection(1, 0, 2, 0));
        connections.add(new Connection(1, 1, 1, 2));
        connections.add(new Connection(1, 1, 2, 1));
        connections.add(new Connection(1, 2, 2, 1));
        connections.add(new Connection(1, 2, 2, 2));
        connections.add(new Connection(2, 0, 3, 0));
        connections.add(new Connection(2, 1, 2, 0));
        connections.add(new Connection(2, 1, 2, 2));
        connections.add(new Connection(2, 1, 3, 0));
        connections.add(new Connection(2, 1, 3, 1));
        connections.add(new Connection(2, 1, 3, 2));
        connections.add(new Connection(2, 2, 3, 2));
        connections.add(new Connection(3, 1, 4, 1));
        connections.add(new Connection(3, 0, 4, 1));
        connections.add(new Connection(3, 0, 3, 1));
        connections.add(new Connection(3, 1, 3, 2));
        connections.add(new Connection(3, 2, 4, 1));
    }


   public boolean isValidMove(Coordinate coord) {
        for (Coordinate thisCoord : coordinates) {
          if (thisCoord.getX() == coord.getX() && thisCoord.getY() == coord.getY())
            return true;
        }
        return false;
    }

    public List<State> getAllStates() {
        return allStates;
    }
    
    public void setAllStates(List<State> allStates) {
        this.allStates = allStates;
    }

    public boolean isAdjacentConnection(Connection connect) {
        for (Connection c : connections) {
            if (c.equals(connect))
                return true;
        }
        return false;
    }

    public String getGameId() {
        return gameId;
    }

    public List<Coordinate> getCoordinates(){
        return coordinates;
    }

     public Coordinate getCoordinate(Coordinate coord){
        for (Coordinate c : coordinates) {
            if (c.equals(coord))
                return c;
        }
        return null;
    }

    public void updateCoordinate(String pieceType, Move move) {
        Coordinate fromCoord = new Coordinate(move.getFromX(), move.getFromY());
        Coordinate toCoord = new Coordinate(move.getToX(), move.getToY());
        for (Coordinate c : coordinates) {
            if (c.equals(fromCoord)) {
                c.leave();
            }
            if (c.equals(toCoord)) {
                if (pieceType.equals("HARE"))
                    c.hareTakes();
                else if (pieceType.equals("HOUND"))
                    c.houndTakes();
            }
        }
    }

    public String getPieceType() {
        return playerPiece1;
    }

   public void setGameId(String gameId) {
         this.gameId = gameId;
    }

    public Hare getHare(String playerId) {
        for (Coordinate c : coordinates) {
            if (c.getTaken() == 1)
                return new Hare(playerId, c);
        }
        return null;
    }

   public List<Hound> getHounds(String playerId) {
        List<Hound> houndsList = new ArrayList<Hound>(); 
        for (Coordinate c : coordinates) {
            if (c.getTaken() == 2) {
                houndsList.add(new Hound(playerId, c));
            }
        }
        return houndsList;
    }

    public void setHare(Hare hare) {
        this.hare = hare;
    }

    public void setHounds(List<Hound> hounds) {
        this.hounds = new ArrayList<Hound>(); 
        this.hounds = hounds;
    }
   
    public String getGameState() {
        return gameState;
    }

    public void setInitHounds(String playerId) {
        this.hounds = new ArrayList<Hound>(); 
        this.hounds.add( new Hound(playerId, new Coordinate(0, 1)));
        this.hounds.add( new Hound(playerId, new Coordinate(1, 0)));
        this.hounds.add( new Hound(playerId, new Coordinate(1, 2))); 
    }

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }
    
    public boolean getPlayer1Turn() {
        return player1Turn;
    }

    public void setGameState(String gameState) {
         this.gameState = gameState;
    }

    public String getPlayerId1() {
        return playerId1;
    }

    public String getPlayerId2() {
        return playerId2;
    }

     public void setPlayerId1(String plyrId1) {
        this.playerId1 = plyrId1;
    }

    public void setPlayerId2(String plyrId2) {
        this.playerId2 = plyrId2;
    }

     public String getPlayerPiece1() {
        return playerPiece1;
    }

    public String getPlayerPiece2() {
        return playerPiece2;
    }

    public void setPlayerPiece1(String playerPiece1) {
        this.playerPiece1 = playerPiece1;
    }

    public void setPlayerPiece2() {
        if (this.playerPiece1.equals("HARE")) {
            this.playerPiece2 = "HOUND";
            System.out.println("set other to hound");
        } else {
            this.playerPiece2 = "HARE";
            System.out.println("set other to hare");
        }
    }

    public String getGameToString(String pieceType) {
        return "{ gameId: " + gameId +
                ", playerId: " + playerId1 + 
                ", pieceType: " + playerPiece1 + " }";
    }
    
    @Override
    public String toString() {
        return "HandHGame{" +
                "gameId='" + gameId + '\'' +
                ", playerId1=" + playerId1 +
                ", playerPiece1=" + playerPiece1 +
                ", playerId2=" + playerId2 +
                ", playerPiece2=" + playerPiece2 +
                ", gameState='" + gameState + '\'' +
                ", player1Turn=" + player1Turn +
                '}';
    }   
}
