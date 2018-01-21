//-------------------------------------------------------------------------------------------------------------//
// Code based on a tutorial by Shekhar Gulati of SparkJava at
// https://blog.openshift.com/developing-single-page-web-applications-using-java-8-spark-mongodb-and-angularjs/
//-------------------------------------------------------------------------------------------------------------//


package com.oose2017.szappon1.hareandhounds;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;

import static spark.Spark.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bootstrap {
    public static final String IP_ADDRESS = "localhost";
    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {

        //Specify the IP address and Port at which the server should be run
        ipAddress(IP_ADDRESS);
        port(PORT);
        //Specify the sub-directory from which to serve static resources (like html and css)
        staticFileLocation("/public");

        new HandHController(new HandHService());
        System.out.println("CREATED A TODOCONTROLLER");
    }
}

