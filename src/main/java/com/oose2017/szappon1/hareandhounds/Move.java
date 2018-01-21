//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.szappon1.hareandhounds;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

import static spark.Spark.*;


public class Move {

	//{ playerId: <id>, fromX: <x>, fromY: <y>, toX: <x>, toY: <y>
    private String playerId;
    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public Move(String playerId, int fromX, int fromY, int toX, int toY) {
        this.playerId = playerId;
        this.fromX = fromX;
        this.fromY = fromY;
    	this.toX = toX;
    	this.toY = toY;

	    }

	public String getPlayerId() {
	       return playerId;
	 }

	public int getFromX() {
	      return fromX;
	 }

	public int getFromY() {
	    return fromY;
	}

	public int getToX() {
	      return toX;
	 }

	public int getToY() {
	    return toY;
	}

 	public String toString() {
        return "Move{" +
                "playerId='" + playerId + '\'' +
                ", fromX='" + fromX + '\'' +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                '}';
    }

}
