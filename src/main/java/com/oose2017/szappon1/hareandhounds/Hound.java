//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.szappon1.hareandhounds;


public class Hound {
 
    //private String gameId;
    private String playerId;
    private Coordinate coord;

    public Hound(String playerId, Coordinate coord) {
        this.playerId = playerId;
        this.coord = coord;
    }

    public Coordinate getCoord() {
        return coord;
    }
    public String getPlayerId() {
        return playerId;
    }

    public boolean equals(Hound o) {
        if (this.coord == o.coord)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Hound{" + "playerId='" + playerId + ", coord='" + coord + "}";
    }
}
