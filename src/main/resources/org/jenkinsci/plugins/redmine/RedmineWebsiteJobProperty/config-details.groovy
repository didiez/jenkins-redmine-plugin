package org.jenkinsci.plugins.redmine.RedmineWebsiteJobProperty

import lib.FormTagLib
import org.jenkinsci.plugins.redmine.Messages

namespace(FormTagLib).with {

    entry(field: "websiteName", title: Messages.job_redmine_websiteName(), help: descriptor.getHelpFile("websiteName")) {
        select()
    }
    entry(field: "projectName", title: Messages.job_redmine_projectName(), help: descriptor.getHelpFile("projectName")) {
        textbox()
    }
  
}
