package peak.manaslu.aspera.views;

public class View extends io.dropwizard.views.View {

    private final String basePath;

    private View(String basePath, String templateName) {
        super(templateName);
        this.basePath = basePath;
    }

    public String getBasePath() {
        if ("/".equals(basePath)) {
            return "";
        }
        return basePath;
    }

    public static class Builder {
        private String basePath;
        private String templateKey;

        public Builder basePath(String basePath) {
            this.basePath = basePath;
            return this;
        }

        public Builder templateKey(String templateKey) {
            this.templateKey = templateKey;
            return this;
        }

        private String getTemplateName() {
            if (basePath == null) {
                return "";
            }
            return "/views/" + templateKey + ".ftl";
        }

        public View build() {
            return new View(basePath, getTemplateName());
        }
    }

}
