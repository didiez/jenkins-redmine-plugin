package org.jenkinsci.plugins.redmine;

import hudson.Extension;
import hudson.MarkupText;
import hudson.model.Run;
import hudson.scm.ChangeLogAnnotator;
import hudson.scm.ChangeLogSet.Entry;
import java.util.Optional;

/**
 * ChangeLogAnnotator to create issue links for issue references en commit messages.
 *
 * @see https://redmine.org/projects/redmine/wiki/RedmineSettings#Referencing-issues-in-commit-messages
 * @author didiez 
 */
@Extension
public class RedmineLinkChangeLogAnnotator extends ChangeLogAnnotator {

  @Override
  public void annotate(Run<?, ?> build, Entry change, MarkupText text) {
    Optional.ofNullable(build.getParent().getProperty(RedmineWebsiteJobProperty.class))
        .flatMap(prop -> RedmineGlobalConfiguration.get().findWebsiteByName(prop.getWebsiteName()))
        .ifPresent(
            redmineWebsite -> {
              String url = redmineWebsite.getBaseUrl();
              redmineWebsite.getRedmineLinkMarkupProcessor().process(text, url);
            });
  }

  
}
