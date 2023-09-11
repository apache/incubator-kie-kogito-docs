/*
* This file is describing all the Jenkins jobs in the DSL format (see https://plugins.jenkins.io/job-dsl/)
* needed by the Kogito pipelines.
*
* The main part of Jenkins job generation is defined into the https://github.com/apache/incubator-kie-kogito-pipelines repository.
*
* This file is making use of shared libraries defined in
* https://github.com/apache/incubator-kie-kogito-pipelines/tree/main/dsl/seed/src/main/groovy/org/kie/jenkins/jobdsl.
*/

import org.kie.jenkins.jobdsl.model.JobType
import org.kie.jenkins.jobdsl.utils.JobParamsUtils
import org.kie.jenkins.jobdsl.KogitoJobTemplate
import org.kie.jenkins.jobdsl.KogitoJobUtils
import org.kie.jenkins.jobdsl.Utils

jenkins_path = '.ci/jenkins'

createSetupBranchJob()
setupPostReleaseJob()

KogitoJobUtils.createQuarkusUpdateToolsJob(this, 'kogito-docs', [:], [:], [[
    filepath: 'serverlessworkflow/antora.yml',
    regex: 'quarkus_version: ',
],
[
    filepath: 'serverlessworkflow/antora.yml',
    regex: 'quarkus_platform_version: ',
]])

/////////////////////////////////////////////////////////////////
// Methods
/////////////////////////////////////////////////////////////////

void createSetupBranchJob() {
    def jobParams = JobParamsUtils.getBasicJobParams(this, 'kogito-docs', JobType.SETUP_BRANCH, "${jenkins_path}/Jenkinsfile.setup-branch", 'Kogito Docs Setup Branch job')
    jobParams.env.putAll([
        JENKINS_EMAIL_CREDS_ID: "${JENKINS_EMAIL_CREDS_ID}",

        GIT_AUTHOR: "${GIT_AUTHOR_NAME}",

        AUTHOR_CREDS_ID: "${GIT_AUTHOR_CREDENTIALS_ID}",

        IS_MAIN_BRANCH: "${Utils.isMainBranch(this)}"
    ])
    KogitoJobTemplate.createPipelineJob(this, jobParams)?.with {
        parameters {
            stringParam('DISPLAY_NAME', '', 'Setup a specific build display name')

            stringParam('BUILD_BRANCH_NAME', "${GIT_BRANCH}", 'Set the Git branch to checkout')

            booleanParam('SEND_NOTIFICATION', true, 'In case you want the pipeline to send a notification on CI channel for this run.')
        }
    }
}

void setupPostReleaseJob() {
    def jobParams = JobParamsUtils.getBasicJobParams(this, 'kogito-docs-post-release', JobType.RELEASE, "${jenkins_path}/Jenkinsfile.post-release", 'Kogito Docs Post Release')
    jobParams.env.putAll([
        JENKINS_EMAIL_CREDS_ID: "${JENKINS_EMAIL_CREDS_ID}",

        GIT_AUTHOR: "${GIT_AUTHOR_NAME}",

        AUTHOR_CREDS_ID: "${GIT_AUTHOR_CREDENTIALS_ID}",
    ])
    KogitoJobTemplate.createPipelineJob(this, jobParams)?.with {
        parameters {
            stringParam('DISPLAY_NAME', '', 'Setup a specific build display name')

            stringParam('BUILD_BRANCH_NAME', "${GIT_BRANCH}", 'Set the Git branch to checkout')

            stringParam('KOGITO_VERSION', '', 'Kogito final version.')

            booleanParam('SEND_NOTIFICATION', true, 'In case you want the pipeline to send a notification on CI channel for this run.')
        }
    }
}
