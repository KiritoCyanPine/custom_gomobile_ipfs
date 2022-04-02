go-ipfs on Handheld devices
===
This is  step to step guide for creating packages for running ipfs in handheld devices and share files b/w multiple different platforms. Here we will be focusing on creating android and ios application including all the required steps that are absent in the original package, in their platform distribution file types.

This methord is based on the package that is available as an open source here -> https://github.com/ipfs-shipyard/gomobile-ipfs .

This will also guide you through how we can use customized goipfs repository and implement it in our distributions.
Here is the original repo -> https://github.com/ipfs-shipyard/gomobile-ipfs

PS: this method of creation is specifically based on the experience and how we individually were successfully able to create them. By no means there the the only way to complete the work.


## Repositories Required
- [gomobile ipfs from shipyard](https://github.com/ipfs-shipyard/gomobile-ipfs)
- [ipfs go-ipfs](https://github.com/ipfs-shipyard/gomobile-ipfs)

## Install & Dependence
- go 1.17 

## Task list
* [x] documenting steps for Android Build
* [ ] documenting steps for IOS Build

---
---

# Android Build Setup
## <ins>**Generating Required library file for Android. [.aar]**</ins>

## Virtualbox Setup
the following commands are to be run in a linux system. this may also be possible to work on using a virtualbox, just remember you need to have atleast 20gb of free space to get your work done.

This was peviously done in Ubuntu LTS, so the guide is tuned according to that.

## Install & Dependence (for linux)
- go 1.17
- android sdk
- android ndk
- gcc
- make

## Instructions for getting dependencies
- to get **make and gcc** [ all other basic stuff ] in case you dont have them in your linux system run the following commands in terminal.
    ```
    $ sudo apt update && sudo apt upgrade
    ```
    ```
    $ sudo apt install build-essential
    ```

- **setting up android sdk.** This is easy to do in a linux system. after installation add this directory to path in ~/.profile or ~/.bash under the alias of `ANDROID_HOME` . 
    ```
    $ sudo apt install android-sdk
    ```
    This will most probably be written as 
    ```
    export ANDROID_HOME=/usr/lib/android-sdk
    ```
- **setting up android ndk** This is a bit trickey. 
    
    First download the android ndk from this place. -> [android ndk](https://developer.android.com/ndk/downloads) 

    The one we used during the build process was `android-ndk-r23b` . To download the specfic version the option is available [here](https://dl.google.com/android/repository/android-ndk-r23b-linux.zip).

    Once downloaded unzip the content and move it to the same directory were you have you android sdk installed. i.e; in /usr/lib

    And then add it to path under the alias of `ANDROID_NDK_HOME`.
    ```
    export ANDROID_NDK_HOME=/usr/lib/android-ndk-[VERSION]
    ```

- **Installing go** . for our specfic task we need to install go in our system. The latest package wont do as most of the modules we are going to use is not yet competable with anything higher than go 1.17 at the moment.
 Therefore we will install go 1.17.

    The specfic version was `go1.17.8.linux-amd64` the direct download link is [here](https://go.dev/dl/go1.17.8.linux-amd64.tar.gz).

    after unpacking we will move it to our required location by running this command.
    ```
    $ sudo mv go /usr/local 
    ```

    We will do the standard installation of go lang in linux and move the directory under /usr/local. Such that this 'local' directory will have another directory named 'go' with the go tree inside it.

    next we will add it to path directly. no alias needed. which will look something like this in ~/.profile
    ```
    export PATH=$PATH:/usr/local/go/bin
    ```
Now we have completed the environment set up and we can move to the next step.

---
## Generating Shared Library for android
For this purpose we can eighter only use the [gomobile ipfs from shipyard](https://github.com/ipfs-shipyard/gomobile-ipfs)  if you only want to generate the original file needed for our application to build.

**<ins>(or)<ins>**

 we can use it in colloboratoin with our custom goipfs package to also include custom commands and operations.

### <u>For generating original library</u>
move inside the directory named 'package' in the 'gomobile-ipfs' folder. We will find a make file here.

to generate the required build open the terminal in this directory and run the following commands.
```
$ go mod tidy
```
```
$ make build_core.android
```

This will create a directury with the name 'build'. And our aar file will be inside '/android/intermediates/core' which will be named `core.aar`.

<div style="background:#222; padding:20px 20px 20px 20px; margin:10px 10px 10px 10px;">
NOTE : There are occassions where the building process fails sometimes, so we need to do one more thing in case we are hit with an error when this command is run.

mostly it will be something like <pre>go run golang.org/x/mobile/cmd/gomobile bind -v  -target=android -o /home/kritocyanpine/Documents/ipfs/gomobile-ipfs/packages/build/android/intermediates/core/core.aar github.com/ipfs-shipyard/gomobile-ipfs/go/bind/core 
/tmp/go-build2847451994/b001/exe/gomobile: gobind was not found.
Please run gomobile init before trying again
exit status 1
</pre>

IN THAT CASE Go to your home directory.  
You will find a ‘go’ folder, open it.
Head inside ‘bin’ and you will find the required files that went missing.
Copy or move those files to ‘/usr/local/go/bin’ and retry the command. This time it will work.
</div>


### <u>For generating Customized library</u>
For this we need the above repo and the customized repo of go-ipfs .

Now move the go-ipfs folder { ‘github.com/ipfs/go-ipfs’ this one} into the gomobile-ipfs { ‘github.com/ipfs-shipyard/gomobile-ipfs’ this one }

## Directory Hierarchy
```
.
├── android
│   ├── app (dir)
│   ├── bridge (dir)
│   ├── build.gradle
│   ├── gradle
│   │   └── wrapper
│   │       ├── gradle-wrapper.jar
│   │       └── gradle-wrapper.properties
│   ├── gradle.properties
│   ├── gradlew
│   ├── gradlew.bat
│   └── settings.gradle
├── CHANGELOG.md
├── docs
│   ├── android (dir)
│   ├── index.html
│   └── ios (dir)
├── go
│   ├── bind
│   │   └── core (dir)
│   ├── go.mod
│   ├── go.sum
│   └── pkg (dir)
├── go-ipfs ( **CUSTOM PACKAGE**)
├── ios
│   ├── Bridge (dir)
│   ├── Example
│   │   ├── Example (dir)
│   │   ├── Example.xcodeproj (dir)
│   │   └── Tests (dir)
│   └── Example.xcworkspace (dir)
│       
├── LICENSE-APACHE
├── LICENSE-MIT
├── packages
│   ├── build
│   │   └── android
│   │       └── intermediates
│   │           └── core
│   │               ├── core.aar
│   │               └── core-sources.jar
│   ├── deps.go
│   ├── go.mod
│   ├── go.sum
│   ├── Makefile
│   ├── Manifest.yml
│   └── utils (dir)
├── README.md
└── third-party (dir)

```
Here, go to ‘packages’ and you will find a makefile and go.mod file… 
here we will work with the go.mod file to route it to our custom package. For that open the terminal and type in 
```
$ gedit go.mod
```

In the editor in the EOF add the following line or rewrite the current replace notation to the following
```m
replace (
	github.com/ipfs-shipyard/gomobile-ipfs/go => ../go
	github.com/ipfs/go-ipfs => ../go-ipfs
)
```

Save the file and run  
```
$ go mod tidy
```
then build using 

```
$ make build_core.android
```

NOTE : in case of error check the section above for fix.

---
## <ins>**Building the Android Application**</ins>
## Steps to build and run
well we need to open Android Studio and Open the project folder. Create your ADV or connect your android phone or Generate a Signed apk. The usual way that we do with any other application.
## Android ADV
if you are going to set up an ADV follow the following specs.
The generated application dosent run on marshmellow anymore.
```
version: android 24+
name: oreo 
minimum memory: 2 GB
```
All the required changes for the Gradle files have been already made to the project. We are not expecting any problems when building our application.

But still for situations where you need to turn the original project into workable build follow the guide [here](./docs/docs/gomobile-ipfs-fix.pdf).



---
---
---

# IOS build Setup 
will be updated soon

---
## System Details
### Building Platform
- software
  ```
  OS: Windows 10 home 21H2
  go: 1.17.8
  Cygwin: make
  ```
- Virtualbox
  ```
  OS: debian unstable, Ubuntu LTS Ubuntu 20.04.4 LTS
  go: 1.17.8
  ```
  Assigned Specs 
  ```
  Processors: 1
  Ram: 2048 MB
  Memory: 20GB
  ```
- Hardware
  ```
  CPU: Intel i3 9th gen
  ```
### Repo content notes
```diff
- the current build desnot contain any shared-lib's, apk's , dmg's , dll's, or exe's. 
```

