package net.devansh.Muse.entity;

import lombok.*;
import net.devansh.Muse.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;


@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class JournalEntry {
    @Id
    private ObjectId id;
    private LocalDateTime date;
    @NonNull
    private String title;
    private String content;
    private Sentiment sentiment;


}
