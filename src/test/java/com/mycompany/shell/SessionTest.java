package com.mycompany.shell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.Socket;
import java.net.SocketException;

import org.junit.jupiter.api.Test;


public class SessionTest {

    @Test
    void testConstructor() throws SocketException {
        Socket socket = (new Session(new Socket())).socket;
        assertFalse(socket.isOutputShutdown());
        assertFalse(socket.isInputShutdown());
        assertFalse(socket.isConnected());
        assertFalse(socket.isClosed());
        assertFalse(socket.isBound());
        assertEquals(0, socket.getTrafficClass());
        assertFalse(socket.getTcpNoDelay());
        assertEquals(0, socket.getSoTimeout());
        assertEquals(-1, socket.getSoLinger());
        assertEquals(8192, socket.getSendBufferSize());
        assertFalse(socket.getReuseAddress());
        assertNull(socket.getRemoteSocketAddress());
        assertEquals(65536, socket.getReceiveBufferSize());
        assertEquals(0, socket.getPort());
        assertFalse(socket.getOOBInline());
        assertNull(socket.getLocalSocketAddress());
        assertEquals(-1, socket.getLocalPort());
        assertFalse(socket.getKeepAlive());
        assertNull(socket.getChannel());
        assertNull(socket.getInetAddress());
    }




}
