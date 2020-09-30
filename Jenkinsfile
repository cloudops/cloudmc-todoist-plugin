@Library('cloudmc-jenkins-shared@master') _ 

pipeline {
  agent { label 'cmc' }
  options { disableConcurrentBuilds() }
  tools {
    maven 'maven-3.3.9' 
  }
  environment {
    GIT_URL = 'git@github.com:cloudops/cloudmc-todoist-plugin.git'
    JAR_LOCATION = 'target/cloudmc-todoist-plugin.jar'
    REPO_NAME = 'cloudmc-todoist-plugin'
  }
  stages {
    stage('Build, test & archive') {
      steps {
        executeMavenBuild()
      }
    }

    stage('Deploy') {
      when {
        expression { isDeployable(BRANCH_NAME) }
      }
      steps {
        deployPlugin env.JAR_LOCATION
      }
    }

  }
  post{
    always {
      junit 'target/surefire-reports/*.xml'
      notifySlack currentBuild.result
    }
  }
}