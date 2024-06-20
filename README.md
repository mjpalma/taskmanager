# Task manager

In this project we are going to realise a crud service to manage the tasks. 

## Tech Stack

**Java 11.0** 

**CSV as persistence** 
## Run Locally

Make sure you have java 11 installed on the machine

Navigate to the path where the jar is located.

Execute creation command
```bash
  java -jar .\taskmanager.jar create Test3 "test3" "2024-09-01 00:00"
```

Execute update command

```bash
  java -jar .\taskmanager.jar update 1 Test3 "test3" "2024-09-01 00:00" false
```


Execute list command

```bash
  java -jar .\taskmanager.jar delete 1
```

Execute delete command

```bash
  java -jar .\taskmanager.jar list
```

You can always see the original file in the same path as the running jar.

## Authors

- [@mjpalma](https://www.github.com/mjpalma)

