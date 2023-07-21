package com.mycompany.shell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;


public class UserTest {

    @Test
    void testHashpassword() {
        assertEquals("040D030A070C080908030A0109000A0F0E04020E0F060F0201050B080700050E",
                (new User()).hashpassword("iloveyou"));
    }
   
    @Test
    void testCheckAuthentication() {
        assertEquals(true,
                (new User()).CheckAuthentication("admin","password"));
    }

   @Test
    void testCheckInvalidAuthentication() {
        assertEquals(false,
                (new User()).CheckAuthentication("admin","password"));
    }
        
    @Test
    void testGetUserName() {
        assertNull((new User()).GetUserName());
    }

    @Test
    void testGetPassword() {
        assertNull((new User()).GetPassword());
    }

    @Test
    void testGetUserType() {
        assertNull((new User()).GetUserType());
    }
    
    @Test
    void testAddUser() {
        assertEquals(false,
                (new User()).AddUser("testuser","password","standard"));
    }
    
     @Test
    void testRemoveUser() {
        assertEquals(false,
                (new User()).RemoveUser("testuser"));
    }
    
    @Test
    void testChangePassword() {
        assertEquals(false,
                (new User()).AddUser("testuser","newpassword","standard"));
    }

    @Test
    void testChangeUserType() {
        assertEquals(true,
                (new User()).AddUser("testuser","newpassword","admin"));
    }
    
    
}

