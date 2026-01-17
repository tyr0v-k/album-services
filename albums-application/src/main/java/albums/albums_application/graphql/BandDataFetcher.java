package albums.albums_application.graphql;

import albums.albums_application.service.BandService;
import albums.albumsapicontract.dto.BandRequest;
import albums.albumsapicontract.dto.BandResponse;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import java.util.List;
import java.util.Map;

@DgsComponent
public class BandDataFetcher {

    private final BandService bandService;

    public BandDataFetcher(BandService bandService) {
        this.bandService = bandService;
    }

    @DgsQuery
    public List<BandResponse> bands() {
        return bandService.findAll();
    }

    @DgsQuery
    public BandResponse bandById(@InputArgument String id) {
        return bandService.findById(id);
    }

    @DgsMutation
    public BandResponse createBand(@InputArgument("input") Map<String, String> input) {
        BandRequest request = new BandRequest(input.get("bandName"), input.get("bandCountry"));
        return bandService.create(request);
    }

    @DgsMutation
    public BandResponse updateBand(@InputArgument String id, @InputArgument("input") Map<String, String> input) {
        BandRequest request = new BandRequest(input.get("bandName"), input.get("bandCountry"));
        return bandService.update(id, request);
    }

    @DgsMutation
    public String deleteBand(@InputArgument String id) {
        bandService.delete(id);
        return id;
    }
}

