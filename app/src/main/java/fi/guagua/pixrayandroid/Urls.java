package fi.guagua.pixrayandroid;

public class Urls {
    public static final String URL_PIXRAY = "https://kide.biocenter.helsinki.fi/pixray-rest-api";
    public static final String URL_PARAM_API_KEY = "/key/";
    public static final String URL_API_KEY = "test";
    public static final String URL_PROJECTS = "/projects";
    public static final String URL_PROJECT = "/project/";
    public static final String URL_PLATES = "/plates";
    public static final String URL_PLATE = "/plate/";
    public static final String URL_DATE =  "/date/";
    public static final String URL_ROW = "/row/";
    public static final String URL_COLUMN = "/column/";
    public static final String URL_DROP = "/drop/";
    public static final String URL_TYPE = "/type/";
    public static final String URL_THUMBNAIL = "/thumbnail";
    public static final String URL_FORWARD_SLASH = "/";
    public static final String URL_SCORE_TYPES = "/score_types";
    public static final String URL_SCORE = "/score/";

    public static String getUrlSampleScreenScore(Image image) {
        // example: key/test/project/9/plate/38/date/2606/row/0/column/8/drop/1
        int projectId = image.getGalleryInfo().getProjectId();
        int plateId = image.getGalleryInfo().getPlateId();
        int dateId = image.getGalleryInfo().getRequestDateId();
        int[] rowColDrop = image.getRowColumnDrop();
        int row = rowColDrop[0];
        int col = rowColDrop[1];
        int drop = rowColDrop[2];
        return URL_PIXRAY + URL_PARAM_API_KEY + URL_API_KEY + URL_PROJECT +
                projectId + URL_PLATE + plateId + URL_DATE + dateId +
                URL_ROW + row + URL_COLUMN + col + URL_DROP + drop;
    }

    public static String getUrlScoreTypes() {
        // example: http://kide.biocenter.helsinki.fi/pixray-rest-api/key/test/score_types
        return URL_PIXRAY + URL_PARAM_API_KEY + URL_API_KEY + URL_SCORE_TYPES;
    }

    public static String getUrlProjects() {
        return URL_PIXRAY + URL_PARAM_API_KEY + URL_API_KEY + URL_PROJECTS;
    }

    public static String getUrlPlates(int projectId) {
        return URL_PIXRAY + URL_PARAM_API_KEY + URL_API_KEY +
                URL_PROJECT + projectId + URL_PLATES;
    }

    public static String getUrlSinglePlate(int projectId, int plateId) {
        return URL_PIXRAY + URL_PARAM_API_KEY + URL_API_KEY + URL_PROJECT +
                projectId + URL_PLATE + plateId;
    }

    public static String getUrlImageGallery(int projectId, int plateId, int dateId) {
        return getUrlSinglePlate(projectId, plateId) + URL_DATE + dateId;
    }

    public static String getUrlImageThumbnail(String urlPhotoGallery, int typeId, int[] rowColDrop) {
        int row = rowColDrop[0];
        int col = rowColDrop[1];
        int drop = rowColDrop[2];
        String url = urlPhotoGallery + URL_ROW + row + URL_COLUMN + col + URL_DROP +
                     drop + URL_TYPE + typeId + URL_THUMBNAIL; // set type to be 0 for thumbnails
        return url;
    }

    public static String newUrlThumbnailDateChanged(String oldUrlThumbnail, int dateId) {
        int i = oldUrlThumbnail.indexOf(URL_DATE);
        String s1 = oldUrlThumbnail.substring(0, i + URL_DATE.length()) + dateId;
        String s2 = oldUrlThumbnail.substring(i + URL_DATE.length());
        int j = s2.indexOf(URL_FORWARD_SLASH);
        s2 = s2.substring(j); // get rid of old date id
        return s1 + s2;
    }

    public static String newUrlThumbnailTypeChanged(String oldUrlThumbnail, int typeId) {
        int i = oldUrlThumbnail.indexOf(URL_TYPE);
        return oldUrlThumbnail.substring(0, i + URL_TYPE.length()) + typeId + URL_THUMBNAIL;
    }

    public static String getLargeImageUrl(String thumbnailUrl) {
        // replace "thumbnail" at the end of thumbnailUrl with "image".
        // endIndex is exclusive.
        int endIndex = thumbnailUrl.length() - "thumbnail".length();
        return thumbnailUrl.substring(0, endIndex) + "image";
    }

    // Generate url for the PUT request to send new score to remote server.
    public static String urlPutNewScore(Image image, int newScoreId) {
        // "/key/:key/project/:prid/plate/:plid/date/:did/row/:row/column/:col/drop/:drop/score/:score";
        int projectId = image.getGalleryInfo().getProjectId();
        int plateId = image.getGalleryInfo().getPlateId();
        int dateId = image.getGalleryInfo().getRequestDateId();
        int[] rowColDrop = image.getRowColumnDrop();
        int row = rowColDrop[0];
        int col = rowColDrop[1];
        int drop = rowColDrop[2];

        return URL_PIXRAY + URL_PARAM_API_KEY + URL_API_KEY + URL_PROJECT + projectId +
                URL_PLATE + plateId + URL_DATE + dateId + URL_ROW + row + URL_COLUMN +
                col + URL_DROP + drop + URL_SCORE + newScoreId;
    }

}

