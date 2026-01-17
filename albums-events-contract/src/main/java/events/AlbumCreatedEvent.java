package events;

import java.io.Serializable;

public record AlbumCreatedEvent(
        String albumId,
        String title,
        String bandNameCountry
) implements Serializable {}