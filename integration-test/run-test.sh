#
# /*
#  * Copyright (c) 2023  Bitshift D.O.O (http://bitshifted.co)
#  *
#  * This Source Code Form is subject to the terms of the Mozilla Public
#  * License, v. 2.0. If a copy of the MPL was not distributed with this
#  * file, You can obtain one at http://mozilla.org/MPL/2.0/.
#  */
#

#! /bin/bash

WIREMOCK_VERSION=2.35.0
WIREMOCK_DOWNLOAD_URL="https://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-jre8-standalone/${WIREMOCK_VERSION}/wiremock-jre8-standalone-${WIREMOCK_VERSION}.jar"
TARGET_DIR=target
WIREMOCK_PORT=9000
FAILURE_EXIT_CODE=20
TEST_JAR_NAME=reflex-integration-test.jar
ROOT_DIR=${PWD}

echo "Building integration tests"
mvn clean install
MAVEN_BUILD_RESULT=$?
if [ $MAVEN_BUILD_RESULT -ne 0 ]; then
  echo "Maven build failed"
  exit $FAILURE_EXIT_CODE
fi

echo "Installing and starting WireMock..."
mkdir -p $TARGET_DIR
cp -rv wiremock target
curl -C - $WIREMOCK_DOWNLOAD_URL --output /tmp/wiremock.jar && cp /tmp/wiremock.jar $TARGET_DIR
cd $ROOT_DIR/$TARGET_DIR
java -jar wiremock.jar --root-dir wiremock --port $WIREMOCK_PORT &
WIREMOCK_SERVER_PID=$!
echo "Wiremock server started with PID: $WIREMOCK_SERVER_PID"

echo "Running integration tests..."
sleep 5
cd $ROOT_DIR/$TARGET_DIR
echo "Working directory: ${PWD}"
java -cp $TEST_JAR_NAME co.bitshifted.reflex.integration.ReflexTester
TEST_RESULT=$?
kill $WIREMOCK_SERVER_PID
if [ $TEST_RESULT -ne 0 ];then
  echo "There are test failures."
  exit $FAILURE_EXIT_CODE
else
  echo "Test run successful"
fi


