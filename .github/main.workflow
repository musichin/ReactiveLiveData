workflow "New workflow" {
  resolves = ["musichin/action-gradle-android"]
  on = "pull_request"
}

action "musichin/action-gradle-android" {
  uses = "musichin/action-gradle-android@master"
  args = "test assembleDebug"
}
