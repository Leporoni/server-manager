package com.leporonitech.server.service.impl;

import com.leporonitech.server.model.Server;
import com.leporonitech.server.repository.ServerRepository;
import com.leporonitech.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Random;

import static com.leporonitech.server.enumeration.Status.SERVER_DOWN;
import static com.leporonitech.server.enumeration.Status.SERVER_UP;
import static java.util.List.of;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService {

    private final ServerRepository repository;

    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return repository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP: {}", ipAddress);
        Server server = repository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? SERVER_UP : SERVER_DOWN);
        repository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return repository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by ID: {}", id);
        return repository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return repository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server by ID: {}", id);
        repository.deleteById(id);
        return Boolean.TRUE;
    }

    private String setServerImageUrl() {
        String[] imageNames = { "server1.png", "server2.png", "server3.png", "server4.png" };
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/server/image/" + imageNames[new Random().nextInt(4)]).toUriString();
    }
}
