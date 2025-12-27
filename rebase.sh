# !/bin/bash
set -e

rebaseCheck() {
  echo Rebasing $1 onto $2...
  git checkout $1
  git rebase $2
  ./gradlew test
}

onMaster=(
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
    "practice/1.Activity"
    "practice/2.Layout"
    "practice/3.Navigation"
    "practice/3.Navigation-solution"
    "practice/4.Coroutines"
    "practice/5.IO"
)

onIo=(
    "practice/5.IO-solution"
    "practice/6.DI"
    "practice/6.DI-solution"
    "practice/7.Compose"
    "practice/7.Compose-solution"
    "practice/8.Background"
    "practice/8.Background-solution"
)

echo "===================="
echo "= Rebase on master ="
echo "===================="
for b in ${onMaster[@]}; do
    rebaseCheck $b "master"
done

echo "=================="
echo "= Rebase project ="
echo "=================="
base="practice/5.IO"
for b in ${onIo[@]}; do
    rebaseCheck $b "$base"
    base=$b
done

