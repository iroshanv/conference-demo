package com.pluralsight.conferencedemo.controllers;


import com.pluralsight.conferencedemo.model.Speaker;
import com.pluralsight.conferencedemo.repositories.SpeakerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakerController {

    // use to perform all CRUD operation
    @Autowired
    private SpeakerRepository speakerRepository;

    @GetMapping
    public List<Speaker> list() {
        return speakerRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Speaker get(@PathVariable Long id) {
        return speakerRepository.getReferenceById(id);
    }

    @PostMapping // by default returns 200
    public Speaker create(@RequestBody final Speaker speaker) {
        return speakerRepository.saveAndFlush(speaker);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        //todo: update or delete on table "speakers" violates foreign key constraint "session_speakers_speaker_id_fkey" on table "session_speakers"

        // possible solution
        // also delete all the sessions which is assigned to particular speaker \
        speakerRepository.deleteById(id);
    }

    @PatchMapping("{id}")
    public Speaker update(@PathVariable Long id, @RequestBody Speaker speaker) {
        //todo : add validation that all attributes are passed in, otherwise return a 400 bad payload
        Speaker existingSpeaker = speakerRepository.getReferenceById(id);
        BeanUtils.copyProperties(speaker, existingSpeaker, "speaker_id");
        return speakerRepository.saveAndFlush(existingSpeaker);
    }

}
