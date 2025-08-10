
## Setup

1. install aws cli
2. install sam cli


## Run Locally

Currently rancher and sam-cli have issues mounting the .aws-sam folder into the container living the
/var/task (inside the container) empty.

As a workarround we can use WSL

**build:**
```sh
sam build
```  

**Start function as api**
```sh
sam local start-api
```

**Invoke function with event**
```
```

## Dev environment

Given the rancher and sam-cli in windows, we can leverage WSLg to run intelliJ inside WSL and avoid the issue with Windows

1. Install WSL2 or updated to latest
2. Install ubuntu distro from Windows Store
3. Install aws cli in WSL (Ubuntu): [Install AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html)
4. Install sam cli in WSL (Ubuntu): [Install SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html#install-sam-cli-instructions)
5. Install Google chrome in WSL (Ubuntu): [Install Google chrome](https://learn.microsoft.com/en-us/windows/wsl/tutorials/gui-apps#install-google-chrome-for-linux)
6. Install Intelij in WSL (Ubuntu):  [Install guide](https://youtrack.jetbrains.com/articles/SUPPORT-A-847/How-to-install-and-run-IDEA-on-Windows-using-WSLG)
  a. Install with `sudo snap install intellij-idea-community --classic`  
  b. Open Ubuntu and run with `intellij-idea-community`
7. Install AWS tools in Intellij (plugins view) and now setup is done


`note`: To uninstall intelliJ execute
    sudo snap remove intellij-idea-ultimate  
