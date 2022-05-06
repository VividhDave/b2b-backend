pipeline {
  agent any
  stages {
    stage ('Build') {
      steps {
        withMaven(maven : 'maven3.8') {
                    bat 'mvn clean install -DskipTests=true'
                }
      }
    }
  }
}
