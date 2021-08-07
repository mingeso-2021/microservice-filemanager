
pipeline {
	agent any
	stages {
        stage('Init') {
            steps {
                echo "Init"
            }
        }
		stage('SonarQube analysis') {
    			steps {
    			echo "sonarqube with Backend"
				dir("/var/lib/jenkins/workspace/dev-microservice-filemanager") { //nombre del proyecto en jenkins
					withSonarQubeEnv('sonarqube') { // Will pick the global server connection you have configured
						sh 'chmod +x ./gradlew'
						sh './gradlew sonarqube'
                    }
				}
			}
  		}
		stage('JUnit'){
			steps {
			    dir("/var/lib/jenkins/workspace/dev-microservice-filemanager/build/test-results/test") {
                    sh 'touch test.xml'
                    sh 'rm *.xml'
                }
				catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    			dir("/var/lib/jenkins/workspace/dev-microservice-filemanager") {
						sh './gradlew test'
					}
                }
				dir("/var/lib/jenkins/workspace/dev-microservice-filemanager/build/test-results/test") {
					junit '*.xml'
				}
			}
		}
		stage('End') {
            steps {
                echo "Deploying Backend"
            }
        }
	}
}