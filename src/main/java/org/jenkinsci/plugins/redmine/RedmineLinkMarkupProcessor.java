package org.jenkinsci.plugins.redmine;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import hudson.MarkupText;
import hudson.MarkupText.SubText;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Redmine links markup text processor.
 *
 * @author didiez
 */
class RedmineLinkMarkupProcessor {

    private final List<RedmineLinkMarkup> redminelinkMarkups;

    RedmineLinkMarkupProcessor(RedmineLinkMarkup... redminelinkMarkups) {
        this.redminelinkMarkups = ImmutableList.copyOf(redminelinkMarkups);
    }

    void process(MarkupText text, String url) {
        redminelinkMarkups.forEach(linkMarkup -> linkMarkup.process(text, url));
    }

    interface RedmineLinkMarkup {

        void process(MarkupText text, String baseUrl);
    }

    /**
     * Link markup to create issue links for issue references en commit
     * messages.
     *
     * @see https://redmine.org/projects/redmine/wiki/RedmineSettings#Referencing-issues-in-commit-messages
     */
    static class RedmineIssueLinkMarkup implements RedmineLinkMarkup {

        private static final Pattern ISSUE_ID_PATTERN = Pattern.compile("#{0,2}(\\d+(?:#(?:\\bnote\\b|\\bchange\\b)-\\d+)?)"); // #1 | #1#note-3 | #1#change-4
        private static final String HREF_REPLACEMENT = "issues/$1";

        private final Pattern pattern;

        public RedmineIssueLinkMarkup(String referencingKeywords) {
            String keywordsRegex = Joiner.on(" |").join(referencingKeywords.trim().split("\\s*,\\s*"))
                    .concat(" ").replaceAll("\\* ", "#");
            this.pattern = Pattern.compile("(?i)(?:" + keywordsRegex + ")#?((?:\\d|,| #| \\d|&|#|note-|change-)+)");
        }

        @Override
        public void process(MarkupText text, String baseUrl) {
            text.findTokens(pattern).forEach(issuesText -> {
                boolean first = true;
                for (SubText issueId : issuesText.findTokens(ISSUE_ID_PATTERN)) {
                    if (first) {
                        issuesText
                                .subText(0, issueId.end() - issuesText.start())
                                .surroundWithLiteral("<a href='" + baseUrl + issueId.replace(HREF_REPLACEMENT) + "'>", "</a>");
                        first = false;
                    } else {
                        issueId.surroundWith("<a href='" + baseUrl + HREF_REPLACEMENT + "'>", "</a>");
                    }
                }
            });
        }
    }
}
