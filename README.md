# Corpus & Process Management (Server)


## Schema, Format
if custom format / schema, explain it. describe spec (url, text, extern readme/man filepath)

```yaml
def :
  format :
    my-custom-format : explaination
    ...
  schema :
    my-custom-format : explaination
    ...
```
    

## Modules

Modules should be stored in the default "modules" directory.
Other modules stored elsewhere should have their root directory folder added in the configuration (modules_dir)

### Module definition

A module definition requires the file <name of the module>.module to be filled as followed:
- name : name of the module (must be unique)
- description [optional] : the description of the module
- input : the input description of the module
- output : the output description of the module
- log : the files containing logs (error, debug, others)
- run : the pipelined commands to be executed to run the module

#### Variables

Some variables are always accessible from the module definition and can't be modified. Some (input & output definition) can be created.

A variable definition/usage must start with '$' followed by any number of letters,numbers,underscore
To set the value of a variable the starting '$' is removed.
To access attribute of a complexe variable (see variables type below) you have to enclose the attribute path within the variable by '${' and '}'.
ex : ${corpus.path} returns the value of the path of the variable corpus.

The variables can have the following types:
- CORPUS :
  corpus are registred corpus within cpm. attributes accessible via the definition are :
  - path : the root path of the corpus of type VAR
  - items : the list of files of the corpus of type LIST[FILE]
  use the id of the corpus to set the value of a CORPUS variable
- VAR :
  raw string value
- FILE :
  file
  attributes are :
    - basedir
    - filename
    - basename (filename without extension)
- LIST[type] :
  a list of variable
  to set a list of file, wildcard expression are allowed
  otherwise use the yaml syntax to define your list
  list are always flatten

Note that every type has a VAR value :
CORPUS as a VAR value equal to its id
FILE as a VAR value equal to its path
LIST[type] the concataination of the VAR value of the items of the list

To access the output variable of a module (used in run definition) you must prefix the variable name by the module name (optionnaly prefixed with a namespace if multiple instance of the same module is used). ex: ${bonsai_parser.OUT}

All variables can specify their format (csv,json,yaml,xml,...) and schema

Summing up :
A variable used in input/output are of the following form:
$VARNAME:
  - type : VARTYPE
  - format (optional) : FORMAT
  - schema (optional)d: SCHEMA
  - val (required for output, optionally define default value for input) : VALUE



#### Logs

By default a logfile is created containing the standard output and error output of the running module.

### Module implementation detail

every path defined in output are relative (even when starting with "/") to the result directory created for the module for a particular run

there is a default docker container containing default libraries (java,perl,python,gcc, etc.) for running modules without a Dockerfile

every module is launched within a scala app that communicate with the cpm server. this scala app allow to maintain a dockerized module in daemon mode for MAP process


## Pipeline/Module run session

What is needed : 
the path to the RESULT directory

What can be done :
start/run
pause
stop
get status
get log path / view log
get result path / view result (for a particular module)
view => concordancier, etc. can be added to a pipeline definition (to know which data result to view), and are run in the default web view

What is saved :
when a pipeline is run, the result directory path is saved along with the run id
state of the run is also stored (which process has been run, etc.)
by default when skipping part of the run definition, the result of previous run are used

### Run session results management



