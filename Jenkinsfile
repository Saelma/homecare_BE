pipeline {
    agent any

    // 1. 환경 변수 설정 (Jenkins Credentials에서 안전하게 가져오기)
    environment {
        AWS_ACCESS_KEY    = credentials('AWS_ACCESS_KEY')
        AWS_SECRET_KEY    = credentials('AWS_SECRET_KEY')
        DOCKER_TOKEN      = credentials('DOCKERHUB_TOKEN')
        DOCKER_USER       = credentials('DOCKERHUB_USERNAME')
        HOST_IP           = credentials('HOST_IP')
        EC2_USER          = credentials('EC2_USERNAME')
    }

    stages {
        stage('1. Checkout (코드 땡겨오기)') {
            steps {
                // GitHub에서 최신 코드를 가져오는 단계
                checkout scm
            }
        }

        stage('2. Build (빌드)') {
            steps {
                // 실행 권한 부여
                sh 'chmod +x gradlew'

                // env.를 붙여서 빌드 시점에 환경 변수 주입
                // application.yml의 ${AWS_ACCESS_KEY} 등에 값이 들어갑니다.
                sh "AWS_ACCESS_KEY=${env.AWS_ACCESS_KEY} AWS_SECRET_KEY=${env.AWS_SECRET_KEY} ./gradlew clean build"
            }
        }

        stage('3. Deploy (확인)') {
            steps {
                echo "빌드 성공! 배포 대상 서버 IP: ${env.HOST_IP}"
                echo "빌드된 파일 위치: build/libs/"
            }
        }
    }
}