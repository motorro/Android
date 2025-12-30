#!/usr/bin/env bash
set -e

Check=1
while getopts ':c' option; do
  case $option in
    c) # No check
      Check=0
  esac
done

rebase() {
  echo Rebasing $1 onto $2...
  git checkout $1
  git rebase $2
}

check() {
  if [ $Check -eq 1 ]; then
      echo Checking...
      ./gradlew testDebugUnitTest --no-daemon --rerun-tasks
  fi
}

assemble() {
  if [ $Check -eq 1 ]; then
      echo Assembling Debug...
      ./gradlew assembleDebug --no-daemon --rerun-tasks
  fi
}

webinars=(
    "webinars/1.HelloWorld"
    "webinars/10.Resources-2"
    "webinars/11.View-2"
    "webinars/12.RecyclerView"
    "webinars/14.State-classic"
    "webinars/15.Fragments"
    "webinars/16.Navigation"
    "webinars/18.Multithreading"
    "webinars/19.Coroutines"
    "webinars/2.Android"
    "webinars/20.Flow"
    "webinars/22.File-Storage"
    "webinars/23.Object-Storage"
    "webinars/24.Network"
    "webinars/25.Repository"
    "webinars/27.Architecture"
    "webinars/28.DI"
    "webinars/3.Android-App-Architecture"
    "webinars/30.Compose-view"
    "webinars/31.Compose-state"
    "webinars/32.StateMachine"
    "webinars/34.Notifications"
    "webinars/35.Background"
    "webinars/37.Release"
    "webinars/4.Activity"
    "webinars/5.Lifecycle"
    "webinars/6.Activity-Navigation"
    "webinars/7.Resources-1"
    "webinars/9.View-1"
)

declare -A p1t=([branch]="practice/1.Activity" [check]="assemble")
declare -a p1=(p1t)

declare -A p2t=([branch]="practice/2.Layout" [check]="assemble")
declare -a p2=(p2t)

declare -A p3t=([branch]="practice/3.Navigation" [check]="assemble")
declare -A p3s=([branch]="practice/3.Navigation-solution" [check]="check")
declare -a p3=(p3t p3s)

declare -A p4t=([branch]="practice/4.Coroutines" [check]="assemble")
declare -a p4=(p4t)

declare -A p5t=([branch]="practice/5.IO" [check]="assemble")
declare -A p5s=([branch]="practice/5.IO-solution" [check]="check")
declare -A p6t=([branch]="practice/6.DI" [check]="assemble")
declare -A p6s=([branch]="practice/6.DI-solution" [check]="check")
declare -A p7t=([branch]="practice/7.Compose" [check]="assemble")
declare -A p7s=([branch]="practice/7.Compose-solution" [check]="check")
declare -A p8t=([branch]="practice/8.Background" [check]="assemble")
declare -A p8s=([branch]="practice/8.Background-solution" [check]="check")
declare -a project=(
    p5t
    p5s
    p6t
    p6s
    p7t
    p7s
    p8t
    p8s
)

declare -a practice=(
  p1
  p2
  p3
  p4
  project
)

git checkout master

echo "===================="
echo "=     Webinars     ="
echo "===================="
for b in ${webinars[@]}; do
    rebase $b "master"
    check
done

echo "===================="
echo "=     Practice     ="
echo "===================="
declare -n prak actions
for prak in "${practice[@]}"; do
    declare base="master"
    for actions in "${prak[@]}"; do
        rebase ${actions[branch]} $base
        base=${actions[branch]}
        ${actions[check]}
    done
done

git checkout master
