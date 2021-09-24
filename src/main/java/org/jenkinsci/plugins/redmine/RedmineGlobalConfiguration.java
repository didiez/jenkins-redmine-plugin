package org.jenkinsci.plugins.redmine;

import hudson.Extension;
import hudson.model.Descriptor.FormException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Redmine plugin global configuration.
 *
 * @author didiez
 */
@Extension
@Symbol("redmine")
public class RedmineGlobalConfiguration extends GlobalConfiguration {

    private List<RedmineWebsite> websites = new ArrayList<>();

    public RedmineGlobalConfiguration() {
        super();
        load();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        websites.clear();
        req.bindJSON(this, formData);
        save();
        return true;
    }

    public List<RedmineWebsite> getWebsites() {
        return websites;
    }

    @DataBoundSetter
    public void setWebsites(List<RedmineWebsite> websites) {
        this.websites = websites;
    }

    public static RedmineGlobalConfiguration get() {
        return GlobalConfiguration.all().getInstance(RedmineGlobalConfiguration.class);
    }
    
    public Optional<RedmineWebsite> findWebsiteByName(String websiteName) {
        return websites.stream()
            .filter(website -> website.getName().equals(websiteName))
            .findFirst();
      }
}
