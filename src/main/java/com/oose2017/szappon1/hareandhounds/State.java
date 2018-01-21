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


public class State {

    private Piece hare;
    private List<Piece> hounds;
    private int occurances;


    public State(Piece hare, List<Piece> hounds){
        this.hare = hare;
        this.hounds = hounds;
        occurances = 1;
	}

    public int getOccurrences() {
        return occurances;
    }
    
    public void setOccurrences(int i) {
        this.occurances = i;
    }
    
    public boolean equals(State other) {
        boolean isEqual = true;
        List<Piece> allThisPieces = new ArrayList<Piece>();
        allThisPieces.add(this.hare);
        allThisPieces.addAll(this.hounds);

        List<Piece> allOtherPieces = new ArrayList<Piece>();
        allOtherPieces.add(other.hare);
        allOtherPieces.addAll(other.hounds);

        for (int i = 0; i < 4; i++) {
            if (!allOtherPieces.get(i).equals(allThisPieces.get(i))){
                isEqual = false;
            }
        }
        System.out.println("EQUAL??????: " + isEqual); 
        return isEqual;
    }

 	public String toString() {
        return "State{" +
                "hare='" + hare + '\'' +
                "hounds='" + hounds + '\'' +
                "occurances='" + occurances + '\'' + '}';
    }



}
