/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shell;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author matt
 */

    public class Session implements Runnable {
    static String[] postarray;

    Socket socket;
    Thread t;
    
    Session(Socket acceptsocket) {
        socket=acceptsocket;
    }
    
    public void start() {
      if (t == null) {
         t = new Thread (this,"Socket thread");
         t.start ();
      }
    }
    
    public void run() { 
      boolean shudown = true;
      String UserName="";
      Command command=new Command();
      User user=new User();            // create user to authenticate

        System.out.println("Started thread for socket");
        
        try {
             //
            // Main loop
            //
            while (shudown) {
                   
                
                //
                // When a connection has been accepted, create input and output objects connected to the socket
                //
                InputStream is = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line;
                String ThisPage="";
                int count=0;
                
                line = in.readLine();
                String auxLine = line;                  // Get auxilary line
                line = "";
                
                //
                // There might be POST data sent from a form
                // 
                int postDataI = -1;
                
                //
                // Loop through POST data
                //
                while ((line = in.readLine()) != null && (line.length() != 0)) {
                  //  System.out.println(line);
                    
                    if (line.indexOf("Content-Length:") > -1) {             // Found content length
                        postDataI = new Integer(line                        // Extract length from string and convert to integer
                                .substring(
                                        line.indexOf("Content-Length:") + 16,
                                        line.length())).intValue();
                    }
                    
                    //
                    // The referer is used to distinguish between different stages of the sign in process
                    //
                    if(line.startsWith("Referer:") == true) {           // Get page name
                        ThisPage=line.substring(10,line.length());                        
                    }
                }
                
                String postData = "";
                
                //
                // Get POST data
                //
                for (int i = 0; i < postDataI; i++) {
                    int intParser = in.read();
                    postData += (char) intParser;
                }
                // replace + by " "
                int index=postData.indexOf('+');
                while(index>-1){
             
                    postData = postData.substring(0, index) + ' ' + postData.substring(index + 1);
                    index=postData.indexOf('+');
                }  

                System.out.println("POST DATA="+postData);
        
                /**
                 * 
                */
     String SignInPage="<style>" +
		   ".textbox {" +
		   "margin-bottom: 1%;" +
		   "}" +
                   "</style>" +
                    "<H1>Java shell</H1><br><br>" + 
                    "<form name=\"input\" action=\"imback\" method=\"post\">" + 
                    "Username: <input type=\"text\" name=\"user\" class='textbox'><br>" + 
                    "Password: <input type=\"password\" name=\"password\" class='textbox'><br>" +                 
                    "<input type=\"submit\" value=\"Submit\">" + 
                    "</form>";

  String CommandPageTop="<style>" +
		   "body {" +
		   "background-color: lightgray;" +
		   "}" +
		   "#commandbox {" +
		   "width: 100%;" +
		   "}" +
		   "#outputtextarea {" +
		   "width: 100%;" +
		   "height: 80%;" +
                   "margin-bottom: 0px" +
                   "overflow-x: hidden" +
		   "background-color: white;" + 
		   "}" +
		   "</style>" +
		   "<body>" +
		   "<textarea id=outputtextarea readonly width=100%>";
		   

                String CommandPageBottom="</textarea>" +
                        "<form name=\"input\" action=\"imback\" method=\"post\">" + 
		         "<input type=\"text\" name=\"command\" id=\"commandbox\" ><br>"+                         
                         "<input type=\"submit\" value=\"Execute\" \"name=\"exec\" />" +
		         "</form>" +
		         "</body>";
    
                // Send the HTML page  
                
                System.out.println("THISPAGE="+ThisPage);
                
                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html; charset=utf-8");
                out.println("Server: MINISERVER");
                // this blank line signals the end of the headers
        
                out.println("");

                //
                // This is a state machine:
                //
                // It can be in these states
                //
                // (1) Waiting for user to enter username and password
                //
                // (2) Checking username or password
                //
                // (3) Waiting for user to enter command
                //
                // (4) Executing commands
                //
                if(ThisPage.equals("") == true) {          // (1) Waiting for user to enter username and password
                // Send the HTML page                         
                  out.println(SignInPage);                   
                }                                                                                                                                       
                else
                {   
                                     
              
                  //
                // POST is in the format command="command"                                                        
                  postarray=postData.split("&");      
                                                     
                  if(postarray[0].startsWith("user=") && (postarray[1] != null)) {
                      
                        for(count=0;count<postarray.length;count++) {
                             postarray[count]=postarray[count].substring(postarray[count].lastIndexOf("=")+1,postarray[count].length());                       
                        }                      
                        
                        if(user.CheckAuthentication(postarray[0],postarray[1]) == false) { // authenticate user
                   
                            out.println(SignInPage);              // send sign in page again so the user can try again
                            out.println("<H1>Invalid username or password</H1>");                       
                        }
                        else                                // (3) Waiting for user to enter command
                        {
                          UserName=postarray[0];
                                
                          out.println(CommandPageTop);         // show page             
                          out.println(CommandPageBottom); 
                          
                                                    
                          System.out.println("Username="+UserName);
                        
                          command.SetUserName(UserName);          // create command object
                       }
                    }      
                    else {                                        // (4) Executing commands
                      for(count=0;count<postarray.length;count++) {
                             postarray[count]=postarray[count].substring(postarray[count].lastIndexOf("=")+1,postarray[count].length());                       
                        }                      
                        
                      
                        System.out.println("Username="+UserName);
                                             
                        String commandarray[]=new String[255];
                        
                        commandarray=postarray[0].split(" ");  // split command
                                                
                        out.println(CommandPageTop);            // show top of                   
                                                                        
                        command.ExecuteCommand(user,in, out, commandarray);
                        out.println(CommandPageBottom);
                  }
                        
               }                               
                            
                 out.close();                 
                socket.close();
                return;
            }
        
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
    }
    
