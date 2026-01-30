pipeline {
    agent any

    environment {
        // Jenkins에 등록한 ID와 변수명을 연결합니다.
        AWS_ACCESS_KEY    = credentials('AWS_ACCESS_KEY')
        AWS_SECRET_KEY    = credentials('AWS_SECRET_KEY')
        DOCKER_TOKEN      = credentials('DOCKERHUB_TOKEN')
        DOCKER_USER       = credentials('DOCKERHUB_USERNAME')
        HOST_IP           = credentials('HOST_IP')
        EC2_USER          = credentials('EC2_USERNAME')
        // SSH 키는 배포 단계에서 sshagent 등을 사용해 부르게 됩니다.
    }

    stages {
        stage('Build & Test') {
            steps {
                // 빌드 시점에 환경 변수 주입
                sh "AWS_ACCESS_KEY=${AWS_ACCESS_KEY} AWS_SECRET_KEY=${AWS_SECRET_KEY} ./gradlew clean build"
            }
        }
        // 이후 Docker build 및 배포 단계에서 나머지 변수들 사용
    }
}