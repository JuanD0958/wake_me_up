package co.anbora.wakemeup.ui.model;

/**
 * Created by dalgarins on 03/24/18.
 */

public class NotificationViewModel {

    private String title;
    private String content;

    public NotificationViewModel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
