package net.devansh.Muse.repository;

import net.devansh.Muse.entity.JournalEntry;
import net.devansh.Muse.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);

    void deleteByUsername(String name);
}
