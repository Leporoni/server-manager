package com.leporonitech.server.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.leporonitech.server.enumeration.Status;
import com.leporonitech.server.model.Server;
import com.leporonitech.server.repository.ServerRepository;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ServerServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ServerServiceImplTest {
    @MockBean
    private ServerRepository serverRepository;

    @Autowired
    private ServerServiceImpl serverServiceImpl;

    @Test
    void testPing() throws IOException {

        // Given
        Server server = new Server();
        server.setStatus(Status.SERVER_UP);
        server.setId(123L);
        server.setImageUrl("https://example.org/example");
        server.setName("Name");
        server.setMemory("Memory");
        server.setIpAddress("42 Main St");
        server.setType("Type");

        Server server1 = new Server();
        server1.setStatus(Status.SERVER_UP);
        server1.setId(123L);
        server1.setImageUrl("https://example.org/example");
        server1.setName("Name");
        server1.setMemory("Memory");
        server1.setIpAddress("42 Main St");
        server1.setType("Type");

        // When
        when(this.serverRepository.save((Server) any())).thenReturn(server1);
        when(this.serverRepository.findByIpAddress((String) any())).thenReturn(server);
        Server actualPingResult = this.serverServiceImpl.ping("42");

        // Then
        assertSame(server, actualPingResult);
        assertEquals(Status.SERVER_DOWN, actualPingResult.getStatus());
        verify(this.serverRepository).findByIpAddress((String) any());
        verify(this.serverRepository).save((Server) any());
    }

    @Test
    void testPing2() throws IOException {
        Server server = new Server();
        server.setStatus(Status.SERVER_UP);
        server.setId(123L);
        server.setImageUrl("https://example.org/example");
        server.setName("Name");
        server.setMemory("Memory");
        server.setIpAddress("42 Main St");
        server.setType("Type");

        Server server1 = new Server();
        server1.setStatus(Status.SERVER_UP);
        server1.setId(123L);
        server1.setImageUrl("https://example.org/example");
        server1.setName("Name");
        server1.setMemory("Memory");
        server1.setIpAddress("42 Main St");
        server1.setType("Type");
        when(this.serverRepository.save((Server) any())).thenReturn(server1);
        when(this.serverRepository.findByIpAddress((String) any())).thenReturn(server);
        Server actualPingResult = this.serverServiceImpl.ping("");
        assertSame(server, actualPingResult);
        assertEquals(Status.SERVER_UP, actualPingResult.getStatus());
        verify(this.serverRepository).findByIpAddress((String) any());
        verify(this.serverRepository).save((Server) any());
    }

    @Test
    void testList() {
        when(this.serverRepository.findAll((org.springframework.data.domain.Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(this.serverServiceImpl.list(1).isEmpty());
        verify(this.serverRepository).findAll((org.springframework.data.domain.Pageable) any());
    }

    @Test
    void testGet() {
        Server server = new Server();
        server.setStatus(Status.SERVER_UP);
        server.setId(123L);
        server.setImageUrl("https://example.org/example");
        server.setName("Name");
        server.setMemory("Memory");
        server.setIpAddress("42 Main St");
        server.setType("Type");
        Optional<Server> ofResult = Optional.of(server);
        when(this.serverRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(server, this.serverServiceImpl.get(123L));
        verify(this.serverRepository).findById((Long) any());
    }

    @Test
    void testUpdate() {
        Server server = new Server();
        server.setStatus(Status.SERVER_UP);
        server.setId(123L);
        server.setImageUrl("https://example.org/example");
        server.setName("Name");
        server.setMemory("Memory");
        server.setIpAddress("42 Main St");
        server.setType("Type");
        when(this.serverRepository.save((Server) any())).thenReturn(server);

        Server server1 = new Server();
        server1.setStatus(Status.SERVER_UP);
        server1.setId(123L);
        server1.setImageUrl("https://example.org/example");
        server1.setName("Name");
        server1.setMemory("Memory");
        server1.setIpAddress("42 Main St");
        server1.setType("Type");
        assertSame(server, this.serverServiceImpl.update(server1));
        verify(this.serverRepository).save((Server) any());
    }

    @Test
    void testDelete() {
        doNothing().when(this.serverRepository).deleteById((Long) any());
        assertTrue(this.serverServiceImpl.delete(123L));
        verify(this.serverRepository).deleteById((Long) any());
    }
}

