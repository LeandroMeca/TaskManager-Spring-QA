import { Component, EventEmitter, NgModule, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Task } from '../../models/task';
import { TaskService } from '../../services/task-service';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm {
  @Output() taskAdded = new EventEmitter<void>();

  task: Task = { title: '', description: '', completed: false };

  constructor(private taskService: TaskService) {}

  onSubmit(): void {
    if (!this.task.title.trim()) return;

    this.taskService.createTask(this.task).subscribe({
      next: () => {
        this.taskAdded.emit();
        this.task = { title: '', description: '', completed: false };
      },
      error: (erro) => console.log('Erro in create task ', erro),
    });
  }
}
