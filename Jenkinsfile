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

    stage('Checking duplicate language label values') {
      steps{
        script {
          getScriptDuplicateLabelValues()
          def errorEn = sh script:"python3 ./pipeline/find_duplicate_label_values.py src/main/resources/translations/en", returnStatus:true
          def errorFr = sh script:"python3 ./pipeline/find_duplicate_label_values.py src/main/resources/translations/fr", returnStatus:true
          def errorEs = sh script:"python3 ./pipeline/find_duplicate_label_values.py src/main/resources/translations/es", returnStatus:true
          currentBuild.result = 'SUCCESS'
          if (errorEn == 1 || errorFr == 1 || errorEs == 1) {
            rtp  parserName: 'HTML', stableText:  '<h1>Duplicate label values found!!!</h1>'
          }
        }
      }
    }

    stage('Checking duplicate language label keys') {
      steps{
        script {
          getScriptDuplicateLabelValues()
          def errorEn = sh script:"python3 ./pipeline/find_duplicate_label_keys.py src/main/resources/translations/en", returnStatus:true
          def errorFr = sh script:"python3 ./pipeline/find_duplicate_label_keys.py src/main/resources/translations/fr", returnStatus:true
          def errorEs = sh script:"python3 ./pipeline/find_duplicate_label_keys.py src/main/resources/translations/es", returnStatus:true
          if (errorEn == 1 || errorFr == 1 || errorEs == 1) {
            rtp  parserName: 'HTML', stableText:  '<h1>Duplicate label keys found!!!</h1>'
          }
        }
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