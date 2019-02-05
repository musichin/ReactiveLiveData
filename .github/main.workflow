workflow "New workflow" {
  on = "check_run"
  resolves = ["musichin/action-gradle-android"]
}

action "musichin/action-gradle-android" {
  uses = "musichin/action-gradle-android"
  args = "assembleDebug"
}
