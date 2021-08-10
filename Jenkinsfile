
pipeline {
	agent any
	environment{
		DOCKERHUB_CREDENTIALS = credentials('fanunez-dockerhub');
	}
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
		stage('Docker Build'){
            steps{
                dir("/var/lib/jenkins/workspace/dev-microservice-filemanager"){
                    sh 'docker build --build-arg JAR_FILE=build/libs/*.jar -t fanunez/filemanager-mingeso .'
                }
            }
        }
        stage('Login'){
            steps{
                sh  'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }
        stage('Docker Hub'){
            steps{
                dir("/var/lib/jenkins/workspace/dev-microservice-filemanager"){
                    sh 'docker push fanunez/filemanager-mingeso'
                }
            }
		}
		stage('End') {
            steps {
                echo "Deploying Backend"
            }
        }
	}
	post {
		always {
			sh 'docker logout'
		}
	}
}


pipeline {
	agent any
	environment{
	    scannerHome = tool 'sonar-scanner';
		DOCKERHUB_CREDENTIALS = credentials('fanunez-dockerhub');
	}
	stages {
        stage('Init') {
            steps {
                echo "Init"
            }
        }
		stage('SonarQube analysis') {
    			steps {
				echo "sonarqube with Frontend"
				dir("/var/lib/jenkins/workspace/dev-frontend"){
				    withSonarQubeEnv('sonarqube') { // Will pick the global server connection you have configured
                        sh "${scannerHome}/bin/sonar-scanner"
                   	}
				}
			}
  		}
		stage('Docker Build'){
            steps{
                dir("/var/lib/jenkins/workspace/dev-frontend"){
                    sh 'docker build -t fanunez/frontend-mingeso .'
                }
            }
        }
        stage('Login'){
            steps{
                sh  'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }
        stage('Docker Hub'){
            steps{
                dir("/var/lib/jenkins/workspace/dev-frontend"){
                    sh 'docker push fanunez/frontend-mingeso'
                }
            }
		}
		stage('End') {
            steps {
                echo "Deploying Backend"
            }
        }
	}
	post {
		always {
			sh 'docker logout'
		}
	}
}