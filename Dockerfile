FROM alpine:3.6

ARG plugin=./plugin.jar

COPY ${plugin} todoist.jar

ENTRYPOINT exec cp todoist.jar /etc/cloudmc/plugins
