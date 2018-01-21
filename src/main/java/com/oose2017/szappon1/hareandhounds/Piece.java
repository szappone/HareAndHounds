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


public class Piece {

	// { pieceType: <type>, x: <x>, y: <y> }
    private String pieceType;
    private int x;
    private int y;


    public Piece(String pieceType, int x, int y) {
        this.pieceType = pieceType;
        this.x = x;
        this.y = y;
	}

	public String getPieceType() {
	       return pieceType;
	 }

	public int getX() {
	      return x;
	 }

	public int getY() {
	    return y;
	}

 	public String toString() {
        return "Piece{" + pieceType + "(" + x + ", " + y + "}";
    }

    public boolean equals(Piece other) {
        if (this.pieceType == other.pieceType && this.x == other.x && this.y == other.y)
            return true;
        else
            return false;
    }

}
