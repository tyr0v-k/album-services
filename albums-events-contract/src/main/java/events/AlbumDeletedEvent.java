package events;

import java.io.Serializable;

public record AlbumDeletedEvent(String albumId) implements Serializable {
}
