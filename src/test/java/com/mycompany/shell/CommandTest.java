package com.mycompany.shell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CommandTest {

    @Test
    void testSetUserName() {
        (new Command()).SetUserName("janedoe");
        assertEquals("janedoe", Command.CommandUserName);
    }

    @Test
    void testShellGetCwd() {
        assertEquals(null, (new Command()).ShellGetCwd());
    }

    @Test
    void testShellChdir() {
        assertEquals(-1, Command.ShellChdir("Dirname"));
        assertEquals(-1, Command.ShellChdir(""));
        assertEquals(-1, Command.ShellChdir(".."));
        assertEquals(-1, Command.ShellChdir("Dirname"));
    }


    @Test
    void testValidateExternalCommand() {
        assertEquals("", (new Command()).ValidateExternalCommand("Command"));
    }

}
