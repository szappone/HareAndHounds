//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.szappon1.hareandhounds;


public class Hare {
 
    private String playerId;
    private Coordinate coord;
    private int x;
    private int y;

    public Hare(String playerId, Coordinate coord) {
        this.playerId = playerId;
        this.coord = coord;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public boolean equals(Hare o) {
        if (this.coord == o.coord)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Hare{" +  "playerId='" + playerId + ", " + "coord='" + coord + "}";
               
    }
}
