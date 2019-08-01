library(identifier: 'utils@v2.4.0', retriever: modernSCM(
  [$class: 'GitSCMSource',
   remote: 'git@github.com:cloudops/cloudmc-jenkins-shared.git',
   credentialsId: 'gh-jenkins']))

pipeline {
  agent { label 'cmc' }
  options { disableConcurrentBuilds() }
  tools {
    maven 'maven-3.3.9' 
  }
  environment {
    GIT_URL = 'git@github.com:cloudops/cloudmc-todoist-plugin.git'
    JAR_LOCATION = 'target/cloudmc-todoist-plugin.jar'
    SONAR_KEY = 'com.cloudops:cloudmc-todoist-plugin'
    SONAR_TEST_PATHS = 'src/test/groovy'
  }
  stages {
    stage('Setup') {
      steps {
        deleteDir()
        git credentialsId: 'gh-jenkins', url: env.GIT_URL, branch: env.BRANCH_NAME
      }
    }

    stage('Build, test & archive') {
      steps {
        mvn 'deploy'
      }
    }

    stage('Static code analysis') {
      when {
        anyOf { branch 'devel'; branch 'dev-sdk-next' }
      }
      steps{
        kickoffStaticCodeAnalysis getSonarKey(env.SONAR_KEY), env.SONAR_TEST_PATHS
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

    stage('Quality gate') {
      when {
        anyOf { branch 'devel'; branch 'dev-sdk-next' }
      }
      steps{
        processStaticCodeAnalysisResult()
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