pipeline {
    agent any

    environment {
        // 1. 기존 설정
        AWS_ACCESS_KEY    = credentials('AWS_ACCESS_KEY')
        AWS_SECRET_KEY    = credentials('AWS_SECRET_KEY')
        DOCKER_TOKEN      = credentials('DOCKERHUB_TOKEN')
        DOCKER_USER       = credentials('DOCKERHUB_USERNAME')
        HOST_IP           = credentials('HOST_IP')
        EC2_USER          = credentials('EC2_USERNAME')

        // 2. DB 관련 설정 추가 (Jenkins Credentials에 미리 등록해야 함)
        DB_URL            = credentials('DB_URL')
        DB_USERNAME       = credentials('DB_USERNAME')
        DB_PASSWORD       = credentials('DB_PASSWORD')
    }

    stages {
        stage('1. Checkout (코드 땡겨오기)') {
            steps {
                checkout scm
            }
        }

        stage('2. Build (빌드)') {
            steps {
                dir('homecare') {
                    sh 'chmod +x gradlew'

                    // DB 환경 변수까지 모두 주입하여 빌드 실행
                    sh """
                        AWS_ACCESS_KEY=${env.AWS_ACCESS_KEY} \
                        AWS_SECRET_KEY=${env.AWS_SECRET_KEY} \
                        DB_URL=${env.DB_URL} \
                        DB_USERNAME=${env.DB_USERNAME} \
                        DB_PASSWORD=${env.DB_PASSWORD} \
                        ./gradlew clean build
                    """
                }
            }
        }

        stage('3. Deploy (확인)') {
            steps {
                echo "빌드 성공! 배포 대상 서버 IP: ${env.HOST_IP}"
                echo "빌드된 파일 위치: homecare/build/libs/"
            }
        }
    }
}