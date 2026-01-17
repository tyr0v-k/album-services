package albums.albumsapicontract.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Objects;

@Relation(collectionRelation = "bands", itemRelation = "band")
public class BandResponse extends RepresentationModel<BandResponse> {

    private final String id;
    private final String bandName;
    private final String bandCountry;

    public BandResponse(String id, String bandName, String bandCountry) {
        this.id = id;
        this.bandName = bandName;
        this.bandCountry = bandCountry;
    }

    public String getId() {
        return id;
    }

    public String getBandName() {
        return bandName;
    }

    public String getBandCountry() {
        return bandCountry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BandResponse that = (BandResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(bandName, that.bandName) && Objects.equals(bandCountry, that.bandCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, bandName, bandCountry);
    }
}

