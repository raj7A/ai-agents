from crewai import Agent, Crew, Process, Task
from crewai.project import CrewBase, agent, crew, task
from crewai import LLM

@CrewBase
class Developer():
    """Developer crew"""

    agents_config = 'config/agents.yaml'
    tasks_config = 'config/tasks.yaml'
    llm = LLM(
        model = "ollama/llama3.2",
        base_url="http://localhost:11434"
    )

    @agent
    def developer(self) -> Agent:
        return Agent(
            config=self.agents_config['developer'],
            verbose=True,
            llm = self.llm
        )

    @task
    def develop_task(self) -> Task:
        return Task(
            config=self.tasks_config['develop_task'],
            agents=['developer'],
            output_dir='output_dir',
            output_file='output.java'
        )

    @crew
    def crew(self) -> Crew:
        return Crew(
            agents=self.agents,  # Automatically created by the @agent decorator
            tasks=self.tasks,  # Automatically created by the @task decorator
            process=Process.sequential,
            verbose=True,
        )
