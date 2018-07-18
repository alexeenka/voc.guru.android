package guru.voc.vocguru;

public class AppVoice {

    public AppVoice() {
    }

    public AppVoice(String lang) {
        this.lang = lang;
    }

    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
