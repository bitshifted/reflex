#! /bin/bash
#
# /*
#  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
#  *
#  * This Source Code Form is subject to the terms of the Mozilla Public
#  * License, v. 2.0. If a copy of the MPL was not distributed with this
#  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
#  */
#



WIREMOCK_VERSION=2.35.0
WIREMOCK_DOWNLOAD_URL="https://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-jre8-standalone/${WIREMOCK_VERSION}/wiremock-jre8-standalone-${WIREMOCK_VERSION}.jar"
TARGET_DIR=target
WIREMOCK_PORT=9000
FAILURE_EXIT_CODE=20
TEST_JAR_NAME=reflex-integration-test.jar
ROOT_DIR=${PWD}
WIREMOCK_DIR=/tmp/wiremock-server

function run_tests_for_profile() {
  cd $ROOT_DIR
  MAVEN_PROFILE=$1
  echo "Building integration tests with profile $MAVEN_PROFILE"
  if [ "$MAVEN_PROFILE" == "default" ];then
    mvn clean install
  else
    mvn clean install -P $MAVEN_PROFILE
  fi

  MAVEN_BUILD_RESULT=$?
  if [ $MAVEN_BUILD_RESULT -ne 0 ]; then
    echo "Maven build failed"
    return $FAILURE_EXIT_CODE
  fi

  echo "Running integration tests..."
  cd $ROOT_DIR/$TARGET_DIR
  echo "Working directory: ${PWD}"
  java -Djdk.internal.httpclient.disableHostnameVerification=true -cp $TEST_JAR_NAME co.bitshifted.reflex.integration.ReflexTester "${@:2}" #PLAIN_TEXT JSON_JACKSON
  TEST_RESULT=$?
  if [ $TEST_RESULT -ne 0 ];then
    echo "There are test failures."
    kill $WIREMOCK_SERVER_PID
    exit $FAILURE_EXIT_CODE
  else
    echo "Test run successful"
  fi
}

function setup_wiremock() {
  echo "Installing and starting WireMock..."
  mkdir -p $WIREMOCK_DIR/wiremock
  rm -rvf $WIREMOCK_DIR/wiremock/* &&  cp -rv ${PWD}/wiremock/* $WIREMOCK_DIR/wiremock
  curl -C -  $WIREMOCK_DOWNLOAD_URL --output $WIREMOCK_DIR/wiremock.jar
  if [ $? -ne 0 ];then
    echo "Failed to download Wiremock"
    exit $FAILURE_EXIT_CODE
  fi
  cd $WIREMOCK_DIR
  java -jar wiremock.jar --root-dir wiremock --https-port $WIREMOCK_PORT &
  WIREMOCK_SERVER_PID=$!
  echo "Wiremock server started with PID: $WIREMOCK_SERVER_PID"
}

setup_wiremock

run_tests_for_profile default PLAIN_TEXT FORM_URLENCODED
run_tests_for_profile jackson-json-serializer  JSON_JACKSON
run_tests_for_profile gson-serializer JSON_GSON
run_tests_for_profile jackson-xml-serializer  XML_JACKSON
run_tests_for_profile default MULTI_CTX
#run_tests_for_profile jaxb-xml-serializer  XML_JAXB

kill $WIREMOCK_SERVER_PID


