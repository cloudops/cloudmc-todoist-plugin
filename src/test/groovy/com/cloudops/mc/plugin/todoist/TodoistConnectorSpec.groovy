package com.cloudops.mc.plugin.todoist

import spock.lang.Specification


class TodoistConnectorSpec extends Specification {
   def connector = new TodoistConnector()

   def "Ensure that the parameters return are the right one"() {
      when:
      def result = connector.getParameters()
      then:
      result == [Credentials.URL, Credentials.TOKEN]
   }
}