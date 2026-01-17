package albums.albums_application.graphql;

import albums.albums_application.service.AlbumService;
import albums.albumsapicontract.dto.*;
import com.netflix.graphql.dgs.*;
import graphql.schema.DataFetchingEnvironment;

import java.time.LocalDate;
import java.util.Map;

@DgsComponent
public class AlbumDataFetcher {

    private final AlbumService albumService;

    public AlbumDataFetcher(AlbumService albumService) {
        this.albumService = albumService;
    }

    @DgsQuery
    public AlbumResponse albumById(@InputArgument String id) {
        return albumService.findAlbumById(id);
    }

    @DgsQuery
    public PagedResponse<AlbumResponse> albums(@InputArgument String bandId, @InputArgument String genreFilter, @InputArgument int page, @InputArgument int size) {
        return albumService.findAllAlbums(bandId, genreFilter, page, size);
    }

    @DgsData(parentType = "Album", field = "band")
    public BandResponse band(DataFetchingEnvironment dfe) {
        AlbumResponse album = dfe.getSource();
        return album.getBand();
    }

    @DgsMutation
    public AlbumResponse createAlbum(@InputArgument("input") Map<String, Object> input) {
        AlbumRequest request = new AlbumRequest(
                (String) input.get("title"),
                (LocalDate) input.get("released"),
                (String) input.get("catNum"),
                (String) input.get("genre"),
                (String) input.get("bandId").toString()
        );
        return albumService.createAlbum(request);
    }

    @DgsMutation
    public AlbumResponse updateAlbum(@InputArgument String id, @InputArgument("input") Map<String, String> input) {
        UpdateAlbumRequest request = new UpdateAlbumRequest(
                input.get("title"),
                input.get("catNum")
        );
        return albumService.updateAlbum(id, request);
    }

    @DgsMutation
    public AlbumResponse updateAlbumGenre(@InputArgument String id, @InputArgument("input") Map<String, String> input) {
        return albumService.updateAlbumGenre(id, input.get("genre"));
    }

    @DgsMutation
    public String deleteAlbum(@InputArgument String id) {
        albumService.deleteAlbum(id);
        return id;
    }
}

