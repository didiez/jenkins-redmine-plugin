package org.jenkinsci.plugins.redmine.RedmineWebsite

import lib.FormTagLib
import org.jenkinsci.plugins.redmine.Messages

namespace(FormTagLib).with {
  
    entry(field: "name", title: Messages.website_name(), help: descriptor.getHelpFile("name")) {
        textbox()
    }

    entry(field: "baseUrl", title: Messages.website_baseUrl(), help: descriptor.getHelpFile("baseUrl")) {
        textbox()
    }

    entry(field: "referencingKeywords", title: Messages.website_referencingKeywords(), help: descriptor.getHelpFile("referencingKeywords")) {
        textbox()
    }
  
}