package org.jenkinsci.plugins.redmine;

import hudson.model.Action;

/**
 * Renders a link in the job page if the redmine website project name property is configured.
 * 
 * @author didiez
 */
public class RedmineProjectLinkAction implements Action {

    private final RedmineWebsiteJobProperty jobProperty;

    public RedmineProjectLinkAction(RedmineWebsiteJobProperty jobProperty) {
        this.jobProperty = jobProperty;
    }

    @Override
    public String getIconFileName() {
        return "/plugin/redmine/redmine-logo.svg";
    }

    @Override
    public String getDisplayName() {
        return "Redmine";
    }

    @Override
    public String getUrlName() {
        return RedmineGlobalConfiguration.get().findWebsiteByName(jobProperty.getWebsiteName())
                .map(website -> website.getBaseUrl() + "projects/" + jobProperty.getProjectName())
                .orElse(null);
    }
}
