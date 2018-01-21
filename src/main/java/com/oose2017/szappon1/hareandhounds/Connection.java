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


public class Connection {

    final int fromX;
    final int fromY;
    final int toX;
    final int toY;
    
    public Connection(int a, int b, int c, int d) {
        this.fromX = a;
        this.fromY = b;
        this.toX = c;
        this.toY = d;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection connect = (Connection) o;
        if (this.fromX == connect.fromX && this.fromY == connect.fromY && this.toX == connect.toX && this.toY == connect.toY)
            return true;
        return false;
    }

}
