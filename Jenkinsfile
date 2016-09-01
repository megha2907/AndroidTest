#!/usr/bin/env groovy
node {
    stage 'Checkout'
    slackSend channel: "#auto-jenkins", color: "good", message: "<${env.BUILD_URL}|#${env.BUILD_NUMBER}> _Build Started:_ *${env.BUILD_TAG}* `${env.BRANCH_NAME}`"
    deleteDir()
    checkout scm

    stage 'Versioning'
    def BRANCH_NAME = env.BRANCH_NAME //PR-552 OR master
    def BUILD_TAG = env.BUILD_TAG     //jenkins-screact_multibranch-PR-552-2 or jenkins-SportsCafe-screact-master-4

    //Read version file
    def readProperty = { propertyName ->
        sh(returnStdout: true, script: """cat version.properties | grep ${propertyName} | cut -d'=' -f2""").trim()
    }
    def writeProperty = { propertyName, propertyValue ->
        sh("""sed -i "s/${propertyName}=[^ ]*/${propertyName}=${propertyValue}/g" version.properties""")
    }
    def vCode = readProperty('VERSION_CODE').toInteger()
    def versionMajorMinor = readProperty('MAJOR_MINOR_VERSION')
    def versionPatch = readProperty('VERSION_PATCH').toInteger()
    def VER = readProperty('VERSION_NAME')

    if(BRANCH_NAME == 'master') {
        vCode += 1;
        versionPatch += 1;
        VER = "v${versionMajorMinor}${versionPatch}"
        writeProperty('VERSION_CODE', vCode)
        writeProperty('MAJOR_MINOR_VERSION', versionMajorMinor)
        writeProperty('VERSION_PATCH', versionPatch)
        writeProperty('VERSION_NAME', VER)
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'scdep-github', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
            sh('''git config --global user.name "scdep"; git config --global user.email "admin+scdep@sportscafe.in"''')
            sh("git add version.properties; git commit -m \"Version ${VER}\"; git tag -afm \"${VER}\" ${VER}")
            sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/sportscafe/scgame.git HEAD:master')
            sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/sportscafe/scgame.git HEAD:master --tags')
        }
    } else {
        VER = BUILD_TAG
        writeProperty('VERSION_NAME', VER)
    }
    def DIR = (sh(returnStdout: true, script: 'pwd')).trim()
    def FILE_PATH = "${DIR}/app/build/outputs/apk"
    def FILE_NAME = "app-production-release.apk"
    def SOURCE_FILE = "${FILE_PATH}/${FILE_NAME}"
    def UPLOAD_PATH = "scgame/${VER}"
    echo "SOURCE_FILE: ${SOURCE_FILE} and UPLOAD_PATH: ${UPLOAD_PATH}"

    stage 'Building'
    env.ANDROID_HOME="/mnt/disk1/data/android/sdk"
    env.JAVA_HOME="/usr/java/default/jre"
    sh './gradlew clean :app:assembleProductionRelease :app:assembleStageRelease :app:assembleDevRelease'

    stage 'Upload to S3'
    build job: 'upload_to_s3', wait: false, parameters: [
            string(name: 'FILE_PATH', value: FILE_PATH),
            string(name: 'FILE_NAME', value: FILE_NAME),
            string(name: 'UPLOAD_PATH', value: UPLOAD_PATH)
    ]
    def S3_PATH = "https://cdn-deploy.spcafe.in/${UPLOAD_PATH}/${FILE_NAME}"
    slackSend channel: "#auto-jenkins", color: "good", message: "<${env.BUILD_URL}|#${env.BUILD_NUMBER}> _S3 Deployment:_ *scgame | <${S3_PATH}| Download ${VER}>*"

    stage 'Upload to Google Play'
    if(BRANCH_NAME == 'master') {
        build job: 'upload_to_playstore', wait: false, parameters: [
                string(name: 'APK_PATH', value: (SOURCE_FILE))
        ]
    } else {
        echo "Not a master branch release hence not uploading to Play Store."
    }
}

