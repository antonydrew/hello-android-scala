// Slightly complicated build file for use with pfn's excellent
// Android Scala sbt plugin.
//
// Please see here for details:
// https://github.com/pfn/android-sdk-plugin/blob/master/README.md

import android.Keys._

android.Plugin.androidBuild

name := "hello-android-scala"

version := "0.0.1"

scalacOptions in Compile ++= Seq("-feature", "-unchecked", "-deprecation")

platformTarget in Android := "android-19"

libraryDependencies ++= Seq(
  "org.robolectric" % "robolectric" % "2.3" % "test",
  "junit" % "junit" % "4.11" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.1.RC1" % "test",
  "org.scalatest" % "scalatest_2.10" % "2.2.1" % "test"
)

// Make the actually targeted Android jars available to Robolectric for shadowing.
managedClasspath in Test <++= (platformJars in Android, baseDirectory) map {
  (j, b) => Seq(Attributed.blank(b / "bin" / "classes"), Attributed.blank(file(j._1)))
}

// With this option, we cannot have dependencies in the test scope!
debugIncludesTests in Android := false

exportJars in Test := false

// Supress warnings so that Proguard will do its job.
proguardOptions in Android ++= Seq(
  "-dontwarn android.test.**"
)

// Required so Proguard won't remove the actual instrumentation tests.
proguardOptions in Android ++= Seq(
  "-keep public class * extends junit.framework.TestCase",
  "-keepclassmembers class * extends junit.framework.TestCase { *; }"
)

apkbuildExcludes in Android += "LICENSE.txt"
