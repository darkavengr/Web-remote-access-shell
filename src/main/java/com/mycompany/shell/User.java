/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.shell;
import java.io.*;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 *
 * @author matt
 */

    /**\ingroup <TechnicalDetails>
    * 
    * \class User
    * @brief  User class
    * 
    * @author Matthew Boote
    *
    * @details Represents a user of the system.
    *
    */

public class User {
    private static String UserName;
    private static String Password;
    private static String UserType;
    
    int PASSWD_USERNAME=0;              // fields for passwd file
    int PASSWD_PASSWORD=1;
    int PASSWD_USERTYPE=2;

  /**
    *\ingroup <TechnicalDetails>
    * 
    * CheckAuthentication
    *
    * @brief Authenticates a user.
    * 
    * @author Matthew Boote
    *
    * @details Checks the username and hashed password against the list of users in passwd.
    * 
    * @param[in] String username    The username
    *
    * @param[in] String password    The password
    *            
    * @return Returns true if username and password are correct, false otherwise
    *
    * @throw General exception  
    */    
    boolean CheckAuthentication(String username,String password) {            
      String[] usersarray=new String[255];
      BufferedReader fread=null;
      String passwdline="**********";
      String hashedpassword;
     
     try {
       Main.PasswdFileMutex.acquire();               // acquire mutex
     } catch(Exception e) {
             System.out.println(e);
          }
     
     try {              
          fread=new BufferedReader(new FileReader("passwd"));                       // open file
          
      } catch(Exception e) {
         System.out.println("Error opening passwd");

        Main.PasswdFileMutex.release();            // release mutex
         return(false);
     }

//
// Read through passwd file and check username and password
// 

      do {            
         try {
           passwdline=fread.readLine();                  // read line from users file
             } catch(Exception e) {
            System.out.println("Error reading passwd");
            
          Main.PasswdFileMutex.release();            // release mutex
            return(false);
          }
         
         if(passwdline == null) {              // end of file           
            try {
              fread.close();
           } catch(IOException e) {
              System.out.println("I/O exception");              
              
              Main.PasswdFileMutex.release();            // release mutex
              return(false);
           }
            
              Main.PasswdFileMutex.release();            // release mutex
            return(false);
         }
         
         usersarray=passwdline.split(":");                  // split line
                            
         hashedpassword=hashpassword(password);       // hash password
           
           // compare username and password

         System.out.println(usersarray[PASSWD_PASSWORD]+" "+hashedpassword);
         
         if((usersarray[PASSWD_USERNAME].equals(username) == true) && (usersarray[PASSWD_PASSWORD].equals(hashedpassword) == true)) {           // if username and password match
           System.out.println("OKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
           
           try {                    // close file 
              fread.close();
           } catch(IOException e) {
              System.out.println("I/O exception");
        //      Main.PasswdFileMutex.release();            // release mutex
              return(false);
           }

           UserName=username;               // set username
           UserType=usersarray[PASSWD_USERTYPE];          // set user type
           Password=usersarray[PASSWD_PASSWORD];
    
           Main.PasswdFileMutex.release();            // release mutex
           return(true);
         }
      
       }while(passwdline != null);
   
     try {
      fread.close();
     } catch(IOException e) {
       System.out.println("I/O exception");
       
       Main.PasswdFileMutex.release();            // release mutex
       return(false);
     }
     
     Main.PasswdFileMutex.release();            // release mutex
     return(false);
    }

  /**
    *\ingroup <TechnicalDetails>
    * 
    * hashpassword
    *
    * @brief Hashes a password
    * 
    * @author Matthew Boote
    *
    * @details Returns the hashed equivalent to a password
    * 
    * @param[in] String password  The password
    *       
    * @return Returns the hashed password or empty string on error
    *
    * @throw General exception  
    */    
   String hashpassword(String password) {
     MessageDigest md=null;
      byte[] encodedhash=new byte[255];
      int count;
      String hashstring="";
      String hexarray[]={ "0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F" };
      
      try {
        md = MessageDigest.getInstance("SHA-256"); 
       
        encodedhash = md.digest(password.getBytes(StandardCharsets.UTF_8));       // hash password
     
        for(count=0;count<encodedhash.length;count++) {         
         hashstring += hexarray[(encodedhash[count] & 0xf) >> 4];               // get high nibble
         hashstring += hexarray[encodedhash[count] & 0xf];               // get low nibble
        }
          
     return(hashstring);
      } catch(Exception e) {
         System.out.println("Error initializing password crypto");
         return("");
     }
          
   }

  /**
    *\ingroup <TechnicalDetails>
    * 
    * GetUserName
    *
    * @brief Gets the username
    * 
    * @author Matthew Boote
    *           
    * @return String username
    *
    * @throw General exception  
    */   
public String GetUserName() {
    return(UserName);
}

/**
    *\ingroup <TechnicalDetails>
    * 
    * GetPassword
    *
    * @brief Gets the password
    * 
    * @author Matthew Boote
    *           
    * @return String password
    *
    * @throw General exception  
    */   
public String GetPassword() {
    return(Password);
}

/**
    *\ingroup <TechnicalDetails>
    * 
    * GetUserType
    *
    * @brief Gets the username
    * 
    * @author Matthew Boote
    *           
    * @return String username
    *
    * @throw General exception  
    */   
public String GetUserType() {
    return UserType;
}
        
  /**
    *\ingroup <TechnicalDetails>
    * 
    * AddUser
    *
    * @brief Adds a user
    * 
    * @author Matthew Boote
    *
    * @details Returns true on success, false on failure
    * 
    * @param[in] String Username  The username
    *       
    * @param[in] String Password  The password
    *       
    * @param[in] String UserType  The user type, either standard or admin
    *       
    * @return Returns true on success, false on failure
    *
    * @throw General exception  
    */ 
public boolean AddUser(String Username,String Password,String UserType) {
    String passwdline="**********";
    String[] usersarray=new String[255];

    try {
       Main.PasswdFileMutex.acquire();               // acquire mutex
    } catch(Exception e) {
         Main.PasswdFileMutex.release();            // release mutex                    
         
         System.out.println(e);
         return false;       
    }
    
    File hd=new File("shellroot");             // get fake root directory
    String newuserdir=hd.getAbsolutePath()+"/"+Username;          // get absolute path 
     
    try {
     Files.createDirectory(Paths.get(newuserdir));
    } catch(Exception e) {
         System.out.println(e);
         return false;
    }       
         
        //
        // Read through passwd file and check username exists
        //
        try { 
                RandomAccessFile adduserwriter = new RandomAccessFile("passwd","rw");
            //
            // Read through passwd file and check username exists
            //
            
            do {
                passwdline=adduserwriter.readLine();                  // read line from users file
                
                if(passwdline == null) {              // end of file                    
                    break;
                }
                
                usersarray=passwdline.split(":");                  // split line
                
                //  check if user exists
    
                if(usersarray[PASSWD_USERNAME].equals(Username) == true) {
                    adduserwriter.close();
                    
                    System.out.println("User already exists");

                    Main.PasswdFileMutex.release();            // release mutex                    
                    return(false);
                }
                
            }while(passwdline != null);
            
            // Seek to end of file and add user information
            
            adduserwriter.seek(adduserwriter.length());
            
            adduserwriter.writeBytes(Username + ":"+ hashpassword(Password) + ":" + UserType+"\n");
               
            adduserwriter.close();
     
      } catch(Exception e) {
         System.out.println(e);

         Main.PasswdFileMutex.release();            // release mutex         
         return(false);
     }
      
        //
        // Copy list of permitted commands from template file
        //
     try {
        System.out.println(Files.copy(Paths.get("template.command.dat"),Paths.get(Username+".command.dat"),REPLACE_EXISTING));
               
     } catch(Exception e) {
           System.out.println(e);
    }
             
     return(true);
    }

/**
    *\ingroup <TechnicalDetails>
    * 
    * RemoveUser
    *
    * @brief Removes a user
    * 
    * @author Matthew Boote
    *
    * @details Removes a user from the passwd file. Does not delete the user directory or files
    * 
    * @param[in] String Username  The username
    *                
    * @return Returns true on sucess, false on failure
    *
    * @throw General exception  
    */ 
public boolean RemoveUser(String Username) {
    String passwdline="**********";
    String[] usersarray=new String[255];
    
     try {
         Main.PasswdFileMutex.acquire();               // acquire mutex

         RandomAccessFile adduserwriter = new RandomAccessFile("passwd","r");          
         RandomAccessFile TempFile = new RandomAccessFile("passwd.tmp","rw");
     
               
          //
          // Read through passwd file and find username
         // remove user if username found

      do {            
         passwdline=adduserwriter.readLine();                  // read line from users file
         
         if(passwdline == "\n") {              // empty line
             continue;             
         }
         
         if(passwdline == null) {              // end of file           
             break;             
         }
                    
         usersarray=passwdline.split(":");                  // split line
        
           // Write user information to temporary file
           // excluding user to be removed
           
         
         if(usersarray[PASSWD_USERNAME].equals(Username) == false) {  
           System.out.println(passwdline);
         
           TempFile.writeBytes(usersarray[PASSWD_USERNAME] + ":"+ hashpassword(usersarray[PASSWD_PASSWORD]) + ":" + usersarray[PASSWD_USERTYPE]+"\n");
         }
         
       }while(passwdline != null);
                
      adduserwriter.close();    
      TempFile.close();
      
      // Move temporary file to passwd
      
      Files.move(Paths.get("passwd.tmp"), Paths.get("passwd"), StandardCopyOption.REPLACE_EXISTING);
      
      } catch(Exception e) {
         System.out.println(e);
         
         Main.PasswdFileMutex.release();            // release mutex
         return(false);
     }

     Main.PasswdFileMutex.release();            // release mutex
     return(true);
    }

/**
    *\ingroup <TechnicalDetails>
    * 
    * UpdateUser
    *
    * @brief Updates user information
    * 
    * @author Matthew Boote
    *   
    * @param[in] String Username  The username
    *       
    * @param[in] String Password  The password
    *       
    * @param[in] String UserType  The user type, either standard or admin
    *       
    * @return Returns true on success, false on failure
    *
    * @throw General exception  
    */ 
public boolean UpdateUser(String Username,String Password,String UserType) {
    String passwdline="**********";
    String[] usersarray=new String[255];
    
     try {
         Main.PasswdFileMutex.acquire();               // acquire mutex

         RandomAccessFile adduserwriter = new RandomAccessFile("passwd","r");          
         RandomAccessFile TempFile = new RandomAccessFile("passwd.tmp","rw");
     
               
          //
          // Read through passwd file and find username
         // remove user if username found

      do {            
         passwdline=adduserwriter.readLine();                  // read line from users file
         
         if(passwdline == "\n") {              // empty line
             continue;             
         }
         
         if(passwdline == null) {              // end of file           
             break;             
         }
                    
         usersarray=passwdline.split(":");                  // split line
                      
         
         if(usersarray[PASSWD_USERNAME].equals(Username) == true) {  		// found user
          usersarray[PASSWD_USERNAME]=Username;
          usersarray[PASSWD_PASSWORD]=Password;           
          usersarray[PASSWD_USERTYPE]=UserType;
         }

	// write line
         TempFile.writeBytes(usersarray[PASSWD_USERNAME] + ":"+ hashpassword(usersarray[PASSWD_PASSWORD]) + ":" + usersarray[PASSWD_USERTYPE]+"\n");         
       }while(passwdline != null);
                
      adduserwriter.close();    
      TempFile.close();
      
      // Move temporary file to passwd
      
      Files.move(Paths.get("passwd.tmp"), Paths.get("passwd"), StandardCopyOption.REPLACE_EXISTING);
      
      } catch(Exception e) {
         System.out.println(e);
         
         Main.PasswdFileMutex.release();            // release mutex
         return(false);
     }

     Main.PasswdFileMutex.release();            // release mutex
     return(true);
    }
}

