//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//

package com.oose2017.szappon1.hareandhounds;


public class Coordinate {
 
    private int taken;
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.taken = 0;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
 
    public int getTaken() {
        return this.taken; 
    }
    
    public void hareTakes() {
        this.taken = 1;
    }
    
    public void houndTakes() {
        this.taken = 2;
    }
    
    public void leave() {
        this.taken = 0;
    }

    public boolean equals(Coordinate c) {
        if(this.x == c.getX() && this.y == c.getY())
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "Coordinate{" + "(" + x + "," + y + ")" + " " + taken + "}";
    }
}
