package com.pluralsight.conferencedemo.controllers;


import com.pluralsight.conferencedemo.model.Session;
import com.pluralsight.conferencedemo.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {

    // use to perform all CRUD operation
    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping
    public List<Session> list(){
        return sessionRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Session get(@PathVariable Long id) {
        return sessionRepository.getReferenceById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Session create(@RequestBody final Session session) {
        return sessionRepository.saveAndFlush(session);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        //todo : also need to check for children records before deleting
        // org.postgresql.util.PSQLException:
        // ERROR: update or delete on table "sessions" violates foreign key constraint "session_schedule_session_id_fkey" on table "session_schedule"

        // also delete speaker who is giving that session
        sessionRepository.deleteById(id);
    }

    @PutMapping("{id}")
    public Session update(@PathVariable Long id, @RequestBody Session session) {
        // because this is PUT, we expect all attributes to be passed in.
        // A PATCH would only need what we need to update
        //todo : add validation that all attributes are passed in, otherwise return a 400 bad payload

        Session existingSession = sessionRepository.getReferenceById(id);
        BeanUtils.copyProperties(session, existingSession, "session_id");
        return sessionRepository.saveAndFlush(existingSession);
    }

}
