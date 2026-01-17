package albums.albumsapicontract.exception;

public class CatalogNumberAlreadyExistsException extends RuntimeException {
    public CatalogNumberAlreadyExistsException(String catNum) {
        super("Album with catalog number = " + catNum + " already exists");
    }
}