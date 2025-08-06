package net.devansh.Muse.service;

import net.devansh.Muse.entity.JournalEntry;
import net.devansh.Muse.entity.User;
import net.devansh.Muse.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

//    @Transactional
    public void saveEntry(JournalEntry entry,String userName) throws Exception {
        try {
            User user = userService.findByUsername(userName);
            entry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(entry);
            user.getJournalEntries().add(saved);
            userService.saveEntry(user);
        } catch (Exception e) {
            System.err.println(e);
            throw new Exception("An error occurred while saving the entry" , e);
        }
    }

    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }


    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

//    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUsername(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.saveEntry(user);
                journalEntryRepository.deleteById(id);
            }

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting the entry", e);
        }
        return removed;

    }



}
