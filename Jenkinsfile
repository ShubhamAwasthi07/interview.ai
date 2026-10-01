pipeline {
    agent any

    environment {
        // App server (EC2). Update if the public IP changes, or attach an Elastic IP.
        APP_HOST = '51.20.74.236'
        APP_USER = 'ubuntu'
        APP_DIR  = '~/interview.ai'
    }

    options {
        disableConcurrentBuilds()
        timeout(time: 20, unit: 'MINUTES')
    }

    stages {
        stage('Build') {
            steps {
                // Fails fast on compile errors before anything is deployed.
                // Tests are skipped because they need a live database.
                sh './mvnw -B -q package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                // Uses the SSH key in the Jenkins home directory (~/.ssh/id_ed25519).
                sh """
                    ssh -o StrictHostKeyChecking=accept-new -o BatchMode=yes ${APP_USER}@${APP_HOST} \
                        'cd ${APP_DIR} && git pull --ff-only && docker compose up -d --build'
                """
            }
        }

        stage('Health check') {
            steps {
                sh "curl -fsS --retry 12 --retry-delay 10 --retry-all-errors http://${APP_HOST}:8080/actuator/health"
            }
        }
    }
}
