package org.jenkinsci.plugins.redmine.RedmineGlobalConfiguration

import lib.FormTagLib
import org.jenkinsci.plugins.redmine.Messages

namespace(FormTagLib).with {
  
    section(title: "Redmine") {
        entry(title: Messages.websites()) {
            repeatableProperty(field: "websites") {
                entry {
                    div(align:'right') {
                        repeatableDeleteButton()
                    }
                }
            }
        }
    }
  
}

