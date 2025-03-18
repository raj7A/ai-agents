## ai-agents

Contains the list of multi ai-agents, which are created using the crewai library.

### Install 
``` shell
brew install python@3.12
python3.12 -m venv venv
```

### Activate
``` shell
source venv/bin/activate
```

### Run
Inside the venv :-

1. to create a new agent template:
``` shell
pip install crewai
crewai create crew {project_name}
cd {project_name_folder}
crewai install
crewai run
```
2. to run the existing agent:
``` shell
pip install crewai
cd {project_name_folder_researcher_developer_etc}
crewai install
crewai run
```
### keys
1. https://platform.openai.com/api-keys - to create/get openapi key
2. https://aistudio.google.com/apikeys - to create/get google api key