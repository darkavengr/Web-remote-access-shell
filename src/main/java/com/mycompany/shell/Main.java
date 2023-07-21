/**
 *
 * Java shell
 *
 * A Remote access shell
 *
*/
package com.mycompany.shell;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.concurrent.Semaphore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;


/** \defgroup TechnicalDetails Technical Details */


/** \mainpage
 *
* Java shell version 1.0
*
* \tableofcontents
*/

/** \defgroup GettingStarted Getting Started
      * To get connect to JavaShell, open a browser and go to localhost:8080
      *      
      * Use the username admin and the password password to connect.
      * it is recommended that you change this password after connecting to something else.
      *
      * After logging in, you will see a terminal interface with a textbox, an input bar and a button
      * 
      * Enter commands into the input bar and click the button or press enter.
      *
      * Results are displayed in the textbox.
      */

/**\ingroup FileAndDirectoryPaths
      * JavaShell does not use "real" directory paths, it uses paths that are relative to the users home directory
      * 
      * It presents what appears to be a directory tree that starts at the root directory / with the users files
      * and directories within that directory, however, each user has their own private directory under the "real"
      * directory shellroot. 
      * Each user is unable to see any of the other user's files.
      *
      * The file and directory paths input by the user and translated into "real" paths by prepending the user's directory
      * to it. In the same way any paths output by the programs are translated to make them into relative paths
      *
      * For example:
      * \path /user/matt/javashell/shellroot/admin/test.txt becomes \path /test.txt
      *
      * Another security feature of the program is that directory paths are validated to prevent acccess outside the user's directory.
      * Paths passed to internal and external commands are checked to make sure that security problems cannot occur.
      */


   public class Main {
    
    static Semaphore PasswdFileMutex = new Semaphore(1);
   
    public static void main(String[] args) throws Exception {
        
        //
        // Intialize HTTP server

        HTTPServer httpserver=new HTTPServer();
        httpserver.start();
        
        // Intialize HTTPS server
        
        
        String pass = "password";
        int HTTPSPORT = 8081;
        String credentialsFilePath="comp20081.jks";
        
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            
            char[] password = pass.toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(credentialsFilePath);
            ks.load(fis, password);

            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);
            sslContext.init(kmf.getKeyManagers(), null, null);

            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

           SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
           SSLServerSocket s  = (SSLServerSocket) ssf.createServerSocket(HTTPSPORT);

         System.out.println("HTTPS listening on port: "+HTTPSPORT);
         
         // Listening to the port         
         
         while (true) {
             
            SSLSocket c = (SSLSocket) s.accept();

            Session session = new Session(c);
            session.start();
             
          
         }
         
        }catch(Exception e) {
            
            System.out.println(e);            
        }
     }
   }

class HTTPServer implements Runnable {
 Socket socket;
 Thread t;
        

public void start() {
      if (t == null) {
         t = new Thread (this,"Socket thread");
         t.start ();
      }
}

public void run() {
    //
        // Create server
        //
        try {
     
            int PORT = 8080;

            ServerSocket server = new ServerSocket(PORT);

            System.out.println("HTTP listening port on port: " + PORT);
            
            //
            // Wait for server to accept connection
            //
            while (true) {
                Socket socket = server.accept();

                Session session = new Session(socket);
                session.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

  }
}