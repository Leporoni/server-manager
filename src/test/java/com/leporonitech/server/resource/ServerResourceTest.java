package com.leporonitech.server.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.leporonitech.server.enumeration.Status;
import com.leporonitech.server.model.Response;
import com.leporonitech.server.model.Server;
import com.leporonitech.server.repository.ServerRepository;
import com.leporonitech.server.service.impl.ServerServiceImpl;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ServerResource.class})
@ExtendWith(SpringExtension.class)
class ServerResourceTest {
    @Autowired
    private ServerResource serverResource;

    @MockBean
    private ServerServiceImpl serverServiceImpl;

    @Test
    void testGetServers() {
        ServerRepository serverRepository = mock(ServerRepository.class);
        when(serverRepository.findAll((org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        ResponseEntity<Response> actualServers = (new ServerResource(new ServerServiceImpl(serverRepository))).getServers();
        assertTrue(actualServers.getHeaders().isEmpty());
        assertTrue(actualServers.hasBody());
        assertEquals(HttpStatus.OK, actualServers.getStatusCode());
        Response body = actualServers.getBody();
        assertNull(body.getReason());
        assertEquals("Servers retrieved", body.getMessage());
        assertNull(body.getDeveloperMessage());
        assertEquals(1, body.getData().size());
        assertEquals(HttpStatus.OK, body.getStatus());
        assertEquals(200, body.getStatusCode());
        verify(serverRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testGetServers2() {
        ServerServiceImpl serverServiceImpl = mock(ServerServiceImpl.class);
        when(serverServiceImpl.list(anyInt())).thenReturn(new ArrayList<>());
        ResponseEntity<Response> actualServers = (new ServerResource(serverServiceImpl)).getServers();
        assertTrue(actualServers.getHeaders().isEmpty());
        assertTrue(actualServers.hasBody());
        assertEquals(HttpStatus.OK, actualServers.getStatusCode());
        Response body = actualServers.getBody();
        assertNull(body.getReason());
        assertEquals("Servers retrieved", body.getMessage());
        assertNull(body.getDeveloperMessage());
        assertEquals(1, body.getData().size());
        assertEquals(HttpStatus.OK, body.getStatus());
        assertEquals(200, body.getStatusCode());
        verify(serverServiceImpl).list(anyInt());
    }

    @Test
    void testSaveServer() {
        Server server = new Server();
        server.setStatus(Status.SERVER_UP);
        server.setId(123L);
        server.setImageUrl("https://example.org/example");
        server.setName("Name");
        server.setMemory("Memory");
        server.setIpAddress("42 Main St");
        server.setType("Type");
        ServerServiceImpl serverServiceImpl = mock(ServerServiceImpl.class);
        when(serverServiceImpl.create((Server) any())).thenReturn(server);
        ServerResource serverResource = new ServerResource(serverServiceImpl);

        Server server1 = new Server();
        server1.setStatus(Status.SERVER_UP);
        server1.setId(123L);
        server1.setImageUrl("https://example.org/example");
        server1.setName("Name");
        server1.setMemory("Memory");
        server1.setIpAddress("42 Main St");
        server1.setType("Type");
        ResponseEntity<Response> actualSaveServerResult = serverResource.saveServer(server1);
        assertTrue(actualSaveServerResult.getHeaders().isEmpty());
        assertTrue(actualSaveServerResult.hasBody());
        assertEquals(HttpStatus.OK, actualSaveServerResult.getStatusCode());
        Response body = actualSaveServerResult.getBody();
        assertNull(body.getReason());
        assertEquals("Server created", body.getMessage());
        assertNull(body.getDeveloperMessage());
        assertEquals(1, body.getData().size());
        assertEquals(HttpStatus.CREATED, body.getStatus());
        assertEquals(201, body.getStatusCode());
        verify(serverServiceImpl).create((Server) any());
    }

    @Test
    void testDeleteServer() {
        ServerRepository serverRepository = mock(ServerRepository.class);
        doNothing().when(serverRepository).deleteById((Long) any());
        ResponseEntity<Response> actualDeleteServerResult = (new ServerResource(new ServerServiceImpl(serverRepository)))
                .deleteServer(123L);
        assertTrue(actualDeleteServerResult.getHeaders().isEmpty());
        assertTrue(actualDeleteServerResult.hasBody());
        assertEquals(HttpStatus.OK, actualDeleteServerResult.getStatusCode());
        Response body = actualDeleteServerResult.getBody();
        assertNull(body.getReason());
        assertEquals("Server deleted", body.getMessage());
        assertNull(body.getDeveloperMessage());
        assertEquals(1, body.getData().size());
        assertEquals(HttpStatus.OK, body.getStatus());
        assertEquals(200, body.getStatusCode());
        verify(serverRepository).deleteById((Long) any());
    }

    @Test
    void testDeleteServer2() {
        ServerServiceImpl serverServiceImpl = mock(ServerServiceImpl.class);
        when(serverServiceImpl.delete((Long) any())).thenReturn(true);
        ResponseEntity<Response> actualDeleteServerResult = (new ServerResource(serverServiceImpl)).deleteServer(123L);
        assertTrue(actualDeleteServerResult.getHeaders().isEmpty());
        assertTrue(actualDeleteServerResult.hasBody());
        assertEquals(HttpStatus.OK, actualDeleteServerResult.getStatusCode());
        Response body = actualDeleteServerResult.getBody();
        assertNull(body.getReason());
        assertEquals("Server deleted", body.getMessage());
        assertNull(body.getDeveloperMessage());
        assertEquals(1, body.getData().size());
        assertEquals(HttpStatus.OK, body.getStatus());
        assertEquals(200, body.getStatusCode());
        verify(serverServiceImpl).delete((Long) any());
    }

    @Test
    void testGetServer() throws Exception {
        Server server = new Server();
        server.setStatus(Status.SERVER_UP);
        server.setId(123L);
        server.setImageUrl("https://example.org/example");
        server.setName("Name");
        server.setMemory("Memory");
        server.setIpAddress("42 Main St");
        server.setType("Type");
        when(this.serverServiceImpl.get((Long) any())).thenReturn(server);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/server/get/{id}", 123L);
        getResult.accept("https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.serverResource).build().perform(getResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(406));
    }

    @Test
    void testPingServer() throws Exception {
        Server server = new Server();
        server.setStatus(Status.SERVER_UP);
        server.setId(123L);
        server.setImageUrl("https://example.org/example");
        server.setName("Name");
        server.setMemory("Memory");
        server.setIpAddress("42 Main St");
        server.setType("Type");
        when(this.serverServiceImpl.ping((String) any())).thenReturn(server);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/server/ping/{ipAddress}", "42 Main St");
        getResult.accept("https://example.org/example");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.serverResource).build().perform(getResult);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(406));
    }
}

