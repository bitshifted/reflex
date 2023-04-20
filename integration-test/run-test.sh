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

mkdir -p $TARGET_DIR
cp -rv wiremock target
curl  $WIREMOCK_DOWNLOAD_URL --output $TARGET_DIR/wiremock.jar
cd target && java -jar wiremock.jar --root-dir wiremock --port $WIREMOCK_PORT &
WIREMOCK_SERVER_PID=$!
echo "Wiremock server PID: $WIREMOCK_SERVER_PID"

