import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Task } from '../../models/task';
import { TaskService } from '../../services/task-service';
import { TaskForm } from '../task-form/task-form';

@Component({
  selector: 'app-task-list',
  imports: [CommonModule, TaskForm],
  template: `
    <div style="max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif;">
      <h2>Minhas Tarefas</h2>
      <app-task-form (taskAdded)="loadTask()"></app-task-form>
      @if (tasks.length === 0) {
        <p>Nenhuma tarefa encontrada. Que tal criar uma?</p>
      }
      <ul style="list-style-type: none; padding: 0;">
        @for (task of tasks; track task.id) {
          <li
            style="padding: 10px; border: 1px solid #ccc; margin-bottom: 5px; border-radius: 5px; display: flex; justify-content: space-between; align-items: center;"
          >
            <div>
              <strong>{{ task.title }}</strong>
              <span [style.color]="task.completed ? 'green' : 'red'">
                ({{ task.completed ? 'Concluida' : 'Pendente' }})
              </span>
              <p style="margin: 5px 0 0 0; font-size: 14px; color: #665;">
                {{ task.description }}
              </p>
            </div>
            <div style="display: flex; gap: 10px;">
              <button
                (click)="updateTask(task)"
                style="padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; background-color: #007bff; color: white;"
              >
                {{ task.completed ? 'Reabrir' : 'Concluir' }}
              </button>
              <button
                (click)="deletarTarefa(task.id)"
                style="padding: 5px 10px; border: none; border-radius: 3px; cursor: pointer; background-color: #dc3545; color: white;"
              >
                Excluir
              </button>
            </div>
          </li>
        }
      </ul>
    </div>
  `,
  styleUrl: './task-list.css',
})
export class TaskList implements OnInit {
  tasks: Task[] = [];

  constructor(
    private taskService: TaskService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadTask();
  }
  loadTask(): void {
    this.taskService.getTasks().subscribe({
      next: (dados) => {
        this.tasks = dados;
        this.cdr.detectChanges();
      },
      error: (erro) => {
        console.log('Error load list of task', erro);
      },
    });
  }

  updateTask(task: Task): void {
    if (task.id === undefined) return;

    const taskUpdated: Task = { ...task, completed: !task.completed };

    this.taskService.updateTask(task.id, taskUpdated).subscribe({
      next: () => {
        this.loadTask();
      },
      error: (erro) => console.log('Erro ao atualizar tarefa', erro),
    });
  }

  deletarTarefa(id: number | undefined): void {
    if (id === undefined) return;

    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
      this.taskService.deleteTask(id).subscribe({
        next: () => {
          this.loadTask();
        },
        error: (erro) => console.log('Erro ao excluir tarefa', erro),
      });
    }
  }
}
