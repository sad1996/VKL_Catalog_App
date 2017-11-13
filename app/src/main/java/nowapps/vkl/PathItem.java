package nowapps.vkl;

/**
 * Created by Sadakathulla on 23-09-2017.
 */

class PathItem {
    private String path, name;
    Boolean isItemSelected;

    public PathItem() {
    }

    public PathItem(String path, String name) {
        this.path = path;
        this.name = name;
        this.isItemSelected = false;

    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

