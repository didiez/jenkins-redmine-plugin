package org.jenkinsci.plugins.redmine;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Job;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.ListBoxModel.Option;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import jenkins.model.OptionalJobProperty;
import jenkins.model.TransientActionFactory;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

/**
 * Redmine website job property.
 *
 * @author didiez
 */
public class RedmineWebsiteJobProperty extends OptionalJobProperty<Job<?, ?>> {

    private String websiteName;
    private String projectName;

    @DataBoundConstructor
    public RedmineWebsiteJobProperty(String websiteName, String projectName) {
        this.websiteName = websiteName;
        this.projectName = projectName;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    @DataBoundSetter
    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getProjectName() {
        return projectName;
    }

    @DataBoundSetter
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public Collection<? extends Action> getJobActions(Job<?, ?> job) {
        return Collections.EMPTY_SET;
    }

    @Extension
    @Symbol("redmine")
    public static final class DescriptorImpl extends OptionalJobPropertyDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.job_redmine_displayName();
        }

        public FormValidation doCheckWebsiteName(@QueryParameter String websiteName) {
            if (StringUtils.isBlank(websiteName)) {
                return FormValidation.error(Messages.job_redmine_websiteName_errors_empty());
            }
            return FormValidation.ok();
        }

        public ListBoxModel doFillWebsiteNameItems() {
            return new ListBoxModel(
                    RedmineGlobalConfiguration.get().getWebsites().stream()
                            .map(website -> new Option(website.getName()))
                            .collect(Collectors.toList()));
        }
    }
    
    @Extension(optional = true)
    public static class RedmineProjectLinkActionFactoryImpl extends TransientActionFactory<Job> {
        @Override
        public Class<Job> type() {
            return Job.class;
        }

        @Override
        public Class<? extends Action> actionType() {
            return RedmineProjectLinkAction.class;
        }

        @Override
        public Collection<RedmineProjectLinkAction> createFor(Job job) {
            RedmineWebsiteJobProperty jobProperty = (RedmineWebsiteJobProperty) job.getProperty(RedmineWebsiteJobProperty.class);
            if (jobProperty == null || StringUtils.isBlank(jobProperty.projectName)) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new RedmineProjectLinkAction(jobProperty));
        }
    }
}
