import { Component, OnInit, signal } from '@angular/core';
import { TaskService } from './services/task-service';
import { TaskList } from './components/task-list/task-list';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [TaskList],
  template: `
    <h1 style="text-align: center; font-family: Arial;">Gerenciador de Tarefas 🚀</h1>
    <app-task-list></app-task-list>
  `,
  styleUrl: './app.css',
})
export class App implements OnInit {
  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    console.log('Iniciando chamada para a API no backend');

    this.taskService.getTasks().subscribe({
      next: (dados) => {
        console.log('✅ SUCESSO! O Spring Boot respondeu:', dados);
      },
      error: (erro) => {
        console.log('❌ ERRO NA COMUNICAÇÃO!');
      },
    });
  }
}
