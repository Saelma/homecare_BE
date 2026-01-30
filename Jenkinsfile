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

        stage('3. Deploy (배포 및 실행)') {
            steps {
                sh '''
                    # 1. 기존 앱 컨테이너가 있다면 삭제
                    docker stop homecare-app || true
                    docker rm homecare-app || true

                    # 2. 이미지 빌드 (Dockerfile 경로 확인: ./homecare 폴더 안에 있다면 아래처럼)
                    docker build -t homecare-app ./homecare

                    # 3. 컨테이너 실행 (호스트 8081 -> 컨테이너 8080 연결)
                    docker run -d \
                      --name homecare-app \
                      -p 8081:8080 \
                      -e SPRING_DATASOURCE_URL="${DB_URL}" \
                      -e SPRING_DATASOURCE_USERNAME="${DB_USERNAME}" \
                      -e SPRING_DATASOURCE_PASSWORD="${DB_PASSWORD}" \
                      homecare-app
                '''
            }
        }
    }
}