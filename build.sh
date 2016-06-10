#!/bin/bash

HOME=$(cd `dirname $0` && pwd)

source $HOME/../scripts/env.sh
sbt assembly
