package com.mycompany.shell;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;

/**
 *
 * @author matt
 */

/**\defgroup commands
 *
 * Java shell commands
 *
 * Java shell allows the execution of a number of internal commands and external commands
 *
*/

/**\defgroup TechnicalDetails
    * 
    * \class main
    * @brief Command interpreter class
    * 
    * @author Matthew Boote
    *
    * @details Is the command processing system of the shell
    *
    */
public class Command {
   
    
    /**
     *\addtogroup TechnicalDetails
     * String CommandUserName is the username of the user that is using this command class
    */
    static String CommandUserName;              // user name
    
    /**
     *\addtogroup TechnicalDetails
     * @var String RealCurrentDir is the real directory name used to access files and directories    
     *
     * See \ref FilesAndDirectories for more information about file and directory names in Java Shell
    */
    
    static String RealCurrentDir;               // real path to current directory
    
    /**
     *\addtogroup TechnicalDetails
     * @var String FakeCurrentDir is the modified directory name used to generate file and directory names used within the shell
     *
     * See \ref FilesAndDirectories for more information about file and directory names in Java Shell
     */

    static String FakeCurrentDir;               // fake path
    
     /**
     *\addtogroup TechnicalDetails
     * @var String RealRootDir is the directory that contains the user home directories and is used
     * to generate path names within the shell
     *
     * See \ref FilesAndDirectories for more information about file and directory names in Java Shell
     */
    static String RealRootDir;                      // real path to home directory
 
   /**
    *\addtogroup TechnicalDetails
    * 
    * SetUserName
    * @brief Set the user name for the command line
    * 
    * @author Matthew Boote
    *
    * @details Is used within the command class to gener
    *
    * @param[in] String[] args Any arguments to pass to the server
    *
    * @param[out] None
    *
    * @return None
    *
    * @throw General exception  
    */
    
   /**
    *\addtogroup FileAndDirectorymmands
    * Commands
    *
    * This program supports the following commands
    *
    */
    
    public void SetUserName(String username) {       
     File hd=new File("shellroot");             // get fake root directory
     RealRootDir=hd.getAbsolutePath()+"/"+username;          // get absolute path 
     
     RealCurrentDir=RealRootDir;                // set real current directory
     FakeCurrentDir="/";                        // set fake current directory
     
     CommandUserName=username;     // get username
    }
 
 /**
 /*\addtogroup commands
  * Commands
  *
  * This program supports the following commands
  *
  */
    
    /**
    *\addtogroup TechnicalDetails
    * 
    * ExecuteCommand
    * @brief Executes a command
    * 
    * @author Matthew Boote
    *
    * @details ExcuteCommand first checks if the command is an internal command and then checks if it is a external command.    
    *
    * @param[in] User user          The user object created on connection. See \ref SessionThread
    *
    * @param[in] BufferedReader in  A BufferedReader object used to read input. This object should be the Buffered  
    *
    * @param[in] PrintWriter user   A PrintWriter object used to write output
    *    
    * @param[in] String argsarray[] A String array that contains the command to be executed.
    *    
    * @param[out] Integer that is 0 on success or -1 on failure
    *
    * @return None
    *
    * @throw General exception  
    */
  
 public int ExecuteCommand(User user,BufferedReader in,PrintWriter out,String argsarray[]) {             
     int retval=0;
     String accessdenied="Access denied!";
     String not_enough_parameters="Not enough parameters";
     Path sourcefile;
     Path destfile;
     File dest;
 
     
     /**\addtogroup commands
      * The super command can execute privileged administrator-only commands that change the
      * configuration of the shell using the @code super @endcode command
      *      
      *@code        
      * super [command] {arguments}
      *@endcode
      *
      * Where command can be any of the following:
      *    
       */
         if(argsarray[0].equals("super") == true ) {           
             if(user.GetUserType().equals("admin") == false) {  // If not administrator user
                 out.println(accessdenied);
                 return(0);
             }
                         
             switch(argsarray[1]) {
                 /** \addtogroup commands
                  * addUser
                  *
                  *@code addUser {user name} {password} {user type }
                  *@endcode
                  *
                  * Adds a new user
                  *
                  * Where @code{user name}@endcode is the username
                    @code{password }@endcode is the password
                    @code{ user type}@endcode is the type of user; either standard to create an ordinary user or admin to create and administrator
                 *
                 */
                 case "addUser":             
                    if(argsarray.length < 3) {         // Not enough parameters
                     out.println(not_enough_parameters);
                     return(0);
                    }
             
                    if(user.AddUser(argsarray[2],argsarray[3],argsarray[4]) == false) {      // add user
                     out.println("Error adding user"); 
                    }
                    else
                    {
                      out.println("New user "+argsarray[2]+" added");
                    }
                    
                    return(0);
      
                 /** \addtogroup commands
                  * delUser
                  *
                  * Removes a user
                  *
                  *@code delUser {user name}
                  *@endcode
                  * 
                  * Where {user name} is the username
                  */
      
                case "delUser":                                         // Remove user                  
                    if(argsarray.length < 3) {         // Not enough parameters
                        out.println(not_enough_parameters);
                        return(0);
                    }
             
                    if(user.RemoveUser(argsarray[2]) == false) {      // remove user
                      out.println("Error removing user"); 
                    }

                    
                    out.println("User "+argsarray[2]+" removed");
                    
                /** \addtogroup commands
                 * chUserType
                 *
                 * Changes the type of a user
                 *
                 *@code chUserType {user name} {user type}@endcode
                 * 
                 * Where {username is the user name and {user type} is the user type to change to
                 * either <b> standard </b> for ordinary users and <b> admin </b> for administrators.
                 *
                 */
                case "chUserType":
                    if(argsarray.length < 3) {         // Not enough parameters
                        out.println(not_enough_parameters);
                        return(0);
                    }
                    
                    // Change user type
                    if(user.UpdateUser(user.GetUserName(),user.GetPassword(),argsarray[2]) == false) {
                     out.println("Error updating user"); 
                    }

                    return(0);
              
                /** \addtogroup commands
                 * chPass
                 *
                 * Changes the password for another user
                 *
                 *@code chUserType {user name} {password}@endcode
                 * 
                 * Where {username is the user name and {password} is the password
                 *                 
                 */
                case "chPass":
                    if(argsarray.length < 3) {         // Not enough parameters
                     out.println(not_enough_parameters);
                     return(0);
                    }
            
                    // Change password
                    user.UpdateUser(user.GetUserName(),argsarray[2],user.GetUserType());
                    
                default:
                    out.println("Unknown superuser command "+argsarray[1]);
                    return(-1);
             }
         }
          
         /** User commands  */
         
     switch(argsarray[0]) {                                         // command
     
        /** \addtogroup commands
         * logoff
         *
         * Disconnects the user.
         *
         *@code logoff@endcode
         *        
         */    
         case "logoff":                                                // exit
             out.println("Goodbye.");                            
             return(0);
    
              
        /** \addtogroup commands
         * copy
         *
         * Copies a file
         *         
         *@code copy [source file] [destination file]@endcode
         *
         * WARNING: EXISTING FILES ARE OVERWRITTEN. Use at your own risk
         *
         */    
         case "copy":                                                // exit            
             if(argsarray.length < 3) {         // Not enough parameters
                out.println(not_enough_parameters);
                return(0);
             }
            
             sourcefile=Paths.get(RealCurrentDir+"/"+argsarray[1]);       // get source
             
             //
             // Check if is directory and append source filename to destination path if it is
             //
             destfile=Paths.get(RealCurrentDir+"/"+argsarray[2]);       // get destinatrion
            
             dest=new File(destfile.toString());
             
             if(dest.isDirectory()) {               // If is directory
                  destfile=Paths.get(RealCurrentDir+"/"+argsarray[2]+"/"+argsarray[1]);       // get destinatrion 
             }
             
             try {              
               Files.copy(sourcefile,destfile,REPLACE_EXISTING);
                
             } catch(Exception e) {
                System.out.println(e);
             }
             
             return(0);
  
        /** \addtogroup commands
         * move
         *
         * Moves a file
         *         
         *@code move [source file] [destination file]@endcode
         *
         * WARNING: EXISTING FILES ARE OVERWRITTEN. Use at your own risk
         *
         */    
         case "move":                                                // exit
             if(argsarray.length < 3) {         // Not enough parameters
                out.println(not_enough_parameters);
                return(0);
             }
            
             sourcefile=Paths.get(RealCurrentDir+"/"+argsarray[1]);       // get source
             destfile=Paths.get(RealCurrentDir+"/"+argsarray[2]);       // get destinatrion
            
             dest=new File(destfile.toString());
             
             if(dest.isDirectory()) {               // If is directory
                  destfile=Paths.get(RealCurrentDir+"/"+argsarray[2]+"/"+argsarray[1]);       // get destinatrion 
             }

             try {
                Files.copy(sourcefile,destfile,REPLACE_EXISTING);
             } catch(Exception e) {
                out.println("Error copying file");
             }
             
             return(0);
             
        /** \addtogroup commands
         * cd
         *
         * Changes the current directory
         *
         *@code cd {directory name}
         * 
         * Where {directory name} is the directory to change to 
         *
         * The user, both administrators and standard users are restricted in which directories they are are
         * able to access; the user will not able able to change to any directory outside of their
         * private directory
         */
         case "cd":                                                 // set current directory                     
             if(argsarray.length == 1) {                            // no directory name
              retval=ShellChdir("/");                               // change to home directory             
             }
             else
             {
              retval=ShellChdir(argsarray[1]);                      //set current directory
             }
             
             if(retval == -1) {                                     // error 
              out.println("cd: No such file or directory\n");
              return(0);  
             }
             
             return(0);

       /** \addtogroup commands
        * showDir
        *
        * Displays the current directory
        *
        *@code showDir@endcode
        * 
        * The directory name displayed will only be a <i>virtual</i> path that is 
        * within the user's directory
        *
        */             
         case "showDir":                                               // show current directory
            out.println(FakeCurrentDir);              // show fake directory
            return(0);

       /** \addtogroup commands
        * showDir
        *
        * Displays the current directory
        *
        *@code showDir@endcode
        * 
        * The directory name displayed will only be a <i>virtual</i> path that is 
        * within the user's directory
        *
        */              
         case "whoAmI":                                            // show username
            out.println(CommandUserName);
            return(0);

       /** \addtogroup commands
        * chPass
        *
        * Changes the password for the current user
        *
        *@code chPass {password}@endcode
        * 
        * Where {password} is the new password
        *
        */ 
         case "chPass":
            if(argsarray.length < 2) {         // Not enough parameters
                out.println(not_enough_parameters);
                return(0);
             }
            
            // Change password
             user.UpdateUser(user.GetUserName(),argsarray[1],user.GetUserType());
             return(0);
             
         default:                                                 // Not built-in command             
             
             System.out.println("EXTERNAL");
             
             if(ExecuteExternalCommand(in,out,argsarray) == -1) {                // execute external command
                 out.println("Command not found "+argsarray[0]);
                 return(-1);
             }
     }
     
     return(0);
 }   

  /** \addtogroup commands
        * External commands
        *
        * Java shell is able to execute external commands.
        *
        * They are invoked in the same way that internal commands are:
        *
        *@code {command} {arguments}@endcode
        *
        * Example:
        *
        * @code ls *.txt @endcode
        *
        * Displays all the files ending in .txt
        *
        */ 
  
  /**
 /*\addtogroup commands
  * Commands
  *
  * This program supports the following commands
  *
    */
    
    /**
    *\addtogroup TechnicalDetails
    * 
    * ExecuteExternalCommand
    * @brief Executes an external command
    * 
    * @author Matthew Boote
    *
    * @details ExcuteExternalCommand executes commands using the PATH enviroment variable. It filters
    *
 
    * @param[in] User user          The user object created on connection. See \ref SessionThread
    *
    * @param[in] BufferedReader in  A BufferedReader object used to read input. This object should be the BufferedReader object created by the user session.
    *
    * @param[in] PrintWriter user   A PrintWriter object used to write output
    *    
    * @param[in] String argsarray[] A String array that contains the command to be executed.
    *            
    * @return Integer that is 0 on success or -1 on failure
    *
    * @throw General exception  
    */
 public int ExecuteExternalCommand(BufferedReader in,PrintWriter out,String commandarray[]) {
        File commandpath;
        String[] patharray=new String[255];
        int count;  
        String originalcommand=commandarray[0];             // save command so it can be restored to hide path name
        String realcommand;
        String newline;
        
        realcommand=ValidateExternalCommand(commandarray[0]);        // validate get command
        
        if(realcommand == "") {         // Invalid command
            out.println("Command not authorised");
            return(-1);
        }
        
        commandarray[0]=realcommand;                                // put real command into array

        //
        // go through the arguments and check if they are paths                    
        // if they are replace them with the real root path+path
        //
        for(count=1;count != commandarray.length;count++) {           
                        
            if(commandarray[count].contains("/") || commandarray[count].contains("..")) {                 // is path
                
                commandpath=new File(commandarray[count]);          
                
                try {
                 commandarray[count]=commandpath.getCanonicalPath();                    //  get canonical path
                } catch(Exception e) {
                    return(-1);
                }
                          
                if(commandarray[count].startsWith("/") == true) {               // if starting with /
                    commandarray[count]=RealRootDir+commandarray[count];        // add root                     
                }
                else
                {
                    commandarray[count]=RealRootDir+"/"+commandarray[count];        // add root/dirname                               
                }
                
            }
        }

        //
        // Start process
        //
        var processBuilder = new ProcessBuilder();
        
        processBuilder.command(commandarray);
        processBuilder.directory(new File(RealCurrentDir));                 // set current directory
        
        try {
            var process=processBuilder.start();                             // start process
           
            String line;
            
            //
            // Read output from process and display it
            ///
            
            var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));   // reader for process output
            
            // Until end of output read it and display it
            
            while ((line = reader.readLine()) != null) {
                
                if(line.contains(RealRootDir)) {                // replace references to real root directory
                  newline=line.replace(RealRootDir,"/");    
                  line=newline;
                }
                                
                out.println(line);
            }         

            commandarray[0]=originalcommand;                // restore command name
            
            return(process.waitFor());                      // wait for command to complete
            
        } catch(Exception e) {                              // On exception, return -1
            commandarray[0]=originalcommand;                // restore command name

            return(-1);
        }
 }
    
    //
    // Get current directory
    //
 
    public String ShellGetCwd() {            
      return(FakeCurrentDir);
    }
    
    
    /**
    *\addtogroup TechnicalDetails
    * 
    *ShellChdir
    * @brief Sets the current directory
    * 
    * @author Matthew Boote
    *
    * @details ShellChdir sets the current directory for the command object. If affects file accesses to relative paths.
    *
 
    * @param[in] String dirname     The directory to change to
    *                   
    * @return Integer that is 0 on success or -1 on failure
    *
    * @throw General exception  
    */
    //
    // Set current directory
    //
    public static int ShellChdir(String dirname) {
     File cd;
     Path path;
     File fakepath;
     String NewRealDir;
     
     if(dirname.equals("/")) {
      cd=new File(RealRootDir);
      path=Paths.get(RealRootDir);                   // get path
     }
     else
     {
      cd=new File(RealCurrentDir+"/"+dirname);
      path=Paths.get(RealCurrentDir+"/"+dirname);                   // get path

     }
          
     if((Files.exists(path) == false) || (Files.isDirectory(path) == false)) {              // path doesn't exist            
      return(-1);
     }
     
     // getCanonicalPath resolves . and .. in the path
    try {
     NewRealDir=cd.getCanonicalPath();               // get absolute path 
    }  catch(Exception e) {
        return(-1);
    }
    
    if(NewRealDir.startsWith(RealRootDir) == false) {               // outside of virtual root directory
        return(-1);
    }
    
    // get fake path
    fakepath=new File(dirname);
    
    try {
      FakeCurrentDir=fakepath.getCanonicalPath();  
    }  catch(Exception e) {
        return(-1);
    }

    if("/".equals(FakeCurrentDir)) {                   // If fake root
       FakeCurrentDir="/";
    }
    else
    {
      FakeCurrentDir=NewRealDir.replace(RealRootDir,"");
    }
    
    RealCurrentDir=NewRealDir;
        
    return(0);
  }
   

   /**
    *\addtogroup TechnicalDetails
    * 
    * ValidateExternalCommand
    *
    * @brief Validates command
    * 
    * @author Matthew Boote
    *
    * @details ValidateExternalCommand validates the command by checking the user's list of permitted commands.
    *
 
    * @param[in] String command     The external command to be executed
    *    
    * @return  If the command is permitted, it returns the full (real) path of the command, otherwise it returns an emp
    *
    * @throw General exception  
    */    
//
// Validate command
//
// Checks command against list of permitted commands
//
// Returns real path to executable
// or empty string on error
    
 String ValidateExternalCommand(String command) {
   String commandline=new String();
   String[] commandarray=new String[255];
   BufferedReader fread=null;
   
// Open command list      
     try {
          fread=new BufferedReader(new FileReader(CommandUserName+".command.dat"));		
          
      } catch(Exception e) {
         System.out.println("Error opening "+CommandUserName+".command.dat");
         return("");
     }

// 
// Read file line by line, split line into array and compare

      do {            
          try {
           commandline=fread.readLine(); 
             } catch(Exception e) {
            System.out.println("Error reading command.dat");
            return("");
          }
         
         if((commandline == null) || (commandline.length() == 0)) {              // end of file                      
            try {
              fread.close();
           } catch(IOException e) {
              System.out.println("Error reading "+CommandUserName+".command.dat");
              return("");            
           }
            
            return("");
         }
         
         commandarray=commandline.split(":");                  // split line                           
         
	 if(commandarray[0].equals(command)) {              // check command matches         
           try {
              fread.close();
           } catch(IOException e) {
              System.out.println("Error reading "+CommandUserName+".command.dat");
              return("");
           }
                           
           return(commandarray[1]);                     // return real path to executable
         }
      
       }while(commandline != null);
   
     try {
      fread.close();
     } catch(IOException e) {
       System.out.println("Error closing "+CommandUserName+".command.dat");
     }
     
     return("");
    }


}
