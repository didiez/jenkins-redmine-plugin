package org.jenkinsci.plugins.redmine;

import hudson.MarkupText;
import java.util.Arrays;
import junit.framework.TestCase;
import org.jenkinsci.plugins.redmine.RedmineLinkMarkupProcessor.RedmineIssueLinkMarkup;

public class RedmineLinkAnnotatorTest extends TestCase {

    private static final String REDMINE_BASE_URL = "http://redmine.local/";

    public void testIssueLinkSyntax() {
        assertAnnotatedTextEquals(
                "Nothing here.",
                "Nothing here.");
        assertAnnotatedTextEquals(
                "This numbers 1, 2, or 3 are not issue references.",
                "This numbers 1, 2, or 3 are not issue references.");
        assertAnnotatedTextEquals(
                "#42",
                "<a href='%s'>#42</a>",
                42);
        assertAnnotatedTextEquals(
                "IssueID 22",
                "<a href='%s'>IssueID 22</a>",
                22);
        assertAnnotatedTextEquals(
                "fixes 10,11,12",
                "<a href='%s'>fixes 10</a>,<a href='%s'>11</a>,<a href='%s'>12</a>",
                10, 11, 12);
        assertAnnotatedTextEquals(
                "references 110,111,112,113",
                "<a href='%s'>references 110</a>,<a href='%s'>111</a>,<a href='%s'>112</a>,<a href='%s'>113</a>",
                110, 111, 112, 113);
        assertAnnotatedTextEquals(
                "closes 210, 211",
                "<a href='%s'>closes 210</a>, <a href='%s'>211</a>",
                210, 211);
        assertAnnotatedTextEquals(
                "closes 210 211",
                "<a href='%s'>closes 210</a> <a href='%s'>211</a>",
                210, 211);
        assertAnnotatedTextEquals(
                "refs 310, 11, 4, 4120",
                "<a href='%s'>refs 310</a>, <a href='%s'>11</a>, <a href='%s'>4</a>, <a href='%s'>4120</a>",
                310, 11, 4, 4120);
        assertAnnotatedTextEquals(
                "refs 1&11&111&1111",
                "<a href='%s'>refs 1</a>&amp;<a href='%s'>11</a>&amp;<a href='%s'>111</a>&amp;<a href='%s'>1111</a>",
                1, 11, 111, 1111);
        assertAnnotatedTextEquals(
                "IssueID 21&11&100",
                "<a href='%s'>IssueID 21</a>&amp;<a href='%s'>11</a>&amp;<a href='%s'>100</a>",
                21, 11, 100);
        assertAnnotatedTextEquals(
                "refs #1,#11,#111,#1111",
                "<a href='%s'>refs #1</a>,<a href='%s'>#11</a>,<a href='%s'>#111</a>,<a href='%s'>#1111</a>",
                1, 11, 111, 1111);
        assertAnnotatedTextEquals(
                "refs #1, #11, #111,#1111",
                "<a href='%s'>refs #1</a>, <a href='%s'>#11</a>, <a href='%s'>#111</a>,<a href='%s'>#1111</a>",
                1, 11, 111, 1111);
        assertAnnotatedTextEquals(
                "refs #1",
                "<a href='%s'>refs #1</a>",
                1);
        assertAnnotatedTextEquals(
                "closes #1&#11",
                "<a href='%s'>closes #1</a>&amp;<a href='%s'>#11</a>",
                1, 11);
        assertAnnotatedTextEquals(
                "closes #1",
                "<a href='%s'>closes #1</a>",
                1);
        assertAnnotatedTextEquals(
                "IssueID #1 #11",
                "<a href='%s'>IssueID #1</a> <a href='%s'>#11</a>",
                1, 11);
        assertAnnotatedTextEquals( // ignore case
                "Fixes #123",
                "<a href='%s'>Fixes #123</a>",
                123);
        assertAnnotatedTextEquals( // ignore case
                "CLOSES #123 and #444",
                "<a href='%s'>CLOSES #123</a> and <a href='%s'>#444</a>",
                123, 444);
        assertAnnotatedTextEquals(
                "fixes #123 This is a commit message.",
                "<a href='%s'>fixes #123</a> This is a commit message.",
                123);
        assertAnnotatedTextEquals(
                "This is a commit message. fixes #123",
                "This is a commit message. <a href='%s'>fixes #123</a>",
                123);
        assertAnnotatedTextEquals(
                "refs #111 This is a commit message starting and ending with issue references closes #123",
                "<a href='%s'>refs #111</a> This is a commit message starting and ending with issue references <a href='%s'>closes #123</a>",
                111, 123);
        assertAnnotatedTextEquals(
                "This is a commit message with fixes #123 in between.",
                "This is a commit message with <a href='%s'>fixes #123</a> in between.",
                123);
        assertAnnotatedTextEquals(
                "fixes #123 [project-one] This is a commit message.",
                "<a href='%s'>fixes #123</a> [project-one] This is a commit message.",
                123);
        assertAnnotatedTextEquals(
                "fixes #123 The implementation details can be found in #123#note-4",
                "<a href='%s'>fixes #123</a> The implementation details can be found in <a href='%s'>#123#note-4</a>",
                123, "123#note-4");
        assertAnnotatedTextEquals(
                "Message with links to issue notes like #123#note-4 and #56#change-7890 (links found in emails)",
                "Message with links to issue notes like <a href='%s'>#123#note-4</a> and <a href='%s'>#56#change-7890</a> (links found in emails)",
                "123#note-4", "56#change-7890");
    }

    private void assertAnnotatedTextEquals(String originalText, String expectedAnnotatedPattern, Object... issueIds) {
        MarkupText markupText = new MarkupText(originalText);

        RedmineLinkMarkupProcessor processor
                = new RedmineLinkMarkupProcessor(
                        new RedmineIssueLinkMarkup(RedmineWebsite.DEFAULT_REFERENCING_KEYWORDS));

        processor.process(markupText, REDMINE_BASE_URL);

        String expected = String.format(expectedAnnotatedPattern, toUrl(issueIds));
        String actual = markupText.toString(true);
        
        System.out.println("expected = " + expected);
        System.out.println("actual   = " + actual);
        System.out.println("");

        assertEquals(expected, actual);
    }

    private Object[] toUrl(Object... issueIds) {
        return Arrays.asList(issueIds).stream()
                .map(issueId -> REDMINE_BASE_URL + "issues/" + issueId)
                .toArray();
    }
}
