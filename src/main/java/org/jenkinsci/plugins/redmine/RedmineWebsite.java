package org.jenkinsci.plugins.redmine;

import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.redmine.RedmineLinkMarkupProcessor.RedmineIssueLinkMarkup;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

/**
 * Redmine website config.
 *
 * @author didiez
 */
public class RedmineWebsite extends AbstractDescribableImpl<RedmineWebsite> implements ExtensionPoint {

    public static final String DEFAULT_REFERENCING_KEYWORDS = "*,refs,references,IssueID,fixes,closes";

    private String name;
    private String baseUrl;
    private String referencingKeywords;

    private RedmineLinkMarkupProcessor redmineLinkMarkupProcessor;

    @DataBoundConstructor
    public RedmineWebsite(String name, String baseUrl, String referencingKeywords) {
        this.name = name;
        this.baseUrl = baseUrl;
        this.referencingKeywords = referencingKeywords;
        this.redmineLinkMarkupProcessor = new RedmineLinkMarkupProcessor(
                new RedmineIssueLinkMarkup(Optional.ofNullable(referencingKeywords).orElse(DEFAULT_REFERENCING_KEYWORDS)));
    }

    public String getName() {
        return name;
    }

    @DataBoundSetter
    public void setName(String name) {
        this.name = name;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    @DataBoundSetter
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getReferencingKeywords() {
        return referencingKeywords;
    }

    @DataBoundSetter
    public void setReferencingKeywords(String referencingKeywords) {
        this.referencingKeywords = referencingKeywords;
        this.redmineLinkMarkupProcessor = new RedmineLinkMarkupProcessor(
                new RedmineIssueLinkMarkup(Optional.ofNullable(referencingKeywords).orElse(DEFAULT_REFERENCING_KEYWORDS)));
    }

    RedmineLinkMarkupProcessor getRedmineLinkMarkupProcessor() {
        return redmineLinkMarkupProcessor;
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<RedmineWebsite> {

        @Override
        public String getDisplayName() {
            return Messages.website();
        }

        public FormValidation doCheckName(@QueryParameter String name) {
            if (StringUtils.isBlank(name)) {
                return FormValidation.error(Messages.website_name_errors_empty());
            }
            return FormValidation.ok();
        }

        public FormValidation doCheckBaseUrl(@QueryParameter String baseUrl) {
            if (StringUtils.isBlank(baseUrl)) {
                return FormValidation.error(Messages.website_baseUrl_errors_empty());
            }
            return FormValidation.ok();
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + Objects.hashCode(this.baseUrl);
        hash = 83 * hash + Objects.hashCode(this.referencingKeywords);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RedmineWebsite other = (RedmineWebsite) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.baseUrl, other.baseUrl)) {
            return false;
        }
        if (!Objects.equals(this.referencingKeywords, other.referencingKeywords)) {
            return false;
        }
        return true;
    }
}
