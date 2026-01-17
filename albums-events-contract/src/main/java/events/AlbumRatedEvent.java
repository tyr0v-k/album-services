package events;

import java.io.Serializable;

public record AlbumRatedEvent(String albumId, Integer score, String verdict) implements Serializable {}