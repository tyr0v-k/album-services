package albums.albumsapicontract.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "albums", itemRelation = "album")
public class AlbumResponse extends RepresentationModel<AlbumResponse> {

    private final String id;
    private final String title;
    private final LocalDate released;
    private final String genre;
    private final String catNum;
    private final BandResponse band;
    private final LocalDateTime createdAt;

    public AlbumResponse(String id, String title, LocalDate released, String genre, String catNum, BandResponse band, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.released = released;
        this.genre = genre;
        this.catNum = catNum;
        this.band = band;
        this.createdAt = createdAt;
    }

    public String  getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getReleased() {
        return released;
    }

    public String getGenre() {
        return genre;
    }

    public String getCatNum() {
        return catNum;
    }

    public BandResponse getBand() {
        return band;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AlbumResponse that = (AlbumResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(released, that.released) && Objects.equals(genre, that.genre) && Objects.equals(catNum, that.catNum) && Objects.equals(band, that.band) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, title, released, genre, catNum, band, createdAt);
    }
}