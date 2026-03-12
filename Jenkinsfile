pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'https://github.com/cx-sunny-chakraborty/vulnerable-scan-testing'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Checkmarx Scan') {
            steps {
                checkmarxASTScanner(
                    projectName: 'jenkins-demo-project',
                    branchName: 'main',
                    additionalOptions: '--scan-types sast,sca --threshold sast-high=1'
                )
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully'
        }

        failure {
            echo 'Pipeline failed'
        }
    }
}
